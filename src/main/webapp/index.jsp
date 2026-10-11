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
<a href="currencies">Сurrencies</a>
<p></p>
<a href="exchangeRate/USDRUB">ExchangeRate USDRUB</a>
<p></p>
<a href="exchangeRates">ExchangeRates</a>
<p></p>
<a href="exchange?from=USD&to=RUB&amount=100">ExchangeAB</a>
<p></p>
<a href="exchange?from=RUB&to=USD&amount=100">ExchangeBA</a>
<p></p>
<a href="exchange?from=EUR&to=RUB&amount=100">ExchangeUA/UB</a>

</body>
</html>