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

import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * List of names of feature types with the same spatial representation (same as spatial attributes).
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 2.0
 */
@UML(identifier="MD_FeatureTypeList", specification=ISO_19115)
public interface FeatureTypeList {
    /**
     * Instance of a type defined in the spatial schema.
     *
     * @return Instance of a type defined in the spatial schema.
     */
    @UML(identifier="spatialObject", obligation=MANDATORY, specification=ISO_19115)
    String getSpatialObject();

    /**
     * Name of the spatial schema used.
     *
     * @return Name of the spatial schema used.
     */
    @UML(identifier="spatialSchemaName", obligation=MANDATORY, specification=ISO_19115)
    String getSpatialSchemaName();
}
