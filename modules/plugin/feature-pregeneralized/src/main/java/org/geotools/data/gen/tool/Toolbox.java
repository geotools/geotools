/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.gen.tool;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureWriter;
import org.geotools.data.FileDataStoreFactorySpi;
import org.geotools.data.Transaction;
import org.geotools.data.gen.info.GeneralizationInfosProviderImpl;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.simplify.TopologyPreservingSimplifier;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Utility class
 *
 * <p>1) Validate xml config 2) generalize shape files
 *
 * @author Chrisitan Mueller
 */
@SuppressWarnings("PMD.SystemPrintln")
public class Toolbox {
    /** read args and delegate jobs */
    static String MissingXMLConfig = "Missing XML config file ";

    static String MissingShapeFile = "Missing shape file ";

    static String MissingTargetDir = "Missing target directory ";

    static String MissingGeneralizations =
            "Missing generalization distances as comma seperated list ";

    public static void main(String[] args) {
        Toolbox toolBox = new Toolbox();

        boolean retval = false;
        try {
            retval = toolBox.parse(args);
        } catch (IOException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            retval = false;
        }

        if (retval) System.exit(0);
        else System.exit(1);
    }

    public boolean parse(String args[]) throws IOException {
        if (args.length == 0) {
            System.out.println("Missing cmd validate | generalize");
            return false;
        }

        if ("validate".equalsIgnoreCase(args[0])) {
            if (args.length != 2) {
                System.out.println(MissingXMLConfig);
                return false;
            }
            validate(args[1]);
            System.out.println("Validation of " + args[1] + " successfull");
        } else if ("generalize".equalsIgnoreCase(args[0])) {
            if (args.length < 2) {
                System.out.println(MissingShapeFile);
                return false;
            }
            if (args.length < 3) {
                System.out.println(MissingTargetDir);
                return false;
            }
            if (args.length < 4) {
                System.out.println(MissingGeneralizations);
                return false;
            }

            if (args.length > 4) {
                System.out.println("Too many arguments");
                dumpGeneralizeParameters(args);
                return false;
            }
            dumpGeneralizeParameters(args);
            generalizeShapeFile(args[1], args[2], args[3]);

        } else {
            System.out.println("Unknwon cmd: " + args[0]);
            return false;
        }

        return true;
    }

    protected void validate(String xmlLocation) throws IOException {
        GeneralizationInfosProviderImpl prov = new GeneralizationInfosProviderImpl();
        prov.getGeneralizationInfos(xmlLocation);
    }

    protected void generalizeShapeFile(
            String shapeFileName, String targetDirName, String generalizations) throws IOException {
        File shapeFile = new File(shapeFileName);
        if (shapeFile.exists() == false) throw new IOException("Could not find " + shapeFileName);
        DataStore shapeDS =
                new ShapefileDataStoreFactory().createDataStore(shapeFile.toURI().toURL());

        File targetDir = new File(targetDirName);
        if (targetDir.exists() == false) throw new IOException("Could not find " + targetDir);

        String[] distanceStrings = generalizations.split(",");
        Double[] distanceArray = new Double[distanceStrings.length];
        for (int i = 0; i < distanceStrings.length; i++)
            distanceArray[i] = Double.valueOf(distanceStrings[i]);

        generalizeShapeFile(shapeFile, shapeDS, targetDir, distanceArray);
        shapeDS.dispose();
    }

    @SuppressWarnings("PMD.CloseResource") // writers are actually closed
    protected void generalizeShapeFile(
            File shapeFile, DataStore shapeDS, File targetDir, Double[] distanceArray)
            throws IOException {
        String typeName = shapeDS.getTypeNames()[0];
        SimpleFeatureSource fs = shapeDS.getFeatureSource(typeName);
        SimpleFeatureType ftype = fs.getSchema();
        DataStore[] dataStores = createDataStores(shapeFile, targetDir, ftype, distanceArray);

        SimpleFeatureCollection fcoll = fs.getFeatures();
        SimpleFeatureIterator it = fcoll.features();
        List<FeatureWriter<SimpleFeatureType, SimpleFeature>> writers =
                new ArrayList<FeatureWriter<SimpleFeatureType, SimpleFeature>>();
        try {
            int countTotal = fcoll.size();

            for (int i = 0; i < dataStores.length; i++) {
                writers.add(dataStores[i].getFeatureWriter(typeName, Transaction.AUTO_COMMIT));
            }

            int counter = 0;
            while (it.hasNext()) {
                SimpleFeature feature = it.next();
                for (int i = 0; i < distanceArray.length; i++) {
                    FeatureWriter<SimpleFeatureType, SimpleFeature> w = writers.get(i);
                    SimpleFeature genFeature = w.next();
                    genFeature.setAttributes(feature.getAttributes());
                    Geometry newGeom =
                            TopologyPreservingSimplifier.simplify(
                                    (Geometry) feature.getDefaultGeometry(), distanceArray[i]);
                    genFeature.setDefaultGeometry(newGeom);
                    w.write();
                }
                counter++;
                showProgress(countTotal, counter);
            }
        } finally {
            for (FeatureWriter<SimpleFeatureType, SimpleFeature> w : writers) {
                try {
                    w.close();
                } catch (Exception e) {
                    // ignore on purpose and move on
                }
            }
            it.close();
        }

        for (DataStore ds : dataStores) {
            ds.dispose();
        }
    }

    DataStore[] createDataStores(
            File shapeFile, File targetDir, SimpleFeatureType ft, Double[] distanceArray)
            throws IOException {

        FileDataStoreFactorySpi factory = new ShapefileDataStoreFactory();
        String shapeFileName = shapeFile.getAbsolutePath();

        String newShapeFileRelativeName = null;
        int index = shapeFileName.lastIndexOf(File.separator);
        if (index == -1) newShapeFileRelativeName = shapeFileName;
        else newShapeFileRelativeName = shapeFileName.substring(index + 1);

        DataStore[] result = new DataStore[distanceArray.length];

        for (int i = 0; i < distanceArray.length; i++) {

            String newShapeFileDirName = targetDir.getAbsolutePath();
            if (newShapeFileDirName.endsWith(File.separator) == false)
                newShapeFileDirName += File.separator;
            newShapeFileDirName += distanceArray[i] + File.separator;

            File dir = new File(newShapeFileDirName);
            if (dir.exists() == false) dir.mkdir();

            File file = new File(newShapeFileDirName + newShapeFileRelativeName);

            Map<String, Serializable> params = new HashMap<String, Serializable>();
            params.put(ShapefileDataStoreFactory.URLP.key, file.toURI().toURL());
            result[i] = factory.createNewDataStore(params);
            result[i].createSchema(ft);
            ((ShapefileDataStore) result[i]).forceSchemaCRS(ft.getCoordinateReferenceSystem());
        }
        return result;
    }

    private void dumpGeneralizeParameters(String argv[]) {
        for (int i = 1; i < argv.length; i++) {
            String paramName = null;
            switch (i) {
                case 1:
                    paramName = "Shape file";
                    break;
                case 2:
                    paramName = "Target directory";
                    break;
                case 3:
                    paramName = "Distances";
                    break;
                default:
                    paramName = "?????";
            }
            System.out.printf("%-20s\t%s\n", new Object[] {paramName, argv[i]});
        }
    }

    private int calculatePercentage(int countTotal, int counter) {
        return counter * 100 / countTotal;
    }

    private void showProgress(int countTotal, int counter) {

        if (counter == 1) System.out.print("% |");

        int percentage = calculatePercentage(countTotal, counter);
        int prevPercentage = counter == 1 ? 0 : calculatePercentage(countTotal, counter - 1);

        if (percentage != prevPercentage) System.out.print("#");

        if (counter == countTotal) System.out.println("|");
    }
}
