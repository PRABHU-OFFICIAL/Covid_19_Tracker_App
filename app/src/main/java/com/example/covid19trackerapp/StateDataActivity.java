package com.example.covid19trackerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StateDataActivity extends AppCompatActivity {

	EditText searchState;
	RecyclerView stateRecycler;
	StateAdapter stateAdapter;
	List<StateModel> stateModelList;
	SwipeRefreshLayout swipeRefreshLayout;
	private String str_state, str_confirmed, str_confirmed_new, str_active, str_active_new, str_recovered, str_recovered_new,
			str_death, str_death_new, str_lastupdatedate;
	ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_state_data);

		swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
		searchState = findViewById(R.id.searchState);

		stateRecycler = findViewById(R.id.stateRecycler);
		stateRecycler.setHasFixedSize(true);
		stateRecycler.setLayoutManager(new LinearLayoutManager(this));

		stateModelList = new ArrayList<>();
		stateAdapter = new StateAdapter(StateDataActivity.this, stateModelList);
		stateRecycler.setAdapter(stateAdapter);

		// Again fetch data
		fetchApiData();

		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				fetchApiData();
				swipeRefreshLayout.setRefreshing(false);
			}
		});

		searchState.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				List<StateModel> list = new ArrayList<>();
				for (StateModel item : stateModelList){
					if (item.getState().toLowerCase().contains(editable.toString().toLowerCase())){
						list.add(item);
					}
				}
				stateAdapter.filterList(list);
			}
		});

	}

	private void fetchApiData() {

		startDialog();

		RequestQueue requestQueue = Volley.newRequestQueue(this);
		String api = "https://data.covid19india.org/data.json";

		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
				Request.Method.GET,
				api,
				null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							JSONArray jsonArray = response.getJSONArray("statewise");
							stateModelList.clear();

							for (int i = 1; i < jsonArray.length() ; i++){
								JSONObject statewise = jsonArray.getJSONObject(i);

								//After fetching, storing the data into strings
								str_state = statewise.getString("state");

								str_confirmed = statewise.getString("confirmed");
								str_confirmed_new = statewise.getString("deltaconfirmed");

								str_active = statewise.getString("active");

								str_death = statewise.getString("deaths");
								str_death_new = statewise.getString("deltadeaths");

								str_recovered = statewise.getString("recovered");
								str_recovered_new = statewise.getString("deltarecovered");
								str_lastupdatedate = statewise.getString("lastupdatedtime");

								//Creating an object of our statewise model class and passing the values in the constructor
								StateModel stateWiseModel = new StateModel(str_state, str_confirmed, str_confirmed_new, str_active,
										str_death, str_death_new, str_recovered, str_recovered_new, str_lastupdatedate);
								//adding data to our arraylist
								stateModelList.add(stateWiseModel);

							}

							Handler delay = new Handler();
							delay.postDelayed(new Runnable() {
								@Override
								public void run() {
									stateAdapter.notifyDataSetChanged();
								}
							}, 1000);

							progressDialog.dismiss();

						}
						catch (JSONException e) {
							e.printStackTrace();
						}
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						error.printStackTrace();
					}
				});
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