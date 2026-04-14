package com.payment.exceptions.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MessageErrorDetails {

    private String field;
    private String message;
}
