package com.figi.example.util;

import com.figi.example.entity.Indice;
import com.figi.example.entity.Stock;
import com.figi.example.repository.IndiceRepository;
import com.figi.example.repository.StockRepository;

public class DbUtil {
    static Indice indice =null;
    public static void setTableData(StockRepository stockRepository, IndiceRepository indiceRepository) {


        //// Indice entry
        indice = new Indice();
        indice.setIndice("NIFTY50");
        indice.setExchCode("IN");
        indiceRepository.save(indice);

        indice = new Indice();
        indice.setIndice("NIFTY100");
        indice.setExchCode("IN");
        indiceRepository.save(indice);

        indice = new Indice();
        indice.setIndice("NIFTY500");
        indice.setExchCode("IN");
        indiceRepository.save(indice);
        System.out.println(indiceRepository.findAll());

        Stock stock1;
        stock1 = new Stock();
        stock1.setCompanyName("Asian Paints Ltd.");
        stock1.setSymbol("ASIANPAINT");
        stock1.setIsinCode("INE021A01026");
        stock1.setIndiceId(1);
        stockRepository.save(stock1);

        stock1 = new Stock();
        stock1.setCompanyName("Adani Ports and Special Economic Zone Ltd.\n");
        stock1.setSymbol("ADANIPORTS");
        stock1.setIsinCode("INE742F01042");
        stock1.setIndiceId(1);
        stockRepository.save(stock1);
        stock1 = new Stock();
        stock1.setCompanyName("Bajaj Finance Ltd.");
        stock1.setSymbol("BAJFINANCE");
        stock1.setIsinCode("INE296A01024");
        stock1.setIndiceId(1);
        stockRepository.save(stock1);
        stock1 = new Stock();
        stock1.setCompanyName("Coal India Ltd.");
        stock1.setSymbol("COALINDIA");
        stock1.setIsinCode("INE522F01014");
        stock1.setIndiceId(1);
        stockRepository.save(stock1);
        stock1 = new Stock();
        stock1.setCompanyName("HDFC Bank Ltd.");
        stock1.setSymbol("HDFCBANK");
        stock1.setIsinCode("INE040A01034");
        stock1.setIndiceId(2);
        stockRepository.save(stock1);

    }
}
