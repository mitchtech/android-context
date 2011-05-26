package edu.fsu.cs.contextprovider.finance;

public class GoogleFinanceQuote {
	private int avgVolume;
	private String brutLast;
	private int brutTradeDateUtc;
	private int brutTradeTimeUtc;
	private String change;
	private String chartUrl;
	private String company;
	private String currency;
	private int currentDateUtc;
	private int currentTimeUtc;
	private boolean daylightSavings;
	private int delay;
	private String disclaimerUrl;
	private int divisor;
	private String ecnUrl;
	private String exchange;
	private int exchangeClosing;
	private String exchangeTimezone;
	private String exchangeUtcOffset;
	private double high;
	private double isldLast;
	private int isldTradeDateUtc;
	private int isldTradeTimeUtc;
	private double last;
	private double low;
	private double marketCap;
	private double open;
	private double percentChange;
	private String prettySymbol;
	private String symbol;
	private String symbolLookupUrl;
	private String symbolUrl;
	private int tradeDateUtc;
	private String tradeTimestamp;
	private int tradeTimeUtc;
	private int volume;
	private double yClose;

	public GoogleFinanceQuote() {
		avgVolume = -1;
		brutLast = "";
		brutTradeDateUtc = -1;
		brutTradeTimeUtc = -1;
		change = "";
		chartUrl = "";
		company = "";
		currency = "";
		currentDateUtc = -1;
		currentTimeUtc = -1;
		daylightSavings = false;
		delay = -1;
		disclaimerUrl = "";
		divisor = -1;
		ecnUrl = "";
		exchange = "";
		exchangeClosing = -1;
		exchangeTimezone = "";
		exchangeUtcOffset = "";
		high = -1.0;
		isldLast = -1.0;
		isldTradeDateUtc = -1;
		isldTradeTimeUtc = -1;
		last = -1.0;
		low = -1.0;
		marketCap = -1.0;
		open = -1.0;
		percentChange = -1.0;
		prettySymbol = "";
		symbol = "";
		symbolLookupUrl = "";
		symbolUrl = "";
		tradeDateUtc = -1;
		tradeTimestamp = "";
		tradeTimeUtc = -1;
		volume = -1;
		yClose = -1.0;
	}

	public int getAvgVolume() {
		return avgVolume;
	}

	public void setAvgVolume(int avgVolume) {
		this.avgVolume = avgVolume;
	}

	public String getBrutLast() {
		return brutLast;
	}

	public void setBrutLast(String brutLast) {
		this.brutLast = brutLast;
	}

	public int getBrutTradeDateUtc() {
		return brutTradeDateUtc;
	}

	public void setBrutTradeDateUtc(int brutTradeDateUtc) {
		this.brutTradeDateUtc = brutTradeDateUtc;
	}

	public int getBrutTradeTimeUtc() {
		return brutTradeTimeUtc;
	}

	public void setBrutTradeTimeUtc(int brutTradeTimeUtc) {
		this.brutTradeTimeUtc = brutTradeTimeUtc;
	}

	public String getChange() {
		return change;
	}

	public void setChange(String change) {
		this.change = change;
	}

	public String getChartUrl() {
		return chartUrl;
	}

	public void setChartUrl(String chartUrl) {
		this.chartUrl = chartUrl;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public int getCurrentDateUtc() {
		return currentDateUtc;
	}

	public void setCurrentDateUtc(int currentDateUtc) {
		this.currentDateUtc = currentDateUtc;
	}

	public int getCurrentTimeUtc() {
		return currentTimeUtc;
	}

	public void setCurrentTimeUtc(int currentTimeUtc) {
		this.currentTimeUtc = currentTimeUtc;
	}

	public boolean isDaylightSavings() {
		return daylightSavings;
	}

	public void setDaylightSavings(boolean daylightSavings) {
		this.daylightSavings = daylightSavings;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public String getDisclaimerUrl() {
		return disclaimerUrl;
	}

	public void setDisclaimerUrl(String disclaimerUrl) {
		this.disclaimerUrl = disclaimerUrl;
	}

	public int getDivisor() {
		return divisor;
	}

	public void setDivisor(int divisor) {
		this.divisor = divisor;
	}

	public String getEcnUrl() {
		return ecnUrl;
	}

	public void setEcnUrl(String ecnUrl) {
		this.ecnUrl = ecnUrl;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public int getExchangeClosing() {
		return exchangeClosing;
	}

	public void setExchangeClosing(int exchangeClosing) {
		this.exchangeClosing = exchangeClosing;
	}

	public String getExchangeTimezone() {
		return exchangeTimezone;
	}

	public void setExchangeTimezone(String exchangeTimezone) {
		this.exchangeTimezone = exchangeTimezone;
	}

	public String getExchangeUtcOffset() {
		return exchangeUtcOffset;
	}

	public void setExchangeUtcOffset(String exchangeUtcOffset) {
		this.exchangeUtcOffset = exchangeUtcOffset;
	}

	public double getHigh() {
		return high;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public double getIsldLast() {
		return isldLast;
	}

	public void setIsldLast(double isldLast) {
		this.isldLast = isldLast;
	}

	public int getIsldTradeDateUtc() {
		return isldTradeDateUtc;
	}

	public void setIsldTradeDateUtc(int isldTradeDateUtc) {
		this.isldTradeDateUtc = isldTradeDateUtc;
	}

	public int getIsldTradeTimeUtc() {
		return isldTradeTimeUtc;
	}

	public void setIsldTradeTimeUtc(int isldTradeTimeUtc) {
		this.isldTradeTimeUtc = isldTradeTimeUtc;
	}

	public double getLast() {
		return last;
	}

	public void setLast(double last) {
		this.last = last;
	}

	public double getLow() {
		return low;
	}

	public void setLow(double low) {
		this.low = low;
	}

	public double getMarketCap() {
		return marketCap;
	}

	public void setMarketCap(double marketCap) {
		this.marketCap = marketCap;
	}

	public double getOpen() {
		return open;
	}

	public void setOpen(double open) {
		this.open = open;
	}

	public double getPercentChange() {
		return percentChange;
	}

	public void setPercentChange(double percentChange) {
		this.percentChange = percentChange;
	}

	public String getPrettySymbol() {
		return prettySymbol;
	}

	public void setPrettySymbol(String prettySymbol) {
		this.prettySymbol = prettySymbol;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getSymbolLookupUrl() {
		return symbolLookupUrl;
	}

	public void setSymbolLookupUrl(String symbolLookupUrl) {
		this.symbolLookupUrl = symbolLookupUrl;
	}

	public String getSymbolUrl() {
		return symbolUrl;
	}

	public void setSymbolUrl(String symbolUrl) {
		this.symbolUrl = symbolUrl;
	}

	public int getTradeDateUtc() {
		return tradeDateUtc;
	}

	public void setTradeDateUtc(int tradeDateUtc) {
		this.tradeDateUtc = tradeDateUtc;
	}

	public String getTradeTimestamp() {
		return tradeTimestamp;
	}

	public void setTradeTimestamp(String tradeTimestamp) {
		this.tradeTimestamp = tradeTimestamp;
	}

	public int getTradeTimeUtc() {
		return tradeTimeUtc;
	}

	public void setTradeTimeUtc(int tradeTimeUtc) {
		this.tradeTimeUtc = tradeTimeUtc;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	public double getyClose() {
		return yClose;
	}

	public void setyClose(double yClose) {
		this.yClose = yClose;
	}
}
