package org.geotools.data.wfs.internal;

import net.opengis.wfs.WFSCapabilitiesType;

import org.eclipse.emf.ecore.EObject;
import org.geotools.data.ows.Capabilities;
import org.geotools.xml.EMFUtils;
import org.w3c.dom.Document;

public class WFSGetCapabilities extends Capabilities {

    protected final Document rawDocument;

    protected final EObject capabilities;

    private WFSGetCapabilities(EObject capabilities, Document rawDocument) {
        this.capabilities = capabilities;
        this.rawDocument = rawDocument;
        try {
            String updateSequence = (String) EMFUtils.get(capabilities, "updateSequence");
            setUpdateSequence(updateSequence);
        } catch (Exception e) {
            //
        }
    }

    public Document getRawDocument() {
        return rawDocument;
    }

    public EObject getParsedCapabilities() {
        return capabilities;
    }

    public static WFSGetCapabilities create(EObject capabilities, Document rawDocument) {
        if (capabilities instanceof WFSCapabilitiesType) {

            WFSCapabilitiesType caps = (WFSCapabilitiesType) capabilities;
            String version = rawDocument.getDocumentElement().getAttribute("version");
            if ("1.0.0".equals(version)) {
                return new WFS_1_0_0(caps, rawDocument);
            }
            if ("1.1.0".equals(version)) {
                return new WFS_1_1_0(caps, rawDocument);
            }
            throw new IllegalArgumentException("Unknown version: " + version);
        } else if (capabilities instanceof net.opengis.wfs20.WFSCapabilitiesType) {

            return new WFS_2_0_0((net.opengis.wfs20.WFSCapabilitiesType) capabilities, rawDocument);
        }
        throw new IllegalArgumentException("Unrecognized capabilities object: " + capabilities);
    }

    private static class WFS_1_0_0 extends WFSGetCapabilities {

        public WFS_1_0_0(WFSCapabilitiesType caps, Document rawDocument) {
            super(caps, rawDocument);
            setVersion("1.0.0");
        }
    }

    private static class WFS_1_1_0 extends WFSGetCapabilities {
        public WFS_1_1_0(WFSCapabilitiesType capabilities, Document rawDocument) {
            super(capabilities, rawDocument);
            setVersion("1.1.0");
        }
    }

    private static class WFS_2_0_0 extends WFSGetCapabilities {
        public WFS_2_0_0(net.opengis.wfs20.WFSCapabilitiesType capabilities, Document rawDocument) {
            super(capabilities, rawDocument);
            setVersion("2.0.0");
        }
    }
}
