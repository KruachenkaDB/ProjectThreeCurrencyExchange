package com.projectthreecurrencyexchange.filter;

import com.projectthreecurrencyexchange.exception.DatabaseOperationException;
import com.projectthreecurrencyexchange.exception.EntityExistsException;
import com.projectthreecurrencyexchange.exception.InvalidParameterException;
import com.projectthreecurrencyexchange.exception.NotFoundException;
import com.projectthreecurrencyexchange.model.ErrorResponse;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebFilter(value = {
       "/currencies", "/currency/*", "/exchangeRate/*", "/exchangeRates", "/exchange"
})
public class ExceptionHandlingFilter implements Filter {
    //повт. строка
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (InvalidParameterException e) {
            HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
            writeResponse(httpResponse, 400, e);
        } catch (EntityExistsException e) {
            HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
            writeResponse(httpResponse, 409, e);
        } catch (NotFoundException e) {
            HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
            writeResponse(httpResponse, 404, e);
        } catch (DatabaseOperationException e) {
            HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
            writeResponse(httpResponse, 500, e);
        }
    }
//    ошибки сериализации как внутренней ошибки сервера со статусом 500

    private void writeResponse(HttpServletResponse httpResponse, int errorCode, RuntimeException e) throws IOException, ServletException {
        try (Jsonb jsonb = JsonbBuilder.create()) {
            httpResponse.setStatus(errorCode);
            httpResponse.setContentType("application/json");
            httpResponse.setCharacterEncoding("UTF-8");
            PrintWriter out = httpResponse.getWriter();
            out.println(jsonb.toJson(new ErrorResponse(e.getMessage())));
        } catch (Exception ex) {
            throw new ServletException("Не удалось сформировать JSON-ответ", ex);
        }
    }
}
