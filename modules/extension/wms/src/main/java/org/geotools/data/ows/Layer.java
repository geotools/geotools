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
package org.geotools.data.ows;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.geotools.data.wms.xml.Dimension;
import org.geotools.data.wms.xml.Extent;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

/**
 * Nested list of zero or more map Layers offered by this server. It contains only fields for
 * information that we currently find interesting. Feel free to add your own.
 * 
 * @author rgould
 *
 * @source $URL$
 *         http://svn.osgeo.org/geotools/branches/2.6.x/modules/extension/wms/src/main/java/org
 *         /geotools/data/ows/Layer.java $
 */
public class Layer implements Comparable<Layer> {
    /** A machine-readable (typically one word) identifier */
    private String name;

    /**
     * Parent Layer may be null if this is the root Layer for a WMS Service
     */
    private Layer parent;

    /**
     * Child Layers (Note we should clear the children cache if any of setter methods are called)
     */
    private List<Layer> children = new ArrayList<Layer>();

    /** The title is for informative display to a human. */
    private String title;

    private String _abstract;

    private String[] keywords;

    /**
     * A set of Strings representing SRSs. These are the SRSs contributed by this layer. For the
     * complete list you need to consider these values and those defined by its parent.
     */
    private Set<String> srs = null;

    /**
     * The bounding boxes on each layer; usually this matches the actual data coordinate reference
     * system (compare with latLonBoundingBox) near as I can tell multiple boundingBoxes are here to
     * account for either data that moves over time; or compound layers that combine several data
     * sets.
     */
    private List<CRSEnvelope> boundingBoxes = new ArrayList<CRSEnvelope>();

    /**
     * A BoundingBox containing the minimum rectangle of the map data in EPSG:4326
     */
    private CRSEnvelope latLonBoundingBox = null;

    /** A list of type org.opengis.layer.Style */
    private List<StyleImpl> styles;

    /** Indicates if this layer responds to a GetFeatureInfo query */
    private Boolean queryable = null;

    private double scaleHintMin = Double.NaN;

    private double scaleHintMax = Double.NaN;

    private double scaleDenominatorMin = Double.NaN;

    private double scaleDenominatorMax = Double.NaN;

    private List<Dimension> dimensions = new ArrayList<Dimension>();

    private List<Extent> extents = new ArrayList<Extent>();

    // Cache
    // These cache data structures are used to store the union of this Layers definition
    // with those inherited from the parent layer.
    //
    // These are all null by default; and populated as needed to respond to get methods;
    // clearCache() reset's these caches to null
    //
    /**
     * The union of the layers's SRSs and the parent's SRSs. This cache is used to check if the SRS
     * is known to work for this layer
     */
    private Set<String> allSRSCache = null;

    /**
     * The union of the layer's boundingBoxes and the parent's bounding boxes.
     */
    private Map<String, CRSEnvelope> allBoundingBoxesCache = null;

    /**
     * A HashMap recording the dimensions on each layer. The Key is the name of the dimension (case
     * insensitive). The Value is the Dimension object itself. Chances are you need to check the
     * parent Dimensions as well.
     */
    private HashMap<String, Dimension> allDimensionsCache = null;

    /**
     * A HashMap recoding the extents on each layer. An Extent is not valid unless there is a
     * Dimension with the same name. The Key is the name of the dimension (case insensitive). The
     * Value is the Extent object itself.
     */
    private HashMap<String, Extent> allExtentsCache = null;

    /**
     * This is where we try and go from our rather lame CRSEnvelope data structure to an actual
     * RefernecedEnvelope with a real CoordianteReferenceSystem.
     */
    private Map<CoordinateReferenceSystem, Envelope> envelopeCache = Collections
            .synchronizedMap(new WeakHashMap<CoordinateReferenceSystem, Envelope>());

    /**
     * Called to clear the internal cache of this layer; and any children.
     */
    public void clearCache() {
        allSRSCache = null;
        allExtentsCache = null;
        allDimensionsCache = null;
        allBoundingBoxesCache = null;
        envelopeCache.clear();
        for( Layer child : children ){
            child.clearCache();
        }
    }

    /**
     * Crate a layer with no human readable title.
     * <p>
     * These layers are simply for organization and storage of common settings (like SRS or style
     * settings). These settings will be valid for all children.
     */
    public Layer() {
        this(null);
    }

    /**
     * Create a layer with an optional title
     * 
     * @param title
     */
    public Layer(String title) {
        this.title = title;
    }

    /**
     * Get the BoundingBoxes associated with this layer.
     * <p>
     * If you modify the contents of this List please call clearCache() so that the
     * getBoundingBoxes() method can return the correct combination of this list and the parent
     * bounding boxes.
     */
    public List<CRSEnvelope> getLayerBoundingBoxes() {
        return boundingBoxes;
    }

    /**
     * Returns every BoundingBox associated with this layer. The <code>HashMap</code> returned has
     * each bounding box's SRS Name (usually an EPSG code) value as the key, and the value is the
     * <code>BoundingBox</code> object itself.
     * 
     * Implements inheritance: if this layer's bounding box is null, query ancestors until the first
     * bounding box is found or no more ancestors
     * 
     * @return a HashMap of all of this layer's bounding boxes or null if no bounding boxes found
     */
    public synchronized Map<String, CRSEnvelope> getBoundingBoxes() {
        if (allBoundingBoxesCache == null) {
            allBoundingBoxesCache = new HashMap<String, CRSEnvelope>();
            
            Layer parent = this.getParent();
            while (parent != null) {
                for( CRSEnvelope bbox : getLayerBoundingBoxes() ){
                    allBoundingBoxesCache.put( bbox.getSRSName(), bbox );
                }
                parent = parent.getParent();
            }
        }
        // May return empty. But that is OK since spec says 0 or more may be specified
        return allBoundingBoxesCache;
    }

    public void setBoundingBoxes(CRSEnvelope boundingBox) {
        this.boundingBoxes.clear();
        this.boundingBoxes.add( boundingBox );
    }

    /**
     * Sets this layer's bounding boxes. The HashMap must have each BoundingBox's CRS/SRS value as
     * its key, and the <code>BoundingBox</code> object as its value.
     * 
     * @param boundingBoxes
     *            a HashMap containing bounding boxes
     */
    public void setBoundingBoxes(Map<String, CRSEnvelope> boundingBoxes) {
        this.boundingBoxes.clear();
        this.boundingBoxes.addAll(boundingBoxes.values());
    }

    /**
     * Direct access to the dimensions contributed by this Layer.
     * For the complete list of Dimensions applicable to the layer
     * this value must be combined with any Dimensions supplied by
     * the parent - this work is done for you using the getDimensions() method.
     * @see getDimensions()
     * @return List of Dimensions contributed by this Layer definition
     */
    public List<Dimension> getLayerDimensions(){
        return dimensions;
    }
    
    /**
     * The dimensions valid for this layer.
     * Includes both getLauerDimensions() and all Dimensions contributed
     * by parent layers. The result is an unmodifiable map indexed by Dimension
     * name.
     * @return Map of valid dimensions for this layer indexed by Dimension name.
     */
    public synchronized Map<String, Dimension> getDimensions() {
        if( allDimensionsCache == null ){
            Layer layer = this;
            allDimensionsCache = new HashMap<String, Dimension>();
            while (layer != null) {
                for( Dimension dimension : layer.getLayerDimensions() ){
                    allDimensionsCache.put(dimension.getName(), dimension );
                }
                layer = layer.getParent();
            }
        }
        return  Collections.unmodifiableMap( allDimensionsCache );
    }

    public void setDimensions(Map<String, Dimension> dimensionMap) {
        dimensions.clear();
        if( dimensionMap != null ){
            dimensions.addAll( dimensionMap.values() );
        }
        clearCache();
    }
    public void setDimensions(Collection<Dimension> dimensionList) {
        dimensions.clear();
        if( dimensionList != null ){
            dimensions.addAll( dimensionList );            
        }
        clearCache();
    }
    public void setDimensions( Dimension dimension) {
        dimensions.clear();
        if( dimension != null ){
            dimensions.add( dimension );
        }
        clearCache();
    }
    /** Look up a Dimension; note this looks up any parent supplied definitions as well */
    public Dimension getDimension(String name) {
        return getDimensions().get(name);
    }
    
    /**
     * The Extents contributed by this Layer.
     * <p>
     * Please note that for the complete list of Extents valid for this layer
     * you should use the getExtents() mehtod which will consider extents defined
     * as part of a Dimension and all those contributed by Parent layers.
     * <p>
     * This is an accessor; if you modify the provided list please call clearCache().
     * </p>
     * @return Extents directly defined by this layer
     */
    public List<Extent> getLayerExtents(){
        return extents;
    }
    /**
     * The Extents valid for this layer; this includes both extents defined by this layer
     * and all extents contributed by parent layers.
     * <p>
     * In keeping with the WMS 1.3.0 specification some extents may be defined as part of
     * a Dimension definition.
     * @return All extents valid for this layer.
     */
    public synchronized Map<String, Extent> getExtents() {
        if( allExtentsCache == null ){
            Layer layer = this;
            allExtentsCache = new HashMap<String, Extent>();
            while (layer != null) {
                for( Extent extent : layer.getLayerExtents() ){
                    allExtentsCache.put(extent.getName(), extent );
                }
                for( Dimension dimension : layer.getLayerDimensions() ){
                    Extent extent = dimension.getExtent(); // only for WMS 1.3.0
                    if( extent == null || extent.isEmpty() ){
                        continue;
                    }
                    allExtentsCache.put(extent.getName(), extent );                    
                }
                layer = layer.getParent();
            }
        }
        return Collections.unmodifiableMap( allExtentsCache );
    }

    /**
     * Look up an extent by name; search includes all parent extent definitions.
     * @param name
     * @return Extent or null if not found
     */
    public Extent getExtent(String name) {
        return getExtents().get(name);
    }

    public void setExtents(Map<String, Extent> extentMap) {
        extents.clear();
        if( extentMap != null ){
            extents.addAll( extentMap.values() );
        }
        clearCache();
    }

    public void setExtents(Collection<Extent> extentList) {
        extents.clear();
        if( extentList != null ){
            extents.addAll( extentList );
        }
        clearCache();
    }
    public void setExtents(Extent extent) {
        extents.clear();
        if( extent != null ){
            extents.add( extent );
        }
        clearCache();
    }
    
    /**
     * Gets the name of the <code>Layer</code>. It is designed to be machine readable, and if it is
     * present, this layer is determined to be drawable and is a valid candidate for use in a GetMap
     * or GetFeatureInfo request.
     * 
     * @return the machine-readable name of the layer
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this layer. Giving the layer name indicates that it can be drawn during a
     * GetMap or GetFeatureInfo request.
     * 
     * @param name
     *            the layer's new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Accumulates all of the srs/crs specified for this layer and all srs/crs inherited from its
     * ancestors. No duplicates are returned.
     * 
     * @return Set of all srs/crs for this layer and its ancestors
     */
    public Set<String> getSrs() {
        synchronized (this) {
            if (allSRSCache == null) {
                allSRSCache = new HashSet<String>(srs);
                // Get my ancestor's srs/crs
                Layer parent = this.getParent();
                if (parent != null) {
                    Set<String> parentSrs = parent.getSrs();
                    if (parentSrs != null) // got something, add to accumulation
                        allSRSCache.addAll(parentSrs);
                }
            }
            // May return an empty list, but spec says at least one must be specified. Perhaps, need
            // to check and throw exception if set is empty. I'm leaving that out for now since
            // it changes the method signature and would potentially break existing users of this
            // class
            return allSRSCache;
        }

    }

    public void setSrs(Set<String> srs) {
        this.srs = srs;
    }

    /**
     * Accumulates all of the styles specified for this layer and all styles inherited from its
     * ancestors. No duplicates are returned.
     * 
     * The List that is returned is of type List<org.opengis.layer.Style>. Before 2.2-RC0 it was of
     * type List<java.lang.String>.
     * 
     * @return List of all styles for this layer and its ancestors
     */
    public List<StyleImpl> getStyles() {
        ArrayList<StyleImpl> allStyles = new ArrayList<StyleImpl>();
        // Get my ancestor's styles
        Layer parent = this.getParent();
        if (parent != null) {
            List<StyleImpl> parentStyles = parent.getStyles();
            if (parentStyles != null) // got something, add to accumulation
                allStyles.addAll(parentStyles);
        }
        // Now add my styles, if any
        // Brute force check for duplicates. The spec says duplicates are not allowed:
        // (para 7.1.4.5.4) "A child shall not redefine a Style with the same Name as one
        // inherited from a parent. A child may define a new Style with a new Name that is
        // not available for the parent Layer."
        if ((styles != null) && !styles.isEmpty()) {
            for (Iterator<StyleImpl> iter = styles.iterator(); iter.hasNext();) {
                StyleImpl style = iter.next();
                if (!allStyles.contains(style))
                    allStyles.add(style);
            }
        }

        // May return an empty list, but that is OK since spec says 0 or more styles may be
        // specified
        return allStyles;
    }

    public void setStyles(List<StyleImpl> styles) {
        this.styles = styles;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Determines if this layer is queryable. Implements inheritance: if this layer's Queryable
     * attribute is null, check ancestors until the first Queryable attribute is found or no more
     * ancestors. If a Queryable attribute is not found for this layer, it will return the default
     * value of false.
     * 
     * @return true is this layer is Queryable
     */
    public boolean isQueryable() {
        if (queryable == null) {
            Layer parent = this.getParent();
            while (parent != null) {
                Boolean q = parent.getQueryable();
                if (q != null)
                    return q.booleanValue();
                else
                    parent = parent.getParent();
            }
            // At this point a attribute was not found so return default
            return false;
        }
        return queryable.booleanValue();
    }

    private Boolean getQueryable() {
        return queryable;
    }

    public void setQueryable(boolean queryable) {
        this.queryable = new Boolean(queryable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Layer layer) {
        if ((this.getName() != null) && (layer.getName() != null)) {
            return this.getName().compareTo(layer.getName());
        }

        return this.getTitle().compareTo(layer.getTitle());
    }

    /**
     * DOCUMENT ME!
     * 
     * @return Returns the parent.
     */
    public Layer getParent() {
        return parent;
    }

    /**
     * Set the parent; child will be added to the parents list of children (if it is not already).
     * 
     * @param parent
     *            The parent to set.
     */
    public void setParent(Layer parentLayer) {
        this.parent = parentLayer;
        if( !parentLayer.children.contains( this )){
            parentLayer.children.add( this );
        }
    }

    /**
     * Returns the LatLonBoundingBox for this layer. Implements inheritance: if this layer's
     * bounding box is null, query ancestors until the first bounding box is found or no more
     * ancestors.
     * 
     * @return the LatLonBoundingBox for this layer or null if no lat/lon bounding box is found
     */
    public CRSEnvelope getLatLonBoundingBox() {
        if (latLonBoundingBox == null) {
            Layer parent = this.getParent();
            while (parent != null) {
                CRSEnvelope llbb = parent.getLatLonBoundingBox();
                if (llbb != null)
                    return llbb;
                else
                    parent = parent.getParent();
            }
            // We should never get to falling out of the while loop w/o a LatLonBoundingBox
            // being found. The WMS spec says one is required. So perhaps if we don't find one,
            // then throw an exception. I'm leaving that out for now since it changes the method
            // signature
            // and would potentially break existing users of this class
        }
        // May return null!
        return latLonBoundingBox;
    }

    public void setLatLonBoundingBox(CRSEnvelope latLonBoundingBox) {
        this.latLonBoundingBox = latLonBoundingBox;
    }

    /**
     * List of children.
     * @return list of children
     */
    public List<Layer> getLayerChildren(){
        return new AbstractList<Layer>(){
            @Override
            public Layer get(int index) {
                return children.get(index);
            }
            @Override
            public int size() {
                return children.size();
            }
            @Override
            public Layer set(int index, Layer element) {
                Layer replaced = children.set(index, element );
                replaced.parent = null;
                element.parent = Layer.this;
                return replaced;
            }
            @Override
            public void add(int index, Layer element) {
                children.add( index, element );
                element.parent = Layer.this;
            }
            @Override
            public Layer remove(int index) {
                Layer removed = children.remove(index);
                if( removed != null ){
                    removed.parent = null;
                }
                return removed;
            }
        };
    }
    public Layer[] getChildren() {
        return children.toArray(new Layer[ children.size()]);
    }

    public void setChildren(Layer[] childrenArray) {
       children.clear();
       for( Layer child : childrenArray ){
           if( child == null || children.contains( child )) {
               continue; // skip
           }
           child.parent = this;
           this.children.add( child );
       }
    }
    public void addChildren( Layer child ){
        child.parent = this;
        children.add( child );
    }

    /**
     * The abstract contains human-readable information about this layer
     * 
     * @return Returns the _abstract.
     */
    public String get_abstract() {
        return _abstract;
    }

    /**
     * @param _abstract
     *            The _abstract to set.
     */
    public void set_abstract(String _abstract) {
        this._abstract = _abstract;
    }

    /**
     * Keywords are Strings to be used in searches
     * 
     * @return Returns the keywords.
     */
    public String[] getKeywords() {
        return keywords;
    }

    /**
     * @param keywords
     *            The keywords to set.
     */
    public void setKeywords(String[] keywords) {
        this.keywords = keywords;
    }

    /**
     * Max scale denominator for which it is appropriate to draw this layer.
     * <p>
     * Scale denominator is calculated based on the bounding box of the central pixel in a request
     * (ie not a scale based on real world size of the entire layer).
     * 
     * @param Max
     *            scale denominator for which it is approprirate to draw this layer
     */
    public void setScaleDenominatorMax(double scaleDenominatorMax) {
        this.scaleDenominatorMax = scaleDenominatorMax;
    }

    /**
     * Max scale denominator for which it is appropriate to draw this layer.
     * <p>
     * Scale denominator is calculated based on the bounding box of the central pixel in a request
     * (ie not a scale based on real world size of the entire layer).
     * <p>
     * Some web map servers will refuse to render images at a scale greater than the value provided
     * here.
     * <p>
     * return Max scale denominator for which it is appropriate to draw this layer.
     */
    public double getScaleDenominatorMax() {
        return scaleDenominatorMax;
    }

    /**
     * Min scale denominator for which it is appropriate to draw this layer.
     * <p>
     * Scale denominator is calculated based on the bounding box of the central pixel in a request
     * (ie not a scale based on real world size of the entire layer).
     * 
     * @param Min
     *            scale denominator for which it is appropriate to draw this layer
     */
    public void setScaleDenominatorMin(double scaleDenominatorMin) {
        this.scaleDenominatorMin = scaleDenominatorMin;
    }

    /**
     * Min scale denominator for which it is appropriate to draw this layer.
     * <p>
     * Scale denominator is calculated based on the bounding box of the central pixel in a request
     * (ie not a scale based on real world size of the entire layer).
     * <p>
     * Some web map servers will refuse to render images at a scale less than the value provided
     * here.
     * <p>
     * return Min scale denominator for which it is appropriate to draw this layer
     */
    public double getScaleDenominatorMin() {
        return scaleDenominatorMin;
    }

    /**
     * Maximum scale for which this layer is considered good.
     * <p>
     * We assume this calculation is done in a similar manner to getScaleDenominatorMax(); but a
     * look at common web services such as JPL show this not to be the case.
     * <p>
     * 
     * @return The second scale hint value (understood to mean the max value)
     * @deprecated Use getScaleDenomiatorMax() as there is less confusion over meaning
     */
    public double getScaleHintMax() {
        return scaleHintMax;
    }

    /**
     * Maximum scale for which this layer is considered good.
     * <p>
     * We assume this calculation is done in a similar manner to setScaleDenominatorMax(); but a
     * look at common web services such as JPL show this not to be the case.
     * <p>
     * 
     * @param The
     *            second scale hint value (understood to mean the max value)
     * @deprecated Use setScaleDenomiatorMax() as there is less confusion over meaning
     */
    public void setScaleHintMax(double scaleHintMax) {
        this.scaleHintMax = scaleHintMax;
    }

    /**
     * Minimum scale for which this layer is considered good.
     * <p>
     * We assume this calculation is done in a similar manner to getScaleDenominatorMin(); but a
     * look at common web services such as JPL show this not to be the case.
     * <p>
     * 
     * @return The first scale hint value (understood to mean the min value)
     * @deprecated Use getScaleDenomiatorMin() as there is less confusion over meaning
     */
    public double getScaleHintMin() {
        return scaleHintMin;
    }

    /**
     * Minimum scale for which this layer is considered good.
     * <p>
     * We assume this calculation is done in a similar manner to setScaleDenominatorMin(); but a
     * look at common web services such as JPL show this not to be the case.
     * <p>
     * param The first scale hint value (understood to mean the min value)
     * 
     * @deprecated Use setScaleDenomiatorMin() as there is less confusion over meaning
     */
    public void setScaleHintMin(double scaleHintMin) {
        this.scaleHintMin = scaleHintMin;
    }

    /**
     * Look up an envelope for the provided CoordianteReferenceSystem.
     * <p>
     * Please note that the lookup is preformed based on the SRS Name of the provided
     * CRS which is assumed to be one of its identifiers.
     * </p>
     * This method returns the first envelope found; this may not be valid for
     * sparse data sets that indicate data location using multiple envelopes for
     * a provided CRS.
     * 
     * @param crs
     * @return GeneralEnvelope matching the provided crs; or null if unavaialble.
     */
    public GeneralEnvelope getEnvelope(CoordinateReferenceSystem crs) {
        if( crs == null ){
            return null;
        }
        // Check the cache!
        GeneralEnvelope found = (GeneralEnvelope) envelopeCache.get(crs);
        if (found != null){
            return found;
        }      
        Collection<Object> identifiers = new ArrayList<Object>();
        identifiers.addAll( crs.getIdentifiers() );
        if (crs == DefaultGeographicCRS.WGS84 || crs == DefaultGeographicCRS.WGS84_3D) {
            identifiers.add( "EPSG:4326" );
        }
        for (Object identifier : identifiers ) {
            String srsName = identifier.toString();
            
            CRSEnvelope tempBBox = null;
            Layer layer = this;

            // Locate an exact bounding box if we can
            Map<String, CRSEnvelope> boxes = layer.getBoundingBoxes(); // extents for layer and parents
            tempBBox = (CRSEnvelope) boxes.get(srsName);

            // Otherwise, locate a LatLon bounding box ... if applicable
            if (tempBBox == null && ("EPSG:4326".equals(srsName.toUpperCase()))) {
                CRSEnvelope latLonBBox = null;

                layer = this;
                while (latLonBBox == null && layer != null) {
                    latLonBBox = layer.getLatLonBoundingBox();
                    if (latLonBBox != null) {
                        try {
                            new GeneralEnvelope(new double[] { latLonBBox.getMinX(),
                                    latLonBBox.getMinY() }, new double[] { latLonBBox.getMaxX(),
                                    latLonBBox.getMaxY() });
                            break;
                        } catch (IllegalArgumentException e) {
                            // TODO LOG here
                            // log("Layer "+layer.getName()+" has invalid bbox declared: "+tempBbox.toString());
                            latLonBBox = null;
                        }
                    }
                    layer = layer.getParent();
                }

                if (latLonBBox == null) {
                    // TODO could convert another bbox to latlon?
                    tempBBox = new CRSEnvelope("EPSG:4326", -180, -90, 180, 90);
                } else {
                    tempBBox = new CRSEnvelope("EPSG:4326", latLonBBox.getMinX(), latLonBBox
                            .getMinY(), latLonBBox.getMaxX(), latLonBBox.getMaxY());
                }
            }

            if (tempBBox == null) {
                // Haven't found a bbox in the requested CRS. Attempt to transform another bbox

                String epsg = null;
                if (getLatLonBoundingBox() != null) {
                    CRSEnvelope latLonBBox = getLatLonBoundingBox();
                    tempBBox = new CRSEnvelope("EPSG:4326", latLonBBox.getMinX(), latLonBBox
                            .getMinY(), latLonBBox.getMaxX(), latLonBBox.getMaxY());
                    epsg = "EPSG:4326";
                }

                if (tempBBox == null && getBoundingBoxes() != null && getBoundingBoxes().size() > 0) {
                    tempBBox = (CRSEnvelope) getBoundingBoxes().values().iterator().next();
                    epsg = tempBBox.getEPSGCode();
                }

                if (tempBBox == null) {
                    continue;
                }

                GeneralEnvelope env = new GeneralEnvelope(new double[] { tempBBox.getMinX(),
                        tempBBox.getMinY() },
                        new double[] { tempBBox.getMaxX(), tempBBox.getMaxY() });

                CoordinateReferenceSystem fromCRS = null;
                try {
                    fromCRS = CRS.decode(epsg);

                    ReferencedEnvelope oldEnv = new ReferencedEnvelope(env.getMinimum(0), env
                            .getMaximum(0), env.getMinimum(1), env.getMaximum(1), fromCRS);
                    ReferencedEnvelope newEnv = oldEnv.transform(crs, true);

                    env = new GeneralEnvelope(new double[] { newEnv.getMinimum(0),
                            newEnv.getMinimum(1) }, new double[] { newEnv.getMaximum(0),
                            newEnv.getMaximum(1) });
                    env.setCoordinateReferenceSystem(crs);

                    // success!!
                    envelopeCache.put(crs, env);
                    return env;

                } catch (NoSuchAuthorityCodeException e) {
                    // TODO Catch e
                } catch (FactoryException e) {
                    // TODO Catch e
                } catch (MismatchedDimensionException e) {
                    // TODO Catch e
                } catch (TransformException e) {
                    // TODO Catch e
                }
            }

            // TODO Attempt to figure out the valid area of the CRS and use that.

            if (tempBBox != null) {
                GeneralEnvelope env = new GeneralEnvelope(new double[] { tempBBox.getMinX(),
                        tempBBox.getMinY() },
                        new double[] { tempBBox.getMaxX(), tempBBox.getMaxY() });
                env.setCoordinateReferenceSystem(crs);
                return env;
            }

        }
        return null;
    }

    @Override
    public String toString() {
        if (this.title != null) {
            return title;
        }
        return name;
    }
}
