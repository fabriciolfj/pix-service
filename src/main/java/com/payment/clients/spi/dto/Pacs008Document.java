package com.payment.clients.spi.dto;

import jakarta.xml.bind.annotation.*;

/**
 * Raiz do documento pacs.008.001.08 (FIToFICustomerCreditTransfer).
 *
 * JAXB serializa/deserializa esta hierarquia de DTOs para/de XML
 * respeitando o namespace ISO 20022 exigido pelo BACEN — sem
 * montar String manualmente.
 *
 * Equivalente ao @JsonRootName do Jackson, mas para XML.
 */
@XmlRootElement(
    name      = "Document",
    namespace = "urn:iso:std:iso:20022:tech:xsd:pacs.008.001.08"
)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = "fiToFICstmrCdtTrf")
public class Pacs008Document {

    @XmlElement(name = "FIToFICstmrCdtTrf", required = true)
    private FIToFICustomerCreditTransfer fiToFICstmrCdtTrf;

    public Pacs008Document() {}

    public Pacs008Document(FIToFICustomerCreditTransfer body) {
        this.fiToFICstmrCdtTrf = body;
    }

    public FIToFICustomerCreditTransfer getFiToFICstmrCdtTrf() {
        return fiToFICstmrCdtTrf;
    }

    public void setFiToFICstmrCdtTrf(FIToFICustomerCreditTransfer fiToFICstmrCdtTrf) {
        this.fiToFICstmrCdtTrf = fiToFICstmrCdtTrf;
    }
}
