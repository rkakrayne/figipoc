package com.figi.example.service;

import com.figi.example.domain.FigiData;
import com.figi.example.domain.MapJob;
import com.figi.example.domain.StockDetailsRequest;
import com.figi.example.entity.Stock;
import com.figi.example.repository.IndiceRepository;
import com.figi.example.repository.StockRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProcessorServiceImplTest {
    @Mock
    private WebServiceClient webServiceClient;
    @Mock
    private IndiceRepository indiceRepository;
    @Mock
    private StockRepository stockRepository;

    private ProcessorServiceImpl processorService;
    private StockDetailsRequest stockDetailsRequest;
    private FigiData figiData;
    private Stock stock;
    private MapJob mapJob;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        processorService = new ProcessorServiceImpl(webServiceClient, stockRepository, indiceRepository);
        stockDetailsRequest = new StockDetailsRequest();
        stockDetailsRequest.setExchCode("IN");
        stockDetailsRequest.setIndice("NIFTY50");
        figiData = new FigiData();
        stock = new Stock();
        mapJob = new MapJob();
    }

    @Test
    public void whenIndiceIsUnavailable() throws Exception {
//when NIFTY50  is not available
        when(indiceRepository.findByExchCodeAndIndices(stockDetailsRequest.getExchCode(), stockDetailsRequest.getIndice())).thenReturn(Optional.empty());
        List<FigiData> list = processorService.getRequestedIndices(stockDetailsRequest);
        assertEquals(list, null);

    }

    @Test
    public void whenStockDataIsUnavailable() throws Exception {
        when(indiceRepository.findByExchCodeAndIndices(stockDetailsRequest.getExchCode(), stockDetailsRequest.getIndice())).thenReturn(Optional.of(1L));
        when(stockRepository.findByIndiceId(1L)).thenReturn(Optional.empty());
        List<FigiData> list = processorService.getRequestedIndices(stockDetailsRequest);
        assertEquals(list, null);

    }

    @Test
    public void whenFigiDataIsUnavailable() throws Exception {

        when(indiceRepository.findByExchCodeAndIndices(stockDetailsRequest.getExchCode(), stockDetailsRequest.getIndice())).thenReturn(Optional.of(1L));
        when(stockRepository.findByIndiceId(1L)).thenReturn(Optional.of(Arrays.asList(stock)));
        List<FigiData> list = processorService.getRequestedIndices(stockDetailsRequest);
        assertEquals(list.isEmpty(), true);

    }

}
