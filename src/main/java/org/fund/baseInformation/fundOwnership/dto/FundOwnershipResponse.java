package org.fund.baseInformation.fundOwnership.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.fund.accounting.detailLedger.DetailLedgerDto;
import org.fund.model.DetailLedger;
import org.fund.model.FundOwnership;
import org.fund.model.view.external.BourseFund;
import org.fund.model.view.external.Instrument;

@Getter
@Setter
@Builder
public class FundOwnershipResponse {
    private Long id;
    private BourseFund bourseFund;
    private Instrument instrument;
    private DetailLedger detailLedger;
}
