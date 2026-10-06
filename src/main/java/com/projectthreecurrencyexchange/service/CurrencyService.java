package com.projectthreecurrencyexchange.service;

import com.projectthreecurrencyexchange.dao.CurrencyDao;
import com.projectthreecurrencyexchange.model.Currency;

public class CurrencyService {
    private CurrencyDao currencyDao = new CurrencyDao();

    public Currency getCurrencyByCode(String code){
        return currencyDao.findByCode(code);
    }
}
