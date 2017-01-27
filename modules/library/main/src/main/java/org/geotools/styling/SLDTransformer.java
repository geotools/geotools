/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003 - 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.styling;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import javax.measure.quantity.Length;
import javax.measure.unit.Unit;

import org.geotools.data.DataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.FilterTransformer;
import org.geotools.gml.producer.FeatureTransformer;
import org.geotools.referencing.CRS;
import org.geotools.util.GrowableInternationalString;
import org.geotools.xml.transform.TransformerBase;
import org.geotools.xml.transform.Translator;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.style.ContrastMethod;
import org.opengis.style.SemanticType;
import org.opengis.util.InternationalString;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Produces SLD to an output stream.
 *
 * @author Ian Schneider
 *
 *
 * @source $URL$
 */
public class SLDTransformer extends TransformerBase {
    /** The logger for this package. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger("org.geotools.styling");

    static final String XLINK_NAMESPACE = "http://www.w3.org/1999/xlink";

    static final FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
    
    static final Font DEFAULT_FONT = CommonFactoryFinder.getStyleFactory().getDefaultFont();

    /**
     * Additional namespace mappings to emit in the start element of the generated. Each entry has a URI key and an associated prefix string value.
     */
    final private Map uri2prefix;
    
    /**
     * don't suppress the export of default values
     */
    private boolean exportDefaultValues = false;

    /**
     * Construct a new instance of <code>SLDTransformer</code> with the default namespace mappings usually found in a simple Styled Layer Descriptor
     * element.
     */
    public SLDTransformer() {
        this(null);
    }

    /**
     * Construct a new instance of <code>SLDTransformer</code> with the additional namespace mappings contained in <code>nsBindings</code>.
     * <p>
     * The designated collection contains mappings of {@link URI} to associated prefix (string) to emit in the generated XML element.
     */
    public SLDTransformer(Map nsBindings) {
        super();
        if (nsBindings == null || nsBindings.isEmpty()) {
            uri2prefix = new HashMap();
        } else {
            uri2prefix = new HashMap(nsBindings.size());
            int count = 0;
            for (Iterator it = nsBindings.entrySet().iterator(); it.hasNext();) {
                Map.Entry e = (Entry) it.next();
                URI uri = (URI) e.getKey();
                String prefix = (String) e.getValue();
                if (uri != null && prefix != null) {
                    uri2prefix.put(uri, prefix.trim());
                    count++;
                }
            }
            LOGGER.info("Added [" + count + "] namespace entries resulting in [" + uri2prefix.size()
                    + "] distinct entries");
        }
    }

    /**
     * @return the exportDefaultValues
     */
    public boolean isExportDefaultValues() {
        return exportDefaultValues;
    }

    /**
     * @param exportDefaultValues the exportDefaultValues to set
     */
    public void setExportDefaultValues(boolean exportDefaultValues) {
        this.exportDefaultValues = exportDefaultValues;
    }
    
    public Translator createTranslator(ContentHandler handler) {
        Translator result = new SLDTranslator(handler);
        // add pre-configured namespace mappings
        if (!uri2prefix.isEmpty()) {
            for (Iterator it = uri2prefix.entrySet().iterator(); it.hasNext();) {
                Map.Entry e = (Entry) it.next();
                URI uri = (URI) e.getKey();
                if (uri != null) {
                    String prefix = (String) e.getValue();
                    // FIXME handle default namespace and possible clash with
                    // one already known to the namespace-support delegate; i.e.
                    // the entry with an empty prefix
                    String uriStr = String.valueOf(uri);
                    result.getNamespaceSupport().declarePrefix(prefix, uriStr);
                }
            }
        }
        ((SLDTranslator)result).setExportDefaultValues(isExportDefaultValues());
        return result;
    }


    /**
     * Currently does nothing.
     * 
     * @param args DOCUMENT ME!
     * 
     * @throws Exception DOCUMENT ME!
     */
    public static final void main(String[] args) throws Exception {
        java.net.URL url = new java.io.File(args[0]).toURI().toURL();
        SLDParser s = new SLDParser(CommonFactoryFinder.getStyleFactory(null), url);
        SLDTransformer transformer = new SLDTransformer();
        transformer.setIndentation(4);
        transformer.transform(s.readXML(),
                new FileOutputStream(System.getProperty("java.io.tmpdir") + "/junk.eraseme"));
    }

    /**
     * Translates the Style data structure into a series of XML events that can be encoded etc...
     * <p>
     * This Translator makes use of the following (currently hardcoded) information:
     * <ul>
     * <li>prefix: sld
     * <li>namespace: http://www.opengis.net/sld
     * </ul>
     * 
     * @author Jody
     */
    static class SLDTranslator extends TranslatorSupport implements StyleVisitor {
        /**
         * Handles any Filters used in our data structure.
         */
        FilterTransformer.FilterTranslator filterTranslator;
        
        private boolean exportDefaultValues = false;
        
        /**
         * Translates into the default of prefix "sld" for "http://www.opengis.net/sld".
         * 
         * @param handler
         */
        public SLDTranslator(ContentHandler handler) {
            this(handler, "sld", "http://www.opengis.net/sld");
        }
        /**
         * Translates
         * 
         * @param handler
         */
        public SLDTranslator(ContentHandler handler, String prefix, String uri) {
            super(handler, prefix, uri);
            filterTranslator = new FilterTransformer.FilterTranslator(handler);
            addNamespaceDeclarations(filterTranslator);
        }

        boolean isNull(Expression expr) {
            if (expr == null)
                return true;
            if (expr == Expression.NIL)
                return true;
            if (expr instanceof Literal) {
                Literal literal = (Literal) expr;
                return literal.getValue() == null;
            }
            return false; // must be some other non null thing
        }

        boolean isDefault(Expression expr, Object defaultValue) {
            if (isExportDefaultValues()) {
                return false;
            }
            if (defaultValue == null)
                return isNull(expr);

            if (expr == null)
                return false;
            if (expr == Expression.NIL)
                return false;
            if (expr instanceof Literal) {
                Literal literal = (Literal) expr;
                if (defaultValue.equals(literal.getValue())) {
                    return true;
                }
                if (defaultValue.toString().equals(literal.getValue().toString())) {
                    return true;
                }
            }
            return false;
        }

        /**
         * @param exportDefaultValues
         */
        public void setExportDefaultValues(boolean exportDefaultValues) {
          this.exportDefaultValues = exportDefaultValues; 
            
        }

        /**
         * @return the exportDefaultValues
         */
        public boolean isExportDefaultValues() {
            return exportDefaultValues;
        }




       

        /**
         * Utility method used to quickly package up the provided expression.
         * 
         * @param element
         * @param expr
         */
        void element(String element, Expression expr) {
            element(element, expr, null);
        }

        /**
         * Utility method used to quickly package up the provided InternationalString.
         * 
         * @param element
         * @param expr
         */
        void element(String element, InternationalString intString) {
            if (intString instanceof GrowableInternationalString) {
                GrowableInternationalString growable = (GrowableInternationalString) intString;
                if (growable.getLocales().isEmpty()) {
                    element(element, intString.toString());
                } else {
                    start(element);
                    chars(intString.toString());
                    for (Locale locale : growable.getLocales()) {
                        if (locale != null) {
                            AttributesImpl atts = new AttributesImpl();
                            atts.addAttribute("", "lang", "lang", "", locale.toString());
                            element("Localized", growable.toString(locale), atts);
                        }
                    }
                    end(element);
                }
            } else {
                element(element, intString.toString());
            }
        }

        /**
         * Utility method used to quickly package up the provided expression.
         * 
         * @param element
         * @param expr
         */
        void element(String element, Expression expr, Object defaultValue) {
            element(element, expr, defaultValue, null);
        }

        void element(String element, Expression expr, Object defaultValue, AttributesImpl atts) {
            if (expr == null || expr == Expression.NIL)
                return;

            // skip encoding if we are using the default value
            if (expr instanceof Literal) {
                if (defaultValue != null) {
                    Object value = expr.evaluate(null, defaultValue.getClass());
                    if (value != null && (!value.equals(defaultValue)||isExportDefaultValues())) {
                        element(element, value.toString(), atts);
                    }
                } else {
                    String value = expr.evaluate(null, String.class);
                    if (value != null) {
                        element(element, value, atts);
                    }
                }
                return;
            }

            start(element, atts);
            filterTranslator.encode(expr);
            end(element);
        }

        void labelContent(Expression expr) {
            if (expr instanceof Literal) {
                Literal literalLabel = ((Literal) expr);
                String label = literalLabel.evaluate(null, String.class);
                if (label != null) {
                    // do we need a CDATA expansion?
                    if (label.matches("^\\s+.*$|^.*\\s+$|^.*\\s{2,}.*$")) {
                        cdata(label);
                    } else {
                        chars(label);
                    }
                }
            } else if (expr instanceof Function && ("strConcat".equals(((Function) expr).getName())
                    || "concat".equals(((Function) expr).getName())
                    || "Concatenate".equals(((Function) expr).getName()))) {
                List<Expression> parameters = ((Function) expr).getParameters();
                for (Expression parameter : parameters) {
                    labelContent(parameter);
                }
            } else {
                filterTranslator.encode(expr);
            }
        }

        /**
         * To be used when the expression is a single literal whose value must be written out as element.
         * <p>
         * For Example OverlapBehaviour is represented as an expression but v 1.0.0 specifications do not define it as an expression. (&ltAVERAGE/&gt)
         * </p>
         * 
         */
        void elementLiteral(String element, Expression e, String defaultValue) {
            if (e == null || e == Expression.NIL)
                return;

            final String value = e.evaluate(null, String.class);
            if (defaultValue == null || !defaultValue.equals(value)) {
                start(element);
                start(value);
                end(value);
                end(element);
            }
        }

        public void visit(PointPlacement pp) {
            start("LabelPlacement");
            start("PointPlacement");

            if (pp.getAnchorPoint() != null) {
                pp.getAnchorPoint().accept(this);
            }

            visit(pp.getDisplacement());

            encodeValue("Rotation", null, pp.getRotation(), Double.valueOf(0.0));
            end("PointPlacement");
            end("LabelPlacement");
        }

        public void visit(Stroke stroke) {
            start("Stroke");

            if (stroke.getGraphicFill() != null) {
                start("GraphicFill");
                stroke.getGraphicFill().accept(this);
                end("GraphicFill");
            }

            if (stroke.getGraphicStroke() != null) {
                start("GraphicStroke");
                stroke.getGraphicStroke().accept(this);
                end("GraphicStroke");
            }

            encodeCssParam("stroke", stroke.getColor(), Color.BLACK);
            encodeCssParam("stroke-linecap", stroke.getLineCap(), "butt");
            encodeCssParam("stroke-linejoin", stroke.getLineJoin(), "miter");
            encodeCssParam("stroke-opacity", stroke.getOpacity(), 1.0);
            encodeCssParam("stroke-width", stroke.getWidth(), 1.0);
            encodeCssParam("stroke-dashoffset", stroke.getDashOffset(), 0.0);

            encodeStrokeDasharray(stroke.dashArray());

            end("Stroke");
        }

        private void encodeStrokeDasharray(List<Expression> expressions) {
            if (expressions == null || expressions.isEmpty()) return;
            boolean isLiteral = true;
            for (Expression expression : expressions) {
                if(!(expression instanceof Literal)) isLiteral = false;
            }
            if(isLiteral) encodeLiteralStrokeDasharray(expressions);
            else encodeMixedStrokeDasharray(expressions);
        }

        private void encodeLiteralStrokeDasharray(List<Expression> expressions) {
           StringBuilder literalDash = new StringBuilder();
            for (Expression expression : expressions) {
                literalDash.append(((Literal) expression).getValue()).append(" ");
            }
            literalDash.deleteCharAt(literalDash.length() - 1);
            encodeCssParam("stroke-dasharray", ff.literal(literalDash.toString()));
        }

        private void encodeMixedStrokeDasharray(List<Expression> expressions) {
            AttributesImpl attributes = new AttributesImpl();
            attributes.addAttribute("", "name", "name", "", "stroke-dasharray");
            start("CssParameter", attributes);
            for (Expression expression : expressions) {
                filterTranslator.encode(expression);
            }
            end("CssParameter");
        }

        public void visit(LinePlacement lp) {
            start("LabelPlacement");
            start("LinePlacement");
            element("PerpendicularOffset", lp.getPerpendicularOffset());
            end("LinePlacement");
            end("LabelPlacement");
        }

        public void visit(AnchorPoint ap) {
            start("AnchorPoint");
            element("AnchorPointX", ap.getAnchorPointX());
            element("AnchorPointY", ap.getAnchorPointY());
            end("AnchorPoint");
        }

        public void visit(TextSymbolizer text) {
            if (text == null) {
                return;
            }

            // adds the uom attribute according to the OGC SE specification
            AttributesImpl atts = new AttributesImpl();
            Unit<Length> uom = text.getUnitOfMeasure();
            if (uom != null)
                atts.addAttribute("", "uom", "uom", "", UomOgcMapping.get(uom).getSEString());

            start("TextSymbolizer", atts);

            encodeGeometryExpression(text.getGeometry());

            if (text.getLabel() != null) {
                start("Label");
                labelContent(text.getLabel());
                end("Label");
            }

            if ((text.fonts() != null) && (!text.fonts().isEmpty())) {
                List<Font> fonts = text.fonts();
                if(areFontsUniform(fonts)) {
                    // go for standard encoding, SLD 1.0 does not allow more than one 
                    // Font item in a TextSymbolizer
                    start("Font");
                    
                    Font initialFont = fonts.get(0);
                    for (Font font : fonts ) {
                        encodeCssParam("font-family", font.getFamily().get(0));
                    }
                    encodeCssParam("font-size", initialFont.getSize());
                    encodeCssParam("font-style", initialFont.getStyle());
                    encodeCssParam("font-weight", initialFont.getWeight());
                    end("Font");
                } else {
                    // use a GT specific encoding with multiple fonts, matching our
                    // internal data model (which we can also parse)
                    for (Font font : fonts ) {
                        start("Font");
                        encodeCssParam("font-family", font.getFamily().get(0));
                        encodeCssParam("font-size", font.getSize());
                        encodeCssParam("font-style", font.getStyle());
                        encodeCssParam("font-weight", font.getWeight());
                        end("Font");
                    }
                }
            }

            if (text.getLabelPlacement() != null) {
                text.getLabelPlacement().accept(this);
            }

            if (text.getHalo() != null) {
                text.getHalo().accept(this);
            }

            if (text.getFill() != null) {
                text.getFill().accept(this);
            }

            if (text instanceof TextSymbolizer2) {
                TextSymbolizer2 text2 = (TextSymbolizer2) text;
                if (text2.getGraphic() != null)
                    visit(text2.getGraphic());
                if (text2.getSnippet() != null)
                    element("Snippet", text2.getSnippet());
                if (text2.getFeatureDescription() != null)
                    element("FeatureDescription", text2.getFeatureDescription());
                OtherText otherText = text2.getOtherText();
                if (otherText != null) {
                    AttributesImpl otherTextAtts = new AttributesImpl();
                    otherTextAtts.addAttribute("", "target", "target", "", otherText.getTarget());
                    element("OtherText", otherText.getText(), null, otherTextAtts);
                }
            }

            if (text.getPriority() != null) {
                element("Priority", text.getPriority());
            }

            if (text.getOptions() != null) {
                encodeVendorOptions(text.getOptions());
            }

            end("TextSymbolizer");
        }

        /**
         * Returns true if the list of fonts has the same settings for
         * everything besides the font family, and can thus be represented
         * as a single Font element
         * @param fonts
         * @return
         */
        private boolean areFontsUniform(List<Font> fonts) {
            if(fonts.size() == 1) {
                return true;
            }
            
            Font reference = fonts.get(0);
            Expression referenceSize = reference.getSize();
            Expression referenceStyle = reference.getStyle();
            Expression referenceWeight = reference.getWeight();
            for (int i = 1; i < fonts.size() ; i++) {
                Font f = fonts.get(i);
                Expression size = f.getSize();
                if(!expressionEquals(referenceSize, size, DEFAULT_FONT.getSize())) {
                    return false;
                }
                Expression style = f.getStyle();
                if(!expressionEquals(referenceStyle, style, DEFAULT_FONT.getStyle())) {
                    return false;
                }
                Expression weight = f.getWeight();
                if(!expressionEquals(referenceWeight, weight, DEFAULT_FONT.getWeight())) {
                    return false;
                }
            }
            
            return true;
        }

        private boolean expressionEquals(Expression reference, Expression exp, Expression defaultValue) {
            if(exp == null) {
                return reference == null || defaultValue.equals(reference);
            } else if(reference == null) {
                return defaultValue.equals(exp);
            } else {
                return reference.equals(exp);
            }
        }

        public void visit(RasterSymbolizer raster) {
            if (raster == null) {
                return;
            }

            // adds the uom attribute according to the OGC SE specification
            AttributesImpl atts = new AttributesImpl();
            Unit<Length> uom = raster.getUnitOfMeasure();
            if (uom != null)
                atts.addAttribute("", "uom", "uom", "", UomOgcMapping.get(uom).getSEString());

            start("RasterSymbolizer", atts);

            encodeGeometryExpression(raster.getGeometry());

            element("Opacity", raster.getOpacity(), 1.0);

            if (raster.getChannelSelection() != null) {
                final ChannelSelection cs = raster.getChannelSelection();
                if (cs.getGrayChannel() != null) {
                    start("ChannelSelection");
                    SelectedChannelType gray = cs.getGrayChannel();

                    start("GrayChannel");
                    gray.accept(this);
                    end("GrayChannel");

                    end("ChannelSelection");
                } else if (cs.getRGBChannels() != null && cs.getRGBChannels().length == 3
                        && cs.getRGBChannels()[0] != null && cs.getRGBChannels()[1] != null
                        && cs.getRGBChannels()[2] != null) {
                    start("ChannelSelection");
                    SelectedChannelType[] rgb = cs.getRGBChannels();

                    start("RedChannel");
                    rgb[0].accept(this);
                    end("RedChannel");

                    start("GreenChannel");
                    rgb[1].accept(this);
                    end("GreenChannel");

                    start("BlueChannel");
                    rgb[2].accept(this);
                    end("BlueChannel");

                    end("ChannelSelection");
                } else {
                    // we have an invalid ChannelSelection ?
                }
            }

            if (raster.getOverlap() != null) {
                Expression overlaps = raster.getOverlap();
                if (overlaps instanceof PropertyName) {
                    final String pn = ((PropertyName) overlaps).getPropertyName();
                    if ("RANDOM".equals(pn)) {
                        start("OverlapBehavior");
                        start(pn);
                        end(pn);
                        end("OverlapBehavior");
                    }
                } else {
                    // this expression needs to be converted to a single string and then written
                    // 1.0.0 specs don't allow it to be written as an expression
                    elementLiteral("OverlapBehavior", overlaps, "RANDOM");
                }
            }

            ColorMap colorMap = raster.getColorMap();
            if (colorMap != null && colorMap.getColorMapEntries() != null
                    && colorMap.getColorMapEntries().length > 0) {
                colorMap.accept(this);
            }

            if (raster.getContrastEnhancement() != null) {
                raster.getContrastEnhancement().accept(this);
            }

            if (raster.getShadedRelief() != null) {
                raster.getShadedRelief().accept(this);
            }

            if (raster.getImageOutline() != null) {
                start("ImageOutline");
                raster.getImageOutline().accept(this);
                end("ImageOutline");
            }

            end("RasterSymbolizer");
        }

        public void visit(ColorMap colorMap) {
            // The type of the ColorMap is stored in an attribute "type" and may store
            // string-values: "ramp", "intervals" or "values".
            AttributesImpl atts = new AttributesImpl();
            String typeString;
            if (colorMap.getType() == ColorMap.TYPE_INTERVALS)
                typeString = "intervals";
            else if (colorMap.getType() == ColorMap.TYPE_VALUES)
                typeString = "values";
            else
                typeString = "ramp"; // Also the default in the parser
            if (!"ramp".equals(typeString)) {
                atts.addAttribute("", "type", "type", "", typeString);
            }
            final boolean extended = colorMap.getExtendedColors();
            if (extended) {
                atts.addAttribute("", "extended", "extended", "", "" + extended);
            }

            start("ColorMap", atts);
            ColorMapEntry[] mapEntries = colorMap.getColorMapEntries();
            for (int i = 0; i < mapEntries.length; i++) {
                mapEntries[i].accept(this);
            }
            end("ColorMap");
        }

        public void visit(ColorMapEntry colorEntry) {
            if (colorEntry != null) {
                AttributesImpl atts = new AttributesImpl();
                atts.addAttribute("", "color", "color", "",
                        colorEntry.getColor().evaluate(null, String.class));
                if (colorEntry.getOpacity() != null) {
                    atts.addAttribute("", "opacity", "opacity", "",
                            colorEntry.getOpacity().toString());
                }
                if (colorEntry.getQuantity() != null) {
                    atts.addAttribute("", "quantity", "quantity", "",
                            colorEntry.getQuantity().toString());
                }
                if (colorEntry.getLabel() != null) {
                    atts.addAttribute("", "label", "label", "", colorEntry.getLabel());
                }
                element("ColorMapEntry", (String) null, atts);
            }
        }

        public void visit(Symbolizer sym) {
            try {
                contentHandler.startElement("", "!--", "!--", NULL_ATTS);
                chars("Unidentified Symbolizer " + sym.getClass());
                contentHandler.endElement("", "--", "--");
            } catch (SAXException se) {
                throw new RuntimeException(se);
            }
        }

        public void visit(PolygonSymbolizer poly) {

            // adds the uom attribute according to the OGC SE specification
            AttributesImpl atts = new AttributesImpl();
            Unit<Length> uom = poly.getUnitOfMeasure();
            if (uom != null)
                atts.addAttribute("", "uom", "uom", "", UomOgcMapping.get(uom).getSEString());

            start("PolygonSymbolizer", atts);
            encodeGeometryExpression(poly.getGeometry());

            if (poly.getFill() != null) {
                poly.getFill().accept(this);
            }

            if (poly.getStroke() != null) {
                poly.getStroke().accept(this);
            }

            if (poly.getOptions() != null) {
                encodeVendorOptions(poly.getOptions());
            }
            end("PolygonSymbolizer");
        }

        public void visit(ExternalGraphic exgr) {
            start("ExternalGraphic");

            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute(XMLNS_NAMESPACE, "xlink", "xmlns:xlink", "", XLINK_NAMESPACE);
            atts.addAttribute(XLINK_NAMESPACE, "type", "xlink:type", "", "simple");
            atts.addAttribute(XLINK_NAMESPACE, "xlink", "xlink:href", "", exgr.getURI());
            element("OnlineResource", (String) null, atts);

            element("Format", exgr.getFormat());

            end("ExternalGraphic");
        }

        public void visit(LineSymbolizer line) {

            // adds the uom attribute according to the OGC SE specification
            AttributesImpl atts = new AttributesImpl();
            Unit<Length> uom = line.getUnitOfMeasure();
            if (uom != null)
                atts.addAttribute("", "uom", "uom", "", UomOgcMapping.get(uom).getSEString());

            start("LineSymbolizer", atts);
            encodeGeometryExpression(line.getGeometry());

            if (line.getStroke() != null) {
                line.getStroke().accept(this);
            }
            if (line.getOptions() != null) {
                encodeVendorOptions(line.getOptions());
            }
            if (line.getPerpendicularOffset() != null) {
                element("PerpendicularOffset", line.getPerpendicularOffset());
            }
            end("LineSymbolizer");
        }

        public void visit(Fill fill) {
            start("Fill");

            if (fill.getGraphicFill() != null) {
                start("GraphicFill");
                fill.getGraphicFill().accept(this);
                end("GraphicFill");
            }

            encodeCssParam("fill", fill.getColor(), "#808080");
            encodeCssParam("fill-opacity", fill.getOpacity(), 1.0);
            end("Fill");
        }

        public void visit(Rule rule) {
            start("Rule");
            if (rule.getName() != null)
                element("Name", rule.getName());
            if (rule.getDescription() != null && rule.getDescription().getTitle() != null)
                element("Title", rule.getDescription().getTitle());
            if (rule.getDescription() != null && rule.getDescription().getAbstract() != null)
                element("Abstract", rule.getDescription().getAbstract());

            Graphic[] gr = rule.getLegendGraphic();
            for (int i = 0; i < gr.length; i++) {
                start("LegendGraphic");
                gr[i].accept(this);
                end("LegendGraphic");
            }

            Filter filter = rule.getFilter();
            if (filter == null || filter == Filter.INCLUDE) {
                // no filter
            } else {
                visit(filter);
            }

            if (rule.isElseFilter()) {
                start("ElseFilter");
                end("ElseFilter");
            }

            if (rule.getMinScaleDenominator() != 0.0) {
                element("MinScaleDenominator", rule.getMinScaleDenominator() + "");
            }

            if (rule.getMaxScaleDenominator() != Double.POSITIVE_INFINITY) {
                element("MaxScaleDenominator", rule.getMaxScaleDenominator() + "");
            }

            Symbolizer[] sym = rule.getSymbolizers();
            for (int i = 0; i < sym.length; i++) {
                sym[i].accept(this);
            }

            end("Rule");
        }

        public void visit(Mark mark) {
            start("Mark");
            if (mark.getWellKnownName() != null
                    && (!"square".equals(mark.getWellKnownName().evaluate(null))||isExportDefaultValues())) {
                encodeValue("WellKnownName", null, mark.getWellKnownName(), null);
            }

            if (mark.getFill() != null) {
                mark.getFill().accept(this);
            }

            if (mark.getStroke() != null) {
                mark.getStroke().accept(this);
            }

            end("Mark");
        }

        public void visit(PointSymbolizer ps) {

            // adds the uom attribute according to the OGC SE specification
            AttributesImpl atts = new AttributesImpl();
            Unit<Length> uom = ps.getUnitOfMeasure();
            if (uom != null)
                atts.addAttribute("", "uom", "uom", "", UomOgcMapping.get(uom).getSEString());

            start("PointSymbolizer", atts);

            encodeGeometryExpression(ps.getGeometry());

            ps.getGraphic().accept(this);

            if (ps.getOptions() != null) {
                encodeVendorOptions(ps.getOptions());
            }
            end("PointSymbolizer");
        }

        public void visit(Halo halo) {
            start("Halo");
            if (halo.getRadius() != null) {
                encodeValue("Radius", null, halo.getRadius(), null);
            }
            if (halo.getFill() != null) {
                halo.getFill().accept(this);
            }
            end("Halo");
        }

        public void visit(Graphic gr) {
            start("Graphic");

            // encodeGeometryProperty(gr.getGeometryPropertyName());

            Symbol[] symbols = gr.getSymbols();

            for (int i = 0; i < symbols.length; i++) {
                symbols[i].accept(this);
            }

            element("Opacity", gr.getOpacity(), 1.0);
            element("Size", gr.getSize());
            element("Rotation", gr.getRotation(), 0.0);
            if (gr.getAnchorPoint() != null) {
                visit(gr.getAnchorPoint());
            }
            if (gr.getDisplacement() != null) {
                visit(gr.getDisplacement());
            }

            end("Graphic");
        }

        public void visit(StyledLayerDescriptor sld) {
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute("", "version", "version", "", "1.0.0");
            start("StyledLayerDescriptor", atts);

            if ((sld.getName() != null) && (sld.getName().length() > 0)) {
                element("Name", sld.getName()); // optional
            }
            if ((sld.getTitle() != null) && (sld.getTitle().length() > 0)) {
                element("Title", sld.getTitle()); // optional
            }
            if ((sld.getAbstract() != null) && (sld.getAbstract().length() > 0)) {
                element("Abstract", sld.getAbstract()); // optional
            }

            StyledLayer[] layers = sld.getStyledLayers();

            for (int i = 0; i < layers.length; i++) {
                if (layers[i] instanceof NamedLayer) {
                    visit((NamedLayer) layers[i]);
                } else if (layers[i] instanceof UserLayer) {
                    visit((UserLayer) layers[i]);
                } else {
                    throw new IllegalArgumentException(
                            "StyledLayer '" + layers[i].getClass().toString() + "' not found");
                }
            }

            end("StyledLayerDescriptor");
        }

        public void visit(NamedLayer layer) {
            start("NamedLayer");
            element("Name", layer.getName());

            FeatureTypeConstraint[] lfc = layer.getLayerFeatureConstraints();
            if ((lfc != null) && lfc.length > 0) {
                start("LayerFeatureConstraints"); // optional
                for (int i = 0; i < lfc.length; i++) {
                    visit(lfc[i]);
                }
                end("LayerFeatureConstraints");
            }

            Style[] styles = layer.getStyles();

            for (int i = 0; i < styles.length; i++) {
                visit(styles[i]);
            }

            end("NamedLayer");
        }

        public void visit(UserLayer layer) {
            start("UserLayer");

            if ((layer.getName() != null) && (layer.getName().length() > 0)) {
                element("Name", layer.getName()); // optional
            }

            DataStore inlineFDS = layer.getInlineFeatureDatastore();
            if (inlineFDS != null) {
                visitInlineFeatureType(inlineFDS, layer.getInlineFeatureType());
            } else if (layer.getRemoteOWS() != null) {
                visit(layer.getRemoteOWS());
            }

            start("LayerFeatureConstraints"); // required
            FeatureTypeConstraint[] lfc = layer.getLayerFeatureConstraints();
            if ((lfc != null) && lfc.length > 0) {
                for (int i = 0; i < lfc.length; i++) {
                    visit(lfc[i]);
                }
            } else { // create an empty FeatureTypeConstraint, since it is required
                start("FeatureTypeConstraint");
                end("FeatureTypeConstraint");
            }
            end("LayerFeatureConstraints");

            Style[] styles = layer.getUserStyles();

            for (int i = 0; i < styles.length; i++) {
                visit(styles[i]);
            }

            end("UserLayer");
        }

        private void visitInlineFeatureType(DataStore dataStore, SimpleFeatureType featureType) {
            start("InlineFeature");
            try {
                final String ftName = featureType.getTypeName();
                final SimpleFeatureSource fs = dataStore.getFeatureSource(ftName);
                final SimpleFeatureCollection fc = fs.getFeatures();
                final FeatureTransformer ftrax = new FeatureTransformer();
                ftrax.setCollectionNamespace(null);
                ftrax.setCollectionPrefix(null);
                ftrax.setGmlPrefixing(true);
                ftrax.setIndentation(2);
                final CoordinateReferenceSystem crs = featureType.getGeometryDescriptor()
                        .getCoordinateReferenceSystem();
                String srsName = null;
                if (crs == null) {
                    LOGGER.warning("Null CRS in feature type named [" + ftName + "]. Ignore CRS");
                } else {
                    srsName = CRS.toSRS(crs, true); // single implementation of toSRS
                    if (srsName == null) {
                        // fallback on origional code
                        // assume the first named identifier of this CRS is its
                        // fully
                        // qualified code; e.g. authoriy and SRID
                        Set<ReferenceIdentifier> ids = crs.getIdentifiers();
                        if (ids == null || ids.isEmpty()) {
                            LOGGER.warning("Null or empty set of named identifiers " + "in CRS ["
                                    + crs + "] of feature type named [" + ftName + "]. Ignore CRS");
                        } else {
                            for (ReferenceIdentifier id : ids) {
                                if (id != null) {
                                    srsName = String.valueOf(id);
                                    break;
                                }
                            }
                        }
                    }
                    if (srsName != null) {
                        // Some Server implementations using older versions of this
                        // library barf on a fully qualified CRS name with messages
                        // like : "couldnt decode SRS - EPSG:EPSG:4326. currently
                        // only supporting EPSG #"; looks like they only needs the
                        // SRID. adjust
                        final int ndx = srsName.indexOf(':');
                        if (ndx > 0) {
                            LOGGER.info("Reducing CRS name [" + srsName + "] to its SRID");
                            srsName = srsName.substring(ndx + 1).trim();
                        }
                    }
                }
                if (srsName != null) {
                    ftrax.setSrsName(srsName);
                }

                final String defaultNS = this.getDefaultNamespace();
                ftrax.getFeatureTypeNamespaces().declareDefaultNamespace("", defaultNS);
                final String ns = featureType.getName().getNamespaceURI();
                if (ns == null) {
                    LOGGER.info("Null namespace URI in feature type named [" + ftName
                            + "]. Ignore namespace in features");
                } else {
                    // find the URI's prefix mapping in this namespace support
                    // delegate and use it; otherwise ignore it
                    final String prefix = this.nsSupport.getPrefix(ns);
                    if (prefix != null)
                        ftrax.getFeatureTypeNamespaces().declareNamespace(featureType, prefix, ns);
                }
                final Translator t = ftrax.createTranslator(this.contentHandler);
                t.encode(fc);
            } catch (IOException ignored) {
            }
            end("InlineFeature");
        }

        public void visit(RemoteOWS remoteOWS) {
            start("RemoteOWS");
            element("Service", remoteOWS.getService());
            element("OnlineResource", remoteOWS.getOnlineResource());
            end("RemoteOWS");
        }

        public void visit(FeatureTypeConstraint ftc) {
            start("FeatureTypeConstraint");

            if (ftc != null) {
                element("FeatureTypeName", ftc.getFeatureTypeName());
                visit(ftc.getFilter());

                Extent[] extent = ftc.getExtents();

                for (int i = 0; i < extent.length; i++) {
                    visit(extent[i]);
                }
            }

            end("FeatureTypeConstraint");
        }

        public void visit(Extent extent) {
            start("Extent");
            element("Name", extent.getName());
            element("Value", extent.getValue());
            end("Extent");
        }

        public void visit(Filter filter) {
            try {
                contentHandler.startElement("", "", "ogc:Filter", NULL_ATTS);
                filterTranslator.encode(filter);
                contentHandler.endElement("", "", "ogc:Filter");
            } catch (SAXException se) {
                throw new RuntimeException(se);
            }
        }

        public void visit(Style style) {
            if (style instanceof NamedStyle) {
                start("NamedStyle");
                element("Name", style.getName());
                end("NamedStyle");
            } else {
                start("UserStyle");
                element("Name", style.getName());
                if (style.getDescription() != null && style.getDescription().getTitle() != null)
                    element("Title", style.getDescription().getTitle());

                if (style.isDefault()) {
                    element("IsDefault", "1");
                }
                if (style.getDescription() != null && style.getDescription().getAbstract() != null)
                    element("Abstract", style.getDescription().getAbstract());
                FeatureTypeStyle[] fts = style.getFeatureTypeStyles();
                for (int i = 0; i < fts.length; i++) {
                    visit(fts[i]);
                }
                end("UserStyle");
            }
        }

        public void visit(FeatureTypeStyle fts) {
            start("FeatureTypeStyle");

            if ((fts.getName() != null) && (fts.getName().length() > 0)) {
                element("Name", fts.getName());
            }

            if (fts.getDescription() != null && fts.getDescription().getTitle() != null)
                element("Title", fts.getDescription().getTitle());
            if (fts.getDescription() != null && fts.getDescription().getAbstract() != null)
                element("Abstract", fts.getDescription().getAbstract());

            if ((fts.featureTypeNames() != null) && (fts.featureTypeNames().size() > 0)) {
                element("FeatureTypeName", fts.featureTypeNames().iterator().next().toString());
            }

            if (fts.getTransformation() != null) {
                element("Transformation", fts.getTransformation());
            }

            String[] sti = fts.getSemanticTypeIdentifiers();

            if (sti.length == 1 && sti[0].equals(SemanticType.ANY.toString())) {
                // skip, it's the default
            } else {
                for (int i = 0; i < sti.length; i++) {
                    element("SemanticTypeIdentifier", sti[i]);
                }
            }

            Rule[] rules = fts.getRules();

            for (int i = 0; i < rules.length; i++) {
                rules[i].accept(this);
            }

            encodeVendorOptions(fts.getOptions());

            end("FeatureTypeStyle");
        }

        public void visit(Displacement dis) {
            if (dis == null) {
                return;
            }

            // We don't want to get huge SLDs with default values. So if displacement = 0 and 0 we
            // drop it.
            Expression dx = dis.getDisplacementX();
            Expression dy = dis.getDisplacementY();
            if (isNull(dx) && isNull(dy)) {
                return;
            }
            if (isDefault(dx, 0) && isDefault(dy, 0)) {
                return;
            }

            start("Displacement");
            element("DisplacementX", dis.getDisplacementX());
            element("DisplacementY", dis.getDisplacementY());
            end("Displacement");
        }

        void encodeGeometryProperty(String name) {
            if ((name == null) || (name.trim().length() == 0)) {
                return;
            }
            // create a property name out the name and encode it
            FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
            Expression expression = ff.property(name);

            start("Geometry");
            filterTranslator.encode(expression);
            end("Geometry");

        }

        void encodeGeometryExpression(Expression geom) {
            if ((geom == null)) {
                return;
            }

            start("Geometry");
            filterTranslator.encode(geom);
            end("Geometry");

        }

        void encodeCssParam(String name, Expression expression) {
            encodeCssParam(name, expression, null);
        }

        void encodeCssParam(String name, Expression expression, Object defaultValue) {
            if (expression == null) {
                return; // protect ourselves from things like a null Stroke Color
            }

            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute("", "name", "name", "", name);
            encodeValue("CssParameter", atts, expression, defaultValue);
        }

        void encodeValue(String elementName, Attributes atts, Expression expression,
                Object defaultValue) {
            if (expression == null) {
                return; // protect ourselves from things like a null Stroke Color
            }
            
            if (!isExportDefaultValues()) {
                // skip encoding if we are using the default value
                if (expression instanceof Literal && defaultValue != null) {
                    Object value = expression.evaluate(null, defaultValue.getClass());
                    if (value != null && value.equals(defaultValue)) {
                        return;
                    }
                }
            }
            if (atts == null) {
                atts = NULL_ATTS;
            }
            if (expression instanceof Literal) {
                // use more compact encoding
                element(elementName, expression.evaluate(null, String.class), atts);
            } else {
                start(elementName, atts);
                filterTranslator.encode(expression);
                end(elementName);
            }
        }

        void encodeVendorOptions(Map options) {
            if (options != null) {
                Iterator it = options.keySet().iterator();
                while (it.hasNext()) {
                    String key = (String) it.next();
                    String value = (String) options.get(key);
                    encodeVendorOption(key, value);
                }
            }
        }

        void encodeVendorOption(String key, String value) {
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute("", "name", "name", "", key);
            start("VendorOption", atts);
            chars(value);
            end("VendorOption");
        }

        public void encode(Style[] styles) {
            try {
                contentHandler.startDocument();

                start("StyledLayerDescriptor", NULL_ATTS);
                start("NamedLayer", NULL_ATTS); // this is correct?

                for (int i = 0, ii = styles.length; i < ii; i++) {
                    styles[i].accept(this);
                }

                end("NamedLayer");
                end("StyledLayerDescriptor");

                contentHandler.endDocument();
            } catch (SAXException se) {
                throw new RuntimeException(se);
            }
        }

        public void encode(StyledLayerDescriptor sld) {
            try {
                contentHandler.startDocument();
                sld.accept(this);
                contentHandler.endDocument();
            } catch (SAXException se) {
                throw new RuntimeException(se);
            }
        }

        public void encode(Object o) throws IllegalArgumentException {
            if (o instanceof StyledLayerDescriptor) {
                encode((StyledLayerDescriptor) o);
            } else if (o instanceof Style[]) {
                encode((Style[]) o);
            } else {
                Class c = o.getClass();

                try {
                    java.lang.reflect.Method m = c.getMethod("accept",
                            new Class[] { StyleVisitor.class });
                    m.invoke(o, new Object[] { this });
                } catch (NoSuchMethodException nsme) {
                    throw new IllegalArgumentException("Cannot encode " + o);
                } catch (Exception e) {
                    throw new RuntimeException("Internal transformation exception", e);
                }
            }
        }

        public void visit(ContrastEnhancement ce) {
            if (ce == null )
                return;

            start("ContrastEnhancement");
            // histogram
            ContrastMethod method = ce.getMethod();
            if (method != null && method != ContrastMethod.NONE) {
                String val = method.name();
                val = val.substring(0, 1).toUpperCase() + val.substring(1).toLowerCase();
                start(val);
                Map<String, Expression> opts = ce.getOptions();

                for (Entry<String, Expression> entry : opts.entrySet()) {
                    AttributesImpl atts = new AttributesImpl();
                    atts.addAttribute("", "name", "name", "", entry.getKey());
                    element("VendorOption", 
                            entry.getValue().evaluate(null).toString(), atts);
                }

                end(val);
            }

            // gamma
            Expression exp = ce.getGammaValue();
            if (exp != null) {
                // gamma is a double so the actual value needs to be printed here
                element("GammaValue", ((Literal) exp).getValue().toString());
                // element("GammaValue", exp);
            }
            end("ContrastEnhancement");

        }

        public void visit(ImageOutline outline) {
            if (outline == null)
                return;
            start("ImageOutline");
            outline.getSymbolizer().accept(this);
            end("ImageOutline");
        }

        public void visit(ChannelSelection cs) {
            if (cs == null)
                return;
            start("ChannelSelection");
            final SelectedChannelType[] sct = cs.getSelectedChannels();
            for (int i = 0; i < sct.length && sct != null; i++)
                visit(sct[i]);
            end("ChannelSelection");

        }

        public void visit(OverlapBehavior ob) {
            start("OverlapBehavior");
            final String pn = (String) ob.getValue();
            start(pn);
            end(pn);
            end("OverlapBehavior");

        }

        public void visit(SelectedChannelType sct) {
            element("SourceChannelName", sct.getChannelName());
            final ContrastEnhancement ce = sct.getContrastEnhancement();
            if (ce != null)
                ce.accept(this);

        }

        public void visit(ShadedRelief sr) {
            start("ShadedRelief");
            // brightnessonly
            if (sr.isBrightnessOnly())
                element("BrightnessOnly", "true");
            else
                element("BrightnessOnly", "false");

            // relief factor
            if (sr.getReliefFactor() != null) {
                // element("ReliefFactor",sr.getReliefFactor());
                // this expression needs to be converted to a single string and then written
                // 1.0.0 specs don't allow it to be written as an expression
                Literal l = (Literal) sr.getReliefFactor();
                element("ReliefFactor", l.getValue().toString());
            }
            end("ShadedRelief");

        }

      
       
        
    }
}
