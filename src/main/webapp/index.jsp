<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>ПроектОбменВалют</title>
</head>
<body>
<h1><%= "Hello GET методы!" %>
</h1>
<br/>
<a href="currency/USD">Сurrency USD</a>
<p></p>
<a href="currency/DIA">Сurrency DIA / Status=404</a>
<p></p>
<a href="currencies">Сurrencies</a>
<p></p>
<a href="exchangeRate/USDRUB">ExchangeRate USDRUB</a>
<p></p>
<a href="exchangeRates">ExchangeRates</a>
<p></p>
<a href="exchange?from=USD&to=RUB&amount=100">ExchangeAB</a>
<p></p>
<a href="exchange?from=USD&to=RUB">ExchangeAB / Status=400</a>
<p></p>
<a href="exchange?from=RUB&to=USD&amount=100">ExchangeBA</a>
<p></p>
<a href="exchange?from=RUB&to=DIA&amount=100">ExchangeBA(DIA) / Status=404</a>
<p></p>
<a href="exchange?from=EUR&to=RUB&amount=100">ExchangeUA/UB</a>
<p></p>
<a href="exchange?from=DIA&to=EUR&amount=100">ExchangeBA(DIA) / Status=404</a>
<p></p>
<a href="exchange?from=DIA&to=EUR&amount=-100">ExchangeBA(amount = -100) / Status=400</a>
<p></p>
<a href="exchange?from=EUR&to=DIA&amount=100">ExchangeUA/UB(DIA) / Status=404</a>

</body>
</html>