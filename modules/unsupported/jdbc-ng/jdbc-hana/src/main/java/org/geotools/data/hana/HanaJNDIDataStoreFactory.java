/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.hana;

import static org.geotools.data.hana.HanaDataStoreFactory.ENCODE_FUNCTIONS;

import java.util.Map;
import org.geotools.jdbc.JDBCJNDIDataStoreFactory;

/**
 * A JNDI data store factory for SAP HANA.
 *
 * @author Stefan Uhrig, SAP SE
 */
public class HanaJNDIDataStoreFactory extends JDBCJNDIDataStoreFactory {

    public HanaJNDIDataStoreFactory() {
        super(new HanaDataStoreFactory());
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected void setupParameters(Map parameters) {
        super.setupParameters(parameters);
        parameters.put(ENCODE_FUNCTIONS.key, ENCODE_FUNCTIONS);
    }
}
