package org.geotools.gml4wcs;

import java.util.Map;

import javax.xml.namespace.QName;

import net.opengis.gml.Gml4wcsFactory;

import org.eclipse.emf.ecore.EFactory;
import org.geotools.feature.DefaultFeatureCollections;
import org.geotools.gml2.FeaturePropertyExtractor;
import org.geotools.gml2.FeatureTypeCache;
import org.geotools.gml4wcs.bindings.AbstractGeometricPrimitiveTypeBinding;
import org.geotools.gml4wcs.bindings.AbstractGeometryBaseTypeBinding;
import org.geotools.gml4wcs.bindings.AbstractGeometryTypeBinding;
import org.geotools.gml4wcs.bindings.DirectPositionTypeBinding;
import org.geotools.gml4wcs.bindings.DoubleListBinding;
import org.geotools.gml4wcs.bindings.EnvelopeTypeBinding;
import org.geotools.gml4wcs.bindings.EnvelopeWithTimePeriodTypeBinding;
import org.geotools.gml4wcs.bindings.GridEnvelopeTypeBinding;
import org.geotools.gml4wcs.bindings.GridLimitsTypeBinding;
import org.geotools.gml4wcs.bindings.GridTypeBinding;
import org.geotools.gml4wcs.bindings.IntegerListBinding;
import org.geotools.gml4wcs.bindings.NameListBinding;
import org.geotools.gml4wcs.bindings.TemporalPositionTypeBinding;
import org.geotools.gml4wcs.bindings.TimeDurationTypeBinding;
import org.geotools.gml4wcs.bindings.TimePositionTypeBinding;
import org.geotools.xlink.XLINKConfiguration;
import org.geotools.xml.ComplexEMFBinding;
import org.geotools.xml.Configuration;
import org.picocontainer.MutablePicoContainer;

import com.vividsolutions.jts.geom.CoordinateSequenceFactory;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequenceFactory;

/**
 * Parser configuration for the http://www.opengis.net/gml schema.
 *
 * @generated
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/extension/xsd/xsd-wcs/src/main/java/org/geotools/gml4wcs/GMLConfiguration.java $
 */
public class GMLConfiguration extends Configuration {

    /**
     * Creates a new configuration.
     * 
     * @generated
     */     
    public GMLConfiguration() {
       super(GML.getInstance());
       
       addDependency(new XLINKConfiguration());
    }
    
    @Override
    protected void registerBindings(Map bindings) {
        super.registerBindings(bindings);
        
        final EFactory gmlFactory = Gml4wcsFactory.eINSTANCE;
        register(bindings, gmlFactory, GML._GeometricPrimitive);
        register(bindings, gmlFactory, GML._Geometry);
        register(bindings, gmlFactory, GML._GML);
        register(bindings, gmlFactory, GML._MetaData);
        register(bindings, gmlFactory, GML._Object);
        register(bindings, gmlFactory, GML._Ring);
        register(bindings, gmlFactory, GML._Surface);

        bindings.put(GML.AbstractGeometricPrimitiveType, new AbstractGeometricPrimitiveTypeBinding());
        bindings.put(GML.AbstractGeometryBaseType, new AbstractGeometryBaseTypeBinding());
        bindings.put(GML.AbstractGeometryType, new AbstractGeometryTypeBinding());

        bindings.put(GML.integerList, new IntegerListBinding());
        bindings.put(GML.doubleList, new DoubleListBinding());
        bindings.put(GML.NameList, new NameListBinding());

        // temporal
        bindings.put(GML.TimeDurationType, new TimeDurationTypeBinding());
        bindings.put(GML.TimePositionType, new TimePositionTypeBinding());
        bindings.put(GML.TemporalPositionType, new TemporalPositionTypeBinding());
        
        // gml:pos
        bindings.put(GML.DirectPositionType, new DirectPositionTypeBinding());

        // CRS
        register(bindings, gmlFactory, GML.CodeType);
        register(bindings, gmlFactory, GML.CodeListType);

        // Envelope
        bindings.put(GML.EnvelopeType, new EnvelopeTypeBinding());
        bindings.put(GML.EnvelopeWithTimePeriodType, new EnvelopeWithTimePeriodTypeBinding());
        
        // Grid
        bindings.put(GML.GridEnvelopeType, new GridEnvelopeTypeBinding());
        bindings.put(GML.GridLimitsType, new GridLimitsTypeBinding());
        
        bindings.put(GML.GridType, new GridTypeBinding());
        //register(bindings, gmlFactory, GML.RectifiedGridType);
        //register(bindings, gmlFactory, GML.GridType);
    }
    
    private void register(Map bindings, EFactory factory, QName qname) {
        bindings.put(qname, new ComplexEMFBinding(factory, qname));
    }

//    /**
//     * Registers the bindings for the configuration.
//     *
//     * @generated
//     */
//    protected final void registerBindings( MutablePicoContainer container ) {
//        //Types
//        container.registerComponentImplementation(GML.AbstractGeometricPrimitiveType,AbstractGeometricPrimitiveTypeBinding.class);
//        container.registerComponentImplementation(GML.AbstractGeometryBaseType,AbstractGeometryBaseTypeBinding.class);
//        container.registerComponentImplementation(GML.AbstractGeometryType,AbstractGeometryTypeBinding.class);
//        container.registerComponentImplementation(GML.AbstractGMLType,AbstractGMLTypeBinding.class);
//        container.registerComponentImplementation(GML.AbstractMetaDataType,AbstractMetaDataTypeBinding.class);
//        container.registerComponentImplementation(GML.AbstractRingPropertyType,AbstractRingPropertyTypeBinding.class);
//        container.registerComponentImplementation(GML.AbstractRingType,AbstractRingTypeBinding.class);
//        container.registerComponentImplementation(GML.AbstractSurfaceType,AbstractSurfaceTypeBinding.class);
//        container.registerComponentImplementation(GML.BoundingShapeType,BoundingShapeTypeBinding.class);
//        container.registerComponentImplementation(GML.CodeListType,CodeListTypeBinding.class);
//        container.registerComponentImplementation(GML.CodeType,CodeTypeBinding.class);
//        container.registerComponentImplementation(GML.DirectPositionType,DirectPositionTypeBinding.class);
//        container.registerComponentImplementation(GML.doubleList,DoubleListBinding.class);
//        container.registerComponentImplementation(GML.EnvelopeType,EnvelopeTypeBinding.class);
//        container.registerComponentImplementation(GML.EnvelopeWithTimePeriodType,EnvelopeWithTimePeriodTypeBinding.class);
//        container.registerComponentImplementation(GML.GridEnvelopeType,GridEnvelopeTypeBinding.class);
//        container.registerComponentImplementation(GML.GridLimitsType,GridLimitsTypeBinding.class);
//        container.registerComponentImplementation(GML.GridType,GridTypeBinding.class);
//        container.registerComponentImplementation(GML.integerList,IntegerListBinding.class);
//        container.registerComponentImplementation(GML.LinearRingType,LinearRingTypeBinding.class);
//        container.registerComponentImplementation(GML.MetaDataPropertyType,MetaDataPropertyTypeBinding.class);
//        container.registerComponentImplementation(GML.NameList,NameListBinding.class);
//        container.registerComponentImplementation(GML.PointType,PointTypeBinding.class);
//        container.registerComponentImplementation(GML.PolygonType,PolygonTypeBinding.class);
//        container.registerComponentImplementation(GML.RectifiedGridType,RectifiedGridTypeBinding.class);
//        container.registerComponentImplementation(GML.ReferenceType,ReferenceTypeBinding.class);
//        container.registerComponentImplementation(GML.StringOrRefType,StringOrRefTypeBinding.class);
//        container.registerComponentImplementation(GML.TemporalPositionType,TemporalPositionTypeBinding.class);
//        container.registerComponentImplementation(GML.TimeDurationType,TimeDurationTypeBinding.class);
//        container.registerComponentImplementation(GML.TimeIndeterminateValueType,TimeIndeterminateValueTypeBinding.class);
//        container.registerComponentImplementation(GML.TimePositionType,TimePositionTypeBinding.class);
//        container.registerComponentImplementation(GML.VectorType,VectorTypeBinding.class);
//    }
    
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
        container.registerComponentInstance(Gml4wcsFactory.eINSTANCE);
        
        container.registerComponentInstance(new FeatureTypeCache());
        container.registerComponentImplementation(FeaturePropertyExtractor.class);

        //factories
        container.registerComponentInstance(CoordinateSequenceFactory.class, CoordinateArraySequenceFactory.instance());
        container.registerComponentImplementation(GeometryFactory.class);
        container.registerComponentImplementation(DefaultFeatureCollections.class);
    }
} 
