package zhangliang.view.android.klibrary.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

import zhangliang.view.android.klibrary.R;

/**
 * Created by zhangliang on 16/11/5.
 * QQ:1179980507
 */
public class GridChartKView extends View {


	// ////////////默认值////////////////
	/** 默认背景色 */
	public static final int DEFAULT_BACKGROUD = 0x090A0B;

	/** 默认字体大小 **/
	public float DEFAULT_AXIS_TITLE_SIZE =10;


	/** 默认字体颜色 **/
	public static  int DEFAULT_AXIS_TITLE_COLOR = 0x78797A;
	/** 默认点击xy选择框颜色 **/
	public static int DEFAULT_AXIS_XYCLICK_COLOR = 0x535d66;

	/** 默认XY坐标轴颜色 */
	private static int DEFAULT_AXIS_COLOR = Color.RED;

	/** 默认经纬线颜色 */
	private static int DEFAULT_LONGI_LAITUDE_COLOR = 0x3b3b3d;

	/** 默认上表纬线数 */
	public static final int DEFAULT_UPER_LATITUDE_NUM = 4;
	/** 默认中表纬线数 */
	private static final int DEFAULT_MIDDLE_LATITUDE_NUM = 2;
	/** 默认下表纬线数 */
	public static  int DEFAULT_LOWER_LATITUDE_NUM = 2;

	/** 当前被选中touch点 */
	private PointF touchPoint = null;

	/** 选中位置X坐标 */
	private float clickPostX = 0f;

	/** 选中位置Y坐标*/
	private float clickPostY = 0f;

	/** 点击时横轴X刻度*/
	private String axisXTitleClick="";

	/** 点击时横轴Y刻度*/
	private String axisYTitleClick="";


	/** 横轴X刻度*/
	private List<String> axisXTitles;
	/** 纵轴Y刻度*/
	private List<String> axisYTitles;


	/** 默认Y轴刻度显示长度 */
	private int DEFAULT_AXIS_Y_MAX_TITLE_LENGTH = 7;

	/** 默认经线数 */
	public static final int DEFAULT_LOGITUDE_NUM = 8;

	/** 默认边框的颜色 */
	public static final int DEFAULT_BORDER_COLOR = Color.RED;

	/** 默认虚线效果 */
	private static final PathEffect DEFAULT_DASH_EFFECT = new DashPathEffect(
			new float[] { 15, 15, 15, 15 }, 15);


	/** 上表的上间隙 */
	public static float UPER_CHART_MARGIN_TOP;

	/** 上表的顶部 */
	public static float UPER_CHART_TOP;
	/** 上表的下间隙 */
	public static float UPER_CHART_MARGIN_BOTTOM;

	/** 中表的顶部 */
	public static float MIDDLE_CHART_TOP;

	/** 下表的顶部 */
	public static float LOWER_CHART_TOP;

	/** 所有表的title高 */
	public float TITLE_HEIGHT = DEFAULT_AXIS_TITLE_SIZE+2;

	/** 图表跟右边距离 */
	public float DEFAULT_AXIS_MARGIN_RIGHT = DEFAULT_AXIS_TITLE_SIZE*2;

	// /////////////属性////////////////
	/** 背景色 */
	private int mBackGround;

	/** 坐标轴XY颜色 */
	private int mAxisColor;

	/** 经纬线颜色 */
	private int mLongiLatitudeColor;

	/** 虚线效果 */
	private PathEffect mDashEffect;

	/** 边线色 */
	private int mBorderColor;

	/** 上表高度 */
	public float mUperChartHeight;
	/** 中表高度 */
	public float mMiddleChartHeight;
	/** 下表高度 */
	public float mLowerChartHeight;

	//经线间隔度
	private float longitudeSpacing;
	//维线间隔度
	private float latitudeSpacing;
	private Context mContext;


	public GridChartKView(Context context) {
		super(context);
		init(context);
	}
	public GridChartKView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public GridChartKView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		mContext = context;
		mBackGround = context.getResources().getColor(R.color.klinebg);
		DEFAULT_AXIS_TITLE_COLOR = context.getResources().getColor(R.color.kViewztblack);
		DEFAULT_LONGI_LAITUDE_COLOR = context.getResources().getColor(R.color.kViewjwblack);
		DEFAULT_AXIS_XYCLICK_COLOR= context.getResources().getColor(R.color.kViewclickblack);
		DEFAULT_AXIS_COLOR = DEFAULT_AXIS_TITLE_COLOR;
		mAxisColor = DEFAULT_AXIS_COLOR;
		mLongiLatitudeColor = DEFAULT_LONGI_LAITUDE_COLOR;
		initSize();
		mDashEffect = DEFAULT_DASH_EFFECT;
		mBorderColor = DEFAULT_LONGI_LAITUDE_COLOR;

	}

	public void initSize()
	{
		DEFAULT_AXIS_TITLE_SIZE = sp2px(mContext,DEFAULT_AXIS_TITLE_SIZE);
		TITLE_HEIGHT = DEFAULT_AXIS_TITLE_SIZE+sp2px(mContext,2);
		DEFAULT_AXIS_MARGIN_RIGHT =  sp2px(mContext,40);

	}


	/**
	 * 重新控件大�?
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(measureWidth(widthMeasureSpec),
				measureHeight(heightMeasureSpec));
	}

	private int measureWidth(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else if (specMode == MeasureSpec.AT_MOST) {
			result = Math.min(result, specSize);
		}
		return result;
	}

	private int measureHeight(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else if (specMode == MeasureSpec.AT_MOST) {
			result = Math.min(result, specSize);
		}
		return result;
	}


	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		setBackgroundColor(mBackGround);

		int viewHeight = getHeight();
		int viewWidth = getWidth();
		//经度
		longitudeSpacing = (viewWidth - DEFAULT_AXIS_MARGIN_RIGHT-2)/DEFAULT_LOGITUDE_NUM;
		//维度#####4-8
		latitudeSpacing =(viewHeight - TITLE_HEIGHT*9)/(DEFAULT_UPER_LATITUDE_NUM+DEFAULT_LOWER_LATITUDE_NUM+DEFAULT_MIDDLE_LATITUDE_NUM);

		mUperChartHeight = DEFAULT_UPER_LATITUDE_NUM*latitudeSpacing;
		mMiddleChartHeight = DEFAULT_MIDDLE_LATITUDE_NUM*latitudeSpacing;
		mLowerChartHeight = DEFAULT_LOWER_LATITUDE_NUM*latitudeSpacing;


		UPER_CHART_MARGIN_TOP = 3*TITLE_HEIGHT;
		//###加上表间隔
		UPER_CHART_TOP = TITLE_HEIGHT+UPER_CHART_MARGIN_TOP;
		UPER_CHART_MARGIN_BOTTOM = 2*TITLE_HEIGHT;

		MIDDLE_CHART_TOP = 3*TITLE_HEIGHT+mUperChartHeight+UPER_CHART_MARGIN_BOTTOM+UPER_CHART_MARGIN_TOP;
		LOWER_CHART_TOP =MIDDLE_CHART_TOP+mMiddleChartHeight+TITLE_HEIGHT;
		// 绘制边框
		drawBorders(canvas, viewHeight, viewWidth);
		// 绘制纬线
		drawLatitudes(canvas, viewWidth, latitudeSpacing);
		// 绘制经线
		drawLongitudes(canvas,longitudeSpacing);

		//十字行
		//drawWithFingerClick(canvas);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getY() > 0
				&& event.getY() < MIDDLE_CHART_TOP-2*TITLE_HEIGHT
				&& event.getX() > 0
				&& event.getX() < super.getWidth()-DEFAULT_AXIS_MARGIN_RIGHT) {

			/*
			 * 判定用户是否触摸到�?���?如果是单点触摸则�?��绘制十字线 如果是2点触控则�?��K线放大
			 */
			if (event.getPointerCount() == 1) {
				// 获取点击坐�?
				clickPostX = event.getX();
				clickPostY = event.getY();

				PointF point = new PointF(clickPostX,clickPostY);
				touchPoint = point;
				super.invalidate();

			}
		}
		return super.onTouchEvent(event);
	}

	/**
	 * 绘制边框
	 *
	 * @param canvas
	 */
	private void drawBorders(Canvas canvas, int viewHeight, int viewWidth) {
		Paint paint = new Paint();
		paint.setColor(mBorderColor);
		paint.setStrokeWidth(1);
		canvas.drawLine(0, 0, 0,viewHeight, paint);
		canvas.drawLine(0, 0, viewWidth,0, paint);
		canvas.drawLine(viewWidth-1, 0, viewWidth-1,viewHeight-1, paint);
		canvas.drawLine(0,viewHeight-1, viewWidth-1,viewHeight-1, paint);
		canvas.drawLine(viewWidth-DEFAULT_AXIS_MARGIN_RIGHT,0, viewWidth-DEFAULT_AXIS_MARGIN_RIGHT,viewHeight, paint);
	}


	/**
	 * 绘制纬线
	 *
	 * @param canvas
	 * @param viewWidth
	 */
	private void drawLatitudes(Canvas canvas, int viewWidth, float latitudeSpacing ) {
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(mLongiLatitudeColor);
		paint.setPathEffect(mDashEffect);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(1);
		paint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);



		Paint paintAxis = new Paint();
		paintAxis.setColor(mAxisColor);
		paintAxis.setTextSize(DEFAULT_AXIS_TITLE_SIZE);



		Path path = new Path();
		path.moveTo(0, UPER_CHART_TOP);
		path.lineTo(viewWidth-DEFAULT_AXIS_MARGIN_RIGHT,UPER_CHART_TOP);
		canvas.drawPath(path, paint);

		path.moveTo(0, TITLE_HEIGHT);
		path.lineTo(viewWidth-DEFAULT_AXIS_MARGIN_RIGHT,TITLE_HEIGHT);
		canvas.drawPath(path, paint);


		path.moveTo(0, MIDDLE_CHART_TOP-2*TITLE_HEIGHT-UPER_CHART_MARGIN_BOTTOM);
		path.lineTo(viewWidth-DEFAULT_AXIS_MARGIN_RIGHT,MIDDLE_CHART_TOP-2*TITLE_HEIGHT-UPER_CHART_MARGIN_BOTTOM);
		canvas.drawPath(path, paint);

		path.moveTo(0, MIDDLE_CHART_TOP-TITLE_HEIGHT);
		path.lineTo(viewWidth-DEFAULT_AXIS_MARGIN_RIGHT,MIDDLE_CHART_TOP-TITLE_HEIGHT);
		canvas.drawPath(path, paint);

		path.moveTo(0, MIDDLE_CHART_TOP-2*TITLE_HEIGHT);
		path.lineTo(viewWidth-DEFAULT_AXIS_MARGIN_RIGHT,MIDDLE_CHART_TOP-2*TITLE_HEIGHT);
		canvas.drawPath(path, paint);

		path.moveTo(0, LOWER_CHART_TOP-TITLE_HEIGHT);
		path.lineTo(viewWidth-DEFAULT_AXIS_MARGIN_RIGHT,LOWER_CHART_TOP-TITLE_HEIGHT);
		canvas.drawPath(path, paint);


		//刻度颜色
	 paint.setColor(mLongiLatitudeColor);
		for (int i = 0; i <DEFAULT_UPER_LATITUDE_NUM; i++) {
			//线

			path.moveTo(0, UPER_CHART_TOP+latitudeSpacing * (i+1));
			path.lineTo(viewWidth-DEFAULT_AXIS_MARGIN_RIGHT,UPER_CHART_TOP  + latitudeSpacing * (i+1));
			canvas.drawPath(path, paint);

			if(axisYTitles!=null)
			{
				// 绘制Y刻度
				canvas.drawText(axisYTitles.get(i), viewWidth-DEFAULT_AXIS_MARGIN_RIGHT, MIDDLE_CHART_TOP-2*TITLE_HEIGHT-UPER_CHART_MARGIN_BOTTOM-latitudeSpacing*(i), paintAxis);
				if(i==DEFAULT_UPER_LATITUDE_NUM-1)
				{
					canvas.drawText(axisYTitles.get(DEFAULT_UPER_LATITUDE_NUM), viewWidth-DEFAULT_AXIS_MARGIN_RIGHT, MIDDLE_CHART_TOP-2*TITLE_HEIGHT-UPER_CHART_MARGIN_BOTTOM-latitudeSpacing*(DEFAULT_UPER_LATITUDE_NUM), paintAxis);

				}
			}

		}

		for (int i = 0; i < DEFAULT_MIDDLE_LATITUDE_NUM; i++) {

			path.moveTo(0,MIDDLE_CHART_TOP+latitudeSpacing * i);
			path.lineTo(viewWidth-DEFAULT_AXIS_MARGIN_RIGHT, MIDDLE_CHART_TOP+latitudeSpacing * i);
			canvas.drawPath(path, paint);
		}

		for (int i = 0; i <= DEFAULT_LOWER_LATITUDE_NUM; i++) {

			path.moveTo(0,LOWER_CHART_TOP+latitudeSpacing * i);
			path.lineTo(viewWidth-DEFAULT_AXIS_MARGIN_RIGHT, LOWER_CHART_TOP+latitudeSpacing * i);
			canvas.drawPath(path, paint);

		}

	}

	/**
	 * 绘制经线
	 *
	 * @param canvas
	 */
	private void drawLongitudes(Canvas canvas, float longitudeSpacing) {
		Paint paint = new Paint();
		paint.setColor(mLongiLatitudeColor);
		paint.setPathEffect(mDashEffect);
		paint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);


		Paint paintAxis = new Paint();
		paintAxis.setColor(mAxisColor);
		paintAxis.setTextSize(DEFAULT_AXIS_TITLE_SIZE);

		for (int i = 1; i <= DEFAULT_LOGITUDE_NUM; i++) {
		/*
		经线掩藏
		canvas.drawLine(longitudeSpacing * i, TITLE_HEIGHT, longitudeSpacing * i,
					TITLE_HEIGHT+mUperChartHeight+UPER_CHART_MARGIN_BOTTOM+UPER_CHART_MARGIN_TOP, paint);*/
			if(axisXTitles!=null) {
				try{

					float tWidth = paint.measureText(axisXTitles.get(i-1));
					// 绘制刻度
					canvas.drawText(axisXTitles.get(i-1), super.getWidth()-DEFAULT_AXIS_MARGIN_RIGHT-longitudeSpacing * (i-1)-tWidth, TITLE_HEIGHT + mUperChartHeight +UPER_CHART_MARGIN_BOTTOM+UPER_CHART_MARGIN_TOP+DEFAULT_AXIS_TITLE_SIZE, paintAxis);

				}catch (Exception e){

				}

			}
		}

	}
	public float sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return  (spValue * fontScale + 0.5f);
	}

	//获取字体大小
	public float adjustFontSize(int screenWidth, int screenHeight) {
		screenWidth=screenWidth>screenHeight?screenWidth:screenHeight;
		float rate = ((float) screenWidth/320);
		return rate; //字体太小也不好看的
	}


	public int getBackGround() {
		return mBackGround;
	}

	public void setBackGround(int BackGround) {
		this.mBackGround = BackGround;
	}

	public int getAxisColor() {
		return mAxisColor;
	}

	public void setAxisColor(int AxisColor) {
		this.mAxisColor = AxisColor;
	}

	public int getLongiLatitudeColor() {
		return mLongiLatitudeColor;
	}

	public void setLongiLatitudeColor(int LongiLatitudeColor) {
		this.mLongiLatitudeColor = LongiLatitudeColor;
	}

	public PathEffect getDashEffect() {
		return mDashEffect;
	}

	public void setDashEffect(PathEffect DashEffect) {
		this.mDashEffect = DashEffect;
	}

	public int getBorderColor() {
		return mBorderColor;
	}

	public void setBorderColor(int BorderColor) {
		this.mBorderColor = BorderColor;
	}

	public float getUperChartHeight() {
		return mUperChartHeight;
	}

	public void setUperChartHeight(float UperChartHeight) {
		this.mUperChartHeight = UperChartHeight;
	}

	public int getAxisYMaxTitleLength() {
		return DEFAULT_AXIS_Y_MAX_TITLE_LENGTH;
	}

	public void setAxisYMaxTitleLength(int axisYMaxTitleLength) {
		this.DEFAULT_AXIS_Y_MAX_TITLE_LENGTH = axisYMaxTitleLength;
	}

	public float getLowerChartHeight() {
		return mLowerChartHeight;
	}

	public void setLowerChartHeight(float LowerChartHeight) {
		this.mLowerChartHeight = LowerChartHeight;
	}


	public float getLongitudeSpacing() {
		return longitudeSpacing;
	}

	public void setLongitudeSpacing(float longitudeSpacing) {
		this.longitudeSpacing = longitudeSpacing;
	}

	public float getLatitudeSpacing() {
		return latitudeSpacing;
	}

	public void setLatitudeSpacing(float latitudeSpacing) {
		this.latitudeSpacing = latitudeSpacing;
	}

	public float getTitleHeight() {
		return TITLE_HEIGHT;
	}

	public List<String> getAxisXTitles() {
		return axisXTitles;
	}

	public void setAxisXTitles(List<String> axisXTitles) {
		this.axisXTitles = axisXTitles;
	}

	public List<String> getAxisYTitles() {
		return axisYTitles;
	}

	public void setAxisYTitles(List<String> axisYTitles) {
		this.axisYTitles = axisYTitles;
	}


	public void setAxisXClickTitle(String axisXTitle) {
		this.axisXTitleClick = axisXTitle;
	}

	public String getAxisYClickTitle() {
		return axisYTitleClick;
	}

	public void setAxisYClickTitle(String axisYTitle) {
		this.axisYTitleClick = axisYTitle;
	}

	public String getAxisXClickTitle() {
		return axisXTitleClick;
	}

	/**
	 * 获取X轴刻度的百分比最大1
	 *
	 * @param value
	 * @return
	 */
	public String getAxisXGraduate(Object value) {

		float length = super.getWidth() - DEFAULT_AXIS_MARGIN_RIGHT;
		float valueLength = ((Float) value).floatValue();

		return String.valueOf(valueLength / length);
	}

	/**
	 * 获取Y轴刻度的百分比最大1
	 *
	 * @param value
	 * @return
	 */
	public String getAxisYGraduate(Object value) {

		float length = latitudeSpacing*DEFAULT_UPER_LATITUDE_NUM;
		float valueLength =  (((Float) value).floatValue() - TITLE_HEIGHT);

		return String.valueOf(valueLength / length);
	}


	/**
	 * 单点击事件
	 */
	protected void drawWithFingerClick(Canvas canvas) {
		Paint mPaint = new Paint();
		mPaint.setColor(DEFAULT_AXIS_XYCLICK_COLOR);
		mPaint.setStrokeWidth(2);
		// 垂直线高度
		float lineVLength = getHeight() - 2f;
		if(!getAxisXClickTitle().equals(""))
		drawAlphaXTextBox(getAxisXClickTitle(),canvas);
		if(!getAxisYClickTitle().equals(""))
		drawAlphaYTextBox(getAxisYClickTitle(),canvas);
	//	drawAlphaTopTextBox("当日行情------开:2926.48  高:2930.00 低:2930.47 收:2927.71",canvas);
	//	drawAlphaMiddleTextBox("量:483.343  MA5:888 MA10:33333",canvas);
	//	drawAlphaBottomTextBox("MACD(12,26,9) DIF:0.10 DEA:0.00 MACD:0.19",canvas);

		if (touchPoint!=null) {
			// 显示纵线
			canvas.drawLine(touchPoint.x>super.getWidth()-DEFAULT_AXIS_MARGIN_RIGHT?super.getWidth()-DEFAULT_AXIS_MARGIN_RIGHT:touchPoint.x, 1f, touchPoint.x>super.getWidth()-DEFAULT_AXIS_MARGIN_RIGHT?super.getWidth()-DEFAULT_AXIS_MARGIN_RIGHT:touchPoint.x, lineVLength, mPaint);
			// 显示横线
			canvas.drawLine(1f, touchPoint.y >MIDDLE_CHART_TOP-2*TITLE_HEIGHT ? MIDDLE_CHART_TOP-2*TITLE_HEIGHT : touchPoint.y, super.getWidth() -DEFAULT_AXIS_MARGIN_RIGHT , touchPoint.y >MIDDLE_CHART_TOP-2*TITLE_HEIGHT ? MIDDLE_CHART_TOP-2*TITLE_HEIGHT : touchPoint.y, mPaint);
			//画实心圆
			canvas.drawCircle(touchPoint.x>super.getWidth()-DEFAULT_AXIS_MARGIN_RIGHT?super.getWidth()-DEFAULT_AXIS_MARGIN_RIGHT:touchPoint.x, touchPoint.y >MIDDLE_CHART_TOP-2*TITLE_HEIGHT ? MIDDLE_CHART_TOP-2*TITLE_HEIGHT : touchPoint.y, 10, mPaint);
			//画空心圆
			mPaint.setStyle(Paint.Style.STROKE);
			canvas.drawCircle(touchPoint.x>super.getWidth()-DEFAULT_AXIS_MARGIN_RIGHT?super.getWidth()-DEFAULT_AXIS_MARGIN_RIGHT:touchPoint.x, touchPoint.y >MIDDLE_CHART_TOP-2*TITLE_HEIGHT ? MIDDLE_CHART_TOP-2*TITLE_HEIGHT : touchPoint.y, 25, mPaint);
		}
	}

	/**
	 * 绘制半�?明文本�?
	 * @param content
	 * @param canvas
	 */

	private void drawAlphaXTextBox(String content, Canvas canvas) {


		if (touchPoint==null)
			return;
		Paint mPaintBox = new Paint();
		mPaintBox.setColor(mBackGround);
		//mPaintBox.setAlpha(150);

		Paint mPaintBoxLine = new Paint();
		mPaintBoxLine.setColor(Color.WHITE);
		mPaintBoxLine.setAntiAlias(true);
		mPaintBoxLine.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
		float strw = mPaintBoxLine.measureText(content);

		float left = touchPoint.x-strw/2;
		float top = MIDDLE_CHART_TOP-2*TITLE_HEIGHT;
		float right = touchPoint.x+strw/2;
		float bottom = MIDDLE_CHART_TOP-TITLE_HEIGHT;

		canvas.drawRect(left, top, right, bottom, mPaintBox);
		Paint borderPaint = new Paint();
		borderPaint.setColor(Color.WHITE);
		borderPaint.setStrokeWidth(2);
		canvas.drawLine(left, top, left, bottom, borderPaint);
		canvas.drawLine(left, top, right, top, borderPaint);
		canvas.drawLine(right, bottom, right, top, borderPaint);
		canvas.drawLine(right, bottom, left, bottom, borderPaint);
		canvas.drawText(content, touchPoint.x-strw/2, MIDDLE_CHART_TOP-TITLE_HEIGHT-2, mPaintBoxLine);
	}


	/**
	 * 绘制半�?明文本�?
	 *
	 * @param content
	 * @param canvas
	 */

	private void drawAlphaYTextBox(String content, Canvas canvas) {

		if (touchPoint==null)
			return;
		Paint mPaintBox = new Paint();
		mPaintBox.setColor(mBackGround);
		//mPaintBox.setAlpha(150);

		Paint mPaintBoxLine = new Paint();
		mPaintBoxLine.setColor(Color.WHITE);
		mPaintBoxLine.setAntiAlias(true);
		mPaintBoxLine.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
		Rect rect = new Rect();
		mPaintBoxLine.getTextBounds(content, 0, 1, rect);
		float strh = rect.height();

		float left = super.getWidth()-DEFAULT_AXIS_MARGIN_RIGHT;
		float top = touchPoint.y-strh/2>MIDDLE_CHART_TOP-2*TITLE_HEIGHT-UPER_CHART_MARGIN_BOTTOM-strh/2?MIDDLE_CHART_TOP-2*TITLE_HEIGHT-UPER_CHART_MARGIN_BOTTOM-strh/2:touchPoint.y-strh/2;
		float right = super.getWidth()-2;
		float bottom = touchPoint.y+strh/2>MIDDLE_CHART_TOP-2*TITLE_HEIGHT-UPER_CHART_MARGIN_BOTTOM+strh/2?MIDDLE_CHART_TOP-2*TITLE_HEIGHT-UPER_CHART_MARGIN_BOTTOM+strh/2:touchPoint.y+strh/2;

		canvas.drawRect(left, top-3, right, bottom+3, mPaintBox);
		Paint borderPaint = new Paint();
		borderPaint.setColor(Color.WHITE);
		borderPaint.setStrokeWidth(2);
		canvas.drawLine(left, top-3, left, bottom+3, borderPaint);
		canvas.drawLine(left, top-3, right, top-3, borderPaint);
		canvas.drawLine(right, bottom+3, right, top-3, borderPaint);
		canvas.drawLine(right, bottom+3, left, bottom+3, borderPaint);
		canvas.drawText(content, left, bottom, mPaintBoxLine);
	}

	/**
	 * 绘制半�?明文本�?
	 * @param content
	 * @param canvas
	 */

	public void drawAlphaTopTextBox(String content, Canvas canvas) {
		Paint mPaintBoxLine = new Paint();
		mPaintBoxLine.setColor(getAxisColor());
		mPaintBoxLine.setAntiAlias(true);
		mPaintBoxLine.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
		canvas.drawText(content, 2f, TITLE_HEIGHT-2f, mPaintBoxLine);
	}



	public void drawAlphaMiddleTextBox(String content, Canvas canvas) {
		Paint mPaintBoxLine = new Paint();
		mPaintBoxLine.setColor(getAxisColor());
		mPaintBoxLine.setAntiAlias(true);
		mPaintBoxLine.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
		canvas.drawText(content, 2f, MIDDLE_CHART_TOP-2f, mPaintBoxLine);
	}


	public void drawAlphaBottomTextBox(String content, Canvas canvas) {
		Paint mPaintBoxLine = new Paint();
		mPaintBoxLine.setColor(getAxisColor());
		mPaintBoxLine.setAntiAlias(true);
		mPaintBoxLine.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
		canvas.drawText(content, 2f, LOWER_CHART_TOP-2f, mPaintBoxLine);
	}

	public PointF getTouchPoint() {
		return touchPoint;
	}

	public void setTouchPoint(PointF p) {
		 this.touchPoint=p;
	}
}