package com.projectthreecurrencyexchange;

import com.projectthreecurrencyexchange.model.Currency;
import com.projectthreecurrencyexchange.model.ExchangeRate;
import com.projectthreecurrencyexchange.service.CurrencyService;
import com.projectthreecurrencyexchange.service.ExchangeRateService;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "exchangeRatesServlet", value = "/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    private ExchangeRateService exchangeRateService = new ExchangeRateService();

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();

        List<ExchangeRate> currencyList = exchangeRateService.getAllRate();
        Jsonb jsonb = JsonbBuilder.create();

        if (!currencyList.isEmpty()) {
            response.setStatus(200);
            response.setContentType("application/json");
            out.println(jsonb.toJson(currencyList));
        } else {
            //подумать над возвращаемым статусом
            response.setStatus(500);
        }
    }
}