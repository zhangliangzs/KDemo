package zhangliang.view.android.kdemo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import rx.Subscriber;
import zhangliang.view.android.kdemo.R;
import zhangliang.view.android.kdemo.adapters.NormalSpinerAdapter;
import zhangliang.view.android.kdemo.entity.BTC123MarketData;
import zhangliang.view.android.kdemo.entity.ChartData;
import zhangliang.view.android.kdemo.entity.MarketDataResult;
import zhangliang.view.android.kdemo.tool.HttpMethods;
import zhangliang.view.android.kdemo.tool.SubscriberOnNextListener;
import zhangliang.view.android.kdemo.view.SpinerPopWindow;
import zhangliang.view.android.klibrary.entity.MarketChartData;
import zhangliang.view.android.klibrary.view.KView;

/**
 * zhangliang
 * 2016-01-04 09:10
 * 1179980505@qq.com
 * K线图
 */
public class KDiagramActivity extends Activity implements View.OnClickListener {
    private TextView mTexthight, mTextPrice, mTextLow, mTextVol, mTextRate;
    private ImageView mImageView;
    private KView mMyChartsView;
    private String currencyType, currencyType_dw, exchangeType = "CNY";
    private String kChartTimeInterval = "900";                       //图表数据间隔
    private final String kChartDataSize = "1440";                          //图表数据总条数
    private Double lastPrice, price = 0.0;
    private ImageView mChangeScreen;
    //定时器
    Timer timer1 = null;  //刷新定时器
    Timer timer2 = null;  //刷新定时器
    private final int dataRefreshTime1 = 4 * 1000;                         //数据刷新间隔
    private final int dataRefreshTime2 = 60 * 1000;                         //数据刷新间隔
    String type = "chbtcbtccny";
    private LinearLayout title_lay;
    private RadioGroup myRadioGroup;
    private HorizontalScrollView mHorizontalScrollView;// 上面的水平滚动控件
    private List<Map<String, Object>> titleList = new ArrayList<Map<String, Object>>();
    private Context mContext;
    private Button spinerButton;
    private boolean islandscape = false;
    List<String> list = new ArrayList<String>();
    private SubscriberOnNextListener indexMarketChartOnNext;
    private List<MarketChartData> marketChartDataLists = new ArrayList<MarketChartData>();
    private SpinerPopWindow mSpinerPopWindow;
    private ArrayList data_list;
    private Resources res;
    private ArrayList symbol_str;
    private int ConfigurationType = Configuration.ORIENTATION_PORTRAIT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 设置显示的视图
        setContentView(R.layout.index_kdiagram);

        mContext = this;
        res = this.getResources();
        initView();
        initGroup();

    }

    private void initView() {

        Intent intent = getIntent();
        currencyType = "BTC";
        currencyType_dw = intent.getStringExtra("currencyType_dw");

        mMyChartsView = (KView) findViewById(R.id.my_charts_view);

        mImageView = (ImageView) findViewById(R.id.image);
        mTexthight = (TextView) findViewById(R.id.hight);
        mTextPrice = (TextView) findViewById(R.id.price);
        mChangeScreen = (ImageView) findViewById(R.id.changeScreen);
        mChangeScreen.setOnClickListener(this);
        mTextLow = (TextView) findViewById(R.id.low);
        mTextVol = (TextView) findViewById(R.id.vol);
        mTextRate = (TextView) findViewById(R.id.rate);


        if (currencyType.equals("BTC")) {
            type = "chbtcbtccny";

        }
        spinerButton = (Button) findViewById(R.id.spinerButton);
        spinerButton.setOnClickListener(this);
        initSpinner();

    }

    /**
     * 生成图表显示的数据
     *
     * @param marketChartDatas
     */
    private void createChartDataSet(String[][] marketChartDatas) {
        if (marketChartDatas != null && marketChartDatas.length > 0) {
            marketChartDataLists.clear();
            for (int i = 0; i < marketChartDatas.length; i++) {
                String[] data = marketChartDatas[i];

                MarketChartData mMarketChartData = new MarketChartData();
                mMarketChartData.setTime(Long.parseLong(data[0]));
                mMarketChartData.setOpenPrice(Double.parseDouble(data[3]));
                mMarketChartData.setClosePrice(Double.parseDouble(data[4]));
                mMarketChartData.setHighPrice(Double.parseDouble(data[5]));
                mMarketChartData.setLowPrice(Double.parseDouble(data[6]));
                mMarketChartData.setVol(Double.parseDouble(data[7]));
                marketChartDataLists.add(mMarketChartData);
            }
            //更新图表

            mMyChartsView.setOHLCData(marketChartDataLists);
            mMyChartsView.postInvalidate();
        }
    }


    private void showSpinWindow() {

        mSpinerPopWindow.setWidth(spinerButton.getWidth());
        mSpinerPopWindow.showAtLocation(spinerButton, Gravity.BOTTOM | Gravity.RIGHT, 0, spinerButton.getHeight());
    }

    private void initSpinner() {


        //数据
        data_list = new ArrayList<String>();
        data_list.add(res.getString(R.string.close_quota));
        data_list.add("MACD");
        data_list.add("KDJ");

        NormalSpinerAdapter mAdapter = new NormalSpinerAdapter(this);
        mAdapter.refreshData(data_list, 0);

        mSpinerPopWindow = new SpinerPopWindow(this);

        mSpinerPopWindow.setAdatper(mAdapter);
        mSpinerPopWindow.setItemListener(new SpinerPopWindow.IOnItemSelectListener() {
            @Override
            public void onItemClick(int pos) {
                if (pos >= 0 && pos <= data_list.size()) {
                    String value = data_list.get(pos).toString();

                    switch (pos) {
                        case 0:
                            mMyChartsView.setClose();
                            spinerButton.setText(R.string.specifications);
                            break;
                        case 1:
                            mMyChartsView.setMACDShow();
                            spinerButton.setText(value.toString());
                            break;
                        case 2:
                            mMyChartsView.setKDJShow();
                            spinerButton.setText(value.toString());
                            break;
                    }
                }
            }
        });
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initGroup() {


        Map<String, Object> map = new HashMap<String, Object>();
        map = new HashMap<String, Object>();
        map.put("id", "1");
        map.put("title", res.getString(R.string.one_min));
        map.put("time", 1 * 60 + "");
        titleList.add(map);
        map = new HashMap<String, Object>();
        map.put("id", "2");
        map.put("title", res.getString(R.string.five_min));
        map.put("time", 5 * 60 + "");
        titleList.add(map);
        map = new HashMap<String, Object>();
        map.put("id", "3");
        map.put("title", res.getString(R.string.fifteen_min));
        map.put("time", 15 * 60 + "");
        titleList.add(map);
        map = new HashMap<String, Object>();
        map.put("id", "4");
        map.put("title", res.getString(R.string.thirty_min));
        map.put("time", 30 * 60 + "");
        titleList.add(map);
        map = new HashMap<String, Object>();
        map.put("id", "5");
        map.put("title", res.getString(R.string.one_hour));
        map.put("time", 60 * 60 + "");
        titleList.add(map);
        map = new HashMap<String, Object>();
        map.put("id", "6");
        map.put("title", res.getString(R.string.two_hour));
        map.put("time", 2 * 60 * 60 + "");
        titleList.add(map);
        map = new HashMap<String, Object>();
        map.put("id", "7");
        map.put("title", res.getString(R.string.four_hour));
        map.put("time", 4 * 60 * 60 + "");
        titleList.add(map);
        map = new HashMap<String, Object>();
        map.put("id", "8");
        map.put("title", res.getString(R.string.six_hour));
        map.put("time", 6 * 60 * 60 + "");
        titleList.add(map);
        map = new HashMap<String, Object>();
        map.put("id", "9");
        map.put("title", res.getString(R.string.twelve_hour));
        map.put("time", 12 * 60 * 60 + "");
        titleList.add(map);
        map = new HashMap<String, Object>();
        map.put("id", "10");
        map.put("title", res.getString(R.string.one_day));
        map.put("time", 24 * 60 * 60 + "");
        titleList.add(map);
        map = new HashMap<String, Object>();
        map.put("id", "11");
        map.put("title", res.getString(R.string.three_day));
        map.put("time", 3 * 24 * 60 * 60 + "");
        titleList.add(map);
        map = new HashMap<String, Object>();
        map.put("id", "12");
        map.put("title", res.getString(R.string.one_week));
        map.put("time", 7 * 24 * 60 * 60 + "");
        titleList.add(map);

        title_lay = (LinearLayout) findViewById(R.id.title_lay);
        mHorizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
        myRadioGroup = new RadioGroup(mContext);
        myRadioGroup.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        myRadioGroup.setOrientation(LinearLayout.HORIZONTAL);
        title_lay.addView(myRadioGroup);
        for (int i = 0; i < titleList.size(); i++) {
            Map<String, Object> map1 = titleList.get(i);
            RadioButton radio = new RadioButton(mContext);
            radio.setButtonDrawable(R.color.kViewztblack);
            LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT,
                    Gravity.CENTER);
            radio.setLayoutParams(l);
            radio.setGravity(Gravity.CENTER);
            radio.setPadding(20, 20, 20, 20);
            // radio.setPadding(left, top, right, bottom)
            radio.setId(1 + i);
            radio.setText(map1.get("title") + "");
            if (i == 2) {
                radio.setTextColor(getResources().getColor(R.color.subject_select));
                radio.setChecked(true);
                radio.setBackground(getResources().getDrawable(R.drawable.bg_bottom));
            } else {
                radio.setTextColor(getResources().getColor(R.color.kViewztblack));
                radio.setChecked(false);
                radio.setBackground(null);
            }

            radio.setTag(map1);
            myRadioGroup.addView(radio);
        }
        myRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioButtonId = group.getCheckedRadioButtonId();
                // 根据ID获取RadioButton的实例
                RadioButton rb = (RadioButton) findViewById(radioButtonId);
                for (int i = 0; i < group.getChildCount(); i++) {
                    RadioButton vradio = (RadioButton) group.getChildAt(i);
                    vradio.setGravity(Gravity.CENTER);
                    vradio.setPadding(20, 20, 20, 20);
                    if (rb.getId() == vradio.getId()) {
                        vradio.setTextColor(getResources().getColor(R.color.subject_select));
                        vradio.setChecked(true);
                        vradio.setBackground(getResources().getDrawable(R.drawable.bg_bottom));
                    } else {
                        vradio.setTextColor(getResources().getColor(R.color.kViewztblack));
                        vradio.setChecked(false);
                        vradio.setBackground(null);
                    }

                }
                //           CustomProgress.show(mContext, true, null);
                kChartTimeInterval = titleList.get(checkedId - 1).get("time") + "";
                indexMarketChart();


            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.spinerButton:
                showSpinWindow();
                break;

            case R.id.changeScreen:
                if (ConfigurationType == Configuration.ORIENTATION_PORTRAIT) {

                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else {

                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
                break;
        }
    }

    private void indexMarketChart() {
        list.clear();
        list.add(currencyType);
        list.add("CNY");
        list.add(kChartTimeInterval);
        list.add(kChartDataSize);

        Subscriber progressSubscriber = new Subscriber<ChartData>(){
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ChartData chartData1) {
                if (chartData1 != null && chartData1.getChartData().length > 0) {
                    createChartDataSet(chartData1.getChartData());
                }
            }
        };
        HttpMethods.getInstance().indexMarketChart(progressSubscriber, list);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }


    private void getMarketDataByWebservice() {
        Subscriber subscriber = new Subscriber<MarketDataResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(MarketDataResult marketDataResult) {
                if (marketDataResult.isSuc()) {
                    BTC123MarketData btc123MarketData = marketDataResult.getDatas();
                    btc123MarketData.getSymbol();
                    setTextView(btc123MarketData);
                }
            }
        };
        HttpMethods.getInstance().tickers(subscriber, type);
    }

    public void setTextView(BTC123MarketData btc123MarketData) {

        String str_rate = btc123MarketData.getTicker().getRiseRate();
        String str_clo = "#1BA905";
        Drawable drawable;
        if (str_rate.indexOf("-") != -1) {
            drawable = getResources().getDrawable(R.mipmap.ico_down2_green);
            /// 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mTextRate.setText(btc123MarketData.getTicker().getRiseRate() + "%");
            mTextPrice.setTextColor(Color.parseColor("#1BA905"));
        } else {
            drawable = getResources().getDrawable(R.mipmap.ico_up2_red);
            /// 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mTextRate.setText("+" + btc123MarketData.getTicker().getRiseRate() + "%");
            mTextPrice.setTextColor(Color.parseColor("#E70101"));
        }

        mImageView.setImageDrawable(drawable);
        mTexthight.setText(btc123MarketData.getTicker().getHigh());
        mTextPrice.setText("￥" + btc123MarketData.getTicker().getLast());
        mTextLow.setText(btc123MarketData.getTicker().getLow());
        mTextVol.setText(btc123MarketData.getTicker().getVol());

    }

    @Override
    public void onPause() {
        super.onPause();
        this.stopTimer();
    }

    @Override
    public void onResume() {
        super.onResume();
        startTimer();

    }

    private void setImageViewBackg() {
        Resources res = this.getResources();
        if (ConfigurationType == Configuration.ORIENTATION_LANDSCAPE) {
            mChangeScreen.setImageDrawable(res.getDrawable(R.mipmap.swift_screen_a));
        } else {
            mChangeScreen.setImageDrawable(res.getDrawable(R.mipmap.swift_screen_b));
        }
    }

    Handler handlerOfTrans = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                getMarketDataByWebservice();
            } else if (msg.what == 2) {
                indexMarketChart();
            }
            super.handleMessage(msg);
        }

        ;
    };

    /**
     * 启动定时器
     */
    public void startTimer() {
        if (timer1 == null) {
            Log.i("KD_startTimer", "KD_startTimer+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            timer1 = new Timer();
            TransTimerTask task1 = new TransTimerTask(handlerOfTrans, 1);
            timer1.schedule(task1, 0, dataRefreshTime1);
        }
        if (timer2 == null) {
            Log.i("KD_startTimer", "KD_startTimer+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            timer2 = new Timer();
            TransTimerTask task2 = new TransTimerTask(handlerOfTrans, 2);
            timer2.schedule(task2, 0, dataRefreshTime2);
        }
    }

    /**
     * 停止定时器
     */
    public void stopTimer() {
        if (timer1 != null) {
            Log.i("KD_stoptimer", "KD_stoptimer+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            timer1.cancel();
            timer1 = null;
        }
        if (timer2 != null) {
            Log.i("KD_stoptimer", "KD_stoptimer+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            timer2.cancel();
            timer2 = null;
        }
    }

    /**
     * 定时器任务
     */
    class TransTimerTask extends TimerTask {
        private int code;
        private Handler mHandler;

        public TransTimerTask() {

        }

        public TransTimerTask(Handler handler, int code) {
            this.mHandler = handler;
            this.code = code;
        }

        @Override
        public void run() {
            // 需要做的事:发送消息
            Message message = new Message();
            message.what = code;
            this.mHandler.sendMessage(message);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int type = this.getResources().getConfiguration().orientation;

        if (type == Configuration.ORIENTATION_LANDSCAPE) {

            ConfigurationType = Configuration.ORIENTATION_LANDSCAPE;

        } else if (type == Configuration.ORIENTATION_PORTRAIT) {

            ConfigurationType = Configuration.ORIENTATION_PORTRAIT;
        }
        setImageViewBackg();
    }

}