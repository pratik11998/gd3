package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemLongClickListener {
    String token, title1,diatitle,amount2;
    int amount1;
    SimpleAdapter mSchedule;
    ListView list;
    String identity1;
    FloatingActionButton fab;
    ArrayList<HashMap<String, Object>> mylist = new ArrayList<HashMap<String, Object>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setStatusBarColor(findViewById(R.id.statusBarBackground), getResources().getColor(R.color.colorPrimary));
        list = findViewById(R.id.SCHEDULE);
        fab = findViewById(R.id.floatingActionButton);

        fab.setOnClickListener(this);
        list.setOnItemLongClickListener(this);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bootom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.homee);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPref", 0);
        token= sharedPreferences.getString("token","");

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem mitem) {

                switch (mitem.getItemId()) {
                    case R.id.profile:

                        startActivity(new Intent(MainActivity.this, Profile.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.homee:
                        return true;
                }
                return false;
            }
        });
        showlist();
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

    public void showlist() {
        mylist.clear();
        JSONObject object = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "http://15.206.124.137:3000/txn", object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Log.d("JSON", String.valueOf(response));
                            Boolean status1 = response.getBoolean("status");
                            JSONArray jsonArray = response.getJSONArray("txns");
                            if (status1 == true) {
                                int n = jsonArray.length();
                                for (int i = 0; i < n; i++) {
                                    JSONObject JObject = jsonArray.getJSONObject(i);
                                    HashMap<String, Object> map = new HashMap<String, Object>();
                                    map.put("title", JObject.getString("title"));
                                    String URL=JObject.getString("createdDate");
                                    String date1 = URL.substring(0,10);
                                    String time=URL.substring(11,19);
                                    String datetime=date1+"::"+time;
                                    map.put("date", datetime);
                                    map.put("amount", JObject.getInt("amount"));
                                    map.put("id", JObject.getString("_id"));
                                    mylist.add(map);

                                }
                                mSchedule = new SimpleAdapter(MainActivity.this, mylist, R.layout.row,
                                        new String[]{"title", "date", "amount"}, new int[]{R.id.TRAIN_CELL, R.id.FROM_CELL, R.id.TO_CELL});
                                list.setAdapter(mSchedule);

                            } else {
                                Toast.makeText(MainActivity.this, "home fail", Toast.LENGTH_LONG).show();
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

    @Override
    public void onClick(View v) {
        showinsertDialog();
    }

    private void showinsertDialog() {
       SweetAlertDialog sweetAlertDialog= new SweetAlertDialog(this);
        LayoutInflater li = LayoutInflater.from(this);
        View prompt = li.inflate(R.layout.insertdialog, null);
        sweetAlertDialog.setCustomView(prompt);
        final EditText title = prompt.findViewById(R.id.intitle);
        final EditText amount = prompt.findViewById(R.id.inamount);
        sweetAlertDialog.setTitle("Add Data");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.setConfirmButton("add", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                title1 = title.getText().toString();
                amount1 = Integer.parseInt(amount.getText().toString());
                insert(title1, amount1);
                sweetAlertDialog.cancel();

            }
        });
        sweetAlertDialog.setCancelButton("cancel", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.cancel();
            }
        });


        sweetAlertDialog.show();
    }

    public void insert(String title1, int amount1) {
        JSONObject object = new JSONObject();
        try {
            object.put("amount", amount1);
            object.put("title", title1);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://15.206.124.137:3000/txn/add", object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Log.d("JSON", String.valueOf(response));
                            Boolean status1 = response.getBoolean("status");

                            if (status1 == true) {
                                Toast.makeText(MainActivity.this, "added", Toast.LENGTH_LONG).show();
                                showlist();
                            } else {
                                Toast.makeText(MainActivity.this, "fail", Toast.LENGTH_LONG).show();

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

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        HashMap<String, Object> item = mylist.get(position);
        identity1 = item.get("id").toString();
        diatitle=item.get("title").toString();
        amount2=item.get("amount").toString();
        String URL = item.get("date").toString();

        showAlertDialogButtonClicked(view);

        return false;
    }

    public void deletelist(String identity) {
        JSONObject object = new JSONObject();
        try {
            object.put("txn_id", identity);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://15.206.124.137:3000/txn/del", object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Log.d("JSON", String.valueOf(response));
                            Boolean status1 = response.getBoolean("status");

                            if (status1 == true) {
                                Toast.makeText(MainActivity.this, "deleted", Toast.LENGTH_LONG).show();
                                showlist();


                            } else {
                                Toast.makeText(MainActivity.this, "fail", Toast.LENGTH_LONG).show();

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

    public void showAlertDialogButtonClicked(View view) {

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.Theme_AppCompat_Light_Dialog_Alert);
        builder.setTitle("Choose an Action");
        // add a list
        String[] animals = {"Delete", "Edit"};
        builder.setItems(animals, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {


                    case 0:
                        SweetAlertDialog sweetAlertDialog=new SweetAlertDialog(
                                MainActivity.this, SweetAlertDialog.WARNING_TYPE);
                                sweetAlertDialog.setTitleText("Are you sure?")
                                //.setContentText("Won't be able to recover this file!")
                                .setCancelText("Cancel").setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.cancel();
                                    }
                                })
                                .setConfirmText("Yes, delete it!").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                deletelist(identity1);
sweetAlertDialog.cancel();
                            }
                        })
                                .show();
                        break;
                    case 1:
                        showinsertupdateDialog();
                        break;

                }
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showinsertupdateDialog() {
        SweetAlertDialog sweetAlertDialog= new SweetAlertDialog(this);
        LayoutInflater li = LayoutInflater.from(this);
        View prompt = li.inflate(R.layout.insertdialog, null);
        sweetAlertDialog.setCustomView(prompt);
        final EditText title = prompt.findViewById(R.id.intitle);
        final EditText amount = prompt.findViewById(R.id.inamount);
        title.setText(diatitle);
        amount.setText(amount2);
        sweetAlertDialog.setTitle("update Data");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.setConfirmButton("Update", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                title1 = title.getText().toString();
                amount1 = Integer.parseInt(amount.getText().toString());
                update(title1, amount1,identity1);
                sweetAlertDialog.cancel();

            }
        });
        sweetAlertDialog.setCancelButton("cancel", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.cancel();
            }
        });


        sweetAlertDialog.show();





    }

    private void update(String title1, int amount1, String identity2) {
        JSONObject object = new JSONObject();
        try {
            object.put("txn_id", identity2);
            object.put("amount", amount1);
            object.put("title", title1);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://15.206.124.137:3000/txn/update", object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Log.d("JSON", String.valueOf(response));
                            Boolean status1 = response.getBoolean("status");

                            if (status1 == true) {
                                Toast.makeText(MainActivity.this, "updated", Toast.LENGTH_LONG).show();
                                showlist();

                            } else {
                                Toast.makeText(MainActivity.this, "fail", Toast.LENGTH_LONG).show();

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


}