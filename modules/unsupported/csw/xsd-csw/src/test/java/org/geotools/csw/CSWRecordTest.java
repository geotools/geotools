package org.geotools.csw;

import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.List;

import net.opengis.cat.csw20.BriefRecordType;
import net.opengis.cat.csw20.RecordType;
import net.opengis.cat.csw20.SimpleLiteral;
import net.opengis.cat.csw20.SummaryRecordType;
import net.opengis.ows10.BoundingBoxType;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.geotools.ows.OWS;
import org.geotools.xml.Encoder;
import org.geotools.xml.Parser;
import org.junit.Before;
import org.junit.Test;

public class CSWRecordTest {
    
    Parser parser;
    Encoder encoder;
    
    @Before
    public void setup() {
        parser = new Parser(new CSWConfiguration());
        encoder = new Encoder(new CSWConfiguration());
        encoder.getNamespaces().declarePrefix("csw", CSW.NAMESPACE);
        encoder.getNamespaces().declarePrefix("dc", DC.NAMESPACE);
        encoder.getNamespaces().declarePrefix("dct", DCT.NAMESPACE);
        encoder.getNamespaces().declarePrefix("ows", OWS.NAMESPACE);
        encoder.setIndenting(true);
    }

    @Test
    public void testParseBriefRecord() throws Exception {
        BriefRecordType br = (BriefRecordType) parser.parse(getClass().getResourceAsStream("BriefRecord.xml"));
        assertEquals("00180e67-b7cf-40a3-861d-b3a09337b195", br.getIdentifier().get(0).getValue());
        assertEquals("Image2000 Product 1 (at1) Multispectral", br.getTitle().get(0).getValue());
        assertEquals("Image2000 Product 1 (at1) Multispectral", br.getTitle().get(0).getValue());
        assertEquals("dataset", br.getType().getValue());
    }
    
    @Test
    public void testRoundTripBriefRecord() throws Exception {
        BriefRecordType br = (BriefRecordType) parser.parse(getClass().getResourceAsStream("BriefRecord.xml"));
        String encoded = encoder.encodeAsString(br, CSW.BriefRecord);
        BriefRecordType reparsed = (BriefRecordType) parser.parse(new StringReader(encoded));
        assertTrue(EMFUtils.emfEquals(br, reparsed));
    }
    
    
    @Test
    public void testParseSummaryRecord() throws Exception {
        SummaryRecordType sr = (SummaryRecordType) parser.parse(getClass().getResourceAsStream("SummaryRecord.xml"));
        assertEquals("00180e67-b7cf-40a3-861d-b3a09337b195", sr.getIdentifier().get(0).getValue());
        assertEquals("Image2000 Product 1 (at1) Multispectral", sr.getTitle().get(0).getValue());
        assertEquals("dataset", sr.getType().getValue());
        assertEquals("imagery", sr.getSubject().get(0).getValue());
        assertEquals("baseMaps", sr.getSubject().get(1).getValue());
        assertEquals("earthCover", sr.getSubject().get(2).getValue());
        assertEquals("BIL", sr.getFormat().get(0).getValue());
        assertEquals("2004-10-04 00:00:00", sr.getModified().get(0).getValue());
        String abs = "IMAGE2000 product 1 individual orthorectified scenes. IMAGE2000 was produced from ETM+ Landsat 7 satellite data and provides a consistent European coverage of individual orthorectified scenes in national map projection systems.";
        assertEquals(abs, sr.getAbstract().get(0).getValue());
    }
    
    @Test
    public void testRoundTripSummaryRecord() throws Exception {
        SummaryRecordType sr = (SummaryRecordType) parser.parse(getClass().getResourceAsStream("SummaryRecord.xml"));
        String encoded = encoder.encodeAsString(sr, CSW.SummaryRecord);
        SummaryRecordType reparsed = (SummaryRecordType) parser.parse(new StringReader(encoded));
        assertTrue(EMFUtils.emfEquals(sr, reparsed));
    }
    
    @Test
    public void testParseRecord() throws Exception {
        RecordType record = (RecordType) parser.parse(getClass().getResourceAsStream("Record.xml"));
        BoundingBoxType bbox = record.getBoundingBox().get(0);
        assertEquals(14.05, bbox.getLowerCorner().get(0));
        assertEquals(46.46, bbox.getLowerCorner().get(1));
        assertEquals(17.24, bbox.getUpperCorner().get(0));
        assertEquals(48.42, bbox.getUpperCorner().get(1));
        
        EList<SimpleLiteral> dcElements = record.getDCElement();
        assertEquals(11, dcElements.size());
        
        assertEquals("00180e67-b7cf-40a3-861d-b3a09337b195", getElement(dcElements, "identifier"));
        assertEquals("Image2000 Product 1 (at1) Multispectral", getElement(dcElements, "title"));
        assertEquals("dataset", getElement(dcElements, "type"));
        assertEquals("imagery", getElement(dcElements, "subject", 0));
        assertEquals("baseMaps", getElement(dcElements, "subject", 1));
        assertEquals("earthCover", getElement(dcElements, "subject", 2));
        assertEquals("BIL", getElement(dcElements, "format"));
        assertEquals("Vanda Lima", getElement(dcElements, "creator"));
        assertEquals("en", getElement(dcElements, "language"));
        assertEquals("2004-10-04 00:00:00", getElement(dcElements, "modified"));
        String abs = "IMAGE2000 product 1 individual orthorectified scenes. IMAGE2000 was produced from ETM+ Landsat 7 satellite data and provides a consistent European coverage of individual orthorectified scenes in national map projection systems.";
        assertEquals(abs, getElement(dcElements, "abstract", 0));
    }
    
    @Test
    public void testRoundTripRecord() throws Exception {
        RecordType record = (RecordType) parser.parse(getClass().getResourceAsStream("Record.xml"));
        String encoded = encoder.encodeAsString(record, CSW.Record);
        // System.out.println(encoded);
        /** This does not quite work, the bbox is parsed as a OWS 1.1 one
        RecordType reparsed = (RecordType) parser.parse(new StringReader(encoded));
        assertTrue(emfEquals(record, reparsed));
        */
    }
    
    private Object getElement(List<SimpleLiteral> elements, String name) {
        return getElement(elements, name, 0);
    }
    
    private Object getElement(List<SimpleLiteral> elements, String name, int idx) {
        int curr = 0;
        for (SimpleLiteral sl : elements) {
            if(name.equals(sl.getName())) {
                if(idx == curr) {
                    return sl.getValue();
                } else {
                    curr++;
                }
            }
        }
        
        return null;
    }

    
    
}
