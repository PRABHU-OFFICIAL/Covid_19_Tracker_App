package com.example.covid19trackerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DistrictDataActivity extends AppCompatActivity {

	private DistrictAdapter districtAdapter;
	private List<DistrictModel> districtModelList;
	private SwipeRefreshLayout swipeRefreshLayout;
	private String str_state_name, str_district, str_confirmed, str_confirmed_new, str_active, str_active_new, str_recovered, str_recovered_new,
			str_death, str_death_new;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_district_data);

		RecyclerView districtRecycler = findViewById(R.id.districtRecycler);
		swipeRefreshLayout = findViewById(R.id.activity_district_wise_swipe_refresh_layout);
		EditText search = findViewById(R.id.activity_district_wise_search_editText);

		districtRecycler.setHasFixedSize(true);
		districtRecycler.setLayoutManager(new LinearLayoutManager(this));

		districtModelList = new ArrayList<>();
		districtAdapter = new DistrictAdapter(getApplicationContext(), districtModelList);
		districtRecycler.setAdapter(districtAdapter);

		Intent intent = getIntent();
		str_state_name = intent.getStringExtra("name");

		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				fetchApiData();
				swipeRefreshLayout.setRefreshing(false);
			}
		});

		search.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				List<DistrictModel> list = new ArrayList<>();
				for (DistrictModel item : districtModelList) {
					if (item.getDistrict().toLowerCase().contains(editable.toString().toLowerCase())) {
						list.add(item);
					}
				}
				districtAdapter.filterList(list, editable.toString());
			}
		});

	}

	private void fetchApiData() {

		final RequestQueue requestQueue = Volley.newRequestQueue(this);
		String apiURL = "https://data.covid19india.org/v2/state_district_wise.json";

		JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
				Request.Method.GET,
				apiURL,
				null,
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						try {
							int flag=0;
							districtModelList.clear();
							for (int i=1;i<response.length();i++){
								JSONObject jsonObjectState = response.getJSONObject(i);

								if (str_state_name.toLowerCase().equals(jsonObjectState.getString("state").toLowerCase())){
									JSONArray jsonArrayDistrict = jsonObjectState.getJSONArray("districtData");

									for (int j=0; j<jsonArrayDistrict.length(); j++){
										JSONObject jsonObjectDistrict = jsonArrayDistrict.getJSONObject(j);
										str_district = jsonObjectDistrict.getString("district");
										str_confirmed = jsonObjectDistrict.getString("confirmed");
										str_active = jsonObjectDistrict.getString("active");
										str_death = jsonObjectDistrict.getString("deceased");
										str_recovered = jsonObjectDistrict.getString("recovered");

										JSONObject jsonObjectDistNew = jsonObjectDistrict.getJSONObject("delta");
										str_confirmed_new = jsonObjectDistNew.getString("confirmed");
										str_recovered_new = jsonObjectDistNew.getString("recovered");
										str_death_new = jsonObjectDistNew.getString("deceased");

										//Creating an object of our statewise model class and passing the values in the constructor
										DistrictModel models = new DistrictModel(str_district, str_confirmed,
												str_active, str_recovered, str_death, str_confirmed_new, str_recovered_new,
												str_death_new);
										//adding data to our arraylist
										districtModelList.add(models);
									}
									flag=1;
								}
								if (flag==1)
									break;
							}
							Handler makeDelay = new Handler();
							makeDelay.postDelayed(new Runnable() {
								@SuppressLint("NotifyDataSetChanged")
								@Override
								public void run() {
									districtAdapter.notifyDataSetChanged();
								}
							}, 1000);
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
		requestQueue.add(jsonArrayRequest);
	}

	@Override
	protected void onStart() {
		super.onStart();
		fetchApiData();
	}
}