package com.figi.example.controller;

import com.figi.example.domain.FigiData;
import com.figi.example.domain.RequestIndiceList;
import com.figi.example.domain.StockDetailsRequest;
import com.figi.example.repository.IndiceRepository;
import com.figi.example.repository.StockRepository;
import com.figi.example.service.ProcessorService;
import com.figi.example.service.WebServiceClient;
import com.figi.example.util.DbUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.net.HttpURLConnection;
import java.util.List;

@Api
@RestController
@CrossOrigin
@RequestMapping(value = "/figi")
public class Controller {
    private final Logger logger = LoggerFactory.getLogger(Controller.class);
    @Autowired
    ProcessorService processorService;
    @Autowired
    WebServiceClient webServiceClient;
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private IndiceRepository indiceRepository;

    /**
     * This method is to retrieve the data from OpenFigi api.
     *
     * @param requestStockDetails , indice and exchange code whose stocks detail to be retrieved
     * @return {@link List} of details
     */
    @ApiOperation(value = "This is to fetch the details from OpenFigi api.")
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Successfully retrieved data"),
            @ApiResponse(code = HttpURLConnection.HTTP_NO_CONTENT, message = "No data found")
    })
    @PostMapping(value = "/figiDetails", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getDetails(@RequestBody StockDetailsRequest requestStockDetails) {
        logger.info("Entering getDetails");
        ResponseEntity<String> responseEntity = null;
        List<FigiData> indiceDetails = processorService.getRequestedIndices(requestStockDetails);
        if (CollectionUtils.isEmpty(indiceDetails)) {
            logger.error("no details found- " + indiceDetails);
            responseEntity = new ResponseEntity("No Data found", HttpStatus.NO_CONTENT);
        } else {
            responseEntity = new ResponseEntity(indiceDetails, HttpStatus.OK);
        }
        logger.info("getDetails response " + responseEntity);
        return responseEntity;
    }

    /**
     * This is to fetch the exchange codes list.
     *
     * @return {@link List} of exchange codes.
     */
    @ApiOperation(value = "This is to fetch the exchange codes list.")
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Successfully retrieved data"),
            @ApiResponse(code = HttpURLConnection.HTTP_NO_CONTENT, message = "No data found")
    })
    @GetMapping("/getExchangeCode")
    public ResponseEntity<String> getExchangeCodeList() {
        logger.info("Entering getExchangeCodelist");
        ResponseEntity<String> responseEntity = null;
        List<String> exchangeCodes = webServiceClient.getExchangeCodeList();
        if (CollectionUtils.isEmpty(exchangeCodes)) {
            logger.error("no details found- " + exchangeCodes);
            responseEntity = new ResponseEntity("No Data found", HttpStatus.NO_CONTENT);
        } else {
            responseEntity = new ResponseEntity(exchangeCodes, HttpStatus.OK);
        }
        return responseEntity;

    }

    /**
     * This method is to get the Indices based on exchange code provided.
     *
     * @param requestIndiceList exchange code
     * @return {@link List} of indices
     */
    @ApiOperation(value = "This is to fetch the Indices based on exchange code provided.")
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Successfully retrieved data"),
            @ApiResponse(code = HttpURLConnection.HTTP_NO_CONTENT, message = "No data found")
    })
    @PostMapping(value = "/getIndiceListOnExchCode", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getIndiceListOnExchCode(@RequestBody RequestIndiceList requestIndiceList) {
        logger.info("Entering getIndiceListOnExchCode");
        ResponseEntity<String> responseEntity = null;
        List<String> indices = processorService.getIndiceListOnExchCode(requestIndiceList);
        if (CollectionUtils.isEmpty(indices)) {
            logger.error("no details found- " + indices);
            responseEntity = new ResponseEntity("No Data found", HttpStatus.NO_CONTENT);
        } else {
            responseEntity = new ResponseEntity(indices, HttpStatus.OK);
        }
        return responseEntity;

    }

    /**
     * This is to insert data in tables
     */
    //TODO: Have to update this api
    @ApiOperation(value = "Temporary end-point to insert data in tables")
    @GetMapping("/insertDataInH2")
    public void getDetails() {
        DbUtil.setTableData(stockRepository, indiceRepository);
    }


}
