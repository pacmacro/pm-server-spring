package com.pm.server.datatype;

/**
 * Basic datatype for a pacdot.
 *
 */
public class Pacdot {

	/**
	 * Should never be null.
	 */
	private Coordinate location;

	private boolean eaten;

	private boolean powerdot = false;

	public Pacdot() {
	}

	public Pacdot(boolean powerdot) {
		this.powerdot = powerdot;
	}

	public Coordinate getLocation() {
		return location;
	}

	public void setLocation(Coordinate location) {
		this.location = location;
	}

	public boolean getEaten() {
		return eaten;
	}

	public void setEaten(boolean eaten) {
		this.eaten = eaten;
	}

	public boolean getPowerdot() {
		return powerdot;
	}

	public void setPowerdot(boolean powerdot) {
		this.powerdot = powerdot;
	}

}
