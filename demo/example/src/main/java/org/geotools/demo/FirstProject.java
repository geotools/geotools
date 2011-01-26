/*
 *    GeoTools - The Open Source Java GIS Tookit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.demo;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.measure.unit.Unit;
import javax.swing.JOptionPane;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.GeoTools;
import org.geotools.swing.data.JFileDataStoreChooser;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;

/**
 * The example code for the "FirstProject" in the GeoTools wiki.
 * <p>
 * This code matches these examples:
 * <ul>
 * <li><a href="http://docs.codehaus.org/display/GEOTDOC/03+First+Project">First Project</a>
 * <li><a href="http://docs.codehaus.org/display/GEOTDOC/04+How+to+Read+a+Shapefile">How to Read a
 * Shapefile</a>
 * </ul>
 * 
 * The code illustrates how to open a shapefile and collect data about the features contained within
 * it.
 * 
 * @author Jody Garnett
 *
 * @source $URL$
 */
public class FirstProject {

    /**
     * Opens a shapefile provided by the user (that should contain line or polygon features), then
     * iterates over the features within it to calculate the sum of their line or boundary length.
     * The user can specify the path/name of the shapefile as an argument on the command line;
     * otherwise a dialog is displayed to prompt for the file.
     * <p>
     * <b>Note:</b> If specifying the shapefile at the command line, don't forget the quotes around
     * your path if there are spaces!
     * <p>
     * The code for this method illustrates:
     * <ul>
     * <li>connecting to the shapefile using a {@code DataStore} object
     * <li>accessing features in the shapefile
     * <li>getting spatial data for each feature
     * </ul>
     * 
     * @param args
     *            Optionally, the path and name of the shapefile
     * 
     * @throws java.lang.Exception
     *             if the shapefile cannot be opened
     */
    public static void main(String[] args) throws Exception {
        System.out.println("Welcome to GeoTools:" + GeoTools.getVersion());

        /*
         * Get the shapefile, prompting for its path/name if this was not provided on the command
         * line
         */
        File file = promptShapeFile(args);

        try {
            /*
             * Attmpt to find a GeoTools DataStore that can handle the shapefile
             */
            Map<String, Serializable> connectParameters = new HashMap<String, Serializable>();

            connectParameters.put("url", file.toURI().toURL());
            connectParameters.put("create spatial index", true);

            DataStore dataStore = DataStoreFinder.getDataStore(connectParameters);
            if (dataStore == null) {
                Logger.getLogger(FirstProject.class.getName()).log(Level.WARNING,
                        "No DataStore found to handle" + file.getPath());
                System.exit(1);
            }

            /*
             * We are now connected to the shapefile. Get the type name of the features within it
             */
            String[] typeNames = dataStore.getTypeNames();
            String typeName = typeNames[0];

            /*
             * Iterate through the features, collecting some spatial data (line or boundary length)
             * on each one
             */
            SimpleFeatureSource featureSource =
                    dataStore.getFeatureSource(typeName);

            SimpleFeatureCollection collection =
                    featureSource.getFeatures();

            SimpleFeatureIterator iterator = collection.features();

            double totalLength = 0.0;
            try {
                while (iterator.hasNext()) {
                    SimpleFeature feature = iterator.next();

                    /*
                     * The spatial portion of the feature is represented by a Geometry object
                     */
                    Geometry geometry = (Geometry) feature.getDefaultGeometry();
                    totalLength += geometry.getLength();
                }
            } finally {
                /*
                 * You MUST explicitly close the feature iterator otherwise terrible things will
                 * happen !!!
                 */
                if (iterator != null) {
                    iterator.close();
                }
            }

            /*
             * Find out what units the features are measured in so that
             * we can display the results with this info
             */
            String units = "";
            CoordinateReferenceSystem crs = featureSource.getSchema().getCoordinateReferenceSystem();
            if (crs != null) {
                Unit<?> distanceUnit = crs.getCoordinateSystem().getAxis(0).getUnit();
                units = distanceUnit.toString();
            }
            JOptionPane.showMessageDialog(null,
                    String.format("Total length is %.4f %s", totalLength, units));

        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }

        System.exit(0);
    }

    /**
     * This method takes the command line argument from the main method which will either be empty
     * or the name of a shapefile to open (only the first argument is examined). If empty, a dialog
     * is displayed to prompt the user for the shapefile.
     * <p>
     * 
     * @return The shapefile as a new {@code File} object
     */
    private static File promptShapeFile(String[] args) {
        File file = null;

        // check if the filename was provided on the command line
        if (args.length > 0) {
            file = new File(args[0]);
            if (file.exists()) {
                return file;
            }

            // file didn't exist - see if the user wants to continue
            int rtnVal = JOptionPane.showConfirmDialog(null,
                    "Can't find " + file.getName() + ". Choose another ?",
                    "Input shapefile", JOptionPane.YES_NO_OPTION);
            if (rtnVal != JOptionPane.YES_OPTION) {
                return null;
            }
        }

        // display a data store file chooser dialog for shapefiles
        return JFileDataStoreChooser.showOpenFile("shp", null);
    }
}
