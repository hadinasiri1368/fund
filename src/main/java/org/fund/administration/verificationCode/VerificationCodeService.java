package org.fund.administration.verificationCode;

import org.fund.common.FundUtils;
import org.fund.model.FundBranch;
import org.fund.model.VerificationCode;
import org.fund.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VerificationCodeService {
    private final JpaRepository repository;

    public VerificationCodeService(JpaRepository repository) {
        this.repository = repository;
    }

    public void insert(VerificationCode verificationCode, Long userId, String uuid) throws Exception {
        repository.save(verificationCode, userId, uuid);
    }

    public void delete(Long VerificationCodeId, Long userId, String uuid) throws Exception {
        repository.removeById(VerificationCode.class, VerificationCodeId, userId, uuid);
    }

    public void update(VerificationCode verificationCode, Long userId, String uuid) throws Exception {
        repository.update(verificationCode, userId, uuid);
    }

    public List<VerificationCode> list(Long id) {
        if (FundUtils.isNull(id))
            return repository.findAll(VerificationCode.class);
        return repository.findAll(VerificationCode.class).stream()
                .filter(a -> a.getId().equals(id)).toList();
    }
}
