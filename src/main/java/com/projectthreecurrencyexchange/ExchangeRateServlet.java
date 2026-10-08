package com.projectthreecurrencyexchange;

import com.projectthreecurrencyexchange.model.ExchangeRate;
import com.projectthreecurrencyexchange.service.ExchangeRateService;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
@WebServlet(name = "exchangeRateServlet", value = "/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    private ExchangeRateService exchangeRateService = new ExchangeRateService();

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uri = request.getRequestURI();

        int position = uri.lastIndexOf("/");

        //подумать над проверкой
        String str = uri.substring(position+1);
        if (str.length()!=6) {
            response.setStatus(400);
            return;
        }

        String baseCode = str.substring(0, 3);
        String targetCode = str.substring(3, 6);

        PrintWriter out = response.getWriter();

        ExchangeRate exchangeRate = exchangeRateService.getCurrencyByCodes(baseCode, targetCode);
        Jsonb jsonb = JsonbBuilder.create();

        if (exchangeRate != null) {
            response.setStatus(200);
            response.setContentType("application/json");
            out.println(jsonb.toJson(exchangeRate));
        } else {
            // исправить
            response.setStatus(404);
        }
    }
}