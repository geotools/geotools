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
 *
 */
package org.geotools.image;

import java.awt.Rectangle;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.util.Vector;
import org.geotools.util.Utilities;

/**
 * Stop the dispose mechanism we have in place for renderedImage.
 *
 * @author Simone Giannecchini, GeoSolutions SAS
 */
public class DisposeStopper implements RenderedImage {

    private RenderedImage adaptee;

    public DisposeStopper(RenderedImage adaptee) {
        Utilities.ensureNonNull("adaptee", adaptee);
        this.adaptee = adaptee;
    }

    public Vector<RenderedImage> getSources() {
        return adaptee.getSources();
    }

    public Object getProperty(String name) {
        return adaptee.getProperty(name);
    }

    public String[] getPropertyNames() {
        return adaptee.getPropertyNames();
    }

    public ColorModel getColorModel() {
        return adaptee.getColorModel();
    }

    public SampleModel getSampleModel() {
        return adaptee.getSampleModel();
    }

    public int getWidth() {
        return adaptee.getWidth();
    }

    public int getHeight() {
        return adaptee.getHeight();
    }

    public int getMinX() {
        return adaptee.getMinX();
    }

    public int getMinY() {
        return adaptee.getMinY();
    }

    public int getNumXTiles() {
        return adaptee.getNumXTiles();
    }

    public int getNumYTiles() {
        return adaptee.getNumYTiles();
    }

    public int getMinTileX() {
        return adaptee.getMinTileX();
    }

    public int getMinTileY() {
        return adaptee.getMinTileY();
    }

    public int getTileWidth() {
        return adaptee.getTileWidth();
    }

    public int getTileHeight() {
        return adaptee.getTileHeight();
    }

    public int getTileGridXOffset() {
        return adaptee.getTileGridXOffset();
    }

    public int getTileGridYOffset() {
        return adaptee.getTileGridYOffset();
    }

    public Raster getTile(int tileX, int tileY) {
        return adaptee.getTile(tileX, tileY);
    }

    public Raster getData() {
        return adaptee.getData();
    }

    public Raster getData(Rectangle rect) {
        return adaptee.getData(rect);
    }

    public WritableRaster copyData(WritableRaster raster) {
        return adaptee.copyData(raster);
    }
}
