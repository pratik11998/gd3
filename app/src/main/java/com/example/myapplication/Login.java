package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
    long nu;
    String pm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        setContentView(R.layout.activity_login);
        num=findViewById(R.id.numberlogin);
        num.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        pass=findViewById(R.id.lgtxtPwd);
        login=findViewById(R.id.Login);
        newuser=findViewById(R.id.lnkreister);
        login.setOnClickListener(this);
        newuser.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.Login) {
            nu = Long.parseLong(num.getText().toString());
            pm = pass.getText().toString();


            JSONObject object = new JSONObject();
            try {
                //input your API parameters
                object.put("mobile",nu);
                object.put("password",pm);
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

                                   JSONObject jsonObject= response.getJSONObject("user");
                                        if(jsonObject.get("pin").equals(null))
                                        {
                                            Intent intent=new Intent(Login.this,Pincreation.class);
                                            intent.putExtra("token",  response.getString("x-token"));
                                            startActivity(intent);
                                        }
                                        else
                                        {

                                        }
                                }
                                else {
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


        if (v.getId() == R.id.lnkreister) {

            Intent intent=new Intent(Login.this,Signup.class);
            startActivity(intent);
        }
    }



}