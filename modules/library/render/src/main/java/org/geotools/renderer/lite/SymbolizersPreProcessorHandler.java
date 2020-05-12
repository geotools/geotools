/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.map.Layer;
import org.geotools.renderer.SymbolizersPreProcessor;
import org.geotools.styling.Symbolizer;
import org.geotools.util.factory.GeoTools;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.PropertyName;

/** Contains methods for handling the symbolizers pre-procesors extension points logic. */
public class SymbolizersPreProcessorHandler {

    private static final FilterFactory filterFactory =
            CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());

    /**
     * The {@link SymbolizersPreProcessor} extension instances collection to be called before
     * rendering a feature. For every pre-processor, the current feature and {@link Symbolizer} list
     * will be provided. The symbolizers pre-processor is allowed to enhance or add new symbolizers
     * to the feature rendering at runtime.
     */
    private final Collection<SymbolizersPreProcessor> symbolizersPreProcessors =
            new CopyOnWriteArrayList<>();

    public SymbolizersPreProcessorHandler() {}

    public Collection<SymbolizersPreProcessor> getSymbolizersPreProcessors() {
        return symbolizersPreProcessors;
    }

    /**
     * Declares how much of an extra space is needed to catch the symbolizers it is about to add.
     */
    public double getBuffer(Layer layer) {
        return getSymbolizersPreProcessorsByLayerStream(layer)
                .map(proc -> proc.getBuffer(layer, layer.getStyle()))
                .reduce(0.0d, (result, elem) -> Math.max(result, elem));
    }

    /**
     * Adds required attributes from extensions if missing.
     *
     * @param attributes current attributes
     * @param schema the feature schema
     * @param layer the layer instance
     * @return an enhanced list of properties with the missing ones added from extensions, or the
     *     same provided list if didn't need to add attributes
     */
    public List<PropertyName> addRequiredAttributes(
            List<PropertyName> attributes, FeatureType schema, Layer layer) {
        // find and add missing attributes
        Set<String> extensionAttributes =
                getSymbolizersPreProcessorsByLayerStream(layer)
                        .flatMap(pp -> pp.getAttributes(layer).stream())
                        .collect(Collectors.toSet());
        if (extensionAttributes.isEmpty()) return attributes;
        List<PropertyName> attributesRequired = new ArrayList<>(attributes);
        List<PropertyName> missingAttributes =
                filterMissingAttributes(extensionAttributes, attributesRequired);
        attributesRequired.addAll(missingAttributes);

        return attributesRequired;
    }

    private List<PropertyName> filterMissingAttributes(
            Collection<String> attributes, List<PropertyName> attributesAlreadyIncluded) {
        List<String> included =
                attributesAlreadyIncluded
                        .stream()
                        .map(PropertyName::getPropertyName)
                        .collect(Collectors.toList());
        return attributes
                .stream()
                .filter(attr -> !included.contains(attr))
                .map(attr -> filterFactory.property(attr))
                .collect(Collectors.toList());
    }

    /**
     * Returns the {@link SymbolizersPreProcessor} instances that can be applied to the provided
     * layer.
     *
     * @param layer the layer to be used
     * @return the result collection or an empty collection if no one complies
     */
    public Iterator<SymbolizersPreProcessor> getSymbolizersPreProcessorsByLayer(Layer layer) {
        return getSymbolizersPreProcessorsByLayerStream(layer).iterator();
    }

    private Stream<SymbolizersPreProcessor> getSymbolizersPreProcessorsByLayerStream(Layer layer) {
        return this.symbolizersPreProcessors.stream().filter(pp -> pp.appliesTo(layer));
    }

    /**
     * Execute the {@link SymbolizersPreProcessor} extension points configured in this instance.
     *
     * @param feature the feature for rendering.
     * @param symbolizers the symbolizers to be enhanced.
     * @return the enhanced symbolizers list, or the original if no extensions were applied.
     */
    public List<Symbolizer> preProcessSymbolizers(
            Feature feature, Layer layer, List<Symbolizer> symbolizers) {
        Iterator<SymbolizersPreProcessor> processorsByLayer =
                getSymbolizersPreProcessorsByLayer(layer);
        while (processorsByLayer.hasNext()) {
            SymbolizersPreProcessor preProcessor = processorsByLayer.next();
            List<Symbolizer> returnedSymbolizers = preProcessor.apply(feature, layer, symbolizers);
            if (returnedSymbolizers != null) symbolizers = returnedSymbolizers;
        }
        return symbolizers;
    }
}
