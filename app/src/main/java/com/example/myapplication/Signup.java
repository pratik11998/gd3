package com.example.myapplication;



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
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class Signup extends AppCompatActivity implements View.OnClickListener {
    EditText pass, number;
    TextView alreadyuser;
    Button Signup;
    String url;
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    String  pm;
long nu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        setContentView(R.layout.activity_signup);
        setStatusBarColor(findViewById(R.id.statusBarBackground),getResources().getColor(R.color.colorPrimary));
        pass = findViewById(R.id.txtPwd);
        number = findViewById(R.id.number);
        alreadyuser = findViewById(R.id.lnkLogin);
        Signup = findViewById(R.id.signup);
        Signup.setOnClickListener(this);
        alreadyuser.setOnClickListener(this);



    }
    public void setStatusBarColor(View statusBar,int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //status bar height
            int statusBarHeight = getStatusBarHeight();
            //int actionbarheight=getActionBarHeight();
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
 /*   public int getActionBarHeight() {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }*/
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.signup) {


    nu = Long.parseLong(number.getText().toString());
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
                            try {
                                Log.d("JSON", String.valueOf(response));
                               Boolean status1= response.getBoolean("status");

                                if (status1==true){
                                   Toast.makeText(Signup.this,"succes",Toast.LENGTH_LONG).show();
                                    Intent intent=new Intent(Signup.this,Pincreation.class);
                                    pref= getApplicationContext().getSharedPreferences("MyPref", 0);
                                    editor = pref.edit();
                                    editor.putString("token",response.getString("x-token"));
                                    editor.commit();
                                         intent.putExtra("activity","pinactivity");
                                    startActivity(intent);
                                }
                                else {
                                    Toast.makeText(Signup.this,"fail",Toast.LENGTH_LONG).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();

                            }
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
