package org.geotools.styling;

import javax.measure.quantity.Length;
import javax.measure.unit.Unit;

import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.PropertyName;

public abstract class AbstractSymbolizer implements Symbolizer {
    protected String name;

    protected Description description;

    protected Expression geometry;

    protected Unit<Length> unitOfMeasure;

    protected AbstractSymbolizer() {
    }

    public AbstractSymbolizer(String name, Description description, Expression geometry,
            Unit<Length> unitOfMeasure) {
        this.name = name;
        this.description = description;
        this.geometry = geometry;
        this.unitOfMeasure = unitOfMeasure;
    }
    
    public AbstractSymbolizer(String name, Description description, String geometryPropertyName,
            Unit<Length> unitOfMeasure) {
        this.name = name;
        this.description = description;
        this.unitOfMeasure = unitOfMeasure;
        setGeometryPropertyName(geometryPropertyName);
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(org.opengis.style.Description description) {
        this.description = DescriptionImpl.cast(description);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUnitOfMeasure(Unit<Length> uom) {
        this.unitOfMeasure = uom;
    }

    public Unit<Length> getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public Expression getGeometry() {
        return geometry;
    }

    public void setGeometry(Expression geometry) {
        this.geometry = geometry;
    }

    public String getGeometryPropertyName() {
        if (geometry instanceof PropertyName) {
            PropertyName pg = (PropertyName) geometry;
            return pg.getPropertyName();
        }
        return null;
    }

    public void setGeometryPropertyName(String geometryPropertyName) {
        if (geometryPropertyName == null) {
            geometry = null;
        } else {
            org.opengis.filter.FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
            geometry = ff.property(geometryPropertyName);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((geometry == null) ? 0 : geometry.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((unitOfMeasure == null) ? 0 : unitOfMeasure.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AbstractSymbolizer other = (AbstractSymbolizer) obj;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (geometry == null) {
            if (other.geometry != null)
                return false;
        } else if (!geometry.equals(other.geometry))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (unitOfMeasure == null) {
            if (other.unitOfMeasure != null)
                return false;
        } else if (!unitOfMeasure.equals(other.unitOfMeasure))
            return false;
        return true;
    }
    
    

}
