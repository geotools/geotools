package com.esri.sde.sdk.client;

/**
 * 
 *
 * @source $URL$
 */
public interface SeRasterProducer {
	
	public void addConsumer(SeRasterConsumer arg0);
	public boolean isConsumer(SeRasterConsumer arg0);
	public void removeConsumer(SeRasterConsumer arg0);


}
