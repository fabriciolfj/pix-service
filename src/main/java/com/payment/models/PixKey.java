package com.payment.models;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class PixKey {

    private String key;
    private PixKeyType keyType;
    private String accountNumber;
    private String branchCode;
    private String bankIspb;
    private String ownerName;
    private String ownerDocument; // CPF ou CNPJ

    public enum PixKeyType {
        CPF, CNPJ, EMAIL, PHONE, EVP
    }

    public static PixKey of(String key, PixKeyType keyType) {
        return PixKey.builder()
                .key(key)
                .keyType(keyType)
                .build();
    }
}
