package com.payment.clients.spi.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"msgId", "creDtTm", "nbOfTxs", "sttlmInf"})
public class GroupHeader {

    @XmlElement(name = "MsgId", required = true)
    private String msgId;

    @XmlElement(name = "CreDtTm", required = true)
    private String creDtTm;

    @XmlElement(name = "NbOfTxs", required = true)
    private String nbOfTxs = "1";

    @XmlElement(name = "SttlmInf", required = true)
    private SettlementInstruction sttlmInf;

    public GroupHeader() {}

    public GroupHeader(String msgId, String creDtTm) {
        this.msgId    = msgId;
        this.creDtTm  = creDtTm;
        this.sttlmInf = new SettlementInstruction();
    }

    public String getMsgId()                   { return msgId; }
    public String getCreDtTm()                 { return creDtTm; }
    public String getNbOfTxs()                 { return nbOfTxs; }
    public SettlementInstruction getSttlmInf() { return sttlmInf; }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public void setSttlmInf(SettlementInstruction sttlmInf) {
        this.sttlmInf = sttlmInf;
    }

    public void setNbOfTxs(String nbOfTxs) {
        this.nbOfTxs = nbOfTxs;
    }

    public void setCreDtTm(String creDtTm) {
        this.creDtTm = creDtTm;
    }
}
