/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2024, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xml.transform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.geotools.api.style.Style;
import org.geotools.api.style.StyleFactory;
import org.geotools.api.style.StyledLayerDescriptor;
import org.geotools.api.style.UserLayer;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.xml.styling.SLDTransformer;
import org.junit.Test;

/**
 * Tests that XML namespace attributes are written in a deterministic (sorted) order. This prevents spurious diffs when
 * SLD files are re-serialized.
 */
public class NamespaceAttributeOrderTest {

    private static final StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);

    /**
     * Verifies that namespace declarations in the root element of a transformed SLD are emitted in alphabetical order
     * by prefix. This ensures deterministic output regardless of internal HashMap iteration order.
     */
    @Test
    public void testNamespaceDeclarationsAreSortedByPrefix() throws Exception {
        // Create a minimal SLD that will trigger multiple namespace declarations
        StyledLayerDescriptor sld = sf.createStyledLayerDescriptor();
        sld.setName("test");
        UserLayer layer = sf.createUserLayer();
        layer.setName("testLayer");
        Style style = sf.createStyle();
        style.setName("testStyle");
        layer.addUserStyle(style);
        sld.addStyledLayer(layer);

        SLDTransformer transformer = new SLDTransformer();
        transformer.setIndentation(2);

        // Transform multiple times to verify determinism
        String xml1 = transformer.transform(sld);
        String xml2 = transformer.transform(sld);

        // Output should be identical across multiple serializations
        assertEquals("Repeated serialization should produce identical output", xml1, xml2);

        // Extract the root element's opening tag (everything up to the first >)
        // Skip the XML declaration if present
        int rootStart = xml1.indexOf("<sld:");
        if (rootStart < 0) {
            rootStart = xml1.indexOf("<StyledLayerDescriptor");
        }
        assertTrue("Should find root element in output: " + xml1, rootStart >= 0);
        String rootTag = xml1.substring(rootStart, xml1.indexOf(">", rootStart) + 1);

        // Extract all xmlns:prefix declarations and verify the non-element prefixes are sorted.
        // Note: The JAXP Transformer always emits the element's own namespace prefix first
        // (e.g., "sld" for <sld:StyledLayerDescriptor>), so we skip the first prefix and verify
        // the remaining ones are in alphabetical order.
        Pattern nsPattern = Pattern.compile("xmlns:(\\w+)=");
        Matcher matcher = nsPattern.matcher(rootTag);

        java.util.List<String> prefixes = new java.util.ArrayList<>();
        while (matcher.find()) {
            prefixes.add(matcher.group(1));
        }

        // Verify we found multiple namespace prefixes (sld, ogc, gml at minimum)
        assertTrue("Should have found at least 3 namespace prefixes in: " + rootTag, prefixes.size() >= 3);

        // Skip the first prefix (element's own namespace, placed first by JAXP Transformer)
        // and verify the rest are sorted
        java.util.List<String> remainingPrefixes = prefixes.subList(1, prefixes.size());
        for (int i = 1; i < remainingPrefixes.size(); i++) {
            String prev = remainingPrefixes.get(i - 1);
            String curr = remainingPrefixes.get(i);
            assertTrue(
                    "Namespace prefix '" + curr + "' should come after '" + prev + "' (alphabetical order). Root tag: "
                            + rootTag,
                    curr.compareTo(prev) >= 0);
        }
    }

    /**
     * Verifies that the output is byte-identical across many iterations, ruling out any non-determinism from hash-based
     * data structures.
     */
    @Test
    public void testRepeatedSerializationIsDeterministic() throws Exception {
        StyledLayerDescriptor sld = sf.createStyledLayerDescriptor();
        sld.setName("determinism-test");
        UserLayer layer = sf.createUserLayer();
        layer.setName("layer1");
        Style style = sf.createStyle();
        style.setName("style1");
        layer.addUserStyle(style);
        sld.addStyledLayer(layer);

        SLDTransformer transformer = new SLDTransformer();

        String reference = transformer.transform(sld);

        // Run 20 times to increase chance of catching non-determinism
        for (int i = 0; i < 20; i++) {
            String result = transformer.transform(sld);
            assertEquals("Serialization attempt " + (i + 1) + " differs from reference", reference, result);
        }
    }
}
