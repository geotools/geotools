/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2008, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.style;

import java.util.List;

/**
 * A UserStyle is at the same semantic level as a NamedStyle used in the context of a WMS. In a sense, a named style can
 * be thought of as a reference to a hidden UserStyle that is stored inside of a map server.
 *
 * <p>A portrayal catalog consits of a set of feature portrayal objects. Many may exist for each feature type that may
 * occur in the dataset. each feature object has assigned a set of portrayal rules. This class is a merged between ISO
 * 19117 Portrayal and OGC SLD 1.1.0
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/sld">Implementation specification 1.1.0</A>
 * @author Open Geospatial Consortium
 * @author Johann Sorel (Geomatys)
 * @since GeoAPI 2.2
 */
public interface Style {

    /**
     * Style name (machine readable, don't show to users)
     *
     * @return String, identification name of this style
     */
    String getName();

    void setName(String name);

    void setBackground(Fill background);

    /**
     * Description for this style.
     *
     * @return Human readable description for use in user interfaces
     * @since 2.5.x
     */
    Description getDescription();

    /**
     * The IsDefault element identifies whether a style is the default style of a layer, for use in SLD ‘library mode’
     * when rendering or for storing inside of a map server. IsDefault uses “1” or “true” for true and “0” or “false”
     * for false. The default value is “0”.
     */
    boolean isDefault();

    /**
     * Indicates that this is the default style.
     *
     * <p>Assume this is kept for GeoServer enabling a WMS to track which style is considered the default. May consider
     * providing a clientProperties mechanism similar to Swing JComponent allowing applications to mark up the Style
     * content for custom uses.
     */
    void setDefault(boolean isDefault);

    /** FeatureTypeStyles rendered in order of appearance in this list. */
    List<FeatureTypeStyle> featureTypeStyles();

    /**
     * This functionality is from an ISO specificaiton; and conflicts with the idea of an else rule presented by SLD.
     *
     * <p>Implementations may choose to look up the first symbolizer of an elseFilter or allow this to be provided?
     *
     * @return Symbolizer to use if no rules work out.
     */
    Symbolizer getDefaultSpecification();

    /**
     * calls the visit method of a StyleVisitor
     *
     * @param visitor the style visitor
     */
    Object accept(TraversingStyleVisitor visitor, Object extraData);

    /** @param defaultSymbolizer To be used if a feature is not rendered by any of the rules */
    void setDefaultSpecification(Symbolizer defaultSymbolizer);

    /** Used to navigate Style information during portrayal. */
    void accept(StyleVisitor visitor);

    /** The background Fill , if any, <code>null</code> otherwise */
    default Fill getBackground() {
        return null;
    }
}
