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

import static jakarta.servlet.http.HttpServletResponse.*;

@WebFilter(value = {
        "/currencies", "/currency/*", "/exchangeRate/*", "/exchangeRates", "/exchange"
})
public class ExceptionHandlingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (InvalidParameterException e) {
            HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
            writeResponse(httpResponse, SC_BAD_REQUEST, e);
        } catch (EntityExistsException e) {
            HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
            writeResponse(httpResponse, SC_CONFLICT, e);
        } catch (NotFoundException e) {
            HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
            writeResponse(httpResponse, SC_NOT_FOUND, e);
        } catch (DatabaseOperationException e) {
            HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
            writeResponse(httpResponse, SC_INTERNAL_SERVER_ERROR, e);
        } catch (RuntimeException e) {
//            поймает неожиданные ошибки
            HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
            writeResponse(httpResponse, SC_INTERNAL_SERVER_ERROR, e);
        }
    }

    private void writeResponse(HttpServletResponse httpResponse, int errorCode, RuntimeException e) throws IOException, ServletException {
        try (Jsonb jsonb = JsonbBuilder.create()) {
            httpResponse.setStatus(errorCode);
            httpResponse.setContentType("application/json");
            httpResponse.setCharacterEncoding("UTF-8");
            PrintWriter out = httpResponse.getWriter();
            out.println(jsonb.toJson(new ErrorResponse(e.getMessage())));
        } catch (Exception ex) {
            throw new ServletException("Failed to build JSON response", ex);
        }
    }
}
