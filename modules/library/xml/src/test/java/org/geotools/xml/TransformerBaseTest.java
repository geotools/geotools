/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.xml;

import java.io.StringWriter;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import org.junit.Assert;
import org.junit.Test;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.Diff;

public class TransformerBaseTest {

    @Test
    public void testUnbufferedUsageNoErrors() throws TransformerException {
        Source expected = Input.fromString(
                        "<?xml version=\"1.0\" encoding=\"UTF-8\"?><test:integers xmlns=\"http://geotools.org/test\" xmlns:test=\"http://geotools.org/test\"><test:integer>1</test:integer><test:integer>2</test:integer><test:integer>3</test:integer><test:integer>4</test:integer><test:integer>5</test:integer><test:integer>6</test:integer><test:integer>7</test:integer><test:integer>8</test:integer><test:integer>9</test:integer><test:integer>10</test:integer></test:integers>")
                .build();
        ExampleTransformer tx = new ExampleTransformer(0, 0, false);
        Source actual = Input.fromString(tx.transform(10)).build();

        Diff diff =
                DiffBuilder.compare(expected).withTest(actual).checkForSimilar().build();

        Assert.assertFalse(diff.toString(), diff.hasDifferences());
    }

    @Test
    public void testUnbufferedUsageOneError() {
        StringWriter w = new StringWriter();
        try {
            ExampleTransformer tx = new ExampleTransformer(0, 10, false);
            tx.transform(10, w);
            Assert.fail("Should have thrown an exception before reaching this point");
        } catch (TransformerException e) {
            Source expected = Input.fromString(
                            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><test:integers xmlns=\"http://geotools.org/test\" xmlns:test=\"http://geotools.org/test\"><test:integer>1</test:integer><test:integer>2</test:integer><test:integer>3</test:integer><test:integer>4</test:integer><test:integer>5</test:integer><test:integer>6</test:integer><test:integer>7</test:integer><test:integer>8</test:integer><test:integer>9</test:integer><test:integer>10</test:integer></test:integers>")
                    .build();
            Source actual = Input.fromString(w.toString() + "</test:integers>").build();

            Diff diff = DiffBuilder.compare(expected)
                    .withTest(actual)
                    .checkForSimilar()
                    .build();

            Assert.assertFalse(diff.toString(), diff.hasDifferences());
        }
    }

    @Test
    public void testBufferedUsageNoErrors() throws TransformerException {
        Source expected = Input.fromString(
                        "<?xml version=\"1.0\" encoding=\"UTF-8\"?><test:integers xmlns=\"http://geotools.org/test\" xmlns:test=\"http://geotools.org/test\"><test:integer>1</test:integer><test:integer>2</test:integer><test:integer>3</test:integer><test:integer>4</test:integer><test:integer>5</test:integer><test:integer>6</test:integer><test:integer>7</test:integer><test:integer>8</test:integer><test:integer>9</test:integer><test:integer>10</test:integer></test:integers>")
                .build();
        ExampleTransformer tx = new ExampleTransformer(1, 0, false);
        Source actual = Input.fromString(tx.transform(10)).build();

        Diff diff =
                DiffBuilder.compare(expected).withTest(actual).checkForSimilar().build();

        Assert.assertFalse(diff.toString(), diff.hasDifferences());
    }

    @Test
    public void testBufferedUsageOneError() {
        StringWriter w = new StringWriter();
        try {
            ExampleTransformer tx = new ExampleTransformer(1, 10, false);
            tx.transform(10, w);
            Assert.fail("Should have thrown an exception before reaching this point!");
        } catch (TransformerException e) {
            Source expected = Input.fromString(
                            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><test:integers xmlns=\"http://geotools.org/test\" xmlns:test=\"http://geotools.org/test\"><test:integer>1</test:integer><test:integer>2</test:integer><test:integer>3</test:integer><test:integer>4</test:integer><test:integer>5</test:integer><test:integer>6</test:integer><test:integer>7</test:integer><test:integer>8</test:integer><test:integer>9</test:integer></test:integers>")
                    .build();
            Source actual = Input.fromString(w.toString() + "</test:integers>").build();

            Diff diff = DiffBuilder.compare(expected)
                    .withTest(actual)
                    .checkForSimilar()
                    .build();

            Assert.assertFalse(diff.toString(), diff.hasDifferences());
        }
    }

    @Test
    public void testBufferedUsageIgnoringOneError() throws TransformerException {
        ExampleTransformer tx = new ExampleTransformer(1, 10, true);
        Source expected = Input.fromString(
                        "<?xml version=\"1.0\" encoding=\"UTF-8\"?><test:integers xmlns=\"http://geotools.org/test\" xmlns:test=\"http://geotools.org/test\"><test:integer>1</test:integer><test:integer>2</test:integer><test:integer>3</test:integer><test:integer>4</test:integer><test:integer>5</test:integer><test:integer>6</test:integer><test:integer>7</test:integer><test:integer>8</test:integer><test:integer>9</test:integer></test:integers>")
                .build();
        Source actual = Input.fromString(tx.transform(10)).build();

        Diff diff =
                DiffBuilder.compare(expected).withTest(actual).checkForSimilar().build();

        Assert.assertFalse(diff.toString(), diff.hasDifferences());
    }

    @Test
    public void testBufferedUsageIgnoringMultipleErrors() throws TransformerException {
        ExampleTransformer tx = new ExampleTransformer(1, 2, true);
        Source expected = Input.fromString(
                        "<?xml version=\"1.0\" encoding=\"UTF-8\"?><test:integers xmlns=\"http://geotools.org/test\" xmlns:test=\"http://geotools.org/test\"><test:integer>1</test:integer><test:integer>3</test:integer><test:integer>5</test:integer><test:integer>7</test:integer><test:integer>9</test:integer></test:integers>")
                .build();
        Source actual = Input.fromString(tx.transform(10)).build();

        Diff diff =
                DiffBuilder.compare(expected).withTest(actual).checkForSimilar().build();

        Assert.assertFalse(diff.toString(), diff.hasDifferences());
    }

    @Test
    public void testPassingInNull() throws TransformerException {
        ExampleTransformer tx = new ExampleTransformer(0, 0, true);
        try {
            tx.transform(null);
            Assert.fail("Expected NullPointerException but none was thrown.");
        } catch (TransformerException e) {
            // Swallow exception IFF it was due to a NullPointerException; otherwise rethrow
            if (!(e.getCause() instanceof NullPointerException)) throw e;
        }
    }
}
