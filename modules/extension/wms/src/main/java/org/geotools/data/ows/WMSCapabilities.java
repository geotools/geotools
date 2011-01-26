/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
 *    
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data.ows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a base object for a WMS getCapabilities response.
 *
 * @author Richard Gould, Refractions Research
 * @source $URL$
 */
public class WMSCapabilities extends Capabilities {
    private WMSRequest request;
    private Layer layer;
    
    private List<Layer> layers; //cache
	private String[] exceptions;

	/**
	 * Get the root layer, the contents of the Web Map Server are the children
	 * of this layer.
	 * 
	 * @return The "root" Layer for the Web Map Server
	 */
    public Layer getLayer() {
        return layer;
    }

    public void setLayer(Layer layer) {
        this.layer = layer;
    }
        
    /**
     * Access a flat view of the layers available in the WMS.
     * <p>
     * The information available here is the same as doing a top down
     * walk of all the layers available via getLayer().
     * 
     * @return List of all available layers
     */
    public List<Layer> getLayerList() {
        if (layers == null) {
            layers = new ArrayList<Layer>();
            layers.add(layer);
            addChildrenRecursive(layers, layer);
        }        
        return Collections.unmodifiableList(layers);
    }
    
    private void addChildrenRecursive(List<Layer> layers, Layer layer) {
        if (layer.getChildren() != null){
            for( Layer child : layer.getChildren() ){
                layers.add( child );
                addChildrenRecursive( layers, child );
            }
        }
    }

    /**
     * The request contains information about possible Requests that can be 
     * made against this server, including URLs and formats.
     *
     * @return Returns the request.
     */
    public WMSRequest getRequest() {
        return request;
    }

    /**
     * @param request The request to set.
     */
    public void setRequest(WMSRequest request) {
        this.request = request;
    }

	/**
	 * Exceptions declare what kind of formats this server can return exceptions
	 * in. They are used during subsequent requests.
	 */
	public String[] getExceptions() {
	    return exceptions;
	}

	public void setExceptions(String[] exceptions) {
	    this.exceptions = exceptions;
	}
}
