/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.metadata.content;

import java.util.Collection;
import java.util.Locale;
import org.geotools.api.metadata.citation.Citation;
import org.geotools.api.util.GenericName;

/**
 * Information identifying the feature catalogue.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.0
 */
public interface FeatureCatalogueDescription extends ContentInformation {
    /**
     * Indication of whether or not the cited feature catalogue complies with ISO 19110. This value
     * is optional, and therefore may be null.
     *
     * @return Whether or not the cited feature catalogue complies with ISO 19110, or {@code null}.
     */
    Boolean isCompliant();

    /**
     * Language(s) used within the catalogue.
     *
     * @return Language(s) used within the catalogue.
     */
    Collection<Locale> getLanguages();

    /**
     * Indication of whether or not the feature catalogue is included with the dataset.
     *
     * @return whether or not the feature catalogue is included with the dataset.
     */
    boolean isIncludedWithDataset();

    /**
     * Subset of feature types from cited feature catalogue occurring in dataset.
     *
     * @return Subset of feature types occurring in dataset.
     */
    Collection<? extends GenericName> getFeatureTypes();

    /**
     * Complete bibliographic reference to one or more external feature catalogues.
     *
     * @return Bibliographic reference to one or more external feature catalogues.
     */
    Collection<? extends Citation> getFeatureCatalogueCitations();
}
