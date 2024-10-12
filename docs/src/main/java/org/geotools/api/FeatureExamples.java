/*
 *    GeoTools Sample code and Tutorials by Open Source Geospatial Foundation, and others
 *    https://docs.geotools.org
 *
 *    To the extent possible under law, the author(s) have dedicated all copyright
 *    and related and neighboring rights to this software to the public domain worldwide.
 *    This software is distributed without any warranty.
 *
 *    You should have received a copy of the CC0 Public Domain Dedication along with this
 *    software. If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */
package org.geotools.api;

import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public class FeatureExamples {

    public void javaFlag() {
        // javaFlag start
        class Flag {
            public Point location;
            public String name;
            public int classification;
            public double height;
        }
        ;

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
