package com.projectthreecurrencyexchange;

import com.projectthreecurrencyexchange.model.Currency;
import com.projectthreecurrencyexchange.model.ExchangeRate;
import com.projectthreecurrencyexchange.service.CurrencyService;
import com.projectthreecurrencyexchange.service.ExchangeRateService;
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String baseCode = request.getParameter("baseCurrencyCode");
        String targetCode = request.getParameter("targetCurrencyCode");
        String rateStr = request.getParameter("rate");

        if (baseCode == null || baseCode.isEmpty()
                || targetCode == null || targetCode.isEmpty()
                || rateStr == null || rateStr.isEmpty() ) {
            response.setStatus(400);
            return;
        }
        //а еси вместо числа будет текст, обработать
        BigDecimal rate = new BigDecimal(rateStr);

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        // тут сделать валидация исходных данных, проверить и другие методы!!

        ExchangeRate exchangeRate = exchangeRateService.addExchangeRate(baseCode, targetCode, rate);
        Jsonb jsonb = JsonbBuilder.create();
        if (exchangeRate != null) {
            response.setStatus(201);
            out.println(jsonb.toJson(exchangeRate));
        } else {
            response.setStatus(404);
        }
        //статусы дописать
    }
    //url для проверки в постман
    // http://localhost:8080/ProjectThreeCurrencyExchange_war_exploded/exchangeRates
}