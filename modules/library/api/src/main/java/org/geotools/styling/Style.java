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
package org.geotools.styling;

import java.util.List;

/**
 * Indicates how geographical content should be displayed (we call this a style for simplicity; in the spec it is called a UserStyle (user-defined style)).
 * <p>
 * The details of this object are taken from the
 * <a href="https://portal.opengeospatial.org/files/?artifact_id=1188">
 * OGC Styled-Layer Descriptor Report (OGC 02-070) version 1.0.0.</a>:
 * <pre><code>
 * &lt;xsd:element name="UserStyle"&gt;
 *   &lt;xsd:annotation&gt;
 *     &lt;xsd:documentation&gt;
 *       A UserStyle allows user-defined styling and is semantically
 *       equivalent to a WMS named style.
 *     &lt;/xsd:documentation&gt;
 *   &lt;/xsd:annotation&gt;
 *   &lt;xsd:complexType&gt;
 *     &lt;xsd:sequence&gt;
 *       &lt;xsd:element ref="sld:Name" minOccurs="0"/&gt;
 *       &lt;xsd:element ref="sld:Title" minOccurs="0"/&gt;
 *       &lt;xsd:element ref="sld:Abstract" minOccurs="0"/&gt;
 *       &lt;xsd:element ref="sld:IsDefault" minOccurs="0"/&gt;
 *       &lt;xsd:element ref="sld:FeatureTypeStyle" maxOccurs="unbounded"/&gt;
 *     &lt;/xsd:sequence&gt;
 *   &lt;/xsd:complexType&gt;
 * &lt;/xsd:element&gt;
 * </code></pre>
 *
 * @source $URL$
 * @version $Id$
 * @author James Macgill
 */
public interface Style extends org.opengis.style.Style {

    void setName(String name);

    /**
     * Description for this style.
     * @return Human readable description for use in user interfaces
     * @since 2.5.x
     */
    Description getDescription();
    
    /**
     * Style Title (human readable name for user interfaces) 
     * 
     * @deprecated use getDescription().getTitle().toString()
     */
    String getTitle();

    /**
     * @param title
     * @deprecated please use getDescription().setTitle( new SimpleInternationalString( text ) );
     */
    void setTitle(String title);

    /** 
     * Description of this style 
     * 
     * @deprecated use getDesciption().getAbstract().toString()
     */
    String getAbstract();

    /**
     * @deprecated use getDescription().setAbstract( new SimpleInternationalString( text ) );
     */
    void setAbstract(String abstractStr);

    /**
     * Indicates that this is the default style.
     * <p>
     * Assume this is kept for GeoServer enabling a WMS to track
     * which style is considered the default. May consider providing a
     * clientProperties mechanism similar to Swing JComponent allowing
     * applications to mark up the Style content for custom uses.
     * </p>
     * @param isDefault
     */
    void setDefault(boolean isDefault);

    /**
     * FeatureTypeStyles rendered in order of appearance in this list.
     */
    public List<FeatureTypeStyle> featureTypeStyles();

    /**
     * This functionality is from an ISO specificaiton; and conflicts with the idea of an
     * else rule presented by SLD.
     * <p>
     * Implementations may choose to look up the first symbolizer of an elseFilter or allow
     * this to be provided?
     * 
     * @return Symbolizer to use if no rules work out.
     */
    public Symbolizer getDefaultSpecification();
    
    /**
     * @param defaultSymbolizer To be used if a feature is not rendered by any of the rules
     */
    public void setDefaultSpecification( Symbolizer defaultSymbolizer );
    
    /**
     * Array of FeatureTypeStyles in portrayal order.
     * <p>
     * FeatureTypeStyle entries are rendered in order of appearance in this list.
     * </p>
     * <p>
     * <i>Note: We are using a Array here to continue with Java 1.4 deployment.</i>
     * </p>
     * @deprecated use featureTypeStyles().toArray( new FeatureTypeStyle[0] )
     */
    FeatureTypeStyle[] getFeatureTypeStyles();

    
    /**
     * @deprecated Use featureTypeStyles().clear(); featureTypeStyles.addAll( ... )
     */
    void setFeatureTypeStyles(FeatureTypeStyle[] types);

    /**
     * @deprecated Use featureTypeStyles().add( type )
     */
    void addFeatureTypeStyle(FeatureTypeStyle type);

    /**
     * Used to navigate Style information during portrayal.
     *
     * @param visitor
     */
    void accept(org.geotools.styling.StyleVisitor visitor);
}
