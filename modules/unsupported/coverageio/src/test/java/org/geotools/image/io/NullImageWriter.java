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
package org.geotools.image.io;

import java.io.IOException;
import java.util.Locale;
import javax.imageio.IIOImage;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.metadata.IIOMetadata;
import org.geotools.factory.GeoTools;
import org.geotools.resources.i18n.Vocabulary;
import org.geotools.resources.i18n.VocabularyKeys;


/**
 * An image writer which doesn't write anything.
 * This is sometime useful for testing purpose.
 *
 * @since 2.5
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public class NullImageWriter extends GeographicImageWriter {
    /**
     * Constructs a {@code NullImageWriter}.
     *
     * @param provider The {@code ImageWriterSpi} that
     *        is constructing this object, or {@code null}.
     */
    protected NullImageWriter(final ImageWriterSpi provider) {
        super(provider);
    }

    /**
     * Silently ignore the given parameters.
     *
     * @param metadata Ignored.
     * @throws IOException Never thrown.
     */
    @Override
    public void write(IIOMetadata metadata, IIOImage image, ImageWriteParam param) throws IOException {
        LOGGER.info(Vocabulary.format(VocabularyKeys.SAVING_$1, output));
    }




    /**
     * Service provider interface (SPI) for {@link NullImageWriter}s.
     *
     * @since 2.5
     * @source $URL$
     * @version $Id$
     * @author Martin Desruisseaux
     */
    public static class Spi extends ImageWriterSpi {
        /**
         * Constructs a default {@code NullImageWriter.Spi}. This constructor
         * provides the following defaults:
         *
         * <ul>
         *   <li>{@link #names}           = {@code "null"}</li>
         *   <li>{@link #pluginClassName} = {@code "org.geotools.image.io.NullImageWriter"}</li>
         *   <li>{@link #vendorName}      = {@code "Geotools"}</li>
         * </ul>
         */
        public Spi() {
            names           = new String[] {"null"};
            pluginClassName = "org.geotools.image.io.NullImageWriter";
            vendorName      = "GeoTools";
            version         = GeoTools.getVersion().toString();
        }

        /**
         * Returns a description of the image writer.
         */
        @Override
        public String getDescription(Locale locale) {
            return "Null";
        }

        /**
         * Returns {@code true} in all cases.
         */
        @Override
        public boolean canEncodeImage(ImageTypeSpecifier type) {
            return true;
        }

        /**
         * Returns a new {@link NullImageWriter} instance.
         *
         * @throws IOException If an I/O operation was required and failed.
         */
        @Override
        public ImageWriter createWriterInstance(Object extension) throws IOException {
            return new NullImageWriter(this);
        }
    }
}
