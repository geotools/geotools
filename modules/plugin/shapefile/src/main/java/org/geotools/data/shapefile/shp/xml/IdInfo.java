/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.shapefile.shp.xml;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Bean of idinfo element of shp.xml.
 * 
 *
 * @source $URL$
 */
public class IdInfo {

    /** spdom/bounding represents */
    Envelope bounding;

    /** spdom/lbounding represents */
    Envelope lbounding;

    /**
     * @return Returns the bounding.
     */
    public Envelope getBounding() {
        return bounding;
    }

    /**
     * @param bounding
     *                The bounding to set.
     */
    public void setBounding(Envelope bounding) {
        this.bounding = bounding;
    }

    /**
     * @return Returns the lbounding.
     */
    public Envelope getLbounding() {
        return lbounding;
    }

    /**
     * @param lbounding
     *                The lbounding to set.
     */
    public void setLbounding(Envelope lbounding) {
        this.lbounding = lbounding;
    }
}
