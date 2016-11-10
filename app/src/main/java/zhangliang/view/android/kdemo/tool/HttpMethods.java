package zhangliang.view.android.kdemo.tool;

import android.app.Activity;
import android.util.Log;


import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import zhangliang.view.android.kdemo.entity.ChartData;
import zhangliang.view.android.kdemo.entity.MarketDataResult;


public class HttpMethods {
    public static String BASE_URL =SystemConfig.shareInstance().getApiUrl();

    //超时时间60s
    private static final int DEFAULT_TIMEOUT = 60;

    private Retrofit retrofit;
    private ApiService apiService;

    //构造方法私有
    private HttpMethods() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        Log.e("BASE_URL",BASE_URL);
        apiService = retrofit.create(ApiService.class);
    }
    //在访问HttpMethods时创建单例
    private static class SingletonHolder{
        private static final HttpMethods INSTANCE = new HttpMethods();
    }


    //获取单例
    public static HttpMethods getInstance(){
        return SingletonHolder.INSTANCE;
    }

    public Map<String,String> paramPut(List<String> map, List<String> list){
        Map<String,String> param=new HashMap<String,String>();
        int select_index=0;
        for (String str:map) {
            try {
                param.put(str, list.get(select_index));
                select_index++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return param;

    }

    /**
     * 获取首页行情图表
     */
    public void indexMarketChart(Subscriber<HttpResult<ChartData>> subscriber, List<String> list){
        List<String> map= SystemConfig.getHttpIndexMarketChart();
        Map<String,String> param=paramPut(map,list);
        Observable observable = apiService.indexMarketChart(param).map(new HttpResultFunc());;
        toSubscribe(observable, subscriber);

    }

    /**
     * 获取行情
     */
    public void tickers(Subscriber<MarketDataResult> subscriber, String symbol){
        Map<String,String> param=new HashMap<String,String>();
        param.put("symbol",symbol);
        Observable observable = apiService.getTicker(param).map(new HttpResultFunc());;
        toSubscribe(observable, subscriber);
    }

    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s){
         o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T>   Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {

        @Override
        public T call(final  HttpResult<T> httpResult) {

                return httpResult.getDatas();
            }


    }

}
