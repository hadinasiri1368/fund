package org.fund.accounting.detailLedger;

import org.fund.accounting.detailLedger.constant.DetailLedgerType;
import org.fund.administration.params.ParamService;
import org.fund.common.FundUtils;
import org.fund.constant.Consts;
import org.fund.model.DetailLedger;
import org.fund.model.Fund;
import org.fund.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DetailLedgerService {
    private JpaRepository repository;
    private ParamService paramService;

    public DetailLedgerService(JpaRepository repository, ParamService paramService) {
        this.repository = repository;
        this.paramService = paramService;
    }

    public void insert(DetailLedger detailLedger, Long userId, String uuid) throws Exception {
        repository.save(detailLedger, userId, uuid);
    }

    public void delete(Long detailLedgerId, Long userId, String uuid) throws Exception {
        repository.removeById(DetailLedger.class, detailLedgerId, userId, uuid);
    }

    public void deleteByCustomerId(Long customerId, Long userId, String uuid) throws Exception {
        String hql = "delete detailLedger dl where exists (select 1 from customer c where c.id=:customerId and dl.id=c.detailLedger.id)";
        Map<String, Object> param = new HashMap<>();
        param.put("customerId", customerId);
        repository.executeUpdate(hql, param, userId, uuid);
    }

    public void update(DetailLedger detailLedger, Long userId, String uuid) throws Exception {
        repository.update(detailLedger, userId, uuid);
    }

    public List<DetailLedger> list(Long id) {
        if (FundUtils.isNull(id))
            return repository.findAll(DetailLedger.class);
        return repository.findAll(DetailLedger.class).stream()
                .filter(a -> a.getId().equals(id)).toList();
    }

    public DetailLedger get(String name, Fund fund, DetailLedgerType detailLedgerType) {
        DetailLedger detailLedger = DetailLedger.builder()
                .name(name)
                .detailLedgerType(repository.findOne(org.fund.model.DetailLedgerType.class, detailLedgerType.getId()))
                .code(getCode(fund, detailLedgerType))
                .isActive(true).build();
        return detailLedger;
    }

    private String getCode(Fund fund, DetailLedgerType detailLedgerType) {
        Long detailLedgerLength = paramService.getLongValue(fund, Consts.PARAMS_DETAIL_LEDGER_LENGTH);
        String maxCode = getMaxCode(detailLedgerType);
        if (FundUtils.isNull(maxCode)) {
            maxCode = buildLCode(detailLedgerLength);
        } else {
            maxCode = (FundUtils.longValue(maxCode) + 1) + "";
        }
        return maxCode;
    }

    private String getMaxCode(DetailLedgerType detailLedgerType) {
        String hql = "SELECT MAX(dl.code)\n" +
                "FROM detailLedger dl\n" +
                "WHERE dl.detailLedgerType.id = :detailLedgerTypeId";
        Map<String, Object> paramMap = new HashMap();
        paramMap.put("detailLedgerTypeId", detailLedgerType.getId());
        return repository.getStringValue(hql, paramMap);
    }

    private String buildLCode(Long detailLedgerLength) {
        StringBuilder sb = new StringBuilder();
        sb.append('1');
        for (int i = 1; i <= detailLedgerLength - 2; i++) {
            sb.append('0');
        }
        sb.append('1');
        return sb.toString();
    }
}
