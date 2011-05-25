/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.v1_0;

import org.eclipse.xsd.util.XSDSchemaLocationResolver;
import org.picocontainer.MutablePicoContainer;
import org.opengis.filter.FilterFactory;
import org.geotools.filter.FilterFactoryImpl;
import org.geotools.gml2.GMLConfiguration;
import org.geotools.xml.Configuration;


/**
 * Parser configuration for the filter 1.0 schema.
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 *
 * @source $URL$
 */
public class OGCConfiguration extends Configuration {
    /**
     * Adds a dependency on {@link GMLConfiguration}
     */
    public OGCConfiguration() {
        super(OGC.getInstance());
        addDependency(new GMLConfiguration());
    }

    protected void registerBindings(MutablePicoContainer container) {
        //expr.xsd
        container.registerComponentImplementation(OGC.Add, OGCAddBinding.class);
        container.registerComponentImplementation(OGC.BinaryOperatorType,
            OGCBinaryOperatorTypeBinding.class);
        container.registerComponentImplementation(OGC.Div, OGCDivBinding.class);
        container.registerComponentImplementation(OGC.ExpressionType, OGCExpressionTypeBinding.class);
        container.registerComponentImplementation(OGC.FunctionType, OGCFunctionTypeBinding.class);
        container.registerComponentImplementation(OGC.LiteralType, OGCLiteralTypeBinding.class);
        container.registerComponentImplementation(OGC.Mul, OGCMulBinding.class);
        container.registerComponentImplementation(OGC.PropertyNameType,
            OGCPropertyNameTypeBinding.class);
        container.registerComponentImplementation(OGC.Sub, OGCSubBinding.class);

        //filter.xsd
        container.registerComponentImplementation(OGC.And, OGCAndBinding.class);
        container.registerComponentImplementation(OGC.BBOXType, OGCBBOXTypeBinding.class);
        container.registerComponentImplementation(OGC.Beyond, OGCBeyondBinding.class);
        container.registerComponentImplementation(OGC.BinaryComparisonOpType,
            OGCBinaryComparisonOpTypeBinding.class);
        container.registerComponentImplementation(OGC.BinaryLogicOpType,
            OGCBinaryLogicOpTypeBinding.class);
        container.registerComponentImplementation(OGC.BinarySpatialOpType,
            OGCBinarySpatialOpTypeBinding.class);
        container.registerComponentImplementation(OGC.Contains, OGCContainsBinding.class);
        container.registerComponentImplementation(OGC.Crosses, OGCCrossesBinding.class);
        container.registerComponentImplementation(OGC.Disjoint, OGCDisjointBinding.class);
        //container.registerComponentImplementation(OGC.COMPARISONOPSTYPE,OGCComparisonOpsTypeBinding.class);
        container.registerComponentImplementation(OGC.DistanceBufferType,
            OGCDistanceBufferTypeBinding.class);
        container.registerComponentImplementation(OGC.DistanceType, OGCDistanceTypeBinding.class);
        container.registerComponentImplementation(OGC.DWithin, OGCDWithinBinding.class);
        container.registerComponentImplementation(OGC.Equals, OGCEqualsBinding.class);
        container.registerComponentImplementation(OGC.FeatureIdType, OGCFeatureIdTypeBinding.class);
        container.registerComponentImplementation(OGC.FilterType, OGCFilterTypeBinding.class);
        //container.registerComponentImplementation(OGC.LOGICOPSTYPE,OGCLogicOpsTypeBinding.class);
        container.registerComponentImplementation(OGC.Intersects, OGCIntersectsBinding.class);
        container.registerComponentImplementation(OGC.LowerBoundaryType,
            OGCLowerBoundaryTypeBinding.class);
        container.registerComponentImplementation(OGC.Not, OGCNotBinding.class);
        container.registerComponentImplementation(OGC.Or, OGCOrBinding.class);
        container.registerComponentImplementation(OGC.PropertyIsBetweenType,
            OGCPropertyIsBetweenTypeBinding.class);
        container.registerComponentImplementation(OGC.PropertyIsEqualTo,
            OGCPropertyIsEqualToBinding.class);
        container.registerComponentImplementation(OGC.PropertyIsGreaterThan,
            OGCPropertyIsGreaterThanBinding.class);
        container.registerComponentImplementation(OGC.PropertyIsGreaterThanOrEqualTo,
            OGCPropertyIsGreaterThanOrEqualToBinding.class);
        container.registerComponentImplementation(OGC.PropertyIsLessThan,
            OGCPropertyIsLessThanBinding.class);
        container.registerComponentImplementation(OGC.PropertyIsLessThanOrEqualTo,
            OGCPropertyIsLessThanOrEqualToBinding.class);
        container.registerComponentImplementation(OGC.PropertyIsLikeType,
            OGCPropertyIsLikeTypeBinding.class);
        container.registerComponentImplementation(OGC.PropertyIsNullType,
            OGCPropertyIsNullTypeBinding.class);
        container.registerComponentImplementation(OGC.PropertyIsNotEqualTo,
            OGCPropertyIsNotEqualToBinding.class);
        container.registerComponentImplementation(OGC.Overlaps, OGCOverlapsBinding.class);
        container.registerComponentImplementation(OGC.Touches, OGCTouchesBinding.class);
        //container.registerComponentImplementation(OGC.SPATIALOPSTYPE,OGCSpatialOpsTypeBinding.class);
        //container.registerComponentImplementation(OGC.UnaryLogicOpType,
        //    OGCUnaryLogicOpTypeBinding.class);
        container.registerComponentImplementation(OGC.UpperBoundaryType,
            OGCUpperBoundaryTypeBinding.class);
        container.registerComponentImplementation(OGC.Within, OGCWithinBinding.class);
    }

    /**
     * Configures the filter context.
     * <p>
     * The following factories are registered:
     * <ul>
     * <li>{@link FilterFactoryImpl} under {@link FilterFactory}
     * </ul>
     * </p>
     */
    public void configureContext(MutablePicoContainer container) {
        super.configureContext(container);

        container.registerComponentImplementation(FilterFactory.class, FilterFactoryImpl.class);
    }
}
