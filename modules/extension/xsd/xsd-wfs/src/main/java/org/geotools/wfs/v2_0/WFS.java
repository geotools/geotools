package org.geotools.wfs.v2_0;

import java.util.Set;
import javax.xml.namespace.QName;

import org.geotools.filter.v2_0.FES;
import org.geotools.gml3.v3_2.GML;
import org.geotools.ows.v1_1.OWS;
import org.geotools.xml.XSD;

/**
 * This interface contains the qualified names of all the types,elements, and 
 * attributes in the http://www.opengis.net/wfs/2.0 schema.
 *
 * @generated
 */
public final class WFS extends XSD {

    /** singleton instance */
    private static final WFS instance = new WFS();
    
    /**
     * Returns the singleton instance.
     */
    public static final WFS getInstance() {
       return instance;
    }
    
    /**
     * private constructor
     */
    private WFS() {
    }
    
    protected void addDependencies(Set dependencies) {
        dependencies.add(OWS.getInstance());
        dependencies.add(FES.getInstance());
        dependencies.add(GML.getInstance());
    }
    
    /**
     * Returns 'http://www.opengis.net/wfs/2.0'.
     */
    public String getNamespaceURI() {
       return NAMESPACE;
    }
    
    /**
     * Returns the location of 'wfs.xsd.'.
     */
    public String getSchemaLocation() {
       return getClass().getResource("wfs.xsd").toString();
    }
    
    /** @generated */
    public static final String NAMESPACE = "http://www.opengis.net/wfs/2.0";
    
    /* Type Definitions */
    /** @generated */
    public static final QName AbstractTransactionActionType = 
        new QName("http://www.opengis.net/wfs/2.0","AbstractTransactionActionType");
    /** @generated */
    public static final QName ActionResultsType = 
        new QName("http://www.opengis.net/wfs/2.0","ActionResultsType");
    /** @generated */
    public static final QName AllSomeType = 
        new QName("http://www.opengis.net/wfs/2.0","AllSomeType");
    /** @generated */
    public static final QName BaseRequestType = 
        new QName("http://www.opengis.net/wfs/2.0","BaseRequestType");
    /** @generated */
    public static final QName CreatedOrModifiedFeatureType = 
        new QName("http://www.opengis.net/wfs/2.0","CreatedOrModifiedFeatureType");
    /** @generated */
    public static final QName CreateStoredQueryResponseType = 
        new QName("http://www.opengis.net/wfs/2.0","CreateStoredQueryResponseType");
    /** @generated */
    public static final QName CreateStoredQueryType = 
        new QName("http://www.opengis.net/wfs/2.0","CreateStoredQueryType");
    /** @generated */
    public static final QName DeleteType = 
        new QName("http://www.opengis.net/wfs/2.0","DeleteType");
    /** @generated */
    public static final QName DescribeFeatureTypeType = 
        new QName("http://www.opengis.net/wfs/2.0","DescribeFeatureTypeType");
    /** @generated */
    public static final QName DescribeStoredQueriesResponseType = 
        new QName("http://www.opengis.net/wfs/2.0","DescribeStoredQueriesResponseType");
    /** @generated */
    public static final QName DescribeStoredQueriesType = 
        new QName("http://www.opengis.net/wfs/2.0","DescribeStoredQueriesType");
    /** @generated */
    public static final QName ElementType = 
        new QName("http://www.opengis.net/wfs/2.0","ElementType");
    /** @generated */
    public static final QName EmptyType = 
        new QName("http://www.opengis.net/wfs/2.0","EmptyType");
    /** @generated */
    public static final QName EnvelopePropertyType = 
        new QName("http://www.opengis.net/wfs/2.0","EnvelopePropertyType");
    /** @generated */
    public static final QName ExecutionStatusType = 
        new QName("http://www.opengis.net/wfs/2.0","ExecutionStatusType");
    /** @generated */
    public static final QName ExtendedDescriptionType = 
        new QName("http://www.opengis.net/wfs/2.0","ExtendedDescriptionType");
    /** @generated */
    public static final QName FeatureCollectionType = 
        new QName("http://www.opengis.net/wfs/2.0","FeatureCollectionType");
    /** @generated */
    public static final QName FeaturesLockedType = 
        new QName("http://www.opengis.net/wfs/2.0","FeaturesLockedType");
    /** @generated */
    public static final QName FeaturesNotLockedType = 
        new QName("http://www.opengis.net/wfs/2.0","FeaturesNotLockedType");
    /** @generated */
    public static final QName FeatureTypeListType = 
        new QName("http://www.opengis.net/wfs/2.0","FeatureTypeListType");
    /** @generated */
    public static final QName FeatureTypeType = 
        new QName("http://www.opengis.net/wfs/2.0","FeatureTypeType");
    /** @generated */
    public static final QName GetCapabilitiesType = 
        new QName("http://www.opengis.net/wfs/2.0","GetCapabilitiesType");
    /** @generated */
    public static final QName GetFeatureType = 
        new QName("http://www.opengis.net/wfs/2.0","GetFeatureType");
    /** @generated */
    public static final QName GetFeatureWithLockType = 
        new QName("http://www.opengis.net/wfs/2.0","GetFeatureWithLockType");
    /** @generated */
    public static final QName GetPropertyValueType = 
        new QName("http://www.opengis.net/wfs/2.0","GetPropertyValueType");
    /** @generated */
    public static final QName InsertType = 
        new QName("http://www.opengis.net/wfs/2.0","InsertType");
    /** @generated */
    public static final QName ListStoredQueriesResponseType = 
        new QName("http://www.opengis.net/wfs/2.0","ListStoredQueriesResponseType");
    /** @generated */
    public static final QName ListStoredQueriesType = 
        new QName("http://www.opengis.net/wfs/2.0","ListStoredQueriesType");
    /** @generated */
    public static final QName LockFeatureResponseType = 
        new QName("http://www.opengis.net/wfs/2.0","LockFeatureResponseType");
    /** @generated */
    public static final QName LockFeatureType = 
        new QName("http://www.opengis.net/wfs/2.0","LockFeatureType");
    /** @generated */
    public static final QName MemberPropertyType = 
        new QName("http://www.opengis.net/wfs/2.0","MemberPropertyType");
    /** @generated */
    public static final QName MetadataURLType = 
        new QName("http://www.opengis.net/wfs/2.0","MetadataURLType");
    /** @generated */
    public static final QName NativeType = 
        new QName("http://www.opengis.net/wfs/2.0","NativeType");
    /** @generated */
    public static final QName nonNegativeIntegerOrUnknown = 
        new QName("http://www.opengis.net/wfs/2.0","nonNegativeIntegerOrUnknown");
    /** @generated */
    public static final QName OutputFormatListType = 
        new QName("http://www.opengis.net/wfs/2.0","OutputFormatListType");
    /** @generated */
    public static final QName ParameterExpressionType = 
        new QName("http://www.opengis.net/wfs/2.0","ParameterExpressionType");
    /** @generated */
    public static final QName ParameterType = 
        new QName("http://www.opengis.net/wfs/2.0","ParameterType");
    /** @generated */
    public static final QName positiveIntegerWithStar = 
        new QName("http://www.opengis.net/wfs/2.0","positiveIntegerWithStar");
    /** @generated */
    public static final QName PropertyType = 
        new QName("http://www.opengis.net/wfs/2.0","PropertyType");
    /** @generated */
    public static final QName QueryExpressionTextType = 
        new QName("http://www.opengis.net/wfs/2.0","QueryExpressionTextType");
    /** @generated */
    public static final QName QueryType = 
        new QName("http://www.opengis.net/wfs/2.0","QueryType");
    /** @generated */
    public static final QName ReplaceType = 
        new QName("http://www.opengis.net/wfs/2.0","ReplaceType");
    /** @generated */
    public static final QName ResolveValueType = 
        new QName("http://www.opengis.net/wfs/2.0","ResolveValueType");
    /** @generated */
    public static final QName ResultTypeType = 
        new QName("http://www.opengis.net/wfs/2.0","ResultTypeType");
    /** @generated */
    public static final QName ReturnFeatureTypesListType = 
        new QName("http://www.opengis.net/wfs/2.0","ReturnFeatureTypesListType");
    /** @generated */
    public static final QName SimpleFeatureCollectionType = 
        new QName("http://www.opengis.net/wfs/2.0","SimpleFeatureCollectionType");
    /** @generated */
    public static final QName StarStringType = 
        new QName("http://www.opengis.net/wfs/2.0","StarStringType");
    /** @generated */
    public static final QName StateValueType = 
        new QName("http://www.opengis.net/wfs/2.0","StateValueType");
    /** @generated */
    public static final QName StoredQueryDescriptionType = 
        new QName("http://www.opengis.net/wfs/2.0","StoredQueryDescriptionType");
    /** @generated */
    public static final QName StoredQueryListItemType = 
        new QName("http://www.opengis.net/wfs/2.0","StoredQueryListItemType");
    /** @generated */
    public static final QName StoredQueryType = 
        new QName("http://www.opengis.net/wfs/2.0","StoredQueryType");
    /** @generated */
    public static final QName TransactionResponseType = 
        new QName("http://www.opengis.net/wfs/2.0","TransactionResponseType");
    /** @generated */
    public static final QName TransactionSummaryType = 
        new QName("http://www.opengis.net/wfs/2.0","TransactionSummaryType");
    /** @generated */
    public static final QName TransactionType = 
        new QName("http://www.opengis.net/wfs/2.0","TransactionType");
    /** @generated */
    public static final QName TupleType = 
        new QName("http://www.opengis.net/wfs/2.0","TupleType");
    /** @generated */
    public static final QName UpdateActionType = 
        new QName("http://www.opengis.net/wfs/2.0","UpdateActionType");
    /** @generated */
    public static final QName UpdateType = 
        new QName("http://www.opengis.net/wfs/2.0","UpdateType");
    /** @generated */
    public static final QName ValueCollectionType = 
        new QName("http://www.opengis.net/wfs/2.0","ValueCollectionType");
    /** @generated */
    public static final QName ValueListType = 
        new QName("http://www.opengis.net/wfs/2.0","ValueListType");
    /** @generated */
    public static final QName WFS_CapabilitiesType = 
        new QName("http://www.opengis.net/wfs/2.0","WFS_CapabilitiesType");
    /** @generated */
    public static final QName _Abstract = 
        new QName("http://www.opengis.net/wfs/2.0","_Abstract");
    /** @generated */
    public static final QName _additionalObjects = 
        new QName("http://www.opengis.net/wfs/2.0","_additionalObjects");
    /** @generated */
    public static final QName _additionalValues = 
        new QName("http://www.opengis.net/wfs/2.0","_additionalValues");
    /** @generated */
    public static final QName _DropStoredQuery = 
        new QName("http://www.opengis.net/wfs/2.0","_DropStoredQuery");
    /** @generated */
    public static final QName _PropertyName = 
        new QName("http://www.opengis.net/wfs/2.0","_PropertyName");
    /** @generated */
    public static final QName _Title = 
        new QName("http://www.opengis.net/wfs/2.0","_Title");
    /** @generated */
    public static final QName _truncatedResponse = 
        new QName("http://www.opengis.net/wfs/2.0","_truncatedResponse");
    /** @generated */
    public static final QName FeatureTypeType_NoCRS = 
        new QName("http://www.opengis.net/wfs/2.0","FeatureTypeType_NoCRS");
    /** @generated */
    public static final QName PropertyType_ValueReference = 
        new QName("http://www.opengis.net/wfs/2.0","PropertyType_ValueReference");
    /** @generated */
    public static final QName WFS_CapabilitiesType_WSDL = 
        new QName("http://www.opengis.net/wfs/2.0","WFS_CapabilitiesType_WSDL");

    /* Elements */
    /** @generated */
    public static final QName Abstract = 
        new QName("http://www.opengis.net/wfs/2.0","Abstract");
    /** @generated */
    public static final QName AbstractTransactionAction = 
        new QName("http://www.opengis.net/wfs/2.0","AbstractTransactionAction");
    /** @generated */
    public static final QName additionalObjects = 
        new QName("http://www.opengis.net/wfs/2.0","additionalObjects");
    /** @generated */
    public static final QName additionalValues = 
        new QName("http://www.opengis.net/wfs/2.0","additionalValues");
    /** @generated */
    public static final QName boundedBy = 
        new QName("http://www.opengis.net/wfs/2.0","boundedBy");
    /** @generated */
    public static final QName CreateStoredQuery = 
        new QName("http://www.opengis.net/wfs/2.0","CreateStoredQuery");
    /** @generated */
    public static final QName CreateStoredQueryResponse = 
        new QName("http://www.opengis.net/wfs/2.0","CreateStoredQueryResponse");
    /** @generated */
    public static final QName Delete = 
        new QName("http://www.opengis.net/wfs/2.0","Delete");
    /** @generated */
    public static final QName DescribeFeatureType = 
        new QName("http://www.opengis.net/wfs/2.0","DescribeFeatureType");
    /** @generated */
    public static final QName DescribeStoredQueries = 
        new QName("http://www.opengis.net/wfs/2.0","DescribeStoredQueries");
    /** @generated */
    public static final QName DescribeStoredQueriesResponse = 
        new QName("http://www.opengis.net/wfs/2.0","DescribeStoredQueriesResponse");
    /** @generated */
    public static final QName DropStoredQuery = 
        new QName("http://www.opengis.net/wfs/2.0","DropStoredQuery");
    /** @generated */
    public static final QName DropStoredQueryResponse = 
        new QName("http://www.opengis.net/wfs/2.0","DropStoredQueryResponse");
    /** @generated */
    public static final QName Element = 
        new QName("http://www.opengis.net/wfs/2.0","Element");
    /** @generated */
    public static final QName FeatureCollection = 
        new QName("http://www.opengis.net/wfs/2.0","FeatureCollection");
    /** @generated */
    public static final QName FeatureTypeList = 
        new QName("http://www.opengis.net/wfs/2.0","FeatureTypeList");
    /** @generated */
    public static final QName GetCapabilities = 
        new QName("http://www.opengis.net/wfs/2.0","GetCapabilities");
    /** @generated */
    public static final QName GetFeature = 
        new QName("http://www.opengis.net/wfs/2.0","GetFeature");
    /** @generated */
    public static final QName GetFeatureWithLock = 
        new QName("http://www.opengis.net/wfs/2.0","GetFeatureWithLock");
    /** @generated */
    public static final QName GetPropertyValue = 
        new QName("http://www.opengis.net/wfs/2.0","GetPropertyValue");
    /** @generated */
    public static final QName Insert = 
        new QName("http://www.opengis.net/wfs/2.0","Insert");
    /** @generated */
    public static final QName ListStoredQueries = 
        new QName("http://www.opengis.net/wfs/2.0","ListStoredQueries");
    /** @generated */
    public static final QName ListStoredQueriesResponse = 
        new QName("http://www.opengis.net/wfs/2.0","ListStoredQueriesResponse");
    /** @generated */
    public static final QName LockFeature = 
        new QName("http://www.opengis.net/wfs/2.0","LockFeature");
    /** @generated */
    public static final QName LockFeatureResponse = 
        new QName("http://www.opengis.net/wfs/2.0","LockFeatureResponse");
    /** @generated */
    public static final QName member = 
        new QName("http://www.opengis.net/wfs/2.0","member");
    /** @generated */
    public static final QName Native = 
        new QName("http://www.opengis.net/wfs/2.0","Native");
    /** @generated */
    public static final QName Property = 
        new QName("http://www.opengis.net/wfs/2.0","Property");
    /** @generated */
    public static final QName PropertyName = 
        new QName("http://www.opengis.net/wfs/2.0","PropertyName");
    /** @generated */
    public static final QName Query = 
        new QName("http://www.opengis.net/wfs/2.0","Query");
    /** @generated */
    public static final QName Replace = 
        new QName("http://www.opengis.net/wfs/2.0","Replace");
    /** @generated */
    public static final QName SimpleFeatureCollection = 
        new QName("http://www.opengis.net/wfs/2.0","SimpleFeatureCollection");
    /** @generated */
    public static final QName StoredQuery = 
        new QName("http://www.opengis.net/wfs/2.0","StoredQuery");
    /** @generated */
    public static final QName Title = 
        new QName("http://www.opengis.net/wfs/2.0","Title");
    /** @generated */
    public static final QName Transaction = 
        new QName("http://www.opengis.net/wfs/2.0","Transaction");
    /** @generated */
    public static final QName TransactionResponse = 
        new QName("http://www.opengis.net/wfs/2.0","TransactionResponse");
    /** @generated */
    public static final QName truncatedResponse = 
        new QName("http://www.opengis.net/wfs/2.0","truncatedResponse");
    /** @generated */
    public static final QName Tuple = 
        new QName("http://www.opengis.net/wfs/2.0","Tuple");
    /** @generated */
    public static final QName Update = 
        new QName("http://www.opengis.net/wfs/2.0","Update");
    /** @generated */
    public static final QName Value = 
        new QName("http://www.opengis.net/wfs/2.0","Value");
    /** @generated */
    public static final QName ValueCollection = 
        new QName("http://www.opengis.net/wfs/2.0","ValueCollection");
    /** @generated */
    public static final QName ValueList = 
        new QName("http://www.opengis.net/wfs/2.0","ValueList");
    /** @generated */
    public static final QName WFS_Capabilities = 
        new QName("http://www.opengis.net/wfs/2.0","WFS_Capabilities");

    /* Attributes */

}
    