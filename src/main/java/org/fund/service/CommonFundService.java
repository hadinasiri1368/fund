package org.fund.service;

import org.fund.model.Fund;
import org.fund.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class CommonFundService {
    private final JpaRepository repository;

    public CommonFundService(JpaRepository repository) {
        this.repository = repository;
    }

    public boolean isFixFund() {
        return repository.findAll(Fund.class).stream()
                .filter(a -> !a.getIsETF())
                .count() == 1;
    }

    public boolean isEtfFund() {
        return repository.findAll(Fund.class).stream()
                .filter(a -> a.getIsETF())
                .count() == 1;
    }

    public boolean isMarketingFund() {
        return repository.findAll(Fund.class).stream()
                .filter(a -> !a.getIsETF())
                .count() > 1;
    }
}
