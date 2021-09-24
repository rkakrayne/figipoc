package com.figi.example.domain;

import lombok.Data;

@Data
public class StockDetailsRequest {

    private String indice;
    private String exchCode;
}
