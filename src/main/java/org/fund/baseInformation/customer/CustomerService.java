package org.fund.baseInformation.customer;

import lombok.Getter;
import lombok.Setter;
import org.fund.accounting.detailLedger.DetailLedgerService;
import org.fund.administration.calendar.CalendarService;
import org.fund.administration.params.ParamDto;
import org.fund.administration.params.ParamService;
import org.fund.authentication.permission.PermissionService;
import org.fund.baseInformation.customer.dto.CustomerBankAccountDto;
import org.fund.baseInformation.customer.dto.CustomerDto;
import org.fund.common.DateUtils;
import org.fund.common.FundUtils;
import org.fund.constant.Consts;
import org.fund.exception.CustomerExceptionType;
import org.fund.exception.FundException;
import org.fund.model.*;
import org.fund.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.*;


record NavDataRec(Long lastAppliedProfitDateNavCount, Long nextWorkingDateAfterLastAppliedProfitDateNavCount) {
}

@Service
public class CustomerService {
    private final JpaRepository repository;
    private final CalendarService calendarService;
    private final ParamService paramService;
    private final DetailLedgerService detailLedgerService;

    public CustomerService(JpaRepository jpaRepository
            , CalendarService calendarService
            , ParamService paramService
            , DetailLedgerService detailLedgerService
    ) {
        this.repository = jpaRepository;
        this.calendarService = calendarService;
        this.paramService = paramService;
        this.detailLedgerService = detailLedgerService;
    }

    public void insert(CustomerDto customer, Fund fund, Long userId, String uuid) throws Exception {
        checkBeforInsert(customer, fund);
        repository.save(customer.toCustomer(), userId, uuid);
        customer.setDetailLedgerId(insertDetailLedger(customer, fund, userId, uuid));
    }

    public void batchInsert(List<CustomerDto> customerList, Fund fund, Long userId, String uuid) throws Exception {
        List<Customer> customers = new ArrayList<>();
        for (CustomerDto customer : customerList) {
            checkBeforInsert(customer, fund);
            customer.setDetailLedgerId(insertDetailLedger(customer, fund, userId, uuid));
            customers.add(customer.toCustomer());
        }
        repository.batchInsert(customers, userId, uuid);
    }

    public void update(CustomerDto customer, Fund fund, Long userId, String uuid) throws Exception {
        NavDataRec navDataRec = getNavData(fund);
        checkBeforUpdate(customer, fund, navDataRec);
        repository.update(customer.toCustomer(), userId, uuid);
    }

    public void batchUpdate(List<CustomerDto> customerList, Fund fund, Long userId, String uuid) throws Exception {
        NavDataRec navDataRec = getNavData(fund);
        List<Customer> customers = new ArrayList<>();
        for (CustomerDto customerDto : customerList) {
            checkBeforUpdate(customerDto, fund, navDataRec);
            customers.add(customerDto.toCustomer());
        }
        repository.update(customers, userId, uuid);
    }

    public void delete(Long customerId, Long userId, String uuid) throws Exception {
        deleteDetailLedger(customerId, userId, uuid);
        deleteCustomerBankAccountByCustomerId(customerId, userId, uuid);
        repository.removeById(Customer.class, customerId, userId, uuid);
    }

    private void checkBeforUpdate(CustomerDto newCustomer, Fund fund, NavDataRec navDataRec) throws Exception {
        Customer oldCustomer = repository.findOne(Customer.class, newCustomer.getId());
        if (oldCustomer.isSejam())
            throw new FundException(CustomerExceptionType.CAN_NOT_EDIT_SEJAM_CUSTOMER);

        if (navDataRec.lastAppliedProfitDateNavCount() == 2L && navDataRec.nextWorkingDateAfterLastAppliedProfitDateNavCount() == 0)
            throw new FundException(CustomerExceptionType.CAN_NOT_EDIT_CUSTOMER_IN_APPLIED_PROFIT);
        validateCustomer(newCustomer, fund);
    }

    private void checkBeforInsert(CustomerDto newCustomer, Fund fund) {
        if (!paramService.getBooleanValue(fund, Consts.PARAMS_BO_CUST_REGISTER_MANUAL))
            throw new FundException(CustomerExceptionType.CAN_NOT_INSERT_CUSTOMER);
        validateCustomer(newCustomer, fund);
    }

    private void validateCustomer(CustomerDto newCustomer, Fund fund) {
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
        String hql = "SELECT f.calcDate calcDate, COUNT(f) cnt " +
                "FROM netAssetValue f " +
                "WHERE f.calcDate IN (:dates) " +
                "AND   f.fund.id = :fundId " +
                "GROUP BY f.calcDate";
        Map<String, Object> param = new HashMap();
        param.put("dates", Arrays.asList(LastAppliedProfitDate, nextWorkingDateAfterLastAppliedProfitDate));
        param.put("fundId", fund.getId());
        List<Map<String, Object>> list = repository.listMapByQuery(hql, param);

        Long LastAppliedProfitDateNavCount = list.stream()
                .filter(row -> LastAppliedProfitDate.equals(row.get("calcDate")))
                .map(row -> (FundUtils.longValue(row.get("cnt"))))
                .findFirst().orElse(0L);

        Long nextWorkingDateAfterLastAppliedProfitDateNavCount = list.stream()
                .filter(row -> LastAppliedProfitDate.equals(row.get("calcDate")))
                .map(row -> (FundUtils.longValue(row.get("cnt"))))
                .findFirst().orElse(0L);

        return new NavDataRec(LastAppliedProfitDateNavCount, nextWorkingDateAfterLastAppliedProfitDateNavCount);
    }

    public List<Customer> list(Long id) {
        if (FundUtils.isNull(id))
            return repository.findAll(Customer.class);
        return repository.findAll(Customer.class).stream()
                .filter(a -> a.getId().equals(id)).toList();
    }

    public DetailLedger getDetailLedger(Long id) {
        Customer customer = repository.findOne(Customer.class, id);
        String customerName = customer.getPerson().getIsCompany() ? customer.getPerson().getCompanyName() :
                customer.getPerson().getFirstName() + " " + customer.getPerson().getLastName();
        if (FundUtils.isNull(customer.getDetailLedger()))
            throw new FundException(CustomerExceptionType.HAS_NOT_DETAILLEDGER, new Object[]{customerName});
        return customer.getDetailLedger();
    }

    public BankAccount getBankAccount(Long id) {
        Customer customer = repository.findOne(Customer.class, id);
        String customerName = customer.getPerson().getIsCompany() ? customer.getPerson().getCompanyName() :
                customer.getPerson().getFirstName() + " " + customer.getPerson().getLastName();
        if (FundUtils.isNull(customer.getDetailLedger()))
            throw new FundException(CustomerExceptionType.HAS_NOT_BANKACCOUNT, new Object[]{customerName});
        return customer.getCustomerBankAccount().getBankAccount();
    }

    public void saveCustomerBankAccount(CustomerBankAccountDto customerBankAccount, Long userId, String uuid) throws Exception {
        Long customerBankAccountCount = getCustomerBankAccountCount(customerBankAccount.getCustomerId());
        boolean isFirst = FundUtils.isNull(customerBankAccount.getId()) && customerBankAccountCount == 0L ? true : false;
        CustomerBankAccount cba = customerBankAccount.toCustomerBankAccount();
        repository.save(cba, userId, uuid);
        if (isFirst) {
            Customer customer = cba.getCustomer();
            customer.setCustomerBankAccount(cba);
            repository.update(customer, userId, uuid);
        }
    }

    public void updateCustomerBankAccount(CustomerBankAccountDto customerBankAccount, Long userId, String uuid) throws Exception {
        repository.save(customerBankAccount.toCustomerBankAccount(), userId, uuid);
    }

    public void deleteCustomerBankAccount(Long customerBankAccountId, Long userId, String uuid) throws Exception {
        Customer customer = repository.findAll(Customer.class).stream()
                .filter(row -> !FundUtils.isNull(row.getCustomerBankAccount()) &&
                        row.getCustomerBankAccount().getId().equals(customerBankAccountId))
                .findFirst().orElse(null);
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
        return repository.findAll(CustomerBankAccount.class).stream()
                .filter(a -> a.getCustomer().getId().equals(customerId))
                .count();
    }

    private Long insertDetailLedger(CustomerDto newCustomer, Fund fund, Long userId, String uuid) throws Exception {
        DetailLedger detailLedger = detailLedgerService.get(newCustomer.toCustomer(), fund);
        detailLedgerService.insert(detailLedger, userId, uuid);
        return detailLedger.getId();
    }


    private void deleteDetailLedger(Long customerId, Long userId, String uuid) throws Exception {
        detailLedgerService.deleteByCustomerId(customerId, userId, uuid);
    }

    private void deleteCustomerBankAccountByCustomerId(Long customerId, Long userId, String uuid) throws Exception {
        List<CustomerBankAccount> customerBankAccounts = repository.findAll(CustomerBankAccount.class).stream()
                .filter(a -> a.getCustomer().getId().equals(customerId)).toList();
        repository.batchRemove(customerBankAccounts, userId, uuid);
    }

    private String getLastAppliedProfitDate() {
        throw new RuntimeException("AppliedProfit has not been launched yet");
    }

}
