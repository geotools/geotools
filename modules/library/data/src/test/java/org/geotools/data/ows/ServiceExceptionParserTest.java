/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2018, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.ows;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.geotools.ows.ServiceException;
import org.junit.Assert;
import org.junit.Test;

public class ServiceExceptionParserTest {

    @Test
    public void testSimple() throws Exception {
        ServiceException exception =
                ServiceExceptionParser.parse(
                        mockStream(
                                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n"
                                        + "<ServiceExceptionReport>\n"
                                        + "  <ServiceException code=\"42\">test</ServiceException>\n"
                                        + "</ServiceExceptionReport>"));

        Assert.assertThat(
                exception,
                Matchers.both(Matchers.hasProperty("message", Matchers.equalTo("test")))
                        .and(Matchers.hasProperty("code", Matchers.equalTo("42"))));
        Assert.assertThat(exception.getNext(), Matchers.nullValue());
    }

    @Test
    public void testSequence() throws Exception {
        ServiceException exception =
                ServiceExceptionParser.parse(
                        mockStream(
                                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n"
                                        + "<ServiceExceptionReport>\n"
                                        + "  <ServiceException code=\"42\">test</ServiceException>\n"
                                        + "  <ServiceException code=\"20\">another test</ServiceException>\n"
                                        + "  <ServiceException code=\"60\">yet another test</ServiceException>\n"
                                        + "</ServiceExceptionReport>"));

        Assert.assertThat(
                exception,
                Matchers.both(Matchers.hasProperty("message", Matchers.equalTo("test")))
                        .and(Matchers.hasProperty("code", Matchers.equalTo("42"))));
        Assert.assertThat(
                exception.getNext(),
                Matchers.both(Matchers.hasProperty("message", Matchers.equalTo("another test")))
                        .and(Matchers.hasProperty("code", Matchers.equalTo("20"))));
        Assert.assertThat(
                exception.getNext().getNext(),
                Matchers.both(Matchers.hasProperty("message", Matchers.equalTo("yet another test")))
                        .and(Matchers.hasProperty("code", Matchers.equalTo("60"))));
        Assert.assertThat(exception.getNext().getNext().getNext(), Matchers.nullValue());
    }

    @Test
    public void testSequenceWithEmptyCodes() throws Exception {
        ServiceException exception =
                ServiceExceptionParser.parse(
                        mockStream(
                                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n"
                                        + "<ServiceExceptionReport>\n"
                                        + "  <ServiceException>test</ServiceException>\n"
                                        + "  <ServiceException code=\"\">another test</ServiceException>\n"
                                        + "  <ServiceException code=\"60\">yet another test</ServiceException>\n"
                                        + "  <ServiceException code=\"20\">still another test</ServiceException>\n"
                                        + "</ServiceExceptionReport>"));

        Assert.assertThat(
                exception,
                Matchers.both(Matchers.hasProperty("message", Matchers.equalTo("yet another test")))
                        .and(Matchers.hasProperty("code", Matchers.equalTo("60"))));
        Assert.assertThat(
                exception.getNext(),
                Matchers.both(
                                Matchers.hasProperty(
                                        "message", Matchers.equalTo("still another test")))
                        .and(Matchers.hasProperty("code", Matchers.equalTo("20"))));
        Assert.assertThat(
                exception.getNext().getNext(),
                Matchers.both(Matchers.hasProperty("message", Matchers.equalTo("test")))
                        .and(Matchers.hasProperty("code", Matchers.nullValue())));
        Assert.assertThat(
                exception.getNext().getNext().getNext(),
                Matchers.both(Matchers.hasProperty("message", Matchers.equalTo("another test")))
                        .and(Matchers.hasProperty("code", Matchers.equalTo(""))));
        Assert.assertThat(exception.getNext().getNext().getNext().getNext(), Matchers.nullValue());
    }

    @Test
    public void testXXE() throws Exception {
        URL resource = ServiceExceptionParserTest.class.getResource("secret.txt");
        ServiceException exception =
                ServiceExceptionParser.parse(
                        mockStream(
                                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n"
                                        + "<!DOCTYPE foo [ <!ELEMENT ServiceExceptionReport ANY ><!ENTITY xxe SYSTEM \""
                                        + resource
                                        + "\" >]>"
                                        + "<ServiceExceptionReport>\n"
                                        + "  <ServiceException code=\"42\">&xxe;</ServiceException>\n"
                                        + "</ServiceExceptionReport>"));

        Assert.assertThat(
                exception,
                Matchers.hasProperty(
                        "message",
                        Matchers.not(
                                Matchers.containsString(
                                        "Top secret information that shouldn't appear in an error message."))));
        Assert.assertThat(exception.getNext(), Matchers.nullValue());
    }

    protected InputStream mockStream(String string) {
        return new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8));
    }
}
