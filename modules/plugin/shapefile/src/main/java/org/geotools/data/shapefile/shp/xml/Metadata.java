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

/**
 * Bean capturing shapefile metadata information.
 * <p>
 * To create please use the included ShpXmlFileReader, this is only a data
 * object.
 * </p>
 * <p>
 * Note: This bean currently extends MetadataEntity to allow for uses with
 * Discovery.search( QueryRequest ). When QueryRequest can actually handle
 * normal java beans we can remove this restrictions.
 * </p>
 * 
 * @author jgarnett
 * @since 0.3
 *
 * @source $URL$
 */
public class Metadata {

    /** identification information */
    IdInfo idinfo;

    /**
     * @return Returns the idinfo.
     */
    public IdInfo getIdinfo() {
        return idinfo;
    }

    /**
     * @param idinfo
     *                The idinfo to set.
     */
    public void setIdinfo(IdInfo idinfo) {
        this.idinfo = idinfo;
    }
}
