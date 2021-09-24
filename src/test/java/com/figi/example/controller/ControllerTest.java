package com.figi.example.controller;


import com.figi.example.domain.FigiData;
import com.figi.example.domain.StockDetailsRequest;
import com.figi.example.service.ProcessorService;
import com.figi.example.service.WebServiceClient;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class ControllerTest {
    @Mock
    ProcessorService processorService;

    @Mock
    WebServiceClient webServiceClient;

    Controller controller = new Controller();
    private MockMvc mockMvc;

    private StockDetailsRequest stockDetailsRequest;
    private FigiData figiData;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .build();

        MockitoAnnotations.initMocks(this);
        stockDetailsRequest = new StockDetailsRequest();
        stockDetailsRequest.setExchCode("IN");
        stockDetailsRequest.setIndice("NIFTY50");

        figiData = new FigiData();
    }

    @Test
    @Ignore
    public void whenFigiDataIsAvailable() throws Exception {

        List<String> exchangeCodes = Arrays.asList("IN");
        when(processorService.getRequestedIndices(stockDetailsRequest)).thenReturn(Arrays.asList(figiData));
        // assertThat(processorService.getRequestedIndices(testData), Collections.EMPTY_LIST);
        assertNotEquals(processorService.getRequestedIndices(stockDetailsRequest), Collections.EMPTY_LIST);
        assertEquals(processorService.getRequestedIndices(stockDetailsRequest), Arrays.asList(figiData));

        when(webServiceClient.getExchangeCodeList()).thenReturn(exchangeCodes);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/figi/getExchangeCode")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
        ;


    }


}
