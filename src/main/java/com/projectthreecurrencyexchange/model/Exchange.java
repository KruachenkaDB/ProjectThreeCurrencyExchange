package com.projectthreecurrencyexchange.model;

import java.math.BigDecimal;

public class Exchange {
    private Currency baseCurrency;
    private Currency targetCurrency;
    private BigDecimal rate;
    private BigDecimal amount;
    private BigDecimal convertedAmount;

    public Exchange(Currency from, Currency to, BigDecimal rate, BigDecimal amount, BigDecimal convertedAmount) {
        this.baseCurrency = from;
        this.targetCurrency = to;
        this.rate = rate;
        this.amount = amount;
        this.convertedAmount = convertedAmount;
    }
    public Currency getBaseCurrency() {
        return baseCurrency;
    }

    public Currency getTargetCurrency() {
        return targetCurrency;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }
}
