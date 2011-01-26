/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.image.io.metadata;

import java.text.Format;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Logger;
import java.util.logging.LogRecord;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.metadata.IIOInvalidTreeException;
import org.w3c.dom.Node;

import org.geotools.util.logging.LoggedFormat;
import org.geotools.util.logging.Logging;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.OptionalDependencies;
import org.geotools.image.io.GeographicImageReader;
import org.geotools.image.io.GeographicImageWriter;


/**
 * Geographic informations encoded in image as metadata. This class provides various methods for
 * reading and writting attribute values in {@link IIOMetadataNode} according the {@linkplain
 * GeographicMetadataFormat geographic metadata format}. If some inconsistency are found while
 * reading (for example if the coordinate system dimension doesn't match the envelope dimension),
 * then the default implementation {@linkplain #warningOccurred logs a warning}. We do not throw
 * an exception because minor errors are not uncommon in geographic data, and we want to process
 * the data on a "<cite>best effort</cite>" basis. However because every warnings are logged
 * through the {@link #warningOccurred} method, subclasses can override this method if they want
 * treat some warnings as fatal errors.
 *
 * @since 2.4
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public class GeographicMetadata extends IIOMetadata {
    /**
     * The {@link ImageReader} or {@link ImageWriter} that holds the metadata,
     * or {@code null} if none.
     */
    private final Object owner;

    /**
     * The root node to be returned by {@link #getAsTree}.
     */
    private Node root;

    /**
     * The coordinate reference system node.
     * Will be created only when first needed.
     */
    private ImageReferencing referencing;

    /**
     * The geometry information.
     * Will be created only when first needed.
     */
    private ImageGeometry geometry;

    /**
     * The list of {@linkplain Band bands}.
     * Will be created only when first needed.
     */
    private ChildList<Band> bands;

    /**
     * The standard date format. Will be created only when first needed.
     */
    private transient LoggedFormat<Date> dateFormat;

    /**
     * Creates a default metadata instance. This constructor defines no standard or native format.
     * The only format defined is the {@linkplain GeographicMetadataFormat geographic} one.
     */
    public GeographicMetadata() {
        this((Object) null);
    }

    /**
     * Creates a default metadata instance for the given reader.
     *
     * @param reader The source image reader, or {@code null} if none.
     */
    public GeographicMetadata(final ImageReader reader) {
        this((Object) reader);
    }

    /**
     * Creates a default metadata instance for the given writer.
     *
     * @param writer The target image writer, or {@code null} if none.
     */
    public GeographicMetadata(final ImageWriter writer) {
        this((Object) writer);
    }

    /**
     * Creates a default metadata instance. This constructor defines no standard or native format.
     * The only format defined is the {@linkplain GeographicMetadataFormat geographic} one.
     */
    private GeographicMetadata(final Object owner) {
        super(false, // Can not return or accept a DOM tree using the standard metadata format.
              null,  // There is no native metadata format.
              null,  // There is no native metadata format.
              new String[] {
                  GeographicMetadataFormat.FORMAT_NAME
              },
              new String[] {
                  "org.geotools.image.io.metadata.GeographicMetadataFormat"
              });
        this.owner = owner;
    }

    /**
     * Constructs a geographic metadata instance with the given format names and format class names.
     * This constructor passes the arguments to the {@linkplain IIOMetadata#IIOMetadata(boolean,
     * String, String, String[], String[]) super-class constructor} unchanged.
     *
     * @param standardMetadataFormatSupported {@code true} if this object can return or accept
     *        a DOM tree using the standard metadata format.
     * @param nativeMetadataFormatName The name of the native metadata, or {@code null} if none.
     * @param nativeMetadataFormatClassName The name of the class of the native metadata format,
     *        or {@code null} if none.
     * @param extraMetadataFormatNames Additional formats supported by this object,
     *        or {@code null} if none.
     * @param extraMetadataFormatClassNames The class names of any additional formats
     *        supported by this object, or {@code null} if none.
     */
    public GeographicMetadata(final boolean  standardMetadataFormatSupported,
                              final String   nativeMetadataFormatName,
                              final String   nativeMetadataFormatClassName,
                              final String[] extraMetadataFormatNames,
                              final String[] extraMetadataFormatClassNames)
    {
        super(standardMetadataFormatSupported,
              nativeMetadataFormatName,
              nativeMetadataFormatClassName,
              extraMetadataFormatNames,
              extraMetadataFormatClassNames);
        owner = null;
    }

    /**
     * Returns {@code false} since this node support some write operations.
     */
    public boolean isReadOnly() {
        return false;
    }

    /**
     * Returns the root of a tree of metadata contained within this object
     * according to the conventions defined by a given metadata format.
     */
    final Node getRootNode() {
        if (root == null) {
            root = new IIOMetadataNode(GeographicMetadataFormat.FORMAT_NAME);
        }
        return root;
    }

    /**
     * Returns the grid referencing.
     */
    public ImageReferencing getReferencing() {
        if (referencing == null) {
            referencing = new ImageReferencing(this);
        }
        return referencing;
    }

    /**
     * Returns the grid geometry.
     */
    public ImageGeometry getGeometry() {
        if (geometry == null) {
            geometry = new ImageGeometry(this);
        }
        return geometry;
    }

    /**
     * Returns the list of all {@linkplain Band bands}.
     */
    final ChildList<Band> getBands() {
        if (bands == null) {
            bands = new ChildList.Bands(this);
        }
        return bands;
    }

    /**
     * Returns the sample type (typically {@value GeographicMetadataFormat#GEOPHYSICS} or
     * {@value GeographicMetadataFormat#PACKED}), or {@code null} if none. This type applies
     * to all {@linkplain Band bands}.
     */
    public String getSampleType() {
        return getBands().getAttributeAsString("type");
    }

    /**
     * Set the sample type for all {@linkplain Band bands}. Valid types include
     * {@value GeographicMetadataFormat#GEOPHYSICS} and {@value GeographicMetadataFormat#PACKED}.
     *
     * @param type The sample type, or {@code null} if none.
     */
    public void setSampleType(final String type) {
        getBands().setAttributeAsEnum("type", type, GeographicMetadataFormat.SAMPLE_TYPES);
    }

    /**
     * Returns the number of {@linkplain Band bands} in the coverage.
     */
    public int getNumBands() {
        return getBands().childCount();
    }

    /**
     * Returns the band at the specified index.
     *
     * @param  bandIndex the band index, ranging from 0 inclusive to {@link #getNumBands} exclusive.
     * @throws IndexOutOfBoundsException if the index is out of bounds.
     */
    public Band getBand(final int bandIndex) throws IndexOutOfBoundsException {
        return getBands().getChild(bandIndex);
    }

    /**
     * Creates a new band and returns it.
     *
     * @param name The name for the new band.
     */
    public Band addBand(final String name) {
        final Band band = getBands().addChild();
        band.setName(name);
        return band;
    }

    /**
     * Checks the format name.
     */
    private void checkFormatName(final String formatName) throws IllegalArgumentException {
        if (!GeographicMetadataFormat.FORMAT_NAME.equals(formatName)) {
            throw new IllegalArgumentException(Errors.getResources(getLocale()).getString(
                    ErrorKeys.ILLEGAL_ARGUMENT_$2, "formatName", formatName));
        }
    }

    /**
     * Returns the root of a tree of metadata contained within this object
     * according to the conventions defined by a given metadata format.
     *
     * @param formatName the desired metadata format.
     * @return The node forming the root of metadata tree.
     * @throws IllegalArgumentException if the format name is {@code null} or is not
     *         one of the names returned by {@link #getMetadataFormatNames}.
     */
    public Node getAsTree(final String formatName) throws IllegalArgumentException {
        checkFormatName(formatName);
        return getRootNode();
    }

    /**
     * Alters the internal state of this metadata from a tree whose syntax is defined by
     * the given metadata format. The default implementation simply replaces all existing
     * state with the contents of the given tree.
     *
     * @param formatName The desired metadata format.
     * @param root An XML DOM Node object forming the root of a tree.
     *
     * @todo We need to performs a real merge; this is required by mosaic image readers.
     *       See {@link MetadataMerge}.
     */
    public void mergeTree(final String formatName, final Node root) throws IIOInvalidTreeException {
        checkFormatName(formatName);
        reset();
        this.root = root;
    }

    /**
     * Alters the internal state of this metadata from a tree defined by the specified metadata.
     * The default implementation expects the {@value GeographicMetadataFormat#FORMAT_NAME} format.
     *
     * @param  metadata The metadata to merge to this object.
     * @throws IIOInvalidTreeException If the metadata can not be merged.
     */
    public void mergeTree(final IIOMetadata metadata) throws IIOInvalidTreeException {
        final Node tree;
        try {
            tree = metadata.getAsTree(GeographicMetadataFormat.FORMAT_NAME);
        } catch (IllegalArgumentException exception) {
            throw new IIOInvalidTreeException(Errors.format(
                    ErrorKeys.GEOTOOLS_EXTENSION_REQUIRED_$1, "mergeTree"), exception, null);
        }
        mergeTree(GeographicMetadataFormat.FORMAT_NAME, tree);
    }

    /**
     * Resets all the data stored in this object to default values.
     */
    public void reset() {
        root        = null;
        referencing = null;
        geometry    = null;
        bands       = null;
    }

    /**
     * Returns the language to use when {@linkplain #warningOccurred logging a warning},
     * or {@code null} if none has been set. The default implementation delegates to
     * {@link ImageReader#getLocale} or {@link ImageWriter#getLocale} if possible, or
     * returns {@code null} otherwise.
     */
    public Locale getLocale() {
        if (owner instanceof ImageReader) {
            return ((ImageReader) owner).getLocale();
        }
        if (owner instanceof ImageWriter) {
            return ((ImageWriter) owner).getLocale();
        }
        return null;
    }

    /**
     * Invoked when a warning occured. This method is invoked when some inconsistency has
     * been detected in the geographic metadata. The default implementation delegates to
     * {@link GeographicImageReader#warningOccurred} if possible, or send the record to
     * the {@code "org.geotools.image.io.metadata"} logger otherwise.
     * <p>
     * Subclasses may override this method if more processing is wanted, or for
     * throwing exception if some warnings should be considered as fatal errors.
     */
    protected void warningOccurred(final LogRecord record) {
        if (owner instanceof GeographicImageReader) {
            ((GeographicImageReader) owner).warningOccurred(record);
        } else if (owner instanceof GeographicImageWriter) {
            ((GeographicImageWriter) owner).warningOccurred(record);
        } else {
            final Logger logger = Logging.getLogger(GeographicMetadata.class);
            record.setLoggerName(logger.getName());
            logger.log(record);
        }
    }

    /**
     * A {@link LoggedFormat} which use the {@link GeographicMetadata#getLocale reader locale}
     * for warnings.
     */
    private final class FormatAdapter<T> extends LoggedFormat<T> {
        private static final long serialVersionUID = -1108933164506428318L;

        FormatAdapter(final Format format, final Class<T> type) {
            super(format, type);
        }

        @Override
        protected Locale getWarningLocale() {
            return getLocale();
        }

        @Override
        protected void logWarning(final LogRecord warning) {
            warningOccurred(warning);
        }
    }

    /**
     * Wraps the specified format in order to either parse fully a string, or log a warning.
     *
     * @param format The format to use for parsing and formatting.
     * @param type   The expected type of parsed values.
     */
    protected <T> LoggedFormat<T> createLoggedFormat(final Format format, final Class<T> type) {
        return new FormatAdapter<T>(format, type);
    }

    /**
     * Returns a standard date format to be shared by {@link MetadataAccessor}.
     */
    final LoggedFormat<Date> dateFormat() {
        if (dateFormat == null) {
            final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            dateFormat = createLoggedFormat(format, Date.class);
            dateFormat.setLogger("org.geotools.image.io.metadata");
            dateFormat.setCaller(MetadataAccessor.class, "getDate");
        }
        return dateFormat;
    }

    /**
     * Returns a string representation of this metadata, mostly for debugging purpose.
     */
    @Override
    public String toString() {
        return OptionalDependencies.toString(
                OptionalDependencies.xmlToSwing(getAsTree(GeographicMetadataFormat.FORMAT_NAME)));
    }
}
