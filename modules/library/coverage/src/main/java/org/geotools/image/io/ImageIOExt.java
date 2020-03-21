/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2016, Open Source Geospatial Foundation (OSGeo)
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

import com.sun.media.imageioimpl.common.PackageUtil;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageInputStreamSpi;
import javax.imageio.spi.ImageReaderWriterSpi;
import javax.imageio.stream.FileCacheImageOutputStream;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import javax.media.jai.PlanarImage;
import org.geotools.image.ImageWorker;
import org.geotools.util.Classes;
import org.geotools.util.Utilities;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Provides an alternative source of image input and output streams that uses optimized behavior.
 *
 * <p>Currently implemented optimizations:
 *
 * <ul>
 *   <li>wrap an OutputStream into a {@link MemoryCacheImageOutputStream} or a {@link
 *       FileCacheImageOutputStream} based on a image size threshold
 * </ul>
 *
 * @author Andrea Aime - GeoSolutions
 * @since 2.7.2
 */
public class ImageIOExt {

    static Long filesystemThreshold = null;

    static File cacheDirectory = null;

    /**
     * Builds a {@link ImageOutputStream} writing to <code>destination</code>, based on logic that
     * involves the image size
     *
     * @param image the image to be written on the destination (can be null)
     * @param destination the destination
     */
    public static ImageOutputStream createImageOutputStream(RenderedImage image, Object destination)
            throws IOException {
        // already what we need?
        if (destination instanceof ImageOutputStream) {
            return (ImageOutputStream) destination;
        }

        // generate the ImageOutputStream
        if (destination instanceof OutputStream && filesystemThreshold != null && image != null) {
            OutputStream stream = (OutputStream) destination;

            // if going to wrap a output stream and we have a threshold set
            long imageSize = computeImageSize(image);
            if (imageSize > filesystemThreshold) {
                File cacheDirectory = getCacheDirectory();
                return new FileCacheImageOutputStream(stream, cacheDirectory);
            } else {
                return new MemoryCacheImageOutputStream(stream);
            }
        } else {
            return ImageIO.createImageOutputStream(destination);
        }
    }

    /**
     * Returns a {@link ImageOutputStream} suitable for writing on the specified <code>input</code>
     */
    public static ImageInputStream createImageInputStream(Object input) throws IOException {
        return ImageIO.createImageInputStream(input);
    }

    /**
     * Returns the cache directory used by ImageIOExt, either the manually configured one, or the
     * result of calling {@link ImageIO#getCacheDirectory()}
     */
    public static File getCacheDirectory() {
        File cacheDir = cacheDirectory;
        if (cacheDir == null) {
            cacheDir = ImageIO.getCacheDirectory();
        }
        return cacheDir;
    }

    /**
     * Sets the directory where cache files are to be created. If set to null (the default value)
     * {@link ImageIO#getCacheDirectory()} will be used as the value
     *
     * @param cache a <code>File</code> specifying a directory.
     */
    public static void setCacheDirectory(File cache) {
        ImageIOExt.cacheDirectory = cache;
    }

    /**
     * The threshold at which the class will flip from {@link MemoryCacheImageOutputStream} to
     * {@link FileCacheImageOutputStream}. If the in memory, uncompressed image size is lower than
     * the threshold a {@link MemoryCacheImageOutputStream} will be returned, otherwise a {@link
     * FileCacheImageOutputStream} will be used instead
     */
    public static Long getFilesystemThreshold() {
        return filesystemThreshold;
    }

    /**
     * Sets the memory/file usage threshold (or null to have the code fall back on ImageIO behavior)
     *
     * @see #getFilesystemThreshold()
     */
    public static void setFilesystemThreshold(Long filesystemThreshold) {
        ImageIOExt.filesystemThreshold = filesystemThreshold;
    }

    /**
     * Allows or disallows native acceleration for the specified image format. By default, the image
     * I/O extension for JAI provides native acceleration for PNG and JPEG. Unfortunatly, those
     * native codec has bug in their 1.0 version. Invoking this method will force the use of
     * standard codec provided in J2SE 1.4.
     *
     * <p><strong>Implementation note:</strong> the current implementation assume that JAI codec
     * class name start with "CLib". It work for Sun's 1.0 implementation, but may change in future
     * versions. If this method doesn't recognize the class name, it does nothing.
     *
     * @param format The format name (e.g. "png").
     * @param category {@code ImageReaderSpi.class} to set the reader, or {@code
     *     ImageWriterSpi.class} to set the writer.
     * @param allowed {@code false} to disallow native acceleration.
     */
    public static synchronized <T extends ImageReaderWriterSpi> void allowNativeCodec(
            final String format, final Class<T> category, final boolean allowed) {
        T standard = null;
        T codeclib = null;
        final IIORegistry registry = IIORegistry.getDefaultInstance();
        for (final Iterator<T> it = registry.getServiceProviders(category, false); it.hasNext(); ) {
            final T provider = it.next();
            final String[] formats = provider.getFormatNames();
            for (int i = 0; i < formats.length; i++) {
                if (formats[i].equalsIgnoreCase(format)) {
                    if (Classes.getShortClassName(provider).startsWith("CLib")) {
                        codeclib = provider;
                    } else {
                        standard = provider;
                    }
                    break;
                }
            }
        }
        if (standard != null && codeclib != null) {
            if (allowed) {
                registry.setOrdering(category, codeclib, standard);
            } else {
                registry.setOrdering(category, standard, codeclib);
            }
        }
    }

    /**
     * Get a proper {@link ImageInputStreamSpi} instance for the provided {@link Object} input
     * without trying to create an {@link ImageInputStream}.
     *
     * @see #getImageInputStreamSPI(Object, boolean)
     */
    public static final ImageInputStreamSpi getImageInputStreamSPI(final Object input) {
        return getImageInputStreamSPI(input, true);
    }

    /**
     * Get a proper {@link ImageInputStreamSpi} instance for the provided {@link Object} input.
     *
     * @param input the input object for which we need to find a proper {@link ImageInputStreamSpi}
     *     instance
     * @param streamCreationCheck if <code>true</code>, when a proper {@link ImageInputStreamSpi}
     *     have been found for the provided input, use it to try creating an {@link
     *     ImageInputStream} on top of the input.
     * @return an {@link ImageInputStreamSpi} instance.
     */
    public static final ImageInputStreamSpi getImageInputStreamSPI(
            final Object input, final boolean streamCreationCheck) {

        Iterator<ImageInputStreamSpi> iter;
        // Ensure category is present
        try {
            iter =
                    IIORegistry.getDefaultInstance()
                            .getServiceProviders(ImageInputStreamSpi.class, true);
        } catch (IllegalArgumentException e) {
            return null;
        }

        boolean usecache = ImageIO.getUseCache();

        ImageInputStreamSpi spi = null;
        while (iter.hasNext()) {
            spi = iter.next();
            if (spi.getInputClass().isInstance(input)) {

                // Stream creation check
                if (streamCreationCheck) {
                    ImageInputStream stream = null;
                    try {
                        stream =
                                spi.createInputStreamInstance(
                                        input, usecache, ImageIO.getCacheDirectory());
                        break;
                    } catch (IOException e) {
                        return null;
                    } finally {
                        // Make sure to close the created stream
                        if (stream != null) {
                            try {
                                stream.close();
                            } catch (Throwable t) {
                                // eat exception
                            }
                        }
                    }
                } else {
                    break;
                }
            }
        }

        return spi;
    }

    /**
     * Tells me whether or not the native libraries for JAI/ImageIO are active or not.
     *
     * @return <code>false</code> in case the JAI/ImageIO native libs are not in the path, <code>
     *     true</code> otherwise.
     */
    public static boolean isCLibAvailable() {
        return PackageUtil.isCodecLibAvailable();
    }

    /**
     * Look for an {@link ImageReader} instance that is able to read the provided {@link
     * ImageInputStream}, which must be non null.
     *
     * <p>In case no reader is found, <code>null</code> is returned.
     *
     * @param inStream an instance of {@link ImageInputStream} for which we need to find a suitable
     *     {@link ImageReader}.
     * @return a suitable instance of {@link ImageReader} or <code>null</code> if one cannot be
     *     found.
     */
    public static ImageReader getImageioReader(final ImageInputStream inStream) {
        Utilities.ensureNonNull("inStream", inStream);
        // get a reader
        inStream.mark();
        final Iterator<ImageReader> readersIt = ImageIO.getImageReaders(inStream);
        if (!readersIt.hasNext()) {
            return null;
        }
        return readersIt.next();
    }

    /** Computes the image size based on the SampleModel and image dimension */
    static long computeImageSize(RenderedImage image) {
        long bits = 0;
        final int bands = image.getSampleModel().getNumBands();
        for (int i = 0; i < bands; i++) {
            bits += image.getSampleModel().getSampleSize(i);
        }
        return (long) Math.ceil(bits / 8d) * image.getWidth() * image.getHeight();
    }

    /**
     * Reads an image from the given input, working around some JDK reader issues. At the time of
     * writing, this applies a work around for PNGs with RGB (no alpha) and a transparent color
     * configured in the header, that the JDK reader cannot handle.
     *
     * @param input A non null image source, like a {@link File}, {@link java.net.URL}, or {@link
     *     java.io.InputStream}
     * @return A image
     */
    public static RenderedImage read(Object input) throws IOException {
        if (input == null) {
            throw new IllegalArgumentException("input == null!");
        }

        // build an image input stream
        try (ImageInputStream stream = getImageInputStream(input)) {
            // get the readers
            Iterator iter = ImageIO.getImageReaders(stream);
            if (!iter.hasNext()) {
                return null;
            }

            ImageReader reader = (ImageReader) iter.next();
            // work around PNG with transparent RGB color if needed
            // we can remove it once we run on JDK 11, see
            // https://bugs.openjdk.java.net/browse/JDK-6788458
            boolean isJdkPNGReader =
                    "com.sun.imageio.plugins.png.PNGImageReader"
                            .equals(reader.getClass().getName());
            // if it's the JDK PNG reader, we cannot skip the metadata, the tRNS section will be in
            // there
            reader.setInput(stream, true, !isJdkPNGReader);

            BufferedImage bi;
            try {
                ImageReadParam param = reader.getDefaultReadParam();
                bi = reader.read(0, param);
            } finally {
                reader.dispose();
            }

            // apply transparency in post-processing if needs be
            if (isJdkPNGReader
                    && bi.getColorModel() instanceof ComponentColorModel
                    && !bi.getColorModel().hasAlpha()
                    && bi.getColorModel().getNumComponents() == 3) {
                IIOMetadata imageMetadata = reader.getImageMetadata(0);
                Node tree = imageMetadata.getAsTree(imageMetadata.getNativeMetadataFormatName());
                Node trns_rgb = getNodeFromPath(tree, Arrays.asList("tRNS", "tRNS_RGB"));
                if (trns_rgb != null) {
                    NamedNodeMap attributes = trns_rgb.getAttributes();
                    Integer red = getIntegerAttribute(attributes, "red");
                    Integer green = getIntegerAttribute(attributes, "green");
                    Integer blue = getIntegerAttribute(attributes, "blue");

                    if (red != null && green != null && blue != null) {
                        Color color = new Color(red, green, blue);
                        ImageWorker iw = new ImageWorker(bi);
                        iw.makeColorTransparent(color);
                        return iw.getRenderedImage();
                    }
                }
            }
            return bi;
        }
    }

    private static ImageInputStream getImageInputStream(Object input) throws IOException {
        if (input instanceof ImageInputStream) {
            return (ImageInputStream) input;
        }
        ImageInputStream stream = ImageIO.createImageInputStream(input);
        if (stream == null) {
            throw new IOException("Can't create an ImageInputStream!");
        }
        return stream;
    }

    /**
     * Same as {@link #read(Object)} but ensures the result is a {@link BufferedImage}, eventually
     * transforming it if needs be. Callers that can deal with {@link RenderedImage} should use the
     * other method for efficiency sake.
     *
     * @return A image
     */
    public static BufferedImage readBufferedImage(Object input) throws IOException {
        RenderedImage ri = ImageIOExt.read(input);
        if (ri == null) {
            return null;
        } else if (ri instanceof BufferedImage) {
            return (BufferedImage) ri;
        } else {
            return PlanarImage.wrapRenderedImage(ri).getAsBufferedImage();
        }
    }

    private static Integer getIntegerAttribute(NamedNodeMap attributes, String attributeName) {
        return Optional.ofNullable(attributes.getNamedItem(attributeName))
                .map(n -> n.getNodeValue())
                .map(s -> Integer.valueOf(s))
                .orElse(null);
    }

    /** Locates a node in the tree, by giving a list of path components */
    private static Node getNodeFromPath(Node root, List<String> pathComponents) {
        if (pathComponents.isEmpty()) {
            return root;
        }

        String firstComponent = pathComponents.get(0);
        NodeList childNodes = root.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node child = childNodes.item(i);

            if (firstComponent.equals(child.getNodeName())) {
                return getNodeFromPath(child, pathComponents.subList(1, pathComponents.size()));
            }
        }

        // not found
        return null;
    }
}
