package com.payment.clients.spi.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class RemittanceInformation {

    // Ustrd = campo livre de até 140 chars (infoPagador no Pix)
    @XmlElement(name = "Ustrd")
    private String ustrd;

    public RemittanceInformation() {}
    public RemittanceInformation(String info) { this.ustrd = info; }
    public String getUstrd() { return ustrd; }

    public void setUstrd(String ustrd) {
        this.ustrd = ustrd;
    }
}
