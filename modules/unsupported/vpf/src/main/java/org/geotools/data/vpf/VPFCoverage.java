/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.vpf;

import static org.geotools.data.vpf.ifc.FileConstants.CHARACTER_VALUE_DESCRIPTION_TABLE;
import static org.geotools.data.vpf.ifc.FileConstants.TABLE_FCS;
import static org.geotools.data.vpf.ifc.VPFCoverageIfc.FIELD_COVERAGE_NAME;
import static org.geotools.data.vpf.ifc.VPFCoverageIfc.FIELD_LEVEL;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.geotools.data.vpf.file.VPFFile;
import org.geotools.data.vpf.file.VPFFileFactory;
import org.geotools.data.vpf.ifc.VPFCoverageIfc;
import org.geotools.feature.SchemaException;
import org.opengis.feature.simple.SimpleFeature;

/**
 * A VPF coverage. This class constructs and contains both feature classes and feature types.
 *
 * @author <a href="mailto:jeff@ionicenterprise.com">Jeff Yutzler</a>
 * @source $URL$
 */
public class VPFCoverage {
    /** The description attribute of the coverage */
    private final String description;

    /** List of feature classes part of this coverage */
    private final List featureClasses = new Vector();

    /** List of feature types part of this coverage */
    private final List featureTypes = new Vector(25);

    /** The owning library */
    private final VPFLibrary library;
    /** Path name for the directory containing this coverage */
    private final String pathName;
    /** The topology level (0-3) */
    private final int topologyLevel;

    /** The namespace to create features with. */
    private final URI namespace;

    /**
     * Constructor
     *
     * @param cDirectoryName path to directory containing coverage
     * @throws IOException if the directory does not contain a valid FCS file
     * @throws SchemaException For problems making one of the feature classes as a FeatureType.
     */
    public VPFCoverage(VPFLibrary cLibrary, SimpleFeature feature, String cDirectoryName)
            throws IOException, SchemaException {
        this(cLibrary, feature, cDirectoryName, null);
    }

    /**
     * Constructor with namespace
     *
     * @param cDirectoryName path to directory containing coverage
     * @throws IOException if the directory does not contain a valid FCS file
     * @throws SchemaException For problems making one of the feature classes as a FeatureType.
     */
    public VPFCoverage(
            VPFLibrary cLibrary, SimpleFeature feature, String cDirectoryName, URI namespace)
            throws IOException, SchemaException {
        topologyLevel = Short.parseShort(feature.getAttribute(FIELD_LEVEL).toString());
        library = cLibrary;
        description = feature.getAttribute(VPFCoverageIfc.FIELD_DESCRIPTION).toString();
        this.namespace = namespace;
        pathName =
                cDirectoryName
                        .concat(File.separator)
                        .concat(feature.getAttribute(FIELD_COVERAGE_NAME).toString().toUpperCase());
        discoverFeatureClasses();
        discoverFeatureTypes();
    }

    /** Builds feature classes for the coverage */
    private void discoverFeatureClasses() throws IOException, SchemaException {
        VPFFeatureClass featureClass = null;
        // boolean hasFeatureClass;
        String fcsFileName = pathName + File.separator + TABLE_FCS;
        // AbstractSet featureClassNames = new HashSet();
        String featureClassName;

        // We need to record all of the possible files
        // for each of the feature classes in this coverage
        VPFFile file = VPFFileFactory.getInstance().getFile(fcsFileName);

        // We might want to grab the FCS list and pass it to the feature class
        // constructor just to save time.
        Iterator iter = file.readAllRows().iterator();
        while (iter.hasNext()) {
            SimpleFeature row = (SimpleFeature) iter.next();
            featureClassName = row.getAttribute("feature_class").toString().trim();
            featureClass = new VPFFeatureClass(this, featureClassName, pathName, namespace);
            featureClasses.add(featureClass);
        }
    }

    /**
     * The point of this block of code is to scan the CHAR.VDT file for FACC codes. There is a one
     * to one relationship between FACC codes and feature types, but there is a one to many
     * relationship between feature classes and feature types/FACC codes. Since objects are stored
     * in the file system by feature class, this mechanism allows us to separate features of
     * different feature types in the same feature class.
     *
     * <p>Some coverages do not have a CHAR.VDT file. In these cases, there is a 1:1:1 relationship
     * between the coverage, feature class, and feature type.
     */
    private void discoverFeatureTypes() {
        try {
            Iterator charVDTIter = getCharVDT().readAllRows().iterator();

            while (charVDTIter.hasNext()) {
                // Figure out which featureClass owns it
                SimpleFeature row = (SimpleFeature) charVDTIter.next();
                // String attr = row.getAttribute("attribute").toString().trim().toLowerCase();

                // if (!ALLOWED_FCODE_ATTRIBUTES_LIST.contains(attr)) continue;

                String tableFileName = row.getAttribute("table").toString().trim().toUpperCase();

                // We need to go through all of this
                // so that entries match what is in FCS
                String featureClassName =
                        tableFileName.substring(0, tableFileName.indexOf(".")).toLowerCase();
                Iterator featureClassIter = featureClasses.iterator();

                while (featureClassIter.hasNext()) {
                    VPFFeatureClass featureClass = (VPFFeatureClass) featureClassIter.next();

                    if (featureClassName.equalsIgnoreCase(featureClass.getTypeName())) {
                        VPFFeatureType featureType = new VPFFeatureType(featureClass, row);
                        featureTypes.add(featureType);

                        break;
                    }
                }
            }
        } catch (IOException exc) {
            // If there is no char.vdt,
            // we can assume there is only one feature type
            // and only one feature class
            VPFFeatureClass coverageClass = (VPFFeatureClass) featureClasses.get(0);
            VPFFeatureType featureType = new VPFFeatureType(coverageClass);
            featureTypes.add(featureType);
        }
    }

    /**
     * Look for a char.vdt
     *
     * @return a TableInputStream for the char.vdt for this coverage
     * @throws IOException on any IO problems, particularly not being able to find the char.vdt file
     */
    private VPFFile getCharVDT() throws IOException {
        VPFFile charvdtInputStream = null;
        String charvdtFileName = pathName + File.separator + CHARACTER_VALUE_DESCRIPTION_TABLE;
        charvdtInputStream = VPFFileFactory.getInstance().getFile(charvdtFileName);

        return charvdtInputStream;
    }

    /**
     * Returns the feature classes in the coverage
     *
     * @return the feature classes in the coverage
     */
    public List getFeatureClasses() {
        return featureClasses;
    }

    /**
     * Returns the feature types for this coverage
     *
     * @return a <code>List</code> of the feature types
     */
    public List getFeatureTypes() {
        return featureTypes;
    }

    /**
     * Returns the owning Module (When refactored this will be the VPFDataSource))
     *
     * @return the owning Module
     */
    public VPFLibrary getLibrary() {
        return library;
    }

    /**
     * Returns the coverage name
     *
     * @return the coverage name
     */
    public String getName() {
        String result = pathName.substring(pathName.lastIndexOf(File.separator) + 1);

        return result;
    }

    /**
     * Returns the name of the path of the directory containing the coverage
     *
     * @return the path of the directory containing the coverage
     */
    public String getPathName() {
        return pathName;
    }
    /** @return Returns the topologyLevel. */
    public int getTopologyLevel() {
        return topologyLevel;
    }

    public String getDescription() {
        return this.description;
    }
    /*
     *  (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "VPF Coverage "
                + getName()
                + ". "
                + description
                + "\n"
                + "Topology level "
                + topologyLevel;
    }
}
