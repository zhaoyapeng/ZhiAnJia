package com.zhianjia.m.zhianjia.UI.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhianjia.m.zhianjia.R;
import com.zhianjia.m.zhianjia.haier.data.DeviceDetailsItemModel;

/**
 * Created by dp on 2016/12/29.
 */

public class NatureHomeButton extends LinearLayout {

//    private ImageButton imageButton;
//    private TextView textView;
//    private View bottomLineView;

    private boolean isSelected;
    private int selectedDrawableId;
    private int unselectDrawableId;

    private TextView valueText, unitText, titleText;
    private ImageView iconImg;

    private DeviceDetailsItemModel model;
    public NatureHomeButton(Context context) {
        super(context);

        initView(context);
    }

    public NatureHomeButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        initView(context);
    }

    public NatureHomeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context);
    }

    private void initView(Context context) {
        buildComponents(context);
    }

    /**
     * 建立布局
     *
     * @param context 上下文
     */
    private void buildComponents(Context context) {
        LayoutInflater.from(context).inflate(R.layout.nature_home_button, this);

//        imageButton = (ImageButton) findViewById(R.id.nature_home_imagebutton);
//        textView = (TextView) findViewById(R.id.nature_home_textview);
//        bottomLineView = findViewById(R.id.bottom_line);
        valueText = (TextView) findViewById(R.id.text_value);
        unitText = (TextView) findViewById(R.id.text_unit);
        titleText = (TextView) findViewById(R.id.text_title);
        iconImg = (ImageView) findViewById(R.id.img_icon);
    }

    public void makeStyle(boolean isSelected, int selectedDrawableId, int unselectDrawableId, String title, OnClickListener listener) {
        this.setOnClickListener(listener);
        this.selectedDrawableId = selectedDrawableId;
        this.unselectDrawableId = unselectDrawableId;

        setSelected(isSelected);
    }

    public void click() {
//        imageButton.performClick();
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public void setSelected(boolean selected) {
        isSelected = selected;

        if (isSelected) {
            iconImg.setImageResource(selectedDrawableId);
            valueText.setTextColor(getResources().getColor(R.color.home_button_select));
            unitText.setTextColor(getResources().getColor(R.color.home_button_select));
            titleText.setTextColor(getResources().getColor(R.color.home_button_select));
        } else {
            iconImg.setImageResource(unselectDrawableId);
            valueText.setTextColor(getResources().getColor(R.color.home_button_unselect));
            unitText.setTextColor(getResources().getColor(R.color.home_button_unselect));
            titleText.setTextColor(getResources().getColor(R.color.home_button_unselect));
        }
    }


    public void setData(DeviceDetailsItemModel model) {
        this.model=model;
        if(model!=null)
        valueText.setText(model.getValue());

    }

    public DeviceDetailsItemModel getModel(){
        return model;
    }

    public void setIcon(int resId) {
        iconImg.setImageResource(resId);
    }

    public void setTitle(String unit, String title) {
        titleText.setText(title);
        unitText.setText(unit);
    }

    public String getValue() {
        return valueText.getText().toString();
    }

    public float getIntValue() {
        try {
            if (TextUtils.isEmpty(valueText.getText().toString())) {
                return 0;
            } else {
                return Float.parseFloat(valueText.getText().toString());
            }
        } catch (Exception e) {

        }
        return 0;

    }
}
