/*    (C) 2014 Open Source Geospaital Foundation (OSGeo)
 *    (C) 2012, Refractions Research Inc.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * (http://www.eclipse.org/legal/epl-v10.html), and the Refractions BSD
 * License v1.0 (http://udig.refractions.net/files/bsd3-v10.html).
 */
package docs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ConfluenceDom {

    private Document dom;

    private Element docEle;

    private Map<String, ConfluencePage> pageList = new HashMap<String, ConfluencePage>();

    // private List<String> titleList = new ArrayList<String>();

    // private String textileLocation = "textile/";
    private String htmlLocation = "html/";

    public ConfluenceDom(File xmlFileLocation) {

        // get the factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            // Using factory get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();

            // parse using builder to get DOM representation of the XML file
            dom = db.parse(xmlFileLocation); // "entities.xml"
            docEle = dom.getDocumentElement();

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void writeCurrentPages(File location) {
        processXML();
        fixAllHeaders();

        for (ConfluencePage tempPage : pageList.values()) {
            if (tempPage.isCurrent()) {
                tempPage.writeHTML(location);

                // test for duplicate titles
                /*
                 * if (titleList.contains(tempPage.getTitle())) { System.out.println("Duplicate: " +
                 * tempPage.getTitle()); } else { titleList.add(tempPage.getTitle()); }
                 */
            }
        }

        // test for duplicate titles
        /*
         * int count = 0; for (String pageTitle: titleList) { count++; System.out.println(count +
         * "--> " + pageTitle); }
         */

        // writeToc(location);
    }

    private void processXML() {
        getAllPages();
        updatePageContent();
    }

    private void fixAllHeaders() {
        for (ConfluencePage tempPage : pageList.values()) {
            tempPage.fixHTML();
        }
    }

    private void getAllPages() {

        NodeList objectList = docEle.getElementsByTagName("object");
        if (objectList != null && objectList.getLength() > 0) {

            for (int i = 0; i < objectList.getLength(); i++) {
                // find a page
                Element objectItem = (Element) objectList.item(i);
                if (objectItem.getAttribute("class").equalsIgnoreCase("Page")) {
                    // temp page
                    ConfluencePage tempPage = new ConfluencePage();

                    // get the properties
                    NodeList pagePropertyList = objectItem.getElementsByTagName("property");
                    for (int j = 0; j < pagePropertyList.getLength(); j++) {
                        Element pagePropertyItem = (Element) pagePropertyList.item(j);
                        // get page details
                        if (pagePropertyItem.getAttribute("name").equalsIgnoreCase("title")) {
                            tempPage.setTitle(pagePropertyItem.getTextContent());
                        }
                        if (pagePropertyItem.getAttribute("name").equalsIgnoreCase("contentStatus")) {
                            tempPage.setStatus(pagePropertyItem.getTextContent());
                        }
                        if (pagePropertyItem.getAttribute("name").equalsIgnoreCase("version")) {
                            tempPage.setVersion(pagePropertyItem.getTextContent());
                        }
                        if (pagePropertyItem.getAttribute("name").equalsIgnoreCase(
                                "originalVersion")) {
                            tempPage.setOriginalVersion(true);
                        }
                        if (pagePropertyItem.getAttribute("name").equalsIgnoreCase("parent")) {
                            NodeList pageIdList = pagePropertyItem.getElementsByTagName("id");
                            for (int k = 0; k < pageIdList.getLength(); k++) {
                                Element pageId = (Element) pageIdList.item(k);
                                tempPage.setParent(pageId.getTextContent());
                            }
                        }
                    }

                    // check if it has historical versions
                    NodeList collectionList = objectItem.getElementsByTagName("collection");
                    for (int j = 0; j < collectionList.getLength(); j++) {
                        Element collectionItem = (Element) collectionList.item(j);
                        if (collectionItem.getAttribute("name").equalsIgnoreCase(
                                "historicalVersions")) {
                            tempPage.setHistoricalVersions(true);
                        }
                    }

                    // get the page id
                    NodeList pageIdList = objectItem.getElementsByTagName("id");
                    tempPage.setId(pageIdList.item(0).getTextContent());

                    // add to page list
                    pageList.put(tempPage.getId(), tempPage);
                }
            }
        }
    }

    private void updatePageContent() {
        ConfluencePage tempPage = new ConfluencePage();

        NodeList objectList = docEle.getElementsByTagName("object");
        if (objectList != null && objectList.getLength() > 0) {

            for (int i = 0; i < objectList.getLength(); i++) {
                // find a page content
                Element objectItem = (Element) objectList.item(i);
                if (objectItem.getAttribute("class").equalsIgnoreCase("BodyContent")) {
                    NodeList pagePropertyList = objectItem.getElementsByTagName("property");
                    for (int j = 0; j < pagePropertyList.getLength(); j++) {
                        Element pagePropertyItem = (Element) pagePropertyList.item(j);
                        // get body content
                        if (pagePropertyItem.getAttribute("name").equalsIgnoreCase("body")) {
                            tempPage.setBodyText(pagePropertyItem.getTextContent());
                        }

                        if (pagePropertyItem.getAttribute("name").equalsIgnoreCase("content")) {
                            NodeList pageIdList = pagePropertyItem.getElementsByTagName("id");
                            for (int k = 0; k < pageIdList.getLength(); k++) {
                                Element pageId = (Element) pageIdList.item(k);
                                tempPage.setId(pageId.getTextContent());
                            }
                        }

                    }

                    if (pageList.containsKey(tempPage.getId())) {
                        // update page content
                        ConfluencePage setPage = new ConfluencePage();
                        setPage = pageList.get(tempPage.getId());
                        // System.out.println(tempPage.getId());
                        // System.out.println(setPage.getId());
                        setPage.setBodyText(tempPage.getBodyText());

                        // update list
                        pageList.put(tempPage.getId(), setPage);
                    }
                }
            }
        }
    }


}
