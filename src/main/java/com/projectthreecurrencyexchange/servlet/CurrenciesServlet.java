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

import static jakarta.servlet.http.HttpServletResponse.SC_CREATED;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;

@WebServlet(value = "/currencies")
public class CurrenciesServlet extends HttpServlet {
    private final CurrencyService currencyService = new CurrencyService();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Currency> currencyList = currencyService.getAllCurrencies();

        JsonResponseWriter.writeJsonResponse(response, currencyList, SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        String name = request.getParameter("name");
        String sign = request.getParameter("sign");

        if (code == null || code.isBlank() ||
                name == null || name.isBlank() ||
                sign == null || sign.isBlank()) {
            throw new InvalidParameterException("Required fields are not filled in");
        }

        if (sign.length() > 3) {
            throw new InvalidParameterException("The length of the sign variable is more than three characters");
        }

        Currency currency = new Currency(code, name, sign);
        currency = currencyService.addCurrency(currency);

        JsonResponseWriter.writeJsonResponse(response, currency, SC_CREATED);
    }
}