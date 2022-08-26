package com.example.covid19trackerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class IndivisualStateActivity extends AppCompatActivity {

	private TextView tv_confirmed, tv_confirmed_new, tv_active, tv_active_new, tv_death, tv_death_new,
			tv_recovered, tv_recovered_new, tv_lastupdatedate, tv_dist;

	private String str_stateName, str_confirmed, str_confirmed_new, str_active, str_active_new, str_death, str_death_new,
			str_recovered, str_recovered_new, str_lastupdatedate;

	private PieChart pieChart;

	ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_indivisual_state);

		MaterialToolbar toolbar = findViewById(R.id.my_toolbar);
		setSupportActionBar(toolbar);
		toolbar.setTitleCentered(true);

		tv_confirmed = findViewById(R.id.stateConfirmed);
		tv_confirmed_new = findViewById(R.id.stateConfirmedNew);
		tv_active = findViewById(R.id.stateActive);
		tv_active_new = findViewById(R.id.stateActiveNew);
		tv_death = findViewById(R.id.stateDeath);
		tv_death_new = findViewById(R.id.stateDeathNew);
		tv_recovered = findViewById(R.id.stateRecovered);
		tv_recovered_new = findViewById(R.id.stateRecoveredNew);
		tv_lastupdatedate = findViewById(R.id.stateUpdate);
		tv_dist = findViewById(R.id.districtTitle);
		pieChart = findViewById(R.id.statePie);
		LinearLayout districtLayout = findViewById(R.id.districtLayout);

		Intent intent = getIntent();
		str_stateName = intent.getStringExtra("name");
		str_confirmed = intent.getStringExtra("confirmed");
		str_confirmed_new = intent.getStringExtra("confirmedNew");
		str_active = intent.getStringExtra("active");
		str_death = intent.getStringExtra("death");
		str_death_new = intent.getStringExtra("deathNew");
		str_recovered = intent.getStringExtra("recovered");
		str_recovered_new = intent.getStringExtra("recoveredNew");
		str_lastupdatedate = intent.getStringExtra("lastUpdate");

		districtLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(getApplicationContext() , DistrictDataActivity.class)
						.putExtra("name" , str_stateName));
			}
		});

		startDialog();
		Handler delay = new Handler();
		delay.postDelayed(new Runnable() {
			@SuppressLint({"SetTextI18n", "SimpleDateFormat"})
			@Override
			public void run() {
				tv_confirmed.setText(NumberFormat.getInstance().format(Integer.parseInt(str_confirmed)));
				tv_confirmed_new.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(str_confirmed_new)));

				tv_active.setText(NumberFormat.getInstance().format(Integer.parseInt(str_active)));

				int activeNew = Integer.parseInt(str_confirmed_new) - (Integer.parseInt(str_recovered_new) + Integer.parseInt(str_death_new));
				tv_active_new.setText("+"+NumberFormat.getInstance().format(activeNew));

				tv_death.setText(NumberFormat.getInstance().format(Integer.parseInt(str_death)));
				tv_death_new.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(str_death_new)));

				tv_recovered.setText(NumberFormat.getInstance().format(Integer.parseInt(str_recovered)));
				tv_recovered_new.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(str_recovered_new)));

				try {
					Date date = null;
					date = new SimpleDateFormat("dd/MM/yyyy HH:mm" , Locale.US).parse(str_lastupdatedate);
					assert date != null;
					tv_lastupdatedate.setText(new SimpleDateFormat("dd MMM yyyy").format(date));
				} catch (ParseException e) {
					e.printStackTrace();
				}

				tv_dist.setText("District data of "+str_stateName);

				// Pie Chart Data
				pieChart.addPieSlice(new PieModel("Active", Integer.parseInt(str_active), Color.parseColor("#007afe")));
				pieChart.addPieSlice(new PieModel("Recovered", Integer.parseInt(str_recovered), Color.parseColor("#08a045")));
				pieChart.addPieSlice(new PieModel("Deceased", Integer.parseInt(str_death), Color.parseColor("#F6404F")));

				pieChart.startAnimation();

				progressDialog.dismiss();
			}
		} , 1000);
	}

	private void startDialog() {

		progressDialog = new ProgressDialog(this);
		progressDialog.show();
		progressDialog.setContentView(R.layout.progress_dialog);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

	}
}