/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
/*
 * NOTICE OF RELEASE TO THE PUBLIC DOMAIN
 *
 * This work was created by employees of the USDA Forest Service's
 * Fire Science Lab for internal use.  It is therefore ineligible for
 * copyright under title 17, section 105 of the United States Code.  You
 * may treat it as you would treat any public domain work: it may be used,
 * changed, copied, or redistributed, with or without permission of the
 * authors, for free or for compensation.  You may not claim exclusive
 * ownership of this code because it is already owned by everyone.  Use this
 * software entirely at your own risk.  No warranty of any kind is given.
 *
 * A copy of 17-USC-105 should have accompanied this distribution in the file
 * 17USC105.html.  If not, you may access the law via the US Government's
 * public websites:
 *   - http://www.copyright.gov/title17/92chap1.html#105
 *   - http://www.gpoaccess.gov/uscode/  (enter "17USC105" in the search box.)
 */
package org.geotools.coverage.grid.io.imageio.geotiff;

import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * This exception is thrown when the problem with reading the GeoTiff file has to do with constructing either the raster
 * to model transform, or the coordinate system. A GeoTiffException:
 *
 * <p>
 *
 * <ul>
 *   <li>encapsulates the salient information in the GeoTiff tags, making the values available as read only properties.
 *   <li>sends the appropriate log message to the log stream
 *   <li>produces a readable message property for later retrieval
 * </ul>
 *
 * <p>This exception is expected to be thrown when there is absolutely nothing wrong with the GeoTiff file which
 * produced it. In this case, the exception is reporting an unsupported coordinate system description or raster to model
 * transform, or some other unrecognized configuration of the GeoTIFFWritingUtilities tags. By doing so, it attempts to
 * record enough information so that the maintainers can support it in the future.
 *
 * @author Bryce Nordgren / USDA Forest Service
 * @author Simone Giannecchini
 */
public final class GeoTiffException extends IOException {

    /** */
    private static final long serialVersionUID = 1008533682021487024L;

    private GeoTiffIIOMetadataDecoder metadata = null;

    /**
     * Constructs an instance of <code>GeoTiffException</code> with the specified detail message.
     *
     * @param metadata The metadata from the GeoTIFFWritingUtilities image causing the error.
     * @param msg the detail message.
     */
    public GeoTiffException(GeoTiffIIOMetadataDecoder metadata, String msg, Throwable t) {
        super(msg);
        this.metadata = metadata;
        if (t != null) this.initCause(t);
    }

    /**
     * Getter for property modelTransformation.
     *
     * @return Value of property modelTransformation.
     */
    public AffineTransform getModelTransformation() {
        if (metadata != null) return metadata.getModelTransformation();
        return null;
    }

    /**
     * Getter for property geoKeys.
     *
     * @return Value of property geoKeys.
     */
    public GeoKeyEntry[] getGeoKeys() {
        return metadata != null
                ? metadata.getGeoKeys()
                        .toArray(new GeoKeyEntry[metadata.getGeoKeys().size()])
                : null;
    }

    @Override
    public String getMessage() {
        final StringWriter text = new StringWriter(1024);
        final PrintWriter message = new PrintWriter(text);

        // Header
        message.println("GEOTIFF Module Error Report");

        // start with the message the user specified
        message.println(super.getMessage());

        // do the model pixel scale tags
        message.print("ModelPixelScaleTag: ");
        if (metadata != null) {
            final PixelScale modelPixelScales = metadata.getModelPixelScales();
            if (modelPixelScales != null) {
                message.println("["
                        + modelPixelScales.getScaleX()
                        + ","
                        + modelPixelScales.getScaleY()
                        + ","
                        + modelPixelScales.getScaleZ()
                        + "]");
            } else {
                message.println("NOT AVAILABLE");
            }
        } else message.println("NOT AVAILABLE");

        // do the model tie point tags
        message.print("ModelTiePointTag: ");
        if (metadata != null) {
            final TiePoint[] modelTiePoints = metadata.getModelTiePoints();
            if (modelTiePoints != null) {
                final int numTiePoints = modelTiePoints.length;
                message.println("(" + numTiePoints + " tie points)");
                for (int i = 0; i < numTiePoints; i++) {
                    message.print("TP #" + i + ": ");
                    message.print("[" + modelTiePoints[i].getValueAt(0));
                    message.print("," + modelTiePoints[i].getValueAt(1));
                    message.print("," + modelTiePoints[i].getValueAt(2));
                    message.print("] -> [" + modelTiePoints[i].getValueAt(3));
                    message.print("," + modelTiePoints[i].getValueAt(4));
                    message.println("," + modelTiePoints[i].getValueAt(5) + "]");
                }
            } else message.println("NOT AVAILABLE");
        } else message.println("NOT AVAILABLE");

        // do the transformation tag
        message.print("ModelTransformationTag: ");

        AffineTransform modelTransformation = getModelTransformation();

        if (modelTransformation != null) {
            message.println("[");

            message.print(" [" + modelTransformation.getScaleX());
            message.print("," + modelTransformation.getShearX());
            message.print("," + modelTransformation.getScaleY());
            message.print("," + modelTransformation.getShearY());
            message.print("," + modelTransformation.getTranslateX());
            message.print("," + modelTransformation.getTranslateY() + "]");

            message.println("]");
        } else {
            message.println("NOT AVAILABLE");
        }

        // do all the GeoKeys
        if (metadata != null) {
            int i = 1;
            for (GeoKeyEntry geokey : metadata.getGeoKeys()) {
                message.print("GeoKey #" + i + ": ");
                message.println("Key = " + geokey.getKeyID() + ", Value = " + metadata.getGeoKey(geokey.getKeyID()));
                i++;
            }
        }

        // print out the localized message
        Throwable t = getCause();
        if (t != null) java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", t);

        // close and return
        message.close();
        return text.toString();
    }
}
