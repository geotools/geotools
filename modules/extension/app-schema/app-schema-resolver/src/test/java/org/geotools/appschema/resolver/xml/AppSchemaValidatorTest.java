/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2010-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.appschema.resolver.xml;

import java.io.IOException;
import java.io.InputStream;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link AppSchemaValidator}.
 *
 * @author Ben Caradoc-Davies (CSIRO Earth Science and Resource Engineering)
 */
public class AppSchemaValidatorTest {

    /** Test that validation succeeds for a known-valid XML instance document. */
    @Test
    public void validateErMineralOccurrence() {
        AppSchemaValidator.validateResource("/test-data/er_MineralOccurrence.xml", null);
    }

    /**
     * Test that validation fails with an expected error message for a known-invalid XML instance
     * document.
     */
    @Test
    public void validateErMineralOccurrenceWithErrors() {
        try {
            AppSchemaValidator.validateResource(
                    "/test-data/er_MineralOccurrence_with_errors.xml", null);
            Assert.fail(
                    "Unexpected schema validation success for known-invalid XML instance document");
        } catch (Exception e) {
            Assert.assertTrue(
                    e.getMessage(),
                    e.getMessage()
                            .startsWith(
                                    "Schema validation failures: 2"
                                            + System.getProperty("line.separator")));
        }
    }

    /**
     * Test that validation succeeds for a WFS 2.0 / GML 3.2 example from an annex of a draft of the
     * WFS 2.0 specification.
     */
    @Test
    public void validateWfs20Example01() {
        AppSchemaValidator.validateResource("/test-data/Example01.xml", null);
    }

    /** Tests for {@link AppSchemaValidator#getEncoding(String)}. */
    @Test
    public void getEncoding() {
        Assert.assertNull(AppSchemaValidator.getEncoding(""));
        Assert.assertNull(AppSchemaValidator.getEncoding("<?xml version=\"1.0\"?>"));
        Assert.assertNull(AppSchemaValidator.getEncoding("<?xml version=\"1.0\"? >"));
        Assert.assertNull(AppSchemaValidator.getEncoding("<?xml version=\"1.0\"?><root/>"));
        Assert.assertEquals(
                "UTF-8",
                AppSchemaValidator.getEncoding("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
        Assert.assertEquals(
                "UTF-8", AppSchemaValidator.getEncoding("<?xml version='1.0' encoding='UTF-8'?>"));
        Assert.assertEquals(
                "UTF-8",
                AppSchemaValidator.getEncoding("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
        Assert.assertEquals(
                "UTF-8",
                AppSchemaValidator.getEncoding(
                        "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"));
        Assert.assertEquals(
                "UTF-8",
                AppSchemaValidator.getEncoding(
                        "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root/>"));
    }

    /**
     * Test support for testing {@link AppSchemaValidator#validate(String)}. This method converts
     * reads a classpath resource into a string (using the default platform encoding) before
     * applying string schema validation.
     */
    public static void validateResourceAsString(String name) {
        InputStream input = null;
        try {
            input = AppSchemaValidatorTest.class.getResourceAsStream(name);
            byte[] bytes = new byte[input.available()];
            int count = input.read(bytes);
            Assert.assertEquals("Unexpected read underrun", bytes.length, count);
            String xml = new String(bytes);
            AppSchemaValidator.validate(xml, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    // we tried
                }
            }
        }
    }

    /**
     * Test that validation succeeds for a known-valid XML instance document. This version converts
     * the resource to a string and back before validation.
     */
    @Test
    public void validateErMineralOccurrenceAsString() {
        validateResourceAsString("/test-data/er_MineralOccurrence.xml");
    }

    /**
     * Test that validation fails with an expected error message for a known-invalid XML instance
     * document. This version converts the resource to a string and back before validation.
     */
    @Test
    public void validateErMineralOccurrenceWithErrorsAsString() {
        try {
            validateResourceAsString("/test-data/er_MineralOccurrence_with_errors.xml");
            Assert.fail(
                    "Unexpected schema validation success for known-invalid XML instance document");
        } catch (Exception e) {
            Assert.assertTrue(
                    e.getMessage(),
                    e.getMessage()
                            .startsWith(
                                    "Schema validation failures: 2"
                                            + System.getProperty("line.separator")));
        }
    }

    /**
     * Test that validation succeeds for a WFS 2.0 / GML 3.2 example from an annex of a draft of the
     * WFS 2.0 specification. This version converts the resource to a string and back before
     * validation.
     */
    @Test
    public void validateWfs20Example01AsString() {
        validateResourceAsString("/test-data/Example01.xml");
    }

    /** Test that a GetFeature can be validated. */
    @Test
    public void validateGetFeature() {
        AppSchemaValidator.validate(
                "<wfs:GetFeature " //
                        + "xmlns:wfs=\"http://www.opengis.net/wfs\" " //
                        + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " //
                        + "xsi:schemaLocation=\"" //
                        + "http://www.opengis.net/wfs http://schemas.opengis.net/wfs/1.1.0/wfs.xsd" //
                        + "\"><wfs:Query typeName=\"test\"/></wfs:GetFeature>",
                null);
    }
}
