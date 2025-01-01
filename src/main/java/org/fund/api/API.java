package org.fund.api;

import jakarta.servlet.http.HttpServletRequest;
import org.fund.common.FundUtils;
import org.fund.config.request.RequestContext;
import org.fund.constant.Consts;
import org.fund.model.TestNasiri;
import org.fund.repository.JpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(Consts.PREFIX_API_URL)
public class API {
    @Autowired
    private JpaRepository jpaRepository;

//    @Autowired
//    private Test test;

    @GetMapping("getCustomerCount")
    long customerCount() throws Exception {
        String uuid = RequestContext.getUuid();
        Long userId = RequestContext.getUserId();
        TestNasiri tt = new TestNasiri();
        tt.setName("1111");
        jpaRepository.save(tt, userId, uuid);

        tt = new TestNasiri();
        tt.setName("1111");
        jpaRepository.save(tt, userId, uuid);
        return 1L;
    }
}
