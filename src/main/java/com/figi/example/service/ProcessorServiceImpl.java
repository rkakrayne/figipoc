package com.figi.example.service;

import com.figi.example.constant.Constant;
import com.figi.example.domain.MapJob;
import com.figi.example.domain.RequestIndiceList;
import com.figi.example.domain.StockDetailsRequest;
import com.figi.example.domain.FigiData;
import com.figi.example.entity.Stock;
import com.figi.example.repository.IndiceRepository;
import com.figi.example.repository.StockRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ProcessorServiceImpl implements ProcessorService {

    private final Logger logger = LoggerFactory.getLogger(ProcessorServiceImpl.class);

    private final WebServiceClient webServiceClient;
    private final StockRepository stockRepository;
    private final IndiceRepository indiceRepository;

    @Autowired
    public ProcessorServiceImpl(WebServiceClient webServiceClient, StockRepository stockRepository,IndiceRepository indiceRepository) {
        this.webServiceClient = webServiceClient;
        this.stockRepository = stockRepository;
        this.indiceRepository=indiceRepository;
    }

    /**
     * This method retrieves the stocks data based on exchangeCode and indice provided.
     * @param requestStockDetails, exchange code and indice value provided.
     * @return {@link List} of {@link FigiData}, cna be null or empty
     */
    public List<FigiData> getRequestedIndices(StockDetailsRequest requestStockDetails) {
        logger.info(" getExchCode :: " + requestStockDetails.getExchCode() + " getIndice:: " + requestStockDetails.getIndice());
        List<FigiData> responseDetailsList = null;
        Optional<Long> indiceId = indiceRepository.findByExchCodeAndIndices(requestStockDetails.getExchCode(), requestStockDetails.getIndice());
        if (indiceId.isPresent()) {
            Optional<List<Stock>> stockList = stockRepository.findByIndiceId(indiceId.get());
            if (stockList.isPresent()) {
                responseDetailsList = webServiceClient.getFigiDetailsOfStocks(populateMappingRequestData(stockList.get(), requestStockDetails.getExchCode()));
            }
        } else {
            logger.info("No search result present in Table Indice");
        }
        return responseDetailsList;
    }

    /**
     * This method is to get the Indices based on exchange code.
     * @param requestIndiceList, exchange code
     * @return {@link List} of indices.
     */
    @Override
    public List<String> getIndiceListOnExchCode(RequestIndiceList requestIndiceList) {
        logger.debug("Get Indices for exchangeCode:: "+requestIndiceList);
        List<String> indiceList = indiceRepository.findByExchCode(requestIndiceList.getExchCode());
        logger.debug("exchange codes:: "+indiceList);
        return indiceList;
    }


    /**
     * This method populates the request data object to be sent to OpenFIGI
     *
     * @param stocks    {@link List} of stocks whose data needs to be populated
     * @param exchCode, exchange code of stocks requested
     * @return {@link List} of {@link MapJob} data to be sent to OpenFIGI
     */
     private List<MapJob> populateMappingRequestData(List<Stock> stocks, String exchCode) {
        List<MapJob> list = new ArrayList<>();
        MapJob job;
        for (Stock stock : stocks) {

            job = new MapJob();
            job.setExchCode(exchCode);
            job.setIdType(Constant.ID_ISIN);
            job.setIdValue(stock.getIsinCode());
            list.add(job);
        }
        return list;
    }
}
