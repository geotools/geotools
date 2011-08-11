package org.geotools.styling.builder;

import java.util.ArrayList;
import java.util.List;

import org.geotools.Builder;
import org.geotools.styling.GraphicLegend;
import org.geotools.styling.Rule;
import org.geotools.styling.Symbolizer;
import org.opengis.filter.Filter;

public class RuleBuilder extends AbstractStyleBuilder<Rule> {
    List<Symbolizer> symbolizers = new ArrayList<Symbolizer>();

    Builder<? extends Symbolizer> symbolizerBuilder;

    String name;

    String ruleAbstract;

    double minScaleDenominator;

    double maxScaleDenominator;

    Filter filter = null;

    boolean elseFilter;

    String title;

    private GraphicLegendBuilder legend = new GraphicLegendBuilder(this).unset();

    public RuleBuilder() {
        this(null);
    }

    public RuleBuilder(FeatureTypeStyleBuilder parent) {
        super(parent);
        reset();
    }

    public RuleBuilder name(String name) {
        unset = false;
        this.name = name;
        return this;
    }

    public RuleBuilder title(String title) {
        unset = false;
        this.title = title;
        return this;
    }

    public RuleBuilder ruleAbstract(String ruleAbstract) {
        unset = false;
        this.ruleAbstract = ruleAbstract;
        return this;
    }

    public GraphicLegendBuilder legend() {
        unset = false;
        return legend;
    }

    public RuleBuilder min(double minScaleDenominator) {
        unset = false;
        if (minScaleDenominator < 0)
            throw new IllegalArgumentException(
                    "Invalid min scale denominator, should be positive or 0");
        this.minScaleDenominator = minScaleDenominator;
        return this;
    }

    public RuleBuilder max(double maxScaleDenominator) {
        unset = false;
        if (maxScaleDenominator < 0)
            throw new IllegalArgumentException(
                    "Invalid max scale denominator, should be positive or 0");
        this.maxScaleDenominator = maxScaleDenominator;
        return this;
    }

    public RuleBuilder elseFilter() {
        unset = false;
        this.elseFilter = true;
        this.filter = null;
        return this;
    }

    public RuleBuilder filter(Filter filter) {
        unset = false;
        this.elseFilter = false;
        this.filter = filter;
        return this;
    }

    public RuleBuilder filter(String cql) {
        unset = false;
        this.elseFilter = false;
        this.filter = cqlFilter(cql);
        return this;
    }

    public PointSymbolizerBuilder point() {
        unset = false;
        if (symbolizerBuilder != null)
            symbolizers.add(symbolizerBuilder.build());
        symbolizerBuilder = new PointSymbolizerBuilder(this);
        return (PointSymbolizerBuilder) symbolizerBuilder;
    }

    public LineSymbolizerBuilder line() {
        unset = false;
        if (symbolizerBuilder != null)
            symbolizers.add(symbolizerBuilder.build());
        symbolizerBuilder = new LineSymbolizerBuilder(this);
        return (LineSymbolizerBuilder) symbolizerBuilder;
    }

    public PolygonSymbolizerBuilder polygon() {
        unset = false;
        if (symbolizerBuilder != null)
            symbolizers.add(symbolizerBuilder.build());
        symbolizerBuilder = new PolygonSymbolizerBuilder(this);
        return (PolygonSymbolizerBuilder) symbolizerBuilder;
    }

    public TextSymbolizerBuilder text() {
        unset = false;
        if (symbolizerBuilder != null)
            symbolizers.add(symbolizerBuilder.build());
        symbolizerBuilder = new TextSymbolizerBuilder(this);
        return (TextSymbolizerBuilder) symbolizerBuilder;
    }

    public RasterSymbolizerBuilder raster() {
        unset = false;
        if (symbolizerBuilder != null)
            symbolizers.add(symbolizerBuilder.build());
        symbolizerBuilder = new RasterSymbolizerBuilder(this);
        return (RasterSymbolizerBuilder) symbolizerBuilder;
    }

    public ExtensionSymbolizerBuilder extension() {
        unset = false;
        if (symbolizerBuilder != null)
            symbolizers.add(symbolizerBuilder.build());
        symbolizerBuilder = new ExtensionSymbolizerBuilder(this);
        return (ExtensionSymbolizerBuilder) symbolizerBuilder;
    }

    public Rule build() {
        if (unset) {
            return null;
        }
        if (symbolizerBuilder == null && symbolizers.size() == 0) {
            symbolizerBuilder = new PointSymbolizerBuilder();
        }
        if (symbolizerBuilder != null) {
            symbolizers.add(symbolizerBuilder.build());
        }

        Rule rule = sf.createRule();
        rule.setName(name);
        // TODO: rule's description cannot be set
        rule.setTitle(title);
        rule.setAbstract(ruleAbstract);
        rule.setMinScaleDenominator(minScaleDenominator);
        rule.setMaxScaleDenominator(maxScaleDenominator);
        rule.setFilter(filter);
        rule.setElseFilter(elseFilter);
        rule.symbolizers().addAll(symbolizers);
        GraphicLegend gl = legend.build();
        if (gl != null) {
            rule.setLegend(gl);
        }

        if (parent == null) {
            reset();
        }
        return rule;
    }

    public RuleBuilder unset() {
        return (RuleBuilder) super.unset();
    }

    public RuleBuilder reset() {
        name = null;
        title = null;
        ruleAbstract = null;
        minScaleDenominator = 0;
        maxScaleDenominator = Double.POSITIVE_INFINITY;
        filter = Filter.INCLUDE;
        elseFilter = false;
        symbolizers.clear();
        legend.unset();
        unset = false;
        return this;
    }

    public RuleBuilder reset(Rule rule) {
        if (rule == null) {
            return unset();
        }
        name = rule.getName();
        title = rule.getTitle();
        ruleAbstract = rule.getAbstract();
        minScaleDenominator = rule.getMinScaleDenominator();
        maxScaleDenominator = rule.getMaxScaleDenominator();
        filter = rule.getFilter();
        elseFilter = rule.isElseFilter();
        symbolizers.clear();
        symbolizers.addAll(rule.symbolizers()); // TODO: unpack into builders in order to "copy"
        symbolizerBuilder = null;
        unset = false;
        legend.reset(rule.getLegend());
        return this;
    }

    protected void buildStyleInternal(StyleBuilder sb) {
        sb.featureTypeStyle().rule().init(this);
    }

}
