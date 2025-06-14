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
 *
 * Created on November 3, 2003, 12:00 PM
 */

package org.geotools.styling;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.geotools.api.data.DataStore;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.style.FeatureTypeConstraint;
import org.geotools.api.style.RemoteOWS;
import org.geotools.api.style.Style;
import org.geotools.api.style.StyleVisitor;
import org.geotools.api.style.UserLayer;
import org.geotools.util.Utilities;

/**
 * DJB: on inlinefeature support: The inline features also lets you "sort of" make your WMS into a WFS-T.
 *
 * <p>I was going to implement this after SLD POST on monday, but I was expecting the definition in the spec to be a bit
 * "nicer". Right now its just:
 *
 * <pre>
 * {@code <element name="InlineFeature"> <complexType> <sequence> <element ref="gml:_Feature" maxOccurs="unbounded"/> </sequence> </complexType>}
 * </pre>
 *
 * <p>(the spec hasnt been finalized)
 *
 * <p>I guess if we make some assumptions about the data coming in - ie. every feature is the same type, and its simple
 * (no nesting, no <choices>, and no attributes), then we can parse ones that look like:
 *
 * <pre>
 * {@code <Feature> <Name>David Blasby</Name> <Location> ... GML ... </Location> </Feature>}
 * </pre>
 *
 * <p>I'm not the best at reading .xsd, but I think that means you can stick in ANY GML Feature. If so, its way too
 * general.
 *
 * <p>My plan was to parse the first Feature (or, the given schema if there is one) to find out all the property names
 * (and which one(s) are the geometry) and make a FeatureType. (I'd assume all the properties were strings)
 *
 * <p>Then, make a MemoryDataStore and put the features in it. I can pass this off to the lite renderer as normal.
 *
 * @author jamesm
 */
public class UserLayerImpl extends StyledLayerImpl implements UserLayer {

    /**
     * the (memory) datastore that will contain the inline features. The initial implementation has this as a
     * MemoryDataStore with one FeatureType in it. You should ensure that you dont keep references to it around so it
     * can be GCed.
     */
    private DataStore inlineFeatureDatastore = null;

    private SimpleFeatureType inlineFeatureType = null;
    RemoteOWS remoteOWS;
    List<Style> styles = new ArrayList<>();
    List<FeatureTypeConstraint> constraints = new ArrayList<>();

    @Override
    public RemoteOWS getRemoteOWS() {
        return remoteOWS;
    }

    @Override
    public DataStore getInlineFeatureDatastore() {
        return inlineFeatureDatastore;
    }

    @Override
    public SimpleFeatureType getInlineFeatureType() {
        return inlineFeatureType;
    }

    @Override
    public void setInlineFeatureDatastore(Object store) {
        inlineFeatureDatastore = (DataStore) store;
    }

    @Override
    public void setInlineFeatureType(SimpleFeatureType ft) {
        inlineFeatureType = ft;
    }

    @Override
    public void setRemoteOWS(RemoteOWS service) {
        this.remoteOWS = service;
    }

    @Override
    public List<FeatureTypeConstraint> layerFeatureConstraints() {
        return constraints;
    }

    @Override
    public FeatureTypeConstraint[] getLayerFeatureConstraints() {
        return constraints.toArray(new FeatureTypeConstraint[0]);
    }

    @Override
    public void setLayerFeatureConstraints(FeatureTypeConstraint... array) {
        this.constraints.clear();
        this.constraints.addAll(Arrays.asList(array));
    }

    @Override
    public List<Style> userStyles() {
        return styles;
    }

    @Override
    public Style[] getUserStyles() {
        return styles.toArray(new Style[0]);
    }

    @Override
    public void setUserStyles(Style... styles) {
        this.styles.clear();
        this.styles.addAll(Arrays.asList(styles));
    }

    @Override
    public void addUserStyle(Style style) {
        styles.add(style);
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

        if (oth instanceof UserLayerImpl) {
            UserLayerImpl other = (UserLayerImpl) oth;

            if (!(Utilities.equals(inlineFeatureDatastore, other.inlineFeatureDatastore)
                    && Utilities.equals(inlineFeatureType, other.inlineFeatureType)
                    && Utilities.equals(remoteOWS, other.remoteOWS)
                    && Utilities.equals(styles, other.styles))) {
                return false;
            }
            if (!Utilities.equals(constraints, other.constraints)) {
                return false;
            }
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(inlineFeatureDatastore, inlineFeatureType, remoteOWS, styles, constraints);
    }
}
