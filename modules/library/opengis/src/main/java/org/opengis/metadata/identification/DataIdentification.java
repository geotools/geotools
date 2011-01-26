/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.metadata.identification;

import java.util.Collection;
import java.util.Locale;
import org.opengis.metadata.extent.Extent;
import org.opengis.metadata.spatial.SpatialRepresentationType;
import org.opengis.util.InternationalString;
import org.opengis.annotation.UML;
import org.opengis.annotation.Profile;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;
import static org.opengis.annotation.ComplianceLevel.*;


/**
 * Information required to identify a dataset.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 2.0
 */
@Profile (level=CORE)
@UML(identifier="MD_DataIdentification", specification=ISO_19115)
public interface DataIdentification extends Identification {
    /**
     * Method used to spatially represent geographic information.
     *
     * @return Method(s) used to spatially represent geographic information.
     */
    @Profile (level=CORE)
    @UML(identifier="spatialRepresentationType", obligation=OPTIONAL, specification=ISO_19115)
    Collection<SpatialRepresentationType> getSpatialRepresentationTypes();

    /**
     * Factor which provides a general understanding of the density of spatial data
     * in the dataset.
     *
     * @return Factor which provides a general understanding of the density of spatial data.
     */
    @Profile (level=CORE)
    @UML(identifier="spatialResolution", obligation=OPTIONAL, specification=ISO_19115)
    Collection<? extends Resolution> getSpatialResolutions();

    /**
     * Language(s) used within the dataset.
     *
     * @return Language(s) used.
     */
    @Profile (level=CORE)
    @UML(identifier="language", obligation=MANDATORY, specification=ISO_19115)
    Collection<Locale> getLanguage();

    /**
     * Full name of the character coding standard(s) used for the dataset.
     *
     * @return Name(s) of the character coding standard(s) used.
     */
    @Profile (level=CORE)
    @UML(identifier="characterSet", obligation=CONDITIONAL, specification=ISO_19115)
    Collection<CharacterSet> getCharacterSets();

    /**
     * Main theme(s) of the dataset.
     *
     * @return Main theme(s).
     */
    @Profile (level=CORE)
    @UML(identifier="topicCategory", obligation=MANDATORY, specification=ISO_19115)
    Collection<TopicCategory> getTopicCategories();

    /**
     * Description of the dataset in the producer's processing environment, including items
     * such as the software, the computer operating system, file name, and the dataset size.
     *
     * @return Description of the dataset in the producer's processing environment, or {@code null}.
     */
    @UML(identifier="environmentDescription", obligation=OPTIONAL, specification=ISO_19115)
    InternationalString getEnvironmentDescription();

    /**
     * Additional extent information including the bounding polygon, vertical, and temporal
     * extent of the dataset.
     *
     * @return Additional extent information.
     */
    @Profile (level=CORE)
    @UML(identifier="extent", obligation=OPTIONAL, specification=ISO_19115)
    Collection<? extends Extent> getExtent();

    /**
     * Any other descriptive information about the dataset.
     *
     * @return Other descriptive information, or {@code null}.
     */
    @UML(identifier="supplementalInformation", obligation=OPTIONAL, specification=ISO_19115)
    InternationalString getSupplementalInformation();
}
