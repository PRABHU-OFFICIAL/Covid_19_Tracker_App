package com.example.covid19trackerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.NumberFormat;

public class IndiisualDistrictActivity extends AppCompatActivity {

	private TextView tv_confirmed, tv_confirmed_new, tv_active, tv_active_new,
			tv_recovered, tv_recovered_new, tv_death, tv_death_new;

	String str_districtName, str_confirmed, str_confirmed_new, str_active, str_active_new, str_death, str_death_new,
			str_recovered, str_recovered_new;

	private ProgressDialog progressDialog;

	PieChart pieChart;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_indiisual_district);

		MaterialToolbar toolbar = findViewById(R.id.my_toolbar);
		setSupportActionBar(toolbar);
		toolbar.setTitleCentered(true);

		tv_confirmed = findViewById(R.id.districtConfirmed);
		tv_confirmed_new = findViewById(R.id.districtConfirmedNew);
		tv_active = findViewById(R.id.districtActive);
		tv_active_new = findViewById(R.id.districtActiveNew);
		tv_recovered = findViewById(R.id.districtRecovered);
		tv_recovered_new = findViewById(R.id.districtRecoveredNew);
		tv_death = findViewById(R.id.districtDeath);
		tv_death_new = findViewById(R.id.districtDeathNew);
		pieChart = findViewById(R.id.districtPie);

		Intent intent = getIntent();
		str_districtName = intent.getStringExtra("name");
		str_confirmed = intent.getStringExtra("confirmed");
		str_confirmed_new = intent.getStringExtra("confirmedNew");
		str_active = intent.getStringExtra("active");
		str_death = intent.getStringExtra("death");
		str_death_new = intent.getStringExtra("deathNew");
		str_recovered = intent.getStringExtra("recovered");
		str_recovered_new = intent.getStringExtra("recoveredNew");

		startDialog();

		Handler postDelayToshowProgress = new Handler();
		postDelayToshowProgress.postDelayed(new Runnable() {
			@SuppressLint("SetTextI18n")
			@Override
			public void run() {
				tv_confirmed.setText(NumberFormat.getInstance().format(Integer.parseInt(str_confirmed)));
				tv_confirmed_new.setText("+"+ NumberFormat.getInstance().format(Integer.parseInt(str_confirmed_new)));

				tv_active.setText(NumberFormat.getInstance().format(Integer.parseInt(str_active)));

				int activeNew = Integer.parseInt(str_confirmed_new) - (Integer.parseInt(str_recovered_new) + Integer.parseInt(str_death_new));
				tv_active_new.setText("+"+ NumberFormat.getInstance().format(activeNew));

				tv_death.setText(NumberFormat.getInstance().format(Integer.parseInt(str_death)));
				tv_death_new.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(str_death_new)));

				tv_recovered.setText(NumberFormat.getInstance().format(Integer.parseInt(str_recovered)));
				tv_recovered_new.setText("+"+ NumberFormat.getInstance().format(Integer.parseInt(str_recovered_new)));

				//setting piechart
				pieChart.addPieSlice(new PieModel("Active", Integer.parseInt(str_active), Color.parseColor("#007afe")));
				pieChart.addPieSlice(new PieModel("Recovered", Integer.parseInt(str_recovered), Color.parseColor("#08a045")));
				pieChart.addPieSlice(new PieModel("Deceased", Integer.parseInt(str_death), Color.parseColor("#F6404F")));

				pieChart.startAnimation();

				progressDialog.dismiss();
			}
		},1000);
	}

	private void startDialog() {

		progressDialog = new ProgressDialog(this);
		progressDialog.show();
		progressDialog.setContentView(R.layout.progress_dialog);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

	}
}