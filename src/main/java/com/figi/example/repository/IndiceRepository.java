package com.figi.example.repository;

import com.figi.example.entity.Indice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface IndiceRepository extends JpaRepository<Indice, BigInteger> {
    @Query("select indice from Indice where exchCode= :exchangeCode")
    List<String> findByExchCode(@Param("exchangeCode") String exchangeCode);

    @Query("select id from Indice where exchCode= :exchangeCode and indice = :indiceValue")
    Optional<Long> findByExchCodeAndIndices(@Param("exchangeCode") String exchangeCode, @Param("indiceValue") String indiceValue);


}