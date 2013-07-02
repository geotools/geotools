/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.grid.io;


/**
 * Describes a "dimension" exposed by a structured grid coverage reader. 
 * 
 * @author Simone Giannecchini, GeoSolutions SAS
 * @author Andrea Aime, GeoSolutions SAS
 * @author Daniele Romagnoli, GeoSolutions SAS
 * 
 */
public interface DimensionDescriptor {

    /**
    * The dimension name
    *
    * @return
    */
   String getName();

   /**
    * The dimension unit symbol
    *
    * @return
    */
   String getUnitSymbol();

   /**
    * The dimension units
    *
    * @return
    */
   String getUnits();

   /**
    * The start attribute 
    *
    * @return
    */
   String getStartAttribute();

   /**
    * The end attribute (In case of dimensions with ranges) 
    *
    * @return
    */
   String getEndAttribute();

}

