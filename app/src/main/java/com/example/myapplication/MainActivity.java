package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    String token;
    SimpleAdapter mSchedule;
    ListView list;
    ArrayList<HashMap<String, Object>> mylist = new ArrayList<HashMap<String, Object>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setStatusBarColor(findViewById(R.id.statusBarBackground),getResources().getColor(R.color.colorPrimary));
         list = findViewById(R.id.SCHEDULE);


        BottomNavigationView bottomNavigationView=findViewById(R.id.bootom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.homee);
        token = getIntent().getStringExtra("token");
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem mitem) {

                switch (mitem.getItemId())
                {
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(),Profile.class));
                        overridePendingTransition(0,0);
                        return  true;
                    case R.id.homee:
                        return  true;
                }
                return false;
            }
        });
        JSONObject object = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,"http://15.206.124.137:3000/txn", object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Log.d("JSON", String.valueOf(response));
                            Boolean status1= response.getBoolean("status");
                            JSONArray jsonArray= response.getJSONArray("txns");
                            if (status1==true){
                                int n = jsonArray.length();
                                for(int i=0;i<n;i++) {
                                    JSONObject JObject = jsonArray.getJSONObject(i);
                                    String title = JObject.getString("_id");
                                    Toast.makeText(MainActivity.this,""+title,Toast.LENGTH_LONG).show();

                                    HashMap<String, Object> map = new HashMap<String, Object>();
                                    map.put("title",JObject.getString("title") );
                                    map.put("date", JObject.getString("createdDate"));
                                    map.put("amount", JObject.getInt("amount"));
                                    mylist.add(map);
                                    Toast.makeText(MainActivity.this, "home success"+title, Toast.LENGTH_LONG).show();

                                }
                                mSchedule = new SimpleAdapter(MainActivity.this, mylist, R.layout.row,
                                        new String[] {"title", "date", "amount"}, new int[] {R.id.TRAIN_CELL, R.id.FROM_CELL, R.id.TO_CELL});
                                list.setAdapter(mSchedule);

                            }
                            else {
                                Toast.makeText(MainActivity.this,"home fail",Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error", "Error: " + error.getMessage());

            }
        }){
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headerMap = new HashMap<String, String>();
                headerMap.put("Content-Type", "application/json");
                headerMap.put("Authorization", token);
                return headerMap;
            }

        };


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
    public void setStatusBarColor(View statusBar, int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //status bar height
            int statusBarHeight = getStatusBarHeight();
            int actionbarheight=getActionBarHeight();
            //action bar height
            statusBar.getLayoutParams().height =actionbarheight+statusBarHeight;
            statusBar.setBackgroundColor(color);
        }
    }
    public int getActionBarHeight() {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
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

}