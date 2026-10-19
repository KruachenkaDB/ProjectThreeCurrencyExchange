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

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;

import static jakarta.servlet.http.HttpServletResponse.SC_OK;

@WebServlet(value = "/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    private final ExchangeRateService exchangeRateService = new ExchangeRateService();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();

        int position = uri.lastIndexOf("/");
        String str = uri.substring(position + 1);

        if (str.length() != 6) {
            throw new InvalidParameterException("Invalid currency codes");
        }

        String baseCode = str.substring(0, 3);
        String targetCode = str.substring(3, 6);

        ExchangeRate exchangeRate = exchangeRateService.getExchangeRateByCurrencyCodes(baseCode, targetCode);

        JsonResponseWriter.writeJsonResponse(response, exchangeRate, SC_OK);
    }

    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        int position = uri.lastIndexOf("/");

        String str = uri.substring(position + 1);

        if (str.length() != 6) {
            throw new InvalidParameterException("Invalid currency codes");
        }

        String baseCode = str.substring(0, 3);
        String targetCode = str.substring(3, 6);

        BufferedReader reader = request.getReader();
        String rateStr = reader.readLine();

        if (rateStr == null || rateStr.isBlank()) {
            throw new InvalidParameterException("Required field is not filled in");
        }
        position = rateStr.lastIndexOf("=");
        rateStr = rateStr.substring(position + 1);

        BigDecimal rate;
        try {
            rate = new BigDecimal(rateStr);
        } catch (NumberFormatException e) {
            throw new InvalidParameterException("Invalid rate value");
        }

        ExchangeRate exchangeRate = exchangeRateService.updateExchangeRate(baseCode, targetCode, rate);

        JsonResponseWriter.writeJsonResponse(response, exchangeRate, SC_OK);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        if ("PATCH".equalsIgnoreCase(req.getMethod())) {
            doPatch(req, resp);
        } else {
            super.service(req, resp);
        }
    }
}