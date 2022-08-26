package com.example.covid19trackerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.MaterialToolbar;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView tvConfirmed , tvConfirmedNew , tvActive , tvActiveNew , tvRecovered , tvRecoveredNew
            , tvDeath , tvDeathNew , tvTest , tvTestNew , tvDate , tvTime;
    private SwipeRefreshLayout swipeRefreshLayout;
    private PieChart pieChart;
    String strConfirmed , strConfirmedNew , strActive , strRecovered , strRecoveredNew , strDeath , strDeathNew , strTests , strTestNew , strUpdateTime;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialToolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleCentered(true);

        tvConfirmed = findViewById(R.id.confirmed);
        tvConfirmedNew = findViewById(R.id.confirmedNew);
        tvActive = findViewById(R.id.active);
        tvActiveNew = findViewById(R.id.activeNew);
        tvRecovered = findViewById(R.id.recovered);
        tvRecoveredNew = findViewById(R.id.recoveredNew);
        tvDeath = findViewById(R.id.death);
        tvDeathNew = findViewById(R.id.deathNew);
        tvTest = findViewById(R.id.test);
        tvTestNew = findViewById(R.id.testNew);
        tvDate = findViewById(R.id.date);
        tvTime = findViewById(R.id.time);
        pieChart = findViewById(R.id.pieChart);
        LinearLayout stateLayout = findViewById(R.id.layoutState);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        apiDataFetch();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                apiDataFetch();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        stateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext() , StateDataActivity.class));
            }
        });

    }

    private void apiDataFetch() {

        // Starting with a Dialog
        startDialog();

        // Requesting data from the papi using Volley
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String api = "https://data.covid19india.org/data.json";

        pieChart.clearChart();  // In every Refresh it will show the latest data with an animation.

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,     // Requesting http:// data from server
                api,                                // Taking the api url stored above
                null,        // responseListener for JSON Object
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Getting the first data in array format
                        JSONArray stateData = null;
                        JSONArray testData = null;

                        try {
                            // Initialising the arrays
                            // State data fetched
                            stateData = response.getJSONArray("statewise");
                            JSONObject indiaData = stateData.getJSONObject(0);

                            // Test Sample data fetched
                            testData = response.getJSONArray("tested");
                            JSONObject testDataIndia = testData.getJSONObject(testData.length() - 1);

                            // Now let's create strings to save the data fetched from the api.
                            strConfirmed = indiaData.getString("confirmed");
                            strConfirmedNew = indiaData.getString("deltaconfirmed");
                            strActive = indiaData.getString("active");
                            strRecovered = indiaData.getString("recovered");
                            strRecoveredNew = indiaData.getString("deltarecovered");
                            strDeath = indiaData.getString("deaths");
                            strDeathNew = indiaData.getString("deltadeaths");
                            strUpdateTime = indiaData.getString("lastupdatedtime");
                            strTests = testDataIndia.getString("totalsamplestested");
                            strTestNew = testDataIndia.getString("samplereportedtoday");

                            // Now it's time for a short delay so that our app fetches the data in time and sets them to their required field
                            Handler delay = new Handler();
                            delay.postDelayed(new Runnable() {
                                @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
                                @Override
                                public void run() {
                                    // Our text data will now be stored here
                                    tvConfirmed.setText(NumberFormat.getInstance().format(Integer.parseInt(strConfirmed)));
                                    tvConfirmedNew.setText("+" + NumberFormat.getInstance().format(Integer.parseInt(strConfirmedNew)));
                                    tvActive.setText(NumberFormat.getInstance().format(Integer.parseInt(strActive)));

                                    int ActiveNew = Integer.parseInt(strConfirmedNew) - (Integer.parseInt(strConfirmedNew) + Integer.parseInt(strDeathNew));

                                    tvActiveNew.setText(NumberFormat.getInstance().format(ActiveNew));
                                    tvRecovered.setText(NumberFormat.getInstance().format(Integer.parseInt(strRecovered)));
                                    tvRecoveredNew.setText("+" +NumberFormat.getInstance().format(Integer.parseInt(strRecoveredNew)));
                                    tvDeath.setText(NumberFormat.getInstance().format(Integer.parseInt(strDeath)));
                                    tvDeathNew.setText("+" +NumberFormat.getInstance().format(Integer.parseInt(strDeathNew)));
                                    tvTest.setText(NumberFormat.getInstance().format(Integer.parseInt(strTests)));
                                    tvTestNew.setText("+" +NumberFormat.getInstance().format(Integer.parseInt(strTestNew)));

                                    // Setting Date and Time from fetched results
                                    try {
                                        Date date = null;
                                        date = new SimpleDateFormat("dd/MM/yyyy HH:mm" , Locale.US).parse(strUpdateTime);
                                        assert date != null;
                                        tvDate.setText(new SimpleDateFormat("dd MMM yyyy").format(date));
                                        tvTime.setText(new SimpleDateFormat("hh:mm a").format(date));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    // PieChart data distribution and display
                                    pieChart.addPieSlice(new PieModel("Active" , Integer.parseInt(strActive) , Color.parseColor("#007afe")));
                                    pieChart.addPieSlice(new PieModel("Recovered" , Integer.parseInt(strRecovered) , Color.parseColor("#0ea045")));
                                    pieChart.addPieSlice(new PieModel("Death" , Integer.parseInt(strDeath) , Color.parseColor("#f6404f")));
                                    pieChart.startAnimation();

                                    //Loading screen close (exceptional)
                                    progressDialog.dismiss();

                                }
                            } , 1000);      // Delay for 1000 milliSeconds

                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        // Required else the data fetched will no be set to layout's objects
        requestQueue.add(jsonObjectRequest);
    }

    private void startDialog() {

        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }
}