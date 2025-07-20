package org.fund.baseInformation.customer;

import org.fund.administration.fund.FundService;
import org.fund.baseInformation.customer.dto.request.CustomerBankAccountRequestDto;
import org.fund.baseInformation.customer.dto.response.CustomerBankAccountResponseDto;
import org.fund.baseInformation.customer.dto.request.CustomerRequestDto;
import org.fund.baseInformation.customer.dto.response.CustomerResponseDto;
import org.fund.config.request.RequestContext;
import org.fund.constant.Consts;
import org.fund.model.Customer;
import org.fund.model.CustomerBankAccount;
import org.fund.validator.NotEmpty;
import org.fund.validator.ValidateField;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping(Consts.DEFAULT_PREFIX_API_URL)
public class CustomerController {
    private final CustomerService service;
    private final FundService fundService;

    public CustomerController(CustomerService service, FundService fundService) {
        this.service = service;
        this.fundService = fundService;
    }

    @PostMapping(path = Consts.DEFAULT_VERSION_API_URL + "/baseInformation/customer/add")
    public void insert(@RequestBody CustomerRequestDto customerRequestDto) throws Exception {
        service.insert(customerRequestDto, fundService.getDefaultFund(), RequestContext.getUserId(), RequestContext.getUuid());
    }

    @PostMapping(path = Consts.DEFAULT_VERSION_API_URL + "/baseInformation/customer/batch/add")
    public void insert(@RequestBody List<CustomerRequestDto> customerRequestDtoList) throws Exception {
        service.batchInsert(customerRequestDtoList, fundService.getDefaultFund(), RequestContext.getUserId(), RequestContext.getUuid());
    }

    @PutMapping(path = Consts.DEFAULT_VERSION_API_URL + "/baseInformation/customer/edit")
    public void edit(@RequestBody CustomerRequestDto customerRequestDto) throws Exception {
        service.update(customerRequestDto, fundService.getDefaultFund(), RequestContext.getUserId(), RequestContext.getUuid());
    }

    @PutMapping(path = Consts.DEFAULT_VERSION_API_URL + "/baseInformation/customer/batch/edit")
    public void edit(@RequestBody List<CustomerRequestDto> customerRequestDtoList) throws Exception {
        service.batchUpdate(customerRequestDtoList, fundService.getDefaultFund(), RequestContext.getUserId(), RequestContext.getUuid());
    }

    @DeleteMapping(path = Consts.DEFAULT_VERSION_API_URL + "/baseInformation/customer/remove/{id}")
    public void remove(@PathVariable("id") @NotEmpty(fieldName = "id")
                       @ValidateField(fieldName = "id", entityClass = Customer.class) Long customerId) throws Exception {
        service.delete(customerId, RequestContext.getUserId(), RequestContext.getUuid());
    }

    @GetMapping(path = Consts.DEFAULT_VERSION_API_URL + "/baseInformation/customer/{id}")
    public CustomerResponseDto getCustomerList(@PathVariable("id") @ValidateField(fieldName = "id", entityClass = Customer.class) Long customerId) {
        return service.list(customerId).getFirst();
    }

    @PostMapping(path = Consts.DEFAULT_VERSION_API_URL + "/baseInformation/customer/bankAccount/add")
    public void insert(@RequestBody CustomerBankAccountRequestDto customerBankAccount) throws Exception {
        service.saveCustomerBankAccount(customerBankAccount, RequestContext.getUserId(), RequestContext.getUuid());
    }

    @PutMapping(path = Consts.DEFAULT_VERSION_API_URL + "/baseInformation/customer/bankAccount/edit")
    public void edit(@RequestBody CustomerBankAccountResponseDto customerBankAccountDto) throws Exception {
        service.updateCustomerBankAccount(customerBankAccountDto, RequestContext.getUserId(), RequestContext.getUuid());
    }

    @DeleteMapping(path = Consts.DEFAULT_VERSION_API_URL + "/baseInformation/customer/bankAccount/remove/{id}")
    public void removeCustomerBankAccount(@PathVariable("id") @NotEmpty(fieldName = "id")
                                          @ValidateField(fieldName = "id", entityClass = CustomerBankAccount.class) Long customerBankAccountId) throws Exception {
        service.deleteCustomerBankAccount(customerBankAccountId, RequestContext.getUserId(), RequestContext.getUuid());
    }

    @PutMapping(path = Consts.DEFAULT_VERSION_API_URL + "/baseInformation/customer/{customerId}/bankAccount/default/{accountId}")
    public void setCustomerDefaultBankAccount(@PathVariable("customerId") @NotEmpty(fieldName = "customerId")
                                              @ValidateField(fieldName = "customerId", entityClass = Customer.class) Long customerId,
                                              @PathVariable("accountId") @NotEmpty(fieldName = "accountId")
                                              @ValidateField(fieldName = "accountId", entityClass = CustomerBankAccount.class) Long accountId) throws Exception {
        service.setCustomerDefaultBankAccount(customerId, accountId, RequestContext.getUserId(), RequestContext.getUuid());
    }

    @GetMapping(path = Consts.DEFAULT_VERSION_API_URL + "/baseInformation/customer")
    public Page<CustomerResponseDto> getCustomerList(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size) {
        return service.listDto(page, size);
    }

    @GetMapping(path = Consts.DEFAULT_VERSION_API_URL + "/baseInformation/customer/bankAccount/{customerId}")
    public CustomerBankAccountResponseDto getCustomerBankAccountList(@PathVariable("customerId") @ValidateField(fieldName = "id", entityClass = Customer.class) Long customerId) {
        return service.getCustomerBankAccount(customerId);
    }
}
