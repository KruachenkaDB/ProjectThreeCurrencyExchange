package com.projectthreecurrencyexchange.servlet;

import com.projectthreecurrencyexchange.exception.InvalidParameterException;
import com.projectthreecurrencyexchange.model.ExchangeRate;
import com.projectthreecurrencyexchange.service.ExchangeRateService;
import com.projectthreecurrencyexchange.util.JsonResponseWriter;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(name = "exchangeRatesServlet", value = "/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    private ExchangeRateService exchangeRateService = new ExchangeRateService();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<ExchangeRate> exchangeRateList = exchangeRateService.getAllExchangeRates();

        JsonResponseWriter.writeJsonResponse(response, exchangeRateList, 200);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String baseCode = request.getParameter("baseCurrencyCode");
        String targetCode = request.getParameter("targetCurrencyCode");
        String rateStr = request.getParameter("rate");

        if (baseCode == null || baseCode.isBlank() ||
                targetCode == null || targetCode.isBlank() ||
                rateStr == null || rateStr.isBlank() ) {
            throw new InvalidParameterException("Обязательные поля не заполнены");
        }

        BigDecimal rate = null;
        try {
            rate = new BigDecimal(rateStr);
        } catch (NumberFormatException e) {
            throw new InvalidParameterException("Вы ввели не числовое значение");
        }

        ExchangeRate exchangeRate = exchangeRateService.addExchangeRate(baseCode, targetCode, rate);

        JsonResponseWriter.writeJsonResponse(response, exchangeRate, 201);
    }
    //url для проверки в постман
    // http://localhost:8080/ProjectThreeCurrencyExchange_war_exploded/exchangeRates
}