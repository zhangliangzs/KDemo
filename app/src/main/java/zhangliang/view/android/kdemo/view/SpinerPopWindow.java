package zhangliang.view.android.kdemo.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;


import java.util.List;

import zhangliang.view.android.kdemo.R;
import zhangliang.view.android.kdemo.adapters.NormalSpinerAdapter;

/**
 * Created by zhangliang on 16/6/8.
 * @@author QQ1179980507
 */
public class SpinerPopWindow extends PopupWindow implements AdapterView.OnItemClickListener {

    private Context mContext;
    private ListView mListView;
    private NormalSpinerAdapter mAdapter;
    private IOnItemSelectListener mItemSelectListener;




    public void setAdatper(NormalSpinerAdapter adapter){
        mAdapter = adapter;
        mListView.setAdapter(mAdapter);
    }

    public void setItemListener(IOnItemSelectListener listener){
        mItemSelectListener = listener;
    }

    public SpinerPopWindow(Context context)
    {
        super(context);
        mContext = context;
        init();
    }
    private void init()
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.spiner_window_layout, null);
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        setFocusable(true);


/*        ColorDrawable dw = new ColorDrawable(0x00);
        setBackgroundDrawable(dw);*/

        mListView = (ListView) view.findViewById(R.id.spiner_list_item);

        mAdapter = new NormalSpinerAdapter(mContext);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }


    public void refreshData(List<String> list, int selIndex)
    {
        if (list != null && selIndex  != -1)
        {
            mAdapter.refreshData(list, selIndex);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int pos, long arg3) {
        dismiss();
        if (mItemSelectListener != null){
            mItemSelectListener.onItemClick(pos);
        }
    }

public interface IOnItemSelectListener
{
        public void onItemClick(int pos);
}

}