package zhangliang.view.android.kdemo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import zhangliang.view.android.kdemo.R;

/**
 * Created by bitbank on 16/6/8.
 */
public class NormalSpinerAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mObjects = new ArrayList<String>();
    private int mSelectItem = 0;

    private LayoutInflater mInflater;

    public NormalSpinerAdapter(Context context) {
        init(context);
    }

    public void refreshData(List<String> objects, int selIndex) {
        mObjects = objects;
        if (selIndex < 0) {
            selIndex = 0;
        }
        if (selIndex >= mObjects.size()) {
            selIndex = mObjects.size() - 1;
        }

        mSelectItem = selIndex;
    }

    private void init(Context context) {
        mContext = context;

    }


    @Override
    public int getCount() {

        return mObjects.size();
    }

    @Override
    public Object getItem(int pos) {
        return mObjects.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup arg2) {
        ViewHolder viewHolder;
        if (convertView == null) {
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.spiner_window_layout_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.mTextView = (TextView) convertView.findViewById(R.id.textview);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Object item = getItem(pos);
        viewHolder.mTextView.setText(item.toString());
        return convertView;
    }

    public static class ViewHolder {
        public TextView mTextView;
    }
}
