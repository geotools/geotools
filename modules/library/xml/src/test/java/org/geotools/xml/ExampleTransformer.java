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

import org.geotools.xml.transform.TransformerBase;
import org.geotools.xml.transform.Translator;
import org.xml.sax.ContentHandler;

public class ExampleTransformer extends TransformerBase {
    private final int bufferEveryNth;
    private final int exceptionEveryNth;
    private final boolean ignoreErrors;

    public ExampleTransformer(int bufferEveryNth, int exceptionEveryNth, boolean ignoreErrors) {
        this.bufferEveryNth = bufferEveryNth;
        this.exceptionEveryNth = exceptionEveryNth;
        this.ignoreErrors = ignoreErrors;
    }

    @Override
    public Translator createTranslator(ContentHandler handler) {
        return new ExampleTranslator(handler);
    }

    private class ExampleTranslator extends TranslatorSupport {
        public ExampleTranslator(ContentHandler handler) {
            super(handler, "test", "http://geotools.org/test");
        }

        @Override
        public void encode(Object o) {
            Integer i = (Integer) o;
            start("integers");
            for (int j = 1; j <= i; j++) {
                boolean buffer = bufferEveryNth != 0 && j % bufferEveryNth == 0;
                boolean exception = exceptionEveryNth != 0 && j % exceptionEveryNth == 0;

                try {
                    if (buffer) mark();
                    element("integer", String.valueOf(j));
                    if (exception) throw new RuntimeException();
                    if (buffer) commit();
                } catch (RuntimeException e) {
                    if (!ignoreErrors) throw e;
                } finally {
                    reset();
                }
            }

            end("integers");
        }
    }
}
