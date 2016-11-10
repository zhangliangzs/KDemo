package zhangliang.view.android.klibrary.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangliang on 16/11/5.
 * QQ:1179980507
 */
public class RSV {
    private ArrayList<Double> rsv;
    private int n;
    double high = 0.0;
    double low = 0.0;
    double close = 0.0;
    public RSV(List<MarketChartData> OHLCData, int m) {
        n = m;
        rsv = new ArrayList<Double>();
        ArrayList<Double> r = new ArrayList<Double>();
        double rs = 0.0;

        if (OHLCData != null && OHLCData.size() > 0) {

            for (int i = OHLCData.size() - 1; i >= 0; i--) {
                MarketChartData   oHLCEntity = OHLCData.get(i);
                high = oHLCEntity.getHighPrice();
                low = oHLCEntity.getLowPrice();
                close = oHLCEntity.getClosePrice();
                if(OHLCData.size()-i<n)
                {
                    for(int j=0; j<OHLCData.size()-i; j++)
                    {
                        MarketChartData oHLCEntity1 = OHLCData.get(i+j);
                        high = high > oHLCEntity1.getHighPrice() ? high : oHLCEntity1.getHighPrice();
                        low = low < oHLCEntity1.getLowPrice() ? low : oHLCEntity1.getLowPrice();
                    }
                }else
                {
                    for(int j=0; j<n; j++)
                    {
                        MarketChartData oHLCEntity1 = OHLCData.get(i+j);
                        high = high > oHLCEntity1.getHighPrice() ? high : oHLCEntity1.getHighPrice();
                        low = low < oHLCEntity1.getLowPrice() ? low : oHLCEntity1.getLowPrice();
                    }
                }
                if (high != low) {
                    rs = (close - low) / (high - low) * 100;
                    r.add(rs);
                }
            }
            for (int i = r.size() - 1; i >= 0; i--) {
                rsv.add(r.get(i));
            }
        }
    }
    public List<Double> getRSV() {
        return rsv;
    }
}

