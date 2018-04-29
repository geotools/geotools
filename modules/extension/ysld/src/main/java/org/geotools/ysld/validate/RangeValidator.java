/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016 Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014-2016 Boundless Spatial
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
package org.geotools.ysld.validate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.yaml.snakeyaml.events.ScalarEvent;

/**
 * Validators for tuples that represent a range of values This Validator is stateful, do not re-use
 * it.
 *
 * @param <T> Content type of the tuple.
 */
public abstract class RangeValidator<T extends Comparable<T>> extends TupleValidator {
    T min = null;

    public RangeValidator() {
        super(Collections.<ScalarValidator>emptyList());
    }

    @Override
    protected List<ScalarValidator> getSubValidators() {
        return Arrays.asList((ScalarValidator) new ValueValidator(true), new ValueValidator(false));
    }

    class ValueValidator extends ScalarValidator {
        ValueValidator previous;

        boolean isMin;

        public ValueValidator(boolean isMin) {
            super();
            this.isMin = isMin;
        }

        /** {@inheritDoc} */
        @Override
        protected String validate(String value, ScalarEvent evt, YsldValidateContext context) {
            try {
                if (value != null
                        && !value.isEmpty()
                        && !(isMin ? "min" : "max").equalsIgnoreCase(value)) {
                    T parsed = parse(value);
                    validateParsed(parsed, evt, context);
                    if (isMin) {
                        min = parsed;
                    } else {
                        if (min != null && parsed != null && min.compareTo(parsed) > 0) {
                            return "Minimum is greater than maximum";
                        }
                    }
                }
                return null;
            } catch (IllegalArgumentException ex) {
                return ex.getMessage();
            }
        }
    }

    abstract T parse(String s) throws IllegalArgumentException;

    protected void validateParsed(T parsed, ScalarEvent evt, YsldValidateContext context) {
        // Do Nothing
    }

    @Override
    void reset() {
        super.reset();
        min = null;
    }
}
