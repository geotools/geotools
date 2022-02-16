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
package org.geotools.xml.styling;

/**
 * This code generated using Refractions SchemaCodeGenerator For more information, view the attached
 * licensing information. CopyRight 105
 */
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Map;
import org.geotools.xml.schema.Attribute;
import org.geotools.xml.schema.AttributeGroup;
import org.geotools.xml.schema.ComplexType;
import org.geotools.xml.schema.Element;
import org.geotools.xml.schema.Group;
import org.geotools.xml.schema.Schema;
import org.geotools.xml.schema.SimpleType;
import org.geotools.xml.schema.Type;

public class sldSchema implements Schema {

    public static final URI NAMESPACE = loadNS();

    private static URI loadNS() {
        try {
            return new URI("http://www.opengis.net/sld");
        } catch (URISyntaxException e) {
            return null;
        }
    }

    @Override
    public int getBlockDefault() {
        return 0;
    }

    @Override
    public int getFinalDefault() {
        return 0;
    }

    @Override
    public String getId() {
        return "null";
    }

    private static Schema[] imports = null;

    @Override
    public Schema[] getImports() {
        if (imports == null) {
            imports =
                    new Schema[] {
                        // TODO add instance of org.geotools.xml.xLink.XLinkSchema@e94e92,
                        // TODO add instance of org.geotools.xml.ogc.FilterSchema@18020cc
                    };
        }
        return imports;
    }

    @Override
    public String getPrefix() {
        return "sld";
    }

    @Override
    public URI getTargetNamespace() {
        return NAMESPACE;
    }

    @Override
    public URI getURI() {
        return NAMESPACE;
    }

    @Override
    public String getVersion() {
        return "null";
    }

    @Override
    public boolean includesURI(URI uri) {
        // // TODO fill me in!
        return false; // // safer
    }

    @Override
    public boolean isAttributeFormDefault() {
        return false;
    }

    @Override
    public boolean isElementFormDefault() {
        return true;
    }

    @Override
    public AttributeGroup[] getAttributeGroups() {
        return null;
    }

    @Override
    public Attribute[] getAttributes() {
        return null;
    }
    /** TODO comment here */
    private static ComplexType[] complexTypes = null;

    @Override
    public ComplexType[] getComplexTypes() {
        if (complexTypes == null) {
            complexTypes =
                    new ComplexType[] {
                        sldComplexTypes2.SelectedChannelType.getInstance(),
                        sldComplexTypes2.ParameterValueType.getInstance(),
                        sldComplexTypes2.SymbolizerType.getInstance()
                    };
        }
        return complexTypes;
    }
    /** TODO comment here */
    private static Element[] elements = null;

    @Override
    public Element[] getElements() {
        if (elements == null) {
            elements =
                    new Element[] {
                        new sldElement(
                                "Displacement",
                                sldComplexTypes._Displacement.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement(
                                "GammaValue",
                                org.geotools.xml.xsi.XSISimpleTypes.Double.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement(
                                "MaxScaleDenominator",
                                org.geotools.xml.xsi.XSISimpleTypes.Double.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement(
                                "PolygonSymbolizer",
                                sldComplexTypes2._PolygonSymbolizer.getInstance(),
                                new sldElement(
                                        "Symbolizer",
                                        sldComplexTypes2.SymbolizerType.getInstance(),
                                        null,
                                        1,
                                        1),
                                1,
                                1),
                        new sldElement(
                                "ColorMapEntry",
                                sldComplexTypes._ColorMapEntry.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement(
                                "GreenChannel",
                                sldComplexTypes2.SelectedChannelType.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement(
                                "LATEST_ON_TOP",
                                sldComplexTypes._LATEST_ON_TOP.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement(
                                "LayerFeatureConstraints",
                                sldComplexTypes2._LayerFeatureConstraints.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement(
                                "Size",
                                sldComplexTypes2.ParameterValueType.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement(
                                "LineSymbolizer",
                                sldComplexTypes2._LineSymbolizer.getInstance(),
                                new sldElement(
                                        "Symbolizer",
                                        sldComplexTypes2.SymbolizerType.getInstance(),
                                        null,
                                        1,
                                        1),
                                1,
                                1),
                        new sldElement(
                                "PointSymbolizer",
                                sldComplexTypes2._PointSymbolizer.getInstance(),
                                new sldElement(
                                        "Symbolizer",
                                        sldComplexTypes2.SymbolizerType.getInstance(),
                                        null,
                                        1,
                                        1),
                                1,
                                1),
                        new sldElement(
                                "ChannelSelection",
                                sldComplexTypes._ChannelSelection.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement(
                                "Graphic", sldComplexTypes._Graphic.getInstance(), null, 1, 1),
                        new sldElement(
                                "WellKnownName",
                                org.geotools.xml.xsi.XSISimpleTypes.String.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement(
                                "Name",
                                org.geotools.xml.xsi.XSISimpleTypes.String.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement(
                                "MinScaleDenominator",
                                org.geotools.xml.xsi.XSISimpleTypes.Double.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement(
                                "BlueChannel",
                                sldComplexTypes2.SelectedChannelType.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement(
                                "RANDOM", sldComplexTypes2._RANDOM.getInstance(), null, 1, 1),
                        new sldElement(
                                "FeatureTypeName",
                                org.geotools.xml.xsi.XSISimpleTypes.String
                                        .getInstance() /* simpleType name is string */,
                                null,
                                1,
                                1),
                        new sldElement("Font", sldComplexTypes._Font.getInstance(), null, 1, 1),
                        new sldElement(
                                "Title",
                                org.geotools.xml.xsi.XSISimpleTypes.String
                                        .getInstance() /* simpleType name is string */,
                                null,
                                1,
                                1),
                        new sldElement(
                                "UserStyle", sldComplexTypes2._UserStyle.getInstance(), null, 1, 1),
                        new sldElement(
                                "PointPlacement",
                                sldComplexTypes2._PointPlacement.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement(
                                "Rotation",
                                sldComplexTypes2.ParameterValueType.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement(
                                "OnlineResource",
                                sldComplexTypes2._OnlineResource.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement("Mark", sldComplexTypes2._Mark.getInstance(), null, 1, 1),
                        new sldElement(
                                "BrightnessOnly",
                                org.geotools.xml.xsi.XSISimpleTypes.Boolean.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement(
                                "SemanticTypeIdentifier",
                                org.geotools.xml.xsi.XSISimpleTypes.String.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement(
                                "EARLIEST_ON_TOP",
                                sldComplexTypes._EARLIEST_ON_TOP.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement(
                                "Geometry", sldComplexTypes._Geometry.getInstance(), null, 1, 1),
                        new sldElement(
                                "ElseFilter",
                                sldComplexTypes._ElseFilter.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement(
                                "StyledLayerDescriptor",
                                sldComplexTypes2._StyledLayerDescriptor.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement(
                                "Abstract",
                                org.geotools.xml.xsi.XSISimpleTypes.String.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement(
                                "AnchorPoint",
                                sldComplexTypes._AnchorPoint.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement(
                                "GraphicStroke",
                                sldComplexTypes._GraphicStroke.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement(
                                "ContrastEnhancement",
                                sldComplexTypes._ContrastEnhancement.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement(
                                "FeatureTypeStyle",
                                sldComplexTypes._FeatureTypeStyle.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement(
                                "Format",
                                org.geotools.xml.xsi.XSISimpleTypes.String.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement(
                                "DisplacementY",
                                sldComplexTypes2.ParameterValueType.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement(
                                "DisplacementX",
                                sldComplexTypes2.ParameterValueType.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement(
                                "NamedLayer",
                                sldComplexTypes2._NamedLayer.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement(
                                "TextSymbolizer",
                                sldComplexTypes2._TextSymbolizer.getInstance(),
                                new sldElement(
                                        "Symbolizer",
                                        sldComplexTypes2.SymbolizerType.getInstance(),
                                        null,
                                        1,
                                        1),
                                1,
                                1),
                        new sldElement(
                                "LabelPlacement",
                                sldComplexTypes._LabelPlacement.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement(
                                "Value",
                                org.geotools.xml.xsi.XSISimpleTypes.String.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement(
                                "Histogram", sldComplexTypes._Histogram.getInstance(), null, 1, 1),
                        new sldElement(
                                "ExternalGraphic",
                                sldComplexTypes._ExternalGraphic.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement(
                                "NamedStyle",
                                sldComplexTypes2._NamedStyle.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement(
                                "AnchorPointY",
                                sldComplexTypes2.ParameterValueType.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement(
                                "RemoteOWS", sldComplexTypes2._RemoteOWS.getInstance(), null, 1, 1),
                        new sldElement(
                                "CssParameter",
                                sldComplexTypes._CssParameter.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement(
                                "PerpendicularOffset",
                                sldComplexTypes2.ParameterValueType.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement(
                                "Label",
                                sldComplexTypes2.ParameterValueType.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement(
                                "OverlapBehavior",
                                sldComplexTypes2._OverlapBehavior.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement("Halo", sldComplexTypes._Halo.getInstance(), null, 1, 1),
                        new sldElement(
                                "ImageOutline",
                                sldComplexTypes._ImageOutline.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement("Fill", sldComplexTypes._Fill.getInstance(), null, 1, 1),
                        new sldElement(
                                "ShadedRelief",
                                sldComplexTypes2._ShadedRelief.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement(
                                "SourceChannelName",
                                org.geotools.xml.xsi.XSISimpleTypes.String.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement(
                                "Service", sldSimpleTypes._Service.getInstance(), null, 1, 1),
                        new sldElement(
                                "GrayChannel",
                                sldComplexTypes2.SelectedChannelType.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement("Rule", sldComplexTypes2._Rule.getInstance(), null, 1, 1),
                        new sldElement(
                                "RedChannel",
                                sldComplexTypes2.SelectedChannelType.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement(
                                "GraphicFill",
                                sldComplexTypes._GraphicFill.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement(
                                "LegendGraphic",
                                sldComplexTypes2._LegendGraphic.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement(
                                "AVERAGE", sldComplexTypes._AVERAGE.getInstance(), null, 1, 1),
                        new sldElement(
                                "IsDefault",
                                org.geotools.xml.xsi.XSISimpleTypes.Boolean.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement(
                                "LinePlacement",
                                sldComplexTypes2._LinePlacement.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement(
                                "Normalize", sldComplexTypes2._Normalize.getInstance(), null, 1, 1),
                        new sldElement("Extent", sldComplexTypes._Extent.getInstance(), null, 1, 1),
                        new sldElement(
                                "ReliefFactor",
                                org.geotools.xml.xsi.XSISimpleTypes.Double.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement(
                                "RasterSymbolizer",
                                sldComplexTypes2._RasterSymbolizer.getInstance(),
                                new sldElement(
                                        "Symbolizer",
                                        sldComplexTypes2.SymbolizerType.getInstance(),
                                        null,
                                        1,
                                        1),
                                1,
                                1),
                        new sldElement(
                                "FeatureTypeConstraint",
                                sldComplexTypes._FeatureTypeConstraint.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement(
                                "Stroke", sldComplexTypes2._Stroke.getInstance(), null, 1, 1),
                        new sldElement(
                                "ColorMap", sldComplexTypes._ColorMap.getInstance(), null, 1, 1),
                        new sldElement(
                                "UserLayer", sldComplexTypes2._UserLayer.getInstance(), null, 1, 1),
                        new sldElement(
                                "Symbolizer",
                                sldComplexTypes2.SymbolizerType.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement(
                                "Opacity",
                                sldComplexTypes2.ParameterValueType.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement(
                                "Radius",
                                sldComplexTypes2.ParameterValueType.getInstance(),
                                null,
                                1,
                                1),
                        new sldElement(
                                "AnchorPointX",
                                sldComplexTypes2.ParameterValueType.getInstance(),
                                null,
                                1,
                                1)
                    };
        }
        return elements;
    }

    @Override
    public Group[] getGroups() {
        return null;
    }

    @Override
    public SimpleType[] getSimpleTypes() {
        return null;
    }

    private static final int SLD_ELEMENT = 32;

    public Type getSLDType() {
        return getElements()[SLD_ELEMENT].getType();
    }

    /** Returns the implementation hints. The default implementation returns en empty map. */
    @Override
    public Map<java.awt.RenderingHints.Key, ?> getImplementationHints() {
        return Collections.emptyMap();
    }
}
