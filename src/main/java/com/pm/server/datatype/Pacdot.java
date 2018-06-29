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

	private boolean powerdot;

	public Pacdot() {
		powerdot = false;
	}

	public Pacdot(Coordinate location, boolean eaten, boolean powerdot) {
		this.location = location;
		this.eaten = eaten;
		this.powerdot = powerdot;
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

	public boolean isEaten() {
		return eaten;
	}

	public void setEaten() {
		this.eaten = true;
	}

	public void setUneaten() {
		this.eaten = false;
	}

	public boolean isPowerdot() {
		return powerdot;
	}

	public void setAsPowerdot() {
		this.powerdot = true;
	}

	public void setAsNormalPacDot() {
		this.powerdot = false;
	}

}
