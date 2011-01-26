package org.geotools.styling.builder;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.measure.unit.Unit;

import org.geotools.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.expression.ChildExpressionBuilder;
import org.geotools.styling.ExtensionSymbolizer;
import org.geotools.styling.StyleFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.style.Description;

public class ExtensionSymbolizerBuilder<P> implements Builder<ExtensionSymbolizer> {
    private StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);

    private P parent;

    boolean unset = true; // current value is null

    private String name;

    private String geometry;

    private Description description;

    private Unit<?> unit;

    private String extensionName;

    private Map<String, ChildExpressionBuilder<ExtensionSymbolizerBuilder<P>>> parameters = new HashMap<String, ChildExpressionBuilder<ExtensionSymbolizerBuilder<P>>>();

    public ExtensionSymbolizerBuilder() {
        this(null);
    }

    public ExtensionSymbolizerBuilder(P parent) {
        this.parent = parent;
        reset();
    }

    public ExtensionSymbolizer build() {
        if (unset) {
            return null;
        }
        Map<String, Expression> params = new HashMap<String, Expression>();
        for (Entry<String, ChildExpressionBuilder<ExtensionSymbolizerBuilder<P>>> entry : parameters
                .entrySet()) {
            params.put(entry.getKey(), entry.getValue().build());
        }
        ExtensionSymbolizer symbolizer = sf.extensionSymbolizer(name, geometry, description, unit,
                extensionName, params);
        return symbolizer;
    }

    public P end() {
        return parent;
    }

    public ExtensionSymbolizerBuilder<P> reset() {
        unset = false;
        return this;
    }

    public ExtensionSymbolizerBuilder<P> reset(ExtensionSymbolizer symbolizer) {
        if (symbolizer == null) {
            return reset();
        }
        unset = false;
        return this;
    }

    public ExtensionSymbolizerBuilder<P> unset() {
        unset = true;
        return this;
    }

}
