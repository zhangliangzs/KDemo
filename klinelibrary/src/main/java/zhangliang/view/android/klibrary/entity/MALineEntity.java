package zhangliang.view.android.klibrary.entity;

import java.util.List;

/**
 * Created by zhangliang on 16/11/5.
 * QQ:1179980507
 */
public class MALineEntity {

	/** 线表示数据 */
	private List<Double> lineData;

	/** 线的标题 */
	private String title;

	/** 线表示颜色 */
	private int lineColor;

	public MALineEntity() {
		super();
	}

	public MALineEntity(List<Double> lineData, String title, int lineColor) {
		this.lineData = lineData;
		this.title = title;
		this.lineColor = lineColor;
	}

	public List<Double> getLineData() {
		return lineData;
	}

	public void setLineData(List<Double> lineData) {
		this.lineData = lineData;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getLineColor() {
		return lineColor;
	}

	public void setLineColor(int lineColor) {
		this.lineColor = lineColor;
	}

}
