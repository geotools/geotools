/**
 * 
 */
package org.geotools.gce.geotiff;

import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageMetadata;
import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReaderSpi;
import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageWriterSpi;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.IIOException;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;

import org.apache.commons.io.FilenameUtils;
import org.geotools.coverage.grid.io.imageio.geotiff.GeoTiffConstants;
import org.geotools.coverage.grid.io.imageio.geotiff.GeoTiffIIOMetadataEncoder;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.data.PrjFileReader;
import org.geotools.data.WorldFileReader;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Parent;
import org.jdom.input.DOMBuilder;
import org.jdom.output.DOMOutputter;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

/**
 * Sparse utilities for the various classes. I use them to extract complex code
 * from other places.
 * 
 * @author Simone Giannecchini, GeoSolutions S.A.S.
 * 
 */
class Utils {

    /** Logger for the {@link Utils} class. */
    private final static Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger(Utils.class.toString());
        
    static CoordinateReferenceSystem WGS84 = DefaultGeographicCRS.WGS84;

    static URL checkSource(Object source) throws MalformedURLException, DataSourceException {
        URL sourceURL = null;

        //
        // Check source
        //

        // if it is a URL or a String let's try to see if we can get a file to
        // check if we have to build the index
        if (source instanceof URL) {
            sourceURL = ((URL) source);
            source = DataUtilities.urlToFile(sourceURL);
        } else if (source instanceof String) {
            // is it a File?
            final String tempSource = (String) source;
            File tempFile = new File(tempSource);
            if (!tempFile.exists()) {
                // is it a URL
                try {
                    sourceURL = new URL(tempSource);
                    source = DataUtilities.urlToFile(sourceURL);
                } catch (MalformedURLException e) {
                    sourceURL = null;
                    source = null;
                }
            } else {
                sourceURL = tempFile.toURI().toURL();
                source = tempFile;
            }
        }

        // at this point we have tried to convert the thing to a File as hard as
        // we could, let's see what we can do
        if (source instanceof File) {
            final File sourceFile = (File) source;
            if (!sourceFile.isDirectory()) {
                sourceURL = ((File) source).toURI().toURL();
            }
        } else {
            sourceURL = null;
        }
        return sourceURL;
    }

    /**
     * Checks that a {@link File} is a real file, exists and is readable.
     * 
     * @param file
     *            the {@link File} instance to check. Must not be null.
     * 
     * @return <code>true</code> in case the file is a real file, exists and is
     *         readable; <code>false </code> otherwise.
     */
    static boolean checkFileReadable(final File file) {
        if (LOGGER.isLoggable(Level.FINE)) {
            final StringBuilder builder = new StringBuilder();
            builder.append("Checking file:")
                    .append(FilenameUtils.getFullPath(file.getAbsolutePath())).append("\n")
                    .append("canRead:").append(file.canRead()).append("\n").append("isHidden:")
                    .append(file.isHidden()).append("\n").append("isFile").append(file.isFile())
                    .append("\n").append("canWrite").append(file.canWrite()).append("\n");
            LOGGER.fine(builder.toString());
        }
        if (!file.exists() || !file.canRead() || !file.isFile()) {
            return false;
        }
        return true;
    }

    /**
     * @throws IOException
     */
    static MathTransform parseWorldFile(Object source) throws IOException {
        MathTransform raster2Model = null;

        // TODO: Add support for FileImageInputStreamExt
        // TODO: Check for WorldFile on URL beside the actual connection.
        if (source instanceof File) {
            final File sourceFile = ((File) source);
            String parentPath = sourceFile.getParent();
            String filename = sourceFile.getName();
            final int i = filename.lastIndexOf('.');
            filename = (i == -1) ? filename : filename.substring(0, i);

            // getting name and extension
            final String base = (parentPath != null) ? parentPath + File.separator + filename
                    : filename;

            // We can now construct the baseURL from this string.
            File file2Parse = new File(base + ".wld");

            if (file2Parse.exists()) {
                final WorldFileReader reader = new WorldFileReader(file2Parse);
                raster2Model = reader.getTransform();
            } else {
                // looking for another extension
                file2Parse = new File(base + ".tfw");

                if (file2Parse.exists()) {
                    // parse world file
                    final WorldFileReader reader = new WorldFileReader(file2Parse);
                    raster2Model = reader.getTransform();
                }
            }
        }
        return raster2Model;
    }

    static CoordinateReferenceSystem getCRS(Object source) {
        CoordinateReferenceSystem crs = null;
        if (source instanceof File
                || (source instanceof URL && (((URL) source).getProtocol() == "file"))) {
            // getting name for the prj file
            final String sourceAsString;

            if (source instanceof File) {
                sourceAsString = ((File) source).getAbsolutePath();
            } else {
                String auth = ((URL) source).getAuthority();
                String path = ((URL) source).getPath();
                if (auth != null && !auth.equals("")) {
                    sourceAsString = "//" + auth + path;
                } else {
                    sourceAsString = path;
                }
            }

            final int index = sourceAsString.lastIndexOf(".");
            final String base = sourceAsString.substring(0, index) + ".prj";

            // does it exist?
            final File prjFile = new File(base);
            if (prjFile.exists()) {
                // it exists then we have top read it
                PrjFileReader projReader = null;
                try {
                    final FileChannel channel = new FileInputStream(prjFile).getChannel();
                    projReader = new PrjFileReader(channel);
                    crs = projReader.getCoordinateReferenceSystem();
                } catch (FileNotFoundException e) {
                    // warn about the error but proceed, it is not fatal
                    // we have at least the default crs to use
                    LOGGER.log(Level.INFO, e.getLocalizedMessage(), e);
                } catch (IOException e) {
                    // warn about the error but proceed, it is not fatal
                    // we have at least the default crs to use
                    LOGGER.log(Level.INFO, e.getLocalizedMessage(), e);
                } catch (FactoryException e) {
                    // warn about the error but proceed, it is not fatal
                    // we have at least the default crs to use
                    LOGGER.log(Level.INFO, e.getLocalizedMessage(), e);
                } finally {
                    if (projReader != null)
                        try {
                            projReader.close();
                        } catch (IOException e) {
                            // warn about the error but proceed, it is not fatal
                            // we have at least the default crs to use
                            LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
                        }
                }

            }
        }
        return crs;
    }

    /**
     * Creates image metadata which complies to the GeoTIFFWritingUtilities
     * specification for the given image writer, image type and
     * GeoTIFFWritingUtilities metadata.
     * 
     * @param writer
     *            the image writer, must not be null
     * @param type
     *            the image type, must not be null
     * @param geoTIFFMetadata
     *            the GeoTIFFWritingUtilities metadata, must not be null
     * @param params
     * @return the image metadata, never null
     * @throws IIOException
     *             if the metadata cannot be created
     */
    final static IIOMetadata createGeoTiffIIOMetadata(ImageWriter writer, ImageTypeSpecifier type,
            GeoTiffIIOMetadataEncoder geoTIFFMetadata, ImageWriteParam params) throws IIOException {
        IIOMetadata imageMetadata = writer.getDefaultImageMetadata(type, params);
        imageMetadata = writer.convertImageMetadata(imageMetadata, type, params);
        org.w3c.dom.Element w3cElement = (org.w3c.dom.Element) imageMetadata
                .getAsTree(GeoTiffConstants.GEOTIFF_IIO_METADATA_FORMAT_NAME);
        final Element element = new DOMBuilder().build(w3cElement);

        geoTIFFMetadata.assignTo(element);

        final Parent parent = element.getParent();
        parent.removeContent(element);

        final Document document = new Document(element);

        try {
            final org.w3c.dom.Document w3cDoc = new DOMOutputter().output(document);
            final IIOMetadata iioMetadata = new TIFFImageMetadata(TIFFImageMetadata.parseIFD(w3cDoc
                    .getDocumentElement().getFirstChild()));
            imageMetadata = iioMetadata;
        } catch (JDOMException e) {
            throw new IIOException("Failed to set GeoTIFFWritingUtilities specific tags.", e);
        } catch (IIOInvalidTreeException e) {
            throw new IIOException("Failed to set GeoTIFFWritingUtilities specific tags.", e);
        }

        return imageMetadata;
    }

    /** factory for getting tiff writers. */
    final static TIFFImageWriterSpi TIFFWRITERFACTORY = new TIFFImageWriterSpi();

    /** SPI for creating tiff readers in ImageIO tools */
    final static TIFFImageReaderSpi TIFFREADERFACTORY = new TIFFImageReaderSpi();

    static final double AFFINE_IDENTITY_EPS = 1E-6;

    /**
     * A transparent color for missing data.
     */
    static final Color TRANSPARENT = new Color(0, 0, 0, 0);
}
