package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity implements View.OnClickListener {
    EditText num,pass;
    Button login;
    TextView newuser;
    long nu,v;
    String pm;
    SharedPreferences.Editor editor;
    SharedPreferences pref;
ProgressDialog mProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        setContentView(R.layout.activity_login);
        setStatusBarColor(findViewById(R.id.statusBarBackground),getResources().getColor(R.color.colorPrimary));
        num=findViewById(R.id.numberlogin);
        num.requestFocus();
        mProgress = new ProgressDialog(Login.this);
        mProgress.setTitle("Validation");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        pass=findViewById(R.id.lgtxtPwd);
        pref= getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        String ps=pref.getString("password",null);
        pass.setText(pref.getString("password", null));
        v=pref.getLong("number", 0);

        if(pass.getText().toString().equals(""))
        {
Toast.makeText(Login.this,"Enter login details",Toast.LENGTH_LONG).show();
        }
        else
        {
            num.setText(""+v);
            mProgress.show();
            login(v,pass.getText().toString());
        }
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        login=findViewById(R.id.Login);
        newuser=findViewById(R.id.lnkreister);
        login.setOnClickListener(this);
        newuser.setOnClickListener(this);
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
        mProgress.show();
        if (v.getId() == R.id.Login) {
            nu = Long.parseLong(num.getText().toString());
            pm = pass.getText().toString();
                login(nu,pm);


        }


        if (v.getId() == R.id.lnkreister) {

            Intent intent=new Intent(Login.this,Signup.class);
            startActivity(intent);
        }
    }


public  void login(Long num,String pass)
{
    JSONObject object = new JSONObject();
    try {
        //input your API parameters
        object.put("mobile",num);
        object.put("password",pass);
    } catch (JSONException e) {
        e.printStackTrace();
    }
    // Enter the correct url for your api service site
    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,"http://15.206.124.137:3000/login", object,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        Log.d("JSON", String.valueOf(response));
                        Boolean status1= response.getBoolean("status");

                        if (status1==true){
                            editor = pref.edit();
                            editor.putString("password", pm);
                            editor.putLong("number", nu);
                            editor.putString("token",response.getString("x-token"));


                            JSONObject jsonObject= response.getJSONObject("user");
                            if(jsonObject.get("pin").equals(null))
                            {
                                mProgress.dismiss();
                                Intent intent=new Intent(Login.this,Pincreation.class);
                               // intent.putExtra("token",  response.getString("x-token"));
                                startActivity(intent);
                            }
                            else
                            {
                                mProgress.dismiss();
                                Intent intent=new Intent(Login.this,Pincreation.class);
                                intent.putExtra("activity","loginactivity");
                                editor.putInt("pin",jsonObject.getInt("pin"));
                                editor.commit();
                                startActivity(intent);
                            }
                        }
                        else {
                            mProgress.dismiss();
                            login.setError("wrong Credentials/User Not Registerd");
                            Toast.makeText(Login.this,"fail",Toast.LENGTH_LONG).show();
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
    }); /*{
             public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headerMap = new HashMap<String, String>();
                    headerMap.put("Content-Type", "application/json");
                    headerMap.put("Authorization", "Bearer " + );
                    return headerMap;
                }

            };*/

    RequestQueue requestQueue = Volley.newRequestQueue(this);
    requestQueue.add(jsonObjectRequest);
}

}