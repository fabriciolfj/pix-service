package com.payment.configuration;

import com.payment.clients.spi.dto.CreditTransferTransaction;
import com.payment.clients.spi.dto.FIToFICustomerCreditTransfer;
import com.payment.clients.spi.dto.GroupHeader;
import com.payment.clients.spi.dto.Pacs008Document;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Produces;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;

@ApplicationScoped
public class JaxbConfig {

    @Produces
    @Singleton
    public JAXBContext pacs008JaxbContext() throws JAXBException {
        return JAXBContext.newInstance(
                Pacs008Document.class,
                FIToFICustomerCreditTransfer.class,
                GroupHeader.class,
                CreditTransferTransaction.class
        );
    }
}
