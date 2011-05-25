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

import java.net.URL;

/**
 * Used to hold data regarding Layers. Used by the DescribeLayerResponse
 * 
 * @author Richard Gould
 *
 * @source $URL$
 */
public class LayerDescription {
    private String name;
    private URL wfs;
    private String owsType;
    private URL owsURL;
    
    public String getName() {
        return name;
    }
    public void setName( String name ) {
        this.name = name;
    }
    public String getOwsType() {
        return owsType;
    }
    public void setOwsType( String owsType ) {
        this.owsType = owsType;
    }
    public URL getOwsURL() {
        return owsURL;
    }
    public void setOwsURL( URL owsURL ) {
        this.owsURL = owsURL;
    }
    public String[] getQueries() {
        return queries;
    }
    public void setQueries( String[] queries ) {
        this.queries = queries;
    }
    public URL getWfs() {
        return wfs;
    }
    public void setWfs( URL wfs ) {
        this.wfs = wfs;
    }
    private String[] queries;
}
