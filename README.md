# KDemo
专业k线图表组件，支持JDK,MACD,MA，VOL等指标，后续指标陆续更新

![image](https://github.com/zhangliangzs/KDemo/blob/master/photo/device.png)

android studio开发工具中：
第一步：在工程build.gradle中添加如下代码

allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
第二步：在dependency，添加如下代码

dependencies {
	        compile 'com.github.zhangliangzs:KDemo:1.0.0'
	}
  


项目应用：
在布局文件中添加组件：
<zhangliang.view.android.klibrary.view.KView
        android:id="@+id/kview"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />


   activity中找到控件：
    KView mMyChartsView = (KView) findViewById(R.id.kview);

    最主要的是数据的显示，数据mode的封装，主要封装在MarketChartData中：结构如下：
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

new 个数组装载model数据：
private List<MarketChartData> marketChartDataLists = new ArrayList<MarketChartData>();
你的activity界面请求json数据,封装在这个模型中，在代码中添加

      mMyChartsView.setOHLCData(marketChartDataLists);
      mMyChartsView.postInvalidate();

      就能将图表数据显示出来


      主要方法：
            mMyChartsView.setClose();//关闭指标
            mMyChartsView.setMACDShow();//显示macd
            mMyChartsView.setKDJShow();//显示kdj

如果有疑问请加QQ：1179980507，一起完善和做好这个控件

IOS版地址：
https://github.com/zhiquan911/CHKLineChart


  效果如下图：
![image](https://github.com/zhangliangzs/KDemo/blob/master/photo/deviceetc.png)

  
