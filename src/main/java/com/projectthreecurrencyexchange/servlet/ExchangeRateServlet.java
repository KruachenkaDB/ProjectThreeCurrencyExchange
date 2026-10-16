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

@WebServlet(name = "exchangeRateServlet", value = "/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    private ExchangeRateService exchangeRateService = new ExchangeRateService();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();

        int position = uri.lastIndexOf("/");
        String str = uri.substring(position + 1);

        if (str.length() != 6) {
            throw new InvalidParameterException("Некорректно указаны коды валют");
        }

        String baseCode = str.substring(0, 3);
        String targetCode = str.substring(3, 6);

        ExchangeRate exchangeRate = exchangeRateService.getExchangeRateByCodes(baseCode, targetCode);

        JsonResponseWriter.writeJsonResponse(response, exchangeRate, 200);
    }

    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        int position = uri.lastIndexOf("/");

        String str = uri.substring(position + 1);

        if (str.length() != 6) {
            throw new InvalidParameterException("Некорректно указаны коды валют");
        }

        String baseCode = str.substring(0, 3);
        String targetCode = str.substring(3, 6);

        BufferedReader reader = request.getReader();
        String rateStr = reader.readLine();

        if (rateStr == null || rateStr.isBlank()) {
            throw new InvalidParameterException("Обязательное поле не заполнено");
        }
        position = rateStr.lastIndexOf("=");
        rateStr = rateStr.substring(position + 1);

        BigDecimal rate;
        try {
            rate = new BigDecimal(rateStr);
        } catch (NumberFormatException e) {
            throw new InvalidParameterException("Некорректное значение rate");
        }

        ExchangeRate exchangeRate = exchangeRateService.updateExchangeRate(baseCode, targetCode, rate);

        JsonResponseWriter.writeJsonResponse(response, exchangeRate, 200);
    }
    //    http://localhost:8080/ProjectThreeCurrencyExchange_war_exploded/exchangeRate/USDRUB

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        if ("PATCH".equalsIgnoreCase(req.getMethod())) {
            doPatch(req, resp);
        } else {
            super.service(req, resp);
        }
    }
}