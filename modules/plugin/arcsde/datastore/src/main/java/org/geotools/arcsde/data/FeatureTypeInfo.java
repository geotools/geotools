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
 */
package org.geotools.arcsde.data;

import java.util.logging.Logger;

import net.sf.jsqlparser.statement.select.PlainSelect;

import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeatureType;

import com.esri.sde.sdk.client.SeQueryInfo;

/**
 * Stores information about known ArcSDE feature types or in-process registered "views".
 * 
 * @author Gabriel Roldan (TOPP)
 * @version $Id$
 * @since 2.5
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/arcsde/datastore/src/main/java
 *         /org/geotools/arcsde/data/FeatureTypeInfo.java $
 */
final class FeatureTypeInfo {

    private static final Logger LOGGER = Logging.getLogger(FeatureTypeInfo.class.getName());

    private final SimpleFeatureType featureType;

    private final FIDReader fidStrategy;

    /**
     * Whether the
     */
    private final boolean isWritable;

    /**
     * Whether the sde table is multi versioned. {@code true} implies {@code isView() == false}
     */
    private final boolean versioned;

    /** is it a SDE registered view? */
    private final boolean isView;

    private final PlainSelect definitionQuery;

    private final SeQueryInfo sdeDefinitionQuery;

    /**
     * Creates a FeatureTypeInfo instance of a real ArcSDE table or registered view.
     * 
     * @param featureType
     *            the FeatureType representing the table structure
     * @param fidStrategy
     *            the strategy object used to handle feature ids
     * @param isWritable
     *            whether the user has write (insert and update) priviledges
     * @param isMultiVersion
     *            whether the table is marked as multi versioned
     * @param isView
     *            whether the table is an ArcSDE registered view
     */
    public FeatureTypeInfo(final SimpleFeatureType featureType, final FIDReader fidStrategy,
            final boolean isWritable, final boolean isMultiVersion, final boolean isView) {
        this(featureType, fidStrategy, isWritable, isMultiVersion, isView, null, null);
    }

    /**
     * Creates a FeatureTypeInfo instance of an in-process view, defined by an SQL {@code SELECT}
     * statement in the DataStore's configuration.
     * <p>
     * Such a FeatureType does not match any registered ArcSDE table or view, but is a read only one
     * made out of a sql query at run time.
     * </p>
     * 
     * @param featureType
     *            the FeatureType representing the query structure
     * @param fidStrategy
     *            the strategy object used to handle fids reading
     * @param definitionQuery
     *            the object that represents the SQL SELECT statement for the runtime view
     * @param sdeDefinitionQuery
     *            the object homologous to the {@code definitionQuery}, that holds the query in
     *            ArcSDE Java API terms
     * @see ArcSDEDataStore#registerView(String, PlainSelect)
     */
    public FeatureTypeInfo(final SimpleFeatureType featureType, final FIDReader fidStrategy,
            final PlainSelect definitionQuery, final SeQueryInfo sdeDefinitionQuery) {
        this(featureType, fidStrategy, false, false, false, definitionQuery, sdeDefinitionQuery);
    }

    /**
     * Private full constructor the public ones delegates to
     */
    private FeatureTypeInfo(final SimpleFeatureType featureType, final FIDReader fidStrategy,
            final boolean isWritable, final boolean isMultiVersion, final boolean isView,
            final PlainSelect definitionQuery, final SeQueryInfo sdeDefinitionQuery) {
        assert featureType != null;
        assert fidStrategy != null;

        if (definitionQuery != null || sdeDefinitionQuery != null) {
            if (definitionQuery == null || sdeDefinitionQuery == null) {
                throw new NullPointerException(
                        "both SeQueryInfo and PlainSelect are needed for an in-process view");
            }
            if (isWritable) {
                throw new IllegalArgumentException("In-process views can't be writable");
            }
        }

        this.featureType = featureType;
        this.fidStrategy = fidStrategy;
        this.versioned = isMultiVersion;
        this.isView = isView;
        this.definitionQuery = definitionQuery;
        this.sdeDefinitionQuery = sdeDefinitionQuery;

        if (isView && isWritable) {
            LOGGER.info("Asked to create a writable view feature type, "
                    + "which is not supported. Using it readonly");
            this.isWritable = false;
        } else {
            this.isWritable = isWritable;
        }
    }

    public String getFeatureTypeName() {
        return featureType.getTypeName();
    }

    public SimpleFeatureType getFeatureType() {
        return featureType;
    }

    public FIDReader getFidStrategy() {
        return fidStrategy;
    }

    public boolean isWritable() {
        return isWritable;
    }

    public boolean isInProcessView() {
        return definitionQuery != null;
    }

    public PlainSelect getDefinitionQuery() {
        return definitionQuery;
    }

    public SeQueryInfo getSdeDefinitionQuery() {
        return sdeDefinitionQuery;
    }

    public boolean isVersioned() {
        return versioned;
    }

    public boolean isView() {
        return isView;
    }
}
