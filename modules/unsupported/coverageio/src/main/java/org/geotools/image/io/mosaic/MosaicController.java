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
import javax.imageio.IIOParam;
import javax.imageio.IIOParamController;
import javax.imageio.ImageReader;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageWriteParam;


/**
 * The {@linkplain IIOParam#defaultController default controller} for mosaic parameters.
 * Controllers are implemented by objects that can determine the settings of {@link IIOParam}
 * objects, either by putting up a GUI to obtain values from a user, or by other means. Whether
 * the controller puts up a GUI or merely computes a set of values is irrelevant to this class.
 * <p>
 * Two kinds of {@link IIOParam} can be setup by {@code MosaicController}:
 * <p>
 * <ul>
 *   <li>The parameters for the mosaic as a whole (typically as an instance of
 *       {@link MosaicImageReadParam} or {@link MosaicImageWriteParam}) are setup
 *       by {@link #activate}.</li>
 *   <li>The parameters for the tiles (typically as {@link ImageReadParam} or
 *       {@link ImageWriteParam} of arbitrary class) are setup by {@link #activateForTile}.</li>
 * </ul>
 * <p>
 * The {@code activate} methods are executed only when explicitly invoked, typically through a
 * call to {@link IIOParam#activateController}. They are <strong>not</strong> invoked automatically
 * by {@linkplain MosaicImageReader mosaic image reader} or {@linkplain MosaicImageWriter writer}.
 * This is different than {@link #configure}, which is always invoked automatically by the
 * above.
 * <p>
 * Users can create a subclass of {@code MosaicController} and pass it to
 * {@link IIOParam#setController} in order to control the configuration of a mosaic to be read or
 * written.
 *
 * @since 2.5
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public class MosaicController implements IIOParamController {
    /**
     * A shared default instance. Not public because users should get the
     * instance through {@link MosaicImageReadParam#getDefaultController}.
     */
    static final MosaicController DEFAULT = new MosaicController();

    /**
     * Creates a default mosaic controller. This constructor is for subclassing only. For other
     * uses, an instance can be obtained with {@link MosaicImageReadParam#getDefaultController}.
     */
    protected MosaicController() {
    }

    /**
     * Activates the mosaic controller. This method is not invoked automatically,
     * but only when {@link IIOParam#activateController()} is invoked explicitly.
     * <p>
     * When reading, the default implementation {@linkplain MosaicImageReadParam#getTileParameters
     * creates parameters for tile readers} if not already done, then delegates the activation to
     * their controllers. It returns {@code true} only if activation succeed for every tile readers.
     * <p>
     * When writting, the default implementation {@linkplain MosaicImageWriteParam#getTileParameters
     * creates parameters for tile writers} if not already done, then delegates the activation to
     * their controllers. It returns {@code true} only if activation succeed for every tile writers.
     *
     * @param parameters
     *          The parameters to be modified. It should be an instance of
     *          {@link MosaicImageReadParam} or {@link MosaicImageWriteParam},
     *          but this is not mandatory.
     * @return {@code true} if the parameters are ready for use, or
     *         {@code false} if the user canceled the operation.
     *
     * @todo Handle the {@link MosaicImageWriteParam} case.
     */
    public boolean activate(final IIOParam parameters) {
        if (parameters instanceof MosaicImageReadParam) {
            final MosaicImageReadParam params = (MosaicImageReadParam) parameters;
            for (final Map.Entry<ImageReader,ImageReadParam> entry : params.readers.entrySet()) {
                ImageReadParam p = entry.getValue();
                if (p == null) {
                    p = params.getTileParameters(entry.getKey());
                    entry.setValue(p);
                }
                if (!activateForTile(p)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Invoked by {@link #activate activate} for every
     * {@linkplain MosaicImageReadParam#getTileParameters tile read parameters} or
     * {@linkplain MosaicImageWriteParam#getTileParameters tile write parameters}.
     * Note that this method is not necessarly invoked for every tiles, but only for every readers
     * or writers to be used with tiles. For example this method is invoked only once if every
     * tiles are to be read with the same {@linkplain ImageReader image reader}.
     * <p>
     * The default implementation delegates to {@link IIOParam#activateController}.
     * Subclasses can override this method is more control is wanted.
     *
     * @param parameters
     *          The parameters to be used for reading tiles. This is <strong>not</strong> the same
     *          than the parameters given to {@link #activate}, and is usually unrelated to mosaic
     *          parameters.
     * @return {@code true} if the parameters are ready for use, or
     *         {@code false} if the user canceled the operation.
     */
    protected boolean activateForTile(final IIOParam parameters) {
        if (parameters.hasController()) {
            return parameters.activateController();
        } else {
            return true;
        }
    }

    /**
     * Automatically invoked by {@link MosaicImageReader} or {@link MosaicImageWriter} when a tile
     * is about to be read or written. The parameters setting are determined by reader or writer
     * before to invoke this method and usually don't need to be changed. This method is provided
     * as a hook allowing subclasses to performs additional setting.
     * <p>
     * The default implementation does nothing.
     *
     * @param tile
     *          The tile which is about to be read or written.
     * @param parameters
     *          The parameter to be given to the image reader or writer.
     */
    protected void configure(Tile tile, IIOParam parameters) {
    }
}
