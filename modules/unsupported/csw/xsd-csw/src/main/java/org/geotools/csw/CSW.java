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
package org.geotools.csw;


import java.util.Set;
import javax.xml.namespace.QName;
import org.geotools.xml.XSD;

/**
 * This interface contains the qualified names of all the types,elements, and 
 * attributes in the http://www.opengis.net/cat/csw/2.0.2 schema.
 *
 * @generated
 * @author Andrea Aime - GeoSolutions
 */
public final class CSW extends XSD {

    /** singleton instance */
    private static final CSW instance = new CSW();
    
    /**
     * Returns the singleton instance.
     */
    public static final CSW getInstance() {
       return instance;
    }
    
    /**
     * private constructor
     */
    private CSW() {
    }
    
    protected void addDependencies(Set dependencies) {
       //TODO: add dependencies here
    }
    
    /**
     * Returns 'http://www.opengis.net/cat/csw/2.0.2'.
     */
    public String getNamespaceURI() {
       return NAMESPACE;
    }
    
    /**
     * Returns the location of 'csw.xsd.'.
     */
    public String getSchemaLocation() {
       return getClass().getResource("csw.xsd").toString();
    }
    
    /** @generated */
    public static final String NAMESPACE = "http://www.opengis.net/cat/csw/2.0.2";
    
    /* Type Definitions */
    /** @generated */
    public static final QName AbstractQueryType = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","AbstractQueryType");
    /** @generated */
    public static final QName AbstractRecordType = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","AbstractRecordType");
    /** @generated */
    public static final QName AcknowledgementType = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","AcknowledgementType");
    /** @generated */
    public static final QName BriefRecordType = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","BriefRecordType");
    /** @generated */
    public static final QName CapabilitiesType = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","CapabilitiesType");
    /** @generated */
    public static final QName ConceptualSchemeType = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","ConceptualSchemeType");
    /** @generated */
    public static final QName DCMIRecordType = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","DCMIRecordType");
    /** @generated */
    public static final QName DeleteType = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","DeleteType");
    /** @generated */
    public static final QName DescribeRecordResponseType = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","DescribeRecordResponseType");
    /** @generated */
    public static final QName DescribeRecordType = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","DescribeRecordType");
    /** @generated */
    public static final QName DistributedSearchType = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","DistributedSearchType");
    /** @generated */
    public static final QName DomainValuesType = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","DomainValuesType");
    /** @generated */
    public static final QName EchoedRequestType = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","EchoedRequestType");
    /** @generated */
    public static final QName ElementSetNameType = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","ElementSetNameType");
    /** @generated */
    public static final QName ElementSetType = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","ElementSetType");
    /** @generated */
    public static final QName EmptyType = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","EmptyType");
    /** @generated */
    public static final QName GetCapabilitiesType = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","GetCapabilitiesType");
    /** @generated */
    public static final QName GetDomainResponseType = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","GetDomainResponseType");
    /** @generated */
    public static final QName GetDomainType = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","GetDomainType");
    /** @generated */
    public static final QName GetRecordByIdResponseType = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","GetRecordByIdResponseType");
    /** @generated */
    public static final QName GetRecordByIdType = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","GetRecordByIdType");
    /** @generated */
    public static final QName GetRecordsResponseType = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","GetRecordsResponseType");
    /** @generated */
    public static final QName GetRecordsType = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","GetRecordsType");
    /** @generated */
    public static final QName HarvestResponseType = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","HarvestResponseType");
    /** @generated */
    public static final QName HarvestType = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","HarvestType");
    /** @generated */
    public static final QName InsertResultType = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","InsertResultType");
    /** @generated */
    public static final QName InsertType = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","InsertType");
    /** @generated */
    public static final QName ListOfValuesType = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","ListOfValuesType");
    /** @generated */
    public static final QName QueryConstraintType = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","QueryConstraintType");
    /** @generated */
    public static final QName QueryType = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","QueryType");
    /** @generated */
    public static final QName RangeOfValuesType = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","RangeOfValuesType");
    /** @generated */
    public static final QName RecordPropertyType = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","RecordPropertyType");
    /** @generated */
    public static final QName RecordType = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","RecordType");
    /** @generated */
    public static final QName RequestBaseType = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","RequestBaseType");
    /** @generated */
    public static final QName RequestStatusType = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","RequestStatusType");
    /** @generated */
    public static final QName ResultType = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","ResultType");
    /** @generated */
    public static final QName SchemaComponentType = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","SchemaComponentType");
    /** @generated */
    public static final QName SearchResultsType = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","SearchResultsType");
    /** @generated */
    public static final QName SummaryRecordType = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","SummaryRecordType");
    /** @generated */
    public static final QName TransactionResponseType = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","TransactionResponseType");
    /** @generated */
    public static final QName TransactionSummaryType = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","TransactionSummaryType");
    /** @generated */
    public static final QName TransactionType = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","TransactionType");
    /** @generated */
    public static final QName TypeNameListType = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","TypeNameListType");
    /** @generated */
    public static final QName UpdateType = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","UpdateType");

    /* Elements */
    /** @generated */
    public static final QName AbstractQuery = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","AbstractQuery");
    /** @generated */
    public static final QName AbstractRecord = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","AbstractRecord");
    /** @generated */
    public static final QName Acknowledgement = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","Acknowledgement");
    /** @generated */
    public static final QName BriefRecord = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","BriefRecord");
    /** @generated */
    public static final QName Capabilities = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","Capabilities");
    /** @generated */
    public static final QName Constraint = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","Constraint");
    /** @generated */
    public static final QName DCMIRecord = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","DCMIRecord");
    /** @generated */
    public static final QName DescribeRecord = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","DescribeRecord");
    /** @generated */
    public static final QName DescribeRecordResponse = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","DescribeRecordResponse");
    /** @generated */
    public static final QName ElementSetName = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","ElementSetName");
    /** @generated */
    public static final QName GetCapabilities = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","GetCapabilities");
    /** @generated */
    public static final QName GetDomain = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","GetDomain");
    /** @generated */
    public static final QName GetDomainResponse = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","GetDomainResponse");
    /** @generated */
    public static final QName GetRecordById = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","GetRecordById");
    /** @generated */
    public static final QName GetRecordByIdResponse = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","GetRecordByIdResponse");
    /** @generated */
    public static final QName GetRecords = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","GetRecords");
    /** @generated */
    public static final QName GetRecordsResponse = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","GetRecordsResponse");
    /** @generated */
    public static final QName Harvest = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","Harvest");
    /** @generated */
    public static final QName HarvestResponse = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","HarvestResponse");
    /** @generated */
    public static final QName Query = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","Query");
    /** @generated */
    public static final QName Record = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","Record");
    /** @generated */
    public static final QName RecordProperty = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","RecordProperty");
    public static final QName SimpleType = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","SummaryRecord");
    /** @generated */
    public static final QName SummaryRecord = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","SummaryRecord");
    /** @generated */
    public static final QName Transaction = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","Transaction");
    /** @generated */
    public static final QName TransactionResponse = 
        new QName("http://www.opengis.net/cat/csw/2.0.2","TransactionResponse");
    public static final QName SimpleLiteral = 
        new QName("http://purl.org/dc/elements/1.1/", "SimpleLiteral");
    
    /* Attributes */

}
    