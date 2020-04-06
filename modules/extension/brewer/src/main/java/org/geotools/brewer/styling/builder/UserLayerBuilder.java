/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.brewer.styling.builder;

import java.util.ArrayList;
import java.util.List;
import org.geotools.data.DataStore;
import org.geotools.styling.FeatureTypeConstraint;
import org.geotools.styling.Style;
import org.geotools.styling.UserLayer;
import org.opengis.feature.simple.SimpleFeatureType;

public class UserLayerBuilder extends AbstractSLDBuilder<UserLayer> {

    DataStore inlineFeatureDataStore;

    SimpleFeatureType inlineFeatureType;

    RemoteOWSBuilder remoteOWS = new RemoteOWSBuilder().unset();

    List<FeatureTypeConstraintBuilder> featureTypeConstraint =
            new ArrayList<FeatureTypeConstraintBuilder>();

    List<StyleBuilder> userStyles = new ArrayList<StyleBuilder>();

    public UserLayerBuilder() {
        this(null);
    }

    public UserLayerBuilder(AbstractSLDBuilder<?> parent) {
        super(parent);
        reset();
    }

    public UserLayerBuilder inlineData(DataStore store, SimpleFeatureType sft) {
        this.unset = false;
        this.inlineFeatureDataStore = store;
        this.inlineFeatureType = sft;
        return this;
    }

    public UserLayerBuilder remoteOWS(String onlineResource, String service) {
        this.unset = false;
        remoteOWS.resource(onlineResource).service(service);
        return this;
    }

    public FeatureTypeConstraintBuilder featureTypeConstraint() {
        this.unset = false;
        FeatureTypeConstraintBuilder builder = new FeatureTypeConstraintBuilder(this);
        featureTypeConstraint.add(builder);
        return builder;
    }

    public StyleBuilder style() {
        this.unset = false;
        StyleBuilder sb = new StyleBuilder(this);
        userStyles.add(sb);
        return sb;
    }

    /** Reset stroke to default values. */
    public UserLayerBuilder reset() {
        unset = false;
        inlineFeatureDataStore = null;
        inlineFeatureType = null;
        remoteOWS.unset();
        featureTypeConstraint.clear();
        userStyles.clear();
        return this;
    }

    /** Reset builder to provided original stroke. */
    public UserLayerBuilder reset(UserLayer other) {
        if (other == null) {
            return unset();
        }

        inlineFeatureDataStore = other.getInlineFeatureDatastore();
        inlineFeatureType = other.getInlineFeatureType();
        remoteOWS.reset(other.getRemoteOWS());
        featureTypeConstraint.clear();
        for (FeatureTypeConstraint ftc : other.getLayerFeatureConstraints()) {
            featureTypeConstraint.add(new FeatureTypeConstraintBuilder(this).reset(ftc));
        }
        userStyles.clear();
        for (Style style : other.getUserStyles()) {
            userStyles.add(new StyleBuilder(this).reset(style));
        }

        unset = false;
        return this;
    }

    @Override
    public UserLayerBuilder unset() {
        return (UserLayerBuilder) super.unset();
    }

    public UserLayer build() {
        if (unset) {
            return null;
        }
        UserLayer layer = sf.createUserLayer();
        layer.setRemoteOWS(remoteOWS.build());
        layer.setInlineFeatureDatastore(inlineFeatureDataStore);
        layer.setInlineFeatureType(inlineFeatureType);
        if (featureTypeConstraint.size() > 0) {
            FeatureTypeConstraint[] constraints =
                    new FeatureTypeConstraint[featureTypeConstraint.size()];
            for (int i = 0; i < constraints.length; i++) {
                constraints[i] = featureTypeConstraint.get(i).build();
            }
            layer.setLayerFeatureConstraints(constraints);
        }
        for (StyleBuilder sb : userStyles) {
            layer.addUserStyle(sb.build());
        }
        return layer;
    }

    @Override
    protected void buildSLDInternal(StyledLayerDescriptorBuilder sb) {
        sb.userLayer().init(this);
    }
}
