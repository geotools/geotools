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
 */
package org.geotools.renderer.lite;


import org.geotools.factory.Hints;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

/**
 * Cache of context information associated with the Symbolizer.
 * <p>
 * Examples of context information include the transformations
 * employed at different stages of the rendering pileline.
 * 
 * @source $URL$
 */
class SymbolizerAssociation{
    /**
     * Full transform from data {@link #crs} through to viewport CRS followed through
     * to the screen.
     */
     public MathTransform  xform = null;
     
     /**
      * Initial transform between data {@link #crs} and viewport CRS.
      */
     public MathTransform  crsxform = null;
     
     /**
      * The source CooridinateReferenceSystem used for the individual Geometries.
      * <p>
      * Although we request Geometry information with {@link Hints#FEATURE_2D} the
      * geometry may still be provided with with 3D ordinates. In this case
      * we will need to post process the information into 2D for rendering.
      */
     public CoordinateReferenceSystem crs = null;
     
     /**
      * Transform used between viewport CRS through to the screen.
      */
     public MathTransform axform;
}
