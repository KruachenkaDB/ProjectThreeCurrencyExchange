package com.projectthreecurrencyexchange.service;

import com.projectthreecurrencyexchange.dao.CurrencyDao;
import com.projectthreecurrencyexchange.model.Currency;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CurrencyService {
    private CurrencyDao currencyDao = new CurrencyDao();
    private List<Currency> currencyDaoList = new ArrayList<>();

    public Currency getCurrencyByCode(String code) {
        return currencyDao.findByCode(code);
    }

    public List<Currency> getCurrencyAll() {
        return currencyDao.findAll();
    }

    public Currency addCurrency(Currency currency) {
        return currencyDao.add(currency);
    }
}
