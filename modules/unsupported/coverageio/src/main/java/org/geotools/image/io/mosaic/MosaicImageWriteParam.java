/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.image.io.mosaic;

import java.io.File;
import javax.imageio.ImageWriteParam;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;


/**
 * The parameters for {@link MosaicImageWriter}.
 *
 * @since 2.5
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public class MosaicImageWriteParam extends ImageWriteParam {
    /**
     * The index of the {@linkplain TileManager tile manager} to use in the array returned by
     * {@link MosaicImageWriter#getOutput}.
     */
    private int outputIndex = 0;

    /**
     * Controls the way {@link MosaicImageWriter} writes the tiles.
     */
    private TileWritingPolicy policy = TileWritingPolicy.OVERWRITE;

    /**
     * Constructs an empty set of parameters.
     */
    public MosaicImageWriteParam() {
    }

    /**
     * Creates a new set of parameters with the same mosaic-specific parameters than the given
     * one, and the default value for all other parameters.
     * <p>
     * This method is not public because for a public API, it would be cleaner to copy all
     * parameters (as we usually expect from a copy constructor), while {@link MosaicImageWriter}
     * really needs to copy only the mosaic-specific parameters and left the other ones to their
     * default value.
     *
     * @param copy The parameters to copy.
     */
    MosaicImageWriteParam(final MosaicImageWriteParam copy) {
        outputIndex = copy.outputIndex;
        policy      = copy.policy;
    }

    /**
     * Returns the index of the image to be written. This is the index of the
     * {@linkplain TileManager tile manager} to use in the array returned by
     * {@link MosaicImageWriter#getOutput}. The default value is 0.
     *
     * @return The index of the image to be written.
     */
    public int getOutputIndex() {
        return outputIndex;
    }

    /**
     * Sets the index of the image to be written. This is the index of the
     * {@linkplain TileManager tile manager} to use in the array returned by
     * {@link MosaicImageWriter#getOutput}. The default value is 0.
     *
     * @param index The index of the image to be written.
     */
    public void setOutputIndex(final int index) {
        if (index < 0 || index > Tile.MASK) {
            throw new IllegalArgumentException(Errors.format(
                    ErrorKeys.VALUE_OUT_OF_BOUNDS_$3, index, 0, Tile.MASK));
        }
        outputIndex = index;
    }

    /**
     * Returns whatever existings {@linkplain File files} should be skipped or overwritten.
     * The default value is {@link TileWritingPolicy#OVERWRITE OVERWRITE}.
     *
     * @return The policy to apply when writting tiles.
     */
    public TileWritingPolicy getTileWritingPolicy() {
        return policy;
    }

    /**
     * Sets whatever existings {@linkplain File files} should be skipped. The default behavior
     * is to {@linkplain TileWritingPolicy#OVERWRITE overwrite} every files inconditionnaly.
     * Settings the policy to {@link TileWritingPolicy#WRITE_NEWS_ONLY WRITE_NEWS_ONLY} may
     * speedup {@link MosaicImageWriter} when the process of writting tiles is started again
     * after a previous partial failure, by skipping the tiles that were successfully generated
     * in the previous run.
     *
     * @param policy The policy to apply when writting tiles.
     */
    public void setTileWritingPolicy(final TileWritingPolicy policy) {
        Tile.ensureNonNull("policy", policy);
        this.policy = policy;
    }
}
