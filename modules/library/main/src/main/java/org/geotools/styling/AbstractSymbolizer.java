/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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

import java.util.LinkedHashMap;
import java.util.Map;
import javax.measure.Unit;
import javax.measure.quantity.Length;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.style.Description;
import org.geotools.api.style.Symbolizer;
import org.geotools.factory.CommonFactoryFinder;

public abstract class AbstractSymbolizer implements Symbolizer {
    protected String name;

    protected Description description;

    protected Expression geometry;

    protected Unit<Length> unitOfMeasure;

    protected Map<String, String> options;

    protected AbstractSymbolizer() {}

    public AbstractSymbolizer(String name, Description description, Expression geometry, Unit<Length> unitOfMeasure) {
        this.name = name;
        this.description = description;
        this.geometry = geometry;
        this.unitOfMeasure = unitOfMeasure;
    }

    public AbstractSymbolizer(
            String name, Description description, String geometryPropertyName, Unit<Length> unitOfMeasure) {
        this.name = name;
        this.description = description;
        this.unitOfMeasure = unitOfMeasure;
        setGeometryPropertyName(geometryPropertyName);
    }

    @Override
    public Description getDescription() {
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
    public void setUnitOfMeasure(Unit<Length> uom) {
        this.unitOfMeasure = uom;
    }

    @Override
    public Unit<Length> getUnitOfMeasure() {
        return unitOfMeasure;
    }

    @Override
    public Expression getGeometry() {
        return geometry;
    }

    @Override
    public void setGeometry(Expression geometry) {
        this.geometry = geometry;
    }

    @Override
    public String getGeometryPropertyName() {
        if (geometry instanceof PropertyName) {
            PropertyName pg = (PropertyName) geometry;
            return pg.getPropertyName();
        }
        return null;
    }

    @Override
    public void setGeometryPropertyName(String geometryPropertyName) {
        if (geometryPropertyName == null) {
            geometry = null;
        } else {
            org.geotools.api.filter.FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
            geometry = ff.property(geometryPropertyName);
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (description == null ? 0 : description.hashCode());
        result = prime * result + (geometry == null ? 0 : geometry.hashCode());
        result = prime * result + (name == null ? 0 : name.hashCode());
        result = prime * result + (unitOfMeasure == null ? 0 : unitOfMeasure.hashCode());
        result = prime * result + (options == null ? 0 : options.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        AbstractSymbolizer other = (AbstractSymbolizer) obj;
        if (description == null) {
            if (other.description != null) return false;
        } else if (!description.equals(other.description)) return false;
        if (geometry == null) {
            if (other.geometry != null) return false;
        } else if (!geometry.equals(other.geometry)) return false;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        if (unitOfMeasure == null) {
            if (other.unitOfMeasure != null) return false;
        } else if (!unitOfMeasure.equals(other.unitOfMeasure)) return false;
        if (options == null) {
            if (other.options != null && !other.options.isEmpty()) return false;
        }
        if (options == null || options.isEmpty()) {
            // this options are NULL or empty
            if (other.options != null && !other.options.isEmpty()) {
                // the other options are neither NULL or empty
                return false;
            }
        } else if (!options.equals(other.options)) {
            // options are not considered the same
            return false;
        }
        return true;
    }
}
