/*
 * GeoTools - The Open Source Java GIS Toolkit http://geotools.org
 *
 * (C) 2017, Open Source Geospatial Foundation (OSGeo)
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; version 2.1 of
 * the License.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */
package org.geotools.ows.wmts.model;

import static org.geotools.ows.wmts.WMTSTestUtils.createCapabilities;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.logging.Logger;
import org.geotools.api.metadata.citation.Contact;
import org.geotools.api.metadata.citation.ResponsibleParty;
import org.geotools.util.SimpleInternationalString;
import org.geotools.util.logging.Logging;
import org.junit.Test;

public class WMTSServiceTest {

    static final Logger LOGGER = Logging.getLogger(WMTSServiceTest.class);

    public WMTSServiceTest() {}

    @Test
    public void testParser2() throws Exception {
        WMTSCapabilities capabilities = createCapabilities("admin_ch.getcapa.xml");
        try {
            WMTSService service = (WMTSService) capabilities.getService();
            assertNotNull(service.get_abstract());

            assertEquals("WMTS BGDI", service.getTitle());

            ResponsibleParty serviceProvider = service.getContactInformation();
            assertEquals("David Oesch", serviceProvider.getIndividualName());
            assertEquals(new SimpleInternationalString(""), serviceProvider.getPositionName());
            assertEquals(
                    new SimpleInternationalString("Bundesamt für Landestopografie swisstopo"),
                    serviceProvider.getOrganisationName());
            assertNull(serviceProvider.getRole());

            Contact contactDetails = serviceProvider.getContactInfo();
            assertEquals(1, contactDetails.getAddress().getDeliveryPoints().size());
            assertEquals(
                    new SimpleInternationalString("Bern"),
                    contactDetails.getAddress().getCity());
            assertEquals("3084", contactDetails.getAddress().getPostalCode());
            assertEquals(
                    new SimpleInternationalString("BE"),
                    contactDetails.getAddress().getAdministrativeArea());
            assertEquals(
                    new SimpleInternationalString("Switzerland"),
                    contactDetails.getAddress().getCountry());

            assertEquals(
                    1, contactDetails.getAddress().getElectronicMailAddresses().size());

            assertArrayEquals(
                    new String[] {
                        "Switzerland",
                        "Web Map Service",
                        "Schweiz",
                        "OGC",
                        "WMS",
                        "swisstopo",
                        "Bundesamt fuer Landestopografie",
                        "Landeskarte",
                        "Pixelkarte",
                        "Luftbilder",
                        "SWISSIMAGE",
                        "Grenzen",
                        "swissBOUNDARIES3D",
                        "Historische Karten",
                        "Siegfriedatlas",
                        "Dufourkarte"
                    },
                    service.getKeywordList());

            assertEquals(0, service.getMaxHeight()); // MaxHeight doesn't apply to WMTS
            assertEquals(0, service.getMaxWidth()); // MaxWidth doesn't apply to WMTS
            assertEquals(0, service.getLayerLimit()); // Layer limit doesn't apply to WMTS

        } catch (Exception e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            if (e.getMessage() != null && e.getMessage().indexOf("timed out") > 0) {
                LOGGER.warning("Unable to test - timed out: " + e);
            } else {
                throw e;
            }
        }
    }
}
