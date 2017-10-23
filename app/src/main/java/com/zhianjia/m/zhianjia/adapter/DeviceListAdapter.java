package com.zhianjia.m.zhianjia.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhianjia.m.zhianjia.R;
import com.zhianjia.m.zhianjia.haier.data.ZDeviceItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 7 on 2017/1/11.
 */

public class DeviceListAdapter extends BaseAdapter {

    private Context mContext;
    private List<ZDeviceItemModel> devices;
    public DeviceListAdapter(Context context) {
        mContext = context;
        devices = new ArrayList<>();
    }

    private View.OnClickListener renameOnClickListener;

    @Override
    public int getCount() {
        return devices.size();
    }

    @Override
    public Object getItem(int i) {
        return devices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        DeviceListViewHolder viewHolder;

        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.device_list_item, parent, false);
            viewHolder = new DeviceListViewHolder();


            viewHolder.addressTextView = (TextView) view.findViewById(R.id.tv_address);
            viewHolder.configureText = (TextView) view.findViewById(R.id.btn_set_net);
            viewHolder.unBindText = (TextView) view.findViewById(R.id.btn_unbind);
            viewHolder.nameTextView = (TextView) view.findViewById(R.id.tv_name);
            viewHolder.renameButton = (TextView) view.findViewById(R.id.btn_rename);
            viewHolder.text_statue = (TextView) view.findViewById(R.id.text_statue);
            viewHolder.renameButton.setOnClickListener(renameOnClickListener);
            viewHolder.renameButton.setTag(devices.get(position));
            viewHolder.rnameLayout = (LinearLayout)view.findViewById(R.id.layout_rename);
            viewHolder.rootLayout = (LinearLayout)view.findViewById(R.id.layout_outer_layer);
            viewHolder.haierImg = (ImageView)view.findViewById(R.id.img_icon_haier);


            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (DeviceListViewHolder) view.getTag();
        }
        final ZDeviceItemModel model = devices.get(position);
        if(model!=null) {
            viewHolder.addressTextView.setText(model.getDevice_name());
            if ("0".equals(model.getSelf_device())) {
                // 0 海尔设备
                viewHolder.nameTextView.setText(model.getDevice_id());
            } else {
                Log.e("tag","nameTextView="+"检测仪 ID:" + model.getDevice_id());
                viewHolder.nameTextView.setText("检测仪 ID:" + model.getDevice_id());
            }
            viewHolder.configureText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (callBack != null) {
                        callBack.configureNetWor(model);
                    }
                }
            });

            viewHolder.unBindText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (callBack != null) {
                        callBack.unBind(model);
                    }
                }
            });

            viewHolder.rootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (callBack != null) {
                        callBack.onClickItem(model);
                    }
                }
            });


            if ("0".equals(devices.get(position).getSelf_device())) {
//                viewHolder.rnameLayout.setVisibility(View.GONE);
                viewHolder.haierImg.setVisibility(View.VISIBLE);
            } else {
//                viewHolder.rnameLayout.setVisibility(View.VISIBLE);
                viewHolder.haierImg.setVisibility(View.GONE);
            }
            if ("0".equals(model.getIs_online())){
                viewHolder.text_statue.setVisibility(View.VISIBLE);
            }else{
                viewHolder.text_statue.setVisibility(View.GONE);
            }
        }

        return view;
    }

    public void setRenameOnClickListener(View.OnClickListener renameOnClickListener) {
        this.renameOnClickListener = renameOnClickListener;
    }


    public void refresh(List<ZDeviceItemModel> devices) {
        this.devices.clear();
        this.devices.addAll(devices);

//        UacDevice uacDevice = new UacDevice();
//        uacDevice.setName("海尔U+设备");
//        uacDevice.setId("智能空调");
//        this.devices.add(uacDevice);
//
//        UacDevice uacDevice1 = new UacDevice();
//        uacDevice1.setName("海尔U+设备");
//        uacDevice1.setId("智能燃气灶");
//        this.devices.add(uacDevice1);
//
//        UacDevice uacDevice2 = new UacDevice();
//        uacDevice2.setName("海尔U+设备");
//        uacDevice2.setId("智能空气净化器");
//        this.devices.add(uacDevice2);

        notifyDataSetChanged();


    }



    public  interface CallBack{
        void configureNetWor(ZDeviceItemModel model);

        void unBind(ZDeviceItemModel model);

        void onClickItem(ZDeviceItemModel model);
    }

    public CallBack callBack;

    public void setCallBack(CallBack callBack){
        this.callBack=callBack;
    }
}

class DeviceListViewHolder {
    TextView addressTextView;
    TextView nameTextView;
    TextView renameButton;
    TextView configureText,unBindText,text_statue;
    LinearLayout rnameLayout,rootLayout;
    ImageView haierImg;
}



