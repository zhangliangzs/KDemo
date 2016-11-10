package zhangliang.view.android.kdemo.entity;
/**
 * Created by zhangliang on 16/11/5.
 * QQ:1179980507
 */
public class TickerData {

    private String buy = "";                   // 买一价（人民币）
    private String buydollar = "";               // 买一价（美元）
    private String date = "";                    // 时间戳
    private String dollar = "";                  // 最新成交价（美元）
    private String high = "";                  // 最高价（人民币）
    private String highdollar = "";              // 最高价（美元）
    private String last = "";                // 最新成交价（人民币）
    private String low = "";                     // 最低价（人民币）
    private String lowdollar = "";             // 最低价（美元）
    private String sell = "";                   // 卖一价（人民币）
    private String selldollar = "";              // 卖一价（美元）
    private String vol = "";                     // 成交量
    private String riseRate = "";

    public String getBuy() {
        return buy;
    }

    public void setBuy(String buy) {
        this.buy = buy;
    }

    public String getBuydollar() {
        return buydollar;
    }

    public void setBuydollar(String buydollar) {
        this.buydollar = buydollar;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDollar() {
        return dollar;
    }

    public void setDollar(String dollar) {
        this.dollar = dollar;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getHighdollar() {
        return highdollar;
    }

    public void setHighdollar(String highdollar) {
        this.highdollar = highdollar;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getLowdollar() {
        return lowdollar;
    }

    public void setLowdollar(String lowdollar) {
        this.lowdollar = lowdollar;
    }

    public String getSell() {
        return sell;
    }

    public void setSell(String sell) {
        this.sell = sell;
    }

    public String getSelldollar() {
        return selldollar;
    }

    public void setSelldollar(String selldollar) {
        this.selldollar = selldollar;
    }

    public String getVol() {
        return vol;
    }

    public void setVol(String vol) {
        this.vol = vol;
    }

    public String getRiseRate() {
        if(riseRate.equals("")){
            riseRate="0.00";
        }
        return riseRate;
    }

    public void setRiseRate(String riseRate) {
        this.riseRate = riseRate;
    }

    @Override
    public String toString() {
        return " buy=" + buy
                + " buydollar=" + buydollar
                + " date=" + date
                + " dollar=" + dollar
                + " high=" + high
                + " highdollar=" + highdollar
                + " last=" + last
                + " low=" + low
                + " lowdollar=" + lowdollar
                + " sell=" + sell
                + " selldollar=" + selldollar
                + " vol=" + vol
                + " riseRate=" + riseRate
                +" | ";
    }
}
