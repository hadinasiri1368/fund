package org.fund.administration.branch;

import org.fund.common.FundUtils;
import org.fund.model.Fund;
import org.fund.model.FundBranch;
import org.fund.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FundBranchService {
    private final JpaRepository repository;

    public FundBranchService(JpaRepository repository) {
        this.repository = repository;
    }

    public void insert(FundBranch fundBranch, Long userId, String uuid) throws Exception {
        repository.save(fundBranch, userId, uuid);
    }

    public void delete(Long fundBranchId, Long userId, String uuid) throws Exception {
        repository.removeById(FundBranch.class, fundBranchId, userId, uuid);
    }

    public void update(FundBranch fundBranch, Long userId, String uuid) throws Exception {
        repository.update(fundBranch, userId, uuid);
    }

    public List<FundBranch> list(Long id) {
        if (FundUtils.isNull(id))
            return repository.findAll(FundBranch.class);
        return repository.findAll(FundBranch.class).stream()
                .filter(a -> a.getId().equals(id)).toList();
    }
}
