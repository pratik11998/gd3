    package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.textclassifier.ConversationActions;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

    public class Profile extends AppCompatActivity implements View.OnClickListener {
Button signout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setStatusBarColor(findViewById(R.id.statusBarBackground),getResources().getColor(R.color.colorPrimary));
signout=findViewById(R.id.Signout);
signout.setOnClickListener(this);
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
        public void setStatusBarColor(View statusBar, int color) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Window w = getWindow();
                w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                //status bar height
                int statusBarHeight = getStatusBarHeight();
                int actionbarheight = getActionBarHeight();
                //action bar height
                statusBar.getLayoutParams().height = actionbarheight + statusBarHeight;
                statusBar.setBackgroundColor(color);
            }
        }

        public int getActionBarHeight() {
            int actionBarHeight = 0;
            TypedValue tv = new TypedValue();
            if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            }
            return actionBarHeight;
        }


        public int getStatusBarHeight() {
            int result = 0;
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = getResources().getDimensionPixelSize(resourceId);
            }
            return result;
        }

        @Override
        public void onClick(View v) {


        Intent intent=new Intent(Profile.this,Login.class);
        intent.putExtra("pro1","pro1");
        startActivity(intent);
          /*  HttpClient httpclient = new DefaultHttpClient();

            HttpPost httppost = new HttpPost(
                    "https://api.twilio.com/2010-04-01/Accounts/{ACCOUNT_SID}/SMS/Messages");
            String base64EncodedCredentials = "Basic "
                    + Base64.encodeToString(
                    ("AC7377f3f6c21e6263f5db4a889129d3c5" + ":" + "0fcb5f58bb83014a687cca546cfe500a").getBytes(),
                    Base64.NO_WRAP);

            httppost.setHeader("Authorization",
                    base64EncodedCredentials);
            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("From",
                        "+12057497163"));
                nameValuePairs.add(new BasicNameValuePair("To",
                        "+919028687650"));
                nameValuePairs.add(new BasicNameValuePair("Body",
                        "Welcome to Twilio"));

                httppost.setEntity(new UrlEncodedFormEntity(
                        nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                System.out.println("Entity post is: "
                        + EntityUtils.toString(entity));


            } catch (Exception e) {
              Toast.makeText(Profile.this,e.getMessage(),Toast.LENGTH_LONG).show();
            }*/
        }

        }
