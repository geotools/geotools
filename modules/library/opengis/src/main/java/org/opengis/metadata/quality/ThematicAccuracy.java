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

import static org.opengis.annotation.Specification.*;

import org.opengis.annotation.UML;

/**
 * Accuracy of quantitative attributes and the correctness of non-quantitative attributes and of the
 * classifications of features and their relationships.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.0
 */
@UML(identifier = "DQ_ThematicAccuracy", specification = ISO_19115)
public interface ThematicAccuracy extends Element {}
