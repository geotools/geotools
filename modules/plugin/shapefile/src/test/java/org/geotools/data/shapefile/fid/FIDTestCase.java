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
package org.geotools.data.shapefile.fid;

import java.io.File;

import org.geotools.data.shapefile.TestCaseSupport;
import org.geotools.data.shapefile.files.ShpFiles;
import org.junit.Before;

/**
 * 
 *
 * @source $URL$
 */
public abstract class FIDTestCase extends TestCaseSupport {

    protected final String TYPE_NAME = "archsites";

    protected File backshp;
    protected File backdbf;
    protected File backshx;
    protected File backprj;
    protected File backqix;
    String filename;
    protected File fixFile;

    protected ShpFiles shpFiles;

    @Before
    public void setUpArchsites() throws Exception {
        backshp = copyShapefiles("shapes/" + TYPE_NAME + ".shp");

        backdbf = sibling(backshp, "dbf");
        backshx = sibling(backshp, "shx");
        backprj =  sibling(backshp, "prj");
        backqix =  sibling(backshp, "qix");

        fixFile =  sibling(backshp, "fix");

        shpFiles = new ShpFiles(backshx);
    }

}
