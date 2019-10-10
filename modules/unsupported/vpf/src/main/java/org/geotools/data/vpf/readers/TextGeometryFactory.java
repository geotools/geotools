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
import org.geotools.data.vpf.VPFFeatureClass;
import org.geotools.data.vpf.VPFFeatureType;
import org.geotools.data.vpf.VPFLibrary;
import org.geotools.data.vpf.file.VPFFile;
import org.geotools.data.vpf.file.VPFFileFactory;
import org.geotools.data.vpf.ifc.FileConstants;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;

/**
 * Builds text primities
 *
 * @author John Meagher
 * @source $URL$
 */
public class TextGeometryFactory extends VPFGeometryFactory implements FileConstants {

    public synchronized void createGeometry(VPFFeatureType featureType, SimpleFeature values)
            throws SQLException, IOException, IllegalAttributeException {

        Geometry result = this.buildGeometry(featureType.getFeatureClass(), values);

        values.setDefaultGeometry(result);
    }

    public synchronized Geometry buildGeometry(VPFFeatureClass featureClass, SimpleFeature values)
            throws SQLException, IOException, IllegalAttributeException {

        Geometry result = null;

        int textId = Integer.parseInt(values.getAttribute("txt_id").toString());

        // Get the right text table
        String baseDirectory = featureClass.getDirectoryName();
        String tileDirectory = baseDirectory;

        // If the primitive table is there, this coverage is not tiled
        if (!new File(tileDirectory.concat(File.separator).concat(TEXT_PRIMITIVE)).exists()) {
            Short tileId =
                    Short.valueOf(Short.parseShort(values.getAttribute("tile_id").toString()));
            VPFLibrary vpf = featureClass.getCoverage().getLibrary();
            String tileName = (String) vpf.getTileMap().get(tileId);

            if (tileName != null) {

                tileDirectory =
                        tileDirectory.concat(File.separator).concat(tileName.toUpperCase()).trim();
            }
        }
        if (!new File(tileDirectory.concat(File.separator).concat(TEXT_PRIMITIVE)).exists()) {
            return null;
        }

        String textTableName = tileDirectory.concat(File.separator).concat(TEXT_PRIMITIVE);
        VPFFile textFile = VPFFileFactory.getInstance().getFile(textTableName);
        SimpleFeature row = textFile.getRowFromId("id", textId);
        result = (Geometry) row.getAttribute("shape_line");

        return result;
    }
}
