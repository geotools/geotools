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
@SuppressWarnings("PMD.ReplaceVectorWithList")
public class DisposeStopper implements RenderedImage {

    private RenderedImage adaptee;

    public DisposeStopper(RenderedImage adaptee) {
        Utilities.ensureNonNull("adaptee", adaptee);
        this.adaptee = adaptee;
    }

    @Override
    public Vector<RenderedImage> getSources() {
        return adaptee.getSources();
    }

    @Override
    public Object getProperty(String name) {
        return adaptee.getProperty(name);
    }

    @Override
    public String[] getPropertyNames() {
        return adaptee.getPropertyNames();
    }

    @Override
    public ColorModel getColorModel() {
        return adaptee.getColorModel();
    }

    @Override
    public SampleModel getSampleModel() {
        return adaptee.getSampleModel();
    }

    @Override
    public int getWidth() {
        return adaptee.getWidth();
    }

    @Override
    public int getHeight() {
        return adaptee.getHeight();
    }

    @Override
    public int getMinX() {
        return adaptee.getMinX();
    }

    @Override
    public int getMinY() {
        return adaptee.getMinY();
    }

    @Override
    public int getNumXTiles() {
        return adaptee.getNumXTiles();
    }

    @Override
    public int getNumYTiles() {
        return adaptee.getNumYTiles();
    }

    @Override
    public int getMinTileX() {
        return adaptee.getMinTileX();
    }

    @Override
    public int getMinTileY() {
        return adaptee.getMinTileY();
    }

    @Override
    public int getTileWidth() {
        return adaptee.getTileWidth();
    }

    @Override
    public int getTileHeight() {
        return adaptee.getTileHeight();
    }

    @Override
    public int getTileGridXOffset() {
        return adaptee.getTileGridXOffset();
    }

    @Override
    public int getTileGridYOffset() {
        return adaptee.getTileGridYOffset();
    }

    @Override
    public Raster getTile(int tileX, int tileY) {
        return adaptee.getTile(tileX, tileY);
    }

    @Override
    public Raster getData() {
        return adaptee.getData();
    }

    @Override
    public Raster getData(Rectangle rect) {
        return adaptee.getData(rect);
    }

    @Override
    public WritableRaster copyData(WritableRaster raster) {
        return adaptee.copyData(raster);
    }
}
