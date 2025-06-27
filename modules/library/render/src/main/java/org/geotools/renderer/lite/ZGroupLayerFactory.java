/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.lite;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import org.geotools.api.data.Query;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.sort.SortBy;
import org.geotools.api.style.FeatureTypeStyle;
import org.geotools.api.style.Style;
import org.geotools.api.style.StyleFactory;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.map.DirectLayer;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.renderer.style.SLDStyleFactory;

/**
 * Builds {@link ZGroupLayer} instances from a MapContent using {@link FeatureTypeStyle#SORT_BY_GROUP} options
 *
 * @author Andrea Aime - GeoSolutions
 */
class ZGroupLayerFactory {

    private static StyleFactory STYLE_FACTORY = CommonFactoryFinder.getStyleFactory();

    /**
     * Filters a MapContent and returns a new one where adjacent {@link FeatureTypeStyle} using the same
     * {@link FeatureTypeStyle#SORT_BY_GROUP} key are turned into {@link ZGroupLayer}
     */
    public static MapContent filter(MapContent mapContent) {
        // Quick check, do we have any z-group to care for? For the common
        // case of not having z-groups, we want to avoid building a new object and
        // go though the complex splitting and checking logic
        // Also check that the usage of groups is not made in conjunction with incompatible
        // functionality
        if (!hasZGroup(mapContent, false)) {
            return mapContent;
        }

        // build a new map content where the z-groups are munched toghether into ZGroupLayer
        MapContent result = new MapContent();
        result.getViewport()
                .setCoordinateReferenceSystem(mapContent.getViewport().getCoordinateReferenceSystem());
        result.getViewport().setBounds(mapContent.getViewport().getBounds());
        ZGroupLayer currentGroup = null;
        for (Layer layer : mapContent.layers()) {
            if (layer instanceof DirectLayer) {
                result.layers().add(layer);
                currentGroup = null;
            } else {
                // in case this layer is clean, eventually dump the current group, dump the
                // layer, and move on
                if (!hasZGroup(layer, true)) {
                    if (currentGroup != null) {
                        result.layers().add(currentGroup);
                        currentGroup = null;
                    }
                    result.layers().add(layer);
                } else {
                    // reorganize the layer in z-groups, and eventually attach to the current one
                    List<Layer> zGroupLayers = arrangeOnZGroups(layer, currentGroup);
                    // if the last in the list is a z-group, it's the new current, we don't
                    // want to add it into the MapContent yet, might join with the next layer
                    if (zGroupLayers.get(zGroupLayers.size() - 1) instanceof ZGroupLayer) {
                        currentGroup = (ZGroupLayer) zGroupLayers.remove(zGroupLayers.size() - 1);
                    } else {
                        currentGroup = null;
                    }
                    // dump the rest
                    for (Layer l : zGroupLayers) {
                        result.layers().add(l);
                    }
                }
            }
        }
        // dump the last group, if any
        if (currentGroup != null) {
            result.layers().add(currentGroup);
        }

        return result;
    }

    private static List<Layer> arrangeOnZGroups(Layer layer, ZGroupLayer previousGroup) {
        List<Layer> splitLayers = new ArrayList<>();
        if (previousGroup != null) {
            splitLayers.add(previousGroup);
        }
        String currentGroupId = previousGroup != null ? previousGroup.getGroupId() : null;
        List<FeatureTypeStyle> featureTypeStyles = new ArrayList<>();
        for (FeatureTypeStyle fts : layer.getStyle().featureTypeStyles()) {
            String groupName = fts.getOptions().get(org.geotools.api.style.FeatureTypeStyle.SORT_BY_GROUP);
            if (!(groupName == currentGroupId || groupName != null && groupName.equals(currentGroupId))
                    && !featureTypeStyles.isEmpty()) {
                // the group name changed, dump the current feature type styles
                addToSplitLayers(layer, previousGroup, splitLayers, currentGroupId, featureTypeStyles);
            }
            featureTypeStyles.add(fts);
            currentGroupId = groupName;
        }
        // add the residual fts, if needed
        if (featureTypeStyles != null) {
            addToSplitLayers(layer, previousGroup, splitLayers, currentGroupId, featureTypeStyles);
        }

        return splitLayers;
    }

    private static void addToSplitLayers(
            Layer layer,
            ZGroupLayer previousGroup,
            List<Layer> splitLayers,
            String groupId,
            List<FeatureTypeStyle> featureTypeStyles) {
        Style style = STYLE_FACTORY.createStyle();
        style.featureTypeStyles().addAll(featureTypeStyles);
        featureTypeStyles.clear();
        FeatureLayer singleGroupLayer = buildNewFeatureLayer(layer, style);
        if (groupId == null) {
            splitLayers.add(singleGroupLayer);
        } else if (previousGroup != null && groupId.equals(previousGroup.getGroupId())) {
            previousGroup.addLayer(singleGroupLayer);
        } else if (groupId != null) {
            ZGroupLayer newZGroup = new ZGroupLayer(groupId, singleGroupLayer);
            splitLayers.add(newZGroup);
        }
    }

    private static FeatureLayer buildNewFeatureLayer(Layer layer, Style style) {
        FeatureLayer singleGroupLayer = new FeatureLayer(layer.getFeatureSource(), style);
        SortBy[] sortBy =
                SLDStyleFactory.getSortBy(style.featureTypeStyles().get(0).getOptions());
        Query nativeQuery = layer.getQuery();
        Query query = ensureSortProperties(nativeQuery, sortBy);
        singleGroupLayer.setQuery(query);
        singleGroupLayer.setTitle(layer.getTitle());
        return singleGroupLayer;
    }

    /** Makes sure the properties needed for in-memory sorting are available by adding them into the query */
    private static Query ensureSortProperties(Query nativeQuery, SortBy[] sortBy) {
        LinkedHashSet<PropertyName> sortProperties = new LinkedHashSet<>();
        for (SortBy sb : sortBy) {
            PropertyName pn = sb.getPropertyName();
            if (pn != null) {
                sortProperties.add(pn);
            }
        }

        List<PropertyName> nativeProperties = nativeQuery.getProperties();
        Query q = new Query(nativeQuery);
        if (nativeProperties == Query.ALL_PROPERTIES) {
            q.setProperties(new ArrayList<>(sortProperties));
        } else {
            List<PropertyName> allProperties = new ArrayList<>(nativeProperties);
            for (PropertyName propertyName : sortProperties) {
                if (!allProperties.contains(propertyName)) {
                    allProperties.add(propertyName);
                }
            }
            q.setProperties(allProperties);
        }

        return q;
    }

    private static boolean hasZGroup(MapContent mapContent, boolean checkValid) {
        for (Layer layer : mapContent.layers()) {
            if (hasZGroup(layer, true)) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasZGroup(Layer layer, boolean checkValid) {
        boolean hasGroup = false;
        if (layer.getStyle() != null) {
            for (FeatureTypeStyle fts : layer.getStyle().featureTypeStyles()) {
                Map<String, String> options = fts.getOptions();
                String groupName = options.get(org.geotools.api.style.FeatureTypeStyle.SORT_BY_GROUP);
                if (groupName != null && !groupName.trim().isEmpty()) {
                    hasGroup = true;
                    if (checkValid) {
                        if (fts.getTransformation() != null) {
                            throw new IllegalArgumentException("Invalid "
                                    + org.geotools.api.style.FeatureTypeStyle.SORT_BY_GROUP
                                    + " usage in layer "
                                    + layer.getTitle()
                                    + ": cannot be mixed with rendering transformations");
                        } else if (options.get(org.geotools.api.style.FeatureTypeStyle.SORT_BY) == null) {
                            throw new IllegalArgumentException("Invalid "
                                    + org.geotools.api.style.FeatureTypeStyle.SORT_BY_GROUP
                                    + " usage in layer "
                                    + layer.getTitle()
                                    + ": the corresponding sortBy vendor option is missing");
                        }
                    }
                }
            }
        }
        if (hasGroup && !(layer instanceof FeatureLayer)) {
            throw new IllegalArgumentException("Invalid "
                    + org.geotools.api.style.FeatureTypeStyle.SORT_BY_GROUP
                    + " usage in layer "
                    + layer.getTitle()
                    + ": can only be applied to vector layers");
        }
        return hasGroup;
    }
}
