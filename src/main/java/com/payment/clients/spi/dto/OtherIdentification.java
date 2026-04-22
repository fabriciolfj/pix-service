package com.payment.clients.spi.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class OtherIdentification {

    @XmlElement(name = "Id", required = true)
    private String id;

    public OtherIdentification() {}
    public OtherIdentification(String id) { this.id = id; }
    public String getId() { return id; }

    public void setId(String id) {
        this.id = id;
    }
}
