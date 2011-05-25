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

package org.geotools.data.gen.info;

import java.io.IOException;

/**
 * @author Christian Mueller
 * 
 * Interface for objects creating GeneralizsationInfos
 * 
 *
 *
 * @source $URL$
 */
public interface GeneralizationInfosProvider {

    /**
     * @param source ,
     *            source of the info, concrete class depending on implementation
     * @return a GeneralizationInfos object
     * @throws IOException
     *             in case of failure
     * 
     * An implementor of this method must call {@link GeneralizationInfos#validate()} otherwise,
     * behavior is unexpected
     */
    public GeneralizationInfos getGeneralizationInfos(Object source) throws IOException;
}
