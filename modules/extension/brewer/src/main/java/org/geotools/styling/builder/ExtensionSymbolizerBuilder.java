package org.geotools.styling.builder;

import java.util.HashMap;
import java.util.Map;

import javax.measure.unit.Unit;

import org.geotools.styling.ExtensionSymbolizer;
import org.opengis.filter.expression.Expression;

public class ExtensionSymbolizerBuilder extends AbstractStyleBuilder<ExtensionSymbolizer> {
    private String name;

    private Expression geometry;

    private DescriptionBuilder description = new DescriptionBuilder(this).unset();

    private Unit<?> uom;

    private String extensionName;

    private Map<String, Expression> parameters = new HashMap<String, Expression>();

    public ExtensionSymbolizerBuilder() {
        this(null);
    }

    public ExtensionSymbolizerBuilder(AbstractStyleBuilder<?> parent) {
        super(parent);
        reset();
    }

    public ExtensionSymbolizerBuilder name(String name) {
        unset = false;
        this.name = name;
        return this;
    }

    public ExtensionSymbolizerBuilder geometry(Expression geometry) {
        this.geometry = geometry;
        this.unset = false;
        return this;
    }

    public ExtensionSymbolizerBuilder geometryName(String geometry) {
        return geometry(property(geometry));
    }

    public ExtensionSymbolizerBuilder geometry(String cqlExpression) {
        return geometry(cqlExpression(cqlExpression));
    }

    public ExtensionSymbolizerBuilder uom(Unit<?> uom) {
        unset = false;
        this.uom = uom;
        return this;
    }

    public ExtensionSymbolizerBuilder extensionName(String extensionName) {
        unset = false;
        this.extensionName = extensionName;
        return this;
    }

    public DescriptionBuilder description() {
        unset = false;
        return description;
    }

    public ExtensionSymbolizerBuilder param(String name, Expression param) {
        this.unset = false;
        parameters.put(name, param);
        return this;
    }

    public ExtensionSymbolizer build() {
        if (unset) {
            return null;
        }
        ExtensionSymbolizer symbolizer = sf.extensionSymbolizer(name, null, description.build(),
                uom, extensionName, parameters);
        symbolizer.setGeometry(geometry);
        if (parent == null) {
            reset();
        }
        return symbolizer;
    }

    public ExtensionSymbolizerBuilder reset() {
        name = null;
        geometry = null;
        description.unset();
        uom = null;
        extensionName = null;
        parameters = new HashMap<String, Expression>();
        unset = false;
        return this;
    }

    public ExtensionSymbolizerBuilder reset(ExtensionSymbolizer symbolizer) {
        if (symbolizer == null) {
            return unset();
        }
        this.name = symbolizer.getName();
        this.geometry = symbolizer.getGeometry();
        this.description.reset(symbolizer.getDescription());
        this.uom = symbolizer.getUnitOfMeasure();
        this.extensionName = symbolizer.getExtensionName();
        this.parameters.clear();
        this.parameters.putAll(symbolizer.getParameters());
        unset = false;
        return this;
    }

    public ExtensionSymbolizerBuilder unset() {
        return (ExtensionSymbolizerBuilder) super.unset();
    }

    @Override
    protected void buildStyleInternal(StyleBuilder sb) {
        sb.featureTypeStyle().rule().extension().init(this);
    }

}
