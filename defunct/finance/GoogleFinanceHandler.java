package edu.fsu.cs.contextprovider.finance;

import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class GoogleFinanceHandler extends DefaultHandler {
	private static final String PKG = "edu.fsu.cs.contextprovider";
	private static final String TAG = "GoogleFinanceHandler";
	private static final String NEW_QUOTE_TAG = "finance";
	private static final String DATA_ATTRIBUTE = "data";
	private static final ArrayList<String> ATTRIBUTES = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		{
			add("symbol");
			add("pretty_symbol");
			add("symbol_lookup_url");
			add("company");
			add("exchange");
			add("exchange_timezone");
			add("exchange_utc_offset");
			add("exchange_closing");
			add("divisor");
			add("currency");
			add("last");
			add("high");
			add("low");
			add("volume");
			add("avg_volume");
			add("market_cap");
			add("open");
			add("y_close");
			add("change");
			add("perc_change");
			add("delay");
			add("trade_timestamp");
			add("trade_date_utc");
			add("trade_time_utc");
			add("current_date_utc");
			add("current_time_utc");
			add("symbol_url");
			add("chart_url");
			add("disclaimer_url");
			add("ecn_url");
			add("isld_last");
			add("isld_trade_date_utc");
			add("isld_trade_time_utc");
			add("brut_last");
			add("brut_trade_date_utc");
			add("brut_trade_time_utc");
			add("daylight_savings");
		}
	};

	private ArrayList<GoogleFinanceQuote> quotes;
	private GoogleFinanceQuote currentQuote;
	private boolean inQuote = false;

	public ArrayList<GoogleFinanceQuote> getQuotes() {
		Log.i(PKG, TAG + ": getQuotes()");
		return quotes;
	}

	@Override
	public void startDocument() throws SAXException {
		Log.i(PKG, TAG + ": startDocument()");
		this.quotes = new ArrayList<GoogleFinanceQuote>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		String dataAttribute;
		Log.i(PKG, TAG + ": startElement()");
		if (localName.contentEquals(NEW_QUOTE_TAG)) {
			inQuote = true;
			currentQuote = new GoogleFinanceQuote();
		} else {
			dataAttribute = attributes.getValue(DATA_ATTRIBUTE);
			switch (ATTRIBUTES.indexOf(localName)) {
			case 0:
				Log.i(PKG, TAG + ": Found - " + localName + " \\ data - "
						+ dataAttribute);
				this.currentQuote.setSymbol(dataAttribute);
				break;
			case 1:
				Log.i(PKG, TAG + ": Found - " + localName + " \\ data - "
						+ dataAttribute);
				this.currentQuote.setPrettySymbol(dataAttribute);
				break;
			case 2:
				Log.i(PKG, TAG + ": Found - " + localName + " \\ data - "
						+ dataAttribute);
				this.currentQuote.setSymbolLookupUrl(dataAttribute);
				break;
			case 3:
				Log.i(PKG, TAG + ": Found - " + localName + " \\ data - "
						+ dataAttribute);
				this.currentQuote.setCompany(dataAttribute);
				break;
			case 4:
				Log.i(PKG, TAG + ": Found - " + localName + " \\ data - "
						+ dataAttribute);
				this.currentQuote.setExchange(dataAttribute);
				break;
			case 5:
				Log.i(PKG, TAG + ": Found - " + localName + " \\ data - "
						+ dataAttribute);
				this.currentQuote.setExchangeTimezone(dataAttribute);
				break;
			case 6:
				Log.i(PKG, TAG + ": Found - " + localName + " \\ data - "
						+ dataAttribute);
				this.currentQuote.setExchangeUtcOffset(dataAttribute);
				break;
			case 7:
				Log.i(PKG, TAG + ": Found - " + localName + " \\ data - "
						+ dataAttribute);
				try {
					this.currentQuote.setExchangeClosing(Integer
							.parseInt(dataAttribute));
				} catch (NumberFormatException e) {
					Log.i(PKG, TAG + ": Caught NumberFormatException!");
				}
				break;
			case 8:
				Log.i(PKG, TAG + ": Found - " + localName + " \\ data - "
						+ dataAttribute);
				try {
					this.currentQuote.setDivisor(Integer
							.parseInt(dataAttribute));
				} catch (NumberFormatException e) {
					Log.i(PKG, TAG + ": Caught NumberFormatException!");
				}
				break;
			case 9:
				Log.i(PKG, TAG + ": Found - " + localName + " \\ data - "
						+ dataAttribute);
				try {
					this.currentQuote.setCurrency(dataAttribute);
				} catch (NumberFormatException e) {
					Log.i(PKG, TAG + ": Caught NumberFormatException!");
				}
				break;
			case 10:
				Log.i(PKG, TAG + ": Found - " + localName + " \\ data - "
						+ dataAttribute);
				try {
					this.currentQuote
							.setLast(Double.parseDouble(dataAttribute));
				} catch (NumberFormatException e) {
					Log.i(PKG, TAG + ": Caught NumberFormatException!");
				}
				break;
			case 11:
				Log.i(PKG, TAG + ": Found - " + localName + " \\ data - "
						+ dataAttribute);
				try {
					this.currentQuote
							.setHigh(Double.parseDouble(dataAttribute));
				} catch (NumberFormatException e) {
					Log.i(PKG, TAG + ": Caught NumberFormatException!");
				}
				break;
			case 12:
				Log.i(PKG, TAG + ": Found - " + localName + " \\ data - "
						+ dataAttribute);

				try {
					this.currentQuote.setLow(Double.parseDouble(dataAttribute));
				} catch (NumberFormatException e) {
					Log.i(PKG, TAG + ": Caught NumberFormatException!");
				}

				break;
			case 13:
				Log.i(PKG, TAG + ": Found - " + localName + " \\ data - "
						+ dataAttribute);
				try {
					this.currentQuote
							.setVolume(Integer.parseInt(dataAttribute));
				} catch (NumberFormatException e) {
					Log.i(PKG, TAG + ": Caught NumberFormatException!");
				}
				break;
			case 14:
				Log.i(PKG, TAG + ": Found - " + localName + " \\ data - "
						+ dataAttribute);
				try {
					this.currentQuote.setAvgVolume(Integer
							.parseInt(dataAttribute));
				} catch (NumberFormatException e) {
					Log.i(PKG, TAG + ": Caught NumberFormatException!");
				}
				break;
			case 15:
				Log.i(PKG, TAG + ": Found - " + localName + " \\ data - "
						+ dataAttribute);
				try {
					this.currentQuote.setMarketCap(Double
							.parseDouble(dataAttribute));
				} catch (NumberFormatException e) {
					Log.i(PKG, TAG + ": Caught NumberFormatException!");
				}
				break;
			case 16:
				Log.i(PKG, TAG + ": Found - " + localName + " \\ data - "
						+ dataAttribute);
				try {
					this.currentQuote
							.setOpen(Double.parseDouble(dataAttribute));
				} catch (NumberFormatException e) {
					Log.i(PKG, TAG + ": Caught NumberFormatException!");
				}
				break;
			case 17:
				Log.i(PKG, TAG + ": Found - " + localName + " \\ data - "
						+ dataAttribute);
				try {
					this.currentQuote.setyClose(Double
							.parseDouble(dataAttribute));
				} catch (NumberFormatException e) {
					Log.i(PKG, TAG + ": Caught NumberFormatException!");
				}
				break;
			case 18:
				Log.i(PKG, TAG + ": Found - " + localName + " \\ data - "
						+ dataAttribute);
				this.currentQuote.setChange(dataAttribute);
				break;
			case 19:
				Log.i(PKG, TAG + ": Found - " + localName + " \\ data - "
						+ dataAttribute);
				try {
					this.currentQuote.setPercentChange(Double
							.parseDouble(dataAttribute));
				} catch (NumberFormatException e) {
					Log.i(PKG, TAG + ": Caught NumberFormatException!");
				}
				break;
			case 20:
				Log.i(PKG, TAG + ": Found - " + localName + " \\ data - "
						+ dataAttribute);
				try {
					this.currentQuote.setDelay(Integer.parseInt(dataAttribute));
				} catch (NumberFormatException e) {
					Log.i(PKG, TAG + ": Caught NumberFormatException!");
				}
				break;
			case 21:
				Log.i(PKG, TAG + ": Found - " + localName + " \\ data - "
						+ dataAttribute);
				this.currentQuote.setTradeTimestamp(dataAttribute);
				break;
			case 22:
				Log.i(PKG, TAG + ": Found - " + localName + " \\ data - "
						+ dataAttribute);
				try {
					this.currentQuote.setTradeDateUtc(Integer
							.parseInt(dataAttribute));
				} catch (NumberFormatException e) {
					Log.i(PKG, TAG + ": Caught NumberFormatException!");
				}
				break;
			case 23:
				Log.i(PKG, TAG + ": Found - " + localName + " \\ data - "
						+ dataAttribute);

				try {
					this.currentQuote.setTradeTimeUtc(Integer
							.parseInt(dataAttribute));
				} catch (NumberFormatException e) {
					Log.i(PKG, TAG + ": Caught NumberFormatException!");
				}

				break;
			case 24:
				Log.i(PKG, TAG + ": Found - " + localName + " \\ data - "
						+ dataAttribute);
				try {
					this.currentQuote.setCurrentDateUtc(Integer
							.parseInt(dataAttribute));
				} catch (NumberFormatException e) {
					Log.i(PKG, TAG + ": Caught NumberFormatException!");
				}
				break;
			case 25:
				Log.i(PKG, TAG + ": Found - " + localName + " \\ data - "
						+ dataAttribute);
				try {
					this.currentQuote.setCurrentTimeUtc(Integer
							.parseInt(dataAttribute));

				} catch (NumberFormatException e) {
					Log.i(PKG, TAG + ": Caught NumberFormatException!");
				}
				break;
			case 26:
				Log.i(PKG, TAG + ": Found - " + localName + " \\ data - "
						+ dataAttribute);
				this.currentQuote.setSymbolUrl(dataAttribute);
				break;
			case 27:
				Log.i(PKG, TAG + ": Found - " + localName + " \\ data - "
						+ dataAttribute);
				this.currentQuote.setChartUrl(dataAttribute);
				break;
			case 28:
				Log.i(PKG, TAG + ": Found - " + localName + " \\ data - "
						+ dataAttribute);
				this.currentQuote.setDisclaimerUrl(dataAttribute);
				break;
			case 29:
				Log.i(PKG, TAG + ": Found - " + localName + " \\ data - "
						+ dataAttribute);
				this.currentQuote.setEcnUrl(dataAttribute);
				break;
			case 30:
				Log.i(PKG, TAG + ": Found - " + localName + " \\ data - "
						+ dataAttribute);
				try {
					this.currentQuote.setIsldLast(Double
							.parseDouble(dataAttribute));
				} catch (NumberFormatException e) {
					Log.i(PKG, TAG + ": Caught NumberFormatException!");
				}
				break;
			case 31:
				Log.i(PKG, TAG + ": Found - " + localName + " \\ data - "
						+ dataAttribute);
				try {
					this.currentQuote.setIsldTradeDateUtc(Integer
							.parseInt(dataAttribute));
				} catch (NumberFormatException e) {
					Log.i(PKG, TAG + ": Caught NumberFormatException!");
				}
				break;
			case 32:
				Log.i(PKG, TAG + ": Found - " + localName + " \\ data - "
						+ dataAttribute);
				try {
					this.currentQuote.setIsldTradeTimeUtc(Integer
							.parseInt(dataAttribute));
				} catch (NumberFormatException e) {
					Log.i(PKG, TAG + ": Caught NumberFormatException!");
				}
				break;
			case 33:
				Log.i(PKG, TAG + ": Found - " + localName + " \\ data - "
						+ dataAttribute);
				this.currentQuote.setBrutLast(dataAttribute);
				break;
			case 34:
				Log.i(PKG, TAG + ": Found - " + localName + " \\ data - "
						+ dataAttribute);
				try {
					this.currentQuote.setBrutTradeDateUtc(Integer
							.parseInt(dataAttribute));
				} catch (NumberFormatException e) {
					Log.i(PKG, TAG + ": Caught NumberFormatException!");
				}
				break;
			case 35:
				Log.i(PKG, TAG + ": Found - " + localName + " \\ data - "
						+ dataAttribute);
				try {
					this.currentQuote.setBrutTradeTimeUtc(Integer
							.parseInt(dataAttribute));
				} catch (NumberFormatException e) {
					Log.i(PKG, TAG + ": Caught NumberFormatException!");
				}
				break;
			case 36:
				Log.i(PKG, TAG + ": Found - " + localName + " \\ data - "
						+ dataAttribute);
				try {
					this.currentQuote.setDaylightSavings(Boolean
							.parseBoolean(dataAttribute));
				} catch (NumberFormatException e) {
					Log.i(PKG, TAG + ": Caught NumberFormatException!");
				}
				break;
			default:
				Log.i(PKG, TAG + ": unknown attribute - " + localName);
			}
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		Log.i(PKG, TAG + ": endElement()");
		if (localName.contentEquals("finance")) {
			inQuote = false;
			quotes.add(currentQuote);
		}
	}

	@Override
	public void endDocument() throws SAXException {
		Log.i(PKG, TAG + ": endDocument()");
		// do nothing
	}
}
