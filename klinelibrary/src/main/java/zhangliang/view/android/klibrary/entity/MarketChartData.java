package zhangliang.view.android.klibrary.entity;

import java.text.SimpleDateFormat;

/**
 * Created by zhangliang on 16/11/5.
 * QQ:1179980507
 */
public class MarketChartData {
    long time = 0;
    double openPrice = 0;
    double closePrice = 0;
    double lowPrice = 0;
    double highPrice = 0;
    double vol = 0;

    public MarketChartData() {

    }

    public long getTime() {
        return time;
    }

    public String getTime2() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(time*1000);
    }
    public String getTime3() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(time*1000);
    }
    public String getTime4() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        return sdf.format(time*1000);
    }
    public void setTime(long time) {
        this.time = time;
    }

    public double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(double openPrice) {
        this.openPrice = openPrice;
    }

    public double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(double closePrice) {
        this.closePrice = closePrice;
    }

    public double getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(double lowPrice) {
        this.lowPrice = lowPrice;
    }

    public double getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(double highPrice) {
        this.highPrice = highPrice;
    }

    public double getVol() {
        return vol;
    }

    public void setVol(double vol) {
        this.vol = vol;
    }
}
