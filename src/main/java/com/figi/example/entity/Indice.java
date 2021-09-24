package com.figi.example.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="Indice")
public class Indice {
    @Id
    @Column(name = "INDICE_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "INDICE")
    private String indice;

    @Column(name = "EXCH_CODE")
    private String exchCode;

    @Override
    public String toString() {
        return "Indice{" +
                "id=" + id +
                ", indice='" + indice + '\'' +
                ", exchCode='" + exchCode + '\'' +
                '}';
    }
}
