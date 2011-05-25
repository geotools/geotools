/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.validation.xml;

import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.geotools.validation.dto.ArgumentDTO;
import org.geotools.validation.dto.PlugInDTO;
import org.geotools.validation.dto.TestDTO;
import org.geotools.validation.dto.TestSuiteDTO;


/**
 * XMLWriter purpose.
 * 
 * <p>
 * Description of XMLWriter ...
 * </p>
 * 
 * <p>
 * Capabilities:
 * </p>
 * 
 * <ul>
 * <li>
 * Feature: description
 * </li>
 * </ul>
 * 
 * <p>
 * Example Use:
 * </p>
 * <pre><code>
 * XMLWriter x = new XMLWriter(...);
 * </code></pre>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 *
 * @source $URL$
 * @version $Id$
 */
public class XMLWriter {
    public static void writePlugIn(PlugInDTO dto, Writer w) {
        WriterUtils cw = new WriterUtils(w);
        Map m = new HashMap();
        m.put("xmlns", "pluginSchema");
        m.put("xmlns:gml", "http://www.opengis.net/gml");
        m.put("xmlns:ogc", "http://www.opengis.net/ogc");
        m.put("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        m.put("xsi:schemaLocation",
            "pluginSchema /data/capabilities/validate/pluginSchema.xsd");

        try {
            cw.openTag("plugin", m);

            try {
                cw.textTag("name", dto.getName());
                cw.textTag("description", dto.getDescription());
                cw.textTag("class", dto.getClassName());

                Iterator i = dto.getArgs().keySet().iterator();

                while (i.hasNext()) {
                    writeArgument((ArgumentDTO) dto.getArgs().get(i.next()), w);
                }
            } catch (Exception e) {
                e.printStackTrace();

                // 	error log it;
            }

            cw.closeTag("plugin");
        } catch (Exception e) {
            e.printStackTrace();

            // error log it;
        }
    }

    public static void writeTest(TestDTO dto, Writer w) {
        WriterUtils cw = new WriterUtils(w);

        try {
            cw.openTag("test");

            try {
                cw.textTag("name", dto.getName());
                cw.textTag("description", dto.getDescription());
                cw.textTag("plugin", dto.getPlugIn().getName());

                Iterator i = dto.getArgs().keySet().iterator();

                while (i.hasNext()) {
                    writeArgument((ArgumentDTO) dto.getArgs().get(i.next()), w);
                }
            } catch (Exception e) {
                e.printStackTrace();

                // 	error log it;
            }

            cw.closeTag("test");
        } catch (Exception e) {
            e.printStackTrace();

            // error log it;
        }
    }

    public static void writeTestSuite(TestSuiteDTO dto, Writer w) {
        WriterUtils cw = new WriterUtils(w);
        Map m = new HashMap();
        m.put("xmlns", "testSuiteSchema");
        m.put("xmlns:gml", "http://www.opengis.net/gml");
        m.put("xmlns:ogc", "http://www.opengis.net/ogc");
        m.put("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        m.put("xsi:schemaLocation",
            "testSuiteSchema /data/capabilities/validate/testSuiteSchema.xsd");

        try {
            cw.openTag("suite", m);

            try {
                cw.textTag("name", dto.getName());
                cw.textTag("description", dto.getDescription());

                Iterator i = dto.getTests().keySet().iterator();

                while (i.hasNext()) {
                    writeTest((TestDTO) dto.getTests().get(i.next()), w);
                }
            } catch (Exception e) {
                e.printStackTrace();

                // 	error log it;
            }

            cw.closeTag("suite");
        } catch (Exception e) {
            e.printStackTrace();

            // error log it;
        }
    }

    public static void writeArgument(ArgumentDTO dto, Writer w) {
        WriterUtils cw = new WriterUtils(w);
        Map m = new HashMap();

        if (dto.isFinal()) {
            m.put("final", new Boolean(true));
        }

        try {
            cw.openTag("argument", m);

            try {
                cw.textTag("name", dto.getName());
                cw.writeln(ArgHelper.getArgumentEncoding(dto.getValue()));
            } catch (Exception e) {
            	// do nothing, just don't write it out
                e.printStackTrace();

                // error log it;
            }

            cw.closeTag("argument");
        } catch (Exception e) {
            e.printStackTrace();

            // error log it;
        }
    }
}
