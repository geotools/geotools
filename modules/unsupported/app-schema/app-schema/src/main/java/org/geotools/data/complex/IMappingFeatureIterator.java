/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2009, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.complex;

import java.util.Iterator;

import org.geotools.feature.FeatureIterator;
import org.opengis.feature.Feature;

/**
 * @author Russell Petty, GSV
 * @version $Id$
 *
 * @source $URL$
 */
public interface IMappingFeatureIterator extends Iterator<Feature>, FeatureIterator<Feature> {

}
