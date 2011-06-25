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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import javax.xml.namespace.QName;
import org.geotools.filter.v1_0.OGC;


public class SLDMockData {
    static Element anchorPoint(Document document, Node parent) {
        Element anchorPoint = element(SLD.ANCHORPOINT, document, parent);

        Element x = element(SLD.ANCHORPOINTX, document, anchorPoint);
        x.appendChild(document.createTextNode("1"));

        Element y = element(SLD.ANCHORPOINTY, document, anchorPoint);
        y.appendChild(document.createTextNode("2"));

        return anchorPoint;
    }

    static Element displacement(Document document, Node parent) {
        Element displacement = element(SLD.DISPLACEMENT, document, parent);

        Element x = element(SLD.DISPLACEMENTX, document, displacement);
        x.appendChild(document.createTextNode("1"));

        Element y = element(SLD.DISPLACEMENTY, document, displacement);
        y.appendChild(document.createTextNode("2"));

        return displacement;
    }

    static Element size(Document document, Node parent) {
        Element size = element(SLD.SIZE, document, parent);
        size.appendChild(document.createTextNode("1"));

        return size;
    }

    static Element opacity(Document document, Node parent) {
        Element opacity = element(SLD.OPACITY, document, parent);
        opacity.appendChild(document.createTextNode("1"));

        return opacity;
    }

    static Element rotation(Document document, Node parent) {
        Element rotation = element(SLD.ROTATION, document, parent);
        rotation.appendChild(document.createTextNode("90"));

        return rotation;
    }

    static Element pointPlacement(Document document, Node parent) {
        Element pointPlacement = element(SLD.POINTPLACEMENT, document, parent);
        anchorPoint(document, pointPlacement);
        displacement(document, pointPlacement);
        rotation(document, pointPlacement);

        return pointPlacement;
    }

    static Element css(String name, String value, Document document, Node parent) {
        Element css = element(SLD.CSSPARAMETER, document, parent);
        css.setAttribute("name", name);
        css.appendChild(document.createTextNode(value));

        return css;
    }

    static Element fill(Document document, Node parent) {
        Element fill = element(SLD.FILL, document, parent);

        css("fill", "#123456", document, fill);
        css("fill-opacity", "1.0", document, fill);

        return fill;
    }

    static Element stroke(Document document, Node parent) {
        Element stroke = element(SLD.STROKE, document, parent);
        css("stroke", "#123456", document, stroke);
        css("stroke-opacity", "1.0", document, stroke);
        css("stroke-width", "1.0", document, stroke);
        css("stroke-linejoin", "mitre", document, stroke);
        css("stroke-linecap", "butt", document, stroke);
        css("stroke-dasharray", "1.1 2.2 3.3 4.4", document, stroke);
        css("stroke-dashoffset", "1", document, stroke);

        graphicFill(document, stroke);

        return stroke;
    }

    static Element wellKnownName(Document document, Node parent) {
        Element wkn = element(SLD.WELLKNOWNNAME, document, parent);
        wkn.appendChild(document.createTextNode("wellKnownName"));

        return wkn;
    }

    static Element mark(Document document, Node parent) {
        Element mark = element(SLD.MARK, document, parent);
        fill(document, mark);
        wellKnownName(document, mark);

        return mark;
    }

    static Element graphic(Document document, Node parent) {
        Element graphic = element(SLD.GRAPHIC, document, parent);
        mark(document, graphic);
        size(document, graphic);
        opacity(document, graphic);
        rotation(document, graphic);

        return graphic;
    }

    static Element graphicFill(Document document, Node parent) {
        Element graphicFill = element(SLD.GRAPHICFILL, document, parent);
        graphic(document, graphicFill);

        return graphicFill;
    }

    static Element graphicStroke(Document document, Node parent) {
        Element graphicStroke = element(SLD.GRAPHICSTROKE, document, parent);
        graphic(document, graphicStroke);

        return graphicStroke;
    }

    static Element polygonSymbolizer(Document document, Node parent) {
        Element polygonSymbolizer = element(SLD.POLYGONSYMBOLIZER, document, parent);

        stroke(document, polygonSymbolizer);
        fill(document, polygonSymbolizer);

        return polygonSymbolizer;
    }
    
    static Element transformedPolygonSymbolizer(Document document, Node parent) {
        Element polygonSymbolizer = element(SLD.POLYGONSYMBOLIZER, document, parent);

        transformedGeometry(document, polygonSymbolizer);
        stroke(document, polygonSymbolizer);
        fill(document, polygonSymbolizer);

        return polygonSymbolizer;
    }

    static Element lineSymbolizer(Document document, Node parent) {
        Element lineSymbolizer = element(SLD.LINESYMBOLIZER, document, parent);

        stroke(document, lineSymbolizer);

        return lineSymbolizer;
    }
    
    static Element transformedLineSymbolizer(Document document, Node parent) {
        Element lineSymbolizer = element(SLD.LINESYMBOLIZER, document, parent);

        transformedGeometry(document, lineSymbolizer);
        stroke(document, lineSymbolizer);

        return lineSymbolizer;
    }

    static Element geometry(Document document, Node parent) {
        Element geometry = element(SLD.GEOMETRY, document, parent);
        Element propertyName = element(OGC.PropertyName, document, geometry);
        propertyName.appendChild(document.createTextNode("the_geom"));

        return geometry;
    }
    
    static Element transformedGeometry(Document document, Node parent) {
        Element geometry = element(SLD.GEOMETRY, document, parent);
        Element function = element(OGC.Function, document, geometry);
        function.setAttribute("name", "buffer");
        Element propertyName = element(OGC.PropertyName, document, function);
        propertyName.appendChild(document.createTextNode("the_geom"));
        Element distance = element(OGC.Literal, document, function);
        distance.appendChild(document.createTextNode("1"));

        return geometry;
    }


    static Element channelSelectionRGB(Document document, Node parent) {
        Element channelSelection = element(SLD.CHANNELSELECTION, document, parent);

        Element channel = element(SLD.REDCHANNEL, document, channelSelection);
        Element sourceChannelName = element(SLD.SOURCECHANNELNAME, document, channel);
        sourceChannelName.appendChild(document.createTextNode("Red"));

        channel = element(SLD.GREENCHANNEL, document, channelSelection);
        sourceChannelName = element(SLD.SOURCECHANNELNAME, document, channel);
        sourceChannelName.appendChild(document.createTextNode("Green"));

        channel = element(SLD.BLUECHANNEL, document, channelSelection);
        sourceChannelName = element(SLD.SOURCECHANNELNAME, document, channel);
        sourceChannelName.appendChild(document.createTextNode("Blue"));

        return channelSelection;
    }
    
    static Element channelSelectionGray(Document document, Node parent) {
        Element channelSelection = element(SLD.CHANNELSELECTION, document, parent);

        Element channel = element(SLD.GRAYCHANNEL, document, channelSelection);
        Element sourceChannelName = element(SLD.SOURCECHANNELNAME, document, channel);
        sourceChannelName.appendChild(document.createTextNode("Gray"));

        return channelSelection;
    }

    static Element colorMap(Document document, Node parent) {
        Element colorMap = element(SLD.COLORMAP, document, parent);
        Element colorMapEntry = element(SLD.COLORMAPENTRY, document, colorMap);
        colorMapEntry.setAttribute("color", "#000000");

        colorMapEntry = element(SLD.COLORMAPENTRY, document, colorMap);
        colorMapEntry.setAttribute("color", "#FFFFFF");

        return colorMap;
    }

    static Element overlapBehaviour(Document document, Node parent) {
        Element overlapBehaviour = element(SLD.OVERLAPBEHAVIOR, document, parent);
        element(SLD.EARLIEST_ON_TOP, document, overlapBehaviour);

        return overlapBehaviour;
    }

    static Element contrastEnhancement(Document document, Node parent) {
        Element contrastEnhancement = element(SLD.CONTRASTENHANCEMENT, document, parent);

        Element gammaValue = element(SLD.GAMMAVALUE, document, contrastEnhancement);
        gammaValue.appendChild(document.createTextNode("1.23"));

        Element histogram = element(SLD.HISTOGRAM, document, contrastEnhancement);

        return contrastEnhancement;
    }

    static Element shadedRelief(Document document, Node parent) {
        Element shadedRelief = element(SLD.SHADEDRELIEF, document, parent);
        element(SLD.BRIGHTNESSONLY, document, shadedRelief, "true");
        element(SLD.RELIEFFACTOR, document, shadedRelief, "1.0");

        return shadedRelief;
    }

    static Element imageOutline(Document document, Node parent) {
        Element imageOutline = element(SLD.IMAGEOUTLINE, document, parent);
        lineSymbolizer(document, imageOutline);

        return imageOutline;
    }

    static Element rasterSymbolizer(Document document, Node parent) {
        Element rasterSymbolizer = element(SLD.RASTERSYMBOLIZER, document, parent);

        geometry(document, rasterSymbolizer);
        opacity(document, rasterSymbolizer);
        channelSelectionRGB(document, rasterSymbolizer);
        overlapBehaviour(document, rasterSymbolizer);
        colorMap(document, rasterSymbolizer);
        contrastEnhancement(document, rasterSymbolizer);
        shadedRelief(document, rasterSymbolizer);
        imageOutline(document, rasterSymbolizer);

        return rasterSymbolizer;
    }

    static Element font(Document document, Node parent) {
        Element font = element(SLD.FONT, document, parent);
        css("font-family", "Arial", document, font);
        css("font-weight", "normal", document, font);
        css("font-size", "14", document, font);
        css("font-style", "normal", document, font);

        return font;
    }

    static Element label(Document document, Node parent) {
        return element(SLD.LABEL, document, parent, "label");
    }

    static Element halo(Document document, Node parent) {
        Element halo = element(SLD.HALO, document, parent);
        fill(document, halo);
        element(SLD.RADIUS, document, halo, "1.0");

        return halo;
    }

    static Element textSymbolizer(Document document, Node parent) {
        Element textSymbolizer = element(SLD.TEXTSYMBOLIZER, document, parent);
        geometry(document, textSymbolizer);
        label(document, textSymbolizer);
        font(document, textSymbolizer);
        pointPlacement(document, textSymbolizer);
        halo(document, textSymbolizer);
        fill(document, textSymbolizer);

        return textSymbolizer;
    }

    static Element textSymbolizerWithVendorOptions(Document document, Node parent) {
        Element textSymbolizer = textSymbolizer(document, parent);
        vendorOption(document, textSymbolizer, "followLine", "true");
        vendorOption(document, textSymbolizer, "spaceAround", "10");
        return textSymbolizer;
    }

    static Element vendorOption(Document document, Node parent, String name, String value) {
        Element vendorOption = element(SLD.VENDOROPTION, document, parent);
        vendorOption.setAttribute("name", name);
        vendorOption.appendChild(document.createTextNode(value));
        return vendorOption;
    }

    static Element pointSymbolizer(Document document, Node parent) {
        Element pointSymbolizer = element(SLD.POINTSYMBOLIZER, document, parent);

        geometry(document, pointSymbolizer);
        graphic(document, pointSymbolizer);

        return pointSymbolizer;
    }
    
    static Element transformedPointSymbolizer(Document document, Node parent) {
        Element pointSymbolizer = element(SLD.POINTSYMBOLIZER, document, parent);

        transformedGeometry(document, pointSymbolizer);
        graphic(document, pointSymbolizer);

        return pointSymbolizer;
    }

    static Element legendGraphic(Document document, Node parent) {
        Element legendGraphic = element(SLD.LEGENDGRAPHIC, document, parent);
        graphic(document, legendGraphic);

        return legendGraphic;
    }

    static Element filter(Document document, Node parent) {
        Element filter = element(OGC.Filter, document, parent);
        Element featureId = element(OGC.FeatureId, document, filter);
        featureId.setAttribute("fid", "someFid");

        return filter;
    }

    static Element rule(Document document, Node parent) {
        Element rule = element(SLD.RULE, document, parent);
        element(SLD.NAME, document, rule, "theName");
        element(SLD.TITLE, document, rule, "theTitle");
        element(SLD.ABSTRACT, document, rule, "theAbstract");
        legendGraphic(document, rule);

        filter(document, rule);

        element(SLD.MINSCALEDENOMINATOR, document, rule, "1.0");
        element(SLD.MAXSCALEDENOMINATOR, document, rule, "1.0");

        pointSymbolizer(document, rule);
        lineSymbolizer(document, rule);
        polygonSymbolizer(document, rule);
        rasterSymbolizer(document, rule);
        textSymbolizer(document, rule);

        return rule;
    }

    static Element featureTypeStyle(Document document, Node parent) {
        Element featureTypeStyle = element(SLD.FEATURETYPESTYLE, document, parent);
        element(SLD.NAME, document, featureTypeStyle, "theName");
        element(SLD.TITLE, document, featureTypeStyle, "theTitle");
        element(SLD.ABSTRACT, document, featureTypeStyle, "theAbstract");
        element(SLD.FEATURETYPENAME, document, featureTypeStyle, "theFeatureTypeName");

        element(SLD.SEMANTICTYPEIDENTIFIER, document, featureTypeStyle, "semanticTypeId1");
        element(SLD.SEMANTICTYPEIDENTIFIER, document, featureTypeStyle, "semanticTypeId2");

        rule(document, featureTypeStyle);
        rule(document, featureTypeStyle);

        return featureTypeStyle;
    }

    static Element namedStyle(Document document, Node parent) {
        Element namedStyle = element(SLD.NAMEDSTYLE, document, parent);
        element(SLD.NAME, document, namedStyle, "theName");

        return namedStyle;
    }

    static Element userStyle(Document document, Node parent) {
        Element userStyle = element(SLD.USERSTYLE, document, parent);

        element(SLD.NAME, document, userStyle, "theName");
        element(SLD.TITLE, document, userStyle, "theTitle");
        element(SLD.ABSTRACT, document, userStyle, "theAbstract");
        element(SLD.ISDEFAULT, document, userStyle, "true");

        featureTypeStyle(document, userStyle);
        featureTypeStyle(document, userStyle);

        return userStyle;
    }

    static Element extent(Document document, Node parent) {
        Element extent = element(SLD.EXTENT, document, parent);
        element(SLD.NAME, document, extent, "theName");
        element(SLD.VALUE, document, extent, "theValue");

        return extent;
    }

    static Element featureTypeConstraint(Document document, Node parent) {
        Element featureTypeConstraint = element(SLD.FEATURETYPECONSTRAINT, document, parent);

        element(SLD.FEATURETYPENAME, document, featureTypeConstraint, "theFeatureTypeName");
        filter(document, featureTypeConstraint);
        extent(document, featureTypeConstraint);
        extent(document, featureTypeConstraint);

        return featureTypeConstraint;
    }

    static Element layerFeatureConstraints(Document document, Node parent) {
        Element layerFeatureConstraints = element(SLD.LAYERFEATURECONSTRAINTS, document, parent);

        featureTypeConstraint(document, layerFeatureConstraints);
        featureTypeConstraint(document, layerFeatureConstraints);

        return layerFeatureConstraints;
    }

    static Element namedLayer(Document document, Node parent) {
        Element namedLayer = element(SLD.NAMEDLAYER, document, parent);
        element(SLD.NAME, document, namedLayer, "theName");

        return namedLayer;
    }

    static Element userLayer(Document document, Node parent) {
        Element namedLayer = element(SLD.USERLAYER, document, parent);

        element(SLD.NAME, document, namedLayer, "theName");

        layerFeatureConstraints(document, namedLayer);
        userStyle(document, namedLayer);
        userStyle(document, namedLayer);

        return namedLayer;
    }

    static Element styledLayerDescriptor(Document document, Node parent) {
        Element sld = element(SLD.STYLEDLAYERDESCRIPTOR, document, parent);

        element(SLD.NAME, document, sld, "theName");
        element(SLD.TITLE, document, sld, "theTitle");
        element(SLD.ABSTRACT, document, sld, "theAbstract");

        userLayer(document, sld);
        userLayer(document, sld);

        return sld;
    }

    static Element element(QName name, Document document, Node parent) {
        Element element = document.createElementNS(name.getNamespaceURI(), name.getLocalPart());

        if (parent != null) {
            parent.appendChild(element);
        }

        return element;
    }

    static Element element(QName name, Document document, Node parent, String text) {
        Element element = element(name, document, parent);

        if (text != null) {
            element.appendChild(document.createTextNode(text));
        }

        return element;
    }
}
