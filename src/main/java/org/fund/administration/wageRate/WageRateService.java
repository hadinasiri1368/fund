package org.fund.administration.wageRate;

import org.fund.common.DateUtils;
import org.fund.common.FundUtils;
import org.fund.config.dataBase.TenantContext;
import org.fund.constant.Consts;
import org.fund.model.NetAssetValue;
import org.fund.model.WageRate;
import org.fund.model.WageRateDetail;
import org.fund.model.view.external.Instrument;
import org.fund.model.view.internal.Security;
import org.fund.repository.JpaRepository;
import org.fund.service.CommonFundService;
import org.fund.administration.wageRate.dto.WageRateInstrumentDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class WageRateService {
    private Map<String, List<Instrument>> listAllInstrument = null;
    private Map<String, List<WageRateInstrumentDto>> listAllWageRateInstrument = null;
    private final JpaRepository repository;
    private final CommonFundService commonFundService;

    public WageRateService(JpaRepository repository, CommonFundService commonFundService) {
        this.repository = repository;
        this.commonFundService = commonFundService;
    }


    @Transactional
    public void insert(WageRate wageRate, WageRateDetail wageRateDetail, Long userId, String uuid) throws Exception {
        repository.save(wageRate, userId, uuid);
        wageRateDetail.setWageRate(wageRate);
        repository.save(wageRateDetail, userId, uuid);
    }

    public void insert(WageRateDetail wageRateDetail, Long userId, String uuid) throws Exception {
        repository.save(wageRateDetail, userId, uuid);
    }

    public void update(WageRate wageRate, Long userId, String uuid) throws Exception {
        repository.update(wageRate, userId, uuid);
    }

    public void update(WageRateDetail wageRateDetail, Long userId, String uuid) throws Exception {
        repository.update(wageRateDetail, userId, uuid);
    }

    public void delete(Long wageRateId, Long userId, String uuid) throws Exception {
        List<WageRateDetail> wageRateDetails = repository.findAll(WageRateDetail.class).stream()
                .filter(a -> a.getWageRate().getId().equals(wageRateId)).toList();
        repository.batchRemove(wageRateDetails, userId, uuid);
    }

    public void deleteWageRateDetail(Long wageRateDetailId, Long userId, String uuid) throws Exception {
        repository.removeById(WageRateDetail.class, wageRateDetailId, userId, uuid);
    }

    public void resetInstrumentCache() {
        this.listAllInstrument.remove(TenantContext.getCurrentTenant());
    }

    public void resetWageRateInstrumenCache() {
        this.listAllWageRateInstrument.remove(TenantContext.getCurrentTenant());
    }

    public WageRateInstrumentDto getWageRateInstrument(Long instrumentId) {
        return wageRateInstrument().stream()
                .filter(a -> a.getId().equals(instrumentId) && !a.getIsContract())
                .findFirst().orElse(null);
    }

    public WageRateInstrumentDto getWageRateContract(Long contractId) {
        return wageRateInstrument().stream()
                .filter(a -> a.getId().equals(contractId) && a.getIsContract())
                .findFirst().orElse(null);
    }

    private List<WageRateInstrumentDto> getWageRateInstrumentDtos() {
        return this.listAllWageRateInstrument.get(TenantContext.getCurrentTenant());
    }

    private void setWageRateInstrumentDtos(List<WageRateInstrumentDto> wageRateInstrumentDtos) {
        this.listAllWageRateInstrument.put(TenantContext.getCurrentTenant(), wageRateInstrumentDtos);
    }

    private List<WageRateInstrumentDto> wageRateInstrument() {
        List<WageRateInstrumentDto> list = getWageRateInstrumentDtos();
        if (!FundUtils.isNull(list))
            return list;
        List<WageRateInstrumentDto> wageRateInstrument = new ArrayList<>();
        List<Instrument> instruments = getAllInstruments();
        Boolean[] purchaseOrSale = {false, true};
        for (Instrument instrument : instruments) {
            for (Boolean pur : purchaseOrSale) {
                boolean isPurchase = pur;
                List<WageRate> keyList = repository.findAll(WageRate.class).stream()
                        .filter(a -> a.getInstrumentTypeId().equals(instrument.getInstrumentType().getId())
                                && a.getInstTypeDerivativesId().equals(instrument.getInstrumentTypeDerivatives().getId())
                                && a.getIsOtc().equals(instrument.getIsOtc())
                                && a.getIsPurchase().equals(isPurchase))
                        .toList();
                for (WageRate wageRate : keyList) {
                    NetAssetValue netAssetValue = commonFundService.getLastEndNav(wageRate.getFund());
                    String date = netAssetValue == null ? DateUtils.getTodayJalali() : netAssetValue.getCalcDate();
                    WageRateDetail wageRateDetail = getWageRateDetail(wageRate, instrument, date);
                    fillWageRateInstrumentDto(wageRateInstrument, instrument.getId(), false, isPurchase, wageRateDetail);
                }
            }
        }

        List<Security> contracts = getAllContracts();
        for (Security security : contracts) {
            for (Boolean pur : purchaseOrSale) {
                boolean isPurchase = pur;
                WageRate key = repository.findAll(WageRate.class).stream()
                        .filter(a -> a.getInstrumentTypeId().equals(security.getSecurityTypeId())
                                && a.getInstTypeDerivativesId().equals(Consts.INSTRUMENT_TYPE_DERIVATIVES_NORMAL)
                                && a.getIsOtc().equals(false)
                                && a.getIsPurchase().equals(isPurchase))
                        .findFirst().orElse(null);
                if (!FundUtils.isNull(key)) {
                    NetAssetValue netAssetValue = commonFundService.getLastEndNav(key.getFund());
                    String date = netAssetValue == null ? DateUtils.getTodayJalali() : netAssetValue.getCalcDate();
                    WageRateDetail wageRateDetail = getWageRateDetail(key, security, date);
                    fillWageRateInstrumentDto(wageRateInstrument, security.getId(), true, isPurchase, wageRateDetail);
                }
            }
        }

        setWageRateInstrumentDtos(wageRateInstrument);
        return wageRateInstrument;
    }

    private WageRateDetail getWageRateDetail(WageRate wageRate, Instrument instrument, String date) {
        return repository.findAll(WageRateDetail.class).stream()
                .sorted(Comparator.comparing(WageRateDetail::getIssueDate, Comparator.reverseOrder()))
                .filter(a -> a.getWageRate().getId().equals(wageRate.getId())
                        && a.getIssueDate().compareTo(date) <= 0
                        && (a.getInstrumentId() == null || a.getInstrumentId().equals(instrument.getId()))
                        && (a.getIndustryId() == null || a.getInstrumentId().equals(instrument.getIndustry().getId())))
                .findFirst().orElse(null);
    }

    private WageRateDetail getWageRateDetail(WageRate wageRate, Security security, String date) {
        return repository.findAll(WageRateDetail.class).stream()
                .sorted(Comparator.comparing(WageRateDetail::getIssueDate, Comparator.reverseOrder()))
                .filter(a -> a.getWageRate().getId().equals(wageRate.getId())
                        && a.getIssueDate().compareTo(date) <= 0
                        && (a.getInstrumentId() == null || a.getInstrumentId().equals(security.getId())))
                .findFirst().orElse(null);
    }

    private List<Instrument> getAllInstruments() {
        List<Instrument> list = this.listAllInstrument.get(TenantContext.getCurrentTenant());
        if (!FundUtils.isNull(list))
            return list;
        String hql = "select i " +
                " from instrument i" +
                " left join instrumentInfo ii on ii.instrument.id=i.id and ii.insMaxLcode like '%1'";
        list = repository.listObjectByQuery(hql, null);
        this.listAllInstrument.put(TenantContext.getCurrentTenant(), list);
        return list;
    }

    private List<Security> getAllContracts() {
        return repository.findAll(Security.class).stream()
                .filter(a -> a.getIsContract()).toList();
    }

    private void fillWageRateInstrumentDto(List<WageRateInstrumentDto> wageRateInstrumentDtos, Long securityId, boolean isContract, boolean isPurchase, WageRateDetail wageRateDetail) {
        if (wageRateDetail == null) return;
        WageRateInstrumentDto wageRateInstrumentDto = wageRateInstrumentDtos.stream()
                .filter(a -> a.getId().equals(securityId) && a.getIsContract() == isContract)
                .findFirst().orElse(null);
        if (wageRateInstrumentDto == null)
            wageRateInstrumentDto = new WageRateInstrumentDto();
        else
            wageRateInstrumentDtos.remove(wageRateInstrumentDto);

        wageRateInstrumentDto.setId(securityId);
        wageRateInstrumentDto.setIsContract(isContract);
        if (isPurchase) {
            wageRateInstrumentDto.setBourseCoPurchase(wageRateDetail.getBourseCo());
            wageRateInstrumentDto.setDepositCoPurchase(wageRateDetail.getDepositCo());
            wageRateInstrumentDto.setBourseOrgPurchase(wageRateDetail.getBourseOrg());
            wageRateInstrumentDto.setItManagementPurchase(wageRateDetail.getItManagement());
            wageRateInstrumentDto.setInterestPurchase(wageRateDetail.getInterest());
            wageRateInstrumentDto.setTaxPurchase(wageRateDetail.getTax());
            wageRateInstrumentDto.setRayanBoursePurchase(wageRateDetail.getMaxRayanBourse());
        } else {
            wageRateInstrumentDto.setBourseCoSale(wageRateDetail.getBourseCo());
            wageRateInstrumentDto.setDepositCoSale(wageRateDetail.getDepositCo());
            wageRateInstrumentDto.setBourseOrgSale(wageRateDetail.getBourseOrg());
            wageRateInstrumentDto.setItManagementSale(wageRateDetail.getItManagement());
            wageRateInstrumentDto.setInterestSale(wageRateDetail.getInterest());
            wageRateInstrumentDto.setTaxSale(wageRateDetail.getTax());
            wageRateInstrumentDto.setRayanBourseSale(wageRateDetail.getMaxRayanBourse());
        }
        wageRateInstrumentDtos.add(wageRateInstrumentDto);
    }
}
