/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014-2015, Open Source Geospatial Foundation (OSGeo)
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

import java.util.HashMap;
import java.util.Map;
import org.geotools.styling.ContrastEnhancement;
import org.opengis.filter.expression.Expression;
import org.opengis.style.ContrastMethod;

public class ContrastEnhancementBuilder extends AbstractStyleBuilder<ContrastEnhancement> {
    private Expression gamma = null;

    private ContrastMethod method;

    private Map<String, Expression> options = new HashMap<>();

    public ContrastEnhancementBuilder() {
        this(null);
    }

    public ContrastEnhancementBuilder(AbstractStyleBuilder<?> parent) {
        super(parent);
        reset();
    }

    public ContrastEnhancementBuilder gamma(Expression gamma) {
        this.gamma = gamma;
        this.unset = false;
        return this;
    }

    public ContrastEnhancementBuilder gamma(double gamma) {
        return gamma(literal(gamma));
    }

    public ContrastEnhancementBuilder normalize(Map<String, Expression> constrastParameters) {
        return contrastMethod("normalize", constrastParameters);
    }

    public ContrastEnhancementBuilder histogram(Map<String, Expression> constrastParameters) {
        return contrastMethod("histogram", constrastParameters);
    }

    public ContrastEnhancementBuilder exponential(Map<String, Expression> constrastParameters) {
        return contrastMethod("exponential", constrastParameters);
    }

    public ContrastEnhancementBuilder logarithmic(Map<String, Expression> constrastParameters) {
        return contrastMethod("logarithm", constrastParameters);
    }

    private ContrastEnhancementBuilder contrastMethod(
            String name, Map<String, Expression> constrastParameters) {
        /*if ("histogram".equals(name)) {
            this.method = ContrastMethod.HISTOGRAM;
        } else if ("normalize".equals(name)) {
            this.method = ContrastMethod.NORMALIZE;
        } else if ("logarithmic".equals(name)) {
            this.method = ContrastMethod.LOGARITHMIC;
        } else if ("exponential".equals(name)) {
            this.method = ContrastMethod.EXPONENTIAL;
        } else {
            throw new IllegalArgumentException("Unexpected ContrastEnhamcement method " + name);
        }*/
        this.method = ContrastMethod.valueOf(name);
        if (constrastParameters != null) {
            options = constrastParameters;
        }
        this.unset = false;
        return this;
    }

    public ContrastEnhancementBuilder gamma(String cqlExpression) {
        return gamma(cqlExpression(cqlExpression));
    }

    public ContrastEnhancement build() {
        if (unset) {
            return null;
        }
        ContrastEnhancement contrastEnhancement = sf.contrastEnhancement(gamma, method);
        contrastEnhancement.setOptions(options);

        return contrastEnhancement;
    }

    public ContrastEnhancementBuilder reset() {
        gamma = null;
        method = null;
        unset = false;
        return this;
    }

    public ContrastEnhancementBuilder reset(ContrastEnhancement contrastEnhancement) {
        if (contrastEnhancement == null) {
            return reset();
        }
        gamma = contrastEnhancement.getGammaValue();
        method = contrastEnhancement.getMethod();
        options = contrastEnhancement.getOptions();
        unset = false;
        return this;
    }

    public ContrastEnhancementBuilder unset() {
        return (ContrastEnhancementBuilder) super.unset();
    }

    @Override
    protected void buildStyleInternal(StyleBuilder sb) {
        throw new UnsupportedOperationException(
                "Cannot build a meaningful style out of a contrast enhancement alone");
    }

    /** @return */
    public ContrastEnhancementBuilder normalize() {
        return contrastMethod("normalize", null);
    }

    public ContrastEnhancementBuilder histogram() {
        return contrastMethod("histogram", null);
    }

    public ContrastEnhancementBuilder logarithmic() {
        return contrastMethod("logarithmic", null);
    }

    public ContrastEnhancementBuilder exponential() {
        return contrastMethod("exponential", null);
    }
}
