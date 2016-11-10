package zhangliang.view.android.kdemo.entity;

public class BTC123MarketData {
    private String exeByRate;
    private String moneyType;
    private String name;
    private String symbol;
    private String type;
    private TickerData ticker;

    public String getExeByRate() {
        return exeByRate;
    }

    public void setExeByRate(String exeByRate) {
        this.exeByRate = exeByRate;
    }

    public String getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(String moneyType) {
        this.moneyType = moneyType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TickerData getTicker() {
        return ticker;
    }

    public void setTicker(TickerData ticker) {
        this.ticker = ticker;
    }
}
