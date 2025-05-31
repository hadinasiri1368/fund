package org.fund.paymentModule.paymentReason;

import org.fund.common.FundUtils;
import org.fund.model.PaymentReason;
import org.fund.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentReasonService {
    private final JpaRepository repository;

    public PaymentReasonService(JpaRepository repository) {
        this.repository = repository;
    }

    public void insert(PaymentReason paymentReason, Long userId, String uuid) throws Exception {
        repository.save(paymentReason, userId, uuid);
    }

    public void update(PaymentReason paymentReason, Long userId, String uuid) throws Exception {
        repository.update(paymentReason, userId, uuid);
    }

    public void delete(Long paymentReasonId, Long userId, String uuid) throws Exception {
        repository.removeById(PaymentReason.class, paymentReasonId, userId, uuid);
    }

    public List<PaymentReason> list(Long id) {
        if (!FundUtils.isNull(id))
            return repository.findAll(PaymentReason.class).stream().filter(a -> a.getId().equals(id)).toList();
        return repository.findAll(PaymentReason.class);
    }
}
