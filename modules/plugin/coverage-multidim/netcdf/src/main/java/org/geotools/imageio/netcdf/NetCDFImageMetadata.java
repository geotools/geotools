/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.imageio.netcdf;

import it.geosolutions.imageio.core.CoreCommonImageMetadata;
import java.awt.image.ColorModel;
import java.awt.image.SampleModel;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.util.logging.Logging;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/** Simple image metadata for NetCDF */
public class NetCDFImageMetadata extends CoreCommonImageMetadata {

    static final Logger LOGGER = Logging.getLogger(NetCDFImageMetadata.class);

    public static final String nativeMetadataFormatName =
            "it_geosolutions_imageioimpl_plugins_netcdf_image_1.0";

    public static final String nativeMetadataFormatClassName = NetCDFImageMetadata.class.getName();

    public NetCDFImageMetadata(
            String variableName, SampleModel sm, ColorModel cm, CoordinateReferenceSystem crs) {
        super(true, nativeMetadataFormatName, nativeMetadataFormatClassName, null, null);
        setDatasetName(variableName);
        // width and height, tile structure assuming row based
        setWidth(sm.getWidth());
        setHeight(sm.getHeight());
        setTileWidth(sm.getWidth());
        setTileHeight(1);
        // color and sample models and information coming from them
        setColorModel(cm);
        setSampleModel(sm);
        setNumBands(sm.getNumBands());
        try {
            if (crs != null) {
                // attempt to set projection description
                setProjection(crs.getIdentifiers().iterator().next().toString());
            }
        } catch (Exception e) {
            LOGGER.log(Level.FINE, "Could not add the projection information into the metadata", e);
        }
    }
}
