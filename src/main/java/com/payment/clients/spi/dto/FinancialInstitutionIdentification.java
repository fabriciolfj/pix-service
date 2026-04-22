package com.payment.clients.spi.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class FinancialInstitutionIdentification {

    @XmlElement(name = "ClrSysMmbId")
    private ClearingSystemMemberIdentification clrSysMmbId;

    public FinancialInstitutionIdentification() {}

    public FinancialInstitutionIdentification(String ispb) {
        this.clrSysMmbId = new ClearingSystemMemberIdentification(ispb);
    }

    public ClearingSystemMemberIdentification getClrSysMmbId() { return clrSysMmbId; }

    public void setClrSysMmbId(ClearingSystemMemberIdentification clrSysMmbId) {
        this.clrSysMmbId = clrSysMmbId;
    }
}
