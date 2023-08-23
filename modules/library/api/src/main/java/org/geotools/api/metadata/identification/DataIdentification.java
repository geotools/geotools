/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.metadata.identification;

import java.util.Collection;
import java.util.Locale;
import org.geotools.api.metadata.extent.Extent;
import org.geotools.api.metadata.spatial.SpatialRepresentationType;
import org.geotools.api.util.InternationalString;

/**
 * Information required to identify a dataset.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.0
 */
public interface DataIdentification extends Identification {
    /**
     * Method used to spatially represent geographic information.
     *
     * @return Method(s) used to spatially represent geographic information.
     */
    Collection<SpatialRepresentationType> getSpatialRepresentationTypes();

    /**
     * Factor which provides a general understanding of the density of spatial data in the dataset.
     *
     * @return Factor which provides a general understanding of the density of spatial data.
     */
    Collection<? extends Resolution> getSpatialResolutions();

    /**
     * Language(s) used within the dataset.
     *
     * @return Language(s) used.
     */
    Collection<Locale> getLanguage();

    /**
     * Full name of the character coding standard(s) used for the dataset.
     *
     * @return Name(s) of the character coding standard(s) used.
     */
    Collection<CharacterSet> getCharacterSets();

    /**
     * Main theme(s) of the dataset.
     *
     * @return Main theme(s).
     */
    Collection<TopicCategory> getTopicCategories();

    /**
     * Description of the dataset in the producer's processing environment, including items such as
     * the software, the computer operating system, file name, and the dataset size.
     *
     * @return Description of the dataset in the producer's processing environment, or {@code null}.
     */
    InternationalString getEnvironmentDescription();

    /**
     * Additional extent information including the bounding polygon, vertical, and temporal extent
     * of the dataset.
     *
     * @return Additional extent information.
     */
    Collection<? extends Extent> getExtent();

    /**
     * Any other descriptive information about the dataset.
     *
     * @return Other descriptive information, or {@code null}.
     */
    InternationalString getSupplementalInformation();
}
