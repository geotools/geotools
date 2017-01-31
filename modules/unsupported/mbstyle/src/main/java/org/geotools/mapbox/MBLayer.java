/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
 *    
 */
package org.geotools.mapbox;

import org.json.simple.JSONObject;

public abstract class MBLayer {

    private JSONObject json;

    private String id;

    private String source;

    private String sourceLayer;

    private Boolean visibility = true;

    public MBLayer(JSONObject json) {        
        super();
        this.json = json;

        this.id = (String) json.get("id");

        if (json.containsKey("source")) {
            this.source = (String) json.get("source");
        }

        if (json.containsKey("source-layer")) {
            this.sourceLayer = (String) json.get("source-layer");
        }

        if (json.containsKey("visibility")) {
            this.visibility = (Boolean) json.get("visibility");
        }
    }

    public JSONObject getJson() {
        return json;
    }

    public void setJson(JSONObject json) {
        this.json = json;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceLayer() {
        return sourceLayer;
    }

    public void setSourceLayer(String sourceLayer) {
        this.sourceLayer = sourceLayer;
    }

    public abstract String getType();

    public Boolean getVisibility() {
        return visibility;
    }

    public void setVisibility(Boolean visibility) {
        this.visibility = visibility;
    }

}
