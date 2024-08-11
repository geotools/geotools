/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014 - 2016, Open Source Geospatial Foundation (OSGeo)
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
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Id;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.style.Description;
import org.geotools.api.style.FeatureTypeStyle;
import org.geotools.api.style.Rule;
import org.geotools.api.style.SemanticType;
import org.geotools.brewer.styling.filter.IdBuilder;
import org.geotools.feature.NameImpl;

public class FeatureTypeStyleBuilder extends AbstractStyleBuilder<FeatureTypeStyle> {
    String name;

    List<RuleBuilder> rules = new ArrayList<>();

    DescriptionBuilder description = new DescriptionBuilder().unset();

    LinkedHashSet<Name> featureTypeNames = new LinkedHashSet<>();

    private IdBuilder<FeatureTypeStyleBuilder> definedFor = new IdBuilder<>(this);

    private Set<SemanticType> types = new LinkedHashSet<>();

    Map<String, String> options = new HashMap<>();

    Expression transformation = null;

    // TODO : add semantic type identifier, provided it makes any sense to have it

    FeatureTypeStyleBuilder(StyleBuilder parent) {
        super(parent);
        reset();
    }

    public FeatureTypeStyleBuilder() {
        this(null);
    }

    public RuleBuilder rule() {
        RuleBuilder ruleBuilder = new RuleBuilder(this);
        rules.add(ruleBuilder);
        return ruleBuilder;
    }

    public FeatureTypeStyleBuilder name(String name) {
        this.name = name;
        return this;
    }

    public FeatureTypeStyleBuilder title(String title) {
        this.description.title(title);
        return this;
    }

    public DescriptionBuilder description() {
        return description;
    }

    /**
     * Accumulates another feature type name in the list of the feature type names for this {@link
     * FeatureTypeStyle}
     */
    public FeatureTypeStyleBuilder featureTypeName(String featureTypeName) {
        this.featureTypeNames.add(new NameImpl(featureTypeName));
        return this;
    }

    public String name() {
        return name;
    }

    public List<RuleBuilder> rules() {
        unset = false;
        return rules;
    }

    public FeatureTypeStyleBuilder rules(List<Rule> rules) {
        unset = false;
        for (Rule rule : rules) {
            this.rules.add(new RuleBuilder(this).reset(rule));
        }
        return this;
    }

    public FeatureTypeStyleBuilder description(Description description) {
        this.description.reset(description);
        this.unset = false;
        return this;
    }

    public LinkedHashSet<Name> featureTypeNames() {
        return featureTypeNames;
    }

    public void setFeatureTypeNames(List<Name> featureTypeNames) {
        this.featureTypeNames.clear();
        this.featureTypeNames.addAll(featureTypeNames);
    }

    public IdBuilder<FeatureTypeStyleBuilder> definedFor() {
        return definedFor;
    }

    public void definedFor(Id fids) {
        this.definedFor.reset(fids);
    }

    public Set<SemanticType> types() {
        return types;
    }

    public FeatureTypeStyleBuilder option(String name, String value) {
        options.put(name, value);
        return this;
    }

    public FeatureTypeStyleBuilder transformation(Expression transformation) {
        this.unset = false;
        this.transformation = transformation;
        return this;
    }

    /**
     * Accumulates another feature type name in the list of the feature type names for this {@link
     * FeatureTypeStyle}
     */
    public FeatureTypeStyleBuilder featureTypeName(Name featureTypeName) {
        this.featureTypeNames.add(featureTypeName);
        unset = false;
        return this;
    }

    @Override
    public FeatureTypeStyle build() {
        if (unset) {
            return null;
        }
        List<org.geotools.api.style.Rule> list = new ArrayList<>();
        for (RuleBuilder ruleBuilder : rules) {
            list.add(ruleBuilder.build());
        }
        FeatureTypeStyle fts =
                sf.featureTypeStyle(
                        name,
                        description.build(),
                        definedFor.build(),
                        featureTypeNames,
                        types,
                        list);
        if (!options.isEmpty()) {
            fts.getOptions().putAll(options);
        }
        fts.setTransformation(transformation);
        if (parent == null) {
            reset();
        }
        return fts;
    }

    @Override
    public FeatureTypeStyleBuilder reset() {
        rules.clear();
        this.name = null;
        this.description.reset();
        this.definedFor.reset();
        this.featureTypeNames.clear();
        this.rules.clear();
        this.options.clear();
        this.transformation = null;

        this.unset = false;
        return this;
    }

    @Override
    public FeatureTypeStyleBuilder reset(FeatureTypeStyle fts) {
        if (fts == null) {
            return unset();
        }
        this.name = fts.getName();
        this.description.reset(fts.getDescription());
        this.definedFor.reset(fts.getFeatureInstanceIDs());
        this.featureTypeNames.clear();
        if (fts.featureTypeNames() != null) {
            this.featureTypeNames.addAll(fts.featureTypeNames());
        }
        this.rules.clear();
        if (fts.rules() != null) {
            for (Rule rule : fts.rules()) {
                this.rules.add(new RuleBuilder(this).reset(rule));
            }
        }
        this.options.clear();
        this.options.putAll(fts.getOptions());
        this.transformation = fts.getTransformation();
        this.unset = false;
        return this;
    }

    @Override
    public FeatureTypeStyleBuilder unset() {
        return (FeatureTypeStyleBuilder) super.unset();
    }

    @Override
    protected void buildStyleInternal(StyleBuilder sb) {
        sb.featureTypeStyle().init(this);
    }

    private void init(FeatureTypeStyleBuilder other) {
        this.definedFor = other.definedFor;
        this.description = other.description;
        this.featureTypeNames = other.featureTypeNames;
        this.parent = other.parent;
        this.rules = other.rules;
        this.sf = other.sf;
        this.types = other.types;
        this.options = other.options;
        this.unset = other.unset;
        this.transformation = other.transformation;
    }
}
