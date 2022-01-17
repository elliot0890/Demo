package com.elliot.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.elliot.demo.model.dto.BankCurrencyDto;
import com.elliot.demo.model.dto.SearchBankCurrencyResponseDto;
import com.elliot.demo.service.BankCurrencyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author shi-wei
 * @date 2022/1/17
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
class BankCurrencyControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    BankCurrencyService bankCurrencyService;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new BankCurrencyController()).build();
    }

    @Test
    void testGetCoinDeskData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/getCoinDeskData")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    void testSaveCurrency() throws Exception {
        BankCurrencyDto requestDto = new BankCurrencyDto();
        requestDto.setId("");
        requestDto.setUpdatedIso(new Date().toString());
        BankCurrencyDto.BpiDto bpiDto = new BankCurrencyDto.BpiDto();
        bpiDto.setId("");
        requestDto.setBpi(bpiDto);

        BankCurrencyDto expectedResponseDto = new BankCurrencyDto();
        expectedResponseDto.setId("1");
        BankCurrencyDto.BpiDto expectedBpiDto = new BankCurrencyDto.BpiDto();
        expectedBpiDto.setId("2");
        expectedResponseDto.setBpi(expectedBpiDto);

        Mockito.when(bankCurrencyService.saveOrUpdate(requestDto)).thenReturn(expectedResponseDto);
        String requestJson = JSONObject.toJSONString(requestDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/saveOrUpdateCurrency")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    void testUpdateCurrency() throws Exception{
        BankCurrencyDto requestDto = new BankCurrencyDto();
        requestDto.setId("1");
        requestDto.setUpdatedIso(new Date().toString());
        BankCurrencyDto.BpiDto bpiDto = new BankCurrencyDto.BpiDto();
        bpiDto.setId("2");
        requestDto.setBpi(bpiDto);

        BankCurrencyDto expectedResponseDto = new BankCurrencyDto();
        expectedResponseDto.setId("1");
        BankCurrencyDto.BpiDto expectedBpiDto = new BankCurrencyDto.BpiDto();
        expectedBpiDto.setId("2");
        expectedResponseDto.setBpi(expectedBpiDto);

        Mockito.when(bankCurrencyService.saveOrUpdate(requestDto)).thenReturn(expectedResponseDto);
        String requestJson = JSONObject.toJSONString(requestDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/saveOrUpdateCurrency")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    void testDeleteCurrency() throws Exception {
        String id = "1";
        Mockito.when(bankCurrencyService.delete(id)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/deleteCurrency/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void testFindCurrencyByChartName() throws Exception {
        String chartName = "Bitcoin";
        SearchBankCurrencyResponseDto expectedResponseDto = new SearchBankCurrencyResponseDto();
        expectedResponseDto.setId("1");
        expectedResponseDto.setChartName("Bitcoin");
        expectedResponseDto.setUpdatedIso(new Date().toString());
        expectedResponseDto.setDisclaimer("This data was produced from the CoinDesk Bitcoin Price Index (USD). Non-USD currency data converted using hourly conversion rate from openexchangerates.org");

        Mockito.when(bankCurrencyService.findCurrencyByChartName(chartName)).thenReturn(expectedResponseDto);

        String returnString = mockMvc.perform(MockMvcRequestBuilders.post("/findCurrency/Bitcoin")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        SearchBankCurrencyResponseDto actual = objectMapper.readValue(returnString, SearchBankCurrencyResponseDto.class);

        Assertions.assertEquals(expectedResponseDto, actual);
    }
}