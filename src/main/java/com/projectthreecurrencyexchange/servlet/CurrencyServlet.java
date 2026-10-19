package com.projectthreecurrencyexchange.servlet;

import java.io.*;

import com.projectthreecurrencyexchange.exception.InvalidParameterException;
import com.projectthreecurrencyexchange.model.Currency;
import com.projectthreecurrencyexchange.service.CurrencyService;
import com.projectthreecurrencyexchange.util.JsonResponseWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import static jakarta.servlet.http.HttpServletResponse.SC_OK;

@WebServlet(value = "/currency/*")
public class CurrencyServlet extends HttpServlet {
    private final CurrencyService currencyService = new CurrencyService();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();

        int position = uri.lastIndexOf("/");
        String result = uri.substring(position + 1);

        if (result.isBlank()) {
            throw new InvalidParameterException("Currency code is not specified");
        }

        Currency currency = currencyService.getCurrencyByCode(result);

        JsonResponseWriter.writeJsonResponse(response, currency, SC_OK);
    }
}