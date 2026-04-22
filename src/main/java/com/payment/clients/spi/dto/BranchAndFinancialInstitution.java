package com.payment.clients.spi.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class BranchAndFinancialInstitution {

    @XmlElement(name = "FinInstnId")
    private FinancialInstitutionIdentification finInstnId;

    public BranchAndFinancialInstitution() {}

    public BranchAndFinancialInstitution(String ispb) {
        this.finInstnId = new FinancialInstitutionIdentification(ispb);
    }

    public FinancialInstitutionIdentification getFinInstnId() { return finInstnId; }

    public void setFinInstnId(FinancialInstitutionIdentification finInstnId) {
        this.finInstnId = finInstnId;
    }
}
