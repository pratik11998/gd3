    package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

    public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setStatusBarColor(findViewById(R.id.statusBarBackground),getResources().getColor(R.color.colorPrimaryDark));

        BottomNavigationView bottomNavigationView=findViewById(R.id.bootom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem mitem) {

                switch (mitem.getItemId())
                {
                    case R.id.profile:

                        return  true;
                    case R.id.homee:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return  true;
                }
                return false;
            }
        });
    }
        public void setStatusBarColor(View statusBar, int color){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Window w = getWindow();
                w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                //status bar height
                int statusBarHeight = getStatusBarHeight();
                //action bar height
                statusBar.getLayoutParams().height =  statusBarHeight;
                statusBar.setBackgroundColor(color);
            }
        }


        public int getStatusBarHeight() {
            int result = 0;
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = getResources().getDimensionPixelSize(resourceId);
            }
            return result;
        }
}