package com.esri.sde.sdk.client;

import java.awt.Image;

public class SeRasterScanLineProducer implements SeRasterProducer {
	
	public SeRasterScanLineProducer(SeRasterAttr a, Image i, int scanlines) {}
	
	public void setBitMaskType(int t) {}
	
	public void addConsumer(SeRasterConsumer arg0) {}
	public boolean isConsumer(SeRasterConsumer arg0) { return false; }
	public void removeConsumer(SeRasterConsumer arg0) {}
	public synchronized void startProduction(SeRasterConsumer arg0) {}

}
