package com.qihoo.browser.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.chromium.chrome.R;

import java.util.List;

/**
 * Created by wangxin on 2/7/17.
 */

public class NewsAdapter extends BaseAdapter{
    private List<NewsBean> mNewsBeanList;
    private LayoutInflater mInflater;

    public NewsAdapter(Context context, List<NewsBean> list) {
        mNewsBeanList = list;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mNewsBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return mNewsBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_layout, null);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
            viewHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvTitle.setText(mNewsBeanList.get(position).title);
        viewHolder.tvDate.setText(mNewsBeanList.get(position).date);
        viewHolder.tvContent.setText(mNewsBeanList.get(position).content);

        return convertView;
    }

    class ViewHolder {
        public TextView tvTitle;
        public TextView tvDate;
        public TextView tvContent;
    }
}
