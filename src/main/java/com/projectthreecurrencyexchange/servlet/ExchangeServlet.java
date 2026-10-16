package com.projectthreecurrencyexchange.servlet;

import com.projectthreecurrencyexchange.exception.InvalidParameterException;
import com.projectthreecurrencyexchange.model.Exchange;
import com.projectthreecurrencyexchange.service.ExchangeService;
import com.projectthreecurrencyexchange.util.JsonResponseWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet(name = "exchangeServlet", value = "/exchange")
public class ExchangeServlet extends HttpServlet {
    private ExchangeService exchangeService = new ExchangeService();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String from = request.getParameter("from");
        String to = request.getParameter("to");
        String amountStr = request.getParameter("amount");

        if (from == null || from.isBlank() ||
                to == null || to.isBlank() ||
                amountStr == null || amountStr.isBlank()) {
            throw new InvalidParameterException("Обязательные поля не заполнены");
        }

        BigDecimal amount;
        try {
            amount = new BigDecimal(amountStr);
        } catch (NumberFormatException e) {
            throw new InvalidParameterException("Некорректная сумма");
        }

        Exchange exchange = exchangeService.calculateExchangeAmount(from, to, amount);

        JsonResponseWriter.writeJsonResponse(response, exchange, 200);
    }
}