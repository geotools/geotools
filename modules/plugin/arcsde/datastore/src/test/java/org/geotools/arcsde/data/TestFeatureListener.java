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
 *
 */
package org.geotools.arcsde.data;

import java.util.LinkedList;
import java.util.List;

import org.geotools.data.FeatureEvent;
import org.geotools.data.FeatureListener;

/**
 * This class copies out some of the details about each event received.
 * 
 * @author Jody Garnett
 * 
 * @source $URL:
 *         http://svn.osgeo.org/geotools/trunk/modules/plugin/arcsde/datastore/src/test/java/org
 *         /geotools/arcsde/data/TestFeatureListener.java $
 */
public class TestFeatureListener implements FeatureListener {
    List<FeatureEvent> list = new LinkedList<FeatureEvent>();

    public void changed(FeatureEvent e) {
        list.add(new FeatureEvent(e));
    }
}
