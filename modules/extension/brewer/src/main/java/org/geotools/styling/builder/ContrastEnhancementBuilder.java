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
package org.geotools.styling.builder;

import org.geotools.styling.ContrastEnhancement;
import org.opengis.filter.expression.Expression;
import org.opengis.style.ContrastMethod;

/**
 * 
 *
 * @source $URL$
 */
public class ContrastEnhancementBuilder extends AbstractStyleBuilder<ContrastEnhancement> {
    private Expression gamma = null;

    private ContrastMethod method;

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

    public ContrastEnhancementBuilder normalize() {
        this.method = ContrastMethod.NORMALIZE;
        this.unset = false;
        return this;
    }

    public ContrastEnhancementBuilder histogram() {
        this.method = ContrastMethod.HISTOGRAM;
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
        return contrastEnhancement;
    }

    public ContrastEnhancementBuilder reset() {
        gamma = null;
        method = ContrastMethod.NONE;
        unset = false;
        return this;
    }

    public ContrastEnhancementBuilder reset(ContrastEnhancement contrastEnhancement) {
        if (contrastEnhancement == null) {
            return reset();
        }
        gamma = contrastEnhancement.getGammaValue();
        method = contrastEnhancement.getMethod();
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

}
