package com.payment.clients.spi.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

/**
 * Corpo da mensagem pacs.008 — equivalente ao payload do JSON.
 * Contém o cabeçalho de grupo (GrpHdr) e as instruções de crédito (CdtTrfTxInf).
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"grpHdr", "cdtTrfTxInf"})
public class FIToFICustomerCreditTransfer {

    @XmlElement(name = "GrpHdr", required = true)
    private GroupHeader grpHdr;

    @XmlElement(name = "CdtTrfTxInf", required = true)
    private CreditTransferTransaction cdtTrfTxInf;

    public FIToFICustomerCreditTransfer() {}

    public FIToFICustomerCreditTransfer(GroupHeader grpHdr, CreditTransferTransaction cdtTrfTxInf) {
        this.grpHdr        = grpHdr;
        this.cdtTrfTxInf   = cdtTrfTxInf;
    }

    public GroupHeader getGrpHdr()                     { return grpHdr; }
    public CreditTransferTransaction getCdtTrfTxInf()  { return cdtTrfTxInf; }

    public void setCdtTrfTxInf(CreditTransferTransaction cdtTrfTxInf) {
        this.cdtTrfTxInf = cdtTrfTxInf;
    }

    public void setGrpHdr(GroupHeader grpHdr) {
        this.grpHdr = grpHdr;
    }
}
