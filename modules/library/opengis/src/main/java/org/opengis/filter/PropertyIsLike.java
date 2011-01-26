/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005 Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.filter;

// OpenGIS direct dependencies
import org.opengis.annotation.Profile;
import org.opengis.annotation.XmlElement;
import org.opengis.filter.expression.Expression;


/**
 * Filter operator that performs the equivalent of the SQL "{@code like}" operator
 * on properties of a feature. The {@code PropertyIsLike} element is intended to encode
 * a character string comparison operator with pattern matching. The pattern is defined
 * by a combination of regular characters, the {@link #getWildCard wildCard} character,
 * the {@link #getSingleChar singleChar} character, and the {@link #getEscape escape}
 * character. The {@code wildCard} character matches zero or more characters. The
 * {@code singleChar} character matches exactly one character. The {@code escape}
 * character is used to escape the meaning of the {@code wildCard}, {@code singleChar}
 * and {@code escape} itself.
 *
 * @version <A HREF="http://www.opengis.org/docs/02-059.pdf">Implementation specification 1.0</A>
 * @author Chris Dillard (SYS Technologies)
 * @since GeoAPI 2.0
 */
@XmlElement("PropertyIsLike")
public interface PropertyIsLike extends Filter {
	/** Operator name used to check FilterCapabilities */
	public static String NAME = "Like";
    /**
     * Returns the expression whose value will be compared against the wildcard-
     * containing string provided by the getLiteral() method.
     */
    @XmlElement("PropertyName")
    Expression getExpression();

    /**
     * Returns the wildcard-containing string that will be used to check the
     * feature's properties.
     */
    @XmlElement("Literal")
    String getLiteral();

   /**
     * Returns the string that can be used in the "literal" property of this
     * object to match any sequence of characters.
     * <p>
     * The default value for this property is the one character string "%".
     */
    @XmlElement("wildCard")
    String getWildCard();

    /**
     * Returns the string that can be used in the "literal" property of this
     * object to match exactly one character.
     * <p>
     * The default value for this property is the one character string "_".
     */
    @XmlElement("singleChar")
    String getSingleChar();

    /**
     * Returns the string that can be used in the "literal" property of this
     * object to prefix one of the wild card characters to indicate that it
     * should be matched literally in the content of the feature's property.
     * The default value for this property is the single character "'".
     */
    @XmlElement("escape")
    String getEscape();

    /**
     * Flag controlling wither comparisons are case sensitive.
     * <p>
     * The ability to match case is pending the Filter 2.0 specification.
     *  
     * @return <code>true</code> if the comparison is case sensetive, otherwise <code>false</code>.
     */
    @XmlElement("matchCase")
    boolean isMatchingCase();
}
