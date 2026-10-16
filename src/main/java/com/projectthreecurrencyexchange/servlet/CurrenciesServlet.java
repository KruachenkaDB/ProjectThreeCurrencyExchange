package com.projectthreecurrencyexchange.servlet;

import com.projectthreecurrencyexchange.exception.InvalidParameterException;
import com.projectthreecurrencyexchange.model.Currency;
import com.projectthreecurrencyexchange.service.CurrencyService;
import com.projectthreecurrencyexchange.util.JsonResponseWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "currenciesServlet", value = "/currencies")
public class CurrenciesServlet extends HttpServlet {
    private CurrencyService currencyService = new CurrencyService();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Currency> currencyList = currencyService.getCurrencyAll();

        JsonResponseWriter.writeJsonResponse(response, currencyList, 200);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        String name = request.getParameter("name");
        String sign = request.getParameter("sign");

        if (code == null || code.isBlank() ||
                name == null || name.isBlank() ||
                sign == null || sign.isBlank()) {
            throw new InvalidParameterException("Обязательные поля не заполнены");
        }

        Currency currency = new Currency(code, name, sign);
        currency = currencyService.addCurrency(currency);

        JsonResponseWriter.writeJsonResponse(response, currency, 201);
    }
}