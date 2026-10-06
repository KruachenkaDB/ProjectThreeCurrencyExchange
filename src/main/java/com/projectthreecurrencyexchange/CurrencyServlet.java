package com.projectthreecurrencyexchange;

import java.io.*;

import com.projectthreecurrencyexchange.model.Currency;
import com.projectthreecurrencyexchange.service.CurrencyService;
import jakarta.json.Json;
import jakarta.json.JsonBuilderFactory;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "currencyServlet", value = "/currency/*")
public class CurrencyServlet extends HttpServlet {
    private CurrencyService currencyService = new CurrencyService();

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uri = request.getRequestURI();

        int position = uri.lastIndexOf("/");
        String result = uri.substring(position+1);

        if (result.isEmpty()) {
            response.setStatus(400);
            return;
        }

        PrintWriter out = response.getWriter();

        Currency currency = currencyService.getCurrencyByCode(result);
        Jsonb jsonb = JsonbBuilder.create();

        if (currency != null) {
            response.setStatus(200);
            response.setContentType("application/json");
            out.println(jsonb.toJson(currency));
        } else {
            response.setStatus(404);
        }
    }
}