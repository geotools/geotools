package com.esri.sde.sdk.client;

public class SeExtent extends SeServerObj {
	
	public SeExtent(){}
	public SeExtent(double i, double j, double k, double l) {}
	public SeExtent(double i, double j, double k, double l, double m, double n) {}
	
	public double getMinX() { return 0.0; }
	public double getMaxX() { return 0.0; }
	public double getMinY() { return 0.0; }
	public double getMaxY() { return 0.0; }
	public double getMinZ() { return 0.0; }
	public double getMaxZ() { return 0.0; }
	public void setMinX(double d){}
	public void setMaxX(double d){}
	public void setMinY(double d){}
    public void setMaxY(double d){}
    public void setMinZ(double d){}
    public void setMaxZ(double d){}
}
