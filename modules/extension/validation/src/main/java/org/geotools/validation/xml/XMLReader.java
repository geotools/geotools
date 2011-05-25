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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.geotools.validation.dto.ArgumentDTO;
import org.geotools.validation.dto.PlugInDTO;
import org.geotools.validation.dto.TestDTO;
import org.geotools.validation.dto.TestSuiteDTO;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * Load validation configuration from XML.
 *
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 *
 * @source $URL$
 * @version $Id$
 */
public class XMLReader {
    /**
     * XMLReader constructor.
     * 
     * <p>
     * Should never be used.
     * </p>
     */
    private XMLReader() {
    }

    /**
     * readPlugIn purpose.
     * 
     * <p>
     * This method is intended to read an XML PlugIn (pluginSchema.xsd) into a
     * PlugInDTO object.
     * </p>
     *
     * @param inputSource A features which contains a copy of a valid PlugIn
     *        desciption.
     *
     * @return the resulting dto based on the input provided.
     *
     * @throws ValidationException DOCUMENT ME!
     */
    public static PlugInDTO readPlugIn(Reader inputSource)
        throws ValidationException {
        PlugInDTO dto = new PlugInDTO();

        try {
            Element elem = null;

            try {
                elem = ReaderUtils.loadConfig(inputSource);
            } catch (ParserConfigurationException pce) {
                throw new ValidationException("Cannot parse the inputSource: Cannot configure the parser.",
                    pce);
            } catch (SAXException se) {
                throw new ValidationException("Cannot parse the inputSource: Cannot configure the parser.",
                    se);
            }

            try {
                dto.setName(ReaderUtils.getElementText(
                        ReaderUtils.getChildElement(elem, "name", true), true));
            } catch (SAXException e) {
                throw new ValidationException("Error parsing name for this plugin",
                    e);
            }

            try {
                dto.setDescription(ReaderUtils.getElementText(
                        ReaderUtils.getChildElement(elem, "description", true),
                        true));
            } catch (SAXException e) {
                throw new ValidationException(
                    "Error parsing description for the " + dto.getName()
                    + " plugin", e);
            }

            try {
                dto.setClassName(ReaderUtils.getElementText(
                        ReaderUtils.getChildElement(elem, "class", true), true));
            } catch (SAXException e) {
                throw new ValidationException("Error parsing class for the "
                    + dto.getName() + " plugin", e);
            }

            NodeList nl = elem.getElementsByTagName("argument");

            if (nl != null) {
                Map m = new HashMap();
                dto.setArgs(m);

                for (int i = 0; i < nl.getLength(); i++) {
                    elem = (Element) nl.item(i);

                    ArgumentDTO adto = null;

                    try {
                        adto = loadArg(elem, dto);
                    } catch (ValidationException e) {
                        e.printStackTrace();

                        // error
                    }

                    m.put(adto.getName(), adto);
                }
            }
        } catch (IOException ioe) {
            throw new ValidationException("Cannot parse the inputSource: Cannot configure the parser.",
                ioe);
        }

        return dto;
    }

    /**
     * readTestSuiteDTO purpose.
     * 
     * <p>
     * This method is intended to read an XML Test (testSuiteSchema.xsd) into a
     * TestSuiteDTO object.
     * </p>
     *
     * @param inputSource A features which contains a copy of a valid TestSuite
     *        desciption.
     * @param plugIns A name of plugin names to valid plugin DTOs
     *
     * @return the resulting dto based on the input provided.
     *
     * @throws ValidationException DOCUMENT ME!
     */
    public static TestSuiteDTO readTestSuite(String name, Reader inputSource, Map plugIns)
        throws ValidationException {
        TestSuiteDTO dto = new TestSuiteDTO();
        try {
            Element elem = null;
            try {
                elem = ReaderUtils.loadConfig(inputSource);
            } catch (ParserConfigurationException e) {
                throw new ValidationException( "Problem parsing "+name+":"+e.getMessage(),
                    e);
            } catch (SAXException e) {
                throw new ValidationException( "XML problem with  "+name+":"+e.getMessage(),e);
            }

            try {
                dto.setName(ReaderUtils.getChildText(elem, "name", true));
            } catch (SAXException e) {
                throw new ValidationException("Error loading test suite name", e);
            }

            try {
                dto.setDescription(ReaderUtils.getChildText(elem,
                        "description", true));
            } catch (SAXException e) {
                throw new ValidationException("Error loading test suite description",
                    e);
            }

            Map l = new HashMap();
            dto.setTests(l);

            NodeList nl = elem.getElementsByTagName("test");

            if ((nl == null) || (nl.getLength() == 0)) {
                throw new ValidationException(
                    "The test suite loader has detected an error: no tests provided.");
            } else {
                for (int i = 0; i < nl.getLength(); i++) {
                    try {
                        TestDTO t = loadTestDTO((Element) nl.item(i), plugIns);
                        l.put(t.getName(), t);
                    } catch (ValidationException e) {
                        throw new ValidationException(
                            "An error occured loading a test in "
                            + dto.getName() + " test suite.", e);
                    }
                    catch (Throwable t) {
                        throw new ValidationException(
                            "Could not load test suite "
                            + dto.getName() + ":"+ t.getMessage(), t);
                    }                    
                }
            }
        } catch (IOException e) {
            throw new ValidationException("An error occured loading the "
                + dto.getName() + "test suite", e);
        }

        return dto;
    }

    /**
     * loadTestDTO purpose.
     * 
     * <p>
     * Helper method used by readTestDTO and readTestSuiteDTO
     * </p>
     *
     * @param elem The head element of a test
     * @param plugIns DOCUMENT ME!
     *
     * @return a TestDTO representing elem, null if elem is not corretly
     *         defined.
     *
     * @throws ValidationException DOCUMENT ME!
     */
    private static TestDTO loadTestDTO(Element elem, Map plugIns)
        throws ValidationException {
        TestDTO dto = new TestDTO();

        try {
            dto.setName(ReaderUtils.getChildText(elem, "name", true));
        } catch (SAXException e) {
            throw new ValidationException("Error reading the name for this test case.",
                e);
        }

        try {
            dto.setDescription(ReaderUtils.getChildText(elem, "description",
                    false));
        } catch (SAXException e) {
            throw new ValidationException(
                "Error reading the description for the " + dto.getName()
                + " test case.", e);
        }

        try {
            String pluginName = ReaderUtils.getChildText(elem, "plugin", true);
            dto.setPlugIn((PlugInDTO) plugIns.get(pluginName));

            if (dto.getPlugIn() == null) {
                throw new NullPointerException(
                    "Error - should have a plugin at "+elem);
            }
        } catch (SAXException e) {
            throw new ValidationException("Error reading the plugin for the "
                + dto.getName() + " test case.", e);
        }

        NodeList nl = elem.getElementsByTagName("argument");

        if (nl != null) {
            Map m = new HashMap();
            dto.setArgs(m);

            for (int i = 0; i < nl.getLength(); i++) {
                elem = (Element) nl.item(i);

                ArgumentDTO adto = null;

                try {
                    adto = loadArg(elem, dto.getPlugIn());
                } catch (ValidationException e) {
                    e.printStackTrace();

                    // error
                }

                if ((adto == null) || !adto.isFinal()) {
                    m.put(adto.getName(), adto);
                }
            }
        }

        return dto;
    }

    private static ArgumentDTO loadArg(Element elem, PlugInDTO dto)
        throws ValidationException {
        String key = "";
        boolean _fixed = false;

        try {
            _fixed = ReaderUtils.getBooleanAttribute(elem, "final", false);
            key = ReaderUtils.getChildText(elem, "name", true);
        } catch (SAXException e) {
            throw new ValidationException("Error reading argument for "
                + dto.getName() + " :name required");
        }

        NodeList nl2 = elem.getChildNodes();
        Element value = null;

        for (int j = 0; j < nl2.getLength(); j++) {
            if (nl2.item(j).getNodeType() == Node.ELEMENT_NODE) {
                elem = (Element) nl2.item(j);

                if (elem.getTagName().trim().equals("name")) {
                    value = elem;
                } else {
                    value = elem;
                }
            }
        }

        if (value == null) {
            throw new ValidationException("Invalid Argument \"" + dto.getName()
                + "\" for argument \"" + key + "\"");
        }

        ArgumentDTO adto = (ArgumentDTO) dto.getArgs().get(key);

        // elem whould have the value now
        Object val = ArgHelper.getArgumentInstance(value.getTagName().trim(),
                value);

        if (val == null) {
            throw new ValidationException(
                "Didn't find a real value for argument " + key);
        }

        if (adto == null) {
            adto = new ArgumentDTO();
        } else {
            adto = (ArgumentDTO) adto.clone();
        }

        adto.setName(key);
        adto.setValue(val);
        adto.setFinal(_fixed);

        return adto;
    }

    /**
     * loadPlugIns purpose.
     * 
     * <p>
     * Loads all the plugins in the directory
     * </p>
     *
     * @param plugInDir
     *
     *
     * @throws ValidationException DOCUMENT ME!
     */
    public static Map loadPlugIns(File plugInDir) throws ValidationException {
        Map r = null;

        try {
            plugInDir = ReaderUtils.initFile(plugInDir, true);

            File[] fileList = plugInDir.listFiles();
            r = new HashMap();

            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].canWrite() && fileList[i].isFile()) {
                    FileReader fr = new FileReader(fileList[i]);
                    PlugInDTO dto = XMLReader.readPlugIn(fr);
                    r.put(dto.getName(), dto);
                    fr.close();
                }
            }
        } catch (IOException e) {
            throw new ValidationException("An io error occured while loading the plugin's",
                e);
        }

        return r;
    }

    /**
     * Loads all the test suites in the validations directory
     * </p>
     *
     * @param validationDir
     * @param plugInDTOs Already loaded list of plug-ins to link.
     *
     *
     * @throws ValidationException DOCUMENT ME!
     */
    public static Map loadValidations(File validationDir, Map plugInDTOs)
        throws ValidationException {
        Map r = null;

        try {
            validationDir = ReaderUtils.initFile(validationDir, true);
        }
        catch( IOException dirException ){
            throw new ValidationException("Problem opening "+validationDir.getName(),
                    dirException);            
        }
        File[] fileList = validationDir.listFiles();
        r = new HashMap();

        for (int i = 0; i < fileList.length; i++) {
            try {
                if (fileList[i].canWrite() && fileList[i].isFile()) {
                    FileReader fr = new FileReader(fileList[i]);
                    try {
                        TestSuiteDTO dto = XMLReader.readTestSuite( fileList[i].getName(), fr, plugInDTOs);
                        r.put(dto.getName(), dto);                        
                    }
                    finally {                    
                        fr.close();
                    }
                }
            }
            catch (IOException open) {
                throw new ValidationException("Could not open "+fileList[i].getName(),
                    open);
            }
        }
        return r;
    }
}
