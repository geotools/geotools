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

import java.util.Map;
import java.util.WeakHashMap;
import javax.imageio.ImageReader;
import javax.imageio.ImageReadParam;
import javax.imageio.IIOParamController;


/**
 * The parameters for {@link MosaicImageReader}. <strong>Users are strongly encouraged to invoke
 * the following:</strong>
 *
 * <blockquote><pre>
 * parameters.{@linkplain #setSubsamplingChangeAllowed setSubsamplingChangeAllowed}(true);
 * </pre></blockquote>
 *
 * <b>Explanation:</b> the {@linkplain TileManager tile manager} will select the {@linkplain Tile
 * tile} overviews depending on the {@linkplain #setSourceSubsampling source subsampling defined
 * in this parameter object}. Suppose that an image size is 1000&times;1000 pixels and this image
 * has an overview of 500&times;500 pixels. If the image (always 1000&times;1000 pixels from user's
 * point of view) is requested with a subsampling of (6,6) along (<var>x</var>,<var>y</var>) axis,
 * then the {@linkplain MosaicImageReader mosaic image reader} will detect that it can read the
 * 500&times;500 pixels overview with a subsampling of (3,3) instead. But if the requested
 * subsampling was (7,7), then the reader can not use the overview because it would require a
 * subsampling of (3.5, 3.5) and fractional subsamplings are not allowed. It will read the full
 * 1000&times;1000 pixels image instead, thus leading to lower performance even if we would have
 * expected the opposite from a higher subsampling value.
 * <p>
 * To avoid this problem, {@link MosaicImageReader} can adjust automatically the subsampling
 * parameter to the highest subsampling that overviews can handle, not greater than the specified
 * subsampling. But this adjustment is <strong>not</strong> allowed by default because it would
 * violate the usual {@link javax.imageio.ImageReader} contract. It is allowed only if the
 * {@link #setSubsamplingChangeAllowed} method has been explicitly invoked with value {@code true}.
 * <p>
 * If subsampling changes are allowed, then the values defined in this {@code MosaicImageReadParam}
 * will be modified during the {@linkplain MosaicImageReader#read(int, ImageReadParam) read process}
 * and can be queried once the reading is finished.
 *
 * @since 2.5
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public class MosaicImageReadParam extends ImageReadParam {
    /**
     * The image type policy, or {@code null} for the default one.
     *
     * @see MosaicImageReader#getDefaultImageTypePolicy
     */
    private ImageTypePolicy imageTypePolicy;

    /**
     * If {@code true}, the {@linkplain MoisaicImageReader mosaic image reader} will be allowed
     * to change the {@linkplain #setSourceSubsampling specified subsampling} to some lower but
     * more efficient subsampling.
     *
     * @see #isSubsamplingChangeAllowed
     */
    private boolean subsamplingChangeAllowed;

    /**
     * If {@code true}, then the {@link MosaicImageReader#read read} method is allowed
     * to return {@code null} if no tile intersect the source region to be read.
     */
    private boolean nullForEmptyImage;

    /**
     * The tile readers obtained from the {@link MosaicImageReader} given at construction time,
     * or an empty map if none. Values are the parameters to be given to those readers, created
     * only when first needed.
     *
     * @see MosaicImageReader#readerInputs
     */
    final Map<ImageReader,ImageReadParam> readers;

    /**
     * The value to be returned by {@link #hasController}, or {@code null} if not yet computed.
     */
    private Boolean hasController;

    /**
     * Constructs default parameters for any mosaic image reader. Parameters created by this
     * constructor will not {@linkplain #hasController have controller}. Consider invoking
     * {@link MosaicImageReader#getDefaultReadParam} for parameters better suited to a
     * particular reader instance.
     */
    public MosaicImageReadParam() {
        this(null);
        hasController = Boolean.FALSE;
    }

    /**
     * Constructs default parameters for the given image reader. This constructor is provided
     * for subclassing only. Uses {@link MosaicImageReader#getDefaultReadParam} instead.
     *
     * @param reader The image reader, or {@code null} if none.
     */
    protected MosaicImageReadParam(final MosaicImageReader reader) {
        readers = new WeakHashMap<ImageReader,ImageReadParam>();
        if (reader != null) {
            for (final ImageReader tileReader : reader.getTileReaders()) {
                readers.put(tileReader, null);
            }
        }
        controller = defaultController = MosaicController.DEFAULT;
    }

    /**
     * Returns {@code true} if the {@linkplain MoisaicImageReader mosaic image reader} is allowed
     * to change the {@linkplain #setSourceSubsampling subsampling} to some more efficient value.
     * The default value is {@code false}, which means that the reader will use exactly the given
     * subsampling and may leads to very slow reading. See {@linkplain MosaicImageReadParam class
     * javadoc}.
     *
     * @return {@code true} if the mosaic image reader is allowed to change the subsampling.
     */
    public boolean isSubsamplingChangeAllowed() {
        return subsamplingChangeAllowed;
    }

    /**
     * Sets whatever the {@linkplain MoisaicImageReader mosaic image reader} will be allowed to
     * change the {@linkplain #setSourceSubsampling subsampling} to some more efficient value.
     * <strong>Users are strongly encouraged to set this value to {@code true}</strong>, which
     * is not the default because doing so would violate the {@link javax.imageio.ImageReader}
     * contract. See {@linkplain MosaicImageReadParam class javadoc} for more details.
     *
     * @param allowed {@code true} if the mosaic image reader is allowed to change the subsampling.
     */
    public void setSubsamplingChangeAllowed(final boolean allowed) {
        subsamplingChangeAllowed = allowed;
    }

    /**
     * If {@code true}, then the {@link MosaicImageReader#read read} method is allowed to
     * return {@code null} if no tile intersect the source region to be read. The default
     * value is {@code false}.
     * <p>
     * Note that a non-null image is not necessarily non-empty. A non-null image intersects
     * at least one tile, but that tile could be empty in the intersection area.
     *
     * @return {@code true} if the {@code read} method is allowed to return {@code null}.
     */
    public boolean getNullForEmptyImage() {
        return nullForEmptyImage;
    }

    /**
     * Sets whatever the {@link MosaicImageReader#read read} method is allowed to return
     * {@code null} when no tile intersect the source region to be read. Setting this flag
     * to {@code true} speedup the read process in region that doesn't intersect any tile,
     * at the cost of requerying the caller to handle null return value.
     *
     * @param allowed {@code true} if the {@code read} method is allowed to return {@code null}.
     */
    public void setNullForEmptyImage(final boolean allowed) {
        nullForEmptyImage = allowed;
    }

    /**
     * Returns the policy for {@link MosaicImageReader#getImageTypes computing image types}.
     * If no policy has been specified, then this method returns {@code null}. In the later
     * case, the reader will use its {@linkplain MosaicImageReader#getDefaultImageTypePolicy
     * default policy}.
     *
     * @return The policy for computing image types.
     */
    public ImageTypePolicy getImageTypePolicy() {
        return imageTypePolicy;
    }

    /**
     * Sets the policy for {@link MosaicImageReader#getImageTypes computing image types}.
     *
     * @param policy
     *          The new policy, or {@code null} for reader
     *          {@linkplain MosaicImageReader#getDefaultImageTypePolicy default policy}.
     */
    public void setImageTypePolicy(final ImageTypePolicy policy) {
        imageTypePolicy = policy;
    }

    /**
     * Returns the parameters for the given image reader, looking in the cache first. This is
     * needed because {@link MosaicController} may have configured those parameters.
     */
    final ImageReadParam getCachedTileParameters(final ImageReader reader) {
        ImageReadParam parameters = readers.get(reader);
        if (parameters == null) {
            parameters = getTileParameters(reader);
            readers.put(reader, parameters);
        }
        return parameters;
    }

    /**
     * Returns the parameters to use for reading an image from the given image reader.
     * This method is invoked automatically by {@link MosaicImageReader}. The default
     * implementation invokes {@link ImageReader#getDefaultReadParam()} and copies the
     * {@linkplain #setSourceBands source bands},
     * {@linkplain #setDestinationBands destination bands} and
     * {@linkplain #setSourceProgressivePasses progressive passes} settings.
     * Other settings like {@linkplain #setSourceRegion source region} don't need to be
     * copied since they will be computed by the mosaic image reader.
     * <p>
     * Subclasses can override this method if they want to configure the parameters for
     * tile readers in an other way. Note however that {@link MosaicController} provides
     * an other way to configure parameters.
     *
     * @param reader The tile reader.
     * @return The parameter for reading a tile using the given reader.
     */
    protected ImageReadParam getTileParameters(final ImageReader reader) {
        final ImageReadParam parameters = reader.getDefaultReadParam();
        parameters.setSourceBands(sourceBands);
        parameters.setDestinationBands(destinationBands);
        parameters.setSourceProgressivePasses(minProgressivePass, numProgressivePasses);
        return parameters;
    }

    /**
     * Returns {@code true} if there is a controller installed. The implementation in this
     * class is more conservative than the default implementation in that if the current
     * {@linkplain #controller controller} is the {@linkplain #defaultController default
     * controller} instance, then this method returns {@code true} only if the controller
     * for at least one {@linkplain #getTileParameters tile parameters} returned {@code true}.
     */
    @Override
    public boolean hasController() {
        if (controller != defaultController) {
            return super.hasController();
        }
        if (hasController == null) {
            hasController = Boolean.FALSE;
            for (final Map.Entry<ImageReader,ImageReadParam> entry : readers.entrySet()) {
                ImageReadParam parameters = entry.getValue();
                if (parameters == null) {
                    parameters = getTileParameters(entry.getKey());
                    entry.setValue(parameters);
                }
                if (parameters.hasController()) {
                    hasController = Boolean.TRUE;
                    break;
                }
            }
        }
        return hasController;
    }

    /**
     * Returns the default controller. This is typically an instance of
     * {@link MosaicController}, but this is not mandatory.
     */
    @Override
    public IIOParamController getDefaultController() {
        // Overriden only for documentation purpose.
        return super.getDefaultController();
    }
}
