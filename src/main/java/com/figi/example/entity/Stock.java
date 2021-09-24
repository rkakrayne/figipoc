package com.figi.example.entity;

import lombok.Data;

import javax.persistence.*;
@Data
@Entity
@Table(name="Stock")
public class Stock {
    @Id
    @Column(name = "STOCK_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "ISIN_CODE")
    private String isinCode;

    @Column(name = "SYMBOL")
    private String symbol;

    @Column(name = "COMPANY_NAME")
    private String companyName;

    @Column(name = "INDICE_ID")
    private long indiceId;
}
