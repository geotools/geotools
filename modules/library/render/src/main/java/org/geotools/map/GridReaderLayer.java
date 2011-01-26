/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.map;

import java.util.logging.Level;

import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.FactoryRegistryException;
import org.geotools.feature.SchemaException;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.event.MapLayerEvent;
import org.geotools.resources.coverage.FeatureUtilities;
import org.geotools.styling.Style;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

public class GridReaderLayer extends Layer {

    protected Style style;

    protected AbstractGridCoverage2DReader reader;

    protected GeneralParameterValue[] params;

    /**
     * Constructor which adds a new layer and trigger a {@link LayerListEvent}.
     * 
     * @param reader
     *            a reader with the new layer that will be added.
     * @param style
     * @param title
     * @param params
     *            GeneralParameterValue[] that describe how the {@link AbstractGridCoverage2DReader}
     *            shall read the images
     * 
     * @throws SchemaException
     * @throws FactoryRegistryException
     * @throws TransformException
     */
    public GridReaderLayer(AbstractGridCoverage2DReader reader, Style style, String title,
            GeneralParameterValue[] params) {
        this.reader = reader;
        this.params = params;
        this.style = style;
        setTitle(title);
    }

    @Override
    public void dispose() {
        if (reader != null) {
            try{
                reader.dispose();
            }catch (Exception e) {
                // eat me
            }
            reader = null;
        }
        if( style != null ){
            this.style = null;
        }
        if( params != null ){
            this.params = null;
        }
        super.dispose();
    }

    @Override
    public ReferencedEnvelope getBounds() {
        if (reader != null) {
            CoordinateReferenceSystem crs = reader.getCrs();
            GeneralEnvelope envelope = reader.getOriginalEnvelope();
            if (envelope != null) {
                return new ReferencedEnvelope(envelope);
            } else if (crs != null) {
                return new ReferencedEnvelope(crs);
            }
        }
        return null;
    }

    /**
     * Constructor which adds a new layer and trigger a {@link LayerListEvent}.
     * 
     * @param reader
     *            a reader with the new layer that will be added.
     * @param style
     * @param title
     * 
     * @throws SchemaException
     * @throws FactoryRegistryException
     * @throws TransformException
     */
    public GridReaderLayer(AbstractGridCoverage2DReader reader, Style style, String title) {
        this.reader = reader;
        this.style = style;
        setTitle(title);
    }

    /**
     * Constructor which adds a new layer and triggers a {@link LayerListEvent}.
     * 
     * @param reader
     *            a reader with the new layer that will be added
     * @param style
     * 
     * @throws SchemaException
     * @throws FactoryRegistryException
     * @throws TransformException
     */
    public GridReaderLayer(AbstractGridCoverage2DReader reader, Style style) {
        this.reader = reader;
        this.style = style;
    }

    /**
     * Reader used for efficient access to raster content.
     * 
     * @return
     */
    public AbstractGridCoverage2DReader getReader() {
        return reader;
    }

    /**
     * Parameter values used when reading.
     * 
     * @return parameters used when reader
     */
    public GeneralParameterValue[] getParams() {
        return params;
    }

    /**
     * Getter for property style.
     * 
     * @return Value of property style.
     */
    public Style getStyle() {
        return style;
    }

    /**
     * Setter for property style.
     * 
     * @param style
     *            New value of property style.
     * 
     * @throws NullPointerException
     *             DOCUMENT ME!
     */
    public void setStyle(Style style) {
        if (style == null) {
            throw new NullPointerException();
        }
        this.style = style;
        fireMapLayerListenerLayerChanged(MapLayerEvent.STYLE_CHANGED);
    }

    public SimpleFeatureCollection toFeatureCollection() {
        SimpleFeatureCollection collection;
        try {
            collection = FeatureUtilities.wrapGridCoverageReader(reader, params);
            return collection;
        } catch (Exception e) {
            LOGGER.log(Level.FINER, "Coverage could not be converted to FeatureCollection", e);
            return null;
        }
    }
}
