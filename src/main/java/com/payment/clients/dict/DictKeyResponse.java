package com.payment.clients.dict;

public record DictKeyResponse(String key,
                              String keyType,
                              AccountInfo account,
                              OwnerInfo owner) {

    record AccountInfo(String participant, String branch, String number, String accountType) {}
    record OwnerInfo(String type, String taxIdNumber, String name) {}
}
