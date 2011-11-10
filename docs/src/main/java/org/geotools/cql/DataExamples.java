/**
 * 
 */
package org.geotools.cql;

import java.io.IOException;
import java.text.SimpleDateFormat;

import org.apache.commons.io.FileUtils;
import org.geotools.data.DataUtilities;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.WKTReader;

/**
 * This utility class provide the data required by the CQL/ECQL examples.
 * 
 * @author Mauricio Pazos
 * 
 */
final class DataExamples extends ECQLExamples {

    private static SimpleFeature COUNTRY = null;

    private static SimpleFeature CITY = null;

    private DataExamples() {
        // utility class
    }

    /**
     * Creates a feature that represent New York city
     * 
     * @return a Feature
     * @throws Exception
     */
    static public SimpleFeature getInstanceOfCity() throws Exception {

        if (CITY != null) {
            return CITY;
        }

        final SimpleFeatureType type = DataUtilities
                .createType("Location", "geometry:Point:srid=4326," + "cityName:String,"
                        + "over65YearsOld:Double," + "under18YearsOld:Double,"
                        + "population:Integer," + "lastEarthQuake:Date");
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(type);

        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

        Point point = geometryFactory.createPoint(new Coordinate(-17.2053, 11.9517));

        featureBuilder.add(point);
        featureBuilder.add("New York");
        featureBuilder.add(22.6);
        featureBuilder.add(13.4);
        featureBuilder.add(19541453);

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        featureBuilder.add(dateFormatter.parse("1737-11-30T01:30:00Z"));

        CITY = featureBuilder.buildFeature(null);

        return CITY;
    }

    public static SimpleFeature getInstanceOfCountry() throws Exception {

        if (COUNTRY != null) {
            return COUNTRY;
        }
        final SimpleFeatureType type = DataUtilities.createType("Location",
                "geometry:Polygon:srid=4326," + "countryName:String," + "population:Integer,"
                        + "principalMineralResource:String");
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(type);

        WKTReader reader = new WKTReader();
        MultiPolygon geometry = (MultiPolygon) reader.read(usaGeometry());

        featureBuilder.add(geometry);
        featureBuilder.add("USA");
        featureBuilder.add(307006550);
        featureBuilder.add("oil");

        COUNTRY = featureBuilder.buildFeature(null);

        return COUNTRY;
    }

    static private String usaGeometry() {
        try {
            return FileUtils.readFileToString(
                    DataUtilities.urlToFile(DataExamples.class.getResource("usa-geometry.wkt")),
                    "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Show USA geometry can be loaded and used to make a SimpleFeature.
     */
    public static void main(String[] args) {
        try {
            System.out.println(getInstanceOfCountry());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
