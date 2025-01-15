package org.fund.api;


import org.fund.config.dataBase.TenantContext;
import org.fund.config.request.RequestContext;
import org.fund.model.TestNasiri;
import org.fund.repository.JpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
public class TestService {
    @Autowired
    private JpaRepository jpaRepository;

    @Transactional
    public void checkData() throws Exception {
        TestNasiri tt = new TestNasiri();
        System.out.println("==============================================================");
        List<TestNasiri> list = jpaRepository.findAll(TestNasiri.class);
        for (TestNasiri testNasiri : list) {
            System.out.println(TenantContext.getCurrentTenant() + "->cacheService1 =" + testNasiri.getName());
        }
//        tt.setName("new");
//        jpaRepository.save(tt, 10L, "10");
        String sql = "Insert into test ( id,name) Values ( 3,'new')";
        jpaRepository.executeUpdate(sql, RequestContext.getUserId(), RequestContext.getUuid());

        list = jpaRepository.findAll(TestNasiri.class);
        for (TestNasiri testNasiri : list) {
            System.out.println(TenantContext.getCurrentTenant() + "->cacheService2 =" + testNasiri.getName());
        }
        tt=new TestNasiri();
        tt.setName("new");
        jpaRepository.save(tt, 10L, "10");

//        sql = "Insert into test ( id,name) Values ( 4,'new')";
//        jpaRepository.executeUpdate(sql, RequestContext.getUserId(), RequestContext.getUuid());

        list = jpaRepository.findAll(TestNasiri.class);
        for (TestNasiri testNasiri : list) {
            System.out.println(TenantContext.getCurrentTenant() + "->cacheService3 =" + testNasiri.getName());
        }
    }
}
