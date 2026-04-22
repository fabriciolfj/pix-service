package com.payment.clients.spi;

import com.payment.clients.spi.dto.*;
import com.payment.models.PixPayment;
import org.w3c.dom.NodeList;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class SpiMapper {

    private static final DateTimeFormatter DT_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private static final DateTimeFormatter D_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final ZoneId BRASILIA = ZoneId.of("America/Sao_Paulo");

    private String getTextContent(NodeList nodes) {
        if (nodes == null || nodes.getLength() == 0) return null;
        return nodes.item(0).getTextContent();
    }

    public static Pacs008Document toJaxbDocument(final PixPayment payment, final String ispb) {
        LocalDateTime now = LocalDateTime.now(BRASILIA);

        GroupHeader grpHdr = new GroupHeader();
        grpHdr.setMsgId(payment.getEndToEndId());
        grpHdr.setCreDtTm(now.format(DT_FMT));
        grpHdr.setNbOfTxs("1");
        SettlementInstruction si = new SettlementInstruction();
        si.setSttlmMtd("CLRG");
        grpHdr.setSttlmInf(si);

        CreditTransferTransaction cdtTrf = new CreditTransferTransaction();

        PaymentIdentification pmtId = new PaymentIdentification();
        pmtId.setEndToEndId(payment.getEndToEndId());
        cdtTrf.setPmtId(pmtId);

        ActiveCurrencyAmount amt = new ActiveCurrencyAmount();
        amt.setCurrency("BRL");
        amt.setValue(payment.getAmount());
        cdtTrf.setIntrBkSttlmAmt(amt);
        cdtTrf.setIntrBkSttlmDt(LocalDate.now(BRASILIA).format(D_FMT));

        // Pagador
        BranchAndFinancialInstitution dbtrAgt = new BranchAndFinancialInstitution();
        FinancialInstitutionIdentification dbtrFi = new FinancialInstitutionIdentification();
        ClearingSystemMemberIdentification dbtrClr = new ClearingSystemMemberIdentification();
        dbtrClr.setMmbId(ispb);
        dbtrFi.setClrSysMmbId(dbtrClr);
        dbtrAgt.setFinInstnId(dbtrFi);
        cdtTrf.setDbtrAgt(dbtrAgt);

        PartyIdentification dbtr = buildParty(
                payment.getPayerKey().getOwnerName(),
                payment.getPayerKey().getOwnerDocument());
        cdtTrf.setDbtr(dbtr);

        // Recebedor
        BranchAndFinancialInstitution cdtrAgt = new BranchAndFinancialInstitution();
        FinancialInstitutionIdentification cdtrFi = new FinancialInstitutionIdentification();
        ClearingSystemMemberIdentification cdtrClr = new ClearingSystemMemberIdentification();
        cdtrClr.setMmbId(payment.getReceiverKey().getBankIspb());
        cdtrFi.setClrSysMmbId(cdtrClr);
        cdtrAgt.setFinInstnId(cdtrFi);
        cdtTrf.setCdtrAgt(cdtrAgt);

        PartyIdentification cdtr = buildParty(
                payment.getReceiverKey().getOwnerName(),
                payment.getReceiverKey().getOwnerDocument());
        cdtTrf.setCdtr(cdtr);

        if (payment.getDescription() != null && !payment.getDescription().isBlank()) {
            RemittanceInformation rmtInf = new RemittanceInformation();
            rmtInf.setUstrd(payment.getDescription());
            cdtTrf.setRmtInf(rmtInf);
        }

        FIToFICustomerCreditTransfer fiTx = new FIToFICustomerCreditTransfer();
        fiTx.setGrpHdr(grpHdr);
        fiTx.setCdtTrfTxInf(cdtTrf);

        Pacs008Document doc = new Pacs008Document();
        doc.setFiToFICstmrCdtTrf(fiTx);
        return doc;
    }

    private static PartyIdentification buildParty(String name, String document) {
        OtherIdentification othr = new OtherIdentification();
        othr.setId(document != null ? document : "");
        PrivateIdentification prvtId = new PrivateIdentification();
        prvtId.setOthr(othr);
        Party id = new Party();
        id.setPrvtId(prvtId);
        PartyIdentification party = new PartyIdentification();
        party.setNm(name != null ? name : "");
        party.setId(id);
        return party;
    }
}
