/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.grassraster.metadata;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataFormat;
import javax.imageio.metadata.IIOMetadataNode;

import org.geotools.gce.grassraster.JGrassMapEnvironment;
import org.geotools.gce.grassraster.JGrassRegion;
import org.geotools.gce.grassraster.core.GrassBinaryRasterReadHandler;
import org.geotools.gce.grassraster.core.color.JGrassColorTable;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.w3c.dom.Node;

/**
 * Represents the metadata associated with the native grass binary map.
 * <p>
 * Supported metadata entries are for now:
 * <ol>
 * <li>those to create the region information:<BR>
 * <ul>
 * <li> {@linkplain GrassBinaryImageMetadata#north north}</li>
 * <li> {@linkplain GrassBinaryImageMetadata#south south}</li>
 * <li> {@linkplain GrassBinaryImageMetadata#east east}</li>
 * <li> {@linkplain GrassBinaryImageMetadata#west west}</li>
 * <li> {@linkplain GrassBinaryImageMetadata#nRows rows}</li>
 * <li> {@linkplain GrassBinaryImageMetadata#nCols cols}</li>
 * <li> {@linkplain GrassBinaryImageMetadata#xRes x resolution}</li>
 * <li> {@linkplain GrassBinaryImageMetadata#yRes y resolution}</li>
 * </ul>
 * </li>
 * <li>those to create the color table information:<BR>
 * <ul>
 * <li> {@linkplain GrassBinaryImageMetadata#colorRulesString the color rules}</li>
 * </ul>
 * </li>
 * <li>those to create the categories information:<BR>
 * <ul>
 * <li> {@linkplain GrassBinaryImageMetadata#categoriesString the categories}</li>
 * </ul>
 * </li>
 * <li>the {@linkplain GrassBinaryImageMetadata#noData novalue}</li>
 * </ol>
 * </p>
 * 
 * @author Andrea Antonello (www.hydrologis.com)
 * @since 3.0
 * @see {@link JGrassRegion}
 * @see {@link JGrassMapEnvironment}
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/grassraster/src/main/java/org/geotools/gce/grassraster/metadata/GrassBinaryImageMetadata.java $
 */
public final class GrassBinaryImageMetadata extends IIOMetadata {

    /**
     * the string used as separator in rules, to get them in one string.
     */
    public static final String RULESSPLIT = "@@split@@"; //$NON-NLS-1$
    /**
     * the NO_DATA key for the metadata tree.
     */
    public static final String NO_DATA = "NO_DATA";//$NON-NLS-1$
    /**
     * the NROWS key for the metadata tree.
     */
    public static final String NROWS = "NROWS";//$NON-NLS-1$
    /**
     * the NCOLS key for the metadata tree.
     */
    public static final String NCOLS = "NCOLS";//$NON-NLS-1$
    /**
     * the XRES key for the metadata tree.
     */
    public static final String XRES = "XRES";//$NON-NLS-1$
    /**
     * the YRES key for the metadata tree.
     */
    public static final String YRES = "YRES";//$NON-NLS-1$
    /**
     * the SOUTH key for the metadata tree.
     */
    public static final String SOUTH = "SOUTH";//$NON-NLS-1$
    /**
     * the WEST key for the metadata tree.
     */
    public static final String WEST = "WEST";//$NON-NLS-1$
    /**
     * the NORTH key for the metadata tree.
     */
    public static final String NORTH = "NORTH";//$NON-NLS-1$
    /**
     * the EAST key for the metadata tree.
     */
    public static final String EAST = "EAST";//$NON-NLS-1$
    /**
     * the CRS key for the metadata tree.
     */
    public static final String CRS = "CRS";//$NON-NLS-1$

    /**
     * the CATEGORIES_DESCRIPTOR key for the metadata tree.
     */
    public static final String CATEGORIES_DESCRIPTOR = "categoriesDescriptor"; //$NON-NLS-1$

    /**
     * the COLOR_RULES_DESCRIPTOR key for the metadata tree.
     */
    public static final String COLOR_RULES_DESCRIPTOR = "colorRulesDescriptor"; //$NON-NLS-1$

    /**
     * the REGION_DESCRIPTOR key for the metadata tree.
     */
    public static final String REGION_DESCRIPTOR = "regionDescriptor"; //$NON-NLS-1$

    /**
     * the native metadata format name.
     */
    public static final String nativeMetadataFormatName = "eu.hydrologis.jgrass.grassbinary.imageio.GrassBinaryImageMetadata_1.0"; //$NON-NLS-1$

    /**
     * the list of supported metadata format names. In this case, only the native metadata format is
     * supported.
     */
    public static final String[] metadataFormatNames = {nativeMetadataFormatName};

    /**
     * the value used to represent noData for an element of the raster.
     * <p>
     * As default set to {@link HMConstants#doubleNovalue}
     * </p>
     */
    private double noData = Double.NaN;

    /**
     * the size of a single cell of the raster grid along X.
     */
    private double xRes = Double.NaN;

    /**
     * the size of a single cell of the raster grid along Y.
     */
    private double yRes = Double.NaN;

    /**
     * the number of columns of the raster.
     */
    private int nCols = -1;

    /**
     * the number of rows of the raster.
     */
    private int nRows = -1;

    /**
     * The northern boundary of the raster region.
     */
    private double north = Double.NaN;

    /**
     * The southern boundary of the raster region.
     */
    private double south = Double.NaN;

    /**
     * The western boundary of the raster region.
     */
    private double west = Double.NaN;

    /**
     * The eastern boundary of the raster region.
     */
    private double east = Double.NaN;

    /**
     * the list of strings representing the colorrules of this raster.
     * <p>
     * <Color rules are described in the {@link JGrassColorTable} class.
     * </p>
     */
    private List<String> colorRulesString = null;

    /**
     * the list of strings representing the categories of this raster.
     * <p>
     * <b>EXAMPLE:</b> for a category of claysand referring to a value 5 on the raster we have:
     * <code>5.0:claysand</code>
     * </p>
     */
    private List<String> categoriesString = null;

    /**
     * the coordinate reference system for this map (may default to
     * {@link DefaultGeographicCRS#WGS84})
     */
    private CoordinateReferenceSystem crs;

    /**
     * Constructs the object using a {@link GrassBinaryRasterReadHandler} to initialize the metadata
     * fields.
     * 
     * @param rasterReader input {@link GrassBinaryRasterReadHandler} used to retrieve the metadata of the
     *        native grass raster file.
     */
    public GrassBinaryImageMetadata( GrassBinaryRasterReadHandler rasterReader ) {
        this();
        inizializeFromRaster(rasterReader);
    }

    /**
     * Default constructor.
     */
    public GrassBinaryImageMetadata() {
        super(false, nativeMetadataFormatName,
                "eu.hydrologis.jgrass.grassbinary.imageio.GrassBinaryImageMetadataFormat", null, //$NON-NLS-1$
                null);
    }

    /**
     * Constructs the object using user supplied metadata.
     * 
     * @param cols the number of columns.
     * @param rows the number of rows.
     * @param xRes the x size of the grid cell.
     * @param yRes the y size of the grid cell.
     * @param north the northern boundary.
     * @param south the southern boundary.
     * @param east the eastern boundary.
     * @param west the western boundary.
     * @param inNoData the value associated to noData grid values.
     * @param colorRules the list of colorrules.
     * @param categories the list of categories.
     */
    public GrassBinaryImageMetadata( int cols, int rows, double xRes, double yRes, double north,
            double south, double east, double west, double inNoData, List<String> colorRules,
            List<String> categories ) {
        this();
        nCols = cols;
        nRows = rows;
        this.xRes = xRes;
        this.yRes = yRes;
        this.north = north;
        this.west = west;
        this.east = east;
        this.south = south;
        noData = inNoData;
        if (colorRules != null)
            colorRulesString = colorRules;
        if (categories != null)
            categoriesString = categories;
    }

    public Node getAsTree( String formatName ) {
        if (formatName.equals(nativeMetadataFormatName)) {
            return getNativeTree();
        } else {
            throw new IllegalArgumentException("Not a recognized format!");
        }
    }

    public boolean isReadOnly() {
        return false;
    }

    public void mergeTree( String formatName, Node root ) throws IIOInvalidTreeException {
    }

    public void reset() {
        xRes = yRes = north = south = east = west = Double.NaN;
        nCols = nRows = -1;
    }

    public IIOMetadataFormat getMetadataFormat( String formatName ) {
        if (formatName.equals(nativeMetadataFormatName))
            return new GrassBinaryImageMetadataFormat();

        throw new IllegalArgumentException("Not a recognized format!");
    }

    /**
     * Initializes the metadata informations from a {@linkplain GrassBinaryRasterReadHandler}.
     * 
     * @param rasterReader the {@linkplain GrassBinaryRasterReadHandler} used to initialize fields.
     */
    private void inizializeFromRaster( GrassBinaryRasterReadHandler rasterReader ) {
        if (rasterReader != null) {
            JGrassRegion nativeRasterRegion = rasterReader.getNativeRasterRegion();
            nRows = nativeRasterRegion.getRows();
            nCols = nativeRasterRegion.getCols();
            xRes = nativeRasterRegion.getWEResolution();
            yRes = nativeRasterRegion.getNSResolution();
            north = nativeRasterRegion.getNorth();
            south = nativeRasterRegion.getSouth();
            east = nativeRasterRegion.getEast();
            west = nativeRasterRegion.getWest();
            noData = rasterReader.getNoData();
            try {
                colorRulesString = rasterReader.getColorRules(null);
                categoriesString = rasterReader.getCategories();
                crs = rasterReader.getCrs();
            } catch (IOException e) {
                throw new RuntimeException(e.getCause());
            }
        }
    }

    @SuppressWarnings("nls")
    protected IIOMetadataNode getStandardCompressionNode() {
        IIOMetadataNode node = new IIOMetadataNode("Compression");
        IIOMetadataNode subNode = new IIOMetadataNode("Lossless");
        subNode.setAttribute("value", "TRUE");
        node.appendChild(subNode);
        return node;
    }

    /**
     * Creates a {@linkplain Node} tree from the available metadata informations of the native
     * raster.
     * 
     * @return the root of the tree containing the metadata of the native raster.
     */
    private Node getNativeTree() {
        final IIOMetadataNode root = new IIOMetadataNode(nativeMetadataFormatName);

        // Setting region Properties
        IIOMetadataNode node = new IIOMetadataNode(REGION_DESCRIPTOR);
        node.setAttribute(NO_DATA, Double.toString(noData));
        node.setAttribute(NROWS, Integer.toString(nRows));
        node.setAttribute(NCOLS, Integer.toString(nCols));
        node.setAttribute(XRES, Double.toString(xRes));
        node.setAttribute(YRES, Double.toString(yRes));
        node.setAttribute(NORTH, Double.toString(north));
        node.setAttribute(SOUTH, Double.toString(south));
        node.setAttribute(EAST, Double.toString(east));
        node.setAttribute(WEST, Double.toString(west));
        node.setAttribute(CRS, crs.toWKT());
        root.appendChild(node);

        // Setting Colortable Properties
        if (colorRulesString != null) {
            node = new IIOMetadataNode(COLOR_RULES_DESCRIPTOR);
            StringBuffer sB = new StringBuffer();
            for( int i = 0; i < colorRulesString.size(); i++ ) {
                if (i != 0) {
                    sB.append(RULESSPLIT); //$NON-NLS-1$
                }
                sB.append(colorRulesString.get(i));
            }
            node.setAttribute(GrassBinaryImageMetadata.COLOR_RULES_DESCRIPTOR, sB.toString());
            root.appendChild(node);
        }

        // Setting Categories Properties
        if (categoriesString != null) {
            node = new IIOMetadataNode(CATEGORIES_DESCRIPTOR);
            StringBuffer sB = new StringBuffer();
            for( int i = 0; i < categoriesString.size(); i++ ) {
                if (i != 0) {
                    sB.append(RULESSPLIT); //$NON-NLS-1$
                }
                sB.append(categoriesString.get(i));
            }
            node.setAttribute(GrassBinaryImageMetadata.CATEGORIES_DESCRIPTOR, sB.toString());
            root.appendChild(node);
        }

        return root;
    }

    @SuppressWarnings("nls")
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("GrassBinaryImageMetadata[");
        buffer.append("\n " + NORTH + " = ").append(north);
        buffer.append("\n " + SOUTH + " = ").append(south);
        buffer.append("\n " + EAST + " = ").append(east);
        buffer.append("\n " + WEST + " = ").append(west);
        buffer.append("\n " + XRES + " = ").append(xRes);
        buffer.append("\n " + YRES + " = ").append(yRes);
        buffer.append("\n " + NCOLS + " = ").append(nCols);
        buffer.append("\n " + NROWS + " = ").append(nRows);
        buffer.append("\n " + NO_DATA + " = ").append(noData);
        buffer.append("\n\ncategoriesString = ");
        for( String cat : categoriesString ) {
            buffer.append("\n").append(cat);
        }
        buffer.append("\n colorRulesString = ");
        for( String colr : colorRulesString ) {
            buffer.append("\n").append(colr);
        }
        buffer.append("]");
        return buffer.toString();
    }

    /**
     * Creates a hashtable of the metadata for easier access.
     * 
     * @return the metadata hashtable.
     */
    public HashMap<String, String> toHashMap() {
        HashMap<String, String> tmp = new HashMap<String, String>();
        tmp.put(WEST, String.valueOf(west));
        tmp.put(SOUTH, String.valueOf(south));
        tmp.put(EAST, String.valueOf(east));
        tmp.put(NORTH, String.valueOf(north));
        tmp.put(NROWS, String.valueOf(nRows));
        tmp.put(NCOLS, String.valueOf(nCols));
        tmp.put(XRES, String.valueOf(xRes));
        tmp.put(YRES, String.valueOf(yRes));
        tmp.put(NO_DATA, String.valueOf(noData));
        tmp.put(CRS, crs.toWKT());

        StringBuffer buffer = new StringBuffer();
        for( String cat : categoriesString ) {
            buffer.append(cat).append(RULESSPLIT);
        }
        tmp.put(CATEGORIES_DESCRIPTOR, buffer.toString());

        buffer = new StringBuffer();
        for( String colr : colorRulesString ) {
            buffer.append(colr).append(RULESSPLIT);
        }
        tmp.put(COLOR_RULES_DESCRIPTOR, buffer.toString());
        return tmp;
    }

}
