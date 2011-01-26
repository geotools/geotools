/*
 *    GeoTools - The Open Source Java GIS Tookit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.demo.referencing;

//java dependancies
import java.io.File;
import java.net.URL;

import org.geotools.data.DataStore;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureTypes;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CRSFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.CoordinateOperation;
import org.opengis.referencing.operation.CoordinateOperationFactory;

import com.vividsolutions.jts.geom.CoordinateFilter;
import com.vividsolutions.jts.geom.Geometry;

/**
 * A simple demo to transform the geometries in a shapefile from the source
 * coordinate reference system (CRS) to a target CRS. Source and target
 * CRS's are given here as well know text (WKT) strings. The default output path
 * is the users home directory.
 *
 * The following method is rather low-level. Typically the higher-level
 * DefaultQuery method demoed in org.geotools.demo.data.ShapeReprojector would
 * be used instead.
 *
 * @source $URL$
 * @version $Id$
 * @author rschulz
 */
public class TransformData {
    
    //hardcoded WKT strings
    private static String SOURCE_WKT = "GEOGCS[\"WGS84\", DATUM[\"WGS84\", SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], PRIMEM[\"Greenwich\", 0.0], UNIT[\"degree\",0.017453292519943295], AXIS[\"Longitude\",EAST], AXIS[\"Latitude\",NORTH]]";
    private static String TARGET_WKT = "PROJCS[\"UTM Zone 14N\", GEOGCS[\"WGS84\", DATUM[\"WGS84\", SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], PRIMEM[\"Greenwich\", 0.0], UNIT[\"degree\",0.017453292519943295], AXIS[\"Longitude\",EAST], AXIS[\"Latitude\",NORTH]], PROJECTION[\"Transverse_Mercator\"], PARAMETER[\"central_meridian\", -99.0], PARAMETER[\"latitude_of_origin\", 0.0], PARAMETER[\"scale_factor\", 0.9996], PARAMETER[\"false_easting\", 500000.0], PARAMETER[\"false_northing\", 0.0], UNIT[\"metre\",1.0], AXIS[\"x\",EAST], AXIS[\"y\",NORTH]]";
    //private static String TARGET_WKT = "PROJCS[\"Lambert\", GEOGCS[\"WGS84\", DATUM[\"WGS84\", SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], PRIMEM[\"Greenwich\", 0.0], UNIT[\"degree\",0.017453292519943295], AXIS[\"Longitude\",EAST], AXIS[\"Latitude\",NORTH]], PROJECTION[\"Lambert_Conformal_Conic_2SP\"], PARAMETER[\"semi_major\", 6378137.0], PARAMETER[\"semi_minor\", 6356752.314245179], PARAMETER[\"central_meridian\", 0.0], PARAMETER[\"standard_parallel_1\", 20.0], PARAMETER[\"standard_parallel_2\", 20.0], PARAMETER[\"latitude_of_origin\", 0.0], PARAMETER[\"false_easting\", 100000.0], PARAMETER[\"false_northing\", 0.0], UNIT[\"metre\",1.0], AXIS[\"x\",EAST], AXIS[\"y\",NORTH]]";

    //private static String WKT_2 = "PROJCS[\"Mercator\", GEOGCS[\"WGS84\", DATUM[\"WGS84\", SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], PRIMEM[\"Greenwich\", 0.0], UNIT[\"degree\",0.017453292519943295], AXIS[\"Longitude\",EAST], AXIS[\"Latitude\",NORTH]], PROJECTION[\"Mercator_2SP\"], PARAMETER[\"semi_major\", 6378137.0], PARAMETER[\"semi_minor\", 6356752.314245179], PARAMETER[\"central_meridian\", 0.0], PARAMETER[\"standard_parallel_1\", 20.0], PARAMETER[\"false_easting\", 0.0], PARAMETER[\"false_northing\", 1000000.0], UNIT[\"metre\",1.0], AXIS[\"x\",EAST], AXIS[\"y\",NORTH]]";
    //private static String WKT_3 = "GEOGCS[\"Sphere\", DATUM[\"WGS84\", SPHEROID[\"WGS84\", 6370997.0, 0],TOWGS84[0,0,0,0,0,0,0]], PRIMEM[\"Greenwich\", 0.0], UNIT[\"degree\",0.017453292519943295], AXIS[\"Longitude\",EAST], AXIS[\"Latitude\",NORTH]]";
    //private static String WKT_4 = "PROJCS[\"TransverseMercator\", GEOGCS[\"Sphere\", DATUM[\"Sphere\", SPHEROID[\"Sphere\", 6370997.0, 0],TOWGS84[0,0,0,0,0,0,0]],PRIMEM[\"Greenwich\", 0.0], UNIT[\"degree\",0.017453292519943295], AXIS[\"Longitude\",EAST], AXIS[\"Latitude\",NORTH]], PROJECTION[\"Transverse_Mercator\"], PARAMETER[\"semi_major\", 6370997], PARAMETER[\"semi_minor\", 6370997], PARAMETER[\"central_meridian\", 0.0], PARAMETER[\"latitude_of_origin\", 0.0], PARAMETER[\"scale_factor\", 1.0], PARAMETER[\"false_easting\", 0.0], PARAMETER[\"false_northing\", 0.0], UNIT[\"metre\",1.0], AXIS[\"x\",EAST], AXIS[\"y\",NORTH]]";

    /** Factory to create coordinate reference systems from WKT strings*/
    private CRSFactory crsFactory = ReferencingFactoryFinder.getCRSFactory(null);
    
    /** Factory to create transformations from a source and target CS */
    private CoordinateOperationFactory coFactory = ReferencingFactoryFinder.getCoordinateOperationFactory(null);
    
    /** Creates a new instance of TransformData */
    public TransformData(URL inURL, URL outURL, String inWKT, String outWKT) {
        SimpleFeatureCollection fc = null;
        FeatureWriter<SimpleFeatureType, SimpleFeature> outFeatureWriter = null;
        try {
            //create the CS's and transformation
            CoordinateReferenceSystem inCRS = crsFactory.createFromWKT(inWKT);
            CoordinateReferenceSystem outCRS = crsFactory.createFromWKT(outWKT);
            System.out.println("source CRS: " + inCRS.getName().getCode());
            System.out.println("target CRS: " + outCRS.getName().getCode());
            CoordinateOperation co = coFactory.createOperation(inCRS, outCRS);
            System.out.println("transform: " + co.getMathTransform().toWKT());
            CoordinateFilter transFilter = new TransformationCoordinateFilter(co.getMathTransform());
            
            //get the input shapefile
            DataStore inStore = new ShapefileDataStore(inURL);
            String name = inStore.getTypeNames()[0];
            SimpleFeatureSource inSource = inStore.getFeatureSource(name);
            fc = inSource.getFeatures();
            SimpleFeatureType inSchema = inSource.getSchema();

            //create the output shapefile
            DataStore outStore = new ShapefileDataStore(outURL);
            outStore.createSchema(FeatureTypes.transform(inSchema, outCRS));
            outFeatureWriter = outStore.getFeatureWriter(outStore.getTypeNames()[0], Transaction.AUTO_COMMIT);
            
            SimpleFeatureIterator i = fc.features();
            while(i.hasNext()) {
                SimpleFeature inFeature = i.next();
                // create a new feature
                SimpleFeature outFeature = outFeatureWriter.next();
                for (int j = 0; j < inFeature.getAttributeCount(); j++) {
                    Object inAttribute = inFeature.getAttribute(j);
                    if (inAttribute instanceof Geometry) {
                        Geometry geom = (Geometry) inAttribute;
                        geom.apply(transFilter);
                        outFeature.setAttribute(j, geom);
                    } else {
                        outFeature.setAttribute(j, inAttribute);
                    }
                }
                // write the new feature
                outFeatureWriter.write();
            }
            
            //close stuff
            fc.close(i);
            outFeatureWriter.close();
            System.out.println("Done");
        } catch (Exception e) {
            System.out.println("Exception: " + e);
            e.printStackTrace();
        }
    }
    
    /**
     * Command line arguments are:
     *   <ul>
     *     <li>in file - input shapefile
     *     <li>out file - output shapefile
     *     <li>in WKT - WKT string for the source coordinate system
     *     <li>out WKT - WKT string for the target coordinate system
     *   </ul>
     *
     * @param args the command line arguments
     * 
     */
    public static void main(String[] args) throws Exception {
        URL inURL, outURL;
        
        if (args.length == 0) {
            inURL = TransformData.class.getClassLoader().getResource("org/geotools/test-data/shapes/statepop.shp");
            outURL = new File(System.getProperty("user.home") + "/statepopTransform.shp").toURI().toURL();
            new TransformData(inURL, outURL, SOURCE_WKT, TARGET_WKT);
        } else if (args.length == 2) {
            inURL = new File(args[0]).toURI().toURL();
            outURL = new File(args[1]).toURI().toURL();
            new TransformData(inURL, outURL, SOURCE_WKT, TARGET_WKT);
        } else if (args.length == 4) {
            inURL = new File(args[0]).toURI().toURL();
            outURL = new File(args[1]).toURI().toURL();
            new TransformData(inURL, outURL, args[2], args[4]);
        } else {
            System.out.println("Usage: java TransformData <in file> <out file> <in WKT> <out WKT>");
        }
    }
    
}
