package org.geotools.wfs.v2_0;

import net.opengis.wfs.WfsFactory;

import org.geotools.filter.v2_0.FESConfiguration;
import org.geotools.gml3.XSDIdRegistry;
import org.geotools.gml3.v3_2.GMLConfiguration;
import org.geotools.ows.v1_1.OWSConfiguration;
import org.geotools.xml.Configuration;
import org.picocontainer.MutablePicoContainer;

/**
 * Parser configuration for the http://www.opengis.net/wfs/2.0 schema.
 *
 * @generated
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/extension/xsd/xsd-wfs/src/main/java/org/geotools/wfs/v2_0/WFSConfiguration.java $
 */
public class WFSConfiguration extends Configuration {

    /**
     * Creates a new configuration.
     * 
     * @generated
     */     
    public WFSConfiguration() {
       super(WFS.getInstance());
       
       addDependency(new OWSConfiguration());
       addDependency(new FESConfiguration());
       addDependency(new GMLConfiguration());
    }
    
    @Override
    protected void configureContext(MutablePicoContainer container) {
        container.registerComponentInstance(WfsFactory.eINSTANCE);
        container.registerComponentInstance(new XSDIdRegistry());
    }
    
    /**
     * Registers the bindings for the configuration.
     *
     * @generated
     */
    protected final void registerBindings( MutablePicoContainer container ) {
        //Types
//        container.registerComponentImplementation(WFS.AbstractTransactionActionType,AbstractTransactionActionTypeBinding.class);
//        container.registerComponentImplementation(WFS.ActionResultsType,ActionResultsTypeBinding.class);
//        container.registerComponentImplementation(WFS.AllSomeType,AllSomeTypeBinding.class);
//        container.registerComponentImplementation(WFS.BaseRequestType,BaseRequestTypeBinding.class);
//        container.registerComponentImplementation(WFS.CreatedOrModifiedFeatureType,CreatedOrModifiedFeatureTypeBinding.class);
//        container.registerComponentImplementation(WFS.CreateStoredQueryResponseType,CreateStoredQueryResponseTypeBinding.class);
//        container.registerComponentImplementation(WFS.CreateStoredQueryType,CreateStoredQueryTypeBinding.class);
//        container.registerComponentImplementation(WFS.DeleteType,DeleteTypeBinding.class);
//        container.registerComponentImplementation(WFS.DescribeFeatureTypeType,DescribeFeatureTypeTypeBinding.class);
//        container.registerComponentImplementation(WFS.DescribeStoredQueriesResponseType,DescribeStoredQueriesResponseTypeBinding.class);
//        container.registerComponentImplementation(WFS.DescribeStoredQueriesType,DescribeStoredQueriesTypeBinding.class);
//        container.registerComponentImplementation(WFS.ElementType,ElementTypeBinding.class);
//        container.registerComponentImplementation(WFS.EmptyType,EmptyTypeBinding.class);
//        container.registerComponentImplementation(WFS.EnvelopePropertyType,EnvelopePropertyTypeBinding.class);
//        container.registerComponentImplementation(WFS.ExecutionStatusType,ExecutionStatusTypeBinding.class);
//        container.registerComponentImplementation(WFS.ExtendedDescriptionType,ExtendedDescriptionTypeBinding.class);
        container.registerComponentImplementation(WFS.FeatureCollectionType,FeatureCollectionTypeBinding.class);
//        container.registerComponentImplementation(WFS.FeaturesLockedType,FeaturesLockedTypeBinding.class);
//        container.registerComponentImplementation(WFS.FeaturesNotLockedType,FeaturesNotLockedTypeBinding.class);
//        container.registerComponentImplementation(WFS.FeatureTypeListType,FeatureTypeListTypeBinding.class);
//        container.registerComponentImplementation(WFS.FeatureTypeType,FeatureTypeTypeBinding.class);
//        container.registerComponentImplementation(WFS.GetCapabilitiesType,GetCapabilitiesTypeBinding.class);
//        container.registerComponentImplementation(WFS.GetFeatureType,GetFeatureTypeBinding.class);
//        container.registerComponentImplementation(WFS.GetFeatureWithLockType,GetFeatureWithLockTypeBinding.class);
//        container.registerComponentImplementation(WFS.GetPropertyValueType,GetPropertyValueTypeBinding.class);
//        container.registerComponentImplementation(WFS.InsertType,InsertTypeBinding.class);
//        container.registerComponentImplementation(WFS.ListStoredQueriesResponseType,ListStoredQueriesResponseTypeBinding.class);
//        container.registerComponentImplementation(WFS.ListStoredQueriesType,ListStoredQueriesTypeBinding.class);
//        container.registerComponentImplementation(WFS.LockFeatureResponseType,LockFeatureResponseTypeBinding.class);
//        container.registerComponentImplementation(WFS.LockFeatureType,LockFeatureTypeBinding.class);
        container.registerComponentImplementation(WFS.MemberPropertyType,MemberPropertyTypeBinding.class);
//        container.registerComponentImplementation(WFS.MetadataURLType,MetadataURLTypeBinding.class);
//        container.registerComponentImplementation(WFS.NativeType,NativeTypeBinding.class);
//        container.registerComponentImplementation(WFS.nonNegativeIntegerOrUnknown,NonNegativeIntegerOrUnknownBinding.class);
//        container.registerComponentImplementation(WFS.OutputFormatListType,OutputFormatListTypeBinding.class);
//        container.registerComponentImplementation(WFS.ParameterExpressionType,ParameterExpressionTypeBinding.class);
//        container.registerComponentImplementation(WFS.ParameterType,ParameterTypeBinding.class);
//        container.registerComponentImplementation(WFS.positiveIntegerWithStar,PositiveIntegerWithStarBinding.class);
//        container.registerComponentImplementation(WFS.PropertyType,PropertyTypeBinding.class);
//        container.registerComponentImplementation(WFS.QueryExpressionTextType,QueryExpressionTextTypeBinding.class);
//        container.registerComponentImplementation(WFS.QueryType,QueryTypeBinding.class);
//        container.registerComponentImplementation(WFS.ReplaceType,ReplaceTypeBinding.class);
//        container.registerComponentImplementation(WFS.ResolveValueType,ResolveValueTypeBinding.class);
//        container.registerComponentImplementation(WFS.ResultTypeType,ResultTypeTypeBinding.class);
//        container.registerComponentImplementation(WFS.ReturnFeatureTypesListType,ReturnFeatureTypesListTypeBinding.class);
//        container.registerComponentImplementation(WFS.SimpleFeatureCollectionType,SimpleFeatureCollectionTypeBinding.class);
//        container.registerComponentImplementation(WFS.StarStringType,StarStringTypeBinding.class);
//        container.registerComponentImplementation(WFS.StateValueType,StateValueTypeBinding.class);
//        container.registerComponentImplementation(WFS.StoredQueryDescriptionType,StoredQueryDescriptionTypeBinding.class);
//        container.registerComponentImplementation(WFS.StoredQueryListItemType,StoredQueryListItemTypeBinding.class);
//        container.registerComponentImplementation(WFS.StoredQueryType,StoredQueryTypeBinding.class);
//        container.registerComponentImplementation(WFS.TransactionResponseType,TransactionResponseTypeBinding.class);
//        container.registerComponentImplementation(WFS.TransactionSummaryType,TransactionSummaryTypeBinding.class);
//        container.registerComponentImplementation(WFS.TransactionType,TransactionTypeBinding.class);
//        container.registerComponentImplementation(WFS.TupleType,TupleTypeBinding.class);
//        container.registerComponentImplementation(WFS.UpdateActionType,UpdateActionTypeBinding.class);
//        container.registerComponentImplementation(WFS.UpdateType,UpdateTypeBinding.class);
//        container.registerComponentImplementation(WFS.ValueCollectionType,ValueCollectionTypeBinding.class);
//        container.registerComponentImplementation(WFS.ValueListType,ValueListTypeBinding.class);
//        container.registerComponentImplementation(WFS.WFS_CapabilitiesType,WFS_CapabilitiesTypeBinding.class);
//        container.registerComponentImplementation(WFS._Abstract,_AbstractBinding.class);
//        container.registerComponentImplementation(WFS._additionalObjects,_additionalObjectsBinding.class);
//        container.registerComponentImplementation(WFS._additionalValues,_additionalValuesBinding.class);
//        container.registerComponentImplementation(WFS._DropStoredQuery,_DropStoredQueryBinding.class);
//        container.registerComponentImplementation(WFS._PropertyName,_PropertyNameBinding.class);
//        container.registerComponentImplementation(WFS._Title,_TitleBinding.class);
//        container.registerComponentImplementation(WFS._truncatedResponse,_truncatedResponseBinding.class);
//        container.registerComponentImplementation(WFS.FeatureTypeType_NoCRS,FeatureTypeType_NoCRSBinding.class);
//        container.registerComponentImplementation(WFS.PropertyType_ValueReference,PropertyType_ValueReferenceBinding.class);
//        container.registerComponentImplementation(WFS.WFS_CapabilitiesType_WSDL,WFS_CapabilitiesType_WSDLBinding.class);
    }
} 
