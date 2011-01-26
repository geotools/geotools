package org.geotools.styling.builder;

import org.geotools.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.Stroke;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyledLayerDescriptor;

public class StyledLayerDescriptorBuilder implements Builder<StyledLayerDescriptor> {
    StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);

    private boolean unset;

    public StyledLayerDescriptorBuilder() {
        reset();
    }

    public StyledLayerDescriptorBuilder unset() {
        reset();
        unset = true;
        return this;
    }

    /**
     * Reset stroke to default values.
     */
    public StyledLayerDescriptorBuilder reset() {
        unset = false;
        return this;
    }

    /**
     * Reset builder to provided original stroke.
     * 
     * @param stroke
     */
    public StyledLayerDescriptorBuilder reset(Stroke stroke) {
        unset = false;
        return this;
    }

    public StyledLayerDescriptor build() {
        if (unset) {
            return null;
        }
        StyledLayerDescriptor sld = sf.createStyledLayerDescriptor();
        return sld;
    }

    public StyledLayerDescriptorBuilder reset(StyledLayerDescriptor original) {
        return this;
    }
    
}