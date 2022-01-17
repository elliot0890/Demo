package com.elliot.demo.controller;

import com.elliot.demo.model.dto.BankCurrencyDto;
import com.elliot.demo.service.BankCurrencyService;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @author shi-wei
 * @date 2022/1/14
 */
@RestController
public class BankCurrencyController {
    @Autowired
    BankCurrencyService bankCurrencyService;

    @RequestMapping("/getCoinDeskData")
    public ResponseEntity<?> getCoinDeskData() throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("https://api.coindesk.com/v1/bpi/currentprice.json");
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpget);
            int successStatus = 200;
            if (response.getStatusLine().getStatusCode() == successStatus) {
                HttpEntity httpEntity = response.getEntity();
                String content = EntityUtils.toString(httpEntity, "UTF-8");

                return ResponseEntity.status(HttpStatus.OK).body(content);
            }
        } finally {
            if (response != null) {
                response.close();
            }
            httpclient.close();
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("取得coinDesk資料錯誤");
    }

    @PostMapping("/saveOrUpdateCurrency")
    public ResponseEntity<?> saveOrUpdateCurrency(@RequestBody BankCurrencyDto bankCurrencyDto) {
        BankCurrencyDto responseDto = bankCurrencyService.saveOrUpdate(bankCurrencyDto);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @DeleteMapping("/deleteCurrency/{id}")
    public ResponseEntity<?> deleteCurrency(@PathVariable String id) {
        boolean isDelete = bankCurrencyService.delete(id);
        if (isDelete) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id 不存在");
    }

    @PostMapping("/findCurrency/{chartName}")
    public ResponseEntity<?> findCurrencyByChartName(@PathVariable String chartName) {
        return ResponseEntity.status(HttpStatus.OK).body(bankCurrencyService.findCurrencyByChartName(chartName));
    }
}