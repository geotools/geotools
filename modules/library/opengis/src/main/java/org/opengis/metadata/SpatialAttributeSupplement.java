/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.metadata;

import java.util.Collection;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Spatial attributes in the application schema for the feature types.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 2.0
 */
@UML(identifier="MD_SpatialAttributeSupplement", specification=ISO_19115)
public interface SpatialAttributeSupplement {
    /**
     * Provides information about the list of feature types with the same spatial representation.
     *
     * @return The list of feature types with the same spatial representation.
     */
    @UML(identifier="theFeatureTypeList", obligation=MANDATORY, specification=ISO_19115)
    Collection<FeatureTypeList> getFeatureTypeList();
}
