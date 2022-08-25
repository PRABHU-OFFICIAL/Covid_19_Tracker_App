package com.example.covid19trackerapp;

public class DistrictModel {

	private final String district;
	private final String confirmed;
	private final String active;
	private final String recovered;
	private final String deceased;
	private final String newConfirmed;
	private final String newRecovered;
	private final String newDeceased;

	public DistrictModel(String district, String confirmed, String active, String recovered,
	                              String deceased, String newConfirmed, String newRecovered, String newDeceased) {
		this.district = district;
		this.confirmed = confirmed;
		this.active = active;
		this.recovered = recovered;
		this.deceased = deceased;
		this.newConfirmed = newConfirmed;
		this.newRecovered = newRecovered;
		this.newDeceased = newDeceased;
	}

	public String getDistrict() {
		return district;
	}

	public String getConfirmed() {
		return confirmed;
	}

	public String getActive() {
		return active;
	}

	public String getRecovered() {
		return recovered;
	}

	public String getDeceased() {
		return deceased;
	}

	public String getNewConfirmed() {
		return newConfirmed;
	}

	public String getNewRecovered() {
		return newRecovered;
	}

	public String getNewDeceased() {
		return newDeceased;
	}
}
