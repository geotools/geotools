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
package org.geotools.geopkg.wps.xml;

import java.awt.Color;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import org.geotools.geopkg.TileMatrix;
import org.geotools.geopkg.wps.GeoPackageProcessRequest;
import org.geotools.geopkg.wps.GeoPackageProcessRequest.Layer;
import org.geotools.xs.bindings.XSQNameBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

/**
 * Binding object for the type http://www.opengis.net/gpkg:geopkgtype_tiles.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;xs:complexType name="geopkgtype_tiles" xmlns:xs="http://www.w3.org/2001/XMLSchema"&gt;
 *            &lt;xs:complexContent&gt;
 *              &lt;xs:extension base="layertype"&gt;
 *                &lt;xs:sequence&gt;
 *                  &lt;xs:element name="layers" type="xs:string"/&gt;
 *                  &lt;xs:choice&gt;
 *                    &lt;xs:element name="styles" type="xs:string"/&gt;
 *                    &lt;xs:element name="sld" type="xs:string"/&gt;
 *                    &lt;xs:element name="sldbody" type="xs:string"/&gt;
 *                  &lt;/xs:choice&gt;
 *                  &lt;xs:element name="format" type="xs:string"/&gt;
 *                  &lt;xs:element name="bgcolor" type="xs:string"/&gt;
 *                  &lt;xs:element name="transparent" type="xs:boolean"/&gt;
 *                  &lt;xs:element name="gridset" type="gridsettype"/&gt;
 *                  &lt;xs:element name="coverage" type="coveragetype"/&gt;
 *                &lt;/xs:sequence&gt;
 *              &lt;/xs:extension&gt;
 *            &lt;/xs:complexContent&gt;
 *          &lt;/xs:complexType&gt;
 *
 *   </code>
 *  </pre>
 *
 * @generated
 */
public class Geopkgtype_tilesBinding extends LayertypeBinding {

    NamespaceContext namespaceContext;

    public Geopkgtype_tilesBinding(NamespaceContext namespaceContext) {
        this.namespaceContext = namespaceContext;
    }

    /** @generated */
    public QName getTarget() {
        return GPKG.geopkgtype_tiles;
    }

    @Override
    public Layer parseLayer(ElementInstance instance, Node node, Object value) throws Exception {
        XSQNameBinding nameBinding = new XSQNameBinding(namespaceContext);

        GeoPackageProcessRequest.TilesLayer layer = new GeoPackageProcessRequest.TilesLayer();
        List<QName> layers = new ArrayList<QName>();
        for (String layerName : Arrays.asList(((String) node.getChildValue("layers")).split(","))) {
            layers.add((QName) nameBinding.parse(null, layerName.trim()));
        }
        layer.setLayers(layers);
        String styleNames = (String) node.getChildValue("styles");
        if (styleNames != null) {
            List<String> styles = new ArrayList<String>();
            for (String styleName : Arrays.asList(styleNames.split(","))) {
                styles.add(styleName.trim());
            }
            layer.setStyles(styles);
        }
        layer.setSld((URI) node.getChildValue("sld"));
        layer.setSldBody((String) node.getChildValue("sldBody"));
        layer.setFormat((String) node.getChildValue("format"));
        String bgColor = (String) node.getChildValue("bgcolor");
        if (bgColor != null) {
            layer.setBgColor(Color.decode("#" + bgColor));
        }
        Boolean transparent = (Boolean) node.getChildValue("transparent");
        if (transparent != null) {
            layer.setTransparent(transparent);
        }
        layer.setCoverage(
                (GeoPackageProcessRequest.TilesLayer.TilesCoverage) node.getChildValue("coverage"));
        Object gridSet = node.getChildValue("gridset");
        if (gridSet instanceof String) {
            layer.setGridSetName((String) gridSet);
        } else if (gridSet instanceof List<?>) {
            layer.setGrids((List<TileMatrix>) gridSet);
        }
        return layer;
    }
}
