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
package org.geotools.sld;

import org.picocontainer.MutablePicoContainer;
import org.geotools.filter.v1_0.OGCConfiguration;
import org.geotools.sld.bindings.SLD;
import org.geotools.sld.bindings.SLDAnchorPointBinding;
import org.geotools.sld.bindings.SLDChannelSelectionBinding;
import org.geotools.sld.bindings.SLDColorMapBinding;
import org.geotools.sld.bindings.SLDColorMapEntryBinding;
import org.geotools.sld.bindings.SLDContrastEnhancementBinding;
import org.geotools.sld.bindings.SLDCssParameterBinding;
import org.geotools.sld.bindings.SLDDisplacementBinding;
import org.geotools.sld.bindings.SLDExtentBinding;
import org.geotools.sld.bindings.SLDExternalGraphicBinding;
import org.geotools.sld.bindings.SLDFeatureTypeConstraintBinding;
import org.geotools.sld.bindings.SLDFeatureTypeStyleBinding;
import org.geotools.sld.bindings.SLDFillBinding;
import org.geotools.sld.bindings.SLDFontBinding;
import org.geotools.sld.bindings.SLDGeometryBinding;
import org.geotools.sld.bindings.SLDGraphicBinding;
import org.geotools.sld.bindings.SLDGraphicFillBinding;
import org.geotools.sld.bindings.SLDGraphicStrokeBinding;
import org.geotools.sld.bindings.SLDHaloBinding;
import org.geotools.sld.bindings.SLDImageOutlineBinding;
import org.geotools.sld.bindings.SLDLabelPlacementBinding;
import org.geotools.sld.bindings.SLDLayerFeatureConstraintsBinding;
import org.geotools.sld.bindings.SLDLegendGraphicBinding;
import org.geotools.sld.bindings.SLDLinePlacementBinding;
import org.geotools.sld.bindings.SLDLineSymbolizerBinding;
import org.geotools.sld.bindings.SLDMarkBinding;
import org.geotools.sld.bindings.SLDNamedLayerBinding;
import org.geotools.sld.bindings.SLDNamedStyleBinding;
import org.geotools.sld.bindings.SLDOnlineResourceBinding;
import org.geotools.sld.bindings.SLDOverlapBehaviorBinding;
import org.geotools.sld.bindings.SLDParameterValueTypeBinding;
import org.geotools.sld.bindings.SLDPerpendicularOffsetBinding;
import org.geotools.sld.bindings.SLDPointPlacementBinding;
import org.geotools.sld.bindings.SLDPointSymbolizerBinding;
import org.geotools.sld.bindings.SLDPolygonSymbolizerBinding;
import org.geotools.sld.bindings.SLDRasterSymbolizerBinding;
import org.geotools.sld.bindings.SLDRemoteOWSBinding;
import org.geotools.sld.bindings.SLDRuleBinding;
import org.geotools.sld.bindings.SLDSelectedChannelTypeBinding;
import org.geotools.sld.bindings.SLDShadedReliefBinding;
import org.geotools.sld.bindings.SLDStrokeBinding;
import org.geotools.sld.bindings.SLDStyledLayerDescriptorBinding;
import org.geotools.sld.bindings.SLDSymbolizerBinding;
import org.geotools.sld.bindings.SLDSymbolizerTypeBinding;
import org.geotools.sld.bindings.SLDTextSymbolizerBinding;
import org.geotools.sld.bindings.SLDUserLayerBinding;
import org.geotools.sld.bindings.SLDUserStyleBinding;
import org.geotools.sld.bindings.SLDVendorOptionBinding;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyleFactoryImpl;
import org.geotools.xml.Configuration;
import org.geotools.xml.Parser;


/**
 * Parser configuration for the Styled Layer Descriptor  schema.
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 * @source $URL$
 */
public class SLDConfiguration extends Configuration {
    /**
     * Adds a dependency on {@link OGCConfiguration}
     */
    public SLDConfiguration() {
        super(SLD.getInstance());

        addDependency(new OGCConfiguration());
    }

    protected void registerBindings(MutablePicoContainer container) {
        //Types
        container.registerComponentImplementation(SLD.PARAMETERVALUETYPE,
            SLDParameterValueTypeBinding.class);
        container.registerComponentImplementation(SLD.SELECTEDCHANNELTYPE,
            SLDSelectedChannelTypeBinding.class);
        container.registerComponentImplementation(SLD.SYMBOLIZERTYPE, SLDSymbolizerTypeBinding.class);

        //Elements
        container.registerComponentImplementation(SLD.ANCHORPOINT, SLDAnchorPointBinding.class);
        container.registerComponentImplementation(SLD.CHANNELSELECTION,
            SLDChannelSelectionBinding.class);
        container.registerComponentImplementation(SLD.COLORMAP, SLDColorMapBinding.class);
        container.registerComponentImplementation(SLD.COLORMAPENTRY, SLDColorMapEntryBinding.class);
        container.registerComponentImplementation(SLD.CONTRASTENHANCEMENT,
            SLDContrastEnhancementBinding.class);
        container.registerComponentImplementation(SLD.CSSPARAMETER, SLDCssParameterBinding.class);
        container.registerComponentImplementation(SLD.DISPLACEMENT, SLDDisplacementBinding.class);

        container.registerComponentImplementation(SLD.EXTENT, SLDExtentBinding.class);
        container.registerComponentImplementation(SLD.EXTERNALGRAPHIC,
            SLDExternalGraphicBinding.class);
        container.registerComponentImplementation(SLD.FEATURETYPECONSTRAINT,
            SLDFeatureTypeConstraintBinding.class);

        container.registerComponentImplementation(SLD.FEATURETYPESTYLE,
            SLDFeatureTypeStyleBinding.class);
        container.registerComponentImplementation(SLD.FILL, SLDFillBinding.class);
        container.registerComponentImplementation(SLD.FONT, SLDFontBinding.class);

        container.registerComponentImplementation(SLD.GEOMETRY, SLDGeometryBinding.class);
        container.registerComponentImplementation(SLD.GRAPHIC, SLDGraphicBinding.class);
        container.registerComponentImplementation(SLD.GRAPHICFILL, SLDGraphicFillBinding.class);
        container.registerComponentImplementation(SLD.GRAPHICSTROKE, SLDGraphicStrokeBinding.class);

        container.registerComponentImplementation(SLD.HALO, SLDHaloBinding.class);

        container.registerComponentImplementation(SLD.IMAGEOUTLINE, SLDImageOutlineBinding.class);

        container.registerComponentImplementation(SLD.LABELPLACEMENT, SLDLabelPlacementBinding.class);

        container.registerComponentImplementation(SLD.LAYERFEATURECONSTRAINTS,
            SLDLayerFeatureConstraintsBinding.class);
        container.registerComponentImplementation(SLD.LEGENDGRAPHIC, SLDLegendGraphicBinding.class);
        container.registerComponentImplementation(SLD.LINEPLACEMENT, SLDLinePlacementBinding.class);
        container.registerComponentImplementation(SLD.LINESYMBOLIZER, SLDLineSymbolizerBinding.class);
        container.registerComponentImplementation(SLD.MARK, SLDMarkBinding.class);

        container.registerComponentImplementation(SLD.NAMEDLAYER, SLDNamedLayerBinding.class);
        container.registerComponentImplementation(SLD.NAMEDSTYLE, SLDNamedStyleBinding.class);

        container.registerComponentImplementation(SLD.ONLINERESOURCE, SLDOnlineResourceBinding.class);

        container.registerComponentImplementation(SLD.OVERLAPBEHAVIOR,
            SLDOverlapBehaviorBinding.class);
        container.registerComponentImplementation(SLD.PERPENDICULAROFFSET,
            SLDPerpendicularOffsetBinding.class);
        container.registerComponentImplementation(SLD.POINTPLACEMENT, SLDPointPlacementBinding.class);
        container.registerComponentImplementation(SLD.POINTSYMBOLIZER,
            SLDPointSymbolizerBinding.class);
        container.registerComponentImplementation(SLD.POLYGONSYMBOLIZER,
            SLDPolygonSymbolizerBinding.class);

        container.registerComponentImplementation(SLD.RASTERSYMBOLIZER,
            SLDRasterSymbolizerBinding.class);

        container.registerComponentImplementation(SLD.REMOTEOWS, SLDRemoteOWSBinding.class);

        container.registerComponentImplementation(SLD.RULE, SLDRuleBinding.class);

        container.registerComponentImplementation(SLD.SHADEDRELIEF, SLDShadedReliefBinding.class);

        container.registerComponentImplementation(SLD.STROKE, SLDStrokeBinding.class);
        container.registerComponentImplementation(SLD.STYLEDLAYERDESCRIPTOR,
            SLDStyledLayerDescriptorBinding.class);
        container.registerComponentImplementation(SLD.SYMBOLIZER, SLDSymbolizerBinding.class);
        container.registerComponentImplementation(SLD.TEXTSYMBOLIZER, SLDTextSymbolizerBinding.class);

        container.registerComponentImplementation(SLD.USERLAYER, SLDUserLayerBinding.class);
        container.registerComponentImplementation(SLD.USERSTYLE, SLDUserStyleBinding.class);
        
        container.registerComponentImplementation(SLD.VENDOROPTION, SLDVendorOptionBinding.class);
    }

    /**
     * Configures the sld context.
     * <p>
     * The following factories are registered:
     * <ul>
     * <li>{@link StyleFactoryImpl.class} under {@link StyleFactory.class}
     * </ul>
     * </p>
     */
    protected void configureContext(MutablePicoContainer container) {
        super.configureContext(container);

        container.registerComponentImplementation(StyleFactory.class, StyleFactoryImpl.class);
    }
    
    @Override
    protected void configureParser(Parser parser) {
        parser.setHandleMixedContent(true);
    }
}
