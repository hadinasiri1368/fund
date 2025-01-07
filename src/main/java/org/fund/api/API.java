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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(Consts.PREFIX_API_URL)
public class API {
    @Autowired
    private JpaRepository jpaRepository;

//    @Autowired
//    private CacheService cacheService;


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


//        System.out.println("==============================================================");
//        List<TestNasiri> list = jpaRepository.findAll(TestNasiri.class);
//        for (TestNasiri testNasiri : list) {
//            System.out.println(TenantContext.getCurrentTenant() + "->cacheService1 =" + testNasiri.getName());
//        }
//        System.out.println("==============================================================");
//        System.out.println(TenantContext.getCurrentTenant() + "->cacheService2 =" + jpaRepository.findOne(TestNasiri.class, 2L).getName());
//
//        TestNasiri nasiri =new TestNasiri();
//        nasiri.setName("new");
//
//        jpaRepository.save(nasiri, RequestContext.getUserId(),RequestContext.getUuid());
//
//        System.out.println("==============================================================");
//        list = jpaRepository.findAll(TestNasiri.class);
//        for (TestNasiri testNasiri : list) {
//            System.out.println(TenantContext.getCurrentTenant() + "->cacheService3 =" + testNasiri.getName());
//        }
//
//        nasiri.setName("new--2222");
//        jpaRepository.update(nasiri, RequestContext.getUserId(),RequestContext.getUuid());
//        System.out.println("==============================================================");
//        list = jpaRepository.findAll(TestNasiri.class);
//        for (TestNasiri testNasiri : list) {
//            System.out.println(TenantContext.getCurrentTenant() + "->cacheService4 =" + testNasiri.getName());
//        }
//
//        jpaRepository.removeById(TestNasiri.class, nasiri.getId(), RequestContext.getUserId(), RequestContext.getUuid());
//        System.out.println("==============================================================");
//        list = jpaRepository.findAll(TestNasiri.class);
//        for (TestNasiri testNasiri : list) {
//            System.out.println(TenantContext.getCurrentTenant() + "->cacheService5 =" + testNasiri.getName());
//        }

//        System.out.println("==============================================================");
//        List<TestNasiri> list = jpaRepository.findAll(TestNasiri.class);
//        for (TestNasiri testNasiri : list) {
//            System.out.println(TenantContext.getCurrentTenant() + "->TestNasiri =" + testNasiri.getName());
//        }
//
//        System.out.println("==============================================================");
//        List<TestNasiri2> list1 = jpaRepository.findAll(TestNasiri2.class);
//        for (TestNasiri2 testNasiri : list1) {
//            System.out.println(TenantContext.getCurrentTenant() + "->TestNasiri2 =" + testNasiri.getName());
//        }
//
//        TestNasiri nasiri = new TestNasiri();
//        nasiri.setId(2L);
//        nasiri.setName("2222");
//
//        jpaRepository.update(nasiri,RequestContext.getUserId(),RequestContext.getUuid());
//
//        System.out.println("==============================================================");
//        list = jpaRepository.findAll(TestNasiri.class);
//        for (TestNasiri testNasiri : list) {
//            System.out.println(TenantContext.getCurrentTenant() + "->TestNasiri =" + testNasiri.getName());
//        }
//
//        System.out.println("==============================================================");
//        list1 = jpaRepository.findAll(TestNasiri2.class);
//        for (TestNasiri2 testNasiri : list1) {
//            System.out.println(TenantContext.getCurrentTenant() + "->TestNasiri2 =" + testNasiri.getName());
//        }


        System.out.println("==============================================================");
        List<TestNasiri> list = jpaRepository.findAll(TestNasiri.class);
        for (TestNasiri testNasiri : list) {
            System.out.println(TenantContext.getCurrentTenant() + "->cacheService1 =" + testNasiri.getName());
        }
//        System.out.println("==============================================================");
//        System.out.println(TenantContext.getCurrentTenant() + "->cacheService2 =" + jpaRepository.findOne(TestNasiri.class, 2L).getName());

//        String sql = "update test set name='2222' where id=2";
//        jpaRepository.executeUpdate(sql, RequestContext.getUserId(), RequestContext.getUuid());

//        String sql = "update test set name=:name where id=:id";
//        Map<String, Object> params = new HashMap<>();
//        params.put("name", "2222");
//        params.put("id", 2L);
//        jpaRepository.executeUpdate(sql, params, RequestContext.getUserId(), RequestContext.getUuid());

//        TestNasiri nasiri  =new TestNasiri();
//        nasiri.setId(2L);
//        nasiri.setName("2222");
//        jpaRepository.update(nasiri, RequestContext.getUserId(), RequestContext.getUuid());

        TestNasiri rr = new TestNasiri();
        rr.setName("2222");
        List<TestNasiri> list2 = new ArrayList<TestNasiri>();
        list2.add(rr);
        try {
            jpaRepository.batchInsert(list2, RequestContext.getUserId(), RequestContext.getUuid());
        }catch (Exception e) {
            e.printStackTrace();
        }

//        TestNasiri rr = jpaRepository.findOne(TestNasiri.class, 2L);
//        rr.setName("333-444");
//        List<TestNasiri> list2 = new ArrayList<TestNasiri>();
//        list2.add(rr);
//        jpaRepository.batchUpdate(list2, RequestContext.getUserId(), RequestContext.getUuid());

//        TestNasiri rr = jpaRepository.findOne(TestNasiri.class, 2L);
//        List<TestNasiri> list2 = new ArrayList<TestNasiri>();
//        list2.add(rr);
//        jpaRepository.batchRemove(list2, RequestContext.getUserId(), RequestContext.getUuid());

        System.out.println("==============================================================");
        list = jpaRepository.findAll(TestNasiri.class);
        for (TestNasiri testNasiri : list) {
            System.out.println(TenantContext.getCurrentTenant() + "->cacheService1 =" + testNasiri.getName());
        }

        return "";
    }
}
