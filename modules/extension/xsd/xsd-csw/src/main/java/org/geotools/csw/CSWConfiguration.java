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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import javax.xml.namespace.QName;
import net.opengis.cat.csw20.Csw20Factory;
import net.opengis.cat.csw20.ElementSetType;
import net.opengis.cat.csw20.ResultType;
import org.geotools.csw.bindings.ElementSetNameTypeBinding;
import org.geotools.csw.bindings.RecordBinding;
import org.geotools.csw.bindings.TypeNameListTypeBinding;
import org.geotools.filter.v1_1.OGCConfiguration;
import org.geotools.xsd.ComplexEMFBinding;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.EnumSimpleBinding;
import org.geotools.xsd.ows.OWSConfiguration;
import org.picocontainer.MutablePicoContainer;

/**
 * Parser configuration for the http://www.opengis.net/cat/csw/2.0.2 schema.
 *
 * @author Andrea Aime - GeoSolutions
 */
public class CSWConfiguration extends Configuration {

    /**
     * Creates a new configuration.
     *
     * @generated
     */
    public CSWConfiguration() {
        super(CSW.getInstance());

        // add dependencies on OWS 1.0 and Filter 1.1
        addDependency(new OWSConfiguration());
        addDependency(new OGCConfiguration());
        addDependency(new DCTConfiguration());
    }

    /**
     * Registers the bindings for the configuration.
     *
     * @generated
     */
    protected void registerBindings(Map bindings) {
        // generated code, see the main method
        bindings.put(
                CSW.AbstractQueryType,
                new ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.AbstractQueryType));
        bindings.put(
                CSW.AbstractRecordType,
                new ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.AbstractRecordType));
        bindings.put(
                CSW.AcknowledgementType,
                new ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.AcknowledgementType));
        bindings.put(
                CSW.BriefRecordType,
                new ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.BriefRecordType));
        bindings.put(
                CSW.CapabilitiesType,
                new ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.CapabilitiesType));
        bindings.put(
                CSW.ConceptualSchemeType,
                new ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.ConceptualSchemeType));
        bindings.put(
                CSW.DCMIRecordType,
                new ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.DCMIRecordType));
        bindings.put(CSW.DeleteType, new ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.DeleteType));
        bindings.put(
                CSW.DescribeRecordResponseType,
                new ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.DescribeRecordResponseType));
        bindings.put(
                CSW.DescribeRecordType,
                new ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.DescribeRecordType));
        bindings.put(
                CSW.DistributedSearchType,
                new ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.DistributedSearchType));
        bindings.put(
                CSW.DomainValuesType,
                new ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.DomainValuesType));
        bindings.put(
                CSW.EchoedRequestType,
                new ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.EchoedRequestType));
        bindings.put(CSW.ElementSetNameType, new ElementSetNameTypeBinding());
        bindings.put(
                CSW.ElementSetType,
                new EnumSimpleBinding(ElementSetType.class, CSW.ElementSetType));
        bindings.put(CSW.EmptyType, new ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.EmptyType));
        bindings.put(
                CSW.GetCapabilitiesType,
                new ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.GetCapabilitiesType));
        bindings.put(
                CSW.GetDomainResponseType,
                new ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.GetDomainResponseType));
        bindings.put(
                CSW.GetDomainType,
                new ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.GetDomainType));
        bindings.put(
                CSW.GetRecordByIdResponseType,
                new ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.GetRecordByIdResponseType));
        bindings.put(
                CSW.GetRecordByIdType,
                new ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.GetRecordByIdType));
        bindings.put(
                CSW.GetRecordsResponseType,
                new ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.GetRecordsResponseType));
        bindings.put(
                CSW.GetRecordsType,
                new ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.GetRecordsType));
        bindings.put(
                CSW.HarvestResponseType,
                new ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.HarvestResponseType));
        bindings.put(
                CSW.HarvestType, new ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.HarvestType));
        bindings.put(
                CSW.InsertResultType,
                new ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.InsertResultType));
        bindings.put(CSW.InsertType, new ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.InsertType));
        bindings.put(
                CSW.ListOfValuesType,
                new ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.ListOfValuesType));
        bindings.put(
                CSW.QueryConstraintType,
                new ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.QueryConstraintType));
        bindings.put(CSW.QueryType, new ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.QueryType));
        bindings.put(
                CSW.RangeOfValuesType,
                new ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.RangeOfValuesType));
        bindings.put(
                CSW.RecordPropertyType,
                new ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.RecordPropertyType));
        bindings.put(CSW.RecordType, new RecordBinding());
        bindings.put(
                CSW.RequestBaseType,
                new ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.RequestBaseType));
        bindings.put(
                CSW.RequestStatusType,
                new ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.RequestStatusType));
        bindings.put(CSW.ResultType, new EnumSimpleBinding(ResultType.class, CSW.ResultType));
        bindings.put(
                CSW.SchemaComponentType,
                new ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.SchemaComponentType));
        bindings.put(
                CSW.SearchResultsType,
                new ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.SearchResultsType));
        bindings.put(
                CSW.SummaryRecordType,
                new ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.SummaryRecordType));
        bindings.put(
                CSW.TransactionResponseType,
                new ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.TransactionResponseType));
        bindings.put(
                CSW.TransactionSummaryType,
                new ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.TransactionSummaryType));
        bindings.put(
                CSW.TransactionType,
                new ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.TransactionType));
        bindings.put(CSW.TypeNameListType, new TypeNameListTypeBinding());
        /**
         * bindings.put(CSW.UpdateType, new ComplexEMFBinding(Csw20Factory.eINSTANCE,
         * CSW.UpdateType)); bindings.put(CSW.AbstractQuery, new
         * ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.AbstractQuery));
         * bindings.put(CSW.AbstractRecord, new ComplexEMFBinding(Csw20Factory.eINSTANCE,
         * CSW.AbstractRecord)); bindings.put(CSW.Acknowledgement, new
         * ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.Acknowledgement));
         * bindings.put(CSW.BriefRecord, new ComplexEMFBinding(Csw20Factory.eINSTANCE,
         * CSW.BriefRecord)); bindings.put(CSW.Capabilities, new
         * ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.Capabilities));
         * bindings.put(CSW.Constraint, new ComplexEMFBinding(Csw20Factory.eINSTANCE,
         * CSW.Constraint)); bindings.put(CSW.DCMIRecord, new
         * ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.DCMIRecord));
         * bindings.put(CSW.DescribeRecord, new ComplexEMFBinding(Csw20Factory.eINSTANCE,
         * CSW.DescribeRecord)); bindings.put(CSW.DescribeRecordResponse, new
         * ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.DescribeRecordResponse));
         * bindings.put(CSW.ElementSetName, new ComplexEMFBinding(Csw20Factory.eINSTANCE,
         * CSW.ElementSetName)); bindings.put(CSW.GetCapabilities, new
         * ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.GetCapabilities));
         * bindings.put(CSW.GetDomain, new ComplexEMFBinding(Csw20Factory.eINSTANCE,
         * CSW.GetDomain)); bindings.put(CSW.GetDomainResponse, new
         * ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.GetDomainResponse));
         * bindings.put(CSW.GetRecordById, new ComplexEMFBinding(Csw20Factory.eINSTANCE,
         * CSW.GetRecordById)); bindings.put(CSW.GetRecordByIdResponse, new
         * ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.GetRecordByIdResponse));
         * bindings.put(CSW.GetRecords, new ComplexEMFBinding(Csw20Factory.eINSTANCE,
         * CSW.GetRecords)); bindings.put(CSW.GetRecordsResponse, new
         * ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.GetRecordsResponse));
         * bindings.put(CSW.Harvest, new ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.Harvest));
         * bindings.put(CSW.HarvestResponse, new ComplexEMFBinding(Csw20Factory.eINSTANCE,
         * CSW.HarvestResponse)); bindings.put(CSW.Query, new
         * ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.Query)); bindings.put(CSW.Record, new
         * ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.Record)); bindings.put(CSW.RecordProperty,
         * new ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.RecordProperty));
         * bindings.put(CSW.SummaryRecord, new ComplexEMFBinding(Csw20Factory.eINSTANCE,
         * CSW.SummaryRecord)); bindings.put(CSW.Transaction, new
         * ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW.Transaction));
         * bindings.put(CSW.TransactionResponse, new ComplexEMFBinding(Csw20Factory.eINSTANCE,
         * CSW.TransactionResponse));
         */
    }

    protected void configureContext(MutablePicoContainer container) {
        container.registerComponentInstance(Csw20Factory.eINSTANCE);
    }

    /** Generates the bindings registrations for this class */
    @SuppressWarnings("PMD.SystemPrintln")
    public static void main(String[] args) {
        for (Field f : CSW.class.getFields()) {
            if ((f.getModifiers() & (Modifier.STATIC | Modifier.FINAL)) != 0
                    && f.getType().equals(QName.class)) {
                System.out.println(
                        "bindings.put(CSW."
                                + f.getName()
                                + ", new ComplexEMFBinding(Csw20Factory.eINSTANCE, CSW."
                                + f.getName()
                                + "));");
            }
        }
    }
}
