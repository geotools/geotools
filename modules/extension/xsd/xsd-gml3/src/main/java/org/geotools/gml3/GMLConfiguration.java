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
package org.geotools.gml3;

import javax.xml.namespace.QName;

import org.geotools.gml2.FeatureTypeCache;
import org.geotools.gml2.bindings.GMLCoordTypeBinding;
import org.geotools.gml2.bindings.GMLCoordinatesTypeBinding;
import org.geotools.gml3.bindings.AbstractFeatureCollectionTypeBinding;
import org.geotools.gml3.bindings.AbstractFeatureTypeBinding;
import org.geotools.gml3.bindings.AbstractGeometryTypeBinding;
import org.geotools.gml3.bindings.AbstractRingPropertyTypeBinding;
import org.geotools.gml3.bindings.BoundingShapeTypeBinding;
import org.geotools.gml3.bindings.ComplexSupportXSAnyTypeBinding;
import org.geotools.gml3.bindings.CurveArrayPropertyTypeBinding;
import org.geotools.gml3.bindings.CurvePropertyTypeBinding;
import org.geotools.gml3.bindings.CurveSegmentArrayPropertyTypeBinding;
import org.geotools.gml3.bindings.CurveTypeBinding;
import org.geotools.gml3.bindings.DirectPositionListTypeBinding;
import org.geotools.gml3.bindings.DirectPositionTypeBinding;
import org.geotools.gml3.bindings.DoubleListBinding;
import org.geotools.gml3.bindings.EnvelopeTypeBinding;
import org.geotools.gml3.bindings.FeatureArrayPropertyTypeBinding;
import org.geotools.gml3.bindings.FeaturePropertyTypeBinding;
import org.geotools.gml3.bindings.GML3EncodingUtils;
import org.geotools.gml3.bindings.GeometryPropertyTypeBinding;
import org.geotools.gml3.bindings.IntegerListBinding;
import org.geotools.gml3.bindings.LineStringPropertyTypeBinding;
import org.geotools.gml3.bindings.LineStringSegmentTypeBinding;
import org.geotools.gml3.bindings.LineStringTypeBinding;
import org.geotools.gml3.bindings.LinearRingPropertyTypeBinding;
import org.geotools.gml3.bindings.LinearRingTypeBinding;
import org.geotools.gml3.bindings.LocationPropertyTypeBinding;
import org.geotools.gml3.bindings.MeasureTypeBinding;
import org.geotools.gml3.bindings.MultiCurvePropertyTypeBinding;
import org.geotools.gml3.bindings.MultiCurveTypeBinding;
import org.geotools.gml3.bindings.MultiGeometryPropertyTypeBinding;
import org.geotools.gml3.bindings.MultiGeometryTypeBinding;
import org.geotools.gml3.bindings.MultiLineStringPropertyTypeBinding;
import org.geotools.gml3.bindings.MultiLineStringTypeBinding;
import org.geotools.gml3.bindings.MultiPointPropertyTypeBinding;
import org.geotools.gml3.bindings.MultiPointTypeBinding;
import org.geotools.gml3.bindings.MultiPolygonPropertyTypeBinding;
import org.geotools.gml3.bindings.MultiPolygonTypeBinding;
import org.geotools.gml3.bindings.MultiSurfacePropertyTypeBinding;
import org.geotools.gml3.bindings.MultiSurfaceTypeBinding;
import org.geotools.gml3.bindings.NullTypeBinding;
import org.geotools.gml3.bindings.PointArrayPropertyTypeBinding;
import org.geotools.gml3.bindings.PointPropertyTypeBinding;
import org.geotools.gml3.bindings.PointTypeBinding;
import org.geotools.gml3.bindings.PolygonPatchTypeBinding;
import org.geotools.gml3.bindings.PolygonPropertyTypeBinding;
import org.geotools.gml3.bindings.PolygonTypeBinding;
import org.geotools.gml3.bindings.ReferenceTypeBinding;
import org.geotools.gml3.bindings.SurfaceArrayPropertyTypeBinding;
import org.geotools.gml3.bindings.SurfacePropertyTypeBinding;
import org.geotools.gml3.bindings.SurfaceTypeBinding;
import org.geotools.gml3.smil.SMIL20Configuration;
import org.geotools.gml3.smil.SMIL20LANGConfiguration;
import org.geotools.xlink.XLINKConfiguration;
import org.geotools.xml.Configuration;
import org.geotools.xml.Parser;
import org.geotools.xs.XS;
import org.picocontainer.MutablePicoContainer;

import com.vividsolutions.jts.geom.CoordinateSequenceFactory;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequenceFactory;
import org.geotools.gml3.bindings.ArcTypeBinding;
import org.geotools.gml3.bindings.CircleTypeBinding;
import org.geotools.gml3.bindings.RingTypeBinding;
import org.geotools.gml3.bindings.SurfacePatchArrayPropertyTypeBinding;


/**
 * Parser configuration for the gml3 schema.
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 * @source $URL$
 */
public class GMLConfiguration extends Configuration {
    
    /**
     * Boolean property which controls whether encoded features should include bounds.
     */
    public static final QName NO_FEATURE_BOUNDS = org.geotools.gml2.GMLConfiguration.NO_FEATURE_BOUNDS;
    
    /**
     * Boolean property which controls whether the FeatureCollection should be encoded with multiple featureMember
     * as opposed to a single featureMembers 
     */
    public static final QName ENCODE_FEATURE_MEMBER = org.geotools.gml2.GMLConfiguration.ENCODE_FEATURE_MEMBER;

    /**
     * extended support for arcs and surface flag
     */
    boolean extArcSurfaceSupport = false;
    
    public GMLConfiguration() {
        this(false);
    }
    
    public GMLConfiguration(boolean extArcSurfaceSupport) {
        super(GML.getInstance());

        this.extArcSurfaceSupport = extArcSurfaceSupport;
        
        //add xlink cdependency
        addDependency(new XLINKConfiguration());

        //add smil depenedncy
        addDependency(new SMIL20Configuration());
        addDependency(new SMIL20LANGConfiguration());

        //add parser properties
        getProperties().add(Parser.Properties.PARSE_UNKNOWN_ELEMENTS);
        getProperties().add(Parser.Properties.PARSE_UNKNOWN_ATTRIBUTES);
    }

    /**
     * Flag that when set triggers extended support for arcs and surfaces.
     */
    public void setExtendedArcSurfaceSupport(boolean arcSurfaceSupport) {
        this.extArcSurfaceSupport = arcSurfaceSupport;
    }
    
    public boolean isExtendedArcSurfaceSupport() {
        return extArcSurfaceSupport;
    }
    
    protected void registerBindings(MutablePicoContainer container) {
        //Types
        container.registerComponentImplementation(GML.AbstractFeatureType,
            AbstractFeatureTypeBinding.class);
        container.registerComponentImplementation(GML.AbstractFeatureCollectionType,
            AbstractFeatureCollectionTypeBinding.class);
        container.registerComponentImplementation(GML.AbstractGeometryType,
            AbstractGeometryTypeBinding.class);
        container.registerComponentImplementation(GML.AbstractRingPropertyType,
            AbstractRingPropertyTypeBinding.class);
        container.registerComponentImplementation(GML.BoundingShapeType,
            BoundingShapeTypeBinding.class);
        //container.registerComponentImplementation(GML.COORDINATESTYPE,CoordinatesTypeBinding.class);
        container.registerComponentImplementation(GML.CoordinatesType,
            GMLCoordinatesTypeBinding.class);
        //container.registerComponentImplementation(GML.COORDTYPE,CoordTypeBinding.class);
        container.registerComponentImplementation(GML.CoordType, GMLCoordTypeBinding.class);
        container.registerComponentImplementation(GML.CurveArrayPropertyType,
            CurveArrayPropertyTypeBinding.class);
        container.registerComponentImplementation(GML.CurveType, CurveTypeBinding.class);
        container.registerComponentImplementation(GML.CurvePropertyType,
            CurvePropertyTypeBinding.class);
        container.registerComponentImplementation(GML.CurveSegmentArrayPropertyType,
            CurveSegmentArrayPropertyTypeBinding.class);
        container.registerComponentImplementation(GML.DirectPositionListType,
            DirectPositionListTypeBinding.class);
        container.registerComponentImplementation(GML.DirectPositionType,
            DirectPositionTypeBinding.class);
        container.registerComponentImplementation(GML.doubleList, DoubleListBinding.class);
        container.registerComponentImplementation(GML.EnvelopeType, EnvelopeTypeBinding.class);
        container.registerComponentImplementation(GML.FeatureArrayPropertyType,
            FeatureArrayPropertyTypeBinding.class);
        container.registerComponentImplementation(GML.FeaturePropertyType,
            FeaturePropertyTypeBinding.class);
        container.registerComponentImplementation(GML.GeometryPropertyType,
            GeometryPropertyTypeBinding.class);
        container.registerComponentImplementation(GML.integerList, IntegerListBinding.class);
        container.registerComponentImplementation(GML.LinearRingPropertyType,
            LinearRingPropertyTypeBinding.class);
        container.registerComponentImplementation(GML.LinearRingType, LinearRingTypeBinding.class);
        container.registerComponentImplementation(GML.LineStringPropertyType,
            LineStringPropertyTypeBinding.class);
        container.registerComponentImplementation(GML.LineStringSegmentType,
            LineStringSegmentTypeBinding.class);
        container.registerComponentImplementation(GML.LineStringType, LineStringTypeBinding.class);
        container.registerComponentImplementation(GML.LocationPropertyType,
            LocationPropertyTypeBinding.class);

        container.registerComponentImplementation(GML.MeasureType, MeasureTypeBinding.class);
        container.registerComponentImplementation(GML.MultiCurveType, MultiCurveTypeBinding.class);
        container.registerComponentImplementation(GML.MultiCurvePropertyType,
            MultiCurvePropertyTypeBinding.class);
        container.registerComponentImplementation(GML.MultiGeometryType, MultiGeometryTypeBinding.class);
        container.registerComponentImplementation(GML.MultiGeometryPropertyType, MultiGeometryPropertyTypeBinding.class);
        container.registerComponentImplementation(GML.MultiLineStringPropertyType,
            MultiLineStringPropertyTypeBinding.class);
        container.registerComponentImplementation(GML.MultiLineStringType,
            MultiLineStringTypeBinding.class);
        container.registerComponentImplementation(GML.MultiPointPropertyType,
            MultiPointPropertyTypeBinding.class);
        container.registerComponentImplementation(GML.MultiPointType, MultiPointTypeBinding.class);
        container.registerComponentImplementation(GML.MultiPolygonPropertyType,
            MultiPolygonPropertyTypeBinding.class);
        container.registerComponentImplementation(GML.MultiPolygonType,
            MultiPolygonTypeBinding.class);
        container.registerComponentImplementation(GML.MultiSurfaceType,
            MultiSurfaceTypeBinding.class);
        container.registerComponentImplementation(GML.MultiSurfacePropertyType,
            MultiSurfacePropertyTypeBinding.class);
        container.registerComponentImplementation(GML.NullType,
                NullTypeBinding.class);
        container.registerComponentImplementation(GML.PointArrayPropertyType,
            PointArrayPropertyTypeBinding.class);
        container.registerComponentImplementation(GML.PointPropertyType,
            PointPropertyTypeBinding.class);
        container.registerComponentImplementation(GML.PointType, PointTypeBinding.class);
        container.registerComponentImplementation(GML.PolygonPatchType,
                PolygonPatchTypeBinding.class);
        container.registerComponentImplementation(GML.PolygonPropertyType,
            PolygonPropertyTypeBinding.class);
        container.registerComponentImplementation(GML.PolygonType, PolygonTypeBinding.class);
        container.registerComponentImplementation(GML.ReferenceType, ReferenceTypeBinding.class);
        container.registerComponentImplementation(GML.SurfaceArrayPropertyType,
            SurfaceArrayPropertyTypeBinding.class);
        container.registerComponentImplementation(GML.SurfacePropertyType,
            SurfacePropertyTypeBinding.class);
        container.registerComponentImplementation(GML.SurfaceType, SurfaceTypeBinding.class);
        container.registerComponentImplementation(XS.ANYTYPE, ComplexSupportXSAnyTypeBinding.class);
        
        //extended bindings for arc/surface support
        if (isExtendedArcSurfaceSupport()) {
            container.registerComponentImplementation(GML.ArcType,
                    ArcTypeBinding.class);
            container.registerComponentImplementation(GML.CircleType,
                    CircleTypeBinding.class);
            container.registerComponentImplementation(GML.RingType, RingTypeBinding.class);
            container.registerComponentImplementation(GML.SurfacePatchArrayPropertyType,
                    SurfacePatchArrayPropertyTypeBinding.class);
            container.registerComponentImplementation(GML.CurveArrayPropertyType, 
                    org.geotools.gml3.bindings.ext.CurveArrayPropertyTypeBinding.class);
            container.registerComponentImplementation(GML.CurvePropertyType, 
                    org.geotools.gml3.bindings.ext.CurvePropertyTypeBinding.class);
            container.registerComponentImplementation(GML.CurveType, 
                    org.geotools.gml3.bindings.ext.CurveTypeBinding.class);
            container.registerComponentImplementation(GML.MultiCurveType, 
                    org.geotools.gml3.bindings.ext.MultiCurveTypeBinding.class);
            container.registerComponentImplementation(GML.MultiPolygonType, 
                    org.geotools.gml3.bindings.ext.MultiPolygonTypeBinding.class);
            container.registerComponentImplementation(GML.MultiSurfaceType, 
                    org.geotools.gml3.bindings.ext.MultiSurfaceTypeBinding.class);
            container.registerComponentImplementation(GML.PolygonPatchType, 
                    org.geotools.gml3.bindings.ext.PolygonPatchTypeBinding.class);
            container.registerComponentImplementation(GML.SurfaceArrayPropertyType, 
                    org.geotools.gml3.bindings.ext.SurfaceArrayPropertyTypeBinding.class);
            container.registerComponentImplementation(GML.SurfacePatchArrayPropertyType, 
                    org.geotools.gml3.bindings.ext.SurfacePatchArrayPropertyTypeBinding.class);
            container.registerComponentImplementation(GML.SurfacePropertyType, 
                    org.geotools.gml3.bindings.ext.SurfacePropertyTypeBinding.class);
            container.registerComponentImplementation(GML.SurfaceType, 
                    org.geotools.gml3.bindings.ext.SurfaceTypeBinding.class);
        }
    }

    /**
     * Configures the gml3 context.
     * <p>
     * The following factories are registered:
     * <ul>
     * <li>{@link CoordinateArraySequenceFactory} under {@link CoordinateSequenceFactory}
     * <li>{@link GeometryFactory}
     * </ul>
     * </p>
     */
    public void configureContext(MutablePicoContainer container) {
        super.configureContext(container);

        container.registerComponentInstance(new FeatureTypeCache());
        container.registerComponentInstance(new XSDIdRegistry());

        //factories
        container.registerComponentInstance(CoordinateSequenceFactory.class,
            CoordinateArraySequenceFactory.instance());
        container.registerComponentImplementation(GeometryFactory.class);
        
        container.registerComponentInstance(new GML3EncodingUtils());
        
        if (isExtendedArcSurfaceSupport()) {
            container.registerComponentInstance(new ArcParameters());
        }
    }
}
