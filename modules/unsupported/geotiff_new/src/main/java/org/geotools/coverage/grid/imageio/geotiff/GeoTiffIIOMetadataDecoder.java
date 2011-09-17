/*
 * (c) 2004 Mike Nidel
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
 *
 * Take, Modify, Distribute freely
 * Buy, Sell, Pass it off as your own
 *
 * Use this code at your own risk, the author makes no guarantee
 * of performance and retains no liability for the failure of this
 * software.
 *
 * If you feel like it, send any suggestions for improvement or
 * bug fixes, or modified sourceFile code to mike@gelbin.org
 *
 * Do not taunt Happy Fun Ball.
 *
 */
/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.grid.imageio.geotiff;

import it.geosolutions.imageio.plugins.tiff.GeoTIFFTagSet;

import java.awt.geom.AffineTransform;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;

import org.geotools.coverage.grid.imageio.geotiff.metadata.codes.GeoTiffGCSCodes;
import org.geotools.coverage.grid.io.imageio.geotiff.GeoKeyEntry;
import org.geotools.coverage.grid.io.imageio.geotiff.GeoTiffConstants;
import org.geotools.coverage.grid.io.imageio.geotiff.GeoTiffIIOMetadataDecoder;
import org.geotools.coverage.grid.io.imageio.geotiff.PixelScale;
import org.geotools.coverage.grid.io.imageio.geotiff.TiePoint;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class provides an abstraction from the details of TIFF data access for
 * the purpose of retrieving GeoTIFFWritingUtilities metadata from an image.
 * 
 * <p>
 * All of the GeoKey values are included here as constants, and the portions of
 * the GeoTIFFWritingUtilities specification pertaining to each have been copied
 * for easy access.
 * </p>
 * 
 * <p>
 * The majority of the possible GeoKey values and their meanings are NOT
 * reproduced here. Only the most important GeoKey code values have been copied,
 * for others see the specification.
 * </p>
 * 
 * <p>
 * Convenience methods have been included to retrieve the various TIFFFields
 * that are not part of the GeoKey directory, such as the Model Transformation
 * and Model TiePoints. Retrieving a GeoKey from the GeoKey directory is a bit
 * more specialized and requires knowledge of the correct key code.
 * </p>
 * 
 * <p>
 * Making use of the geographic metadata still requires some basic understanding
 * of the GeoKey values that is not provided here.
 * </p>
 * 
 * <p>
 * For more information see the GeoTIFFWritingUtilities specification at
 * http://www.remotesensing.org/geotiff/spec/geotiffhome.html
 * </p>
 * 
 * @author Mike Nidel
 * @author Simone Giannecchini, GeoSolutions
 * 
 * @sourceFile $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/plugin/geotiff/src/org/geotools/gce/geotiff/IIOMetadataAdpaters/GeoTiffIIOMetadataDecoder.java $
 */
public final class GeoTiffIIOMetadataDecoder {
    
    /** {@link Logger}. */
    private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(GeoTiffIIOMetadataDecoder.class);

    private final HashMap<Integer, GeoKeyEntry> geoKeys;

    private int geoKeyDirVersion;

    private int geoKeyRevision;

    private int geoKeyMinorRevision;

    private int geoKeyDirTagsNum;

    private final PixelScale pixelScale;

    private final TiePoint[] tiePoints;

    private final double noData;

    private final AffineTransform modelTransformation;

    private IIOMetadataNode rootNode;

    /**
     * The constructor builds a metadata adapter for the image metadata root IIOMetadataNode.
     * 
     * @param imageMetadata
     *            The image metadata
     */
    public GeoTiffIIOMetadataDecoder(final IIOMetadata imageMetadata) {
        if (imageMetadata == null) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.NULL_ARGUMENT_$1,
                    "imageMetadata"));
        }
        // getting the image metadata root node.
        rootNode = (IIOMetadataNode) imageMetadata.getAsTree(imageMetadata.getNativeMetadataFormatName());
        if (rootNode == null) {
            throw new NullPointerException("Unable to retrieve metadata");
        }

        // getting the geokey directory
        IIOMetadataNode geoKeyDir = GeoTiffUtilities.getTiffField(rootNode, GeoTIFFTagSet.TAG_GEO_KEY_DIRECTORY);
        geoKeys = new HashMap<Integer, GeoKeyEntry>();
        if (geoKeyDir != null) {
            NodeList geoKeyDirEntries = geoKeyDir.getFirstChild().getChildNodes();
            for (int i = 4; i < geoKeyDirEntries.getLength(); i += 4) {
                int keyID = GeoTiffUtilities.getIntValueAttribute(geoKeyDirEntries.item(i));
                GeoKeyEntry key = new GeoKeyEntry(keyID,// key
                        GeoTiffUtilities.getIntValueAttribute(geoKeyDirEntries.item(i + 1)),// location
                        GeoTiffUtilities.getIntValueAttribute(geoKeyDirEntries.item(i + 2)),// count
                        GeoTiffUtilities.getIntValueAttribute(geoKeyDirEntries.item(i + 3)));// offset

                if (!geoKeys.containsKey(keyID)) {
                    geoKeys.put(keyID, key);
                }
            }
            // GeoKeyDirVersion and the other parameters
            geoKeyDirVersion = GeoTiffUtilities.getTiffShort(geoKeyDir,
                    GeoTiffGCSCodes.GEO_KEY_DIRECTORY_VERSION_INDEX);
            geoKeyRevision = GeoTiffUtilities.getTiffShort(geoKeyDir, GeoTiffGCSCodes.GEO_KEY_REVISION_INDEX);
            if (geoKeyRevision != 1) {
                geoKeyRevision = 1;
                // I had to remove this because I did not want to have wrong
                // revision numbers blocking us.
                // throw new UnsupportedOperationException("Unsupported revision");
            }
            geoKeyMinorRevision = GeoTiffUtilities.getTiffShort(geoKeyDir,
                    GeoTiffGCSCodes.GEO_KEY_MINOR_REVISION_INDEX);
            // loading the number of geokeys inside the geokeydirectory
            geoKeyDirTagsNum = GeoTiffUtilities.getTiffShort(geoKeyDir, GeoTiffGCSCodes.GEO_KEY_NUM_KEYS_INDEX);
        } else {
            if(LOGGER.isLoggable(Level.FINE))
                LOGGER.log(Level.FINE,"Unable to find the geo key directory tag");
        }
        
        pixelScale = GeoTiffUtilities.calculateModelPixelScales(rootNode);
        tiePoints = GeoTiffUtilities.calculateTiePoints(rootNode);
        noData = GeoTiffUtilities.calculateNoData(rootNode);
        modelTransformation = GeoTiffUtilities.calculateModelTransformation(rootNode);

       

    }

    /**
     * Gets the version of the GeoKey directory. This is typically a value of 1 and can be used to
     * check that the data is of a valid format.
     * 
     * @return DOCUMENT ME!
     * 
     * @throws UnsupportedOperationException
     *             DOCUMENT ME!
     */
    public int getGeoKeyDirectoryVersion() {
        // now get the value from the correct TIFFShort location
        return geoKeyDirVersion;
    }

    /**
     * Gets the revision number of the GeoKeys in this metadata.
     * 
     * @return DOCUMENT ME!
     * 
     * @throws UnsupportedOperationException
     *             DOCUMENT ME!
     */
    public int getGeoKeyRevision() {
        // Get the value from the correct TIFFShort
        return geoKeyRevision;
    }

    /**
     * Gets the minor revision number of the GeoKeys in this metadata.
     * 
     * @return DOCUMENT ME!
     * 
     * @throws UnsupportedOperationException
     *             DOCUMENT ME!
     */
    public int getGeoKeyMinorRevision() {
        // Get the value from the correct TIFFShort
        return geoKeyMinorRevision;
    }

    /**
     * Gets the number of GeoKeys in the geokeys directory.
     * 
     * @return DOCUMENT ME!
     * 
     * @throws UnsupportedOperationException
     *             DOCUMENT ME!
     */
    public int getNumGeoKeys() {
        return geoKeyDirTagsNum;
    }

    /**
     * Gets a GeoKey value as a String. This implementation should be &quotquiet&quot in the sense
     * that it should not throw any exceptions but only return null in the event that the data
     * organization is not as expected.
     * 
     * @param keyID
     *            The numeric ID of the GeoKey
     * 
     * @return A string representing the value, or null if the key was not found.
     */
    public String getGeoKey(final int keyID) {

        final GeoKeyEntry rec = getGeoKeyRecord(keyID);
        if (rec == null) {
            return null;
        }
        if (rec.getTiffTagLocation() == 0) {
            // value is stored directly in the GeoKey record
            return String.valueOf(rec.getValueOffset());
        }

        // value is stored externally
        // get the TIFF field where the data is actually stored
        final IIOMetadataNode field = GeoTiffUtilities.getTiffField(rootNode, rec.getTiffTagLocation());
        if (field == null) {
            return null;
        }

        final Node sequence = field.getFirstChild();
        if (sequence == null) {
            return null;
        }

        return sequence.getNodeName().equals(GeoTiffConstants.GEOTIFF_ASCIIS_TAG) ? GeoTiffUtilities.getTiffAscii(
                (IIOMetadataNode) sequence, rec.getValueOffset(), rec.getCount())
                : GeoTiffUtilities.getValueAttribute(sequence.getChildNodes().item(rec.getValueOffset()));
    }

    /**
     * Gets a record containing the four TIFFShort values for a geokey entry. For more information
     * see the GeoTIFFWritingUtilities specification.
     * 
     * @param keyID
     *            DOCUMENT ME!
     * 
     * @return the record with the given keyID, or null if none is found
     * 
     * @throws UnsupportedOperationException
     *             DOCUMENT ME!
     */
    public GeoKeyEntry getGeoKeyRecord(int keyID) {
        return geoKeys.get(keyID);
    }

    /**
     * Return the GeoKeys.
     * 
     * @return
     **/
    Collection<GeoKeyEntry> getGeoKeys() {
        return geoKeys.values();
    }

    /**
     * Gets the model pixel scales from the correct TIFFField
     */
    public PixelScale getModelPixelScales() {
        return pixelScale;
    }

    /**
     * Gets the model tie points from the appropriate TIFFField
     * 
     * @return the tie points, or null if not found
     */
    public TiePoint[] getModelTiePoints() {
        return tiePoints.clone();
    }

    /**
     * Gets the noData from the related TIFFField. Check metadata has noData using
     * {@link #hasNoData()} method before calling this method.
     * 
     * @return the noData value or {@link Double#NaN} in case of unable to get noData.
     * 
     */
    public double getNoData() {
        return noData;
    }

    /**
     * Tells me if the underlying {@link IIOMetadata} contains ModelTiepointTag tag for
     * {@link TiePoint}.
     * 
     * @return true if ModelTiepointTag is present, false otherwise.
     */
    public boolean hasTiePoints() {
        return tiePoints != null && tiePoints.length > 0;
    }

    /**
     * Tells me if the underlying {@link IIOMetadata} contains ModelTiepointTag tag for
     * {@link TiePoint}.
     * 
     * @return true if ModelTiepointTag is present, false otherwise.
     */
    public boolean hasPixelScales() {
        if (pixelScale == null) {
            return false;
        } else {
            final double[] values = pixelScale.getValues();
            for (int i = 0; i < values.length; i++) {
                if (Double.isInfinite(values[i]) || Double.isNaN(values[i])) {
                    return false;
                }
            }
            return true;
        }

    }

    /**
     * Tells me if the underlying {@link IIOMetadata} contains NoData Tag.
     * 
     * @return true if NoData Tag is present, false otherwise.
     * @see GeoTiffConstants#TIFFTAG_NODATA
     */
    public boolean hasNoData() {
        return !Double.isNaN(noData);
    }

    /**
     * Gets the model tie points from the appropriate TIFFField
     * 
     * <p>
     * Attention, for the moment we support only 2D baseline transformations.
     * 
     * @return the transformation, or null if not found
     */
    public AffineTransform getModelTransformation() {
        return modelTransformation;
    }

    /**
     * Tells me if the underlying {@link IIOMetadata} contains ModelTransformationTag tag for
     * {@link AffineTransform} that map from Raster Space to World Space.
     * 
     * @return true if ModelTransformationTag is present, false otherwise.
     * 
     */
    public boolean hasModelTrasformation() {
        return modelTransformation != null;
    }

    // private utility methods

    /**
     * Return <code>true</code> if the geokey directory is present, <code>false</code> otherwise. In
     * case no geokey dir is present no CRS can be constructed from this set of metadata.
     * 
     * <p>
     * A prj can be used otherwise.
     * 
     * @return <code>true</code> if the geokey directory is present, <code>false</code> otherwise.
     */
    public boolean hasGeoKey() {
        return !geoKeys.isEmpty();
    }
} // end of class GeoTiffIIOMetadataDecoder
