package com.figi.example.service;

import com.figi.example.constant.Constant;
import com.figi.example.domain.ExchangeCode;
import com.figi.example.domain.FigiDetail;
import com.figi.example.domain.MapJob;
import com.figi.example.domain.FigiData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
public class WebServiceClient {

    private final Logger logger = LoggerFactory.getLogger(WebServiceClient.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${getMappings.request-uri}")
    private String getMappingUrl;

    @Value("${exchcode.request-uri}")
    private String exchangeCodeUri;

    @Value("${figi.api.key}")
    private String figiApiKey;

    @Value("${retry.limit}")
    private int retryLimit;

    @Value("${retry.sleepTime}")
    private long retrySleepTime;

    /**
     * This method call the OpneFIGI's mapping end-point to fetch the details of stocks requested.
     *
     * @param requestDetails {@link List} of stocks whose details are required.
     * @return {@link List} of {@link FigiDetail}, cannot be bull but possibly empty.
     */
    public List<FigiData> getFigiDetailsOfStocks(List<MapJob> requestDetails) {
        ResponseEntity<ArrayList> response = null;
        List<FigiData> responseDetails = null;
        HttpEntity<MapJob> request = new HttpEntity(requestDetails, getRequestHeader());
        logger.debug("getFigiDetailsOfStocks enter.");
        for (int retryCtr = 0; retryCtr <=retryLimit; retryCtr++) {
            try {
                if (retryCtr > 0)
                    Thread.sleep(retrySleepTime);

                response = restTemplate.postForEntity(getMappingUrl, request, ArrayList.class);
                if (response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR || response.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
                    retryCtr++;
                    continue;
                } else if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                    responseDetails = response.getBody();
                } else {
                    logger.debug("response status "+response.getStatusCode());
                }

            } catch (InterruptedException interruptedException) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                logger.error("Exception occurred while rest call", e.getCause());

            }
            if(retryCtr==retryLimit){
              logger.error("Service is unavailable or throwing internal erro");
            }
            break;
        }
        logger.debug("getFigiDetailsOfStocks exit." + response);
        return responseDetails;
    }

    /**
     * This method retrieves the exchange code list from OpenFigi mapping api.
     *
     * @return {@link List} of exchange codes, can be null.
     */
    public List<String> getExchangeCodeList() {
        ResponseEntity<ExchangeCode> response = null;
        List<String> exchangeCodeList = null;
        logger.info("getFigiDetailsOfStocks enter.");
        //This is to pass headers to the Get request, as we cannot directly pass the request header so, have passed it as URI object
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.add(Constant.FIGI_API_KEY_HEADER, figiApiKey);

        try {
            response = restTemplate.getForEntity(exchangeCodeUri, ExchangeCode.class, headers);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                exchangeCodeList = response.getBody().getValues();
            } else {
                logger.debug("response status "+response.getStatusCode());
            }

        } catch (Exception e) {
            logger.error("Exception occurred while rest call", e.getCause());

        }
        logger.debug("getFigiDetailsOfStocks exit." + response);
        return exchangeCodeList;
    }

    /**
     * This method is to set the {@link HttpHeaders} for rest calls to OpenFigi API.
     *
     * @return Instance of {@link HttpHeaders}, never null
     */
    private HttpHeaders getRequestHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(Constant.FIGI_API_KEY_HEADER, figiApiKey);
        return headers;
    }

}
