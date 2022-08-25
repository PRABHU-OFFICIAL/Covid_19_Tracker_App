package com.example.covid19trackerapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StateAdapter extends RecyclerView.Adapter<StateAdapter.Viewholder> {

	private Context context;
	private List<StateModel> stateModelList;

	public StateAdapter(Context context, List<StateModel> stateModelList) {
		this.context = context;
		this.stateModelList = stateModelList;
	}

	@NonNull
	@Override
	public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		return new Viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.state_item , parent , false));
	}

	@Override
	public void onBindViewHolder(@NonNull Viewholder holder, @SuppressLint("RecyclerView") int position) {
		holder.stateName.setText(stateModelList.get(position).getState());
		holder.stateTotalCases.setText(stateModelList.get(position).getConfirmed());

		holder.stateWiseLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(context , IndivisualStateActivity.class);
				intent.putExtra("name" , holder.stateName.getText().toString())
						.putExtra("confirmed" , holder.stateTotalCases.getText().toString())
						.putExtra("confirmedNew" , stateModelList.get(position).getConfirmedNew())
						.putExtra("active" , stateModelList.get(position).getActive())
						.putExtra("death" , stateModelList.get(position).getDeath())
						.putExtra("deathNew" , stateModelList.get(position).getDeathNow())
						.putExtra("recovered" , stateModelList.get(position).getRecovered())
						.putExtra("recoveredNew" , stateModelList.get(position).getRecoveredNew())
						.putExtra("lastUpdate" , stateModelList.get(position).getLastUpdate());

				context.startActivity(intent);
			}
		});
	}

	@Override
	public int getItemCount() {
		return stateModelList.size();
	}

	public void filterList(List<StateModel> list){
		stateModelList = list;
		notifyDataSetChanged();
	}

	public static class Viewholder extends RecyclerView.ViewHolder {

		TextView stateName , stateTotalCases;
		LinearLayout stateWiseLayout;

		public Viewholder(@NonNull View itemView) {
			super(itemView);
			stateName = itemView.findViewById(R.id.stateName);
			stateTotalCases = itemView.findViewById(R.id.stateConfirmed);
			stateWiseLayout = itemView.findViewById(R.id.stateWiseLayout);
		}
	}
}
