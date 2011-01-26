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
package org.geotools.wfs.bindings;

import java.math.BigInteger;
import java.net.URL;

import javax.xml.namespace.QName;

import net.opengis.wfs.ActionType;
import net.opengis.wfs.InsertResultsType;
import net.opengis.wfs.InsertedFeatureType;
import net.opengis.wfs.TransactionResponseType;
import net.opengis.wfs.TransactionResultsType;
import net.opengis.wfs.TransactionSummaryType;

import org.geotools.filter.v1_1.OGC;
import org.geotools.test.TestData;
import org.geotools.wfs.WFS;
import org.geotools.wfs.WFSTestSupport;
import org.geotools.xml.Binding;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Unit test suite for {@link TransactionResponseTypeBinding}
 * 
 * @author Gabriel Roldan (TOPP)
 * @version $Id$
 * @since 2.5.x
 * @source $URL$
 */
public class TransactionResponseTypeBindingTest extends WFSTestSupport {

    public TransactionResponseTypeBindingTest() {
        super(WFS.TransactionResponseType, TransactionResponseType.class, Binding.OVERRIDE);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void testEncode() throws Exception {
        final TransactionResponseType tr = factory.createTransactionResponseType();
        {
            tr.setVersion("1.1.0");

            TransactionSummaryType summary = factory.createTransactionSummaryType();
            summary.setTotalDeleted(BigInteger.valueOf(2));
            summary.setTotalInserted(BigInteger.valueOf(3));
            summary.setTotalUpdated(BigInteger.valueOf(4));
            tr.setTransactionSummary(summary);

            TransactionResultsType results = factory.createTransactionResultsType();
            ActionType action = factory.createActionType();
            action.setCode("actionCode");
            action.setLocator("actionLocator");
            action.setMessage("actionMessage");
            results.getAction().add(action);
            tr.setTransactionResults(results);

            InsertResultsType insertResults = factory.createInsertResultsType();
            InsertedFeatureType feature = factory.createInsertedFeatureType();
            feature.setHandle("handle1");
            feature.getFeatureId().add(filterFac.featureId("fid1"));
            feature.getFeatureId().add(filterFac.featureId("fid2"));
            insertResults.getFeature().add(feature);
            tr.setInsertResults(insertResults);
        }

        final Document dom = encode(tr, WFS.TransactionResponse);
        final Element root = dom.getDocumentElement();

        assertName(WFS.TransactionResponse, root);
        assertEquals("1.1.0", root.getAttribute("version"));

        assertTransactionSummary(root);
        assertTransactionResults(root);
        assertInsertResults(root);
    }

    private void assertInsertResults(final Element root) {
        Element inserts = getElementByQName(root, new QName(WFS.NAMESPACE, "InsertResults"));
        NodeList features = getElementsByQName(inserts, new QName(WFS.NAMESPACE, "Feature"));
        assertEquals(1, features.getLength());
        Element f = (Element) features.item(0);
        assertEquals("handle1", f.getAttribute("handle"));
        assertEquals(2, getElementsByQName(f, OGC.FeatureId).getLength());
    }

    private void assertTransactionResults(final Element root) {
        Element results = getElementByQName(root, new QName(WFS.NAMESPACE, "TransactionResults"));
        NodeList actions = getElementsByQName(results, new QName(WFS.NAMESPACE, "Action"));
        assertEquals(1, actions.getLength());
        Element action = (Element) actions.item(0);
        assertEquals("actionCode", action.getAttribute("code"));
        assertEquals("actionLocator", action.getAttribute("locator"));
        Element message = getElementByQName(action, new QName(WFS.NAMESPACE, "Message"));
        assertEquals("actionMessage", message.getFirstChild().getNodeValue());
    }

    private void assertTransactionSummary(final Element root) {
        Element summary = getElementByQName(root, new QName(WFS.NAMESPACE, "TransactionSummary"));
        Element totalDeleted = getElementByQName(summary, new QName(WFS.NAMESPACE, "totalDeleted"));
        assertEquals("2", totalDeleted.getFirstChild().getNodeValue());

        Element totalInserted = getElementByQName(summary,
                new QName(WFS.NAMESPACE, "totalInserted"));
        assertEquals("3", totalInserted.getFirstChild().getNodeValue());

        Element totalUpdated = getElementByQName(summary, new QName(WFS.NAMESPACE, "totalUpdated"));
        assertEquals("4", totalUpdated.getFirstChild().getNodeValue());
    }

    @Override
    public void testParse() throws Exception {
        final URL resource = TestData.getResource(this, "TransactionResponseTypeBindingTest.xml");
        buildDocument(resource);

        Object parsed = parse(WFS.LockFeatureType);
        assertTrue(parsed instanceof TransactionResponseType);

        final TransactionResponseType tr = (TransactionResponseType) parsed;
        assertEquals("1.1.0", tr.getVersion());
        {
            TransactionSummaryType summary = tr.getTransactionSummary();

            assertNotNull(summary);

            assertNotNull(summary.getTotalInserted());
            assertNotNull(summary.getTotalDeleted());
            assertNotNull(summary.getTotalUpdated());

            assertEquals(3, summary.getTotalInserted().intValue());
            assertEquals(2, summary.getTotalUpdated().intValue());
            assertEquals(1, summary.getTotalDeleted().intValue());
        }

        {
            TransactionResultsType results = tr.getTransactionResults();
            assertEquals(2, results.getAction().size());
            {
                ActionType action1 = (ActionType) results.getAction().get(0);
                assertEquals("locator.1", action1.getLocator());
                assertNull(action1.getCode());
                assertEquals("success", action1.getMessage());
            }
            {
                ActionType action2 = (ActionType) results.getAction().get(1);
                assertEquals("locator.2", action2.getLocator());
                assertEquals("errorCode", action2.getCode());
                assertEquals("failure", action2.getMessage());
            }
        }
        {
            InsertResultsType insertResults = tr.getInsertResults();
            assertEquals(2, insertResults.getFeature().size());
            InsertedFeatureType feature1 = (InsertedFeatureType) insertResults.getFeature().get(0);
            assertEquals("handle1", feature1.getHandle());
            assertEquals(2, feature1.getFeatureId().size());

            InsertedFeatureType feature2 = (InsertedFeatureType) insertResults.getFeature().get(1);
            assertEquals("handle2", feature2.getHandle());
            assertEquals(1, feature2.getFeatureId().size());
        }
    }
}
