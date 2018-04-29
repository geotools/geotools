/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.internal;

import static org.geotools.data.wfs.internal.WFSOperationType.DESCRIBE_FEATURETYPE;

import javax.xml.namespace.QName;

public class DescribeFeatureTypeRequest extends WFSRequest {

    private QName typeName;

    public DescribeFeatureTypeRequest(WFSConfig config, WFSStrategy strategy) {
        super(DESCRIBE_FEATURETYPE, config, strategy);
    }
}
