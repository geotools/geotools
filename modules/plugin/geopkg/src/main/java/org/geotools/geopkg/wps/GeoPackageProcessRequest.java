/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geopkg.wps;

import java.awt.Color;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.xml.namespace.QName;
import org.geotools.geopkg.TileMatrix;
import org.locationtech.jts.geom.Envelope;
import org.opengis.filter.Filter;

/**
 * GeoPackage Process Request. Object representation of the XML submitted to the GeoPackage process.
 *
 * @author Niels Charlier
 */
public class GeoPackageProcessRequest {

    protected String name;

    public enum LayerType {
        FEATURES,
        TILES
    };

    public abstract static class Layer {

        protected String name = null;
        protected String identifier = null;
        protected String description = null;
        protected URI srs = null;
        protected Envelope bbox = null;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public URI getSrs() {
            return srs;
        }

        public void setSrs(URI srs) {
            this.srs = srs;
        }

        public Envelope getBbox() {
            return bbox;
        }

        public void setBbox(Envelope bbox) {
            this.bbox = bbox;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public abstract LayerType getType();
    }

    public static class FeaturesLayer extends Layer {

        protected QName featureType = null;
        protected Set<QName> propertyNames = null;
        protected Filter filter = null;
        protected boolean indexed = false;

        @Override
        public LayerType getType() {
            return LayerType.FEATURES;
        }

        public QName getFeatureType() {
            return featureType;
        }

        public void setFeatureType(QName featureType) {
            this.featureType = featureType;
        }

        public Set<QName> getPropertyNames() {
            return propertyNames;
        }

        public void setPropertyNames(Set<QName> propertyNames) {
            this.propertyNames = propertyNames;
        }

        public Filter getFilter() {
            return filter;
        }

        public void setFilter(Filter filter) {
            this.filter = filter;
        }

        public boolean isIndexed() {
            return indexed;
        }

        public void setIndexed(boolean indexed) {
            this.indexed = indexed;
        }
    }

    public static class TilesLayer extends Layer {

        public static class TilesCoverage {
            protected Integer minZoom = null;
            protected Integer maxZoom = null;
            protected Integer minColumn = null;
            protected Integer maxColumn = null;
            protected Integer minRow = null;
            protected Integer maxRow = null;

            public Integer getMinZoom() {
                return minZoom;
            }

            public void setMinZoom(Integer minZoom) {
                this.minZoom = minZoom;
            }

            public Integer getMaxZoom() {
                return maxZoom;
            }

            public void setMaxZoom(Integer maxZoom) {
                this.maxZoom = maxZoom;
            }

            public Integer getMinColumn() {
                return minColumn;
            }

            public void setMinColumn(Integer minColumn) {
                this.minColumn = minColumn;
            }

            public Integer getMaxColumn() {
                return maxColumn;
            }

            public void setMaxColumn(Integer maxColumn) {
                this.maxColumn = maxColumn;
            }

            public Integer getMinRow() {
                return minRow;
            }

            public void setMinRow(Integer minRow) {
                this.minRow = minRow;
            }

            public Integer getMaxRow() {
                return maxRow;
            }

            public void setMaxRow(Integer maxRow) {
                this.maxRow = maxRow;
            }
        }

        protected List<QName> layers = null;
        protected String format = null;
        protected Color bgColor = null;
        protected boolean transparent = false;
        protected List<String> styles = null;
        protected URI sld = null;
        protected String sldBody = null;
        protected String gridSetName = null;
        protected List<TileMatrix> grids = null;
        protected TilesCoverage coverage = null;

        @Override
        public LayerType getType() {
            return LayerType.TILES;
        }

        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }

        public Color getBgColor() {
            return bgColor;
        }

        public void setBgColor(Color bgColor) {
            this.bgColor = bgColor;
        }

        public boolean isTransparent() {
            return transparent;
        }

        public void setTransparent(boolean transparent) {
            this.transparent = transparent;
        }

        public List<String> getStyles() {
            return styles;
        }

        public void setStyles(List<String> styles) {
            this.styles = styles;
        }

        public URI getSld() {
            return sld;
        }

        public void setSld(URI sld) {
            this.sld = sld;
        }

        public String getSldBody() {
            return sldBody;
        }

        public void setSldBody(String sldBody) {
            this.sldBody = sldBody;
        }

        public String getGridSetName() {
            return gridSetName;
        }

        public void setGridSetName(String gridSetName) {
            this.gridSetName = gridSetName;
        }

        public List<TileMatrix> getGrids() {
            return grids;
        }

        public void setGrids(List<TileMatrix> grids) {
            this.grids = grids;
        }

        public TilesCoverage getCoverage() {
            return coverage;
        }

        public void setCoverage(TilesCoverage coverage) {
            this.coverage = coverage;
        }

        public List<QName> getLayers() {
            return layers;
        }

        public void setLayers(List<QName> layers) {
            this.layers = layers;
        }
    }

    protected List<Layer> layers = new ArrayList<Layer>();
    protected URL path = null;
    protected boolean remove = true;

    public Boolean getRemove() {
        return remove;
    }

    public void setRemove(Boolean remove) {
        this.remove = remove;
    }

    public void addLayer(Layer layer) {
        layers.add(layer);
    }

    public Layer getLayer(int i) {
        return layers.get(i);
    }

    public int getLayerCount() {
        return layers.size();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public URL getPath() {
        return path;
    }

    public void setPath(URL path) {
        this.path = path;
    }
}
