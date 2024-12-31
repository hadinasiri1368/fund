package org.fund.api;

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
        TestNasiri tt = new TestNasiri();
        tt.setId(10L);
        tt.setName("1111");
        jpaRepository.save(tt, 123456L);

        tt.setName("222");
        jpaRepository.update(tt, 123456L);

//        jpaRepository.removeById(TestNasiri.class, tt.getId(), 123456L);

        String sql = "delete test t where t.id = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", tt.getId());
        jpaRepository.executeUpdate(sql, params, 123456L);

        return 1L;
//        String sql = "select count(*) from detailLedger";
//        return jpaRepository.getLongValue(sql);
    }
}
