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
package org.geotools.sld.bindings;

import java.util.Set;
import javax.xml.namespace.QName;
import org.geotools.filter.v1_0.OGC;
import org.geotools.xml.XSD;


/**
 * This interface contains the qualified names of all the types,elements, and
 * attributes in the http://www.opengis.net/sld schema.
 *
 * @generated
 *
 * @source $URL$
 */
public final class SLD extends XSD {
    /**
     * singleton instance
     */
    private static SLD instance = new SLD();

    /** @generated */
    public static final String NAMESPACE = "http://www.opengis.net/sld";

    /* Type Definitions */
    /** @generated */
    public static final QName PARAMETERVALUETYPE = new QName("http://www.opengis.net/sld",
            "ParameterValueType");

    /** @generated */
    public static final QName SELECTEDCHANNELTYPE = new QName("http://www.opengis.net/sld",
            "SelectedChannelType");

    /** @generated */
    public static final QName SYMBOLIZERTYPE = new QName("http://www.opengis.net/sld",
            "SymbolizerType");

    /* Elements */
    /** @generated */
    public static final QName ABSTRACT = new QName("http://www.opengis.net/sld", "Abstract");

    /** @generated */
    public static final QName ANCHORPOINT = new QName("http://www.opengis.net/sld", "AnchorPoint");

    /** @generated */
    public static final QName ANCHORPOINTX = new QName("http://www.opengis.net/sld", "AnchorPointX");

    /** @generated */
    public static final QName ANCHORPOINTY = new QName("http://www.opengis.net/sld", "AnchorPointY");

    /** @generated */
    public static final QName AVERAGE = new QName("http://www.opengis.net/sld", "AVERAGE");

    /** @generated */
    public static final QName BLUECHANNEL = new QName("http://www.opengis.net/sld", "BlueChannel");

    /** @generated */
    public static final QName BRIGHTNESSONLY = new QName("http://www.opengis.net/sld",
            "BrightnessOnly");

    /** @generated */
    public static final QName CHANNELSELECTION = new QName("http://www.opengis.net/sld",
            "ChannelSelection");

    /** @generated */
    public static final QName COLORMAP = new QName("http://www.opengis.net/sld", "ColorMap");

    /** @generated */
    public static final QName COLORMAPENTRY = new QName("http://www.opengis.net/sld",
            "ColorMapEntry");

    /** @generated */
    public static final QName CONTRASTENHANCEMENT = new QName("http://www.opengis.net/sld",
            "ContrastEnhancement");

    /** @generated */
    public static final QName CSSPARAMETER = new QName("http://www.opengis.net/sld", "CssParameter");

    /** @generated */
    public static final QName DISPLACEMENT = new QName("http://www.opengis.net/sld", "Displacement");

    /** @generated */
    public static final QName DISPLACEMENTX = new QName("http://www.opengis.net/sld",
            "DisplacementX");

    /** @generated */
    public static final QName DISPLACEMENTY = new QName("http://www.opengis.net/sld",
            "DisplacementY");

    /** @generated */
    public static final QName EARLIEST_ON_TOP = new QName("http://www.opengis.net/sld",
            "EARLIEST_ON_TOP");

    /** @generated */
    public static final QName ELSEFILTER = new QName("http://www.opengis.net/sld", "ElseFilter");

    /** @generated */
    public static final QName EXTENT = new QName("http://www.opengis.net/sld", "Extent");

    /** @generated */
    public static final QName EXTERNALGRAPHIC = new QName("http://www.opengis.net/sld",
            "ExternalGraphic");

    /** @generated */
    public static final QName FEATURETYPECONSTRAINT = new QName("http://www.opengis.net/sld",
            "FeatureTypeConstraint");

    /** @generated */
    public static final QName FEATURETYPENAME = new QName("http://www.opengis.net/sld",
            "FeatureTypeName");

    /** @generated */
    public static final QName FEATURETYPESTYLE = new QName("http://www.opengis.net/sld",
            "FeatureTypeStyle");

    /** @generated */
    public static final QName FILL = new QName("http://www.opengis.net/sld", "Fill");

    /** @generated */
    public static final QName FONT = new QName("http://www.opengis.net/sld", "Font");

    /** @generated */
    public static final QName FORMAT = new QName("http://www.opengis.net/sld", "Format");

    /** @generated */
    public static final QName GAMMAVALUE = new QName("http://www.opengis.net/sld", "GammaValue");

    /** @generated */
    public static final QName GEOMETRY = new QName("http://www.opengis.net/sld", "Geometry");

    /** @generated */
    public static final QName GRAPHIC = new QName("http://www.opengis.net/sld", "Graphic");

    /** @generated */
    public static final QName GRAPHICFILL = new QName("http://www.opengis.net/sld", "GraphicFill");

    /** @generated */
    public static final QName GRAPHICSTROKE = new QName("http://www.opengis.net/sld",
            "GraphicStroke");

    /** @generated */
    public static final QName GRAYCHANNEL = new QName("http://www.opengis.net/sld", "GrayChannel");

    /** @generated */
    public static final QName GREENCHANNEL = new QName("http://www.opengis.net/sld", "GreenChannel");

    /** @generated */
    public static final QName HALO = new QName("http://www.opengis.net/sld", "Halo");

    /** @generated */
    public static final QName HISTOGRAM = new QName("http://www.opengis.net/sld", "Histogram");

    /** @generated */
    public static final QName IMAGEOUTLINE = new QName("http://www.opengis.net/sld", "ImageOutline");

    /** @generated */
    public static final QName ISDEFAULT = new QName("http://www.opengis.net/sld", "IsDefault");

    /** @generated */
    public static final QName LABEL = new QName("http://www.opengis.net/sld", "Label");

    /** @generated */
    public static final QName LABELPLACEMENT = new QName("http://www.opengis.net/sld",
            "LabelPlacement");

    /** @generated */
    public static final QName LATEST_ON_TOP = new QName("http://www.opengis.net/sld",
            "LATEST_ON_TOP");

    /** @generated */
    public static final QName LAYERFEATURECONSTRAINTS = new QName("http://www.opengis.net/sld",
            "LayerFeatureConstraints");

    /** @generated */
    public static final QName LEGENDGRAPHIC = new QName("http://www.opengis.net/sld",
            "LegendGraphic");

    /** @generated */
    public static final QName LINEPLACEMENT = new QName("http://www.opengis.net/sld",
            "LinePlacement");

    /** @generated */
    public static final QName LINESYMBOLIZER = new QName("http://www.opengis.net/sld",
            "LineSymbolizer");

    /** @generated */
    public static final QName MARK = new QName("http://www.opengis.net/sld", "Mark");

    /** @generated */
    public static final QName MAXSCALEDENOMINATOR = new QName("http://www.opengis.net/sld",
            "MaxScaleDenominator");

    /** @generated */
    public static final QName MINSCALEDENOMINATOR = new QName("http://www.opengis.net/sld",
            "MinScaleDenominator");

    /** @generated */
    public static final QName NAME = new QName("http://www.opengis.net/sld", "Name");

    /** @generated */
    public static final QName NAMEDLAYER = new QName("http://www.opengis.net/sld", "NamedLayer");

    /** @generated */
    public static final QName NAMEDSTYLE = new QName("http://www.opengis.net/sld", "NamedStyle");

    /** @generated */
    public static final QName NORMALIZE = new QName("http://www.opengis.net/sld", "Normalize");

    /** @generated */
    public static final QName ONLINERESOURCE = new QName("http://www.opengis.net/sld",
            "OnlineResource");

    /** @generated */
    public static final QName OPACITY = new QName("http://www.opengis.net/sld", "Opacity");

    /** @generated */
    public static final QName OVERLAPBEHAVIOR = new QName("http://www.opengis.net/sld",
            "OverlapBehavior");

    /** @generated */
    public static final QName PERPENDICULAROFFSET = new QName("http://www.opengis.net/sld",
            "PerpendicularOffset");

    /** @generated */
    public static final QName POINTPLACEMENT = new QName("http://www.opengis.net/sld",
            "PointPlacement");

    /** @generated */
    public static final QName POINTSYMBOLIZER = new QName("http://www.opengis.net/sld",
            "PointSymbolizer");

    /** @generated */
    public static final QName POLYGONSYMBOLIZER = new QName("http://www.opengis.net/sld",
            "PolygonSymbolizer");

    /** @generated */
    public static final QName RADIUS = new QName("http://www.opengis.net/sld", "Radius");

    /** @generated */
    public static final QName RANDOM = new QName("http://www.opengis.net/sld", "RANDOM");

    /** @generated */
    public static final QName RASTERSYMBOLIZER = new QName("http://www.opengis.net/sld",
            "RasterSymbolizer");

    /** @generated */
    public static final QName REDCHANNEL = new QName("http://www.opengis.net/sld", "RedChannel");

    /** @generated */
    public static final QName RELIEFFACTOR = new QName("http://www.opengis.net/sld", "ReliefFactor");

    /** @generated */
    public static final QName REMOTEOWS = new QName("http://www.opengis.net/sld", "RemoteOWS");

    /** @generated */
    public static final QName ROTATION = new QName("http://www.opengis.net/sld", "Rotation");

    /** @generated */
    public static final QName RULE = new QName("http://www.opengis.net/sld", "Rule");

    /** @generated */
    public static final QName SEMANTICTYPEIDENTIFIER = new QName("http://www.opengis.net/sld",
            "SemanticTypeIdentifier");

    /** @generated */
    public static final QName SERVICE = new QName("http://www.opengis.net/sld", "Service");

    /** @generated */
    public static final QName SHADEDRELIEF = new QName("http://www.opengis.net/sld", "ShadedRelief");

    /** @generated */
    public static final QName SIZE = new QName("http://www.opengis.net/sld", "Size");

    /** @generated */
    public static final QName SOURCECHANNELNAME = new QName("http://www.opengis.net/sld",
            "SourceChannelName");

    /** @generated */
    public static final QName STROKE = new QName("http://www.opengis.net/sld", "Stroke");

    /** @generated */
    public static final QName STYLEDLAYERDESCRIPTOR = new QName("http://www.opengis.net/sld",
            "StyledLayerDescriptor");

    /** @generated */
    public static final QName SYMBOLIZER = new QName("http://www.opengis.net/sld", "Symbolizer");

    /** @generated */
    public static final QName TEXTSYMBOLIZER = new QName("http://www.opengis.net/sld",
            "TextSymbolizer");

    /** @generated */
    public static final QName TITLE = new QName("http://www.opengis.net/sld", "Title");

    /** @generated */
    public static final QName USERLAYER = new QName("http://www.opengis.net/sld", "UserLayer");

    /** @generated */
    public static final QName USERSTYLE = new QName("http://www.opengis.net/sld", "UserStyle");

    /** @generated */
    public static final QName VALUE = new QName("http://www.opengis.net/sld", "Value");

    /** @generated */
    public static final QName WELLKNOWNNAME = new QName("http://www.opengis.net/sld",
            "WellKnownName");

    public static final QName VENDOROPTION = new QName("http://www.opengis.net/sld", "VendorOption");
    
    /**
     * private constructor
     */
    private SLD() {
    }

    public static SLD getInstance() {
        return instance;
    }

    protected void addDependencies(Set dependencies) {
        dependencies.add(OGC.getInstance());
    }

    /**
     * Returns 'http://www.opengis.net/sld'
     */
    public String getNamespaceURI() {
        return NAMESPACE;
    }

    /**
     * Returns the location of 'StyledLayerDescriptor.xsd'.
     */
    public String getSchemaLocation() {
        return getClass().getResource("StyledLayerDescriptor.xsd").toString();
    }

    /* Attributes */
}
