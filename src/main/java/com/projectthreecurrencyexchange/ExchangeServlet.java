package com.projectthreecurrencyexchange;

import com.projectthreecurrencyexchange.model.Currency;
import com.projectthreecurrencyexchange.model.ErrorResponse;
import com.projectthreecurrencyexchange.model.Exchange;
import com.projectthreecurrencyexchange.service.CurrencyService;
import com.projectthreecurrencyexchange.service.ExchangeService;
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

@WebServlet(name = "exchangeServlet", value = "/exchange")
public class ExchangeServlet extends HttpServlet {
    private ExchangeService exchangeService = new ExchangeService();
    private CurrencyService currencyService = new CurrencyService();

    //подумать над повторяющимся кодом ответов
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String from = request.getParameter("from");
        String to = request.getParameter("to");
        String amountStr = request.getParameter("amount");

        try (Jsonb jsonb = JsonbBuilder.create()) {
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");

            if (from == null || from.isBlank() ||
                    to == null || to.isBlank() ||
                    amountStr == null || amountStr.isBlank()) {
                response.setStatus(400);
                out.println(jsonb.toJson(new ErrorResponse("Не указаны обязательные параметры")));
                return;
            }

            BigDecimal amount;
            try {
                amount = new BigDecimal(amountStr);
            } catch (NumberFormatException e) {
                response.setStatus(400);
                out.println(jsonb.toJson(new ErrorResponse("Некорректная сумма")));
                return;
            }

            if ((amount.compareTo(BigDecimal.ZERO) <= 0)) {
                response.setStatus(400);
                out.println(jsonb.toJson(new ErrorResponse("Сумма должна быть больше нуля")));
                return;
            }

            Currency targetCurrency = currencyService.getCurrencyByCode(to);
            Currency baseCurrency = currencyService.getCurrencyByCode(from);

            if (targetCurrency == null) {
                response.setStatus(404);
                out.println(jsonb.toJson(new ErrorResponse("Валюта " + to + " не найдена")));
                return;
            }

            if (baseCurrency == null) {
                response.setStatus(404);
                out.println(jsonb.toJson(new ErrorResponse("Валюта " + from + " не найдена")));
                return;
            }

            Exchange exchange = exchangeService.mathExchange(from, to, amount);

            if (exchange != null) {
                response.setStatus(200);
                out.println(jsonb.toJson(exchange));
            } else {
                response.setStatus(404);
                out.println(jsonb.toJson(new ErrorResponse("Курс обмена не найден")));
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}