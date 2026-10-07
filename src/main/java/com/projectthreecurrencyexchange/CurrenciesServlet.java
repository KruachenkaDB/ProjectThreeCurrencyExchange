package com.projectthreecurrencyexchange;

import com.projectthreecurrencyexchange.model.Currency;
import com.projectthreecurrencyexchange.service.CurrencyService;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "currenciesServlet", value = "/currencies")
public class CurrenciesServlet extends HttpServlet {
    private CurrencyService currencyService = new CurrencyService();

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();

        List<Currency> currencyList = currencyService.getCurrencyAll();
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Currency currency = new Currency(
            request.getParameter("code"),
            request.getParameter("name"),
            request.getParameter("sign")
        );

        PrintWriter out = response.getWriter();

        currency = currencyService.addCurrency(currency);
        Jsonb jsonb = JsonbBuilder.create();

        response.setStatus(201);
        response.setContentType("application/json");
        out.println(jsonb.toJson(currency));

        //статусы дописать
    }
}