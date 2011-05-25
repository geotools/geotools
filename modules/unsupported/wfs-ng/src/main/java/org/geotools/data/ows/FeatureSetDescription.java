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
package org.geotools.data.ows;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import com.vividsolutions.jts.geom.Envelope;


/**
 * <p>
 * Represents a wfs:FeatureType ... and didn't want to use FeatureType as it
 * could get confused with org.geotools.data.FeatureType
 * </p>
 *
 * @author dzwiers
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/wfs-ng/src/main/java/org/geotools/data/ows/FeatureSetDescription.java $
 */
public class FeatureSetDescription {
	/**
	 * Mask for no operation allowed on the FeatureType
	 */
    public static final int NO_OPERATION = 0;
	/**
	 * Mask for query operation allowed on the FeatureType
	 */
    public static final int QUERY_OPERATION = 1;
	/**
	 * Mask for insert operation allowed on the FeatureType
	 */
    public static final int INSERT_OPERATION = 2;
	/**
	 * Mask for update operation allowed on the FeatureType
	 */
    public static final int UPDATE_OPERATION = 4;
	/**
	 * Mask for delete operation allowed on the FeatureType
	 */
    public static final int DELETE_OPERATION = 8;
	/**
	 * Mask for lock operation allowed on the FeatureType
	 */
    public static final int LOCK_OPERATION = 16;

    private String name;
    private URI namespace;
    private String title;
    private String _abstract;
    private String SRS;
    private List keywords;
    private Envelope latLongBoundingBox;
    private int operations;

    /**
     * Converts the string into the appropriate mask.
     * 
     * @param s The String to attempt to convert
     * @return one of the Constant Operation Values
     * @see FeatureSetDescription#DELETE_OPERATION
     * @see FeatureSetDescription#UPDATE_OPERATION
     * @see FeatureSetDescription#LOCK_OPERATION
     * @see FeatureSetDescription#NO_OPERATION
     * @see FeatureSetDescription#QUERY_OPERATION
     * @see FeatureSetDescription#INSERT_OPERATION
     */
    public static int findOperation(String s) {
        if ("Query".equals(s)) {
            return 1;
        }

        if ("Insert".equals(s)) {
            return 2;
        }

        if ("Update".equals(s)) {
            return 4;
        }

        if ("Delete".equals(s)) {
            return 8;
        }

        if ("Lock".equals(s)) {
            return 16;
        }

        return 0;
    }

    /**
     * Converts the int into the appropriate String.
     * 
     * @param i the int to convert, must match exactly.
     * @return A string representation of the int.
     * @see FeatureSetDescription#DELETE_OPERATION
     * @see FeatureSetDescription#UPDATE_OPERATION
     * @see FeatureSetDescription#LOCK_OPERATION
     * @see FeatureSetDescription#NO_OPERATION
     * @see FeatureSetDescription#QUERY_OPERATION
     * @see FeatureSetDescription#INSERT_OPERATION
     */
    public static String writeOperation(int i) {
        switch (i) {
        case 1:
            return "Query";

        case 2:
            return "Insert";

        case 4:
            return "Update";

        case 8:
            return "Delete";

        case 16:
            return "Lock";
        }

        return "";
    }

    /**
     * Converts the int mask into the appropriate set of Strings.
     * 
     * @param i The int mask to attempt to convert
     * @return Set of Strings representing the mask
     * @see FeatureSetDescription#DELETE_OPERATION
     * @see FeatureSetDescription#UPDATE_OPERATION
     * @see FeatureSetDescription#LOCK_OPERATION
     * @see FeatureSetDescription#NO_OPERATION
     * @see FeatureSetDescription#QUERY_OPERATION
     * @see FeatureSetDescription#INSERT_OPERATION
     */
    public static String[] writeOperations(int i) {
        List l = new LinkedList();

        if ((i & 1) == 1) {
            l.add("Query");
        }

        if ((i & 2) == 2) {
            l.add("Insert");
        }

        if ((i & 4) == 4) {
            l.add("Update");
        }

        if ((i & 8) == 8) {
            l.add("Delete");
        }

        if ((i & 16) == 16) {
            l.add("Lock");
        }

        return (String[]) l.toArray(new String[l.size()]);
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the abstracT.
     */
    public String getAbstract() {
        return _abstract;
    }

    /**
     * DOCUMENT ME!
     *
     * @param _abstract The abstracT to set.
     */
    public void setAbstract(String _abstract) {
        this._abstract = _abstract;
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the keywords.
     */
    public List getKeywords() {
        return keywords;
    }

    /**
     * DOCUMENT ME!
     *
     * @param keywords The keywords to set.
     */
    public void setKeywords(List keywords) {
        this.keywords = keywords;
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the latLongBoundingBox.
     */
    public Envelope getLatLongBoundingBox() {
        return latLongBoundingBox;
    }

    /**
     * DOCUMENT ME!
     *
     * @param latLongBoundingBox The latLongBoundingBox to set.
     */
    public void setLatLongBoundingBox(Envelope latLongBoundingBox) {
        this.latLongBoundingBox = latLongBoundingBox;
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the operations.
     */
    public int getOperations() {
        return operations;
    }

    /**
     * DOCUMENT ME!
     *
     * @param operations The operations to set.
     */
    public void setOperations(int operations) {
        this.operations = operations;
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the sRS.
     */
    public String getSRS() {
        return SRS;
    }

    /**
     * DOCUMENT ME!
     *
     * @param srs The sRS to set.
     */
    public void setSRS(String srs) {
        SRS = srs;
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * DOCUMENT ME!
     *
     * @param title The title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }
    /**
     * @return Returns the namespace.
     */
    public URI getNamespace() {
        return namespace;
    }
    /**
     * @param namespace The namespace to set.
     */
    public void setNamespace(URI namespace) {
        this.namespace = namespace;
    }
}
