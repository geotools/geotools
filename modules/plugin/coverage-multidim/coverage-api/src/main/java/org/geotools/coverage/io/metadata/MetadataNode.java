/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.metadata;

import java.util.List;
import java.util.Map;

public class MetadataNode {

    public List<MetadataNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<MetadataNode> nodes) {
        this.nodes = nodes;
    }

    public Map<String, MetadataAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, MetadataAttribute> attributes) {
        this.attributes = attributes;
    }

    private List<MetadataNode> nodes;

    private Map<String, MetadataAttribute> attributes;
}
