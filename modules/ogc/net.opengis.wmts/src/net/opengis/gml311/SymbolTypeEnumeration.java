/**
 */
package net.opengis.gml311;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Symbol Type Enumeration</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * <!-- begin-model-doc -->
 * Used to specify the type of the symbol used.
 * <!-- end-model-doc -->
 * @see net.opengis.gml311.Gml311Package#getSymbolTypeEnumeration()
 * @model extendedMetaData="name='SymbolTypeEnumeration'"
 * @generated
 */
public enum SymbolTypeEnumeration implements Enumerator {
    /**
     * The '<em><b>Svg</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #SVG_VALUE
     * @generated
     * @ordered
     */
    SVG(0, "svg", "svg"),

    /**
     * The '<em><b>Xpath</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #XPATH_VALUE
     * @generated
     * @ordered
     */
    XPATH(1, "xpath", "xpath"),

    /**
     * The '<em><b>Other</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #OTHER_VALUE
     * @generated
     * @ordered
     */
    OTHER(2, "other", "other");

    /**
     * The '<em><b>Svg</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Svg</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #SVG
     * @model name="svg"
     * @generated
     * @ordered
     */
    public static final int SVG_VALUE = 0;

    /**
     * The '<em><b>Xpath</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Xpath</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #XPATH
     * @model name="xpath"
     * @generated
     * @ordered
     */
    public static final int XPATH_VALUE = 1;

    /**
     * The '<em><b>Other</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Other</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #OTHER
     * @model name="other"
     * @generated
     * @ordered
     */
    public static final int OTHER_VALUE = 2;

    /**
     * An array of all the '<em><b>Symbol Type Enumeration</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final SymbolTypeEnumeration[] VALUES_ARRAY =
        new SymbolTypeEnumeration[] {
            SVG,
            XPATH,
            OTHER,
        };

    /**
     * A public read-only list of all the '<em><b>Symbol Type Enumeration</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final List<SymbolTypeEnumeration> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Symbol Type Enumeration</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param literal the literal.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static SymbolTypeEnumeration get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            SymbolTypeEnumeration result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Symbol Type Enumeration</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param name the name.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static SymbolTypeEnumeration getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            SymbolTypeEnumeration result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Symbol Type Enumeration</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the integer value.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static SymbolTypeEnumeration get(int value) {
        switch (value) {
            case SVG_VALUE: return SVG;
            case XPATH_VALUE: return XPATH;
            case OTHER_VALUE: return OTHER;
        }
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private final int value;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private final String name;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private final String literal;

    /**
     * Only this class can construct instances.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private SymbolTypeEnumeration(int value, String name, String literal) {
        this.value = value;
        this.name = name;
        this.literal = literal;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public int getValue() {
      return value;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getName() {
      return name;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getLiteral() {
      return literal;
    }

    /**
     * Returns the literal value of the enumerator, which is its string representation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        return literal;
    }
    
} //SymbolTypeEnumeration
