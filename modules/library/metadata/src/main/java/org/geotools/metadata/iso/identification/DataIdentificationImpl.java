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
package org.geotools.metadata.iso.identification;

import java.util.Collection;
import java.util.Locale;
import java.nio.charset.Charset;

import org.opengis.metadata.citation.Citation;
import org.opengis.metadata.identification.DataIdentification;
import org.opengis.metadata.extent.Extent;
import org.opengis.metadata.extent.GeographicBoundingBox;
import org.opengis.metadata.extent.GeographicDescription;
import org.opengis.metadata.identification.CharacterSet;
import org.opengis.metadata.identification.Resolution;
import org.opengis.metadata.identification.TopicCategory;
import org.opengis.metadata.spatial.SpatialRepresentationType;
import org.opengis.util.InternationalString;


/**
 * Information required to identify a dataset.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Touraïvane
 *
 * @since 2.1
 */
public class DataIdentificationImpl extends IdentificationImpl implements DataIdentification {
    /**
     * Serial number for compatibility with different versions.
     */
    private static final long serialVersionUID = -4418520352804939785L;

    /**
     * Method used to spatially represent geographic information.
     */
    private Collection<SpatialRepresentationType> spatialRepresentationTypes;

    /**
     * Factor which provides a general understanding of the density of spatial data
     * in the dataset.
     */
    private Collection<Resolution> spatialResolutions;

    /**
     * Language(s) used within the dataset.
     */
    private Collection<Locale> language;

    /**
     * Full name of the character coding standard used for the dataset.
     */
    private Collection<CharacterSet> characterSets;

    /**
     * Main theme(s) of the datset.
     */
    private Collection<TopicCategory> topicCategories;

    /**
     * Description of the dataset in the producers processing environment, including items
     * such as the software, the computer operating system, file name, and the dataset size
     */
    private InternationalString environmentDescription;

    /**
     * Additional extent information including the bounding polygon, vertical, and temporal
     * extent of the dataset.
     */
    private Collection<Extent> extent;

    /**
     * Any other descriptive information about the dataset.
     */
    private InternationalString supplementalInformation;

    /**
     * Constructs an initially empty data identification.
     */
    public DataIdentificationImpl() {
    }

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @since 2.4
     */
    public DataIdentificationImpl(final DataIdentification source) {
        super(source);
    }

    /**
     * Creates a data identification initialized to the specified values.
     */
    public DataIdentificationImpl(final Citation citation,
                                  final InternationalString abstracts,
                                  final Collection<? extends Locale> language,
                                  final Collection<? extends TopicCategory> topicCategories)
    {
        super(citation, abstracts);
        setLanguage       (language       );
        setTopicCategories(topicCategories);
    }

    /**
     * Method used to spatially represent geographic information.
     */
    public synchronized Collection<SpatialRepresentationType> getSpatialRepresentationTypes() {
        return (spatialRepresentationTypes = nonNullCollection(spatialRepresentationTypes,
                SpatialRepresentationType.class));
    }

    /**
     * Set the method used to spatially represent geographic information.
     */
    public synchronized void setSpatialRepresentationTypes(
            final Collection<? extends SpatialRepresentationType> newValues)
    {
        spatialRepresentationTypes = copyCollection(newValues, spatialRepresentationTypes,
                                                    SpatialRepresentationType.class);
    }

    /**
     * Factor which provides a general understanding of the density of spatial data
     * in the dataset.
     */
    public synchronized Collection<Resolution> getSpatialResolutions() {
        return (spatialResolutions = nonNullCollection(spatialResolutions, Resolution.class));
    }

    /**
     * Set the factor which provides a general understanding of the density of spatial data
     * in the dataset.
     */
    public synchronized void setSpatialResolutions(
            final Collection<? extends Resolution> newValues)
    {
        spatialResolutions = copyCollection(newValues, spatialResolutions, Resolution.class);
    }

    /**
     * Language(s) used within the dataset.
     */
    public synchronized Collection<Locale> getLanguage() {
        return language = nonNullCollection(language, Locale.class);
    }

    /**
     * Set the language(s) used within the dataset.
     */
    public synchronized void setLanguage(final Collection<? extends Locale> newValues)  {
        language = copyCollection(newValues, language, Locale.class);
    }

    /**
     * Full name of the character coding standard used for the dataset.
     */
    public synchronized Collection<CharacterSet> getCharacterSets() {
        return (characterSets = nonNullCollection(characterSets, CharacterSet.class));
    }

    /**
     * Set the full name of the character coding standard used for the dataset.
     */
    public synchronized void setCharacterSets(final Collection<? extends CharacterSet> newValues) {
        characterSets = copyCollection(newValues, characterSets, CharacterSet.class);
    }

    /**
     * Main theme(s) of the datset.
     */
    public synchronized Collection<TopicCategory> getTopicCategories()  {
        return (topicCategories = nonNullCollection(topicCategories, TopicCategory.class));
    }

    /**
     * Set the main theme(s) of the datset.
     */
    public synchronized void setTopicCategories(
            final Collection<? extends TopicCategory> newValues)
    {
        topicCategories = copyCollection(newValues, topicCategories, TopicCategory.class);
    }

    /**
     * Description of the dataset in the producers processing environment, including items
     * such as the software, the computer operating system, file name, and the dataset size.
     */
    public InternationalString getEnvironmentDescription() {
        return environmentDescription;
    }

    /**
     * Set the description of the dataset in the producers processing environment.
     */
    public synchronized void setEnvironmentDescription(final InternationalString newValue)  {
        checkWritePermission();
        environmentDescription = newValue;
    }

    /**
     * Additional extent information including the bounding polygon, vertical, and temporal
     * extent of the dataset.
     */
    public synchronized Collection<Extent> getExtent() {
        return (extent = nonNullCollection(extent, Extent.class));
    }

    /**
     * Set additional extent information.
     */
    public synchronized void setExtent(final Collection<? extends Extent> newValues) {
        extent = copyCollection(newValues, extent, Extent.class);
    }

    /**
     * Any other descriptive information about the dataset.
     */
    public InternationalString getSupplementalInformation() {
        return supplementalInformation;
    }

    /**
     * Set any other descriptive information about the dataset.
     */
    public synchronized void setSupplementalInformation(final InternationalString newValue) {
        checkWritePermission();
        supplementalInformation = newValue;
    }
}
