package com.example.demo.enums;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum PaymentStatus {
    PAYEE("payée"),ECHUE("échue"),IMPAYEE("impayée"),ENCOURS("en cours de traitement");
    private final String status;

    PaymentStatus(String status) {
        this.status = status;
    }
}
