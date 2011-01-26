/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.metadata.quality;

import org.opengis.annotation.UML;
import static org.opengis.annotation.Specification.*;


/**
 * Accuracy of non-quantitative attributes.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author  Martin Desruisseaux (IRD)
 * @author  Cory Horner (Refractions Research)
 * @since   GeoAPI 2.1
 */
@UML(identifier="DQ_NonQuantitativeAttributeAccuracy", specification=ISO_19115)
public interface NonQuantitativeAttributeAccuracy extends ThematicAccuracy {
}
