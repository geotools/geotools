/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2016, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.lang3.StringUtils;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.style.AnchorPoint;
import org.geotools.api.style.ChannelSelection;
import org.geotools.api.style.ColorMap;
import org.geotools.api.style.ColorMapEntry;
import org.geotools.api.style.ContrastEnhancement;
import org.geotools.api.style.ContrastMethod;
import org.geotools.api.style.ContrastMethodStrategy;
import org.geotools.api.style.Displacement;
import org.geotools.api.style.ExternalGraphic;
import org.geotools.api.style.FeatureTypeConstraint;
import org.geotools.api.style.FeatureTypeStyle;
import org.geotools.api.style.Fill;
import org.geotools.api.style.Font;
import org.geotools.api.style.Graphic;
import org.geotools.api.style.GraphicLegend;
import org.geotools.api.style.Halo;
import org.geotools.api.style.LabelPlacement;
import org.geotools.api.style.LinePlacement;
import org.geotools.api.style.LineSymbolizer;
import org.geotools.api.style.Mark;
import org.geotools.api.style.NamedLayer;
import org.geotools.api.style.NamedStyle;
import org.geotools.api.style.OtherText;
import org.geotools.api.style.PointPlacement;
import org.geotools.api.style.PointSymbolizer;
import org.geotools.api.style.PolygonSymbolizer;
import org.geotools.api.style.RasterSymbolizer;
import org.geotools.api.style.RemoteOWS;
import org.geotools.api.style.ResourceLocator;
import org.geotools.api.style.Rule;
import org.geotools.api.style.SelectedChannelType;
import org.geotools.api.style.SemanticType;
import org.geotools.api.style.ShadedRelief;
import org.geotools.api.style.Stroke;
import org.geotools.api.style.Style;
import org.geotools.api.style.StyleFactory;
import org.geotools.api.style.StyledLayer;
import org.geotools.api.style.StyledLayerDescriptor;
import org.geotools.api.style.Symbolizer;
import org.geotools.api.style.TextSymbolizer;
import org.geotools.api.style.UserLayer;
import org.geotools.api.util.InternationalString;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.NameImpl;
import org.geotools.filter.ExpressionDOMParser;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.styling.ContrastEnhancementImpl;
import org.geotools.styling.DefaultResourceLocator;
import org.geotools.styling.ExponentialContrastMethodStrategy;
import org.geotools.styling.FeatureTypeConstraintImpl;
import org.geotools.styling.HistogramContrastMethodStrategy;
import org.geotools.styling.LogarithmicContrastMethodStrategy;
import org.geotools.styling.NamedLayerImpl;
import org.geotools.styling.NormalizeContrastMethodStrategy;
import org.geotools.styling.OtherTextImpl;
import org.geotools.styling.RemoteOWSImpl;
import org.geotools.styling.SelectedChannelTypeImpl;
import org.geotools.styling.ShadedReliefImpl;
import org.geotools.styling.UomOgcMapping;
import org.geotools.styling.UserLayerImpl;
import org.geotools.util.Base64;
import org.geotools.util.GrowableInternationalString;
import org.geotools.util.SimpleInternationalString;
import org.geotools.util.factory.GeoTools;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * TODO: This really needs to be container ready
 *
 * @author jgarnett
 */
public class SLDParser {

    private static final FilterFactory FF = CommonFactoryFinder.getFilterFactory();

    /** HISTOGRAM */
    private static final String HISTOGRAM = "histogram";

    /** EXPONENTIAL */
    private static final String EXPONENTIAL = "exponential";

    /** LOGARITHMIC */
    private static final String LOGARITHMIC = "logarithmic";

    /** NORMALIZE */
    private static final String NORMALIZE = "normalize";
    /** a list of available Contrast Methods */
    static final List<String> CONTRAST_METHODS = Arrays.asList(NORMALIZE, LOGARITHMIC, EXPONENTIAL, HISTOGRAM);

    private static final java.util.logging.Logger LOGGER = org.geotools.util.logging.Logging.getLogger(SLDParser.class);

    private static final String channelSelectionString = "ChannelSelection";

    private static final String graphicSt = "Graphic"; // to make pmd to shut up

    private static final String geomString = "Geometry"; // to make pmd to shut up

    private static final String fillSt = "Fill";

    private static final String opacityString = "Opacity";

    private static final String overlapBehaviorString = "OverlapBehavior";

    private static final String colorMapString = "ColorMap";

    private static final String colorMapOpacityString = "opacity";

    private static final String colorMapColorString = "color";

    private static final String contrastEnhancementString = "ContrastEnhancement";

    private static final String shadedReliefString = "ShadedRelief";

    private static final String imageOutlineString = "ImageOutline";

    private static final String colorMapQuantityString = "quantity";

    private static final String colorMapLabelString = "label";

    private static final String strokeString = "Stroke";

    private static final String uomString = "uom";

    private static final String VendorOptionString = "VendorOption";

    private static final String PerpendicularOffsetString = "PerpendicularOffset";

    private static final Pattern WHITESPACES = Pattern.compile("\\s+", Pattern.MULTILINE);

    private static final Pattern LEADING_WHITESPACES = Pattern.compile("^\\s+");

    private static final Pattern TRAILING_WHITESPACES = Pattern.compile("\\s+$");

    private final FilterFactory ff;

    protected InputSource source;

    private org.w3c.dom.Document dom;

    protected StyleFactory factory;

    /** provides complete control for detecting relative onlineresources */
    private ResourceLocator onlineResourceLocator;

    private EntityResolver entityResolver;

    private boolean disposeInputSource;

    private final ExpressionDOMParser expressionDOMParser = new ExpressionDOMParser(FF);

    /**
     * Create a Stylereader - use if you already have a dom to parse.
     *
     * @param factory The StyleFactory to use to build the style
     */
    public SLDParser(StyleFactory factory) {
        this(factory, CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints()));
    }

    public SLDParser(StyleFactory factory, FilterFactory filterFactory) {
        this.factory = factory;
        this.ff = filterFactory;
        this.onlineResourceLocator = new DefaultResourceLocator();
    }

    /**
     * Creates a new instance of SLDStyler
     *
     * @param factory The StyleFactory to use to read the file
     * @param filename The file to be read.
     * @throws java.io.FileNotFoundException - if the file is missing
     */
    public SLDParser(StyleFactory factory, String filename) throws java.io.FileNotFoundException {
        this(factory);

        File f = new File(filename);
        setInput(f);
    }

    /**
     * Creates a new SLDStyle object.
     *
     * @param factory The StyleFactory to use to read the file
     * @param f the File to be read
     * @throws java.io.FileNotFoundException - if the file is missing
     */
    public SLDParser(StyleFactory factory, File f) throws java.io.FileNotFoundException {
        this(factory);
        setInput(f);
    }

    /**
     * Creates a new SLDStyle object.
     *
     * @param factory The StyleFactory to use to read the file
     * @param url the URL to be read.
     * @throws java.io.IOException - if something goes wrong reading the file
     */
    public SLDParser(StyleFactory factory, java.net.URL url) throws java.io.IOException {
        this(factory);
        setInput(url);
    }

    /**
     * Creates a new SLDStyle object.
     *
     * @param factory The StyleFactory to use to read the file
     * @param s The inputstream to be read
     */
    public SLDParser(StyleFactory factory, java.io.InputStream s) {
        this(factory);
        setInput(s);
    }

    /**
     * Creates a new SLDStyle object.
     *
     * @param factory The StyleFactory to use to read the file
     * @param r The inputstream to be read
     */
    public SLDParser(StyleFactory factory, java.io.Reader r) {
        this(factory);
        setInput(r);
    }

    /**
     * set the file to read the SLD from
     *
     * @param filename the file to read the SLD from
     * @throws java.io.FileNotFoundException if the file is missing
     */
    public void setInput(String filename) throws java.io.FileNotFoundException {
        File f = new File(filename);
        source = new InputSource(new java.io.FileInputStream(f));
        this.disposeInputSource = true;
        try {
            setSourceUrl(f.toURI().toURL());
        } catch (MalformedURLException e) {
            LOGGER.warning("Can't build URL for file " + f.getAbsolutePath());
        }
    }

    /**
     * Sets the file to use to read the SLD from
     *
     * @param f the file to use
     * @throws java.io.FileNotFoundException if the file is missing
     */
    public void setInput(File f) throws java.io.FileNotFoundException {
        source = new InputSource(new java.io.FileInputStream(f));
        this.disposeInputSource = true;
        try {
            setSourceUrl(f.toURI().toURL());
        } catch (MalformedURLException e) {
            LOGGER.warning("Can't build URL for file " + f.getAbsolutePath());
        }
    }

    /**
     * sets an URL to read the SLD from
     *
     * @param url the url to read the SLD from
     * @throws java.io.IOException If anything goes wrong opening the url
     */
    public void setInput(java.net.URL url) throws java.io.IOException {
        source = new InputSource(url.openStream());
        this.disposeInputSource = true;
        setSourceUrl(url);
    }

    /**
     * Sets the input stream to read the SLD from
     *
     * @param in the inputstream used to read the SLD from
     */
    public void setInput(java.io.InputStream in) {
        source = new InputSource(in);
    }

    /**
     * Sets the input stream to read the SLD from
     *
     * @param in the inputstream used to read the SLD from
     */
    public void setInput(java.io.Reader in) {
        source = new InputSource(in);
    }

    /** Sets the resource loader implementation for parsing online resources. */
    public void setOnLineResourceLocator(ResourceLocator onlineResourceLocator) {
        this.onlineResourceLocator = onlineResourceLocator;
    }

    /** Sets the EntityResolver implementation that will be used by DocumentBuilder to resolve XML external entities. */
    public void setEntityResolver(EntityResolver entityResolver) {
        this.entityResolver = entityResolver;
    }

    /** Internal setter for source url. */
    void setSourceUrl(URL sourceUrl) {
        if (onlineResourceLocator instanceof DefaultResourceLocator) {
            ((DefaultResourceLocator) onlineResourceLocator).setSourceUrl(sourceUrl);
        }
    }

    protected javax.xml.parsers.DocumentBuilder newDocumentBuilder(boolean namespaceAware)
            throws ParserConfigurationException {
        javax.xml.parsers.DocumentBuilderFactory dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(namespaceAware);
        javax.xml.parsers.DocumentBuilder db = dbf.newDocumentBuilder();

        if (entityResolver != null) {
            db.setEntityResolver(entityResolver);
        }

        return db;
    }

    /**
     * Read the xml inputsource provided and create a Style object for each user style found
     *
     * @return Style[] the styles constructed.
     * @throws RuntimeException if a parsing error occurs
     */
    public Style[] readXML() {
        try {
            dom = newDocumentBuilder(true).parse(source);
        } catch (ParserConfigurationException | IOException | SAXException pce) {
            throw new RuntimeException(pce);
        } finally {
            disposeInputSource();
        }

        return readDOM(dom);
    }

    /** Close the input source stream/reader if they had been created in this class */
    private void disposeInputSource() {
        if (!disposeInputSource) {
            return;
        }

        try {
            if (source.getByteStream() != null) {
                source.getByteStream().close();
            }

            if (source.getCharacterStream() != null) {
                source.getCharacterStream().close();
            }
        } catch (Exception e) {
            LOGGER.log(Level.FINE, "Trouble releasing input source streams", e);
        }
    }

    /** Read styles from the dom that was previously parsed. */
    public Style[] readDOM() {
        if (dom == null) {
            throw new NullPointerException("dom is null");
        }
        return readDOM(dom);
    }

    /**
     * Read the DOM provided and create a Style object for each user style found
     *
     * @param document a dom containing the SLD
     * @return Style[] the styles constructed.
     */
    public Style[] readDOM(org.w3c.dom.Document document) {
        this.dom = document;

        // for our next trick do something with the dom.
        NodeList nodes = findElements(document, "UserStyle");

        if (nodes == null) return new Style[0];

        final int length = nodes.getLength();
        Style[] styles = new Style[length];

        for (int i = 0; i < length; i++) {
            styles[i] = parseStyle(nodes.item(i));
        }

        return styles;
    }

    /** */
    private NodeList findElements(final org.w3c.dom.Document document, final String name) {
        NodeList nodes = document.getElementsByTagNameNS("*", name);

        if (nodes.getLength() == 0) {
            nodes = document.getElementsByTagName(name);
        }

        return nodes;
    }

    private NodeList findElements(final org.w3c.dom.Element element, final String name) {
        NodeList nodes = element.getElementsByTagNameNS("*", name);

        if (nodes.getLength() == 0) {
            nodes = element.getElementsByTagName(name);
        }

        return nodes;
    }

    public StyledLayerDescriptor parseSLD() {
        try {
            dom = newDocumentBuilder(true).parse(source);
            // for our next trick do something with the dom.

            // NodeList nodes = findElements(dom, "StyledLayerDescriptor");

            StyledLayerDescriptor sld = parseDescriptor(dom.getDocumentElement()); // should only be
            // one per file
            return sld;

        } catch (ParserConfigurationException | IOException | SAXException pce) {
            throw new RuntimeException(pce);
        } finally {
            disposeInputSource();
        }
    }

    public StyledLayerDescriptor parseDescriptor(Node root) {
        StyledLayerDescriptor sld = factory.createStyledLayerDescriptor();
        // StyledLayer layer = null;
        // LineSymbolizer symbol = factory.createLineSymbolizer();

        NodeList children = root.getChildNodes();
        final int length = children.getLength();

        for (int i = 0; i < length; i++) {
            Node child = children.item(i);
            if (child == null || child.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            String childName = child.getLocalName();
            if (childName == null) {
                childName = child.getNodeName();
            }

            if (childName.equalsIgnoreCase("Name")) {
                sld.setName(getFirstChildValue(child));
            } else if (childName.equalsIgnoreCase("Title")) {
                sld.setTitle(getFirstChildValue(child));
            } else if (childName.equalsIgnoreCase("Abstract")) {
                sld.setAbstract(getFirstChildValue(child));
            } else if (childName.equalsIgnoreCase("NamedLayer")) {
                NamedLayer layer = parseNamedLayer(child);
                sld.addStyledLayer(layer);
            } else if (childName.equalsIgnoreCase("UserLayer")) {
                StyledLayer layer = parseUserLayer(child);
                sld.addStyledLayer(layer);
            }
        }

        return sld;
    }

    /** Returns the first child node value, or null if there is no child */
    String getFirstChildValue(Node child) {
        if (child.getFirstChild() != null) return child.getFirstChild().getNodeValue();
        else return null;
    }

    private static String getAttribute(Node node, String attrName) {
        NamedNodeMap attributes = node.getAttributes();
        Node attribute = attributes.getNamedItem(attrName);
        return attribute == null ? null : attribute.getNodeValue();
    }

    private StyledLayer parseUserLayer(Node root) {
        UserLayer layer = new UserLayerImpl();
        // LineSymbolizer symbol = factory.createLineSymbolizer();

        NodeList children = root.getChildNodes();
        final int length = children.getLength();
        for (int i = 0; i < length; i++) {
            Node child = children.item(i);
            if (child == null || child.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            String childName = child.getLocalName();
            if (childName == null) {
                childName = child.getNodeName();
            }

            if (childName.equalsIgnoreCase("InlineFeature")) {
                parseInlineFeature(child, layer);
            } else if (childName.equalsIgnoreCase("UserStyle")) {
                Style user = parseStyle(child);
                layer.addUserStyle(user);
            } else if (childName.equalsIgnoreCase("Name")) {
                String layerName = getFirstChildValue(child);
                layer.setName(layerName);
                if (LOGGER.isLoggable(Level.INFO)) LOGGER.info("layer name: " + layer.getName());
            } else if (childName.equalsIgnoreCase("RemoteOWS")) {
                RemoteOWS remoteOws = parseRemoteOWS(child);
                layer.setRemoteOWS(remoteOws);
            } else if (childName.equalsIgnoreCase("LayerFeatureConstraints")) {
                layer.setLayerFeatureConstraints(parseLayerFeatureConstraints(child));
            }
        }

        return layer;
    }

    private FeatureTypeConstraint[] parseLayerFeatureConstraints(Node root) {
        List<FeatureTypeConstraint> featureTypeConstraints = new ArrayList<>();

        NodeList children = root.getChildNodes();
        final int length = children.getLength();
        for (int i = 0; i < length; i++) {
            Node child = children.item(i);
            if (child == null || child.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            String childName = child.getLocalName();
            if (childName.equalsIgnoreCase("FeatureTypeConstraint")) {
                final FeatureTypeConstraint ftc = parseFeatureTypeConstraint(child);
                if (ftc != null) featureTypeConstraints.add(ftc);
            }
        }
        return featureTypeConstraints.toArray(new FeatureTypeConstraint[0]);
    }

    protected FeatureTypeConstraint parseFeatureTypeConstraint(Node root) {
        FeatureTypeConstraint ftc = new FeatureTypeConstraintImpl();

        NodeList children = root.getChildNodes();
        final int length = children.getLength();
        for (int i = 0; i < length; i++) {
            Node child = children.item(i);
            if (child == null || child.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            String childName = child.getLocalName();
            if (childName.equalsIgnoreCase("FeatureTypeName")) {
                ftc.setFeatureTypeName(getFirstChildValue(child));
            } else if (childName.equalsIgnoreCase("Filter")) {
                ftc.setFilter(parseFilter(child));
            }
        }
        ftc.setExtents();
        if (ftc.getFeatureTypeName() == null) return null;
        else return ftc;
    }

    private static Icon parseIcon(String content) throws IOException {
        byte[] bytes = Base64.decode(content);
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));
        if (image == null) {
            throw new IOException("invalid image content");
        }
        return new ImageIcon(image);
    }

    protected RemoteOWS parseRemoteOWS(Node root) {
        RemoteOWS ows = new RemoteOWSImpl();

        NodeList children = root.getChildNodes();
        final int length = children.getLength();
        for (int i = 0; i < length; i++) {
            Node child = children.item(i);
            if (child == null || child.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            String childName = child.getLocalName();

            if (childName.equalsIgnoreCase("Service")) {
                ows.setService(getFirstChildValue(child));
            } else if (childName.equalsIgnoreCase("OnlineResource")) {
                ows.setOnlineResource(parseOnlineResource(child));
            }
        }
        return ows;
    }

    /** */
    private void parseInlineFeature(Node root, UserLayer layer) {
        try {
            SLDInlineFeatureParser inparser = new SLDInlineFeatureParser(root);
            layer.setInlineFeatureDatastore(inparser.dataStore);
            layer.setInlineFeatureType(inparser.featureType);
        } catch (Exception e) {
            throw (IllegalArgumentException) new IllegalArgumentException().initCause(e);
        }
    }

    /**
     * Parses a NamedLayer.
     *
     * <p>The NamedLayer schema is:
     *
     * <pre>
     * &lt;code&gt;
     * &lt;xsd:element name=&quot;NamedLayer&quot;&gt;
     *  &lt;xsd:annotation&gt;
     *   &lt;xsd:documentation&gt; A NamedLayer is a layer of data that has a name advertised by a WMS. &lt;/xsd:documentation&gt;
     *  &lt;/xsd:annotation&gt;
     *  &lt;xsd:complexType&gt;
     *   &lt;xsd:sequence&gt;
     *    &lt;xsd:element ref=&quot;sld:Name&quot;/&gt;
     *    &lt;xsd:element ref=&quot;sld:LayerFeatureConstraints&quot; minOccurs=&quot;0&quot;/&gt;
     *    &lt;xsd:choice minOccurs=&quot;0&quot; maxOccurs=&quot;unbounded&quot;&gt;
     *     &lt;xsd:element ref=&quot;sld:NamedStyle&quot;/&gt;
     *     &lt;xsd:element ref=&quot;sld:UserStyle&quot;/&gt;
     *    &lt;/xsd:choice&gt;
     *   &lt;/xsd:sequence&gt;
     *  &lt;/xsd:complexType&gt;
     * &lt;/xsd:element&gt;
     * &lt;/code&gt;
     * </pre>
     */
    private NamedLayer parseNamedLayer(Node root) {
        NamedLayer layer = new NamedLayerImpl();

        NodeList children = root.getChildNodes();
        final int length = children.getLength();
        for (int i = 0; i < length; i++) {
            Node child = children.item(i);
            if (child == null || child.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            String childName = child.getLocalName();
            if (childName == null) {
                childName = child.getNodeName();
            }

            if (childName.equalsIgnoreCase("Name")) {
                layer.setName(getFirstChildValue(child));
            } else if (childName.equalsIgnoreCase("NamedStyle")) {
                NamedStyle style = parseNamedStyle(child);
                layer.addStyle(style);
            } else if (childName.equalsIgnoreCase("UserStyle")) {
                Style user = parseStyle(child);
                layer.addStyle(user);
            } else if (childName.equalsIgnoreCase("LayerFeatureConstraints")) {
                layer.setLayerFeatureConstraints(parseLayerFeatureConstraints(child));
            }
        }

        return layer;
    }

    /**
     * Parses a NamedStyle from node.
     *
     * <p>A NamedStyle is used to refer to a style that has a name in a WMS, and is defined as:
     *
     * <pre>
     * &lt;code&gt;
     * &lt;xsd:element name=&quot;NamedStyle&quot;&gt;
     *  &lt;xsd:annotation&gt;
     *   &lt;xsd:documentation&gt; A NamedStyle is used to refer to a style that has a name in a WMS. &lt;/xsd:documentation&gt;
     *  &lt;/xsd:annotation&gt;
     *  &lt;xsd:complexType&gt;
     *   &lt;xsd:sequence&gt;
     *    &lt;xsd:element ref=&quot;sld:Name&quot;/&gt;
     *   &lt;/xsd:sequence&gt;
     *  &lt;/xsd:complexType&gt;
     * &lt;/xsd:element&gt;
     * &lt;/code&gt;
     * </pre>
     */
    public NamedStyle parseNamedStyle(Node n) {
        if (dom == null) {
            try {
                dom = newDocumentBuilder(false).newDocument();
            } catch (javax.xml.parsers.ParserConfigurationException pce) {
                throw new RuntimeException(pce);
            }
        }

        NamedStyle style = factory.createNamedStyle();

        NodeList children = n.getChildNodes();
        final int length = children.getLength();
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest("" + children.getLength() + " children to process");
        }

        for (int j = 0; j < length; j++) {
            Node child = children.item(j);

            if (child == null || child.getNodeType() != Node.ELEMENT_NODE || child.getFirstChild() == null) {
                continue;
            }
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.finest("processing " + child.getLocalName());
            }
            String childName = child.getLocalName();
            if (childName == null) {
                childName = child.getNodeName();
            }
            if (childName.equalsIgnoreCase("Name")) {
                style.setName(getFirstChildValue(child));
            }
        }
        return style;
    }

    /**
     * build a style for the Node provided
     *
     * @param n the node which contains the style to be parsed.
     * @return the Style constructed.
     * @throws RuntimeException if an error occurs setting up the parser
     */
    public Style parseStyle(Node n) {
        if (dom == null) {
            try {
                dom = newDocumentBuilder(false).newDocument();
            } catch (javax.xml.parsers.ParserConfigurationException pce) {
                throw new RuntimeException(pce);
            }
        }

        Style style = factory.createStyle();

        NodeList children = n.getChildNodes();
        final int length = children.getLength();
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest("" + children.getLength() + " children to process");
        }

        for (int j = 0; j < length; j++) {
            Node child = children.item(j);

            if (child == null || child.getNodeType() != Node.ELEMENT_NODE || child.getFirstChild() == null) {
                continue;
            }
            // System.out.println("The child is: " + child.getNodeName() + " or
            // " + child.getLocalName() + " prefix is " +child.getPrefix());
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.finest("processing " + child.getLocalName());
            }
            String childName = child.getLocalName();
            if (childName == null) {
                childName = child.getNodeName();
            }
            String firstChildValue = getFirstChildValue(child);
            if (childName.equalsIgnoreCase("Name")) {
                style.setName(firstChildValue);
            } else if (childName.equalsIgnoreCase("Title")) {

                style.getDescription().setTitle(parseInternationalString(child));
            } else if (childName.equalsIgnoreCase("Abstract")) {
                style.getDescription().setAbstract(parseInternationalString(child));
            } else if (childName.equalsIgnoreCase("IsDefault")) {
                if ("1".equals(firstChildValue)) {
                    style.setDefault(true);
                } else {
                    style.setDefault(Boolean.parseBoolean(firstChildValue));
                }
            } else if (childName.equalsIgnoreCase("FeatureTypeStyle")) {
                style.featureTypeStyles().add(parseFeatureTypeStyle(child));
            } else if (childName.equalsIgnoreCase("Background")) {
                style.setBackground(parseFill(child));
            }
        }

        return style;
    }

    /** Internal parse method - made protected for unit testing */
    protected FeatureTypeStyle parseFeatureTypeStyle(Node style) {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest("Parsing featuretype style " + style.getLocalName());
        }

        FeatureTypeStyle ft = factory.createFeatureTypeStyle();
        ArrayList<Rule> rules = new ArrayList<>();
        ArrayList<String> sti = new ArrayList<>();
        NodeList children = style.getChildNodes();
        final int length = children.getLength();
        for (int i = 0; i < length; i++) {
            Node child = children.item(i);

            if (child == null || child.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.finest("processing " + child.getLocalName());
            }
            String childName = child.getLocalName();
            if (childName == null) {
                childName = child.getNodeName();
            }

            if (childName.equalsIgnoreCase("Name")) {
                ft.setName(getFirstChildValue(child));
            } else if (childName.equalsIgnoreCase("Title")) {
                ft.getDescription().setTitle(parseInternationalString(child));
            } else if (childName.equalsIgnoreCase("Abstract")) {
                ft.getDescription().setAbstract(parseInternationalString(child));
            } else if (childName.equalsIgnoreCase("FeatureTypeName")) {
                ft.featureTypeNames().add(new NameImpl(getFirstChildValue(child)));
            } else if (childName.equalsIgnoreCase("SemanticTypeIdentifier")) {
                String value = getFirstChildValue(child);
                if (value != null) {
                    sti.add(value);
                }
            } else if (childName.equalsIgnoreCase("Rule")) {
                rules.add(parseRule(child));
            } else if (childName.equalsIgnoreCase("Transformation")) {
                ExpressionDOMParser parser = new ExpressionDOMParser(CommonFactoryFinder.getFilterFactory(null));
                Expression tx = parser.expression(getFirstNonTextChild(child));
                ft.setTransformation(tx);
            } else if (childName.equalsIgnoreCase(VendorOptionString)) {
                parseVendorOption(ft.getOptions(), child);
            }
        }

        if (!sti.isEmpty()) {
            ft.semanticTypeIdentifiers().clear();
            sti.forEach(s -> ft.semanticTypeIdentifiers().add(SemanticType.valueOf(s)));
        }
        ft.rules().clear();
        ft.rules().addAll(rules);

        return ft;
    }

    private Node getFirstNonTextChild(Node node) {
        NodeList children = node.getChildNodes();
        final int length = children.getLength();
        for (int i = 0; i < length; i++) {
            Node child = children.item(i);

            if (child == null || child.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            return child;
        }

        return null;
    }

    /** Internal parse method - made protected for unit testing */
    protected Rule parseRule(Node ruleNode) {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest("Parsing rule " + ruleNode.getLocalName());
        }

        Rule rule = factory.createRule();
        List<Symbolizer> symbolizers = new ArrayList<>();
        NodeList children = ruleNode.getChildNodes();
        final int length = children.getLength();
        for (int i = 0; i < length; i++) {
            Node child = children.item(i);

            if (child == null || child.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            String childName = child.getLocalName();
            if (childName == null) {
                childName = child.getNodeName();
            }

            if (childName.indexOf(':') != -1) {
                // the DOM parser wasnt properly set to handle namespaces...
                childName = childName.substring(childName.indexOf(':') + 1);
            }

            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.finest("processing " + child.getLocalName());
            }

            if (childName.equalsIgnoreCase("Name")) {
                rule.setName(getFirstChildValue(child));
            } else if (childName.equalsIgnoreCase("Title")) {
                rule.getDescription().setTitle(parseInternationalString(child));
            } else if (childName.equalsIgnoreCase("Abstract")) {
                rule.getDescription().setAbstract(parseInternationalString(child));
            } else if (childName.equalsIgnoreCase("MinScaleDenominator")) {
                rule.setMinScaleDenominator(Double.parseDouble(getFirstChildValue(child)));
            } else if (childName.equalsIgnoreCase("MaxScaleDenominator")) {
                rule.setMaxScaleDenominator(Double.parseDouble(getFirstChildValue(child)));
            } else if (childName.equalsIgnoreCase("Filter")) {
                Filter filter = parseFilter(child);
                rule.setFilter(filter);
            } else if (childName.equalsIgnoreCase("ElseFilter")) {
                rule.setElseFilter(true);
            } else if (childName.equalsIgnoreCase("LegendGraphic")) {
                findElements((Element) child, graphicSt);
                NodeList g = findElements((Element) child, graphicSt);
                final int l = g.getLength();
                for (int k = 0; k < l; k++) {
                    Graphic graphic = parseGraphic(g.item(k));
                    if (graphic != null) {
                        rule.setLegend((GraphicLegend) graphic);
                        break;
                    }
                }
            } else if (childName.equalsIgnoreCase("LineSymbolizer")) {
                symbolizers.add(parseLineSymbolizer(child));
            } else if (childName.equalsIgnoreCase("PolygonSymbolizer")) {
                symbolizers.add(parsePolygonSymbolizer(child));
            } else if (childName.equalsIgnoreCase("PointSymbolizer")) {
                symbolizers.add(parsePointSymbolizer(child));
            } else if (childName.equalsIgnoreCase("TextSymbolizer")) {
                symbolizers.add(parseTextSymbolizer(child));
            } else if (childName.equalsIgnoreCase("RasterSymbolizer")) {
                symbolizers.add(parseRasterSymbolizer(child));
            } else if (childName.equalsIgnoreCase(VendorOptionString)) {
                parseVendorOption(rule.getOptions(), child);
            }
        }

        rule.symbolizers().clear();
        rule.symbolizers().addAll(symbolizers);

        return rule;
    }

    /**
     * Parse a node with mixed content containing internationalized elements in the form: <Localized
     * lang="locale">text</Localized>
     */
    private InternationalString parseInternationalString(Node root) {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest("parsingInternationalString " + root);
        }

        NodeList children = root.getChildNodes();
        final int length = children.getLength();
        StringBuilder text = new StringBuilder();

        Map<String, String> translations = new HashMap<>();

        for (int i = 0; i < length; i++) {
            Node child = children.item(i);

            if (child == null) {
                continue;
            } else if (child.getNodeType() == Node.TEXT_NODE || child.getNodeType() == Node.CDATA_SECTION_NODE) {
                // append text as is
                String value = child.getNodeValue();
                if (value == null) continue;
                text.append(value.trim());
            } else if (child.getNodeType() == Node.ELEMENT_NODE) {
                // parse value elements
                if (LOGGER.isLoggable(Level.FINEST)) {
                    LOGGER.finest("about to parse " + child.getLocalName());
                }
                Element element = (Element) child;
                if (element.getLocalName().equalsIgnoreCase("localized")) {
                    String lang = element.getAttribute("lang");
                    String translation = getFirstChildValue(element);

                    translations.put(lang, translation);
                }
            } else continue;
        }

        if (translations.isEmpty()) {
            String simpleText = getFirstChildValue(root);
            return new SimpleInternationalString(simpleText == null ? "" : simpleText);
        } else {
            GrowableInternationalString intString = new GrowableInternationalString(text.toString()) {

                @Override
                public String toString() {
                    return super.toString(null);
                }
            };
            for (String lang : translations.keySet()) {
                intString.add("", "_" + lang, translations.get(lang));
            }
            return intString;
        }
    }

    /** Internal parse method - made protected for unit testing */
    protected Filter parseFilter(Node child) {
        // this sounds stark raving mad, but this is actually how the dom parser
        // works...
        // instead of passing in the parent element, pass in the first child and
        // its
        // siblings will also be parsed
        Node firstChild = child.getFirstChild();
        while (firstChild != null && firstChild.getNodeType() != Node.ELEMENT_NODE) {
            // advance to the first actual element (rather than whitespace)
            firstChild = firstChild.getNextSibling();
        }
        Filter filter = org.geotools.filter.FilterDOMParser.parseFilter(firstChild);
        return filter;
    }

    /**
     * parses the SLD for a linesymbolizer
     *
     * @param root a w2c Dom Node
     * @return the linesymbolizer
     */
    protected LineSymbolizer parseLineSymbolizer(Node root) {
        LineSymbolizer symbol = factory.createLineSymbolizer();

        NamedNodeMap namedNodeMap = root.getAttributes();
        Node uomNode = namedNodeMap.getNamedItem(uomString);
        if (uomNode != null) {
            UomOgcMapping uomMapping = UomOgcMapping.get(uomNode.getNodeValue());
            symbol.setUnitOfMeasure(uomMapping.getUnit());
        }

        NodeList children = root.getChildNodes();
        final int length = children.getLength();
        for (int i = 0; i < length; i++) {
            Node child = children.item(i);

            if (child == null || child.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            String childName = child.getLocalName();
            if (childName == null) {
                childName = child.getNodeName();
            }
            if (childName.equalsIgnoreCase(geomString)) {
                symbol.setGeometry(parseGeometry(child));
            } else if (childName.equalsIgnoreCase(strokeString)) {
                symbol.setStroke(parseStroke(child));
            } else if (childName.equalsIgnoreCase(VendorOptionString)) {
                parseVendorOption(symbol.getOptions(), child);
            } else if (childName.equalsIgnoreCase(PerpendicularOffsetString)) {
                final Expression offsetValue = parseCssParameter(child);
                symbol.setPerpendicularOffset(offsetValue);
            }
        }

        return symbol;
    }

    /**
     * parses the SLD for a polygonsymbolizer
     *
     * @param root w3c dom node
     * @return the polygon symbolizer
     */
    protected PolygonSymbolizer parsePolygonSymbolizer(Node root) {
        PolygonSymbolizer symbol = factory.createPolygonSymbolizer();
        symbol.setFill(null);
        symbol.setStroke(null);

        NamedNodeMap namedNodeMap = root.getAttributes();
        Node uomNode = namedNodeMap.getNamedItem(uomString);
        if (uomNode != null) {
            UomOgcMapping uomMapping = UomOgcMapping.get(uomNode.getNodeValue());
            symbol.setUnitOfMeasure(uomMapping.getUnit());
        }

        NodeList children = root.getChildNodes();
        final int length = children.getLength();
        for (int i = 0; i < length; i++) {
            Node child = children.item(i);

            if (child == null || child.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            String childName = child.getLocalName();
            if (childName == null) {
                childName = child.getNodeName();
            }
            if (childName.equalsIgnoreCase(geomString)) {
                symbol.setGeometry(parseGeometry(child));
            } else if (childName.equalsIgnoreCase(strokeString)) {
                symbol.setStroke(parseStroke(child));
            } else if (childName.equalsIgnoreCase(fillSt)) {
                symbol.setFill(parseFill(child));
            } else if (childName.equalsIgnoreCase(VendorOptionString)) {
                parseVendorOption(symbol.getOptions(), child);
            }
        }

        return symbol;
    }

    /**
     * parses the SLD for a text symbolizer
     *
     * @param root w3c dom node
     * @return the TextSymbolizer
     */
    protected TextSymbolizer parseTextSymbolizer(Node root) {
        TextSymbolizer symbol = factory.createTextSymbolizer();
        symbol.setFill(null);

        NamedNodeMap namedNodeMap = root.getAttributes();
        Node uomNode = namedNodeMap.getNamedItem(uomString);
        if (uomNode != null) {
            UomOgcMapping uomMapping = UomOgcMapping.get(uomNode.getNodeValue());
            symbol.setUnitOfMeasure(uomMapping.getUnit());
        }

        List<Font> fonts = new ArrayList<>();
        NodeList children = root.getChildNodes();
        final int length = children.getLength();
        for (int i = 0; i < length; i++) {
            Node child = children.item(i);

            if (child == null || child.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            String childName = child.getLocalName();
            if (childName == null) {
                childName = child.getNodeName();
            }
            if (childName.equalsIgnoreCase(geomString)) {
                symbol.setGeometry(parseGeometry(child));
            } else if (childName.equalsIgnoreCase(fillSt)) {
                symbol.setFill(parseFill(child));
            } else if (childName.equalsIgnoreCase("Label")) {
                if (LOGGER.isLoggable(Level.FINEST)) LOGGER.finest("parsing label " + child.getNodeValue());
                // the label parser should collapse whitespaces to one, so
                // we call parseCssParameter with trimWhiteSpace=false
                symbol.setLabel(parseCssParameter(child, false));
                if (symbol.getLabel() == null) {
                    if (LOGGER.isLoggable(Level.WARNING))
                        LOGGER.warning("parsing TextSymbolizer node - couldnt find anything in the Label element!");
                }
            }

            if (childName.equalsIgnoreCase("Font")) {
                fonts.add(parseFont(child));
            } else if (childName.equalsIgnoreCase("LabelPlacement")) {
                symbol.setLabelPlacement(parseLabelPlacement(child));
            } else if (childName.equalsIgnoreCase("Halo")) {
                symbol.setHalo(parseHalo(child));
            } else if (childName.equalsIgnoreCase("Graphic")) {
                if (LOGGER.isLoggable(Level.FINEST)) LOGGER.finest("Parsing non-standard Graphic in TextSymbolizer");

                symbol.setGraphic(parseGraphic(child));

            } else if (childName.equalsIgnoreCase("Snippet")) {
                if (LOGGER.isLoggable(Level.FINEST)) LOGGER.finest("Parsing non-standard Abstract in TextSymbolizer");

                symbol.setSnippet(parseCssParameter(child, false));
            } else if (childName.equalsIgnoreCase("FeatureDescription")) {
                if (LOGGER.isLoggable(Level.FINEST))
                    LOGGER.finest("Parsing non-standard Description in TextSymbolizer");

                symbol.setFeatureDescription(parseCssParameter(child, false));
            } else if (childName.equalsIgnoreCase("OtherText")) {
                if (LOGGER.isLoggable(Level.FINEST)) LOGGER.finest("Parsing non-standard OtherText in TextSymbolizer");

                symbol.setOtherText(parseOtherText(child));
            } else if (childName.equalsIgnoreCase("priority")) {
                symbol.setPriority(parseCssParameter(child));
            } else if (childName.equalsIgnoreCase(VendorOptionString)) {
                parseVendorOption(symbol.getOptions(), child);
            }
        }
        symbol.fonts().addAll(fonts);

        return symbol;
    }

    protected OtherText parseOtherText(Node root) {
        // TODO: add methods to the factory to create OtherText instances
        OtherText ot = new OtherTextImpl();
        final Node targetAttribute = root.getAttributes().getNamedItem("target");
        if (targetAttribute == null)
            throw new IllegalArgumentException("OtherLocation does not have the " + "required 'target' attribute");
        String target = targetAttribute.getNodeValue();
        Expression text = parseCssParameter(root, true);
        ot.setTarget(target);
        ot.setText(text);
        return ot;
    }

    /** adds the key/value pair from the node ("<VendorOption name="...">...</VendorOption>") */
    private void parseVendorOption(Map<String, String> options, Node child) {
        String key = child.getAttributes().getNamedItem("name").getNodeValue();
        String value = getFirstChildValue(child);

        if (StringUtils.isNotBlank(value)) {
            options.put(key, value);
        }
    }

    /**
     * parses the SLD for a text symbolizer
     *
     * @param root w3c dom node
     * @return the TextSymbolizer
     */
    protected RasterSymbolizer parseRasterSymbolizer(Node root) {
        final RasterSymbolizer symbol = factory.getDefaultRasterSymbolizer();

        NamedNodeMap namedNodeMap = root.getAttributes();
        Node uomNode = namedNodeMap.getNamedItem(uomString);
        if (uomNode != null) {
            UomOgcMapping uomMapping = UomOgcMapping.get(uomNode.getNodeValue());
            symbol.setUnitOfMeasure(uomMapping.getUnit());
        }

        NodeList children = root.getChildNodes();
        final int length = children.getLength();
        for (int i = 0; i < length; i++) {
            Node child = children.item(i);
            if (child == null || child.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            String childName = child.getLocalName();
            if (childName == null) {
                childName = child.getNodeName();
            }
            if (childName.equalsIgnoreCase(geomString)) {
                symbol.setGeometry(parseGeometry(child));
            }
            if (childName.equalsIgnoreCase(opacityString)) {
                try {
                    Expression opacity = parseParameterValueExpression(child, false);
                    symbol.setOpacity(opacity);
                } catch (Throwable e) {
                    if (LOGGER.isLoggable(Level.WARNING)) LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
                }
            } else if (childName.equalsIgnoreCase(channelSelectionString)) {
                symbol.setChannelSelection(parseChannelSelection(child));
            } else if (childName.equalsIgnoreCase(overlapBehaviorString)) {
                try {
                    final String overlapString = child.getFirstChild().getLocalName();
                    symbol.setOverlap(ff.literal(overlapString));
                } catch (Throwable e) {
                    if (LOGGER.isLoggable(Level.WARNING)) LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
                }
            } else if (childName.equalsIgnoreCase(colorMapString)) {
                symbol.setColorMap(parseColorMap(child));
            } else if (childName.equalsIgnoreCase(contrastEnhancementString)) {
                symbol.setContrastEnhancement(parseContrastEnhancement(child));
            } else if (childName.equalsIgnoreCase(shadedReliefString)) {
                symbol.setShadedRelief(parseShadedRelief(child));
            } else if (childName.equalsIgnoreCase(imageOutlineString)) {
                symbol.setImageOutline(parseLineSymbolizer(child));
            } else if (childName.equalsIgnoreCase(VendorOptionString)) {
                parseVendorOption(symbol.getOptions(), child);
            }
        }

        return symbol;
    }

    /**
     * Many elements in an SLD extends ParameterValueType (allowing for the definition of Expressions) - this method
     * will try and produce an expression for the provided node.
     *
     * <p>As an example:
     *
     * <ul>
     *   <li>"sld:Opacity" is defined as a parameter value type:<br>
     *       &lt;sld:Opacity&gt;0.75&lt;\sld:Opacity&gt;
     *   <li>"sld:Label" is defined as a "mixed" parameter value type:<br>
     *       &lt;sld:Label&gt;Hello &lt;sld:PropertyName&gt;name&lt;\sld:PropertyName&gt;&lt;\sld:Label&gt;
     * </ul>
     *
     * From the SLD 1.0 spec: "ParameterValueType" uses WFS-Filter expressions to give values for SLD graphic
     * parameters. A "mixed" element-content model is used with textual substitution for values.
     */
    Expression parseParameterValueExpression(Node root, boolean mixedText) {
        ExpressionDOMParser parser = new ExpressionDOMParser(ff);
        Expression expr = parser.expression(root); // try the provided node first
        if (expr != null) return expr;
        NodeList children = root.getChildNodes();
        // if there is only one CharacterData node - we can make a literal out of it
        if (children.getLength() == 1 && root.getFirstChild() instanceof CharacterData) {
            Node textNode = root.getFirstChild();
            String text = textNode.getNodeValue();
            return ff.literal(text.trim());
        }
        List<Expression> expressionList = new ArrayList<>();
        for (int index = 0; index < children.getLength(); index++) {
            Node child = children.item(index);
            if (child instanceof CharacterData) {
                if (mixedText) {
                    String text = child.getNodeValue();
                    Expression childExpr = ff.literal(text);
                    expressionList.add(childExpr);
                }
            } else {
                Expression childExpr = parser.expression(child);
                if (childExpr != null) {
                    expressionList.add(childExpr);
                }
            }
        }
        if (expressionList.isEmpty()) {
            return Expression.NIL;
        } else if (expressionList.size() == 1) {
            return expressionList.get(0);
        } else if (expressionList.size() == 2) {
            Expression[] expressionArray = expressionList.toArray(new Expression[0]);
            return ff.function("strConcat", expressionArray);
        } else {
            Expression[] expressionArray = expressionList.toArray(new Expression[0]);
            return ff.function("Concatenate", expressionArray);
        }
    }

    /** Internal parse method - made protected for unit testing */
    protected ColorMapEntry parseColorMapEntry(Node root) {
        ColorMapEntry symbol = factory.createColorMapEntry();
        NamedNodeMap atts = root.getAttributes();
        if (atts.getNamedItem(colorMapLabelString) != null) {
            symbol.setLabel(atts.getNamedItem(colorMapLabelString).getNodeValue());
        }
        if (atts.getNamedItem(colorMapColorString) != null) {
            symbol.setColor(ff.literal(atts.getNamedItem(colorMapColorString).getNodeValue()));
        }
        if (atts.getNamedItem(colorMapOpacityString) != null) {
            symbol.setOpacity(
                    ff.literal(atts.getNamedItem(colorMapOpacityString).getNodeValue()));
        }
        if (atts.getNamedItem(colorMapQuantityString) != null) {
            symbol.setQuantity(
                    ff.literal(atts.getNamedItem(colorMapQuantityString).getNodeValue()));
        }

        return symbol;
    }

    /** Internal parse method - made protected for unit testing */
    protected ColorMap parseColorMap(Node root) {
        ColorMap symbol = factory.createColorMap();

        if (root.hasAttributes()) {
            // parsing type attribute
            final NamedNodeMap atts = root.getAttributes();
            Node typeAtt = atts.getNamedItem("type");
            if (typeAtt != null) {
                final String type = typeAtt.getNodeValue();

                if ("ramp".equalsIgnoreCase(type)) {
                    symbol.setType(org.geotools.api.style.ColorMap.TYPE_RAMP);
                } else if ("intervals".equalsIgnoreCase(type)) {
                    symbol.setType(org.geotools.api.style.ColorMap.TYPE_INTERVALS);
                } else if ("values".equalsIgnoreCase(type)) {
                    symbol.setType(org.geotools.api.style.ColorMap.TYPE_VALUES);
                } else if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.fine(MessageFormat.format(ErrorKeys.ILLEGAL_ARGUMENT_$2, "ColorMapType", type));
            }

            // parsing extended colors
            typeAtt = atts.getNamedItem("extended");
            if (typeAtt != null) {
                final String type = typeAtt.getNodeValue();

                if ("true".equalsIgnoreCase(type)) {
                    symbol.setExtendedColors(true);
                } else if ("false".equalsIgnoreCase(type)) {
                    symbol.setExtendedColors(false);
                } else if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.fine(MessageFormat.format(ErrorKeys.ILLEGAL_ARGUMENT_$2, "Extended", type));
            }
        }

        NodeList children = root.getChildNodes();
        final int length = children.getLength();
        for (int i = 0; i < length; i++) {
            Node child = children.item(i);

            if (child == null || child.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            String childName = child.getLocalName();
            if (childName == null) {
                childName = child.getNodeName();
            }

            if (childName.equalsIgnoreCase("ColorMapEntry")) {
                symbol.addColorMapEntry(parseColorMapEntry(child));
            }
        }

        return symbol;
    }

    /** Internal parse method - made protected for unit testing */
    protected SelectedChannelType parseSelectedChannel(Node root) {
        SelectedChannelType symbol = new SelectedChannelTypeImpl();

        NodeList children = root.getChildNodes();
        final int length = children.getLength();
        for (int i = 0; i < length; i++) {
            Node child = children.item(i);

            if (child == null || child.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            String childName = child.getLocalName();

            if (childName == null) {
                childName = child.getNodeName();
            } else if (childName.equalsIgnoreCase("SourceChannelName")) {
                if (child.getFirstChild() != null) {
                    symbol.setChannelName(parseParameterValueExpression(child, true));
                }
            } else if (childName.equalsIgnoreCase("ContrastEnhancement")) {
                symbol.setContrastEnhancement(parseContrastEnhancement(child));

                /*
                 * try { if (child.getFirstChild() != null && child.getFirstChild().getNodeType() == Node.TEXT_NODE)
                 * symbol.setContrastEnhancement((Expression) ExpressionBuilder .parse(child.getFirstChild().getNodeValue())); } catch (Exception e) {
                 * // TODO: handle exception }
                 */
            }
        }

        return symbol;
    }

    /** Internal parse method - made protected for unit testing */
    protected ChannelSelection parseChannelSelection(Node root) {
        NodeList children = root.getChildNodes();
        boolean isGray = isGray(children);
        List<SelectedChannelType> channels = new ArrayList<>();
        for (int i = 0; i < (isGray ? 1 : 3); i++) {
            channels.add(null);
        }
        final int length = children.getLength();
        for (int i = 0; i < length; i++) {
            Node child = children.item(i);

            if (child == null || child.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            String childName = child.getLocalName();
            if (childName != null) {
                if (childName.equalsIgnoreCase("GrayChannel")) {
                    channels.set(0, parseSelectedChannel(child));
                } else if (childName.equalsIgnoreCase("RedChannel")) {
                    channels.set(0, parseSelectedChannel(child));
                } else if (childName.equalsIgnoreCase("GreenChannel")) {
                    channels.set(1, parseSelectedChannel(child));
                } else if (childName.equalsIgnoreCase("BlueChannel")) {
                    channels.set(2, parseSelectedChannel(child));
                }
            }
        }

        ChannelSelection dap =
                factory.createChannelSelection(channels.toArray(new SelectedChannelType[channels.size()]));

        return dap;
    }

    private boolean isGray(NodeList children) {
        final int length = children.getLength();
        for (int i = 0; i < length; i++) {
            Node child = children.item(i);

            if (child == null || child.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            String childName = child.getLocalName();
            if (childName != null) {
                if (childName.equalsIgnoreCase("GrayChannel")) {
                    return true;
                } else if (childName.equalsIgnoreCase("RedChannel")
                        || childName.equalsIgnoreCase("GreenChannel")
                        || childName.equalsIgnoreCase("BlueChannel")) {
                    return false;
                }
            }
        }

        return false;
    }

    /** Internal parse method - made protected for unit testing */
    protected ContrastEnhancement parseContrastEnhancement(Node root) {
        ContrastEnhancement symbol = new ContrastEnhancementImpl();

        NodeList children = root.getChildNodes();
        final int length = children.getLength();
        for (int i = 0; i < length; i++) {
            Node child = children.item(i);

            if (child == null || child.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            String childName = child.getLocalName();
            if (childName == null) {
                childName = child.getNodeName();
            }

            if (CONTRAST_METHODS.contains(childName.toLowerCase())) {
                ContrastMethodStrategy parseContrastMethod = parseContrastMethod(childName, child);
                symbol.setMethod(parseContrastMethod.getMethod());
                symbol.setOptions(parseContrastMethod.getOptions());
            } else if (childName.equalsIgnoreCase("GammaValue")) {
                try {
                    Expression gamma = parseParameterValueExpression(child, false);
                    symbol.setGammaValue(gamma);
                } catch (Exception e) {
                    if (LOGGER.isLoggable(Level.WARNING)) LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
                }
            }
        }

        return symbol;
    }

    /** @return */
    private ContrastMethodStrategy parseContrastMethod(String name, Node root) {
        ContrastMethod met = ContrastMethod.NONE;
        ContrastMethodStrategy ret = null;
        if (NORMALIZE.equalsIgnoreCase(name)) {
            met = ContrastMethod.NORMALIZE;
            ret = new NormalizeContrastMethodStrategy();
        } else if (HISTOGRAM.equalsIgnoreCase(name)) {
            met = ContrastMethod.HISTOGRAM;
            ret = new HistogramContrastMethodStrategy();
        } else if (LOGARITHMIC.equalsIgnoreCase(name)) {
            met = ContrastMethod.LOGARITHMIC;
            ret = new LogarithmicContrastMethodStrategy();
        } else if (EXPONENTIAL.equalsIgnoreCase(name)) {
            met = ContrastMethod.EXPONENTIAL;
            ret = new ExponentialContrastMethodStrategy();
        } else {
            throw new IllegalArgumentException("Unknown strategy " + name);
        }

        ret.setMethod(met);
        // now extract any VendorOptions
        NodeList children = root.getChildNodes();
        final int length = children.getLength();
        for (int i = 0; i < length; i++) {
            Node child = children.item(i);

            if (child == null || child.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            String childName = child.getLocalName();
            if (childName == null) {
                childName = child.getNodeName();
            }
            if ("VendorOption".equalsIgnoreCase(childName)) {

                String key = getAttribute(child, "name");
                if (key == null) continue;
                Expression value = parseCssParameter(child);
                if (value == null) continue;
                ret.addOption(key, value);
            }
        }
        return ret;
    }

    /** Internal parse method - made protected for unit testing */
    protected ShadedRelief parseShadedRelief(Node root) {
        ShadedRelief symbol = new ShadedReliefImpl();

        NodeList children = root.getChildNodes();
        final int length = children.getLength();
        for (int i = 0; i < length; i++) {
            Node child = children.item(i);

            if (child == null || child.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            String childName = child.getLocalName();
            if (childName == null) {
                childName = child.getNodeName();
            }
            if ("BrightnessOnly".equalsIgnoreCase(childName)) {
                symbol.setBrightnessOnly(Boolean.parseBoolean(getFirstChildValue(child)));
            } else if ("ReliefFactor".equalsIgnoreCase(childName)) {
                try {
                    final String reliefString = getFirstChildValue(child);
                    Expression relief = ff.literal(Double.parseDouble(reliefString));
                    symbol.setReliefFactor(relief);
                } catch (Exception e) {
                    if (LOGGER.isLoggable(Level.WARNING)) LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
                }
            }
        }

        return symbol;
    }

    /**
     * parses the SLD for a point symbolizer
     *
     * @param root a w3c dom node
     * @return the pointsymbolizer
     */
    protected PointSymbolizer parsePointSymbolizer(Node root) {
        PointSymbolizer symbol = factory.getDefaultPointSymbolizer();
        // symbol.setGraphic(null);

        NamedNodeMap namedNodeMap = root.getAttributes();
        Node uomNode = namedNodeMap.getNamedItem(uomString);
        if (uomNode != null) {
            UomOgcMapping uomMapping = UomOgcMapping.get(uomNode.getNodeValue());
            symbol.setUnitOfMeasure(uomMapping.getUnit());
        }

        NodeList children = root.getChildNodes();
        final int length = children.getLength();
        for (int i = 0; i < length; i++) {
            Node child = children.item(i);

            if (child == null || child.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            String childName = child.getLocalName();
            if (childName == null) {
                childName = child.getNodeName();
            }

            if (childName.equalsIgnoreCase(geomString)) {
                symbol.setGeometry(parseGeometry(child));
            } else if (childName.equalsIgnoreCase(graphicSt)) {
                symbol.setGraphic(parseGraphic(child));
            } else if (childName.equalsIgnoreCase(VendorOptionString)) {
                parseVendorOption(symbol.getOptions(), child);
            }
        }

        return symbol;
    }

    /** Internal parse method - made protected for unit testing */
    protected Graphic parseGraphic(Node root) {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest("processing graphic " + root);
        }

        Graphic graphic = factory.getDefaultGraphic();

        NodeList children = root.getChildNodes();
        final int length = children.getLength();
        boolean firstGraphic = true;
        for (int i = 0; i < length; i++) {
            Node child = children.item(i);

            if (child == null || child.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            String childName = child.getLocalName();
            if (childName == null) {
                childName = child.getNodeName();
            }

            if (childName.equalsIgnoreCase("ExternalGraphic")) {
                if (LOGGER.isLoggable(Level.FINEST)) LOGGER.finest("parsing extgraphic " + child);
                if (firstGraphic) {
                    graphic.graphicalSymbols().clear();
                    firstGraphic = false;
                }
                graphic.graphicalSymbols().add(parseExternalGraphic(child));
            } else if (childName.equalsIgnoreCase("Mark")) {
                if (firstGraphic) {
                    graphic.graphicalSymbols().clear();
                    firstGraphic = false;
                }
                graphic.graphicalSymbols().add(parseMark(child));
            } else if (childName.equalsIgnoreCase(opacityString)) {
                graphic.setOpacity(parseCssParameter(child));
            } else if (childName.equalsIgnoreCase("size")) {
                graphic.setSize(parseCssParameter(child));
            } else if (childName.equalsIgnoreCase("displacement")) {
                graphic.setDisplacement(parseDisplacement(child));
            } else if (childName.equalsIgnoreCase("anchorPoint")) {
                graphic.setAnchorPoint(parseAnchorPoint(child));
            } else if (childName.equalsIgnoreCase("rotation")) {
                graphic.setRotation(parseCssParameter(child));
            }
        }

        return graphic;
    }

    /** Internal parse method - made protected for unit testing */
    protected String parseGeometryName(Node root) {
        Expression result = parseGeometry(root);
        if (result instanceof PropertyName) {
            return ((PropertyName) result).getPropertyName();
        }
        return null;
    }

    /** Internal parse method - made protected for unit testing */
    protected Expression parseGeometry(Node root) {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest("parsing GeometryExpression");
        }

        return parseCssParameter(root);
    }

    /** Internal parse method - made protected for unit testing */
    protected Mark parseMark(Node root) {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest("parsing mark");
        }

        Mark mark = factory.createMark();
        mark.setFill(null);
        mark.setStroke(null);

        NodeList children = root.getChildNodes();
        final int length = children.getLength();
        for (int i = 0; i < length; i++) {
            Node child = children.item(i);

            if (child == null || child.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            String childName = child.getLocalName();
            if (childName == null) {
                childName = child.getNodeName();
            }

            if (childName.equalsIgnoreCase(strokeString)) {
                mark.setStroke(parseStroke(child));
            } else if (childName.equalsIgnoreCase(fillSt)) {
                mark.setFill(parseFill(child));
            } else if (childName.equalsIgnoreCase("WellKnownName")) {
                if (LOGGER.isLoggable(Level.FINEST)) LOGGER.finest("setting mark to " + getFirstChildValue(child));
                Expression wellKnownName = parseCssParameter(child);
                if (wellKnownName instanceof Literal) {
                    String expanded = wellKnownName.evaluate(null, String.class);
                    if (expanded != null && expanded.startsWith("file://")) {
                        URL url = onlineResourceLocator.locateResource(expanded);
                        if (url != null) {
                            wellKnownName = ff.literal(url.toExternalForm());
                        } else {
                            LOGGER.log(Level.WARNING, "WellKnownName file reference could not be found: " + expanded);
                        }
                    }
                }
                mark.setWellKnownName(wellKnownName);
            }
        }

        return mark;
    }

    /** Internal parse method - made protected for unit testing */
    protected ExternalGraphic parseExternalGraphic(Node root) {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest("processing external graphic ");
        }

        String format = "";
        String uri = "";
        String content = null;
        Map<String, Object> paramList = new HashMap<>();

        NodeList children = root.getChildNodes();
        final int length = children.getLength();
        for (int i = 0; i < length; i++) {
            Node child = children.item(i);

            if (child == null || child.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            String childName = child.getLocalName();
            if (childName == null) {
                childName = child.getNodeName();
            }
            if (childName.equalsIgnoreCase("InlineContent")) {
                String contentEncoding = getAttribute(child, "encoding");
                if (LOGGER.isLoggable(Level.FINEST)) {
                    LOGGER.finest("inline content with encoding " + contentEncoding);
                }
                if ("base64".equals(contentEncoding)) {
                    content = getFirstChildValue(child);
                } else {
                    content = "";
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.warning("could not process <" + contentEncoding + "> content encoding");
                    }
                }
            } else if (childName.equalsIgnoreCase("OnLineResource")) {
                uri = parseOnlineResource(child);
            }

            if (childName.equalsIgnoreCase("format")) {
                if (LOGGER.isLoggable(Level.FINEST)) {
                    LOGGER.finest("format child is " + child);
                    LOGGER.finest("setting ExtGraph format " + getFirstChildValue(child));
                }
                format = getFirstChildValue(child);
            } else if (childName.equalsIgnoreCase("customProperty")) {
                if (LOGGER.isLoggable(Level.FINEST)) LOGGER.finest("custom child is " + child);
                String propName = child.getAttributes().getNamedItem("name").getNodeValue();
                if (LOGGER.isLoggable(Level.FINEST))
                    LOGGER.finest("setting custom property " + propName + " to " + getFirstChildValue(child));
                Expression value = parseCssParameter(child);
                paramList.put(propName, value);
            }
        }

        ExternalGraphic extgraph;
        if (content != null) {
            Icon icon = null;
            if (content.length() > 0) {
                try {
                    icon = parseIcon(content);
                } catch (IOException e) {
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.log(Level.WARNING, "could not parse graphic inline content: " + content, e);
                    }
                }
            }

            if (icon == null) {
                LOGGER.warning("returning empty icon");
                icon = EmptyIcon.INSTANCE;
            }

            extgraph = factory.createExternalGraphic(icon, format);
        } else {
            URL url = onlineResourceLocator.locateResource(uri);
            if (url == null) {
                extgraph = factory.createExternalGraphic(uri, format);
            } else {
                extgraph = factory.createExternalGraphic(url, format);
            }
        }
        extgraph.setCustomProperties(paramList);
        return extgraph;
    }

    /** Internal parse method - made protected for unit testing */
    protected String parseOnlineResource(Node root) {
        Element param = (Element) root;
        org.w3c.dom.NamedNodeMap map = param.getAttributes();
        final int length = map.getLength();
        LOGGER.finest("attributes " + map.toString());

        for (int k = 0; k < length; k++) {
            String res = map.item(k).getNodeValue();
            String name = map.item(k).getNodeName();
            // if(name == null){
            // name = map.item(k).getNodeName();
            // }
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.finest("processing attribute " + name + "=" + res);
            }

            // TODO: process the name space properly
            if (name.equalsIgnoreCase("xlink:href") || name.equalsIgnoreCase("href")) {
                if (LOGGER.isLoggable(Level.FINEST)) LOGGER.finest("seting ExtGraph uri " + res);
                return res;
            }
        }
        return null;
    }

    /** Internal parse method - made protected for unit testing */
    protected Stroke parseStroke(Node root) {
        Stroke stroke = factory.getDefaultStroke();
        NodeList list = findElements((Element) root, "GraphicFill");
        int length = list.getLength();
        if (length > 0) {
            if (LOGGER.isLoggable(Level.FINEST)) LOGGER.finest("stroke: found a graphic fill " + list.item(0));

            NodeList kids = list.item(0).getChildNodes();

            for (int i = 0; i < kids.getLength(); i++) {
                Node child = kids.item(i);

                if (child == null || child.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }
                String childName = child.getLocalName();
                if (childName == null) {
                    childName = child.getNodeName();
                }
                if (childName.equalsIgnoreCase(graphicSt)) {
                    Graphic g = parseGraphic(child);
                    if (LOGGER.isLoggable(Level.FINEST)) LOGGER.finest("setting stroke graphicfill with " + g);
                    stroke.setGraphicFill(g);
                }
            }
        }

        list = findElements((Element) root, "GraphicStroke");
        length = list.getLength();
        if (length > 0) {
            if (LOGGER.isLoggable(Level.FINEST)) LOGGER.finest("stroke: found a graphic stroke " + list.item(0));

            NodeList kids = list.item(0).getChildNodes();

            for (int i = 0; i < kids.getLength(); i++) {
                Node child = kids.item(i);

                if (child == null || child.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }
                String childName = child.getLocalName();
                if (childName == null) {
                    childName = child.getNodeName();
                }
                if (childName.equalsIgnoreCase(graphicSt)) {
                    Graphic g = parseGraphic(child);
                    if (LOGGER.isLoggable(Level.FINEST)) LOGGER.finest("setting stroke graphicStroke with " + g);
                    stroke.setGraphicStroke(g);
                }
            }
        }

        list = findElements((Element) root, "CssParameter");
        length = list.getLength();
        for (int i = 0; i < length; i++) {
            Node child = list.item(i);

            if (child == null || child.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.finest("now I am processing " + child);
            }

            Element param = (Element) child;
            org.w3c.dom.NamedNodeMap map = param.getAttributes();
            final int mapLength = map.getLength();
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.finest("attributes " + map.toString());
            }

            for (int k = 0; k < mapLength; k++) {
                String res = map.item(k).getNodeValue();

                if (LOGGER.isLoggable(Level.FINEST)) {
                    LOGGER.finest("processing attribute " + res);
                }
                // process the css entry
                //
                if (res.equalsIgnoreCase(strokeString)) {
                    Expression color = parseCssParameter(child, true);
                    stroke.setColor(color);
                } else if (res.equalsIgnoreCase("width") || res.equalsIgnoreCase("stroke-width")) {
                    Expression width = parseCssParameter(child, false);
                    stroke.setWidth(width);
                } else if (res.equalsIgnoreCase(opacityString) || res.equalsIgnoreCase("stroke-opacity")) {
                    Expression opacity = parseCssParameter(child, false);
                    stroke.setOpacity(opacity);
                } else if (res.equalsIgnoreCase("linecap") || res.equalsIgnoreCase("stroke-linecap")) {
                    // since these are system-dependent just pass them through and hope.
                    stroke.setLineCap(parseCssParameter(child));
                } else if (res.equalsIgnoreCase("linejoin") || res.equalsIgnoreCase("stroke-linejoin")) {
                    // since these are system-dependent just pass them through
                    // and hope.
                    stroke.setLineJoin(parseCssParameter(child));
                } else if (res.equalsIgnoreCase("dasharray") || res.equalsIgnoreCase("stroke-dasharray")) {
                    stroke.setDashArray(parseDashArray(child));
                } else if (res.equalsIgnoreCase("dashoffset") || res.equalsIgnoreCase("stroke-dashoffset")) {
                    stroke.setDashOffset(parseCssParameter(child));
                }
            }
        }

        return stroke;
    }

    private List<Expression> parseDashArray(Node root) {
        NodeList children = root.getChildNodes();
        List<Expression> expressions = new ArrayList<>();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child == null) continue;
            switch (child.getNodeType()) {
                case Node.TEXT_NODE:
                    handleDashArrayText(child.getNodeValue(), expressions);
                    break;
                case Node.ELEMENT_NODE:
                    handleDashArrayNode(child, expressions);
                    break;
                case Node.CDATA_SECTION_NODE:
                    handleDashArrayText(child.getNodeValue(), expressions);
                    break;
            }
        }
        return expressions;
    }

    private void handleDashArrayText(String text, List<Expression> expressions) {
        if (text == null || text.isEmpty()) {
            return;
        }
        for (String textPart : text.split("\\s+")) {
            if (!textPart.isEmpty()) {
                expressions.add(ff.literal(Float.valueOf(textPart)));
            }
        }
    }

    private void handleDashArrayNode(Node child, List<Expression> expressions) {
        Expression expression = expressionDOMParser.expression(child);
        if (expression instanceof Literal) {
            handleDashArrayLiteral((Literal) expression, expressions);
        } else {
            expressions.add(expression);
        }
    }

    private void handleDashArrayLiteral(Literal literal, List<Expression> expressions) {
        Object value = literal.getValue();
        if (value instanceof String) {
            handleDashArrayText((String) value, expressions);
        } else {
            expressions.add(literal);
        }
    }

    /** Internal parse method - made protected for unit testing */
    protected Fill parseFill(Node root) {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest("parsing fill ");
        }

        Fill fill = factory.getDefaultFill();
        NodeList list = findElements((Element) root, "GraphicFill");
        int length = list.getLength();
        if (length > 0) {
            if (LOGGER.isLoggable(Level.FINEST)) LOGGER.finest("fill found a graphic fill " + list.item(0));

            NodeList kids = list.item(0).getChildNodes();

            for (int i = 0; i < kids.getLength(); i++) {
                Node child = kids.item(i);

                if (child == null || child.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }
                String childName = child.getLocalName();
                if (childName == null) {
                    childName = child.getNodeName();
                }
                if (childName.equalsIgnoreCase(graphicSt)) {
                    Graphic g = parseGraphic(child);
                    if (LOGGER.isLoggable(Level.FINEST)) LOGGER.finest("setting fill graphic with " + g);
                    fill.setGraphicFill(g);
                }
            }
        }

        list = findElements((Element) root, "CssParameter");
        length = list.getLength();
        for (int i = 0; i < length; i++) {
            Node child = list.item(i);

            if (child == null || child.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            Element param = (Element) child;
            org.w3c.dom.NamedNodeMap map = param.getAttributes();
            final int mapLength = map.getLength();
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.finest("now I am processing " + child);
            }

            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.finest("attributes " + map.toString());
            }

            for (int k = 0; k < mapLength; k++) {
                String res = map.item(k).getNodeValue();

                if (LOGGER.isLoggable(Level.FINEST)) {
                    LOGGER.finest("processing attribute " + res);
                }

                if (res.equalsIgnoreCase(fillSt)) {
                    fill.setColor(parseCssParameter(child));
                } else if (res.equalsIgnoreCase(opacityString) || res.equalsIgnoreCase("fill-opacity")) {
                    fill.setOpacity(parseCssParameter(child));
                }
            }
        }

        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest("fill graphic " + fill.getGraphicFill());
        }

        return fill;
    }

    /** Concatenates the given expressions (through the strConcat FunctionFilter expression) */
    private Expression manageMixed(Expression left, Expression right) {
        if (left == null) return right;
        if (right == null) return left;
        Function mixed = ff.function("strConcat", new Expression[] {left, right});
        return mixed;
    }

    /**
     * Parses a css parameter. Default implementation trims whitespaces from text nodes.
     *
     * @param root node to parse
     */
    private Expression parseCssParameter(Node root) {
        return parseCssParameter(root, true);
    }

    /**
     * Parses a css parameter. You can choose if the parser must trim whitespace from text nodes or not.
     *
     * @param root node to parse
     * @param trimWhiteSpace true to trim whitespace from text nodes. If false, whitespaces will be collapsed into one
     */
    private Expression parseCssParameter(Node root, boolean trimWhiteSpace) {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest("parsingCssParam " + root);
        }

        NodeList children = root.getChildNodes();
        final int length = children.getLength();
        List<Expression> expressions = new ArrayList<>();
        List<Boolean> cdatas = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            Node child = children.item(i);

            // Added mixed="true" management through concatenation of text and
            // expression nodes
            if (child == null) {
                continue;
            } else if (child.getNodeType() == Node.TEXT_NODE) {
                String value = child.getNodeValue();
                if (value == null) continue;

                if (trimWhiteSpace) {
                    value = value.trim();
                } else {
                    // by spec the inner spaces should collapsed into one, leading and trailing
                    // space should be eliminated too
                    // http://www.w3.org/TR/2001/REC-xmlschema-2-20010502/ (4.3.6 whiteSpace)

                    // remove inside spaces
                    value = WHITESPACES.matcher(value).replaceAll(" ");
                    // we can't deal with leading and trailing whitespaces now
                    // as the parser will return each line of whitespace as a separate element
                    // we have to do that as post processing
                }

                if (value != null && value.length() != 0) {
                    Literal literal = ff.literal(value);

                    if (LOGGER.isLoggable(Level.FINEST)) {
                        LOGGER.finest("Built new literal " + literal);
                    }
                    // add the text node as a literal
                    expressions.add(literal);
                    cdatas.add(false);
                }
            } else if (child.getNodeType() == Node.ELEMENT_NODE) {

                if (LOGGER.isLoggable(Level.FINEST)) {
                    LOGGER.finest("about to parse " + child.getLocalName());
                }
                // add the element node as an expression
                expressions.add(expressionDOMParser.expression(child));
                cdatas.add(false);
            } else if (child.getNodeType() == Node.CDATA_SECTION_NODE) {
                String value = child.getNodeValue();
                if (value != null && value.length() != 0) {
                    // we build a literal straight, to preserve even cdata sections
                    // that have only spaces (as opposed to try and parse it as a literal
                    // using the expression dom parser)
                    Literal literal = ff.literal(value);

                    if (LOGGER.isLoggable(Level.FINEST)) {
                        LOGGER.finest("Built new literal " + literal);
                    }
                    // add the text node as a literal
                    expressions.add(literal);
                    cdatas.add(true);
                }
            } else continue;
        }

        if (expressions.isEmpty() && LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest("no children in CssParam");
        }

        if (!trimWhiteSpace) {
            // remove all leading white spaces, which means, find all
            // string literals, remove the white space ones, eventually
            // remove the leading white space form the first non white space one
            while (!expressions.isEmpty()) {
                Expression ex = expressions.get(0);

                // if it's not a string literal we're done
                if (!(ex instanceof Literal)) break;
                Literal literal = (Literal) ex;
                if (!(literal.getValue() instanceof String)) break;

                // ok, string literal.
                String s = (String) literal.getValue();
                if (!cdatas.get(0)) {
                    if ("".equals(s.trim())) {
                        // If it's whitespace, we have to remove it and continue
                        expressions.remove(0);
                        cdatas.remove(0);
                    } else {
                        // if it's not only whitespace, remove anyways the eventual whitespace
                        // at its beginning, and then exit, leading whitespace removal is done
                        if (s.startsWith(" ")) {
                            s = LEADING_WHITESPACES.matcher(s).replaceAll("");
                            expressions.set(0, ff.literal(s));
                        }
                        break;
                    }
                } else {
                    break;
                }
            }

            // remove also all trailing white spaces the same way
            while (!expressions.isEmpty()) {
                final int idx = expressions.size() - 1;
                Expression ex = expressions.get(idx);

                // if it's not a string literal we're done
                if (!(ex instanceof Literal)) break;
                Literal literal = (Literal) ex;
                if (!(literal.getValue() instanceof String)) break;

                // ok, string literal.
                String s = (String) literal.getValue();
                if (!cdatas.get(idx)) {
                    if ("".equals(s.trim())) {
                        // If it's whitespace, we have to remove it and continue
                        expressions.remove(idx);
                        cdatas.remove(idx);
                    } else {
                        // if it's not only whitespace, remove anyways the eventual whitespace
                        // at its end, and then exit, trailing whitespace removal is done
                        if (s.endsWith(" ")) {
                            s = TRAILING_WHITESPACES.matcher(s).replaceAll("");
                            expressions.set(idx, ff.literal(s));
                        }
                        break;
                    }
                } else {
                    break;
                }
            }
        }

        // now combine all expressions into one
        Expression ret = null;
        for (Expression expression : expressions) {
            ret = manageMixed(ret, expression);
        }
        // If the expression list is empty, we have an empty tag, and should return an empty string.
        if (ret == null) {
            return ff.literal("");
        }

        return ret;
    }

    /** Internal method to parse a Font Node; protected to allow for unit testing */
    protected Font parseFont(Node root) {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest("parsing font");
        }

        Font font = factory.getDefaultFont();
        boolean firstFontFamily = true;
        NodeList list = findElements((Element) root, "CssParameter");
        int length = list.getLength();
        for (int i = 0; i < length; i++) {
            Node child = list.item(i);

            if (child == null || child.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            Element param = (Element) child;
            org.w3c.dom.NamedNodeMap map = param.getAttributes();
            final int mapLength = map.getLength();
            for (int k = 0; k < mapLength; k++) {
                String res = map.item(k).getNodeValue();

                if (res.equalsIgnoreCase("font-family")) {
                    if (firstFontFamily) {
                        // wipe out the default font
                        font.getFamily().clear();
                        firstFontFamily = false;
                    }
                    // use add instead of set as we might have multiple fonts here
                    font.getFamily().add(parseCssParameter(child));
                } else if (res.equalsIgnoreCase("font-style")) {
                    font.setStyle(parseCssParameter(child));
                } else if (res.equalsIgnoreCase("font-size")) {
                    font.setSize(parseCssParameter(child));
                } else if (res.equalsIgnoreCase("font-weight")) {
                    font.setWeight(parseCssParameter(child));
                }
            }
        }

        return font;
    }

    /** Internal parse method - made protected for unit testing */
    protected LabelPlacement parseLabelPlacement(Node root) {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest("parsing labelPlacement");
        }

        LabelPlacement ret = null;
        NodeList children = root.getChildNodes();
        final int length = children.getLength();
        for (int i = 0; i < length; i++) {
            Node child = children.item(i);

            if (child == null || child.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            String childName = child.getLocalName();
            if (childName == null) {
                childName = child.getNodeName();
            }
            if (childName.equalsIgnoreCase("PointPlacement")) {
                ret = parsePointPlacement(child);
            } else if (childName.equalsIgnoreCase("LinePlacement")) {
                ret = parseLinePlacement(child);
            }
        }

        return ret;
    }

    /** Internal parse method - made protected for unit testing */
    protected PointPlacement parsePointPlacement(Node root) {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest("parsing pointPlacement");
        }

        Expression rotation = ff.literal(0.0);
        AnchorPoint ap = null;
        Displacement dp = null;

        NodeList children = root.getChildNodes();
        final int length = children.getLength();
        for (int i = 0; i < length; i++) {
            Node child = children.item(i);

            if (child == null || child.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            String childName = child.getLocalName();
            if (childName == null) {
                childName = child.getNodeName();
            }
            if (childName.equalsIgnoreCase("AnchorPoint")) {
                ap = parseAnchorPoint(child);
            } else if (childName.equalsIgnoreCase("Displacement")) {
                dp = parseDisplacement(child);
            } else if (childName.equalsIgnoreCase("Rotation")) {
                rotation = parseCssParameter(child);
            }
        }

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("setting anchorPoint " + ap);
            LOGGER.fine("setting displacement " + dp);
        }

        PointPlacement dpp = factory.createPointPlacement(ap, dp, rotation);

        return dpp;
    }

    /** Internal parse method - made protected for unit testing */
    protected LinePlacement parseLinePlacement(Node root) {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest("parsing linePlacement");
        }

        Expression offset = ff.literal(0.0);
        NodeList children = root.getChildNodes();
        final int length = children.getLength();
        for (int i = 0; i < length; i++) {
            Node child = children.item(i);

            if (child == null || child.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            String childName = child.getLocalName();
            if (childName == null) {
                childName = child.getNodeName();
            }
            if (childName.equalsIgnoreCase("PerpendicularOffset")) {
                offset = parseCssParameter(child);
            }
        }

        LinePlacement dlp = factory.createLinePlacement(offset);

        return dlp;
    }

    /** Internal method to parse an AnchorPoint node; protected visibility for testing. */
    protected AnchorPoint parseAnchorPoint(Node root) {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest("parsing anchorPoint");
        }

        Expression x = null;
        Expression y = null;

        NodeList children = root.getChildNodes();
        final int length = children.getLength();
        for (int i = 0; i < length; i++) {
            Node child = children.item(i);

            if (child == null || child.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            String childName = child.getLocalName();
            if (childName == null) {
                childName = child.getNodeName();
            }
            if (childName.equalsIgnoreCase("AnchorPointX")) {
                x = parseCssParameter(child);
            } else if (childName.equalsIgnoreCase("AnchorPointY")) {
                y = parseCssParameter(child);
            }
        }

        AnchorPoint dap = factory.createAnchorPoint(x, y);

        return dap;
    }

    /** Internal parse method - made protected for unit testing */
    protected Displacement parseDisplacement(Node root) {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest("parsing displacment");
        }

        Expression x = null;
        Expression y = null;
        NodeList children = root.getChildNodes();
        final int length = children.getLength();
        for (int i = 0; i < length; i++) {
            Node child = children.item(i);

            if (child == null || child.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            String childName = child.getLocalName();
            if (childName == null) {
                childName = child.getNodeName();
            }
            if (childName.equalsIgnoreCase("DisplacementX")) {
                x = parseCssParameter(child);
            }

            if (childName.equalsIgnoreCase("DisplacementY")) {
                y = parseCssParameter(child);
            }
        }

        Displacement dd = factory.createDisplacement(x, y);

        return dd;
    }

    /** Internal parse method - made protected for unit testing */
    protected Halo parseHalo(Node root) {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest("parsing halo");
        }
        Halo halo = factory.createHalo(factory.createFill(ff.literal("#FFFFFF")), ff.literal(1.0));

        NodeList children = root.getChildNodes();
        final int length = children.getLength();
        for (int i = 0; i < length; i++) {
            Node child = children.item(i);

            if (child == null || child.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            String childName = child.getLocalName();
            if (childName == null) {
                childName = child.getNodeName();
            }
            if (childName.equalsIgnoreCase(fillSt)) {
                halo.setFill(parseFill(child));
            } else if (childName.equalsIgnoreCase("Radius")) {
                halo.setRadius(parseCssParameter(child));
            }
        }

        return halo;
    }

    private static class EmptyIcon implements Icon {
        public static final EmptyIcon INSTANCE = new EmptyIcon();

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {}

        @Override
        public int getIconWidth() {
            return 1;
        }

        @Override
        public int getIconHeight() {
            return 1;
        }
    }
}
