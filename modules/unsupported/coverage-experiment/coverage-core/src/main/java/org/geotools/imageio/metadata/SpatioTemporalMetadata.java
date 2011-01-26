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
package org.geotools.imageio.metadata;

import it.geosolutions.imageio.ndplugin.BaseImageMetadata;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;

import org.geotools.imageio.SpatioTemporalImageReader;
import org.opengis.temporal.Instant;
import org.opengis.temporal.Period;
import org.opengis.temporal.TemporalObject;
import org.w3c.dom.Node;

public abstract class SpatioTemporalMetadata extends IIOMetadata {

    private final static Logger LOGGER = Logger.getLogger(SpatioTemporalMetadata.class.toString());

    /** the imageIndex referring this specific metadata instance */
    private int imageIndex;

    public SpatioTemporalMetadata(final SpatioTemporalImageReader reader,final int imageIndex) {
        this.imageIndex = imageIndex;
        setCoordinateReferenceSystemElement(reader);
        setBoundedByElement(reader);
        setRectifiedGridElement(reader);
        setBandsElement(reader);
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public void mergeTree(String formatName, Node root)
            throws IIOInvalidTreeException {
    }

    @Override
    public void reset() {
    	
    }

    public int getImageIndex() {
        return imageIndex;
    }

    /**
     * The root node to be returned by {@link #getAsTree}.
     */
    private Node root;

    /**
     * The spatial coordinate reference system node. Will be created only when
     * first needed.
     */
    private CoordinateReferenceSystem crs;

    /**
     * The vertical coordinate reference system node. Will be created only when
     * first needed.
     */
    private VerticalCRS verticalCRS;

    /** {@code true} if a VerticalCRS is available */
    private boolean hasVerticalCRS = false;

    /**
     * The temporal coordinate reference system node. Will be created only when
     * first needed.
     */
    private TemporalCRS temporalCRS;

    /** {@code true} if a TemporalCRS is available */
    private boolean hasTemporalCRS = false;

    /**
     * The rectified grid node. Will be created only when first needed.
     */
    private RectifiedGrid rectifiedGrid;

    /**
     * The boundedBy node. Will be created only when first needed.
     */
    private BoundedBy boundedBy;

    /**
     * The list of {@linkplain Band bands}. Will be created only
     * when first needed.
     */
    private ChildList<Band> bands;

    private void checkFormatName(final String formatName) throws IllegalArgumentException {
        if (!SpatioTemporalMetadataFormat.FORMAT_NAME.equals(formatName)) {
            throw new IllegalArgumentException("Illegal formatName:"+ formatName);
        }
    }

    /**
     * Returns the root of a tree of metadata contained within this object
     * according to the conventions defined by a given metadata format.
     * 
     * @param formatName
     *                the desired metadata format.
     * @return The node forming the root of metadata tree.
     * @throws IllegalArgumentException
     *                 if the format name is {@code null} or is not one of the
     *                 names returned by {@link #getMetadataFormatNames()
     *                 getMetadataFormatNames()}.
     */
    public Node getAsTree(final String formatName) throws IllegalArgumentException {
        checkFormatName(formatName);
        return getRootNode();
    }

    public boolean isHasVerticalCRS() {
        return hasVerticalCRS;
    }

    /**
     * Call this method before setting a VerticalCRS node
     * 
     * @param hasVerticalCRS
     */
    public void setHasVerticalCRS(boolean hasVerticalCRS) {
        this.hasVerticalCRS = hasVerticalCRS;
    }

    public boolean isHasTemporalCRS() {
        return hasTemporalCRS;
    }

    /**
     * Call this method before setting a TemporalCRS node
     * 
     * @param hasTemporalCRS
     */
    public void setHasTemporalCRS(boolean hasTemporalCRS) {
        this.hasTemporalCRS = hasTemporalCRS;
    }

    /**
     * Returns the root of a tree of metadata contained within this object
     * according to the conventions defined by a given metadata format.
     */
    final  Node getRootNode() {
        if (root == null) {
            root = new IIOMetadataNode(SpatioTemporalMetadataFormat.FORMAT_NAME);
        }
        return root;
    }

    /**
     * Returns the list of all {@linkplain Band bands}.
     */
    final ChildList<Band> getSampleDimensions() {
        if (bands == null) {
            bands = new ChildList.Bands(this);
        }
        return bands;
    }

    /**
     * Returns a {@linkplain Band band}.
     */
    public Band getBand(int dimensionIndex) {
        return getSampleDimensions().getChild(dimensionIndex);
    }

    /**
     * Add a Sample Dimension to the Sample Dimensions node.
     */
    public  Band addBand() {
        if (bands == null) {
            bands = new ChildList.Bands(this);
        }
        Band candidate = bands.addChild();
        return candidate;
    }

    /**
     * Returns the CoordinateReferenceSystem.
     */
    public  CoordinateReferenceSystem getCRS(final String crsType) {
        if (crs == null) {
            crs = new CoordinateReferenceSystem(this, crsType);
        }
        return crs;
    }

    /**
     * Returns the CoordinateReferenceSystem.
     */
    public CoordinateReferenceSystem getCRS() {
        return getCRS(null);
    }

    /**
     * Returns the Vertical CoordinateReferenceSystem node.
     */
    public  VerticalCRS getVerticalCRS() {
        if (verticalCRS == null && hasVerticalCRS) {
            verticalCRS = new VerticalCRS(this);
        }
        return verticalCRS;
    }

    /**
     * Returns the Temporal CoordinateReferenceSystem node.
     */
    public  TemporalCRS getTemporalCRS() {
        if (temporalCRS == null && hasTemporalCRS) {
            temporalCRS = new TemporalCRS(this);
        }
        return temporalCRS;
    }

    /**
     * Returns the rectified grid.
     */
    public  RectifiedGrid getRectifiedGrid() {
        if (rectifiedGrid == null) {
            rectifiedGrid = new RectifiedGrid(this);
        }
        return rectifiedGrid;
    }

    /**
     * Returns the Bounded by domain.
     */
    public  BoundedBy getBoundedBy() {
        if (boundedBy == null) {
            boundedBy = new BoundedBy(this);
        }
        return boundedBy;
    }

    /**
     * Set the {@code coordinateReferenceSystem} metadata element.
     * 
     * @param reader
     *                the {@link SpatioTemporalImageReader} to be used to set
     *                this metadata element.
     */
    protected abstract void setCoordinateReferenceSystemElement(SpatioTemporalImageReader reader);

    /**
     * Set the {@code boundedBy} metadata element.
     * 
     * @param reader
     *                the {@link SpatioTemporalImageReader} to be used to set
     *                this metadata element.
     */
    protected abstract void setBoundedByElement(SpatioTemporalImageReader reader);

    /**
     * Set the {@code rectifiedGrid} metadata element.
     * 
     * @param reader
     *                the {@link SpatioTemporalImageReader} to be used to set
     *                this metadata element.
     */
    protected abstract void setRectifiedGridElement(SpatioTemporalImageReader reader);

    /**
     * Set the {@code bands} metadata element.
     * 
     * @param reader
     *                the {@link SpatioTemporalImageReader} to be used to set
     *                this metadata element.
     */
    protected abstract void setBandsElement(SpatioTemporalImageReader reader);

    /**
     * Utility method which allows to fill Bands attributes using a
     * {@link BaseImageMetadata} instance obtained from the underlying flat
     * reader.
     * 
     * @param band
     *                the {@link Band} metadata object to be set.
     * @param baseMetadata
     *                a {@link BaseImageMetadata} instance containing the
     *                required values.
     */
    protected static void setBandFromCommonMetadata(Band band, BaseImageMetadata baseMetadata) {
        if (band == null)
            throw new IllegalArgumentException("Provided sample dimension to be set is null");

        if (baseMetadata != null) {
            String name = "";
            double scale = 1.0;
            double offset = 0.0;
            double[] validRange = null;
            double[] noDataValues = null;
            try {
                scale = baseMetadata.getScale(0);
                offset = baseMetadata.getOffset(0);
            } catch (IllegalArgumentException iae) {
                // TODO: no scale and offset are available
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("Using default scale and offset values");
                }
            }
            band.setScale(scale);
            band.setOffset(offset);
            try {
                double max = baseMetadata.getMaximum(0);
                double min = baseMetadata.getMinimum(0);
                validRange = new double[] { min, max };
            } catch (IllegalArgumentException iae) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("ValidRange values not found");
                }
            }
            if (validRange != null) {
                band.setValidRange(validRange[0], validRange[1]);
            }
            try {
                double noData = baseMetadata.getNoDataValue(0);
                noDataValues = new double[] { noData };
            } catch (IllegalArgumentException iae) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("NoData values not found");
                }
            }
            if (noDataValues != null) {
                band.setNoDataValues(noDataValues);
            }
            name = baseMetadata.getDatasetName();
            band.setName(name);
        } else
            throw new IllegalArgumentException("Provided metadata object is null");
    }
    
    /**
     * Set the TimeExtent sub node of a BoundedBy metadata node
     * 
     * @param bb
     * @param timeExtent
     */
    protected static void setTimeExtentNode(final BoundedBy bb, final TemporalObject timeExtent) {
        if (bb == null)
            throw new IllegalArgumentException("Provided BoundedBy element is null");
        if (timeExtent != null) {
            if (timeExtent instanceof Period) {
                Period p = (Period) timeExtent;
                p.getBeginning().getPosition().getDateTime().toString();
                bb.setTemporalExtent(new String[] {p.getBeginning().getPosition().getDateTime().toString(),
                                p.getEnding().getPosition().getDateTime().toString()});
            } else if (timeExtent instanceof Instant) {
                bb.setTemporalExtent(((Instant) timeExtent).getPosition().getDateTime().toString());
            } else
                throw new IllegalArgumentException("Unhandled temporal object: " + timeExtent.getClass().getName());

        } else {
            throw new IllegalArgumentException("Provided TemporalObject is null");
        }
    }
}
