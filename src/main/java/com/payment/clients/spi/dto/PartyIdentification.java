package com.payment.clients.spi.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"nm", "id"})
public class PartyIdentification {

    @XmlElement(name = "Nm")
    private String nm;

    @XmlElement(name = "Id")
    private Party id;

    public PartyIdentification() {}

    public PartyIdentification(String name, String document) {
        this.nm = name;
        this.id = new Party(document);
    }

    public String getNm()  { return nm; }
    public Party getId()   { return id; }

    public void setId(Party id) {
        this.id = id;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }
}
