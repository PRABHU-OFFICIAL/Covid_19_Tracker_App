package com.example.covid19trackerapp;

public class StateModel {
	private String state;
	private String confirmed;
	private String confirmedNew;
	private String active;
	private String death;
	private String deathNow;
	private String recovered;
	private String recoveredNew;
	private String lastUpdate;

	public StateModel(String state, String confirmed, String confirmedNew, String active, String death,
	                  String deathNow, String recovered, String recoveredNew, String lastUpdate) {
		this.state = state;
		this.confirmed = confirmed;
		this.confirmedNew = confirmedNew;
		this.active = active;
		this.death = death;
		this.deathNow = deathNow;
		this.recovered = recovered;
		this.recoveredNew = recoveredNew;
		this.lastUpdate = lastUpdate;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getConfirmed() {
		return confirmed;
	}

	public void setConfirmed(String confirmed) {
		this.confirmed = confirmed;
	}

	public String getConfirmedNew() {
		return confirmedNew;
	}

	public void setConfirmedNew(String confirmedNew) {
		this.confirmedNew = confirmedNew;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getDeath() {
		return death;
	}

	public void setDeath(String death) {
		this.death = death;
	}

	public String getDeathNow() {
		return deathNow;
	}

	public void setDeathNow(String deathNow) {
		this.deathNow = deathNow;
	}

	public String getRecovered() {
		return recovered;
	}

	public void setRecovered(String recovered) {
		this.recovered = recovered;
	}

	public String getRecoveredNew() {
		return recoveredNew;
	}

	public void setRecoveredNew(String recoveredNew) {
		this.recoveredNew = recoveredNew;
	}

	public String getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
}
