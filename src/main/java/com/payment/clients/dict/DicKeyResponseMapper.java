package com.payment.clients.dict;

import com.payment.models.PixKey;

public class DicKeyResponseMapper {

    private DicKeyResponseMapper() { }

    public static PixKey toPixKey(DictKeyResponse response) {
        return PixKey.builder()
                .key(response.key())
                .keyType(PixKey.PixKeyType.valueOf(response.keyType()))
                .accountNumber(response.account().number())
                .branchCode(response.account().branch())
                .bankIspb(response.account().participant())
                .ownerName(response.owner().name())
                .ownerDocument(response.owner().taxIdNumber())
                .build();
    }
}
