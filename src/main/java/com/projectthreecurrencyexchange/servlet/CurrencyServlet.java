package com.projectthreecurrencyexchange.servlet;

import java.io.*;

import com.projectthreecurrencyexchange.exception.InvalidParameterException;
import com.projectthreecurrencyexchange.model.Currency;
import com.projectthreecurrencyexchange.service.CurrencyService;
import com.projectthreecurrencyexchange.util.JsonResponseWriter;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import static jakarta.servlet.http.HttpServletResponse.SC_OK;

@WebServlet(name = "currencyServlet", value = "/currency/*")
public class CurrencyServlet extends HttpServlet {
    private CurrencyService currencyService = new CurrencyService();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();

        int position = uri.lastIndexOf("/");
        //подумать над проверкой
        String result = uri.substring(position + 1);

        if (result.isBlank()) {
            throw new InvalidParameterException("Currency code is not specified");
        }

        Currency currency = currencyService.getCurrencyByCode(result);

        JsonResponseWriter.writeJsonResponse(response, currency, SC_OK);
    }
}