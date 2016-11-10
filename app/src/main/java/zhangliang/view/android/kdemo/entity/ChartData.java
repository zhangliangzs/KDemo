package zhangliang.view.android.kdemo.entity;

/**

 * 获取首页行情图表
 */
public class ChartData {
    private String[][] chartData;//当前货币类型

    public String[][] getChartData() {
        return chartData;
    }

    public void setChartData(String[][] chartData) {
        this.chartData = chartData;
    }

    @Override
    public String toString() {
        return "chartData=" + chartData
                +" | ";
    }
}
