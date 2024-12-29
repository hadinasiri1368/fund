package org.fund.api;

import org.fund.constant.Consts;
import org.fund.repository.JpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.persistence.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Consts.PREFIX_API_URL)
public class API {
    @Autowired
    private JpaRepository jpaRepository;

    @GetMapping("getCustomerCount")
    long customerCount() {
        String sql = "select count(*) from detailLedger";
        return jpaRepository.getLongValue(sql);
    }
}
