/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016 Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014-2016 Boundless Spatial
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
package org.geotools.ysld.encode;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import org.geotools.api.style.NamedLayer;
import org.geotools.api.style.RemoteOWS;
import org.geotools.api.style.Style;
import org.geotools.api.style.StyledLayer;
import org.geotools.api.style.StyledLayerDescriptor;
import org.geotools.api.style.UserLayer;
import org.geotools.styling.SLD;

/**
 * Encodes a {@link StyledLayerDescriptor} as YSLD. Handles top-level elements such a name and title, and delegates to
 * {@link FeatureStyleEncoder} for the content.
 *
 * <p>YSLD focuses on SymbologyEncoding specification; encoding the default (or first) style found, and treating
 * surrounding StyleLayerDescriptor / NamedLayer as a wrapper.
 */
public class RootEncoder extends YsldEncodeHandler<StyledLayerDescriptor> {

    RootEncoder(StyledLayerDescriptor sld) {
        super(Collections.singleton(sld).iterator());
    }

    /**
     * Encode sld as part of root-level information prefixed with {@code sld}.
     *
     * <p>This method looks and encodes the {@link SLD#defaultStyle(StyledLayerDescriptor)} along with it's parent
     * layer.
     *
     * @param sld
     */
    @Override
    protected void encode(StyledLayerDescriptor sld) {
        StyledLayer[] layers = sld.getStyledLayers();

        put("sld-name", sld.getName());
        put("sld-title", sld.getTitle());
        put("sld-abstract", sld.getAbstract());

        Style style = SLD.defaultStyle(sld);

        StyledLayer layer = findParentLayer(sld, style);
        encode(layer);
        encode(style);
    }

    /**
     * Encode user layer information as part of root-level information prefixed with {@code user}.
     *
     * @param layer User layer, or {@code null} if not available.
     */
    protected void encode(UserLayer layer) {
        if (layer == null) return;
        put("user-name", layer.getName());
        if (layer.getRemoteOWS() != null) {
            RemoteOWS remote = layer.getRemoteOWS();
            put("user-service", remote.getService());
            put("user-remote", remote.getOnlineResource());
        }
    }

    /**
     * Encode named layer information as part of root-level with prefix {@code layer}.
     *
     * @param layer Named layer, or null if not available.
     */
    protected void encode(NamedLayer layer) {
        if (layer == null) return;
        put("layer-name", layer.getName());
    }
    /**
     * Look up layer (example UserLayer) containing the provided style.
     *
     * @param sld
     * @param style
     * @return layer containing the provided style, or {@code null} if not found
     */
    private StyledLayer findParentLayer(StyledLayerDescriptor sld, Style style) {
        if (style == null) return null;

        return sld.layers().stream()
                .filter(new Predicate<StyledLayer>() {
                    @Override
                    public boolean test(StyledLayer styledLayer) {
                        List<Style> styles;
                        if (styledLayer instanceof NamedLayer) styles = ((NamedLayer) styledLayer).styles();
                        else if (styledLayer instanceof UserLayer) {
                            styles = ((UserLayer) styledLayer).userStyles();
                        } else {
                            styles = Collections.emptyList();
                        }
                        return styles.contains(style);
                    }
                })
                .findFirst()
                .orElse(null);
    }

    protected void encode(StyledLayer layer) {
        if (layer instanceof UserLayer) {
            encode((UserLayer) layer);
        } else if (layer instanceof NamedLayer) {
            encode((NamedLayer) layer);
        }
    }

    protected void encode(Style style) {
        put("name", style.getName());
        put(
                "title",
                Optional.ofNullable(style.getDescription().getTitle())
                        .map(Object::toString)
                        .orElse(null));
        put(
                "abstract",
                Optional.ofNullable(style.getDescription().getAbstract())
                        .map(Object::toString)
                        .orElse(null));
        put("feature-styles", new FeatureStyleEncoder(style));
    }
}
