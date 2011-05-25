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
package org.geotools.data.vpf.readers;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import org.geotools.data.vpf.VPFFeatureType;
import org.geotools.data.vpf.file.VPFFile;
import org.geotools.data.vpf.file.VPFFileFactory;
import org.geotools.data.vpf.ifc.FileConstants;

import org.geotools.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Geometry;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:jeff@ionicenterprise.com">Jeff Yutzler</a>
 *
 * @source $URL$
 */
public class LineGeometryFactory extends VPFGeometryFactory implements FileConstants {
    /* (non-Javadoc)
     * @see com.ionicsoft.wfs.jdbc.geojdbc.module.vpf.VPFGeometryFactory#createGeometry(com.ionicsoft.wfs.jdbc.geojdbc.module.vpf.VPFIterator)
     */
    public void createGeometry(VPFFeatureType featureType, SimpleFeature values) throws SQLException, IOException, IllegalAttributeException{
        Geometry result = null;
        int edgeId = Integer.parseInt(values.getAttribute("edg_id").toString());
//        VPFFeatureType featureType = (VPFFeatureType) values.getFeatureType();
        
        // Get the right edge table
    	String baseDirectory = featureType.getFeatureClass().getDirectoryName();
    	String tileDirectory = baseDirectory;

    	// If the primitive table is there, this coverage is not tiled
        if(!new File(tileDirectory.concat(File.separator).concat(EDGE_PRIMITIVE)).exists()){
            Short tileId = new Short(Short.parseShort(values.getAttribute("tile_id").toString()));
            tileDirectory = tileDirectory.concat(File.separator).concat(featureType.getFeatureClass().getCoverage().getLibrary().getTileMap().get(tileId).toString()).trim();
        }

        String edgeTableName = tileDirectory.concat(File.separator).concat(EDGE_PRIMITIVE);
        VPFFile edgeFile = VPFFileFactory.getInstance().getFile(edgeTableName);
        SimpleFeature row = edgeFile.getRowFromId("id", edgeId);
        result = (Geometry)row.getAttribute("coordinates");
        values.setDefaultGeometry(result);
    }
}
