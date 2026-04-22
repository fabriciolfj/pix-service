package com.payment.clients.spi.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class SettlementInstruction {

    // CLRG = Clearing — único valor aceito pelo SPI do BACEN
    @XmlElement(name = "SttlmMtd", required = true)
    private String sttlmMtd = "CLRG";

    public String getSttlmMtd() { return sttlmMtd; }

    public void setSttlmMtd(String sttlmMtd) {
        this.sttlmMtd = sttlmMtd;
    }
}
