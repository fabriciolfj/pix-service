package com.payment.clients.spi.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"endToEndId", "txId"})
public class PaymentIdentification {

    @XmlElement(name = "EndToEndId", required = true)
    private String endToEndId;

    @XmlElement(name = "TxId", required = true)
    private String txId;

    public PaymentIdentification() {}

    public PaymentIdentification(String endToEndId, String txId) {
        this.endToEndId = endToEndId;
        this.txId       = txId;
    }

    public String getEndToEndId() { return endToEndId; }
    public String getTxId()       { return txId; }

    public void setEndToEndId(String endToEndId) {
        this.endToEndId = endToEndId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }
}
