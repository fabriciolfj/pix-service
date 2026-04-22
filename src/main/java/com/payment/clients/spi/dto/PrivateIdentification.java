package com.payment.clients.spi.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class PrivateIdentification {

    @XmlElement(name = "Othr")
    private OtherIdentification othr;

    public PrivateIdentification() {}

    public PrivateIdentification(String document) {
        this.othr = new OtherIdentification(document);
    }

    public OtherIdentification getOthr() { return othr; }

    public void setOthr(OtherIdentification othr) {
        this.othr = othr;
    }
}
