package org.geotools.csw;

import static org.junit.Assert.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import net.opengis.cat.csw20.AbstractRecordType;
import net.opengis.cat.csw20.ElementSetNameType;
import net.opengis.cat.csw20.ElementSetType;
import net.opengis.cat.csw20.GetRecordByIdResponseType;
import net.opengis.cat.csw20.GetRecordByIdType;
import net.opengis.cat.csw20.GetRecordsType;
import net.opengis.cat.csw20.QueryConstraintType;
import net.opengis.cat.csw20.QueryType;
import net.opengis.cat.csw20.RecordType;
import net.opengis.cat.csw20.ResultType;

import org.eclipse.emf.common.util.EList;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.xml.Parser;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;

public class CSWGetRecordsTest {

    Parser parser = new Parser(new CSWConfiguration());

    @Test
    public void testParseGetRecordsEbrimBrief() throws Exception {
        GetRecordsType gr = (GetRecordsType) parser.parse(getClass().getResourceAsStream(
                "GetRecordsBrief.xml"));
        assertEquals("CSW", gr.getService());
        assertEquals("2.0.2", gr.getVersion());

        // check the attributes
        assertEquals("application/xml", gr.getOutputFormat());
        assertEquals("urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0", gr.getOutputSchema());

        // the query
        QueryType query = (QueryType) gr.getQuery();
        List<QName> expected = new ArrayList<QName>();
        String rimNamespace = "urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0";
        expected.add(new QName(rimNamespace, "Service"));
        expected.add(new QName(rimNamespace, "Classification"));
        expected.add(new QName(rimNamespace, "Association"));
        assertEquals(expected, query.getTypeNames());

        // the element set name
        ElementSetNameType esn = query.getElementSetName();
        expected.clear();
        expected.add(new QName(rimNamespace, "Service"));
        assertEquals(expected, esn.getTypeNames());
        assertEquals(ElementSetType.BRIEF, esn.getValue());
        
        // the sort by properties
        SortBy[] sorts = query.getSortBy();
        assertEquals(2, sorts.length);
        assertEquals("rim:foo", sorts[0].getPropertyName().getPropertyName());
        assertEquals(SortOrder.ASCENDING, sorts[0].getSortOrder());
        assertEquals("rim:bar", sorts[1].getPropertyName().getPropertyName());
        assertEquals(SortOrder.DESCENDING, sorts[1].getSortOrder());
    }

    @Test
    public void testParseGetRecordsFilterSimple() throws Exception {
        GetRecordsType gr = (GetRecordsType) parser.parse(getClass().getResourceAsStream(
                "GetRecordsFilterSimple.xml"));

        // check the attributes
        assertEquals("CSW", gr.getService());
        assertEquals("2.0.2", gr.getVersion());
        assertEquals("application/xml", gr.getOutputFormat());
        assertEquals("http://www.opengis.net/cat/csw/2.0.2", gr.getOutputSchema());
        assertEquals(new Integer(1), gr.getStartPosition());
        assertEquals(new Integer(5), gr.getMaxRecords());
        assertEquals(ResultType.RESULTS, gr.getResultType());

        // the query
        QueryType query = (QueryType) gr.getQuery();
        List<QName> expected = new ArrayList<QName>();
        expected.add(new QName("http://www.opengis.net/cat/csw/2.0.2", "Record"));
        assertEquals(expected, query.getTypeNames());

        // the element names
        List<QName> names = query.getElementName();
        assertEquals(2, names.size());
        assertEquals(new QName("http://purl.org/dc/terms/", "abstract"), names.get(0));
        assertEquals(new QName("http://purl.org/dc/elements/1.1/", "title"), names.get(1));

        // The filter
        QueryConstraintType constraint = query.getConstraint();
        assertEquals("1.1.0", constraint.getVersion());
        Filter filter = CQL.toFilter("AnyText like '%polution%'");
        assertEquals(filter, constraint.getFilter());
    }

    @Test
    public void testParseGetRecordsById() throws Exception {
        GetRecordByIdType gr = (GetRecordByIdType) parser.parse(getClass().getResourceAsStream(
                "GetRecordById.xml"));
        // check the attributes
        assertEquals("CSW", gr.getService());
        assertEquals("2.0.2", gr.getVersion());
        assertEquals("application/xml", gr.getOutputFormat());
        assertEquals("http://www.opengis.net/cat/csw/2.0.2", gr.getOutputSchema());
        
        // the element set name
        ElementSetNameType esn = gr.getElementSetName();
        assertEquals(ElementSetType.SUMMARY, esn.getValue());
        
        // the ids
        List<URI> ids = gr.getId();
        assertEquals(new URI("REC-10"), ids.get(0));
        assertEquals(new URI("REC-11"), ids.get(1));
        assertEquals(new URI("REC-12"), ids.get(2));
    }
    
    @Test 
    public void testParseGetRecordsByIdResponse() throws Exception {
        GetRecordByIdResponseType response = (GetRecordByIdResponseType) parser.parse(getClass().getResourceAsStream(
                "GetRecordByIdResponse.xml"));
        assertNotNull(response);
        EList<AbstractRecordType> records = response.getAbstractRecord();
        assertEquals(1, records.size());
        RecordType record = (RecordType) records.get(0);
    }
}
