package org.geotools.csw;

import org.eclipse.xsd.util.XSDSchemaLocationResolver;	
import org.geotools.xml.Configuration;
import org.picocontainer.MutablePicoContainer;

/**
 * Parser configuration for the http://www.opengis.net/cat/csw/2.0.2 schema.
 *
 * @generated
 */
public class CSWConfiguration extends Configuration {

    /**
     * Creates a new configuration.
     * 
     * @generated
     */     
    public CSWConfiguration() {
       super(CSW.getInstance());
       
       //TODO: add dependencies here
    }
    
    /**
     * Registers the bindings for the configuration.
     *
     * @generated
     */
    protected final void registerBindings( MutablePicoContainer container ) {
        //Types
        container.registerComponentImplementation(CSW.AbstractQueryType,AbstractQueryTypeBinding.class);
        container.registerComponentImplementation(CSW.AbstractRecordType,AbstractRecordTypeBinding.class);
        container.registerComponentImplementation(CSW.AcknowledgementType,AcknowledgementTypeBinding.class);
        container.registerComponentImplementation(CSW.BriefRecordType,BriefRecordTypeBinding.class);
        container.registerComponentImplementation(CSW.CapabilitiesType,CapabilitiesTypeBinding.class);
        container.registerComponentImplementation(CSW.ConceptualSchemeType,ConceptualSchemeTypeBinding.class);
        container.registerComponentImplementation(CSW.DCMIRecordType,DCMIRecordTypeBinding.class);
        container.registerComponentImplementation(CSW.DeleteType,DeleteTypeBinding.class);
        container.registerComponentImplementation(CSW.DescribeRecordResponseType,DescribeRecordResponseTypeBinding.class);
        container.registerComponentImplementation(CSW.DescribeRecordType,DescribeRecordTypeBinding.class);
        container.registerComponentImplementation(CSW.DistributedSearchType,DistributedSearchTypeBinding.class);
        container.registerComponentImplementation(CSW.DomainValuesType,DomainValuesTypeBinding.class);
        container.registerComponentImplementation(CSW.EchoedRequestType,EchoedRequestTypeBinding.class);
        container.registerComponentImplementation(CSW.ElementSetNameType,ElementSetNameTypeBinding.class);
        container.registerComponentImplementation(CSW.ElementSetType,ElementSetTypeBinding.class);
        container.registerComponentImplementation(CSW.EmptyType,EmptyTypeBinding.class);
        container.registerComponentImplementation(CSW.GetCapabilitiesType,GetCapabilitiesTypeBinding.class);
        container.registerComponentImplementation(CSW.GetDomainResponseType,GetDomainResponseTypeBinding.class);
        container.registerComponentImplementation(CSW.GetDomainType,GetDomainTypeBinding.class);
        container.registerComponentImplementation(CSW.GetRecordByIdResponseType,GetRecordByIdResponseTypeBinding.class);
        container.registerComponentImplementation(CSW.GetRecordByIdType,GetRecordByIdTypeBinding.class);
        container.registerComponentImplementation(CSW.GetRecordsResponseType,GetRecordsResponseTypeBinding.class);
        container.registerComponentImplementation(CSW.GetRecordsType,GetRecordsTypeBinding.class);
        container.registerComponentImplementation(CSW.HarvestResponseType,HarvestResponseTypeBinding.class);
        container.registerComponentImplementation(CSW.HarvestType,HarvestTypeBinding.class);
        container.registerComponentImplementation(CSW.InsertResultType,InsertResultTypeBinding.class);
        container.registerComponentImplementation(CSW.InsertType,InsertTypeBinding.class);
        container.registerComponentImplementation(CSW.ListOfValuesType,ListOfValuesTypeBinding.class);
        container.registerComponentImplementation(CSW.QueryConstraintType,QueryConstraintTypeBinding.class);
        container.registerComponentImplementation(CSW.QueryType,QueryTypeBinding.class);
        container.registerComponentImplementation(CSW.RangeOfValuesType,RangeOfValuesTypeBinding.class);
        container.registerComponentImplementation(CSW.RecordPropertyType,RecordPropertyTypeBinding.class);
        container.registerComponentImplementation(CSW.RecordType,RecordTypeBinding.class);
        container.registerComponentImplementation(CSW.RequestBaseType,RequestBaseTypeBinding.class);
        container.registerComponentImplementation(CSW.RequestStatusType,RequestStatusTypeBinding.class);
        container.registerComponentImplementation(CSW.ResultType,ResultTypeBinding.class);
        container.registerComponentImplementation(CSW.SchemaComponentType,SchemaComponentTypeBinding.class);
        container.registerComponentImplementation(CSW.SearchResultsType,SearchResultsTypeBinding.class);
        container.registerComponentImplementation(CSW.SummaryRecordType,SummaryRecordTypeBinding.class);
        container.registerComponentImplementation(CSW.TransactionResponseType,TransactionResponseTypeBinding.class);
        container.registerComponentImplementation(CSW.TransactionSummaryType,TransactionSummaryTypeBinding.class);
        container.registerComponentImplementation(CSW.TransactionType,TransactionTypeBinding.class);
        container.registerComponentImplementation(CSW.TypeNameListType,TypeNameListTypeBinding.class);
        container.registerComponentImplementation(CSW.UpdateType,UpdateTypeBinding.class);


    
    }
} 