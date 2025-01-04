package org.fund.api;

import jakarta.servlet.http.HttpServletRequest;
import org.fund.common.FundUtils;
import org.fund.config.cache.CacheService;
import org.fund.config.dataBase.TenantContext;
import org.fund.config.request.RequestContext;
import org.fund.constant.Consts;
import org.fund.model.TestNasiri;
import org.fund.model.TestNasiri2;
import org.fund.repository.JpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(Consts.PREFIX_API_URL)
public class API {
    @Autowired
    private JpaRepository jpaRepository;

    @Autowired
    private CacheService cacheService;


//    @GetMapping("getCustomerCount")
//    long customerCount() throws Exception {
//        String uuid = RequestContext.getUuid();
//        Long userId = RequestContext.getUserId();
//        TestNasiri tt = new TestNasiri();
//        tt.setName("1111");
//        jpaRepository.save(tt, userId, uuid);
//
//        tt = new TestNasiri();
//        tt.setName("1111");
//        jpaRepository.save(tt, userId, uuid);
//        return 1L;
//    }

    @GetMapping("getCustomerCount")
    String customerCount() throws Exception {
        String uuid = RequestContext.getUuid();
        Long userId = RequestContext.getUserId();
//        System.out.println("==============================================================");
//        List<TestNasiri> list = cacheService.findAll(TestNasiri.class);
//        for (TestNasiri testNasiri : list) {
//            System.out.println("cacheService1 =" + testNasiri.getName());
//        }
//        TestNasiri tt = new TestNasiri();
//        tt.setName("test");
//        cacheService.save(tt, userId, uuid);
//
//        list = cacheService.findAll(TestNasiri.class);
//        for (TestNasiri testNasiri : list) {
//            System.out.println("cacheService2 =" + testNasiri.getName());
//        }
//        System.out.println("==============================================================");
//        tt.setName("test2222");
//        cacheService.update(tt, userId, uuid);
//
//        list = cacheService.findAll(TestNasiri.class);
//        for (TestNasiri testNasiri : list) {
//            System.out.println("cacheService3 =" + testNasiri.getName());
//        }
//        System.out.println("==============================================================");
//        cacheService.remove(tt, userId, uuid);
//
//        list = cacheService.findAll(TestNasiri.class);
//        for (TestNasiri testNasiri : list) {
//            System.out.println("cacheService4 =" + testNasiri.getName());
//        }

//        System.out.println("==============================================================");
//        List<TestNasiri> list = cacheService.findAll(TestNasiri.class);
//        for (TestNasiri testNasiri : list) {
//            System.out.println("cacheService1 =" + testNasiri.getName());
//        }
//
//        System.out.println("==============================================================");
//
//        System.out.println("cacheService2 =" + cacheService.findOne(TestNasiri.class,1L).getName());

//        System.out.println("==============================================================");
//        List<TestNasiri> list = cacheService.findAll(TestNasiri.class);
//        for (TestNasiri testNasiri : list) {
//            System.out.println("TestNasiri->cacheService1 =" + testNasiri.getName());
//        }
//
//        System.out.println("==============================================================");
//        List<TestNasiri2> list2 = cacheService.findAll(TestNasiri2.class);
//        for (TestNasiri2 testNasiri : list2) {
//            System.out.println("TestNasiri2->cacheService2 =" + testNasiri.getName());
//        }
//
//        System.out.println("==============================================================");
//        TestNasiri tt =new TestNasiri();
//        tt.setId(2L);
//        tt.setName("2222->3333");
//        cacheService.update(tt,userId,uuid);
//
//        list = cacheService.findAll(TestNasiri.class);
//        for (TestNasiri testNasiri : list) {
//            System.out.println("TestNasiri->cacheService3 =" + testNasiri.getName());
//        }
//
//        System.out.println("==============================================================");
//        TestNasiri2 tt2 =new TestNasiri2();
//        tt2.setId(2L);
//        tt2.setName("nasiri->3333");
//        cacheService.update(tt2,userId,uuid);
//
//        list2 = cacheService.findAll(TestNasiri2.class);
//        for (TestNasiri2 testNasiri : list2) {
//            System.out.println("TestNasiri->cacheService3 =" + testNasiri.getName());
//        }


        System.out.println("==============================================================");
        List<TestNasiri> list = cacheService.findAll(TestNasiri.class);
        for (TestNasiri testNasiri : list) {
            System.out.println(TenantContext.getCurrentTenant() +"->cacheService1 =" + testNasiri.getName());
        }

        return "";
    }
}
