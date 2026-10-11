package com.projectthreecurrencyexchange;

import com.projectthreecurrencyexchange.model.ExchangeRate;
import com.projectthreecurrencyexchange.service.ExchangeRateService;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

@WebServlet(name = "exchangeRateServlet", value = "/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    private ExchangeRateService exchangeRateService = new ExchangeRateService();

    @Override
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

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        if ("PATCH".equalsIgnoreCase(req.getMethod())) {
            doPatch(req, resp);
        } else {
            super.service(req, resp);
        }
    }

    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws IOException {
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

        //надо ли делать апперкейс для переменных параметра
        BufferedReader reader = request.getReader();
        String rateStr = reader.readLine();
        position = rateStr.lastIndexOf("=");
        rateStr = rateStr.substring(position+1);

        if (rateStr == null || rateStr.isEmpty() ) {
            response.setStatus(400);
            return;
        }
        //а есkи вместо числа будет текст, обработать и не отрицательное ли оно, 0 допустимо?
        BigDecimal rate = new BigDecimal(rateStr);

        PrintWriter out = response.getWriter();

        ExchangeRate exchangeRate = exchangeRateService.updateExchangeRate(baseCode, targetCode, rate);
        Jsonb jsonb = JsonbBuilder.create();

        if (exchangeRate != null) {
            response.setStatus(200);
            response.setContentType("application/json");
            out.println(jsonb.toJson(exchangeRate));
        } else {
            // исправить
            response.setStatus(404);
        }
        //статусы дописать
    }
//    http://localhost:8080/ProjectThreeCurrencyExchange_war_exploded/exchangeRate/USDRUB
}