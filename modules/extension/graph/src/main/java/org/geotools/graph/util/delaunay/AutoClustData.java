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
import org.geotools.graph.structure.Edge;

/** @author jfc173 */
public class AutoClustData {

    double localMean;
    double localStDev;
    List<Edge> shortEdges;
    List<Edge> longEdges;
    List<Edge> otherEdges;

    /** Creates a new instance of AutoClustData */
    public AutoClustData() {}

    public void setLocalMean(double d) {
        localMean = d;
    }

    public double getLocalMean() {
        return localMean;
    }

    public void setLocalStDev(double d) {
        localStDev = d;
    }

    public double getLocalStDev() {
        return localStDev;
    }

    public void setShortEdges(List<Edge> l) {
        shortEdges = l;
    }

    public List<Edge> getShortEdges() {
        return shortEdges;
    }

    public void setLongEdges(List<Edge> l) {
        longEdges = l;
    }

    public List<Edge> getLongEdges() {
        return longEdges;
    }

    public void setOtherEdges(List<Edge> l) {
        otherEdges = l;
    }

    public List<Edge> getOtherEdges() {
        return otherEdges;
    }
}
