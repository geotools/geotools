/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.factory;

import org.geotools.data.Parameter;

/**
 * Fake process using some metadata in the annotations
 *
 * @author Andrea Aime - GeoSolutions
 */
@DescribeProcess(title = "Meta", description = "Process used to test the metadata annotations")
public class MetaProcess {

    @DescribeResult(
            name = "value",
            description = "the value provided as input",
            meta = {"mimeTypes=application/shapefile,application/json"})
    public byte[] execute(
            @DescribeParameter(
                            name = "extension",
                            meta = {Parameter.EXT + "=shp"})
                    String ext,
            @DescribeParameter(
                            name = "password",
                            meta = {Parameter.IS_PASSWORD + "=true"})
                    String pwd) {
        return null;
    }
}
