package com.projectthreecurrencyexchange.util;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class JsonResponseWriter {
    public static void writeJsonResponse(HttpServletResponse response, Object object, int status) throws IOException, ServletException {
        try (Jsonb jsonb = JsonbBuilder.create()) {
            response.setStatus(status);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.println(jsonb.toJson(object));
        } catch (Exception ex) {
            throw new ServletException("Не удалось сформировать JSON-ответ", ex);
        }
    }
}