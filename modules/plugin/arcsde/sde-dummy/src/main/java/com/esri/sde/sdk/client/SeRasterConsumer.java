package com.esri.sde.sdk.client;

public interface SeRasterConsumer {
	
	public static int COMPLETETILES = 0;
	public static int SINGLEFRAMEDONE = 1;
	public static int STATICIMAGEDONE = 2;
	public static int IMAGEERROR = 3;
	
	public void setHints(int h);
	public void setScanLines(int l, byte[] d, byte[] b);
	
	public void rasterComplete(int status);

}
