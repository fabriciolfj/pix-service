package com.payment.clients.spi.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ClearingSystemMemberIdentification {

    // MmbId = ISPB do banco (8 dígitos)
    @XmlElement(name = "MmbId", required = true)
    private String mmbId;

    public ClearingSystemMemberIdentification() {}
    public ClearingSystemMemberIdentification(String ispb) { this.mmbId = ispb; }
    public String getMmbId() { return mmbId; }

    public void setMmbId(String mmbId) {
        this.mmbId = mmbId;
    }
}
