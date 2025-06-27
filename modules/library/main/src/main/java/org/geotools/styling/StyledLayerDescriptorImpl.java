/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.styling;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import org.geotools.api.style.Style;
import org.geotools.api.style.StyleVisitor;
import org.geotools.api.style.StyledLayer;
import org.geotools.api.style.StyledLayerDescriptor;
import org.geotools.api.style.UserLayer;
import org.geotools.util.Utilities;

/**
 * Holds styling information (from a StyleLayerDescriptor document).
 *
 * <p>This class is based on version 1.0 of the SLD specification.
 *
 * <p>For many of us in geotools this is the reason we came along for the ride - a pretty picture. For documentation on
 * the use of this class please consult the SLD 1.0 specification.
 *
 * <p>We may experiment with our own (or SLD 1.1) ideas but will mark such experiments for you. This is only an issue of
 * you are considering writing out these objects for interoptability with other systems.
 *
 * <p>General strategy for supporting multiple SLD versions (and experiments):
 *
 * <ul>
 *   <li>These classes will be <b>BIGGER</b> and more capabile then any one specification
 *   <li>We can define (and support) explicit interfaces tracking each version (preferably GeoAPI would hold these)
 *   <li>We can use Factories (aka SLD1Factory and SLD1_1Factory and SEFactory) to support the creation of conformant
 *       datastructures. Code (such as user interfaces) can be parameratized with these factories when they need to
 *       confirm to an exact version supported by an individual service. We hope that specifications are always
 *       adaptive, and will be forced to throw unsupported exceptions when functionality is removed from a
 *       specification.
 * </ul>
 */
public class StyledLayerDescriptorImpl implements StyledLayerDescriptor {
    /** The logger for the default core module. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(StyledLayerDescriptorImpl.class);

    /** Holds value of property name. */
    private String name;

    /** Holds value of property title. */
    private String title;

    /** Holds value of property abstract. */
    private String abstractStr;

    private List<StyledLayer> layers = new ArrayList<>();

    /**
     * Convenience method for grabbing the default style from the StyledLayerDescriptor.
     *
     * @return first Style (in SLD-->UserLayers-->UserStyles) that claims to be the default
     */
    public Style getDefaultStyle() {
        // descend into the layers
        for (StyledLayer layer : layers) {
            if (layer instanceof UserLayer) {
                UserLayer userLayer = (UserLayer) layer;

                // descend into the styles
                Style[] styles = userLayer.getUserStyles();

                for (Style style : styles) {
                    // return the first style that claims to be the default
                    if (style.isDefault()) {
                        return style;
                    }
                }
            }
        }

        return null;
    }

    @Override
    public StyledLayer[] getStyledLayers() {
        return layers.toArray(new StyledLayerImpl[layers.size()]);
    }

    @Override
    public void setStyledLayers(StyledLayer... layers) {
        this.layers.clear();

        for (StyledLayer layer : layers) {
            addStyledLayer(layer);
        }

        LOGGER.fine("StyleLayerDescriptorImpl added " + this.layers.size() + " styled layers");
    }

    @Override
    public List<StyledLayer> layers() {
        return layers;
    }

    @Override
    public void addStyledLayer(StyledLayer layer) {
        layers.add(layer);
    }

    /**
     * Getter for property name.
     *
     * @return Value of property name.
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Setter for property name.
     *
     * @param name New value of property name.
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for property title.
     *
     * @return Value of property title.
     */
    @Override
    public String getTitle() {
        return this.title;
    }

    /**
     * Setter for property title.
     *
     * @param title New value of property title.
     */
    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for property abstractStr.
     *
     * @return Value of property abstractStr.
     */
    @Override
    public java.lang.String getAbstract() {
        return abstractStr;
    }

    /**
     * Setter for property abstractStr.
     *
     * @param abstractStr New value of property abstractStr.
     */
    @Override
    public void setAbstract(java.lang.String abstractStr) {
        this.abstractStr = abstractStr;
    }

    @Override
    public void accept(StyleVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean equals(Object oth) {
        if (this == oth) {
            return true;
        }

        if (oth instanceof StyledLayerDescriptorImpl) {
            StyledLayerDescriptorImpl other = (StyledLayerDescriptorImpl) oth;

            return Utilities.equals(abstractStr, other.abstractStr)
                    && Utilities.equals(layers, other.layers)
                    && Utilities.equals(name, other.name)
                    && Utilities.equals(title, other.title);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, title, abstractStr, layers);
    }
}
