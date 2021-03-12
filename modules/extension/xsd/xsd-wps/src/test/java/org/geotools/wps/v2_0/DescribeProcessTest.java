package org.geotools.wps.v2_0;

import java.util.Arrays;
import java.util.List;
import net.opengis.ows20.LanguageStringType;
import net.opengis.ows20.MetadataType;
import net.opengis.wps20.ComplexDataType;
import net.opengis.wps20.InputDescriptionType;
import net.opengis.wps20.LiteralDataType;
import net.opengis.wps20.OutputDescriptionType;
import net.opengis.wps20.ProcessDescriptionType;
import net.opengis.wps20.ProcessOfferingType;
import net.opengis.wps20.ProcessOfferingsType;
import org.eclipse.emf.common.util.EList;
import org.geotools.xsd.Parser;
import org.junit.Assert;
import org.junit.Test;

public class DescribeProcessTest extends WPSTestSupport {

    @Test
    public void testParse() throws Exception {
        Parser parser = new Parser(createConfiguration());

        Object o = parser.parse(getClass().getResourceAsStream("wpsProcessOfferingsExample.xml"));
        Assert.assertTrue(o instanceof ProcessOfferingsType);

        ProcessOfferingsType processOfferings = (ProcessOfferingsType) o;

        Assert.assertNotNull(processOfferings);
        Assert.assertNotNull(processOfferings.getProcessOffering());
        Assert.assertEquals(1, processOfferings.getProcessOffering().size());

        ProcessOfferingType processOffering = processOfferings.getProcessOffering().get(0);
        Assert.assertNotNull(processOffering.getProcess());

        ProcessDescriptionType processDesc = processOffering.getProcess();
        Assert.assertNotNull(processDesc.getIdentifier());
        Assert.assertEquals(
                "http://my.wps.server/processes/proximity/Planar-Buffer",
                processDesc.getIdentifier().getValue());
        assertLanguageString(
                processDesc.getAbstract(),
                1,
                Arrays.asList(
                        "Create a buffer around Simple Feature geometries. Accepts any valid simple features input in GML or GeoJson and computes their joint buffer geometry."));
        assertLanguageString(
                processDesc.getTitle(),
                1,
                Arrays.asList("Planar Buffer operation for Simple Features"));

        assertMetadata(processDesc.getMetadata());
        assertInputs(processDesc.getInput());
        assertOutputs(processDesc.getOutput());
    }

    public void assertMetadata(EList<MetadataType> metadatas) {
        Assert.assertNotNull(metadatas);
        Assert.assertEquals(4, metadatas.size());
        Assert.assertEquals(
                "http://some.host/profileregistry/concept/buffer", metadatas.get(0).getHref());
        Assert.assertEquals(
                "http://some.host/profileregistry/concept/planarbuffer",
                metadatas.get(1).getHref());
        Assert.assertEquals(
                "http://some.host/profileregistry/generic/SF-Buffer", metadatas.get(2).getHref());
        Assert.assertEquals(
                "http://some.host/profileregistry/implementation/Planar-GML-Buffer.html",
                metadatas.get(3).getHref());
    }

    public void assertInputs(EList<InputDescriptionType> inputs) {
        Assert.assertNotNull(inputs);
        Assert.assertEquals(2, inputs.size());

        InputDescriptionType desc = inputs.get(0);
        assertLanguageString(desc.getTitle(), 1, Arrays.asList("Geometry to be buffered"));
        assertLanguageString(
                desc.getAbstract(),
                1,
                Arrays.asList("Simple Features geometry input in GML or GeoJson"));
        Assert.assertNotNull(desc.getIdentifier());
        Assert.assertEquals("INPUT_GEOMETRY", desc.getIdentifier().getValue());
        Assert.assertNotNull(desc.getDataDescription());

        ComplexDataType complexData = (ComplexDataType) desc.getDataDescription();
        Assert.assertNotNull(complexData);
        Assert.assertNotNull(complexData.getFormat());
        Assert.assertEquals(2, complexData.getFormat().size());

        desc = inputs.get(1);
        assertLanguageString(desc.getTitle(), 1, Arrays.asList("Distance"));
        assertLanguageString(
                desc.getAbstract(), 1, Arrays.asList("Distance to be used to calculate buffer."));
        Assert.assertNotNull(desc.getIdentifier());
        Assert.assertEquals("DISTANCE", desc.getIdentifier().getValue());

        Assert.assertNotNull(desc.getDataDescription());

        LiteralDataType literalData = (LiteralDataType) desc.getDataDescription();
        Assert.assertNotNull(literalData.getLiteralDataDomain());
        Assert.assertNotNull(literalData.getLiteralDataDomain().get(0));
        Assert.assertNotNull(literalData.getLiteralDataDomain().get(0).getAllowedValues());
        Assert.assertNotNull(
                literalData.getLiteralDataDomain().get(0).getAllowedValues().getRange());
        Assert.assertNotNull(
                literalData.getLiteralDataDomain().get(0).getAllowedValues().getRange().get(0));
        Assert.assertEquals(
                "INF",
                literalData
                        .getLiteralDataDomain()
                        .get(0)
                        .getAllowedValues()
                        .getRange()
                        .get(0)
                        .getMaximumValue()
                        .getValue());
        Assert.assertEquals(
                "-INF",
                literalData
                        .getLiteralDataDomain()
                        .get(0)
                        .getAllowedValues()
                        .getRange()
                        .get(0)
                        .getMinimumValue()
                        .getValue());
        Assert.assertNotNull(literalData.getLiteralDataDomain().get(0).getDataType());
        Assert.assertNotNull(
                "Double", literalData.getLiteralDataDomain().get(0).getDataType().getValue());
    }

    public void assertOutputs(EList<OutputDescriptionType> outputs) {
        Assert.assertNotNull(outputs);
        Assert.assertEquals(1, outputs.size());

        OutputDescriptionType desc = outputs.get(0);
        assertLanguageString(desc.getTitle(), 1, Arrays.asList("Buffered Geometry"));
        assertLanguageString(
                desc.getAbstract(), 1, Arrays.asList("Output Geometry in GML or GeoJson"));
        Assert.assertNotNull(desc.getIdentifier());
        Assert.assertEquals("BUFFERED_GEOMETRY", desc.getIdentifier().getValue());
        Assert.assertNotNull(desc.getDataDescription());

        ComplexDataType complexData = (ComplexDataType) desc.getDataDescription();
        Assert.assertNotNull(complexData);
        Assert.assertNotNull(complexData.getFormat());
        Assert.assertEquals(2, complexData.getFormat().size());
    }

    public void assertLanguageString(
            List<LanguageStringType> ls, Integer size, List<String> values) {
        Assert.assertNotNull(ls);
        Assert.assertEquals(size, Integer.valueOf(ls.size()));
        for (Integer i = 0; i < size; i++) {
            Assert.assertNotNull(ls.get(i));
            Assert.assertEquals(values.get(i), ls.get(i).getValue());
        }
    }
}
