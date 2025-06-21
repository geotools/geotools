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
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.geotools.api.filter.Filter;
import org.geotools.api.metadata.citation.OnLineResource;
import org.geotools.api.style.Description;
import org.geotools.api.style.Graphic;
import org.geotools.api.style.GraphicLegend;
import org.geotools.api.style.Rule;
import org.geotools.api.style.StyleVisitor;
import org.geotools.api.style.Symbolizer;
import org.geotools.api.style.TraversingStyleVisitor;
import org.geotools.api.util.Cloneable;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.visitor.DuplicatingFilterVisitor;
import org.geotools.util.Utilities;

/**
 * Provides the default implementation of Rule.
 *
 * @author James Macgill
 * @author Johann Sorel (Geomatys)
 * @version $Id$
 */
public class RuleImpl implements Rule, Cloneable {
    private List<Symbolizer> symbolizers = new ArrayList<>();

    private GraphicLegend legend;
    private String name;
    private DescriptionImpl description = new DescriptionImpl();
    private Filter filter = null;
    private boolean hasElseFilter = false;
    private double maxScaleDenominator = Double.POSITIVE_INFINITY;
    private double minScaleDenominator = 0.0;
    private OnLineResource online = null;
    protected Map<String, String> options;

    /** Creates a new instance of DefaultRule */
    protected RuleImpl() {}

    /** Creates a new instance of DefaultRule */
    protected RuleImpl(Symbolizer... symbolizers) {
        this.symbolizers.addAll(Arrays.asList(symbolizers));
    }

    protected RuleImpl(
            Symbolizer[] symbolizers,
            Description desc,
            Graphic legend,
            String name,
            Filter filter,
            boolean isElseFilter,
            double maxScale,
            double minScale) {
        this.symbolizers = new ArrayList<>(Arrays.asList(symbolizers));
        description.setAbstract(desc.getAbstract());
        description.setTitle(desc.getTitle());
        this.legend = (GraphicLegend) legend;
        this.name = name;
        this.filter = filter;
        hasElseFilter = isElseFilter;
        this.maxScaleDenominator = maxScale;
        this.minScaleDenominator = minScale;
    }

    /** Copy constructor */
    public RuleImpl(Rule rule) {
        this.symbolizers = new ArrayList<>();
        for (org.geotools.api.style.Symbolizer sym : rule.symbolizers()) {
            if (sym instanceof Symbolizer) {
                this.symbolizers.add(sym);
            }
        }
        if (rule.getDescription() != null && rule.getDescription().getTitle() != null) {
            this.description.setTitle(rule.getDescription().getTitle());
        }
        if (rule.getDescription() != null && rule.getDescription().getAbstract() != null) {
            this.description.setTitle(rule.getDescription().getAbstract());
        }
        if (rule.getLegend() instanceof Graphic) {
            this.legend = rule.getLegend();
        }
        this.name = rule.getName();
        this.filter = rule.getFilter();
        this.hasElseFilter = rule.isElseFilter();
        this.maxScaleDenominator = rule.getMaxScaleDenominator();
        this.minScaleDenominator = rule.getMinScaleDenominator();
    }

    @Override
    public GraphicLegend getLegend() {
        return legend;
    }

    @Override
    public void setLegend(GraphicLegend legend) {
        this.legend = legend;
    }

    @Override
    public List<Symbolizer> symbolizers() {
        return symbolizers;
    }

    @Override
    public Symbolizer[] getSymbolizers() {

        final Symbolizer[] ret = new Symbolizer[symbolizers.size()];
        for (int i = 0, n = symbolizers.size(); i < n; i++) {
            ret[i] = symbolizers.get(i);
        }

        return ret;
    }

    @Override
    public DescriptionImpl getDescription() {
        return description;
    }

    @Override
    public void setDescription(org.geotools.api.style.Description description) {
        this.description = DescriptionImpl.cast(description);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    @Override
    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    @Override
    public boolean isElseFilter() {
        return hasElseFilter;
    }

    @Override
    public void setElseFilter(boolean defaultb) {
        hasElseFilter = defaultb;
    }

    /**
     * Getter for property maxScaleDenominator.
     *
     * @return Value of property maxScaleDenominator.
     */
    @Override
    public double getMaxScaleDenominator() {
        return maxScaleDenominator;
    }

    /**
     * Setter for property maxScaleDenominator.
     *
     * @param maxScaleDenominator New value of property maxScaleDenominator.
     */
    @Override
    public void setMaxScaleDenominator(double maxScaleDenominator) {
        this.maxScaleDenominator = maxScaleDenominator;
    }

    /**
     * Getter for property minScaleDenominator.
     *
     * @return Value of property minScaleDenominator.
     */
    @Override
    public double getMinScaleDenominator() {
        return minScaleDenominator;
    }

    /**
     * Setter for property minScaleDenominator.
     *
     * @param minScaleDenominator New value of property minScaleDenominator.
     */
    @Override
    public void setMinScaleDenominator(double minScaleDenominator) {
        this.minScaleDenominator = minScaleDenominator;
    }

    @Override
    public Object accept(TraversingStyleVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    @Override
    public void accept(StyleVisitor visitor) {
        visitor.visit(this);
    }

    /** Creates a deep copy clone of the rule. */
    @Override
    public Object clone() {
        try {
            RuleImpl clone = (RuleImpl) super.clone();

            clone.name = name;
            clone.description.setAbstract(description.getAbstract());
            clone.description.setTitle(description.getTitle());
            if (filter == null) {
                clone.filter = null;
            } else {
                DuplicatingFilterVisitor visitor = new DuplicatingFilterVisitor();
                clone.filter = (Filter) filter.accept(visitor, CommonFactoryFinder.getFilterFactory(null));
            }
            clone.hasElseFilter = hasElseFilter;
            clone.legend = legend;

            clone.symbolizers = new ArrayList<>(symbolizers);

            clone.maxScaleDenominator = maxScaleDenominator;
            clone.minScaleDenominator = minScaleDenominator;
            clone.options = options;

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("This will never happen", e);
        }
    }

    /**
     * Generates a hashcode for the Rule.
     *
     * <p>For complex styles this can be an expensive operation since the hash code is computed using all the hashcodes
     * of the object within the style.
     *
     * @return The hashcode.
     */
    @Override
    public int hashCode() {
        final int PRIME = 1000003;
        int result = 0;
        result = PRIME * result + symbolizers.hashCode();

        if (legend != null) result = PRIME * result + legend.hashCode();

        if (name != null) {
            result = PRIME * result + name.hashCode();
        }

        if (description != null) {
            result = PRIME * result + description.hashCode();
        }

        if (filter != null) {
            result = PRIME * result + filter.hashCode();
        }

        if (options != null) {
            result = PRIME * result + options.hashCode();
        }

        result = PRIME * result + (hasElseFilter ? 1 : 0);

        long temp = Double.doubleToLongBits(maxScaleDenominator);
        result = PRIME * result + (int) (temp >>> 32);
        result = PRIME * result + (int) (temp & 0xFFFFFFFF);
        temp = Double.doubleToLongBits(minScaleDenominator);
        result = PRIME * result + (int) (temp >>> 32);
        result = PRIME * result + (int) (temp & 0xFFFFFFFF);

        return result;
    }

    /**
     * Compares this Rule with another for equality.
     *
     * <p>Two RuleImpls are equal if all their properties are equal.
     *
     * <p>For complex styles this can be an expensive operation since it checks all objects for equality.
     *
     * @param oth The other rule to compare with.
     * @return True if this and oth are equal.
     */
    @Override
    public boolean equals(Object oth) {
        if (this == oth) {
            return true;
        }

        if (oth instanceof RuleImpl) {
            RuleImpl other = (RuleImpl) oth;

            return Utilities.equals(name, other.name)
                    && Utilities.equals(description, other.description)
                    && Utilities.equals(filter, other.filter)
                    && hasElseFilter == other.hasElseFilter
                    && Utilities.equals(legend, other.legend)
                    && Utilities.equals(symbolizers, other.symbolizers)
                    && Double.doubleToLongBits(maxScaleDenominator)
                            == Double.doubleToLongBits(other.maxScaleDenominator)
                    && Double.doubleToLongBits(minScaleDenominator)
                            == Double.doubleToLongBits(other.minScaleDenominator)
                    && getOptions().equals(other.getOptions());
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("<RuleImpl");
        if (name != null) {
            buf.append(":");
            buf.append(name);
        }
        buf.append("> ");
        buf.append(filter);
        if (symbolizers != null) {
            buf.append("\n");
            for (Symbolizer symbolizer : symbolizers) {
                buf.append("\t");
                buf.append(symbolizer);
                buf.append("\n");
            }
        }
        buf.append(getOptions());
        return buf.toString();
    }

    @Override
    public OnLineResource getOnlineResource() {
        return online;
    }

    @Override
    public void setOnlineResource(OnLineResource online) {
        this.online = online;
    }

    static RuleImpl cast(Rule rule) {
        if (rule == null) {
            return null;
        } else if (rule instanceof RuleImpl) {
            return (RuleImpl) rule;
        } else {
            RuleImpl copy = new RuleImpl(rule); // replace with casting ...
            return copy;
        }
    }

    @Override
    public boolean hasOption(String key) {
        return options != null && options.containsKey(key);
    }

    @Override
    public Map<String, String> getOptions() {
        if (options == null) {
            options = new LinkedHashMap<>();
        }
        return options;
    }
}
