package com.zhianjia.m.zhianjia.adapter;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zhianjia.m.zhianjia.R;
import com.zhianjia.m.zhianjia.components.data.Wifi;

import java.util.List;

/**
 * Created by 7 on 2017/1/18.
 */

public class WifiListAdapter extends ArrayAdapter<Wifi> {
    private int resourceId;

    public WifiListAdapter(Context context, int resource, List<Wifi> objects) {
        super(context, resource, objects);

        this.resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Wifi wifi = getItem(position);

        View view;
        WifiListViewHolder viewHolder;

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new WifiListViewHolder();
            viewHolder.ssidTextView = (TextView) view.findViewById(R.id.tv_ssid);
            viewHolder.macTextView = (TextView) view.findViewById(R.id.tv_mac);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (WifiListViewHolder) view.getTag();
        }

        viewHolder.ssidTextView.setText(wifi.getSsid());
        viewHolder.macTextView.setText(wifi.getMac());

        return view;
    }
}

class WifiListViewHolder {
    TextView ssidTextView;
    TextView macTextView;
}
