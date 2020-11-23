package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Pincreation extends AppCompatActivity implements View.OnClickListener {


    EditText epin;
    Button bpin;
    int pin;
    String token;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        setContentView(R.layout.activity_pincreation);
        setStatusBarColor(findViewById(R.id.statusBarBackground),getResources().getColor(R.color.colorPrimary));
        epin=findViewById(R.id.pin);
        bpin=findViewById(R.id.pinbutton);
        String ac=getIntent().getStringExtra("activity");
        if (ac.equals("pinactivity")) {

            bpin.setText("Create");

        }else {

            bpin.setText("validate");
        }


        SharedPreferences sharedPreferences = getSharedPreferences("MyPref", 0);
        token= sharedPreferences.getString("token","");
        pin=sharedPreferences.getInt("pin",0);
        bpin.setOnClickListener(this);
    }
    public void setStatusBarColor(View statusBar,int color){
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
    @Override
    public void onClick(View v) {

        if (bpin.getText().toString().equalsIgnoreCase("create")) {


            int p = Integer.parseInt(epin.getText().toString());
            JSONObject object = new JSONObject();
            try {
                object.put("pin", p);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://15.206.124.137:3000/signup/setpin", object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                Log.d("JSON", String.valueOf(response));
                                Boolean status1 = response.getBoolean("status");

                                if (status1 == true) {
                                    Toast.makeText(Pincreation.this, " pin succes", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(Pincreation.this, MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(Pincreation.this, "pin fail", Toast.LENGTH_LONG).show();
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
            }) {
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
        else
        {
if(pin==Integer.parseInt(epin.getText().toString()))
{
    Intent intent=new Intent(Pincreation.this,MainActivity.class);
    startActivity(intent);
}
else {
    Toast.makeText(Pincreation.this,"Worng pin",Toast.LENGTH_LONG).show();

}
        }
    }
}