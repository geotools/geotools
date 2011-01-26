package org.geotools.styling.builder;

import java.util.ArrayList;
import java.util.List;

import org.geotools.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.SubFilterBuilder;
import org.geotools.styling.Rule;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.Symbolizer;
import org.opengis.filter.Filter;

public class RuleBuilder<P> implements Builder<Rule> {
    StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
    P parent;
    
    List<Symbolizer> symbolizers = new ArrayList<Symbolizer>();

    Builder<? extends Symbolizer> symbolizerBuilder;

    String name;

    String ruleAbstract;

    double minScaleDenominator;

    double maxScaleDenominator;

    SubFilterBuilder<RuleBuilder<P>> filter = new SubFilterBuilder<RuleBuilder<P>>(this);

    boolean elseFilter;

    String title;

    private boolean unset = false;

    public RuleBuilder() {
        this( null );
    }
    
    public RuleBuilder(P parent) {
        this.parent = parent;
        reset();
    }

    public RuleBuilder<P> name(String name) {
        unset = false;
        this.name = name;
        return this;
    }

    public RuleBuilder<P> title(String title) {
        unset = false;
        this.title = title;
        return this;
    }

    public RuleBuilder<P> ruleAbstract(String ruleAbstract) {
        unset = false;
        this.ruleAbstract = ruleAbstract;
        return this;
    }

    public RuleBuilder<P> min(double minScaleDenominator) {
        unset = false;
        if (minScaleDenominator < 0)
            throw new IllegalArgumentException(
                    "Invalid min scale denominator, should be positive or 0");
        this.minScaleDenominator = minScaleDenominator;
        return this;
    }

    public RuleBuilder<P> max(double maxScaleDenominator) {
        unset = false;
        if (maxScaleDenominator < 0)
            throw new IllegalArgumentException(
                    "Invalid max scale denominator, should be positive or 0");
        this.maxScaleDenominator = maxScaleDenominator;
        return this;
    }

    public RuleBuilder<P> elseFilter() {
        unset = false;
        this.elseFilter = true;
        this.filter.unset();
        return this;
    }

    public RuleBuilder<P> filter(Filter filter) {
        unset = false;
        this.elseFilter = false;
        this.filter.reset(filter);
        return this;
    }

    public PointSymbolizerBuilder<RuleBuilder<P>> newPoint() {
        unset = false;
        if (symbolizerBuilder != null)
            symbolizers.add(symbolizerBuilder.build());
        symbolizerBuilder = new PointSymbolizerBuilder<RuleBuilder<P>>(this);
        return (PointSymbolizerBuilder) symbolizerBuilder;
    }

    public LineSymbolizerBuilder<RuleBuilder<P>> newLine() {
        unset = false;
        if (symbolizerBuilder != null)
            symbolizers.add(symbolizerBuilder.build());
        symbolizerBuilder = new LineSymbolizerBuilder<RuleBuilder<P>>(this);
        return (LineSymbolizerBuilder) symbolizerBuilder;
    }

    public PolygonSymbolizerBuilder<RuleBuilder<P>> newPolygon() {
        unset = false;
        if (symbolizerBuilder != null)
            symbolizers.add(symbolizerBuilder.build());
        symbolizerBuilder = new PolygonSymbolizerBuilder<RuleBuilder<P>>(this);
        return (PolygonSymbolizerBuilder) symbolizerBuilder;
    }

    public Rule build() {
        if( unset ){
            return null;
        }
        if (symbolizerBuilder == null) {
            symbolizerBuilder = new PointSymbolizerBuilder();
        }
        // cascade build operation
        symbolizers.add(symbolizerBuilder.build());

        Rule rule = sf.createRule();
        rule.setName(name);
        // TODO: rule's description cannot be set
        rule.setTitle(title);
        rule.setAbstract(ruleAbstract);
        rule.setMinScaleDenominator(minScaleDenominator);
        rule.setMaxScaleDenominator(maxScaleDenominator);
        rule.setFilter(filter.build());
        rule.setElseFilter(elseFilter);
        rule.symbolizers().addAll(symbolizers);

        reset();
        return rule;
    }

    public RuleBuilder<P> unset(){
        reset();
        unset = true;
        return this;
    }
    public RuleBuilder<P> reset() {
        name = null;
        title = null;
        ruleAbstract = null;
        minScaleDenominator = 0;
        maxScaleDenominator = Double.POSITIVE_INFINITY;
        filter.reset( Filter.INCLUDE);
        elseFilter = false;
        symbolizers.clear();
        unset = false;
        return this;
    }
    public RuleBuilder<P> reset( Rule rule ){
        if( rule == null ){
            return unset();            
        }
        name = rule.getName();
        title = rule.getTitle();
        ruleAbstract = rule.getAbstract();
        minScaleDenominator = rule.getMinScaleDenominator();
        maxScaleDenominator = rule.getMaxScaleDenominator();
        filter.reset( rule.getFilter() );
        elseFilter = rule.isElseFilter();
        symbolizers.clear();
        symbolizers.addAll( rule.symbolizers() ); // TODO: unpack into builders in order to "copy"
        unset = false;
        return this;
    }

}
