package zhangliang.view.android.klibrary.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import zhangliang.view.android.klibrary.R;
import zhangliang.view.android.klibrary.entity.KDJ;
import zhangliang.view.android.klibrary.entity.MACD;
import zhangliang.view.android.klibrary.entity.MALineEntity;
import zhangliang.view.android.klibrary.entity.MarketChartData;


/**
 * Created by zhangliang on 16/11/5.
 * QQ:1179980507
 */
public class KView extends GridChartKView {

    /** 触摸点 */
    private float mStartX;
    private float mStartY;

    /** 默认Y轴字体颜色 **/
    private static final int DEFAULT_AXIS_Y_TITLE_COLOR = Color.YELLOW;

    /** 默认X轴字体颜色 **/
    private static final int DEFAULT_AXIS_X_TITLE_COLOR = Color.RED;

    /** 显示的OHLC数据起始位置 */
    private int mDataStartIndext;

    /** 显示的OHLC数据个数 */
    private int mShowDataNum;
    private int showNum = 0;

    /** 当前数据的最大最小值 */
    private double mMaxPrice;
    private double mMinPrice;
    /** 成交量最大值 */
    private double mMaxVol;

    //记录最小值
    private int minIndex;
    //记录最大值
    private int maxIndex;

    private float olddistance = 0f;
    private int count = 0;

    private boolean mShowMACD = false;
    private boolean mShowJKD = false;

    /** 显示纬线数 */
    private int latitudeNum = super.DEFAULT_UPER_LATITUDE_NUM;

    /** 显示经线数 */
    private int longtitudeNum = super.DEFAULT_LOGITUDE_NUM;

    /** 显示的最小Candle数 */
    private final static int MIN_CANDLE_NUM = 10;
    /** 显示的最大Candle数 */
    private final static int MAX_CANDLE_NUM = 480;

    /** 默认显示的Candle数 */
    private final static int DEFAULT_CANDLE_NUM = 240;

    /** 最小可识别的移动距离 */
    private final static int MIN_MOVE_DISTANCE = 55;

    /** Candle宽度 */
    private double mCandleWidth;
    /** MA数据 */
    private List<MALineEntity> MALineData;
    /** OHLC数据 */
    private List<MarketChartData> mOHLCData=new ArrayList<MarketChartData>();


    /** 默认五日均线颜色 **/
    public static int kline5dayline = 0x535d66;
    /** 默认十日均线颜色 **/
    public static int kline10dayline = 0x535d66;
    /** 默认30日均线颜色 **/
    public static int kline30dayline = 0x535d66;

    public static int klineRed = 0xCD1A1E;
    public static int klineGreen = 0x7AA376;

    /** 量能均线数组*/
    private List<MALineEntity> lineData;

    private Context mConext;
    private MACD mMACDData;
    private KDJ mKDJData;
    private Resources res;
    public KView(Context context) {
        super(context);
        mConext = context;
        res = mConext.getResources();
        init();
    }

    public KView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mConext = context;
        res = mConext.getResources();
        init();
    }

    public KView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mConext = context;
        res = mConext.getResources();
        init();
    }
    private void init() {
        mShowDataNum = DEFAULT_CANDLE_NUM;
        mDataStartIndext = 0;
        mMaxPrice = 0;
        mMinPrice = 0;
        mShowMACD = true;
        mShowJKD = false;
        kline5dayline=res.getColor(R.color.kline5dayline);
        kline10dayline=res.getColor(R.color.kline10dayline);
        kline30dayline=res.getColor(R.color.kline30dayline);
        klineRed=res.getColor(R.color.klinered);
        klineGreen=res.getColor(R.color.klinegreen);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        initAxisX();
        initAxisY();
        super.onDraw(canvas);
        drawUpperRegion(canvas);
        drawSticks(canvas);
        drawMA(canvas);
        if(mShowMACD)
        {
            drawMACD(canvas);
        }
        if (mShowJKD){
            drawKDJ(canvas);
        }
        drawWithFingerClick(canvas);
    }
    /**
     ma5,ma10,ma30均线
     */

    public void drawMAText(List<MALineEntity> MALineData, Canvas canvas, int index,int k5, int k10, int k30) {
        String ma5text = "MA5:"+new DecimalFormat("#.##").format(MALineData.get(0).getLineData().get(index));
        String ma10text = "MA10:"+new DecimalFormat("#.##").format(MALineData.get(1).getLineData().get(index));
        String ma30text = "MA30:"+new DecimalFormat("#.##").format(MALineData.get(2).getLineData().get(index));
        Paint ma5 = new Paint();
        ma5.setColor(k5);
        ma5.setAntiAlias(true);
        ma5.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
        canvas.drawText(ma5text, 2f, 2*TITLE_HEIGHT-2f, ma5);



        float tWidth = ma5.measureText(ma5text);
        Paint ma10 = new Paint();
        ma10.setColor(k10);
        ma10.setAntiAlias(true);
        ma10.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
        canvas.drawText(ma10text, 2f+tWidth, 2*TITLE_HEIGHT-2f, ma10);


        float width = ma10.measureText(ma10text);
        Paint ma30 = new Paint();
        ma30.setColor(k30);
        ma30.setAntiAlias(true);
        ma30.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
        canvas.drawText(ma30text, 2f+tWidth+width, 2*TITLE_HEIGHT-2f, ma30);
    }
    /*
     * 单点击事件
     */
    @Override
    protected void drawWithFingerClick(Canvas canvas) {
        int index = getSelectedIndex();

        try{
            float y = super.getTouchPoint().y;
            //纠正出界
            if(y<super.getTitleHeight()+UPER_CHART_MARGIN_TOP)
            {
                y = super.getTitleHeight()+UPER_CHART_MARGIN_TOP;
            }
            if(y>super.getTitleHeight()+UPER_CHART_MARGIN_TOP+super.getUperChartHeight())
            {
                y = super.getTitleHeight()+UPER_CHART_MARGIN_TOP+super.getUperChartHeight();
            }
        if (super.getTouchPoint()!=null||mOHLCData != null && mOHLCData.size() > 0) {
            setAxisXClickTitle(String.valueOf(mOHLCData.get(mShowDataNum-1-index+mDataStartIndext).getTime3()));

            float roate = 1-(y  -super.getTitleHeight()-UPER_CHART_MARGIN_TOP)/super.getUperChartHeight();

            setAxisYClickTitle(roate*(mMaxPrice-mMinPrice)+mMinPrice+"");

            float startX = (float) ( mCandleWidth * (index+1) - mCandleWidth/2);
            PointF piont  = new PointF(startX,y);
            super.setTouchPoint(piont);
        }
            super.drawAlphaTopTextBox(res.getString(R.string.open)+mOHLCData.get(mShowDataNum-1-index+mDataStartIndext).getOpenPrice()+res.getString(R.string.high)+mOHLCData.get(mShowDataNum-1-index+mDataStartIndext).getHighPrice()+
                    res.getString(R.string.low)+mOHLCData.get(mShowDataNum-1-index+mDataStartIndext).getLowPrice()+res.getString(R.string.close)+mOHLCData.get(mShowDataNum-1-index+mDataStartIndext).getClosePrice()+res.getString(R.string.vol)+mOHLCData.get(mShowDataNum-1-index+mDataStartIndext).getVol(),canvas);
            drawMAText(MALineData,canvas,mShowDataNum-1-index+mDataStartIndext,kline5dayline,kline10dayline,kline30dayline);
            drawAlphaMiddleTextBox(res.getString(R.string.vol)+mOHLCData.get(mShowDataNum-1-index+mDataStartIndext).getVol(),canvas);
            if(mMACDData!=null&&mShowMACD)
            {
                drawAlphaBottomTextBox("MACD(12,26,9) DIF:"+ new DecimalFormat("#.##").format(mMACDData.getDIF().get(mShowDataNum-1-index+mDataStartIndext))+" DEA:"+new DecimalFormat("#.##").format(mMACDData.getDEA().get(mShowDataNum-1-index+mDataStartIndext))
                        +"MACD:"+new DecimalFormat("#.##").format(mMACDData.getBAR().get(mShowDataNum-1-index+mDataStartIndext)),canvas);
            }

            if(mKDJData!=null&&mShowJKD)
            {
                drawAlphaBottomTextBox("KDJ(9,3,3) K:"+ new DecimalFormat("#.##").format(mKDJData.getK().get(mShowDataNum-1-index+mDataStartIndext))+" D:"+new DecimalFormat("#.##").format(mKDJData.getD().get(mShowDataNum-1-index+mDataStartIndext))+"J:"+new DecimalFormat("#.##").format(mKDJData.getJ().get(mShowDataNum-1-index+mDataStartIndext)),canvas);
            }
        }catch(Exception e){

        }
        super.drawWithFingerClick(canvas);
    }
    public int getSelectedIndex() {
        if(null == super.getTouchPoint()){
            return 0;
        }
        float graduate = Float.valueOf(super.getAxisXGraduate(super.getTouchPoint().x));
        int index = (int) Math.floor(graduate*mShowDataNum);

        if(index >= mShowDataNum){
            index = mShowDataNum -1;
        }else if(index < 0){
            index = 0;
        }

        return index;
    }


/*版本2*/
   @Override
    public boolean onTouchEvent(MotionEvent event) {
        PointF point = null;
        if (event.getPointerCount() == 1) {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    mStartX = event.getX();
                    mStartY = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mOHLCData == null || mOHLCData.size() <= 0) {
                        return true;
                    }
                    float horizontalSpacing = event.getX() - mStartX;
                   if (Math.floor(Math.abs(horizontalSpacing)) < mCandleWidth) {
                        point = new PointF(mStartX, mStartY);
                        super.setTouchPoint(point);
                        postInvalidate();
                            return true;
                        }
                    int mMoveNum = (int) Math.floor(Math.abs(horizontalSpacing) / mCandleWidth);
                    if(mMoveNum==0)
                        mMoveNum=1;

                    mStartX = event.getX();
                    mStartY = event.getY();
                    if (horizontalSpacing < 0) {
                        mDataStartIndext = mDataStartIndext - mMoveNum;
                        if (mDataStartIndext < 0) {
                            mDataStartIndext = 0;
                        }
                    } else if (horizontalSpacing > 0) {
                        mDataStartIndext = mDataStartIndext + mMoveNum;
                        if (mDataStartIndext + mShowDataNum > mOHLCData.size()) {
                            mDataStartIndext = mOHLCData.size() - mShowDataNum;
                        }
                    }
                    point = new PointF(mStartX, mStartY);
                    super.setTouchPoint(point);
                    setCurrentData();
                    postInvalidate();
            }
        } else if (event.getPointerCount() == 2) {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_MOVE:
                    mStartX = event.getX();
                    mStartY = event.getY();
                    point = new PointF(mStartX, mStartY);
                    super.setTouchPoint(point);
                    if (count == 0) {
                        olddistance = spacing(event);
                        count++;
                    } else {
                        count = 0;
                        if (spacing(event) - olddistance < 0) {
                            zoomIn((int) (spacing(event) / mCandleWidth));
                        } else {
                            zoomOut((int) (spacing(event) / mCandleWidth));
                        }
                        setCurrentData();
                    }
                    postInvalidate();

                    break;
            }
        }
        return true;
    }
    //缩小
    private void zoomIn(int move) {

        if(mShowDataNum+mDataStartIndext>=mOHLCData.size())
            return;
        mShowDataNum=mShowDataNum+move;
        if (mShowDataNum > mOHLCData.size()) {
          //  mShowDataNum = MIN_CANDLE_NUM > mOHLCData.size() ? MIN_CANDLE_NUM : mOHLCData.size();
            mShowDataNum = mOHLCData.size();
        }
        if(mShowDataNum>MAX_CANDLE_NUM)
        {
            mShowDataNum=MAX_CANDLE_NUM;
        }

    }

    //放大
    private void zoomOut(int move) {
        mShowDataNum=mShowDataNum-move;
        if (mShowDataNum < MIN_CANDLE_NUM) {
            mShowDataNum = MIN_CANDLE_NUM;
        }

    }

    // 计算移动距离
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private void drawMA(Canvas canvas) {
        if (MALineData == null || MALineData.size() < 0)
            return;

        double rate = (getUperChartHeight()) / (mMaxPrice - mMinPrice);
        // 绘制上部曲线图及上部分MA值
        for (int j = 0; j < MALineData.size(); j++) {
            MALineEntity lineEntity = MALineData.get(j);

            float startX = 0;
            float startY = 0;
            Paint paint = new Paint();
            paint.setStrokeWidth(2);
            paint.setColor(lineEntity.getLineColor());
            paint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);

            for (int i = 0; i < mShowDataNum
                    && mDataStartIndext + i < lineEntity.getLineData().size(); i++) {
                if (i != 0) {
                   canvas.drawLine(
                            startX,
                            startY>MIDDLE_CHART_TOP-2*TITLE_HEIGHT?MIDDLE_CHART_TOP-2*TITLE_HEIGHT:startY,
                            (float) (super.getWidth() - super.DEFAULT_AXIS_MARGIN_RIGHT - 2 - mCandleWidth*i - mCandleWidth * 0.5f),
                            (float) (((mMaxPrice - lineEntity.getLineData().get(mDataStartIndext + i)) * rate)+TITLE_HEIGHT+UPER_CHART_MARGIN_TOP)>MIDDLE_CHART_TOP-2*TITLE_HEIGHT?MIDDLE_CHART_TOP-2*TITLE_HEIGHT:(float) ((mMaxPrice - lineEntity.getLineData().get(mDataStartIndext + i)) * rate)+TITLE_HEIGHT+UPER_CHART_MARGIN_TOP,
                            paint);


                }

                startX = (float) (super.getWidth() - super.DEFAULT_AXIS_MARGIN_RIGHT - 2 - mCandleWidth*i - mCandleWidth * 0.5f);

                startY = (float) ((mMaxPrice - lineEntity.getLineData().get(mDataStartIndext + i)) * rate) +TITLE_HEIGHT+UPER_CHART_MARGIN_TOP;


            }
        }
    }

    /**
     * 初始化X轴
     */
    protected void initAxisX() {

            if (mOHLCData == null || mOHLCData.size() <= 0) {
                return;
            }
            List<String> TitleX = new ArrayList<String>();
        try {
            if(null != mOHLCData){
                float average = mShowDataNum / longtitudeNum;
                average = Float.parseFloat(new DecimalFormat("00.00").format(average));
                //�?��刻度
                int index = 0;
                for (int i = 0; i < longtitudeNum && mDataStartIndext + i < mOHLCData.size(); i++) {
                    index = (int) Math.floor(i * average);
                    if(index > mShowDataNum-1){
                        index = mShowDataNum-1;
                    }
                    if(index+mDataStartIndext<mOHLCData.size()&&(index+mDataStartIndext)>=0)
                        TitleX.add(String.valueOf(mOHLCData.get(index+mDataStartIndext).getTime2()));
                }
                if(index+mDataStartIndext<mOHLCData.size())
                    TitleX.add(String.valueOf(mOHLCData.get(index+mDataStartIndext).getTime2()));
            }
        }catch (Exception e)
        {
        }

        super.setAxisXTitles(TitleX);
    }

    /**
     * 初始化Y轴
     */
    protected void initAxisY() {
        if (mOHLCData == null || mOHLCData.size() <= 0) {
            return;
        }
        List<String> TitleY = new ArrayList<String>();
        float average = (float) ((mMaxPrice - mMinPrice) /latitudeNum )/10 * 10;
        average = Float.parseFloat(new DecimalFormat("0000.00").format(average));
        //处理所有Y刻度
        for (float i = 0; i <latitudeNum; i++) {
            String value = String.valueOf(mMinPrice + i * average);
            if(value.length() < super.getAxisYMaxTitleLength()){
                while(value.length() < super.getAxisYMaxTitleLength()){
                    value = new String(" ") + value;
                }
            }else if(value.length() > super.getAxisYMaxTitleLength())
            {
                value =value.substring(0,super.getAxisYMaxTitleLength());
            }
            TitleY.add(value);
        }
        //处理最大值
         String value = String.valueOf((mMaxPrice) / 10 * 10);
        if(value.length() < super.getAxisYMaxTitleLength()){
            while(value.length() < super.getAxisYMaxTitleLength()){
                value = new String(" ") + value;
            }
        }
        TitleY.add(value);
        super.setAxisYTitles(TitleY);
    }

    private void drawUpperRegion(Canvas canvas) {
        if (mOHLCData == null || mOHLCData.size() <= 0) {
            return;
        }

        // 绘制蜡烛图
        Paint redPaint = new Paint();
        redPaint.setStyle(Paint.Style.STROKE);
        redPaint.setColor(klineRed);

        Paint greenPaint = new Paint();
        greenPaint.setColor(klineGreen);


        // 绘最大最小值
        Paint paint = new Paint();
        paint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
        paint.setColor(super.getAxisColor());

        float width = getWidth()-super.DEFAULT_AXIS_MARGIN_RIGHT-2;
        mCandleWidth = width/ 10.0 * 10.0 / mShowDataNum;
        double rate = (getUperChartHeight()) / (mMaxPrice - mMinPrice);
        for (int i = 0; i < mShowDataNum && mDataStartIndext + i < mOHLCData.size(); i++) {
            MarketChartData entity = mOHLCData.get(mDataStartIndext + i);
            float open = (float) ((mMaxPrice - entity.getOpenPrice())*rate+TITLE_HEIGHT+UPER_CHART_MARGIN_TOP);
            float close = (float) ((mMaxPrice - entity.getClosePrice())*rate+TITLE_HEIGHT+UPER_CHART_MARGIN_TOP);
            float high = (float) ((mMaxPrice - entity.getHighPrice())*rate+TITLE_HEIGHT+UPER_CHART_MARGIN_TOP);
            float low = (float) ((mMaxPrice - entity.getLowPrice())*rate+TITLE_HEIGHT+UPER_CHART_MARGIN_TOP);

            float left = (float) (width - mCandleWidth * (i + 1));
            float right = (float) (width- mCandleWidth * i);
            float startX = (float) (width - mCandleWidth * i - mCandleWidth/2);

            if(open < close){
                canvas.drawRect(left+1, open, right-1, close, greenPaint);
                canvas.drawLine(startX, high, startX, low, greenPaint);
            }else if (open == close) {
                canvas.drawRect(left+1, close-1, right-1, open+1, redPaint);
                canvas.drawLine(startX, high, startX, low, redPaint);
            }else{
                canvas.drawRect(left+1, close, right-1, open, redPaint);
                canvas.drawLine(startX, high, startX, close, redPaint);
                canvas.drawLine(startX, open, startX,low , redPaint);
            }

            Rect rect = new Rect();
            //画最大和最小值
            if(mDataStartIndext + i==maxIndex)
            {
                String maxPrice = entity.getHighPrice()+"";
                paint.getTextBounds(maxPrice, 0, 1, rect);
               float w= paint.measureText(maxPrice);
                //左箭头
                if((i*mCandleWidth+mCandleWidth/2)>(w+50))
                {
                    canvas.drawLine(startX, high-super.getTitleHeight(), startX+50, high-super.getTitleHeight(), paint);
                    canvas.drawLine(startX, high-super.getTitleHeight(), startX+20, high-super.getTitleHeight()-20, paint);
                    canvas.drawLine(startX, high-super.getTitleHeight(), startX+20, high-super.getTitleHeight()+20, paint);
                    canvas.drawText(maxPrice,startX+50,high-super.getTitleHeight()+rect.height()/2, paint);
                }else
                {
                    canvas.drawLine(startX, high-super.getTitleHeight(), startX-50, high-super.getTitleHeight(), paint);
                    canvas.drawLine(startX, high-super.getTitleHeight(), startX-20, high-super.getTitleHeight()+20, paint);
                    canvas.drawLine(startX, high-super.getTitleHeight(), startX-20, high-super.getTitleHeight()-20, paint);
                    canvas.drawText(maxPrice,startX-50-w,high-super.getTitleHeight()+rect.height()/2, paint);
                }

            }
            if(mDataStartIndext + i==minIndex)
            {
                String minPrice =  entity.getLowPrice()+"";
                paint.getTextBounds(minPrice, 0, 1, rect);
                float w= paint.measureText(minPrice);
                //左箭头
                if((i*mCandleWidth+mCandleWidth/2)>(w+50)) {
                    canvas.drawLine(startX, low + super.getTitleHeight(), startX + 50, low + super.getTitleHeight(), paint);
                    canvas.drawLine(startX, low + super.getTitleHeight(), startX + 20, low + super.getTitleHeight() + 20, paint);
                    canvas.drawLine(startX, low + super.getTitleHeight(), startX + 20, low + super.getTitleHeight() - 20, paint);
                    canvas.drawText(minPrice, startX + 50, low + super.getTitleHeight() + rect.height() / 2, paint);
                }else
                {
                    canvas.drawLine(startX, low + super.getTitleHeight(), startX - 50, low + super.getTitleHeight(), paint);
                    canvas.drawLine(startX, low + super.getTitleHeight(), startX - 20, low + super.getTitleHeight() - 20, paint);
                    canvas.drawLine(startX, low + super.getTitleHeight(), startX - 20, low + super.getTitleHeight() + 20, paint);
                    canvas.drawText(minPrice, startX-50-w,low + super.getTitleHeight() + rect.height() / 2, paint);
                }
            }

        }
    }

    private void drawMACD(Canvas canvas)
    {

        float lowertop = LOWER_CHART_TOP + 1;
        float lowerHight = getLowerChartHeight();
        float viewWidth = getWidth()- super.DEFAULT_AXIS_MARGIN_RIGHT;
        float zero = 0;

        Paint whitePaint = new Paint();
        whitePaint.setColor(Color.RED);
        whitePaint.setStrokeWidth(2);
        Paint yellowPaint = new Paint();
        yellowPaint.setColor(Color.GREEN);
        yellowPaint.setStrokeWidth(2);
        Paint magentaPaint = new Paint();
        magentaPaint.setColor(Color.MAGENTA);
        magentaPaint.setStrokeWidth(2);
        Paint textPaint = new Paint();
        textPaint.setColor(super.getAxisColor());
        textPaint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);

        if(mMACDData==null)
            return;
        List<Double> DEA = mMACDData.getDEA();
        List<Double> DIF = mMACDData.getDIF();
        List<Double> BAR = mMACDData.getBAR();
        try{
            double low = DIF.get(mDataStartIndext);
            double high = low;
            double rate = 0.0;
           for (int i = mDataStartIndext; i < mDataStartIndext + mShowDataNum && i < BAR.size(); i++) {
                low = low < BAR.get(i) ? low : BAR.get(i);
                high = high > BAR.get(i) ? high : BAR.get(i);
                low = low < DIF.get(i) ? low : DIF.get(i);
                high = high > DIF.get(i) ? high : DIF.get(i);
               low = low < DEA.get(i) ? low : DEA.get(i);
               high = high > DEA.get(i) ? high : DEA.get(i);
            }
            if(Math.abs(low)>Math.abs(high))
            {
                rate = lowerHight / (2*Math.abs(low));
            }else
            {
                rate = lowerHight / (2*Math.abs(high));
            }
            Paint paint = new Paint();
             zero = LOWER_CHART_TOP+getLowerChartHeight()/2;
            // 绘制双线
            float dea = 0.0f;
            float dif = 0.0f;
            for (int i = mDataStartIndext; i < mDataStartIndext + mShowDataNum && i < BAR.size(); i++) {
                // 绘制矩形
                if (BAR.get(i) >= 0.0) {
                    paint.setColor(klineRed);
                    float top = (float) (zero-BAR.get(i)*rate) ;

                        canvas.drawRect(viewWidth - 1 - (float) mCandleWidth
                                * (i + 1 - mDataStartIndext), top, viewWidth - 2
                                - (float) mCandleWidth * (i - mDataStartIndext), zero, paint);
                } else {
                    paint.setColor(klineGreen);
                    float bottom = (float)(zero-BAR.get(i)*rate);
                        canvas.drawRect(viewWidth - 1 - (float) mCandleWidth
                                * (i + 1 - mDataStartIndext), zero, viewWidth - 2
                                - (float) mCandleWidth * (i - mDataStartIndext), bottom, paint);
                }
                if (i != mDataStartIndext) {
                    canvas.drawLine(viewWidth - 1 - (float) mCandleWidth
                                    * (i + 1 - mDataStartIndext) + (float) mCandleWidth / 2,
                            zero-(float) ((DEA.get(i)) * rate)  , viewWidth - 2
                                    - (float) mCandleWidth * (i - mDataStartIndext)
                                    + (float) mCandleWidth / 2, dea, whitePaint);

                    canvas.drawLine(viewWidth - 1 - (float) mCandleWidth
                                    * (i + 1 - mDataStartIndext) + (float) mCandleWidth / 2,
                            zero- (float) ((DIF.get(i)) * rate) , viewWidth - 2
                                    - (float) mCandleWidth * (i - mDataStartIndext)
                                    + (float) mCandleWidth / 2, dif, yellowPaint);
                }
                dea = zero-(float) ((DEA.get(i)) * rate)  ;
                dif = zero-(float) ((DIF.get(i)) * rate) ;
            }
            canvas.drawText(new DecimalFormat("#.##").format(high), super.getWidth()-DEFAULT_AXIS_MARGIN_RIGHT, lowertop
                    + DEFAULT_AXIS_TITLE_SIZE - 2, textPaint);
            canvas.drawText(new DecimalFormat("#.##").format(0), super.getWidth()-DEFAULT_AXIS_MARGIN_RIGHT, lowertop
                    + lowerHight / 2 + DEFAULT_AXIS_TITLE_SIZE, textPaint);
            canvas.drawText(new DecimalFormat("#.##").format(low), super.getWidth()-DEFAULT_AXIS_MARGIN_RIGHT, lowertop + lowerHight,
                    textPaint);
        }catch (Exception e)
        {

        }
    }

    private void drawKDJ(Canvas canvas) {
        float lowertop = LOWER_CHART_TOP + 1;
        float lowerHight = getLowerChartHeight();
        float viewWidth = getWidth()- super.DEFAULT_AXIS_MARGIN_RIGHT;

        Paint whitePaint = new Paint();
        whitePaint.setColor(Color.RED);
        whitePaint.setStrokeWidth(2);

        Paint yellowPaint = new Paint();
        yellowPaint.setColor(Color.GREEN);
        yellowPaint.setStrokeWidth(2);

        Paint magentaPaint = new Paint();
        magentaPaint.setColor(Color.MAGENTA);
        magentaPaint.setStrokeWidth(2);


        Paint textPaint = new Paint();
        textPaint.setColor(super.getAxisColor());
        textPaint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);

        if(mKDJData==null)
            return;
        List<Double> Ks = mKDJData.getK();
        List<Double> Ds = mKDJData.getD();
        List<Double> Js = mKDJData.getJ();

        double low = 0;
        double high = 100;
        float zero = LOWER_CHART_TOP+getLowerChartHeight();
        double  rate = lowerHight / (high - low);
        // 绘制白、黄、紫线
        float k = 50f;
        float d = 50f;
        float j = 50f;
        for (int i = mDataStartIndext; i < mDataStartIndext + mShowDataNum && i < Ks.size(); i++) {

            if (i != mDataStartIndext) {
                canvas.drawLine(viewWidth - 1 - (float) mCandleWidth * (i + 1 - mDataStartIndext) + (float) mCandleWidth / 2, (float) (zero - Ks.get(i) * rate) , viewWidth - 2 - (float) mCandleWidth * (i - mDataStartIndext) + (float) mCandleWidth / 2, k, whitePaint);
                canvas.drawLine(viewWidth - 1 - (float) mCandleWidth * (i + 1 - mDataStartIndext) + (float) mCandleWidth / 2, (float) (zero - Ds.get(i) * rate) , viewWidth - 2-  (float) mCandleWidth * (i - mDataStartIndext)+ (float) mCandleWidth / 2, d, yellowPaint);
                canvas.drawLine(viewWidth - 1 - (float) mCandleWidth * (i + 1 - mDataStartIndext) + (float) mCandleWidth / 2, (float) (zero - Js.get(i) * rate) , viewWidth - 2-  (float) mCandleWidth * (i - mDataStartIndext)+ (float) mCandleWidth / 2, j, magentaPaint);
            }
            k = (float) (zero - Ks.get(i) * rate);
            d = (float) (zero - Ds.get(i) * rate);
            j = (float) (zero - Js.get(i) * rate);

            canvas.drawText(new DecimalFormat("#.##").format(high), super.getWidth()-DEFAULT_AXIS_MARGIN_RIGHT, lowertop + DEFAULT_AXIS_TITLE_SIZE - 2, textPaint);
            canvas.drawText(new DecimalFormat("#.##").format(50),   super.getWidth()-DEFAULT_AXIS_MARGIN_RIGHT, lowertop + lowerHight / 2 + DEFAULT_AXIS_TITLE_SIZE, textPaint);
            canvas.drawText(new DecimalFormat("#.##").format(low),  super.getWidth()-DEFAULT_AXIS_MARGIN_RIGHT, lowertop + lowerHight, textPaint);
        }
    }


    /**
     * 绘制柱状线
     * @param canvas
     */
    protected void drawSticks(Canvas canvas) {
        // 蜡烛棒宽度
        float stickWidth = ((super.getWidth() - DEFAULT_AXIS_MARGIN_RIGHT-2) /mShowDataNum);
        // 蜡烛棒起始绘制位置
        float stickX = super.getWidth() - DEFAULT_AXIS_MARGIN_RIGHT-stickWidth;

        Paint mPaintRedStick = new Paint();
        mPaintRedStick.setColor(klineRed);

        Paint mPaintGreenStick = new Paint();
        mPaintGreenStick.setColor(klineGreen);

        if(null != mOHLCData){

            //判断显示为方柱或显示为线条
            for (int i = 0; i < mShowDataNum && mDataStartIndext + i < mOHLCData.size(); i++) {
                MarketChartData ohlc = mOHLCData.get(mDataStartIndext+i);

                float highY = (float) ((ohlc.getVol()
                        / (mMaxVol)) * mMiddleChartHeight);

                if(ohlc.getClosePrice()>ohlc.getOpenPrice())
                {
                    canvas.drawRect(stickX+1, super.LOWER_CHART_TOP-super.getTitleHeight()-highY, stickX + stickWidth-1, super.LOWER_CHART_TOP-super.getTitleHeight(), mPaintRedStick);
                }
                else
                {
                    canvas.drawRect(stickX+1, super.LOWER_CHART_TOP-super.getTitleHeight()-highY, stickX + stickWidth-1, super.LOWER_CHART_TOP-super.getTitleHeight(), mPaintGreenStick);
                }
                //X位移
                stickX = stickX-stickWidth;
            }


        }
        Paint textPaint = new Paint();
        textPaint.setColor(super.getAxisColor());
        textPaint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);

        canvas.drawText(new DecimalFormat("#.##").format(mMaxVol), super.getWidth()-DEFAULT_AXIS_MARGIN_RIGHT-1, super.LOWER_CHART_TOP-super.getTitleHeight()-super.mMiddleChartHeight, textPaint);
        canvas.drawText(new DecimalFormat("#.##").format((mMaxVol) / 2), super.getWidth()-DEFAULT_AXIS_MARGIN_RIGHT-1, super.LOWER_CHART_TOP-super.getTitleHeight()-super.mMiddleChartHeight/2, textPaint);
        canvas.drawText(new DecimalFormat("#.##").format(0), super.getWidth()-DEFAULT_AXIS_MARGIN_RIGHT-1, super.LOWER_CHART_TOP-super.getTitleHeight(), textPaint);
    }

public void setKDJShow()
{
    this.mShowJKD = true;
    this.mShowMACD = false;
    super.DEFAULT_LOWER_LATITUDE_NUM = 2;
    postInvalidate();
}
    public void setMACDShow()
    {
        this.mShowMACD = true;
        this.mShowJKD = false;
        super.DEFAULT_LOWER_LATITUDE_NUM = 2;
        postInvalidate();
    }

    public void setClose()
    {
        this.mShowMACD = false;
        this.mShowJKD = false;
        super.DEFAULT_LOWER_LATITUDE_NUM = 0;
        postInvalidate();
    }


    private List<Double> initVMA(int days){

        if (days < 2){
            return null;
        }

        List<Double> MA5Values = new ArrayList<Double>();

        Double sum = 0.0;
        Double avg = 0.0;
        for(int i = 0 ; i < this.mOHLCData.size(); i++){
            Double close =mOHLCData.get(i).getVol();
            if(i< days){
                sum = sum + close;
                avg = sum / (i + 1f);
            }else{
                sum = sum + close - (float)mOHLCData.get(i-days).getVol();
                avg = sum / days;
            }
            MA5Values.add(avg);
        }

        return MA5Values;
    }

    private void setCurrentData() {

        if (mShowDataNum > mOHLCData.size()) {
            mShowDataNum = mOHLCData.size();
        }
        if (MIN_CANDLE_NUM > mOHLCData.size()) {
           // mShowDataNum = MIN_CANDLE_NUM;
            mShowDataNum = mOHLCData.size();
        }

        setMaxMinPrice();
    }


    private void setMaxMinPrice()
    {

        try {
            if(mOHLCData.size()==0)
                return;
            mMinPrice = mOHLCData.get(mDataStartIndext).getLowPrice();
            mMaxPrice = mOHLCData.get(mDataStartIndext).getHighPrice();
            mMaxVol = mOHLCData.get(mDataStartIndext).getVol();
            minIndex=mDataStartIndext;
            maxIndex=mDataStartIndext;

            for (int i = 0; i < mShowDataNum&&mDataStartIndext+i<mOHLCData.size() ; i++)
            {
                MarketChartData entity = mOHLCData.get(mDataStartIndext+i);

                if(mMinPrice > entity.getLowPrice())
                {
                    mMinPrice = entity.getLowPrice();
                    minIndex=mDataStartIndext+i;
                }
                if(mMaxPrice < entity.getHighPrice())
                {
                    mMaxPrice = entity.getHighPrice();
                    maxIndex=i+mDataStartIndext;
                }
                mMaxVol = mMaxVol > entity.getVol() ? mMaxVol : entity.getVol();
            }
        }catch (Exception e){

        }

    }

    public void setOHLCData(List<MarketChartData> OHLCData) {

        //分时，小时切换，重置  mDataStartIndext
        mDataStartIndext = 0;
        if (OHLCData == null || OHLCData.size() <= 0) {
            return;
        }

        if(null != mOHLCData){
            mOHLCData.clear();
        }
        for(MarketChartData e :OHLCData){
            addData(e);
        }
        initMALineData();
        mMACDData = new MACD(mOHLCData);
        mKDJData = new KDJ(mOHLCData);
        setCurrentData();
        postInvalidate();
    }

    public void addData(MarketChartData entity){
        if(null != entity){
            if(null == mOHLCData || 0==mOHLCData.size()){
                mOHLCData = new ArrayList<MarketChartData>();
                this.mMinPrice = ((int)entity.getLowPrice()) / 10 * 10;
                this.mMaxPrice = ((int)entity.getHighPrice()) / 10 * 10;
            }

            this.mOHLCData.add(entity);

            if (this.mMinPrice > entity.getLowPrice()){
                this.mMinPrice = ((int)entity.getLowPrice()) / 10 * 10;
            }

            if (this.mMaxPrice < entity.getHighPrice()){
                this.mMaxPrice = 10 + ((int)entity.getHighPrice()) / 10 * 10;
            }

        }
    }

    //K线均线
    private void initMALineData() {
        MALineEntity MA5 = new MALineEntity();
        MA5.setTitle("MA5");
        MA5.setLineColor(kline5dayline);
        MA5.setLineData(initMA(mOHLCData, 5));

        MALineEntity MA10 = new MALineEntity();
        MA10.setTitle("MA10");
        MA10.setLineColor(kline10dayline);
        MA10.setLineData(initMA(mOHLCData, 10));

        MALineEntity MA30 = new MALineEntity();
        MA30.setTitle("MA30");
        MA30.setLineColor(kline30dayline);
        MA30.setLineData(initMA(mOHLCData, 30));

        MALineData = new ArrayList<MALineEntity>();
        MALineData.add(MA5);
        MALineData.add(MA10);
        MALineData.add(MA30);
    }

    //量能线均线
    private void initMAStickLineData() {
        //以下计算VOL
        lineData = new ArrayList<MALineEntity>();

        //计算5日均线
        MALineEntity VMA5 = new MALineEntity();
        VMA5.setTitle("MA5");
        VMA5.setLineColor(Color.RED);
        VMA5.setLineData(initVMA(5));
        lineData.add(VMA5);

        //计算10日均线
        MALineEntity VMA10 = new MALineEntity();
        VMA10.setTitle("MA10");
        VMA10.setLineColor(Color.WHITE);
        VMA10.setLineData(initVMA(10));
        lineData.add(VMA10);

        //计算25日均线
        MALineEntity VMA20 = new MALineEntity();
        VMA20.setTitle("MA20");
        VMA20.setLineColor(Color.GREEN);
        VMA20.setLineData(initVMA(20));
        lineData.add(VMA20);

    }
    /**
     * 初始化MA值，从数组的最后一个数据开始初始化
     *
     * @param entityList
     * @param days
     * @return
     */
    private List<Double> initMA(List<MarketChartData> entityList, int days) {
        if (days < 2 || entityList == null || entityList.size() <= 0) {
            return null;
        }
        List<Double> MAValues = new ArrayList<Double>();

        Double sum = 0.0;
        Double avg = 0.0;
        for (int i = entityList.size() - 1; i >= 0; i--) {
            Double close = entityList.get(i).getClosePrice();
           if (entityList.size()-i <days) {
                sum = sum + close;
                avg = sum / (entityList.size() - i);
            } else {
                sum = close + avg * (days - 1);
                avg = sum / days;
            }
            MAValues.add(avg);
        }

        List<Double> result = new ArrayList<Double>();
        for (int j = MAValues.size() - 1; j >= 0; j--) {
            result.add(MAValues.get(j));
        }
        return result;
    }
}
