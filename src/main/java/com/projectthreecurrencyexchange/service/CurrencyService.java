package com.projectthreecurrencyexchange.service;

import com.projectthreecurrencyexchange.dao.CurrencyDao;
import com.projectthreecurrencyexchange.exception.EntityExistsException;
import com.projectthreecurrencyexchange.model.Currency;

import java.util.List;

public class CurrencyService {
    private final CurrencyDao currencyDao = new CurrencyDao();

    public Currency getCurrencyByCode(String code) {
        return currencyDao.findByCode(code);
    }

    public List<Currency> getAllCurrencies() {
        return currencyDao.findAll();
    }

    public Currency addCurrency(Currency currency) {
        if (!currencyDao.existsByCode(currency.getCode())) {
            return currencyDao.add(currency);
        } else {
            throw new EntityExistsException("Currency with this code already exists");
        }
    }
}
