package com.figi.example.domain;

import lombok.Data;

@Data
public class FigiDetail {

    private String figi;
    private String name;
    private String ticker;
    private String exchCode;
    private String compositeFIGI;
    private String securityType;
    private String marketSector;
    private String shareClassFIGI;
    private String securityType2;
    private String securityDescription;
}
