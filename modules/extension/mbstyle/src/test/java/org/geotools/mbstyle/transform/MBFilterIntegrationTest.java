package org.geotools.mbstyle.transform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Set;
import org.geotools.api.style.SemanticType;
import org.geotools.api.style.StyledLayerDescriptor;
import org.geotools.mbstyle.MBStyle;
import org.geotools.mbstyle.MapboxTestUtils;
import org.geotools.styling.FeatureTypeStyleImpl;
import org.geotools.styling.SLD;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Test;

public class MBFilterIntegrationTest {

    /**
     * Create a style with three layers, each of them with a filter requiring a '$type' (or a list
     * of them).
     *
     * <p>Assert that the transformed {@link FeatureTypeStyleImpl}s have the correct derived {@link
     * SemanticType}s.
     */
    @Test
    public void testSemanticTypes() throws IOException, ParseException {
        JSONObject jsonObject = MapboxTestUtils.parseTestStyle("filterSemanticTypeTest.json");
        MBStyle mbStyle = new MBStyle(jsonObject);

        StyledLayerDescriptor sld = mbStyle.transform();
        FeatureTypeStyleImpl[] ftss =
                SLD.featureTypeStyles((org.geotools.styling.StyledLayerDescriptor) sld);

        assertEquals(3, ftss.length);

        FeatureTypeStyleImpl circleLayerFts = ftss[0];
        assertEquals("circle-layer", circleLayerFts.getName());
        assertEquals(1, circleLayerFts.semanticTypeIdentifiers().size());
        assertEquals(
                SemanticType.POINT, circleLayerFts.semanticTypeIdentifiers().iterator().next());

        FeatureTypeStyleImpl lineLayerFts = ftss[1];
        assertEquals("line-layer", lineLayerFts.getName());
        assertEquals(1, lineLayerFts.semanticTypeIdentifiers().size());
        assertEquals(SemanticType.LINE, lineLayerFts.semanticTypeIdentifiers().iterator().next());

        FeatureTypeStyleImpl symbolLayerFts = ftss[2];
        assertEquals("symbol-layer", symbolLayerFts.getName());
        assertEquals(2, symbolLayerFts.semanticTypeIdentifiers().size());
        Set<SemanticType> semanticTypes = symbolLayerFts.semanticTypeIdentifiers();
        assertTrue(
                semanticTypes.contains(SemanticType.POINT)
                        && semanticTypes.contains(SemanticType.POLYGON));
    }
}
