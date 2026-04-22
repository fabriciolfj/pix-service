package com.payment.clients.spi.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

import java.math.BigDecimal;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
    "pmtId", "intrBkSttlmAmt", "intrBkSttlmDt",
    "dbtr", "dbtrAgt", "cdtrAgt", "cdtr", "rmtInf"
})
public class CreditTransferTransaction {

    @XmlElement(name = "PmtId", required = true)
    private PaymentIdentification pmtId;

    // @XmlAttribute gera atributo XML: <IntrBkSttlmAmt Ccy="BRL">150.00</IntrBkSttlmAmt>
    @XmlElement(name = "IntrBkSttlmAmt", required = true)
    private ActiveCurrencyAmount intrBkSttlmAmt;

    @XmlElement(name = "IntrBkSttlmDt", required = true)
    private String intrBkSttlmDt;

    @XmlElement(name = "Dbtr", required = true)
    private PartyIdentification dbtr;

    @XmlElement(name = "DbtrAgt", required = true)
    private BranchAndFinancialInstitution dbtrAgt;

    @XmlElement(name = "CdtrAgt", required = true)
    private BranchAndFinancialInstitution cdtrAgt;

    @XmlElement(name = "Cdtr", required = true)
    private PartyIdentification cdtr;

    @XmlElement(name = "RmtInf")
    private RemittanceInformation rmtInf;

    public CreditTransferTransaction() {}

    // builder-style factory para facilitar a construção no adapter
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final CreditTransferTransaction t = new CreditTransferTransaction();

        public Builder pmtId(String endToEndId, String txId) {
            t.pmtId = new PaymentIdentification(endToEndId, txId);
            return this;
        }
        public Builder amount(BigDecimal value) {
            t.intrBkSttlmAmt = new ActiveCurrencyAmount("BRL", value);
            return this;
        }
        public Builder settlementDate(String date) {
            t.intrBkSttlmDt = date;
            return this;
        }
        public Builder debtor(String name, String document) {
            t.dbtr = new PartyIdentification(name, document);
            return this;
        }
        public Builder debtorAgent(String ispb) {
            t.dbtrAgt = new BranchAndFinancialInstitution(ispb);
            return this;
        }
        public Builder creditorAgent(String ispb) {
            t.cdtrAgt = new BranchAndFinancialInstitution(ispb);
            return this;
        }
        public Builder creditor(String name, String document) {
            t.cdtr = new PartyIdentification(name, document);
            return this;
        }
        public Builder remittanceInfo(String info) {
            t.rmtInf = new RemittanceInformation(info);
            return this;
        }
        public CreditTransferTransaction build() { return t; }
    }

    // getters
    public PaymentIdentification getPmtId()                        { return pmtId; }
    public ActiveCurrencyAmount getIntrBkSttlmAmt()               { return intrBkSttlmAmt; }
    public String getIntrBkSttlmDt()                               { return intrBkSttlmDt; }
    public PartyIdentification getDbtr()                            { return dbtr; }
    public BranchAndFinancialInstitution getDbtrAgt()              { return dbtrAgt; }
    public BranchAndFinancialInstitution getCdtrAgt()              { return cdtrAgt; }
    public PartyIdentification getCdtr()                            { return cdtr; }
    public RemittanceInformation getRmtInf()                        { return rmtInf; }

    public void setPmtId(PaymentIdentification pmtId) {
        this.pmtId = pmtId;
    }

    public void setIntrBkSttlmAmt(ActiveCurrencyAmount intrBkSttlmAmt) {
        this.intrBkSttlmAmt = intrBkSttlmAmt;
    }

    public void setIntrBkSttlmDt(String intrBkSttlmDt) {
        this.intrBkSttlmDt = intrBkSttlmDt;
    }

    public void setDbtr(PartyIdentification dbtr) {
        this.dbtr = dbtr;
    }

    public void setDbtrAgt(BranchAndFinancialInstitution dbtrAgt) {
        this.dbtrAgt = dbtrAgt;
    }

    public void setCdtrAgt(BranchAndFinancialInstitution cdtrAgt) {
        this.cdtrAgt = cdtrAgt;
    }

    public void setCdtr(PartyIdentification cdtr) {
        this.cdtr = cdtr;
    }

    public void setRmtInf(RemittanceInformation rmtInf) {
        this.rmtInf = rmtInf;
    }
}
