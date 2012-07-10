/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.transform;

import java.util.Arrays;
import java.util.List;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.PropertyName;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Defines a transformed attribute to be used in {@link TransformFeatureSource}
 * 
 * @author Andrea Aime - GeoSolutions
 */
public class Definition {
    String name;

    Expression expression;

    Class binding;

    CoordinateReferenceSystem crs;
    
    static final FilterFactory2 FF = CommonFactoryFinder.getFilterFactory2();

    /**
     * Creates a new transformed property that mirrors 1-1 an existing property in the source type,
     * without even renaming it
     * 
     * @param name The property name
     */
    public Definition(String name) {
        this(name, null, null);
    }

    /**
     * Creates a new transformed property
     * 
     * @param name The property name
     * @param source The expression generating the property
     */
    public Definition(String name, Expression source) {
        this(name, source, null);
    }

    /**
     * Creates a new transformed property
     * 
     * @param name The property name
     * @param source The expression generating the property
     * @param binding The property type. Optional, the store will try to figure out the type from
     *        the expression in case it's missing
     */
    public Definition(String name, Expression source, Class binding) {
        this(name, source, binding, null);
    }

    /**
     * Creates a new transformed property
     * 
     * @param name The property name
     * @param source The expression generating the property
     * @param binding The property type. Optional, the store will try to figure out the type from
     *        the expression in case it's missing
     * @param crs The coordinate reference system of the property, to be used only for geometry
     *        properties
     */
    public Definition(String name, Expression source, Class binding, CoordinateReferenceSystem crs) {
        this.name = name;
        if (source == null) {
            this.expression = TransformFeatureSource.FF.property(name);
        } else {
            this.expression = source;
        }
        this.binding = binding;
        this.crs = crs;
    }

    public String getName() {
        return name;
    }

    public Expression getExpression() {
        return expression;
    }

    public Class getBinding() {
        return binding;
    }
    
    /**
     * Returns the inverse to this Definition, that is, the definition of the source attribute
     * corresponding to this computed attribute, if any. Only a small set of expression
     * are invertible in general, and a smaller subset of that can be inverted by this method.     
     * Implementor can override this method to provide a custom inversion logic.
     * 
     * @return The inverse of this definition, or null if not invertible or if the inversion
     *         logic for the specified case is missing
     */
    public List<Definition> inverse() {
        if(expression instanceof PropertyName) {
            PropertyName pn = (PropertyName) expression;
            return Arrays.asList(new Definition(pn.getPropertyName(), FF.property(name)));
        } 
        
        // add a Point(x,y) function and then have it be split into x,y here (two definitions)
        
        // TODO: look into algebraic inversion (e.g y = x + 3 -> x = y  - 3) and into creating a concept
        // of invertible function (which would work, with a bit of a loss in precision, 
        // for some math functions for example)
        
        return null;
    }

    /**
     * Computes the output attribute descriptor for this {@link Definition} given a sample feature
     * of the original feature type. The code will first attempt a static analysis on the original
     * feature type, if that fails it will try to evaluate the expression on the sample feature.
     * 
     * @param originalFeature
     * @return
     */
    public AttributeDescriptor getAttributeDescriptor(SimpleFeature originalFeature) {
        AttributeTypeBuilder ab = new AttributeTypeBuilder();
        SimpleFeatureType original = originalFeature.getFeatureType();
        ExpressionTypeEvaluator typeEvaluator = new ExpressionTypeEvaluator(original);

        if (binding != null) {

            CoordinateReferenceSystem computedCRS = crs;
            if (Geometry.class.isAssignableFrom(binding) && computedCRS == null) {
                computedCRS = evaluateCRS(originalFeature);
            }

            ab.setBinding(binding);
            ab.setName(name);
            if (computedCRS != null) {
                ab.setCRS(computedCRS);
            }

            return ab.buildDescriptor(name);
        } else {
            // in case no evaluation succeeds
            Class computedBinding = Object.class;

            // see if we are just passing a property trough
            if (expression instanceof PropertyName) {
                PropertyName pn = (PropertyName) expression;
                AttributeDescriptor descriptor = original.getDescriptor(pn.getPropertyName());

                if (descriptor == null) {
                    throw new IllegalArgumentException(
                            "Original feature type does not have a property named " + name);
                } else {
                    ab.init(descriptor);
                    ab.setName(name);
                    return ab.buildDescriptor(name);
                }
            } else {
                // try static analysis
                computedBinding = (Class) expression.accept(typeEvaluator, null);

                if (computedBinding == null) {
                    // all right, let's try the sample feature then
                    Object result = expression.evaluate(originalFeature);
                    if (result != null) {
                        computedBinding = result.getClass();
                    }
                }

                CoordinateReferenceSystem computedCRS = crs;
                if (Geometry.class.isAssignableFrom(computedBinding) && computedCRS == null) {
                    computedCRS = evaluateCRS(originalFeature);
                }

                ab.setBinding(computedBinding);
                ab.setName(name);
                if (computedCRS != null) {
                    ab.setCRS(computedCRS);
                }

                return ab.buildDescriptor(name);
            }
        }
    }

    private CoordinateReferenceSystem evaluateCRS(SimpleFeature originalFeature) {
        SimpleFeatureType originalSchema = originalFeature.getFeatureType();
        CoordinateReferenceSystem computedCRS;
        // try static analysis
        computedCRS = (CoordinateReferenceSystem) expression.accept(
                new CRSEvaluator(originalSchema), null);
        if (computedCRS == null) {
            // all right, let's try the sample feature then
            Geometry g = expression.evaluate(originalFeature, Geometry.class);
            if (g != null && g.getUserData() instanceof CoordinateReferenceSystem) {
                computedCRS = (CoordinateReferenceSystem) g.getUserData();
            } else {
                try {
                    computedCRS = CRS.decode("EPSG:" + g.getSRID());
                } catch (Exception e) {
                    return null;
                }
            }
        }
        return computedCRS;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((binding == null) ? 0 : binding.hashCode());
        result = prime * result + ((crs == null) ? 0 : crs.hashCode());
        result = prime * result + ((expression == null) ? 0 : expression.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        Definition other = (Definition) obj;
        if (binding == null) {
            if (other.binding != null)
                return false;
        } else if (!binding.equals(other.binding))
            return false;
        if (crs == null) {
            if (other.crs != null)
                return false;
        } else if (!crs.equals(other.crs))
            return false;
        if (expression == null) {
            if (other.expression != null)
                return false;
        } else if (!expression.equals(other.expression))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Definition [name=" + name + ", binding=" + binding
                + ", crs=" + crs + ", expression=" + expression + "]";
    }
    
    

}