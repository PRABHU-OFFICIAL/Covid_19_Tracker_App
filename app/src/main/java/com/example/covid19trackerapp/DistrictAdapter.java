package com.example.covid19trackerapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DistrictAdapter extends RecyclerView.Adapter<DistrictAdapter.ViewHolder>{

	private Context context;
	private List<DistrictModel> districtModelsList;
	private String searchText="";

	public DistrictAdapter(Context context, List<DistrictModel> districtModelsList) {
		this.context = context;
		this.districtModelsList = districtModelsList;
	}

	@NonNull
	@Override
	public DistrictAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		// We are again going to use the same item layout and will just update the data over it
		return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.state_item, parent, false));
	}

	@Override
	public void onBindViewHolder(@NonNull DistrictAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
		holder.tv_districtTotalCases.setText(NumberFormat.getInstance().format(Integer.parseInt(districtModelsList.get(position).getConfirmed())));
		if(searchText.length()>0){
			//color your text here
			int index = districtModelsList.get(position).getDistrict().indexOf(searchText);
			SpannableStringBuilder sb = new SpannableStringBuilder(districtModelsList.get(position).getDistrict());
			Pattern word = Pattern.compile(searchText.toLowerCase());
			Matcher match = word.matcher(districtModelsList.get(position).getDistrict().toLowerCase());
			holder.tv_districtName.setText(sb);

		}else{
			holder.tv_districtName.setText(districtModelsList.get(position).getDistrict());
		}

		holder.districtLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, IndiisualDistrictActivity.class);
				intent.putExtra("name", districtModelsList.get(position).getDistrict());
				intent.putExtra("confirmed", districtModelsList.get(position).getConfirmed());
				intent.putExtra("confirmedNew", districtModelsList.get(position).getNewConfirmed());
				intent.putExtra("active", districtModelsList.get(position).getActive());
				intent.putExtra("death", districtModelsList.get(position).getDeceased());
				intent.putExtra("deathNew", districtModelsList.get(position).getNewDeceased());
				intent.putExtra("recovered", districtModelsList.get(position).getRecovered());
				intent.putExtra("recoveredNew", districtModelsList.get(position).getNewRecovered());
				context.startActivity(intent);
			}
		});
	}

	@Override
	public int getItemCount() {
		return districtModelsList.size();
	}

	@SuppressLint("NotifyDataSetChanged")
	public void filterList(List<DistrictModel> list, String search) {
		districtModelsList = list;
		this.searchText = search;
		notifyDataSetChanged();
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {

		TextView tv_districtName, tv_districtTotalCases;
		LinearLayout districtLayout;

		public ViewHolder(@NonNull View itemView) {
			super(itemView);
			tv_districtName = itemView.findViewById(R.id.stateName);
			tv_districtTotalCases = itemView.findViewById(R.id.stateConfirmed);
			districtLayout = itemView.findViewById(R.id.stateWiseLayout);
		}
	}
}
