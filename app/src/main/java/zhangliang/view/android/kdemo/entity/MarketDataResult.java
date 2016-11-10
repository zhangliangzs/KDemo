package zhangliang.view.android.kdemo.entity;

public class MarketDataResult {
    private String des;
    private boolean isSuc;
    private BTC123MarketData datas;

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public boolean isSuc() {
        return isSuc;
    }

    public void setSuc(boolean suc) {
        isSuc = suc;
    }

    public BTC123MarketData getDatas() {
        return datas;
    }

    public void setDatas(BTC123MarketData datas) {
        this.datas = datas;
    }
    @Override
    public String toString() {
        return " des=" + des
                + " isSuc=" + isSuc
                + " datas=" + datas.toString()
                +" | ";
    }
}
