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
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotools.metadata.iso;

import java.util.Collection;
import java.util.Locale;

import org.opengis.metadata.citation.Citation;
import org.opengis.metadata.content.FeatureCatalogueDescription;
import org.opengis.util.GenericName;

import org.geotools.metadata.iso.content.ContentInformationImpl;


/**
 * Location of the responsible individual or organization.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Touraïvane
 *
 * @since 2.1
 */
public class FeatureCatalogueDescriptionImpl extends ContentInformationImpl
       implements FeatureCatalogueDescription
{
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -5361236546997056467L;

    /**
     * Indication of whether or not the cited feature catalogue complies with ISO 19110.
     */
    private Boolean compliant;

    /**
     * Language(s) used within the catalogue
     */
    private Collection<Locale> language;

    /**
     * Indication of whether or not the feature catalogue is included with the dataset.
     */
    private Boolean includeWithDataset;

    /**
     * Subset of feature types from cited feature catalogue occurring in dataset.
     */
    private Collection<GenericName> featureTypes;

    /**
     * Complete bibliographic reference to one or more external feature catalogues.
     */
    private Collection<Citation> featureCatalogueCitations;

    /**
     * Construct an initially empty feature catalogue description.
     */
    public FeatureCatalogueDescriptionImpl() {
    }

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @since 2.4
     */
    public FeatureCatalogueDescriptionImpl(final FeatureCatalogueDescription source) {
        super(source);
    }

    /**
     * Returns whether or not the cited feature catalogue complies with ISO 19110.
     */
    public Boolean isCompliant() {
        return compliant;
    }
    /**
     * Set whether or not the cited feature catalogue complies with ISO 19110.
     */
    public synchronized void setCompliant(final Boolean newValue) {
        checkWritePermission();
        compliant = newValue;
    }

    /**
     * Returns the language(s) used within the catalogue
     */
    public synchronized Collection<Locale> getLanguages() {
        return (language = nonNullCollection(language, Locale.class));
    }

    /**
     * Returns the language(s) used within the catalogue
     */
    public synchronized void setLanguages(
            final Collection<? extends Locale> newValues)
    {
        language = copyCollection(newValues, language, Locale.class);
    }

    /**
     * Returns whether or not the feature catalogue is included with the dataset.
     *
     * @todo Return type should be {@link Boolean}.
     */
    public boolean isIncludedWithDataset() {
        return includeWithDataset.booleanValue();
    }

    /**
     * Set whether or not the feature catalogue is included with the dataset.
     */
    public synchronized void setIncludedWithDataset(final Boolean newValue) {
        checkWritePermission();
        includeWithDataset = newValue;
    }

    /**
     * Returns the Complete bibliographic reference to one or more external feature catalogues.
     * 
     * @TODO: needs to annotate the package org.geotools.util before.
     */
    public synchronized Collection<GenericName> getFeatureTypes() {
        return featureTypes = nonNullCollection(featureTypes, GenericName.class);
    }

    /**
     * Returns the Complete bibliographic reference to one or more external feature catalogues.
     */
    public synchronized void setFeatureTypes(
            final Collection<? extends GenericName> newValues)
    {
        featureTypes = copyCollection(newValues, featureTypes, GenericName.class);
    }

    /**
     * Returns the Complete bibliographic reference to one or more external feature catalogues.
     */
    public synchronized Collection<Citation> getFeatureCatalogueCitations() {
        return (featureCatalogueCitations = nonNullCollection(featureCatalogueCitations, Citation.class));
    }

    /**
     * Returns the Complete bibliographic reference to one or more external feature catalogues.
     */
    public synchronized void setFeatureCatalogueCitations(
            final Collection<? extends Citation> newValues)
    {
        featureCatalogueCitations = copyCollection(newValues, featureCatalogueCitations, Citation.class);
    }
}
