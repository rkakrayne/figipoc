package com.figi.example.service;

import com.figi.example.domain.RequestIndiceList;
import com.figi.example.domain.StockDetailsRequest;
import com.figi.example.domain.FigiData;

import java.util.List;

public interface ProcessorService {
    List<FigiData> getRequestedIndices(StockDetailsRequest requestStockDetails);

    List<String> getIndiceListOnExchCode(RequestIndiceList exchCode);
}
