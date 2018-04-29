/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
 */ package org.geotools.coverage.io.netcdf;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import javax.imageio.ImageReader;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.geotools.imageio.netcdf.NetCDFImageReaderSpi;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPath;

/**
 * CommandLine Utility to be used in order to create an ImageMosaic indexer.xml as well as the
 * related NetCDF low-level indexer (_auxiliary.xml) for a prototype NetCDF.
 *
 * <p>Arguments to be provided are, in the following order: " /path/to/sampleFile.nc " " -p
 * /path/to/netcdfprojectionsfile" " [/path/to/optional/outputFolder]"
 *
 * @author Daniele Romagnoli, GeoSolutions SAS
 */
public class CreateIndexer {

    private static final String ELEVATION_ATTRIB_TYPE_FLOAT = "java.lang.Float";

    private static final String ELEVATION_ATTRIB_TYPE_DOUBLE = "java.lang.Double";

    private static final String TIME_ATTRIB_TYPE = "java.util.Date";

    static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(CreateIndexer.class);

    public static void main(String[] args) throws JDOMException, IOException, TransformerException {
        if (args.length < 1) {
            System.out.println(
                    "Usage: java -jar CreateIndexer"
                            + " /path/to/sampleFile.nc "
                            + "/path/to/netcdfprojectionsfile "
                            + "[/path/to/optional/outputFolder]\n");
            System.exit(1);
        }
        if (!args[1].equalsIgnoreCase("-P")) {
            System.out.println(
                    " The second parameter needs to be " + "-p /path/to/netcdfprojectionsfile\n");
            System.exit(1);
        }
        String sampleFilePath = args[0];
        String projectionFilePath = args[2];
        System.out.println("Setting netcdf.projections.file = " + projectionFilePath);
        System.setProperty("netcdf.projections.file", projectionFilePath);

        // Force the quickscan properties so that only 1 slice for NetCDF variable
        // will be used at time of NetCDF index creation.
        System.setProperty("org.geotools.netcdf.quickscan", "TRUE");

        File sampleFile = new File(sampleFilePath);
        File temp = File.createTempFile("XML", "NC");
        if (!(temp.delete())) {
            throw new IOException("Could not delete temp file: " + temp.getAbsolutePath());
        }

        if (!(temp.mkdir())) {
            throw new IOException("Could not create temp directory: " + temp.getAbsolutePath());
        }

        System.setProperty("NETCDF_DATA_DIR", temp.getAbsolutePath());
        System.out.println("Reading sample file: " + sampleFilePath);

        ImageReader reader = new NetCDFImageReaderSpi().createReaderInstance();
        reader.setInput(sampleFile);
        reader.dispose();

        File[] files = temp.listFiles((FileFilter) FileFilterUtils.directoryFileFilter());
        files = files[0].listFiles((FileFilter) FileFilterUtils.suffixFileFilter("xml"));

        final File auxiliaryFile = files[0];
        String parentFolder = setOuputFolder(args, sampleFile);
        String indexerFilePath = parentFolder + File.separatorChar + "indexer.xml";
        String auxiliaryFilePath = parentFolder + File.separatorChar + "_auxiliary.xml";
        final File finalAuxFile = new File(auxiliaryFilePath);
        formatAuxiliaryXml(auxiliaryFile, finalAuxFile);

        System.out.println("Grabbing the generated xml: " + finalAuxFile);

        SAXBuilder saxBuilder = new SAXBuilder();
        Document doc = saxBuilder.build(finalAuxFile);
        Element root = doc.getRootElement();
        Set<String> timeAttributes = new HashSet<String>();
        Set<String> elevationAttributes = new HashSet<String>();
        getAttributes(timeAttributes, elevationAttributes, root);
        final StringBuilder builder =
                new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        builder.append("<Indexer>\n");
        setDomains(timeAttributes, elevationAttributes, builder);
        boolean longNameFound = setCoverages(root, builder);
        setParameters(auxiliaryFilePath, builder, longNameFound);

        writeIndexer(builder.toString(), indexerFilePath);
        System.out.println("Deleting temporary folder");
        if (!(FileUtils.deleteQuietly(temp))) {
            System.out.println("Unable to delete folder: " + temp);
        }
        System.out.println("DONE!!");
    }

    private static String setOuputFolder(String[] args, File sampleFile) {
        String outputPath = FilenameUtils.getFullPathNoEndSeparator(sampleFile.getAbsolutePath());
        if (args.length > 3) {
            outputPath = args[3];
            System.out.println("Output folder has been specified: " + outputPath);
            final File outputFolder = new File(outputPath);
            if (!outputFolder.exists()) {
                System.out.println("Creating it");
                outputFolder.mkdirs();
            }
        } else {
            System.out.println(
                    "Output folder hasn't been specified. The files will be created beside the sample file, at: "
                            + outputPath);
        }
        return outputPath;
    }

    private static void writeIndexer(String xml, String indexerFilePath)
            throws FileNotFoundException {
        System.out.println("Writing the indexer.xml: " + indexerFilePath);
        PrintWriter out = new PrintWriter(indexerFilePath);
        out.println(xml);
        out.flush();
        out.close();
    }

    private static void formatAuxiliaryXml(File auxiliaryFile, File finalAuxFile)
            throws FileNotFoundException, TransformerException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        // initialize StreamResult with File object to save to file
        StreamResult result = new StreamResult(new StringWriter());
        InputStream is = null;
        PrintWriter out = null;
        try {
            is = new FileInputStream(auxiliaryFile);

            transformer.transform(new StreamSource(is), result);
            String xmlString = result.getWriter().toString();

            out = new PrintWriter(finalAuxFile);
            out.println(xmlString);
            out.flush();

        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    // does nothing
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    // does nothing
                }
            }
        }
    }

    private static void setParameters(
            String auxiliaryFilePath, StringBuilder builder, boolean longNameFound) {
        builder.append("  <parameters>\n");
        builder.append(
                "    <parameter name=\"AuxiliaryFile\" value=\"" + auxiliaryFilePath + "\" />\n");
        builder.append("    <parameter name=\"AbsolutePath\" value=\"true\" />\n");
        if (longNameFound) {
            builder.append("    <parameter name=\"WrapStore\" value=\"true\" />\n");
        }
        builder.append("  </parameters>\n");
        builder.append("</Indexer>\n");
    }

    private static boolean setCoverages(Element root, StringBuilder builder) throws JDOMException {
        builder.append("  <coverages>\n");
        List<?> coverages = XPath.selectNodes(root, "coverages/coverage");
        boolean longName = false;
        for (Object cov : coverages) {
            if (cov instanceof Element) {
                if (setCoverage(((Element) cov), builder)) {
                    longName = true;
                }
            }
        }
        builder.append("  </coverages>\n");
        return longName;
    }

    private static boolean setCoverage(Element cov, StringBuilder builder) throws JDOMException {
        builder.append("    <coverage>\n");
        Element name = (Element) XPath.selectSingleNode(cov, "name");
        String coverageName = name.getText();
        builder.append("      <name>" + coverageName + "</name>\n");

        Element schema = (Element) XPath.selectSingleNode(cov, "schema");
        String schemaName = schema.getAttributeValue("name");
        builder.append("      <schema name=\"" + schemaName + "\" >\n");
        Element schemaAttributesElement = (Element) XPath.selectSingleNode(schema, "attributes");
        String schemaAttribs = schemaAttributesElement.getText();
        schemaAttribs =
                schemaAttribs.replace("imageindex:Integer", "imageindex:Integer,location:String");
        builder.append("        <attributes>" + schemaAttribs + "</attributes>\n");
        builder.append("      </schema>\n");

        addDomainsToCoverage(schemaAttribs, builder);
        builder.append("    </coverage>\n");
        return coverageName.length() > 62;
    }

    private static void addDomainsToCoverage(String schemaAttribs, StringBuilder builder) {
        Set<String> domains = new HashSet<String>();
        String[] schemaAttributesValues = schemaAttribs.split(",");
        for (String schemaAttr : schemaAttributesValues) {
            if (schemaAttr.contains(TIME_ATTRIB_TYPE)) {
                String[] nameTypePair = schemaAttr.split(":");
                domains.add(nameTypePair[0]);
            }
            if (schemaAttr.contains(ELEVATION_ATTRIB_TYPE_FLOAT)
                    || schemaAttr.contains(ELEVATION_ATTRIB_TYPE_DOUBLE)) {
                String[] nameTypePair = schemaAttr.split(":");
                domains.add(nameTypePair[0]);
            }
        }
        if (!domains.isEmpty()) {
            builder.append("      <domains>\n");
            Iterator<String> it = domains.iterator();
            while (it.hasNext()) {
                builder.append("        <domain ref=\"" + it.next() + "\" />\n");
            }
            builder.append("      </domains>\n");
        }
    }

    private static void getAttributes(
            Set<String> timeAttributes, Set<String> elevationAttributes, Element root)
            throws JDOMException {
        List<?> schemaAttributes = XPath.selectNodes(root, "coverages/coverage/schema/attributes");

        for (Object e : schemaAttributes) {
            if (e instanceof Element) {
                String attributes = ((Element) e).getText();
                String[] attribs = attributes.split(",");
                for (String attrib : attribs) {
                    if (attrib.contains(TIME_ATTRIB_TYPE)) {
                        String[] nameTypePair = attrib.split(":");
                        String name = nameTypePair[0];
                        if (!timeAttributes.contains(name)) {
                            timeAttributes.add(name);
                        }

                    } else if (attrib.contains(ELEVATION_ATTRIB_TYPE_FLOAT)
                            || attrib.contains(ELEVATION_ATTRIB_TYPE_DOUBLE)) {
                        String[] nameTypePair = attrib.split(":");
                        String name = nameTypePair[0];
                        if (!elevationAttributes.contains(name)) {
                            elevationAttributes.add(name);
                        }
                    }
                }
            }
        }
    }

    private static void setDomains(
            Set<String> timeAttributes, Set<String> elevationAttributes, StringBuilder builder) {
        builder.append("  <domains>\n");
        for (String timeDomain : timeAttributes) {
            setDomain(builder, timeDomain);
        }
        for (String elevationDomain : elevationAttributes) {
            setDomain(builder, elevationDomain);
        }
        builder.append("  </domains>\n");
    }

    private static void setDomain(StringBuilder builder, String domain) {
        builder.append("    <domain name=\"" + domain + "\">\n");
        builder.append("      <attributes><attribute>" + domain + "</attribute></attributes>\n");
        builder.append("    </domain>\n");
    }
}
