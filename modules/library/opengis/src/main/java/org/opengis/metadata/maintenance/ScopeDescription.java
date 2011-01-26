/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.metadata.maintenance;

import java.util.Set;
import org.opengis.annotation.UML;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.FeatureType;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Description of the class of information covered by the information.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author  Martin Desruisseaux (IRD)
 * @author  Cory Horner (Refractions Research)
 * @since   GeoAPI 2.0
 */
@UML(identifier="MD_ScopeDescription", specification=ISO_19115)
public interface ScopeDescription {
    /**
     * Attributes to which the information applies.
     *
     * @return Attributes to which the information applies.
     */
    @UML(identifier="attributes", obligation=CONDITIONAL, specification=ISO_19115)
    Set<? extends AttributeType> getAttributes();

    /**
     * Features to which the information applies.
     *
     * @return Features to which the information applies.
     */
    @UML(identifier="features", obligation=CONDITIONAL, specification=ISO_19115)
    Set<? extends FeatureType> getFeatures();

    /**
     * Feature instances to which the information applies.
     *
     * @return Feature instances to which the information applies.
     */
    @UML(identifier="featureInstances", obligation=CONDITIONAL, specification=ISO_19115)
    Set<? extends FeatureType> getFeatureInstances();

    /**
     * Attribute instances to which the information applies.
     *
     * @return Attribute instances to which the information applies.
     *
     * @since GeoAPI 2.1
     */
    @UML(identifier="attributeInstances", obligation=CONDITIONAL, specification=ISO_19115)
    Set<? extends AttributeType> getAttributeInstances();

    /**
     * Dataset to which the information applies.
     *
     * @return Dataset to which the information applies.
     *
     * @since GeoAPI 2.1
     */
    @UML(identifier="dataset", obligation=CONDITIONAL, specification=ISO_19115)
    String getDataset();

    /**
     * Class of information that does not fall into the other categories to
     * which the information applies.
     *
     * @return Class of information that does not fall into the other categories.
     *
     * @since GeoAPI 2.1
     */
    @UML(identifier="other", obligation=CONDITIONAL, specification=ISO_19115)
    String getOther();
}
