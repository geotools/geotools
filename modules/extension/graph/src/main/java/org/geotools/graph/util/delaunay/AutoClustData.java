/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.graph.util.delaunay;

import java.util.List;

/**
 *
 * @author jfc173
 *
 * @source $URL$
 */
public class AutoClustData {
    
    double localMean;
    double localStDev; 
    List shortEdges;
    List longEdges;
    List otherEdges;
    
    /** Creates a new instance of AutoClustData */
    public AutoClustData() {
    }
    
    public void setLocalMean(double d){
        localMean = d;
    }
    
    public double getLocalMean(){
        return localMean;
    }
    
    public void setLocalStDev(double d){
        localStDev = d;
    }
    
    public double getLocalStDev(){
        return localStDev;
    }
    
    public void setShortEdges(List l){
        shortEdges = l;
    }
    
    public List getShortEdges(){
        return shortEdges;
    }
       
    public void setLongEdges(List l){
        longEdges = l;
    }
    
    public List getLongEdges(){
        return longEdges;
    }
    
    public void setOtherEdges(List l){
        otherEdges = l;
    }
    
    public List getOtherEdges(){
        return otherEdges;
    }    
}
