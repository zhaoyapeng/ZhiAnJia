package com.zhianjia.m.zhianjia.UI.Option;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhianjia.m.zhianjia.R;
import com.zhianjia.m.zhianjia.UI.BaseAppCompatActivity;
import com.zhianjia.m.zhianjia.UI.User.ModifyUserInfoActivity;
import com.zhianjia.m.zhianjia.UI.widget.NavigationBar;
import com.zhianjia.m.zhianjia.components.data.User;
import com.zhianjia.m.zhianjia.components.network.apply.user.LogoutApply;
import com.zhianjia.m.zhianjia.components.network.method.HttpActionListener;
import com.zhianjia.m.zhianjia.config.Const;
import com.zhianjia.m.zhianjia.utils.FileUtils;

public class UserProfile extends BaseAppCompatActivity {

    private NavigationBar navigationBar;
    private TextView nameTextView, cityTextView, addressTextView, areaTextView;
    private Button logoutButton;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_user_profile;
    }

    @Override
    protected void getComponents() {
        navigationBar = (NavigationBar) findViewById(R.id.navigationBar);
        nameTextView = (TextView) findViewById(R.id.name_textview);
        cityTextView = (TextView) findViewById(R.id.city_textview);
        addressTextView = (TextView) findViewById(R.id.address_textview);
        areaTextView = (TextView) findViewById(R.id.area_textview);
        logoutButton = (Button) findViewById(R.id.logout_button);
    }

    @Override
    protected void initView() {
        navigationBar.setNavigationLoginStyle("个人中心");
        navigationBar.showRightArea("修改");
    }

    @Override
    protected void onResume() {
        super.onResume();
        User defaultUser = FileUtils.getDefaultUser();
        Log.e("tag","defaultUser="+new Gson().toJson(defaultUser));
        nameTextView.setText((defaultUser!=null)?defaultUser.getName():"");
        cityTextView.setText((defaultUser!=null)?defaultUser.getCity():"");
        addressTextView.setText((defaultUser!=null)?defaultUser.getAddress():"");
        areaTextView.setText((defaultUser!=null)?defaultUser.getArea():"");
    }

    @Override
    protected void setListener() {
        navigationBar.setLeftAreaClickedListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FileUtils.getDefaultUser() != null) {
                    logout();
                } else {
                    clearDefaultUser();
                }
            }
        });
        navigationBar.setRightAreaClickedListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfile.this,ModifyUserInfoActivity.class);
                intent.putExtra("userName",nameTextView.getText().toString());
                startActivity(intent);
            }
        });
    }

    private void logout() {
        new LogoutApply(FileUtils.getDefaultUser().getToken(), new HttpActionListener() {
            @Override
            public void success(String result) {
                clearDefaultUser();
            }

            @Override
            public void failure(String message) {
                clearDefaultUser();
            }
        });
    }

    private void clearDefaultUser() {
        FileUtils.logout();

        Intent intent = new Intent(Const.Actions.ACTION_ACTIVITY_LOGIN);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra(Const.IntentKeyConst.KEY_FROM_WHERE, Const.Actions.ACTION_ACTIVITY_USER_PROFILE);
        UserProfile.this.startActivity(intent);

        finish();
    }
}
