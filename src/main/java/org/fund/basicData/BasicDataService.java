package org.fund.basicData;

import org.fund.model.Fund;
import org.fund.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class BasicDataService {
    private final JpaRepository repository;
    public BasicDataService(JpaRepository repository) {
        this.repository = repository;
    }

    public void insertFund(Fund fund,Long userId,String uuid) throws Exception {
        repository.save(fund,userId,uuid);
    }

//    public void updateFund(Fund fund,Long userId,String uuid) throws Exception {
//        repository.update(fund,userId,uuid);
//    }
//
//    public void updateFund(Fund fund,Long userId,String uuid) throws Exception {
//        repository.update(fund,userId,uuid);
//    }
}
