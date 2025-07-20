package org.fund.baseInformation.customer;

import org.fund.accounting.detailLedger.DetailLedgerService;
import org.fund.accounting.detailLedger.constant.DetailLedgerType;
import org.fund.administration.calendar.CalendarService;
import org.fund.administration.params.ParamService;
import org.fund.baseInformation.bankAccount.BankAccountService;
import org.fund.baseInformation.customer.dto.response.CustomerBankAccountResponseDto;
import org.fund.baseInformation.customer.dto.request.CustomerBankAccountRequestDto;
import org.fund.baseInformation.customer.dto.request.CustomerRequestDto;
import org.fund.baseInformation.customer.dto.response.CustomerResponseDto;
import org.fund.common.DateUtils;
import org.fund.common.FundUtils;
import org.fund.constant.Consts;
import org.fund.dto.GenericDtoMapper;
import org.fund.exception.CustomerExceptionType;
import org.fund.exception.FundException;
import org.fund.model.*;
import org.fund.repository.JpaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


record NavDataRec(Long lastAppliedProfitDateNavCount, Long nextWorkingDateAfterLastAppliedProfitDateNavCount) {
}

@Service
public class CustomerService {
    private final JpaRepository repository;
    private final CalendarService calendarService;
    private final ParamService paramService;
    private final DetailLedgerService detailLedgerService;
    private final BankAccountService bankAccountService;
    private final GenericDtoMapper mapper;

    public CustomerService(JpaRepository jpaRepository, CalendarService calendarService, ParamService paramService, DetailLedgerService detailLedgerService, GenericDtoMapper mapper, BankAccountService bankAccountService) {
        this.repository = jpaRepository;
        this.calendarService = calendarService;
        this.paramService = paramService;
        this.detailLedgerService = detailLedgerService;
        this.bankAccountService = bankAccountService;
        this.mapper = mapper;
    }
    @Value("${PageRequest.page}")
    private Integer page;
    @Value("${PageRequest.size}")
    private Integer size;

    public void insert(CustomerRequestDto customer, Fund fund, Long userId, String uuid) throws Exception {
        checkBeforInsert(customer, fund);
        repository.save(mapper.toEntity(Customer.class, customer), userId, uuid);
        customer.setDetailLedgerId(insertDetailLedger(customer, fund, userId, uuid));
    }

    public void batchInsert(List<CustomerRequestDto> customerList, Fund fund, Long userId, String uuid) throws Exception {
        List<Customer> customers = new ArrayList<>();
        for (CustomerRequestDto customer : customerList) {
            checkBeforInsert(customer, fund);
            customer.setDetailLedgerId(insertDetailLedger(customer, fund, userId, uuid));
            customers.add(mapper.toEntity(Customer.class, customer));
        }
        repository.batchInsert(customers, userId, uuid);
    }

    public void update(CustomerRequestDto customer, Fund fund, Long userId, String uuid) throws Exception {
        NavDataRec navDataRec = getNavData(fund);
        checkBeforUpdate(customer, fund, navDataRec);
        repository.update(mapper.toEntity(Customer.class, customer), userId, uuid);
    }

    public void batchUpdate(List<CustomerRequestDto> customerList, Fund fund, Long userId, String uuid) throws Exception {
        NavDataRec navDataRec = getNavData(fund);
        List<Customer> customers = new ArrayList<>();
        for (CustomerRequestDto customerRequestDto : customerList) {
            checkBeforUpdate(customerRequestDto, fund, navDataRec);
            customers.add(mapper.toEntity(Customer.class, customerRequestDto));
        }
        repository.update(customers, userId, uuid);
    }

    public void delete(Long customerId, Long userId, String uuid) throws Exception {
        deleteDetailLedger(customerId, userId, uuid);
        deleteCustomerBankAccountByCustomerId(customerId, userId, uuid);
        repository.removeById(Customer.class, customerId, userId, uuid);
    }

    private void checkBeforUpdate(CustomerRequestDto newCustomer, Fund fund, NavDataRec navDataRec) throws Exception {
        Customer oldCustomer = repository.findOne(Customer.class, newCustomer.getId());
        if (oldCustomer.isSejam()) throw new FundException(CustomerExceptionType.CAN_NOT_EDIT_SEJAM_CUSTOMER);

        if (navDataRec.lastAppliedProfitDateNavCount() == 2L && navDataRec.nextWorkingDateAfterLastAppliedProfitDateNavCount() == 0)
            throw new FundException(CustomerExceptionType.CAN_NOT_EDIT_CUSTOMER_IN_APPLIED_PROFIT);
        validateCustomer(newCustomer, fund);
    }

    private void checkBeforInsert(CustomerRequestDto newCustomer, Fund fund) {
        if (!paramService.getBooleanValue(fund, Consts.PARAMS_BO_CUST_REGISTER_MANUAL))
            throw new FundException(CustomerExceptionType.CAN_NOT_INSERT_CUSTOMER);
        validateCustomer(newCustomer, fund);
    }

    private void validateCustomer(CustomerRequestDto newCustomer, Fund fund) {
        if (!newCustomer.getPerson().getIsCompany()) {
            if (newCustomer.getPerson().getIsIranian()) {
                if (newCustomer.getPerson().getBirthDate().compareTo(DateUtils.getTodayJalali()) > 0) {
                    throw new FundException(CustomerExceptionType.CAN_NOT_INSERT_BIRTHDATE_AFTER_NOW);
                }
            } else {
                if (!paramService.getBooleanValue(fund, Consts.PARAMS_IS_NEW_FOREIGN_CUSTOMER_VALID)) {
                    throw new FundException(CustomerExceptionType.CAN_NOT_INSERT_FOREIGN_CUSTOMER);
                }
            }
        }
        if (newCustomer.getPerson().getCellPhone() != null) {
            if (!FundUtils.isMobileValid(newCustomer.getPerson().getCellPhone())) {
                throw new FundException(CustomerExceptionType.CELLPHONE_IS_INVALID);
            }
        }

        if (newCustomer.getPerson().getEmail() != null) {
            if (!FundUtils.isEmailValid(newCustomer.getPerson().getEmail())) {
                throw new FundException(CustomerExceptionType.EMAIL_IS_INVALID);
            }
        }
    }

    private NavDataRec getNavData(Fund fund) {
        String LastAppliedProfitDate = getLastAppliedProfitDate();
        String nextWorkingDateAfterLastAppliedProfitDate = calendarService.getNextWorkingDate(LastAppliedProfitDate);
        String hql = "SELECT f.calcDate calcDate, COUNT(f) cnt " + "FROM netAssetValue f " + "WHERE f.calcDate IN (:dates) " + "AND   f.fund.id = :fundId " + "GROUP BY f.calcDate";
        Map<String, Object> param = new HashMap();
        param.put("dates", Arrays.asList(LastAppliedProfitDate, nextWorkingDateAfterLastAppliedProfitDate));
        param.put("fundId", fund.getId());
        List<Map<String, Object>> list = repository.listMapByQuery(hql, param);

        Long LastAppliedProfitDateNavCount = list.stream().filter(row -> LastAppliedProfitDate.equals(row.get("calcDate"))).map(row -> (FundUtils.longValue(row.get("cnt")))).findFirst().orElse(0L);

        Long nextWorkingDateAfterLastAppliedProfitDateNavCount = list.stream().filter(row -> LastAppliedProfitDate.equals(row.get("calcDate"))).map(row -> (FundUtils.longValue(row.get("cnt")))).findFirst().orElse(0L);

        return new NavDataRec(LastAppliedProfitDateNavCount, nextWorkingDateAfterLastAppliedProfitDateNavCount);
    }

    public List<CustomerResponseDto> list(Long id) {
        if (FundUtils.isNull(id)) {
        List<Customer> customers = repository.findAll(Customer.class);
            return customers.stream().map(Customer::toResponseDto).toList();
        }
        return List.of(repository.findOne(Customer.class,id).toResponseDto());
    }

    public Page<CustomerResponseDto> listDto(Integer page, Integer size) {
        List<CustomerResponseDto> list = new ArrayList<>();
        if (FundUtils.isNull(page) || page < 0) page = this.page;
        if (FundUtils.isNull(size) || size <= 0) size = this.size;
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customerList = repository.findAllByPage(Customer.class, pageable);
        for (Customer customer : customerList.getContent()) {
            list.add(customer.toResponseDto());
        }
        return new PageImpl<>(list, pageable, customerList.getTotalElements());
    }

    public DetailLedger getDetailLedger(Long id) {
        Customer customer = repository.findOne(Customer.class, id);
        String customerName = customer.getPerson().getIsCompany() ? customer.getPerson().getCompanyName() : customer.getPerson().getFirstName() + " " + customer.getPerson().getLastName();
        if (FundUtils.isNull(customer.getDetailLedger()))
            throw new FundException(CustomerExceptionType.HAS_NOT_DETAILLEDGER, new Object[]{customerName});
        return customer.getDetailLedger();
    }

    public BankAccount getBankAccount(Long id) {
        Customer customer = repository.findOne(Customer.class, id);
        String customerName = customer.getPerson().getIsCompany() ? customer.getPerson().getCompanyName() : customer.getPerson().getFirstName() + " " + customer.getPerson().getLastName();
        if (FundUtils.isNull(customer.getDetailLedger()))
            throw new FundException(CustomerExceptionType.HAS_NOT_BANKACCOUNT, new Object[]{customerName});
        return customer.getCustomerBankAccount().getBankAccount();
    }

    @Transactional
    public void saveCustomerBankAccount(CustomerBankAccountRequestDto customerBankAccount, Long userId, String uuid) throws Exception {
        BankAccount bankAccount = mapper.toEntity(BankAccount.class, customerBankAccount.getBankAccount());
        bankAccountService.insert(bankAccount, userId, uuid);
        Long customerBankAccountCount = getCustomerBankAccountCount(customerBankAccount.getCustomerId());
        boolean isFirst = FundUtils.isNull(customerBankAccount.getId()) && customerBankAccountCount == 0L ? true : false;
        CustomerBankAccount cba = mapper.toEntity(CustomerBankAccount.class, customerBankAccount);
        cba.setBankAccount(bankAccount);
        repository.save(cba, userId, uuid);
        if (isFirst) {
            Customer customer = cba.getCustomer();
            customer.setCustomerBankAccount(cba);
            repository.update(customer, userId, uuid);
        }
    }

    public void updateCustomerBankAccount(CustomerBankAccountResponseDto customerBankAccount, Long userId, String uuid) throws Exception {
        repository.save(mapper.toEntity(CustomerBankAccount.class, customerBankAccount), userId, uuid);
    }

    public void deleteCustomerBankAccount(Long customerBankAccountId, Long userId, String uuid) throws Exception {
        Customer customer = repository.findAll(Customer.class).stream().filter(row -> !FundUtils.isNull(row.getCustomerBankAccount()) && row.getCustomerBankAccount().getId().equals(customerBankAccountId)).findFirst().orElse(null);
        if (!FundUtils.isNull(customer)) {
            customer.setCustomerBankAccount(null);
            repository.update(customer, userId, uuid);
        }
        repository.removeById(CustomerBankAccount.class, customerBankAccountId, userId, uuid);
    }

    public void setCustomerDefaultBankAccount(Long customerId, Long customerBankAccountId, Long userId, String uuid) throws Exception {
        Customer customer = repository.findOne(Customer.class, customerId);
        customer.setCustomerBankAccount(repository.findOne(CustomerBankAccount.class, customerBankAccountId));
        repository.update(customer, userId, uuid);
    }

    private Long getCustomerBankAccountCount(Long customerId) {
        return repository.findAll(CustomerBankAccount.class).stream().filter(a -> a.getCustomer().getId().equals(customerId)).count();
    }

    private Long insertDetailLedger(CustomerRequestDto newCustomer, Fund fund, Long userId, String uuid) throws Exception {
        String name = newCustomer.getPerson().getIsCompany() ? newCustomer.getPerson().getCompanyName() : newCustomer.getPerson().getLastName() + " " + newCustomer.getPerson().getFirstName();
        DetailLedger detailLedger = detailLedgerService.get(name, fund, DetailLedgerType.CUSTOMER);
        detailLedgerService.insert(detailLedger, userId, uuid);
        return detailLedger.getId();
    }


    private void deleteDetailLedger(Long customerId, Long userId, String uuid) throws Exception {
        detailLedgerService.deleteByCustomerId(customerId, userId, uuid);
    }

    private void deleteCustomerBankAccountByCustomerId(Long customerId, Long userId, String uuid) throws Exception {
        List<CustomerBankAccount> customerBankAccounts = repository.findBy(CustomerBankAccount.class, "customer.id", customerId);
        if (!FundUtils.isNull(customerBankAccounts)) {
            repository.batchRemove(customerBankAccounts, userId, uuid);
        }
    }

    private String getLastAppliedProfitDate() {
        throw new RuntimeException("AppliedProfit has not been launched yet");
    }

    public CustomerBankAccountResponseDto getCustomerBankAccount(Long customerId) {
        List<CustomerBankAccount> customerBankAccounts = repository.findBy(CustomerBankAccount.class, "customer.id", customerId);
        if (FundUtils.isNull(customerBankAccounts)) {
            throw new FundException(CustomerExceptionType.HAS_NOT_BANKACCOUNT, new Object[]{customerId});
        }
        CustomerBankAccountResponseDto customerBankAccountDto = new CustomerBankAccountResponseDto();
        customerBankAccountDto.setCustomerId(customerId);
        customerBankAccountDto.setBankAccountIds(customerBankAccounts.stream().map(a -> a.getBankAccount().getId()).toList());
        return customerBankAccountDto;
    }
}
