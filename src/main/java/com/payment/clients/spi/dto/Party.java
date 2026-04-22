package com.payment.clients.spi.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Party {

    @XmlElement(name = "PrvtId")
    private PrivateIdentification prvtId;

    public Party() {}

    public Party(String document) {
        this.prvtId = new PrivateIdentification(document);
    }

    public PrivateIdentification getPrvtId() { return prvtId; }

    public void setPrvtId(PrivateIdentification prvtId) {
        this.prvtId = prvtId;
    }
}
