package zhangliang.view.android.klibrary.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangliang on 16/11/5.
 * QQ:1179980507
 */
public class EMAEntity {
    private List<Double> EMAs;

    public EMAEntity(List<MarketChartData> OHLCData, int n)
    {




        EMAs = new ArrayList<Double>();
        if (OHLCData != null && OHLCData.size() > 0) {
            double close = 0;
            double EMA12 = 0.0;
            MarketChartData oHLCEntity;
            for (int i = 0; i <= OHLCData.size() - 1; i++) {
                close = OHLCData.get(i).getClosePrice();
                oHLCEntity = OHLCData.get(i);
                if (i ==0) {
                    EMA12 = oHLCEntity.getClosePrice();
                    EMAs.add(EMA12);
                }else
                {
                    EMA12 = EMAs.get(EMAs.size()-1)*(n-1)/(n+1)+close*(2)/(n+1);
                    EMAs.add(EMA12);
                }
            }

        }

  /*      EMAs = new ArrayList<Double>();
        if (OHLCData != null && OHLCData.size() > 0) {
            double close = 0;
            double EMA12 = 0.0;
            MarketChartData oHLCEntity;
            for (int i = OHLCData.size() - 1; i >= 0; i--) {
                close = OHLCData.get(i).getClosePrice();
                oHLCEntity = OHLCData.get(i);
                if (i ==OHLCData.size() - 1) {
                    EMA12 = oHLCEntity.getClosePrice();
                    EMAs.add(EMA12);
                }else
                {
                    EMA12 = EMAs.get(EMAs.size()-1)*(n-1)/(n+1)+close*(2)/(n+1);
                    EMAs.add(EMA12);
                }
            }

        }*/
    }
    public List<Double> getnEMA() {
        return EMAs;
    }
}
