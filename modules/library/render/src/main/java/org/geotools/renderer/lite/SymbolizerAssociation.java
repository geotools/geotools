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
import org.opengis.referencing.operation.MathTransform2D;

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
     * Full transform from data {@link #crs} through to viewport CRS followed throug
     * to the screen.
     */
     public MathTransform2D  xform = null;
     
     /**
      * Initial transform between data {@link #crs} and viewport CRS.
      */
     public MathTransform2D  crsxform = null;
     
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
     public MathTransform2D axform;
     
     /**
      * Post-process geometry to be 2D only.
      * 
      * This flag is employed to reproduce the functionality
      * of {@link Hints#FEATURE_2D}.
      * <ul>
      * <li>null: Feature results have not yet been checked, unknown if post processing is required.</li>
      * <li>True: Feature content is expected to be forced into WGS84 prior to use</li>
      * <li>False: Feature content is known to be 2D only and can be used as provided</li>
      * </ul>
      */
     public Boolean forceWGS84 = null;
}
