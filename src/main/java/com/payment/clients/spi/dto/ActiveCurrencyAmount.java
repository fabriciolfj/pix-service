package com.payment.clients.spi.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlValue;

import java.math.BigDecimal;

@XmlAccessorType(XmlAccessType.FIELD)
public class ActiveCurrencyAmount {

    // @XmlAttribute gera Ccy="BRL" como atributo do elemento XML
    @XmlAttribute(name = "Ccy", required = true)
    private String currency;

    // @XmlValue gera o conteúdo de texto do elemento
    @XmlValue
    private BigDecimal value;

    public ActiveCurrencyAmount() {}

    public ActiveCurrencyAmount(String currency, BigDecimal value) {
        this.currency = currency;
        this.value    = value;
    }

    public String getCurrency()   { return currency; }
    public BigDecimal getValue()  { return value; }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
