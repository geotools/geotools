package org.geotools.process.feature.gs;

import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.process.gs.GSProcess;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;

@DescribeProcess(title = "feature", description = "Turns a single geometry into a feature collection")
public class FeatureProcess implements GSProcess {

    @DescribeResult(name = "result", description = "The feature collection wrapping the geometry")
    public SimpleFeatureCollection execute(
            @DescribeParameter(name = "geometry", description = "The feature geometry", min = 1) Geometry geometry,
            @DescribeParameter(name = "crs", description = "The geometry CRS (if not already available)") CoordinateReferenceSystem crs,
            @DescribeParameter(name = "typeName", description = "The generated feature type name", min = 1) String name) {
        // get the crs
        if (crs == null) {
            try {
                crs = (CoordinateReferenceSystem) geometry.getUserData();
            } catch (Exception e) {
                // may not have a CRS attached
            }
        }
        if (crs == null && geometry.getSRID() > 0) {
            try {
                crs = CRS.decode("EPSG:" + geometry.getSRID());
            } catch (Exception e) {
                // may not have a CRS attached
            }
        }

        // build the feature type
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName(name);
        tb.add("geom", geometry.getClass(), crs);
        SimpleFeatureType schema = tb.buildFeatureType();

        // build the feature
        SimpleFeature sf = SimpleFeatureBuilder.build(schema, new Object[] { geometry }, null);
        ListFeatureCollection result = new ListFeatureCollection(schema);
        result.add(sf);
        return result;
    }

}