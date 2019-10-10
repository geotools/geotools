/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.opengis;

import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class FeatureExamples {

    public void javaFlag() {
        // javaFlag start
        class Flag {
            public Point location;
            public String name;
            public int classification;
            public double height;
        };

        GeometryFactory geomFactory = JTSFactoryFinder.getGeometryFactory();

        Flag here = new Flag();
        here.location = geomFactory.createPoint(new Coordinate(23.3, -37.2));
        here.name = "Here";
        here.classification = 3;
        here.height = 2.0;
        // javaFlag end
    }

    public void featureFlag() {
        // featureFlag start
        SimpleFeatureTypeBuilder b = new SimpleFeatureTypeBuilder();
        b.setName("Flag");
        b.setCRS(DefaultGeographicCRS.WGS84);
        b.add("location", Point.class);
        b.add("name", String.class);
        b.add("classification", Integer.class);
        b.add("height", Double.class);
        SimpleFeatureType type = b.buildFeatureType();

        GeometryFactory geomFactory = JTSFactoryFinder.getGeometryFactory();

        SimpleFeatureBuilder f = new SimpleFeatureBuilder(type);
        f.add(geomFactory.createPoint(new Coordinate(23.3, -37.2)));
        f.add("here");
        f.add(3);
        f.add(2.0);
        SimpleFeature feature = f.buildFeature("fid.1");
        // featureFlag end
    }
}
