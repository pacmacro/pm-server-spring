package com.pm.server.response;

public class PacdotCountResponse {

	private Integer total = 0;

	private Integer eaten = 0;

	private Integer uneaten = 0;

	private Integer uneatenPowerdots = 0;

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getEaten() {
		return eaten;
	}

	public void setEaten(Integer eaten) {
		this.eaten = eaten;
	}

	public Integer getUneaten() {
		return uneaten;
	}

	public void setUneaten(Integer uneaten) {
		this.uneaten = uneaten;
	}

	public Integer getUneatenPowerdots() {
		return uneatenPowerdots;
	}

	public void setUneatenPowerdots(Integer uneatenPowerdots) {
		this.uneatenPowerdots = uneatenPowerdots;
	}

	public void incrementTotal() {
		total++;
	}

	public void incrementEaten() {
		eaten++;
	}

	public void incrementUneaten() {
		uneaten++;
	}

	public void incrementUneatenPowerdots() {
		uneatenPowerdots++;
	}

}
