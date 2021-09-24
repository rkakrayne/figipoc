package com.figi.example.repository;

import com.figi.example.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, BigInteger> {

    Optional<List<Stock>> findByIndiceId(Long indiceIdValue);

}
