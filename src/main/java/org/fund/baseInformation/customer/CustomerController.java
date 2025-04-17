package org.fund.baseInformation.customer;

import org.fund.administration.fund.FundDto;
import org.fund.administration.fund.FundService;
import org.fund.baseInformation.customer.dto.CustomerDto;
import org.fund.config.request.RequestContext;
import org.fund.constant.Consts;
import org.fund.model.Customer;
import org.fund.model.Fund;
import org.fund.validator.NotEmpty;
import org.fund.validator.ValidateField;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
public class CustomerController {
    private final CustomerService service;
    private final FundService fundService;

    public CustomerController(CustomerService service, FundService fundService) {
        this.service = service;
        this.fundService = fundService;
    }

    @PostMapping(path = Consts.DEFAULT_VERSION_API_URL + "/baseInformation/customer/add")
    public void insert(@RequestBody CustomerDto customerDto) throws Exception {
        service.insert(customerDto, fundService.getDefaultFund(), RequestContext.getUserId(), RequestContext.getUuid());
    }

    @PostMapping(path = Consts.DEFAULT_VERSION_API_URL + "/baseInformation/customer/batch/add")
    public void insert(@RequestBody List<CustomerDto> customerDtoList) throws Exception {
        service.batchInsert(customerDtoList, fundService.getDefaultFund(), RequestContext.getUserId(), RequestContext.getUuid());
    }

    @PutMapping(path = Consts.DEFAULT_VERSION_API_URL + "/baseInformation/customer/edit")
    public void edit(@RequestBody CustomerDto customerDto) throws Exception {
        service.update(customerDto, fundService.getDefaultFund(), RequestContext.getUserId(), RequestContext.getUuid());
    }

    @PutMapping(path = Consts.DEFAULT_VERSION_API_URL + "/baseInformation/customer/batch/edit")
    public void edit(@RequestBody List<CustomerDto> customerDtoList) throws Exception {
        service.batchUpdate(customerDtoList, fundService.getDefaultFund(), RequestContext.getUserId(), RequestContext.getUuid());
    }

    @DeleteMapping(path = Consts.DEFAULT_VERSION_API_URL + "/administration/customer/remove/{id}")
    public void remove(@PathVariable @NotEmpty(fieldName = "id")
                       @ValidateField(fieldName = "id", entityClass = Customer.class) Long customerId) throws Exception {
        service.delete(customerId, RequestContext.getUserId(), RequestContext.getUuid());
    }
}
