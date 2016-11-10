package zhangliang.view.android.kdemo.tool;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SystemConfig implements Serializable {

    public final static String SUCCESS = "1000";
    public static SystemConfig instance;


    public String BTC123_API_URL="https://trans.chbtc.com/api/m/V1_7/";


    public final static SystemConfig shareInstance() {
        if (instance == null) {
            instance = new SystemConfig();
        }
        return instance;
    }
    public String getApiUrl(){
        String url=BTC123_API_URL;
        return url;
    }
    /**
     * 根据市场卖买行情数据
     *
     * currencyType	 货币类型
     * exchangeType  兑换货币类型
     * step
     * size
     *
     */

    public static List<String> getHttpIndexMarketChart(){
        List<String> param=new ArrayList<String>();
        param.add("currencyType");
        param.add("exchangeType");
        param.add("step");
        param.add("size");
        return param;
    }
}
