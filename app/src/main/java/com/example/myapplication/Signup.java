package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;


public class Signup extends AppCompatActivity implements View.OnClickListener {
    EditText pass, number;
    TextView alreadyuser;
    Button Signup;
    String url;
    String em, pm;
   long  nu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        pass = findViewById(R.id.txtPwd);
        number = findViewById(R.id.number);
        alreadyuser = findViewById(R.id.lnkLogin);
        Signup = findViewById(R.id.signup);
        Signup.setOnClickListener(this);
        alreadyuser.setOnClickListener(this);
        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);

    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.signup) {


                nu = Integer.parseInt(number.getText().toString());
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
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,"http://15.206.124.137:3000/signup", object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
             //         Toast.makeText(Signup.this,"String Response : "+ response.toString(),Toast.LENGTH_LONG).show();
                            try {
                                Log.d("JSON", String.valueOf(response));
                               Boolean status1= response.getBoolean("status");

                                if (status1==true){
Toast.makeText(Signup.this,"succes",Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Toast.makeText(Signup.this,"fail",Toast.LENGTH_LONG).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();

                            }
//                        resultTextView.setText("String Response : "+ response.toString());
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("Error", "Error: " + error.getMessage());
                    Toast.makeText(Signup.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonObjectRequest);
        }


        if (v.getId() == R.id.lnkLogin) {
            Intent intent = new Intent(Signup.this, Login.class);
            startActivity(intent);
        }

}
    }
