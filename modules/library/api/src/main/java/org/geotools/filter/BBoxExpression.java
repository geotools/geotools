/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter;

import com.vividsolutions.jts.geom.Envelope;


/**
 * A convenience expression to form a geometry literal from an  envelope.
 *
 * @author Ian Turton, CCG
 * @source $URL$
 * @version $Id$
 */
public interface BBoxExpression extends LiteralExpression {
    /**
     * Set the bbox for this expression
     *
     * @param env The envelope to set as the bounds.
     *
     * @throws IllegalFilterException If the box can not be created.
     */
    void setBounds(Envelope env) throws IllegalFilterException;
}
