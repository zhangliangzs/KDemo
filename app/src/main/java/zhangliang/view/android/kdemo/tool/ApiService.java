package zhangliang.view.android.kdemo.tool;



import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import rx.Observable;
import zhangliang.view.android.kdemo.entity.ChartData;
import zhangliang.view.android.kdemo.entity.MarketDataResult;

/**

 */
public interface ApiService {
    /**
     * 行情
     */
    //获取行情

    @GET("getTicker")
    Observable<MarketDataResult> getTicker(@QueryMap Map<String, String> config);

    //获取行情盘口单子
    @GET("indexMarketChart")
    Observable<HttpResult<ChartData>> indexMarketChart(@QueryMap Map<String, String> config);

}