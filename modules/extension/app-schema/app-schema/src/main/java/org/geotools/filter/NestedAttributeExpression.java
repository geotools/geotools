/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2009-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.geotools.data.complex.AppSchemaDataAccessRegistry;
import org.geotools.data.complex.AttributeMapping;
import org.geotools.data.complex.FeatureTypeMapping;
import org.geotools.data.complex.NestedAttributeMapping;
import org.geotools.data.complex.filter.XPath;
import org.geotools.data.complex.filter.XPathUtil.Step;
import org.geotools.data.complex.filter.XPathUtil.StepList;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.Types;
import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.xlink.XLINK;
import org.opengis.feature.Attribute;
import org.opengis.feature.Feature;
import org.opengis.feature.type.Name;
import org.opengis.filter.expression.Expression;

/**
 * This class represents a list of expressions broken up from a single XPath expression that is
 * nested in more than one feature. The purpose is to allow filtering these attributes on the parent
 * feature.
 *
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering)
 *
 *
 *
 *
 * @source $URL$
 *         http://svn.osgeo.org/geotools/trunk/modules/unsupported/app-schema/app-schema/src/main
 *         /java/org/geotools/filter/NestedAttributeExpression.java $
 */
public class NestedAttributeExpression extends AttributeExpressionImpl {

    private NestedAttributeMapping rootMapping;
    
    private StepList fullSteps;

    /**
     * First constructor
     *
     * @param xpath
     *            Attribute XPath
     * @param expressions
     *            List of broken up expressions
     */
    public NestedAttributeExpression(StepList xpath, NestedAttributeMapping nestedMapping) {
        super(xpath.toString());
        this.rootMapping = nestedMapping;
        this.fullSteps = xpath;
    }

    /**
     * see {@link org.geotools.filter.AttributeExpressionImpl#evaluate(Object)}
     */
    @Override
    public Object evaluate(Object object) {
        if (object == null) {
            return null;
        }

        // only simple/complex features are supported
        if (!(object instanceof Feature)) {
            throw new UnsupportedOperationException(
                    "Expecting a feature to apply filter, but found: " + object);
        }
        
        return getValues(((Feature)object), rootMapping, fullSteps);
    }
    
    private List<Object> getValues(Feature feature, NestedAttributeMapping nestedMapping,
            StepList steps) {
        List<Object> values = new ArrayList<Object>();
        FeatureTypeMapping nextFMapping;
        try {
            nextFMapping = nestedMapping.getFeatureTypeMapping(feature);
        } catch (IOException e) {
            nextFMapping = null;
        }
        if (nextFMapping == null) {
            // throw error unless this is polymorphism
            if (nestedMapping.isConditional()) {
                return values;
            }
            throw new UnsupportedOperationException("FeatureTypeMapping not found for " + attPath
                    + ". Please revise PropertyName in your filter!");
        }
        List<Feature> nestedFeatures = new ArrayList<Feature>();
        if (nestedMapping.isSameSource()) {
            // same root/database row, different mappings, used in
            // polymorphism
            nestedFeatures = new ArrayList<Feature>();
            nestedFeatures.add(feature);
        } else {
            // get nested features
            try {
                nestedFeatures = getNestedFeatures(feature, nestedMapping, nextFMapping);
            } catch (IOException e) {
                throw new RuntimeException("Failed evaluating filter expression: '" + attPath
                        + "'. Caused by: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                // might be a polymorphic case where it's looking for an attribute
                // from another type
                // that doesn't match this, but might match another database row
                // so just continue
                return values;
            }
        }

        boolean isClientProperty = isClientProperty(steps);
        StepList newSteps = null;
        if (isClientProperty) {
            // check for client properties for this mapping
            newSteps = steps.subList(0, steps.size() - 1);
            if (newSteps.size() == 1) {
                // special case for client property for this NestedAttributeMapping
                for (Feature f : nestedFeatures) {
                    values.addAll(getClientProperties(nestedMapping, f));
                }
            }
        }        
        // skip element name that is mapped at the next FeatureTypeMapping
        // except when it's a simple content
        // if simple content, then there will be no type name in the xpath, e.g. when gml:name
        // is
        // feature chained the path stays as gml:name.. but if it's a complex type with complex
        // content, e.g. gsml:specification the path will be
        // gsml:specification/gsml:GeologicUnit/<some leaf attribute to filter by>        
        Name nextElementName = nextFMapping.getTargetFeature().getName();
        // starting index for the next search
        int startPos = -1;            
        if (Types.equals(nextElementName, steps.get(0).getName())) {
            // simple contents where nested element name is the same as the nesting element
            startPos = 0;
        } else {
            Step elementNameStep = steps.get(nestedMapping.getTargetXPath().size());
            // support polymorphism
            // check that element type matches the steps
            if (Types.equals(nextElementName, elementNameStep.getName())) {
                startPos = nestedMapping.getTargetXPath().size();
                if (steps.size() > startPos + 1 && !steps.get(startPos + 1).isXmlAttribute()) {
                    // skip next element name for next evaluation
                    // except if the next step is a client property for that element name
                    // since we'd need the AttributeMapping for the client property
                    startPos++;
                }
            }
        }
        if (startPos > -1) {
            newSteps = steps.subList(startPos, steps.size());
            if (!newSteps.isEmpty()) {
                List<NestedAttributeMapping> nestedMappings = nextFMapping.getNestedMappings();
                if (!nestedMappings.isEmpty()) {
                    for (NestedAttributeMapping mapping : nestedMappings) {
                        if (newSteps.startsWith(mapping.getTargetXPath())) {
                            for (Feature f : nestedFeatures) {
                                // loop to this method
                                values.addAll(getValues(f, mapping, newSteps));
                            }
                        }
                    }
                }
                if (isClientProperty) {
                    // check for client properties
                    newSteps = newSteps.subList(0, newSteps.size() - 1);
                }
                boolean isXlinkHref = isClientProperty && isXlinkHref(steps);

                List<AttributeMapping> attMappings = nextFMapping
                        .getAttributeMappingsIgnoreIndex(newSteps);
                for (AttributeMapping attMapping : attMappings) {
                    if (isClientProperty) {
                        if (!(isXlinkHref && attMapping instanceof NestedAttributeMapping)) {
                            // if it's an xlink href,
                            // ignore nested attribute mappings as the values should come from
                            // nested features.. so should be evaluated at the next call
                            for (Feature f : nestedFeatures) {
                                values.addAll(getClientProperties(attMapping, f));
                            }
                        }
                    } else {
                        for (Feature f : nestedFeatures) {
                            values.add(getValue(attMapping.getSourceExpression(), f));
                        }
                    }
                }
            }
        }
        return values;
    }
    
    private boolean isXlinkHref(StepList steps) {
        if (steps.isEmpty()) {
            return false;
        }
        // special case for xlink:href by feature chaining
        // must get the value from the nested attribute mapping instead, i.e. from another table
        // if it's to get the values from the local table, it shouldn't be set with feature chaining
        return steps.get(steps.size() - 1).getName().equals(XLINK.HREF);
    }
    
    private boolean isClientProperty(StepList steps) {
        if (steps.isEmpty()) {
            return false;
        }
        return steps.get(steps.size() - 1).isXmlAttribute();
    }
    
    private List<Object> getClientProperties(AttributeMapping attMapping, Feature f) {
        List<Object> values = new ArrayList<Object>();
        Step lastStep = getLastStep();        
        Expression exp = getClientPropertyExpression(attMapping, lastStep);
        if (exp != null) {
            Object value = getValue(exp, f);
            if (value != null) {
                values.add(value);
            }            
        }
        return values;
    }
    
    private Step getLastStep() {
        return fullSteps.get(fullSteps.size() - 1);
    }

    /**
     * Get nested features from a feature chaining attribute mapping
     *
     * @param root
     *            Root feature being evaluated
     * @param nestedMapping
     *            Attribute mapping for nested features
     * @param fMapping
     *            The root feature type mapping
     * @return list of nested features
     * @throws IOException
     */
    private List<Feature> getNestedFeatures(Feature root, NestedAttributeMapping nestedMapping, FeatureTypeMapping fMapping) throws IOException {
        Object fTypeName = nestedMapping.getNestedFeatureType(root);
        if (fTypeName == null || !(fTypeName instanceof Name)) {
            return null;
        }
        boolean hasSimpleFeatures = AppSchemaDataAccessRegistry.hasName((Name) fTypeName);
        // get foreign key
        Object val = getValue(nestedMapping.getSourceExpression(), root);
        if (val == null) {
            return null;
        }
        if (hasSimpleFeatures) {
            // normal app-schema mapping
            return nestedMapping.getInputFeatures(val, fMapping);
        } else {
            // app-schema with a complex feature source
            return nestedMapping.getFeatures(val, null, root, 0, null);
        }
    }

    private Object getValue(Expression expression, Feature feature) {
        try {
            Object value = expression.evaluate(feature);
            return extractAttributeValue(value);
        } catch (IllegalArgumentException e) {
            // if the field doesn't exist in the feature
            // i.e. if it's polymorphic
            return null;
        }
    }

    /**
     * Extract the value that might be wrapped in an attribute. If the value is a collection, gets
     * the first value.
     *
     * @param value
     * @return
     */
    @SuppressWarnings("rawtypes")
    private Object extractAttributeValue(Object value) {
        if (value == null) {
            return null;
        }

        while (value instanceof Attribute) {
            // get real value
            value = ((Attribute) value).getValue();
        }
        if (value == null) {
            return null;
        }
        if (value instanceof Collection) {
            if (((Collection) value).isEmpty()) {
                return null;
            }
            value = ((Collection) value).iterator().next();
            while (value instanceof Attribute) {
                value = ((Attribute) value).getValue();
            }
        }
        return value;
    }   

    /**
     * Find the source expression if the step is a client property.
     *
     * @param nextRootStep
     *            the step
     * @param fMapping
     *            feature type mapping to get namespaces from
     * @param mapping
     *            attribute mapping
     * @param targetXPath
     *            the full target xpath
     * @return
     */
    
    private Expression getClientPropertyExpression(AttributeMapping mapping, Step lastStep) {
        Expression exp = null;
        if (lastStep.isXmlAttribute()) {
            Map<Name, Expression> clientProperties = mapping.getClientProperties();
            QName lastStepQName = lastStep.getName();
            Name lastStepName;
            if (lastStepQName.getPrefix() != null
                    && lastStepQName.getPrefix().length() > 0
                    && (lastStepQName.getNamespaceURI() == null || lastStepQName.getNamespaceURI()
                            .length() == 0)) {
                String prefix = lastStepQName.getPrefix();
                String uri = namespaceSupport.getURI(prefix);
                lastStepName = Types.typeName(uri, lastStepQName.getLocalPart());
            } else {
                lastStepName = Types.toTypeName(lastStepQName);
            }
            if (clientProperties.containsKey(lastStepName)) {
                // end NC - added
                exp = (Expression) clientProperties.get(lastStepName);
            } else if (XPath.isId(lastStep)) {
                if (mapping.getIdentifierExpression() == Expression.NIL) {
                    // no specific attribute mapping or that idExpression is not mapped
                    // use primary key
                    exp = CommonFactoryFinder.getFilterFactory(null).property("@id");
                } else {
                    exp = mapping.getIdentifierExpression();
                }    
            }
        }
        return exp;
    }
    
}
