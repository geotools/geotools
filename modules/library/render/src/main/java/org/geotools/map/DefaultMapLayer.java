/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.util.Collection;

import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.data.FeatureSource;
import org.geotools.data.memory.CollectionSource;
import org.geotools.factory.FactoryRegistryException;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.styling.Style;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.referencing.operation.TransformException;

/**
 * Default Implementation of MapLayer.
 * 
 * @author wolf
 * @source $URL:
 *         http://svn.osgeo.org/geotools/trunk/modules/library/render/src/main/java/org/geotools
 *         /map/DefaultMapLayer.java $
 *         
 *@deprecated Use MapLayer for user interface work; or use Layer directly for rendering
 */
public class DefaultMapLayer extends MapLayer {
    
    /**
     * Wrap up a layer in a DefaultMapLayer; generally used to
     * ensure layer is shown to the world as a feature source
     * even if it happens to be a WMS or something.
     */
    public DefaultMapLayer( Layer layer ){
        super( layer );
    }
    
    /**
     * Creates a new instance of DefaultMapLayer
     * 
     * @param featureSource
     *            the data source for this layer
     * @param style
     *            the style used to represent this layer
     * @param title
     *            the layer title
     * 
     * @throws NullPointerException
     *             DOCUMENT ME!
     */
    @SuppressWarnings("unchecked")
    public DefaultMapLayer(FeatureSource featureSource, Style style, String title) {
        super( featureSource, style, title );
    }

    public DefaultMapLayer(CollectionSource source, Style style, String title) {
        super( source, style, title );
    }

    /**
     * Creates a new instance of DefaultMapLayer
     * 
     * @param featureSource
     *            the data source for this layer
     * @param style
     *            the style used to represent this layer
     */
    @SuppressWarnings("unchecked")
    public DefaultMapLayer(FeatureSource featureSource, Style style) {
        super( featureSource, style );
    }

    /**
     * Creates a new instance of DefaultMapLayer using a non-emtpy feature collection as a parameter
     * 
     * @param collection
     *            the source feature collection
     * @param style
     *            the style used to represent this layer
     * @param title
     *            Title of map layer
     */
    @SuppressWarnings("unchecked")
    public DefaultMapLayer(FeatureCollection collection, Style style, String title) {
        super( collection, style, title );
    }

    @SuppressWarnings("unchecked")
    public DefaultMapLayer(Collection collection, Style style, String title) {
        super( collection, style, title );
    }

    /**
     * Creates a new instance of DefaultMapLayer using a non-emtpy feature collection as a parameter
     * 
     * @param collection
     *            the source feature collection
     * @param style
     *            the style used to represent this layer
     */
    @SuppressWarnings("unchecked")
    public DefaultMapLayer(FeatureCollection collection, Style style) {
        super( collection, style );
    }

    @SuppressWarnings("unchecked")
    public DefaultMapLayer(Collection collection, Style style) {
        super( collection, style );
    }

    /**
     * * Add a new layer and trigger a {@link LayerListEvent}.
     * 
     * @param coverage
     *            The new layer that has been added.
     * @param style
     * @throws SchemaException
     * @throws FactoryRegistryException
     * @throws TransformException
     */
    public DefaultMapLayer(GridCoverage coverage, Style style) throws TransformException,
            FactoryRegistryException, SchemaException {
        super( coverage, style );
    }

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
    public DefaultMapLayer(AbstractGridCoverage2DReader reader, Style style, String title,
            GeneralParameterValue[] params) throws TransformException, FactoryRegistryException,
            SchemaException {
        super( reader, style, title, params );
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
    public DefaultMapLayer(AbstractGridCoverage2DReader reader, Style style, String title) {
        super( reader, style, title );
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
    public DefaultMapLayer(AbstractGridCoverage2DReader reader, Style style) {
        super( reader, style );
    }

    /**
     * * Add a new layer and trigger a {@link LayerListEvent}.
     * 
     * @param coverage
     *            The new layer that has been added.
     * @param style
     * @param title
     * @throws SchemaException
     * @throws FactoryRegistryException
     * @throws TransformException
     */
    public DefaultMapLayer(GridCoverage coverage, Style style, String title)
            throws TransformException, FactoryRegistryException, SchemaException {
        super( coverage, style, title );
    }
   
}
