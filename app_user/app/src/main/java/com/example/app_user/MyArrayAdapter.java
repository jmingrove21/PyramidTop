package com.example.app_user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class MyArrayAdapter<E> extends BaseAdapter
{
    public MyArrayAdapter(Context context, int layoutRes) {
        mContext = context;
        mInflater = (LayoutInflater)context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        mLayoutRes = layoutRes;
    }

    public void setSource(List<E> list) {
        mList = list;
    }

    public abstract void bindView(View view, E item);

    public E itemForPosition(int position) {
        if (mList == null) {
            return null;
        }

        return mList.get(position);
    }

    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = mInflater.inflate(mLayoutRes, parent, false);
        } else {
            view = convertView;
        }
        bindView(view, mList.get(position));
        return view;
    }

    private final Context mContext;
    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<E> mList;
}