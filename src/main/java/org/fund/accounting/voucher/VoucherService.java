package org.fund.accounting.voucher;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.fund.accounting.voucher.dto.VoucherDetailDto;
import org.fund.accounting.voucher.dto.VoucherDetailResponseDto;
import org.fund.accounting.voucher.dto.VoucherDto;
import org.fund.accounting.voucher.dto.VoucherResponseDto;
import org.fund.common.FundUtils;
import org.fund.exception.FundException;
import org.fund.exception.VoucherExceptionType;
import org.fund.model.Voucher;
import org.fund.model.VoucherDetail;
import org.fund.model.VoucherStatus;
import org.fund.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class VoucherService {
    private final JpaRepository repository;

    public VoucherService(JpaRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void insert(Voucher voucher, List<VoucherDetail> voucherDetails, Long userId, String uuid) throws Exception {
        preparationVoucherData(voucher);
        validateData(voucherDetails);
        repository.save(voucher, userId, uuid);
        preparationVoucherDetailData(voucher, voucherDetails);
        repository.batchInsert(voucherDetails, userId, uuid);
    }

    @Transactional
    public void update(Voucher voucher, List<VoucherDetail> voucherDetails, Long userId, String uuid) throws Exception {
        if (!FundUtils.isNull(voucher)) {
            validateData(voucher);
            repository.update(voucher, userId, uuid);
        }
        if (!FundUtils.isNull(voucherDetails) && voucherDetails.size() > 0)
            update(voucherDetails, userId, uuid);
    }

    @Transactional
    public void update(List<VoucherDetail> voucherDetails, Long userId, String uuid) throws Exception {
        validateData(voucherDetails);
        validateVoucherStatus(voucherDetails.getFirst().getVoucher(), false);
        repository.batchUpdate(voucherDetails, userId, uuid);
    }

    @Transactional
    public void delete(Long voucherId, Long userId, String uuid) throws Exception {
        Voucher oldVoucher = repository.findOne(Voucher.class, voucherId);
        validateVoucherStatus(oldVoucher, true);
        List<VoucherDetail> voucherDetails = getVoucherDetails(voucherId);
        repository.batchRemove(voucherDetails, userId, uuid);
        repository.remove(oldVoucher, userId, uuid);
    }

    @Transactional
    public void delete(List<VoucherDetail> voucherDetails, Long userId, String uuid) throws Exception {
        validateData(voucherDetails);
        validateVoucherStatus(voucherDetails.getFirst().getVoucher(), true);
        boolean deleteVoucher = false;
        if (getVoucherDetails(voucherDetails.getFirst().getVoucher().getId()).size() == voucherDetails.size())
            deleteVoucher = true;
        repository.batchRemove(voucherDetails, userId, uuid);
        if (deleteVoucher)
            repository.remove(voucherDetails.getFirst().getVoucher(), userId, uuid);

    }

    public List<VoucherResponseDto> list(Long id) {
        String hql = "select \n" +
                "          v.id,\n" +
                "          v.id voucherId,\n" +
                "          vt.id  voucherTypeId,\n" +
                "          vt.name  voucherTypeName,\n" +
                "          fb.id  fundBranchId,\n" +
                "          fb.name  fundBranchName,\n" +
                "          vs.id  voucherStatusId,\n" +
                "          vs.name  voucherStatusName,\n" +
                "          f.id  fundId,\n" +
                "          f.name  fundName,\n" +
                "          v.code,\n" +
                "          v.voucherDate,\n" +
                "          v.comments,\n" +
                "          v.isManual,\n" +
                "          vd.id detailId,\n" +
                "          sl.id subsidiaryLedgerId,\n" +
                "          sl.name subsidiaryLedgerName,\n" +
                "          dl.id detailLedgerId,\n" +
                "          dl.name detailLedgerName,\n" +
                "          vd.comments detailComments,\n" +
                "          vd.debitAmount,\n" +
                "          vd.creditAmount,\n" +
                "        from\n" +
                "          voucher v\n" +
                "        inner join voucherDetail vd on vd.voucher.id = v.id\n" +
                "        inner join voucherType vt on vt.id = v.voucherType.id\n" +
                "        inner join fundBranch fb on fb.id = v.fundBranch.id\n" +
                "        inner join voucherStatus vs on vs.id = v.voucherStatus.id\n" +
                "        inner join fund f on f.id = v.fund.id\n" +
                "        inner join subsidiaryLedger sl on sl.id = vd.subsidiaryLedger.id\n" +
                "        left  join detailLedger dl on dl.id = vd.detailLedger.id\n" +
                "        where v.id = :voucherid or :voucherid is null";
        Map<String, Object> params = Map.of("voucherid", id);
        List<Map<String, Object>> rawList = repository.listMapByQuery(hql, params);
        if (rawList == null || rawList.isEmpty())
            return Collections.emptyList();


        ObjectMapper objectMapper = new ObjectMapper();
        Map<Long, VoucherResponseDto> voucherMap = new LinkedHashMap<>();

        for (Map<String, Object> row : rawList) {
            Long voucherId = FundUtils.longValue(row.get("voucherId"));

            VoucherResponseDto voucherDto = voucherMap.computeIfAbsent(voucherId, key -> {
                VoucherResponseDto dto = objectMapper.convertValue(row, VoucherResponseDto.class);
                dto.setVoucherDetails(new ArrayList<>());
                return dto;
            });

            VoucherDetailResponseDto detailDto = objectMapper.convertValue(row, VoucherDetailResponseDto.class);
            voucherDto.getVoucherDetails().add(detailDto);
        }

        return new ArrayList<>(voucherMap.values());
    }

    private List<VoucherDetail> getVoucherDetails(Long voucherId) {
        String hql = "select vd from voucherDetail vd where vd.voucher.id=:voucherId";
        Map<String, Object> params = new HashMap();
        params.put("voucherId", voucherId);
        return repository.listObjectByQuery(hql, params);
    }


    private void preparationVoucherData(Voucher voucher) {
        voucher.setVoucherStatus(repository.findOne(VoucherStatus.class, org.fund.accounting.voucher.constant.VoucherStatus.TEMPORARY.getId()));
    }

    private void preparationVoucherDetailData(Voucher voucher, List<VoucherDetail> voucherDetails) {
        long lineNumber = 0;
        for (VoucherDetail voucherDetailItem : voucherDetails) {
            voucherDetailItem.setLineNumber(++lineNumber);
            voucherDetailItem.setVoucher(voucher);
        }
    }

    private void validateData(Voucher voucher) {
        validateVoucherStatus(voucher, false);
        validateVoucherDate(voucher);
    }

    private void validateData(List<VoucherDetail> voucherDetails) {
        if (FundUtils.isNull(voucherDetails) || voucherDetails.size() == 0)
            throw new FundException(VoucherExceptionType.VOUCHER_DETAIL_IS_NULL);

        boolean hasDuplicate = voucherDetails.stream()
                .collect(Collectors.groupingBy(pd -> pd.getVoucher().getId(), Collectors.counting()))
                .values()
                .stream()
                .anyMatch(count -> count > 1);
        if (hasDuplicate)
            throw new FundException(VoucherExceptionType.VOUCHER_DETAIL_HAS_DIFFERENT_VOUCHER);

        boolean hasNullValue = voucherDetails.stream()
                .anyMatch(a -> (FundUtils.isNull(a.getCreditAmount()) && FundUtils.isNull(a.getDebitAmount())) ||
                        (a.getCreditAmount() == 0L && a.getDebitAmount() == 0L));
        if (hasNullValue)
            throw new FundException(VoucherExceptionType.VOUCHER_DETAIL_HAS_NULL_DEBIT_AND_CREDIT, new Object[]{voucherDetails.getFirst().getVoucher().getVoucherType().getName()});

        boolean isBalanced = voucherDetails.stream().mapToLong(VoucherDetail::getDebitAmount).sum() ==
                voucherDetails.stream().mapToLong(VoucherDetail::getCreditAmount).sum();
        if (!isBalanced)
            throw new FundException(VoucherExceptionType.VOUCHER_DETAIL_DEBIT_IS_NOT_EQUAL_CREDIT);
    }

    private void validateVoucherStatus(Voucher voucher, boolean forDelete) {
        if (!voucher.getVoucherStatus().getId().equals(org.fund.accounting.voucher.constant.VoucherStatus.TEMPORARY.getId())) {
            throw new FundException(VoucherExceptionType.CAN_NOT_CHANGE_VOUCHER);
        }
        if (!forDelete) {
            Voucher oldVoucher = repository.findOne(Voucher.class, voucher.getId());
            if (!oldVoucher.getVoucherStatus().getId().equals(voucher.getVoucherStatus().getId()) && (
                    voucher.getVoucherStatus().getId().equals(org.fund.accounting.voucher.constant.VoucherStatus.REVIEW.getId()) ||
                            voucher.getVoucherStatus().getId().equals(org.fund.accounting.voucher.constant.VoucherStatus.DEFINITE.getId()))) {
                throw new FundException(VoucherExceptionType.CAN_NOT_CHANGE_STATUS,
                        new Object[]{org.fund.accounting.voucher.constant.VoucherStatus.REVIEW.getTitle() + "," +
                                org.fund.accounting.voucher.constant.VoucherStatus.DEFINITE.getTitle()});
            }
        }
    }

    private void validateVoucherDate(Voucher voucher) {
//        throw new RuntimeException("nav has not been launched yet");
    }

}
