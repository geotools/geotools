/*
 *    ImageI/O-Ext - OpenSource Java Image translation Library
 *    http://www.geo-solutions.it/
 *    http://java.net/projects/imageio-ext/
 *    (C) 2007 - 2009, GeoSolutions
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    either version 3 of the License, or (at your option) any later version.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.imageio.hdf4.terascan;

import it.geosolutions.imageio.core.CoreCommonImageMetadata;

import java.io.IOException;
import java.util.HashMap;

import javax.imageio.metadata.IIOMetadataNode;

import org.geotools.imageio.netcdf.NetCDFUtilities.KeyValuePair;
import org.geotools.imageio.unidata.UnidataImageMetadata;
import org.geotools.imageio.unidata.UnidataImageReader;
import org.w3c.dom.Node;

public class HDF4TeraScanImageMetadata extends UnidataImageMetadata {
    public static final String nativeMetadataFormatName = "it_geosolutions_imageio_plugins_hdf4_terascan_terascanImageMetadata_1.0";

    private final static String driverName = "HDF4";

    private final static String driverDescription = "Hierarchical Data Format Release 4";

    private HashMap<String, String> additionalMetadata;

    private IIOMetadataNode nativeTree;

    public HDF4TeraScanImageMetadata(final UnidataImageReader reader,
            final int imageIndex) {
        super(reader, imageIndex);
    }

    protected void setMembers(UnidataImageReader imageReader) throws IOException {
        super.setMembers(imageReader);
        final int imageIndex = getImageIndex();
        if (imageReader instanceof HDF4TeraScanImageReader) {
            final HDF4TeraScanImageReader reader = (HDF4TeraScanImageReader) imageReader;
            setDriverDescription(driverDescription);
            setDriverName(driverName);
            final double scale = reader.getScale(imageIndex);
            if (!Double.isNaN(scale))
                setScales(new Double[] { Double.valueOf(scale) });
            final double offset = reader.getOffset(imageIndex);
            if (!Double.isNaN(offset))
                setOffsets(new Double[] { Double.valueOf(offset) });

            String name = reader.getLongName(imageIndex);
            if (name != null && name.trim().length() > 0) {
                setDatasetName(name);
            }
            final double noData = reader.getFillValue(imageIndex);
            if (!Double.isNaN(noData)) {
                setNoDataValues(new Double[] { Double.valueOf(noData) });
            }

            // TODO: Setting valid range as max min is ok?
            final double[] validRange = reader.getValidRange(imageIndex);
            if (validRange != null && validRange.length == 2
                    && !Double.isNaN(validRange[0])
                    && !Double.isNaN(validRange[1])) {
                setMinimums(new Double[] { Double.valueOf(validRange[0]) });
                setMaximums(new Double[] { Double.valueOf(validRange[1]) });
            }

            // overviews is always 0
            setNumOverviews(new int[] { 0 });
            
            final int numAttributes = reader.getNumAttributes(imageIndex);
            this.additionalMetadata = new HashMap<String, String>(numAttributes);
            for (int i = 0; i < numAttributes; i++) {
            	final KeyValuePair attributePair = reader.getAttribute(imageIndex, i);
                final String attributeName = attributePair.getKey();
                final String attributeValue = attributePair.getValue();
                additionalMetadata.put(attributeName, attributeValue);
            }
        }
    }

    /**
     * Returns an XML DOM <code>Node</code> object that represents the root of
     * a tree of common stream metadata contained within this object according
     * to the conventions defined by a given metadata format name.
     * 
     * @param formatName
     *                the name of the requested metadata format.
     */
    public Node getAsTree(String formatName) {
        if (HDF4TeraScanImageMetadata.nativeMetadataFormatName.equalsIgnoreCase(formatName))
            return createNativeTree();
        else if (CoreCommonImageMetadata.nativeMetadataFormatName.equalsIgnoreCase(formatName))
            return super.createCommonNativeTree();
        throw new IllegalArgumentException(formatName+ " is not a supported format name");
    }

    private Node createNativeTree() {
        if (this.nativeTree != null)
            return this.nativeTree;
        nativeTree = new IIOMetadataNode(HDF4TeraScanImageMetadata.nativeMetadataFormatName);

        // ////////////////////////////////////////////////////////////////////
        //
        // DatasetDescriptor
        //
        // ////////////////////////////////////////////////////////////////////
        if (this.additionalMetadata != null) {
            IIOMetadataNode node = new IIOMetadataNode(ATTRIBUTES_NODE);

            for (String key : this.additionalMetadata.keySet()) {
                final String attributeValue = additionalMetadata.get(key);
                node.setAttribute(key, attributeValue);
            }

            nativeTree.appendChild(node);
        }

        return nativeTree;
    }
}
