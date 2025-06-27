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
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.util.InternationalString;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Geometry;

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

    InternationalString description;

    static final FilterFactory FF = CommonFactoryFinder.getFilterFactory();

    /**
     * Creates a new transformed property that mirrors 1-1 an existing property in the source type, without even
     * renaming it
     *
     * @param name The property name
     */
    public Definition(String name) {
        this(name, null, null);
    }
    /**
     * Creates a new property definition with a description
     *
     * @param name The property name
     * @param description The property description
     */
    public Definition(String name, InternationalString description) {
        this(name, null, null, null, description);
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
     * @param binding The property type. Optional, the store will try to figure out the type from the expression in case
     *     it's missing
     */
    public Definition(String name, Expression source, Class binding) {
        this(name, source, binding, null);
    }

    /**
     * Creates a new transformed property
     *
     * @param name The property name
     * @param source The expression generating the property
     * @param binding The property type. Optional, the store will try to figure out the type from the expression in case
     *     it's missing
     * @param crs The coordinate reference system of the property, to be used only for geometry properties
     */
    public Definition(String name, Expression source, Class binding, CoordinateReferenceSystem crs) {
        this(name, source, binding, crs, null);
    }

    /**
     * Creates a new transformed property
     *
     * @param name The property name
     * @param source The expression generating the property
     * @param binding The property type. Optional, the store will try to figure out the type from the expression in case
     *     it's missing
     * @param crs The coordinate reference system of the property, to be used only for geometry properties
     * @param description The property description
     */
    public Definition(
            String name,
            Expression source,
            Class binding,
            CoordinateReferenceSystem crs,
            InternationalString description) {
        this.name = name;
        if (source == null) {
            this.expression = TransformFeatureSource.FF.property(name);
        } else {
            this.expression = source;
        }
        this.binding = binding;
        this.crs = crs;
        this.description = description;
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

    public InternationalString getDescription() {
        return description;
    }

    /**
     * Returns the inverse to this Definition, that is, the definition of the source attribute corresponding to this
     * computed attribute, if any. Only a small set of expression are invertible in general, and a smaller subset of
     * that can be inverted by this method. Implementor can override this method to provide a custom inversion logic.
     *
     * @return The inverse of this definition, or null if not invertible or if the inversion logic for the specified
     *     case is missing
     */
    public List<Definition> inverse() {
        if (expression instanceof PropertyName) {
            PropertyName pn = (PropertyName) expression;
            return Arrays.asList(new Definition(pn.getPropertyName(), FF.property(name)));
        }

        // add a Point(x,y) function and then have it be split into x,y here (two definitions)

        // TODO: look into algebraic inversion (e.g y = x + 3 -> x = y  - 3) and into creating a
        // concept
        // of invertible function (which would work, with a bit of a loss in precision,
        // for some math functions for example)

        return null;
    }

    /**
     * Computes the output attribute descriptor for this {@link Definition} given a sample feature of the original
     * feature type. The code will first attempt a static analysis on the original feature type, if that fails it will
     * try to evaluate the expression on the sample feature.
     */
    public AttributeDescriptor getAttributeDescriptor(SimpleFeature originalFeature) {
        // try the static analysis
        AttributeDescriptor ad = getAttributeDescriptor(originalFeature.getFeatureType());
        if (ad != null) {
            return ad;
        }

        // build it from the sample then
        AttributeTypeBuilder ab = new AttributeTypeBuilder();
        Object result = expression.evaluate(originalFeature);
        Class computedBinding = null;
        if (result != null) {
            computedBinding = result.getClass();
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

        if (description != null) {
            ab.setDescription(description);
        }

        return ab.buildDescriptor(name);
    }

    /**
     * Computes the output attribute descriptor for this {@link Definition} given only the original feature type. The
     * code will attempt a static analysis on the original feature type, if that fails it will return null
     */
    public AttributeDescriptor getAttributeDescriptor(SimpleFeatureType originalSchema) {
        AttributeTypeBuilder ab = new AttributeTypeBuilder();
        ExpressionTypeEvaluator typeEvaluator = new ExpressionTypeEvaluator(originalSchema);

        if (description != null) {
            ab.setDescription(description);
        }

        if (binding != null) {

            ab.setBinding(binding);
            ab.setName(name);
            if (crs != null) {
                ab.setCRS(crs);
            } else {
                // try to get it from the expression operands, under the assumption that
                // the geometry crs are not getting modified by the filter functions
                expression.accept(typeEvaluator, null);
                ab.setCRS(typeEvaluator.getCoordinateReferenceSystem());
            }

            return ab.buildDescriptor(name);
        } else {

            // see if we are just passing a property trough
            if (expression instanceof PropertyName) {
                PropertyName pn = (PropertyName) expression;
                AttributeDescriptor descriptor = originalSchema.getDescriptor(pn.getPropertyName());

                if (descriptor == null) {
                    throw new IllegalArgumentException("Original feature type does not have a property named " + name);
                } else {
                    ab.init(descriptor);
                    ab.setName(name);
                    if (description != null) {
                        ab.setDescription(description);
                    }
                    return ab.buildDescriptor(name);
                }
            } else {
                // try static analysis
                Class computedBinding = (Class) expression.accept(typeEvaluator, null);
                if (computedBinding == null) {
                    return null;
                }

                CoordinateReferenceSystem computedCRS = crs;
                if (Geometry.class.isAssignableFrom(computedBinding) && computedCRS == null) {
                    computedCRS = evaluateCRS(originalSchema);
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
        CoordinateReferenceSystem computedCRS = evaluateCRS(originalSchema);
        if (computedCRS == null) {
            // all right, let's try the sample feature then
            Geometry g = expression.evaluate(originalFeature, Geometry.class);
            if (g != null && g.getUserData() instanceof CoordinateReferenceSystem) {
                computedCRS = (CoordinateReferenceSystem) g.getUserData();
            } else if (g != null) {
                try {
                    computedCRS = CRS.decode("EPSG:" + g.getSRID());
                } catch (Exception e) {
                    return null;
                }
            }
        }
        return computedCRS;
    }

    private CoordinateReferenceSystem evaluateCRS(SimpleFeatureType originalSchema) {
        return (CoordinateReferenceSystem) expression.accept(new CRSEvaluator(originalSchema), null);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (binding == null ? 0 : binding.hashCode());
        result = prime * result + (crs == null ? 0 : crs.hashCode());
        result = prime * result + (expression == null ? 0 : expression.hashCode());
        result = prime * result + (name == null ? 0 : name.hashCode());
        result = prime * result + (description == null ? 0 : description.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Definition other = (Definition) obj;
        if (binding == null) {
            if (other.binding != null) return false;
        } else if (!binding.equals(other.binding)) return false;
        if (crs == null) {
            if (other.crs != null) return false;
        } else if (!crs.equals(other.crs)) return false;
        if (expression == null) {
            if (other.expression != null) return false;
        } else if (!expression.equals(other.expression)) return false;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        if (description == null) {
            if (other.description != null) return false;
        } else if (!description.equals(other.description)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "Definition [name="
                + name
                + ", binding="
                + binding
                + ", crs="
                + crs
                + ", expression="
                + expression
                + ", description="
                + description
                + "]";
    }
}
