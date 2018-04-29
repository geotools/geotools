/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.temporal;

import static org.opengis.annotation.Specification.*;

import org.opengis.annotation.UML;

/**
 * An abstract class with two subclasses for representing a temporal instant and a temporal period.
 *
 * @author Stephane Fellah (Image Matters)
 * @author Alexander Petkov
 * @source $URL$
 */
@UML(identifier = "TM_GeometricPrimitive", specification = ISO_19108)
public interface TemporalGeometricPrimitive extends TemporalPrimitive, Separation {}
