/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
 *
 */

package org.geotools.wmts;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import javax.xml.namespace.QName;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDSchemaQueryTools;
import org.geotools.ows.v1_1.OWS;
import org.geotools.xsd.XSD;

/**
 * This interface contains the qualified names of all the types,elements, and attributes in the
 * http://www.opengis.net/wmts/1.0 schema.
 *
 * @generated
 */
public final class WMTS extends XSD {

    @Override
    protected XSDSchema buildSchema() throws IOException {
        XSDSchema theSchema = super.buildSchema();

        // This is the WMTS schema, so wherever we see datasetSummaryDescription element, we
        // need to use the layer definition to decode/encode it
        List<XSDElementDeclaration> datasetSummaryBaseTypeDeclarations =
                XSDSchemaQueryTools.findElementsUsingType(
                        theSchema, OWS.NAMESPACE, "DatasetDescriptionSummaryBaseType");

        XSDElementDeclaration layerDeclaration = null;
        XSDElementDeclaration dsSummaryDeclaration = null;
        for (XSDElementDeclaration decl : datasetSummaryBaseTypeDeclarations) {
            if (decl.getName() != null && decl.getName().equals("Layer")) {
                layerDeclaration = decl;
            }
            if (decl.getName() != null && decl.getName().equals("DatasetDescriptionSummary")) {
                dsSummaryDeclaration = decl;
            }
        }

        if (dsSummaryDeclaration != null && layerDeclaration != null) {
            dsSummaryDeclaration.setResolvedElementDeclaration(layerDeclaration);
        }

        return theSchema;
    }

    /** singleton instance */
    private static final WMTS instance = new WMTS();

    /** Returns the singleton instance. */
    public static final WMTS getInstance() {
        return instance;
    }

    /** private constructor */
    private WMTS() {}

    protected void addDependencies(Set dependencies) {
        dependencies.add(org.geotools.ows.v1_1.OWS.getInstance());

        super.addDependencies(dependencies);
    }

    /** Returns 'http://www.opengis.net/wmts/1.0'. */
    public String getNamespaceURI() {
        return NAMESPACE;
    }

    /** Returns the location of 'wmts.xsd.'. */
    public String getSchemaLocation() {
        return getClass().getResource("wmtsKVP.xsd").toString();
    }

    /** @generated */
    public static final String NAMESPACE = "http://www.opengis.net/wmts/1.0";

    public static final String WMTSGetCapabilitiesResponseSchemalocation =
            "http://schemas.opengis.net/wmts/1.0/wmtsGetCapabilities_response.xsd";

    /* Type Definitions */
    /** @generated */
    public static final QName AcceptedFormatsType =
            new QName("http://www.opengis.net/wmts/1.0", "AcceptedFormatsType");

    /** @generated */
    public static final QName ContentsType =
            new QName("http://www.opengis.net/wmts/1.0", "ContentsType");

    /** @generated */
    public static final QName GetCapabilitiesValueType =
            new QName("http://www.opengis.net/wmts/1.0", "GetCapabilitiesValueType");

    /** @generated */
    public static final QName GetFeatureInfoValueType =
            new QName("http://www.opengis.net/wmts/1.0", "GetFeatureInfoValueType");

    /** @generated */
    public static final QName GetTileValueType =
            new QName("http://www.opengis.net/wmts/1.0", "GetTileValueType");

    /** @generated */
    public static final QName LayerType = new QName("http://www.opengis.net/wmts/1.0", "LayerType");

    /** @generated */
    public static final QName RequestServiceType =
            new QName("http://www.opengis.net/wmts/1.0", "RequestServiceType");

    /** @generated */
    public static final QName SectionsType =
            new QName("http://www.opengis.net/wmts/1.0", "SectionsType");

    /** @generated */
    public static final QName URLTemplateType =
            new QName("http://www.opengis.net/wmts/1.0", "URLTemplateType");

    /** @generated */
    public static final QName VersionType =
            new QName("http://www.opengis.net/wmts/1.0", "VersionType");

    /** @generated */
    public static final QName _BinaryPayload =
            new QName("http://www.opengis.net/wmts/1.0", "_BinaryPayload");

    /** @generated */
    public static final QName _Capabilities =
            new QName("http://www.opengis.net/wmts/1.0", "_Capabilities");

    /** @generated */
    public static final QName _Dimension =
            new QName("http://www.opengis.net/wmts/1.0", "_Dimension");

    /** @generated */
    public static final QName _DimensionNameValue =
            new QName("http://www.opengis.net/wmts/1.0", "_DimensionNameValue");

    /** @generated */
    public static final QName _FeatureInfoResponse =
            new QName("http://www.opengis.net/wmts/1.0", "_FeatureInfoResponse");

    /** @generated */
    public static final QName _GetCapabilities =
            new QName("http://www.opengis.net/wmts/1.0", "_GetCapabilities");

    /** @generated */
    public static final QName _GetFeatureInfo =
            new QName("http://www.opengis.net/wmts/1.0", "_GetFeatureInfo");

    /** @generated */
    public static final QName _GetTile = new QName("http://www.opengis.net/wmts/1.0", "_GetTile");

    /** @generated */
    public static final QName _LegendURL =
            new QName("http://www.opengis.net/wmts/1.0", "_LegendURL");

    /** @generated */
    public static final QName _Style = new QName("http://www.opengis.net/wmts/1.0", "_Style");

    /** @generated */
    public static final QName _TextPayload =
            new QName("http://www.opengis.net/wmts/1.0", "_TextPayload");

    /** @generated */
    public static final QName _Theme = new QName("http://www.opengis.net/wmts/1.0", "_Theme");

    /** @generated */
    public static final QName _Themes = new QName("http://www.opengis.net/wmts/1.0", "_Themes");

    /** @generated */
    public static final QName _TileMatrix =
            new QName("http://www.opengis.net/wmts/1.0", "_TileMatrix");

    /** @generated */
    public static final QName _TileMatrixLimits =
            new QName("http://www.opengis.net/wmts/1.0", "_TileMatrixLimits");

    /** @generated */
    public static final QName _TileMatrixSet =
            new QName("http://www.opengis.net/wmts/1.0", "_TileMatrixSet");

    /** @generated */
    public static final QName _TileMatrixSetLimits =
            new QName("http://www.opengis.net/wmts/1.0", "_TileMatrixSetLimits");

    /** @generated */
    public static final QName _TileMatrixSetLink =
            new QName("http://www.opengis.net/wmts/1.0", "_TileMatrixSetLink");

    /* Elements */
    /** @generated */
    public static final QName BinaryPayload =
            new QName("http://www.opengis.net/wmts/1.0", "BinaryPayload");

    /** @generated */
    public static final QName Capabilities =
            new QName("http://www.opengis.net/wmts/1.0", "Capabilities");

    /** @generated */
    public static final QName Dimension = new QName("http://www.opengis.net/wmts/1.0", "Dimension");

    /** @generated */
    public static final QName DimensionNameValue =
            new QName("http://www.opengis.net/wmts/1.0", "DimensionNameValue");

    /** @generated */
    public static final QName FeatureInfoResponse =
            new QName("http://www.opengis.net/wmts/1.0", "FeatureInfoResponse");

    /** @generated */
    public static final QName GetCapabilities =
            new QName("http://www.opengis.net/wmts/1.0", "GetCapabilities");

    /** @generated */
    public static final QName GetFeatureInfo =
            new QName("http://www.opengis.net/wmts/1.0", "GetFeatureInfo");

    /** @generated */
    public static final QName GetTile = new QName("http://www.opengis.net/wmts/1.0", "GetTile");

    /** @generated */
    public static final QName Layer = new QName("http://www.opengis.net/wmts/1.0", "Layer");

    /** @generated */
    public static final QName LegendURL = new QName("http://www.opengis.net/wmts/1.0", "LegendURL");

    /** @generated */
    public static final QName Style = new QName("http://www.opengis.net/wmts/1.0", "Style");

    /** @generated */
    public static final QName TextPayload =
            new QName("http://www.opengis.net/wmts/1.0", "TextPayload");

    /** @generated */
    public static final QName Theme = new QName("http://www.opengis.net/wmts/1.0", "Theme");

    /** @generated */
    public static final QName Themes = new QName("http://www.opengis.net/wmts/1.0", "Themes");

    /** @generated */
    public static final QName TileMatrix =
            new QName("http://www.opengis.net/wmts/1.0", "TileMatrix");

    /** @generated */
    public static final QName TileMatrixLimits =
            new QName("http://www.opengis.net/wmts/1.0", "TileMatrixLimits");

    /** @generated */
    public static final QName TileMatrixSet =
            new QName("http://www.opengis.net/wmts/1.0", "TileMatrixSet");

    /** @generated */
    public static final QName TileMatrixSetLimits =
            new QName("http://www.opengis.net/wmts/1.0", "TileMatrixSetLimits");

    /** @generated */
    public static final QName TileMatrixSetLink =
            new QName("http://www.opengis.net/wmts/1.0", "TileMatrixSetLink");

    /*public static final QName Format = new QName("", "Format");
    public static final QName InfoFormat = new QName("", "InfoFormat");*/

    /* Attributes */

}
