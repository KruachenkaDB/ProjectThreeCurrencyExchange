package com.projectthreecurrencyexchange.servlet;

import com.projectthreecurrencyexchange.exception.InvalidParameterException;
import com.projectthreecurrencyexchange.model.ExchangeRate;
import com.projectthreecurrencyexchange.service.ExchangeRateService;
import com.projectthreecurrencyexchange.util.JsonResponseWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.SC_CREATED;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;

@WebServlet(value = "/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    private final ExchangeRateService exchangeRateService = new ExchangeRateService();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<ExchangeRate> exchangeRateList = exchangeRateService.getAllExchangeRates();

        JsonResponseWriter.writeJsonResponse(response, exchangeRateList, SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String baseCode = request.getParameter("baseCurrencyCode");
        String targetCode = request.getParameter("targetCurrencyCode");
        String rateStr = request.getParameter("rate");

        if (baseCode == null || baseCode.isBlank() ||
                targetCode == null || targetCode.isBlank() ||
                rateStr == null || rateStr.isBlank()) {
            throw new InvalidParameterException("Required fields are not filled in");
        }

        BigDecimal rate;
        try {
            rate = new BigDecimal(rateStr);
        } catch (NumberFormatException e) {
            throw new InvalidParameterException("You entered a non-numeric value");
        }

        ExchangeRate exchangeRate = exchangeRateService.addExchangeRate(baseCode, targetCode, rate);

        JsonResponseWriter.writeJsonResponse(response, exchangeRate, SC_CREATED);
    }
}