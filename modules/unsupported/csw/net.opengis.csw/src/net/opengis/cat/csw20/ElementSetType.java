/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20;

import java.lang.String;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Element Set Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * <!-- begin-model-doc -->
 * Named subsets of catalogue object properties; these
 *          views are mapped to a specific information model and
 *          are defined in an application profile.
 * <!-- end-model-doc -->
 * @see net.opengis.cat.csw20.Csw20Package#getElementSetType()
 * @model extendedMetaData="name='ElementSetType'"
 * @generated
 */
public enum ElementSetType implements Enumerator {
    /**
     * The '<em><b>Brief</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #BRIEF_VALUE
     * @generated
     * @ordered
     */
    BRIEF(0, "brief", "brief"),

    /**
     * The '<em><b>Summary</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #SUMMARY_VALUE
     * @generated
     * @ordered
     */
    SUMMARY(1, "summary", "summary"),

    /**
     * The '<em><b>Full</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #FULL_VALUE
     * @generated
     * @ordered
     */
    FULL(2, "full", "full");

    /**
     * The '<em><b>Brief</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Brief</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #BRIEF
     * @model name="brief"
     * @generated
     * @ordered
     */
    public static final int BRIEF_VALUE = 0;

    /**
     * The '<em><b>Summary</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Summary</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #SUMMARY
     * @model name="summary"
     * @generated
     * @ordered
     */
    public static final int SUMMARY_VALUE = 1;

    /**
     * The '<em><b>Full</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Full</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #FULL
     * @model name="full"
     * @generated
     * @ordered
     */
    public static final int FULL_VALUE = 2;

    /**
     * An array of all the '<em><b>Element Set Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final ElementSetType[] VALUES_ARRAY =
        new ElementSetType[] {
            BRIEF,
            SUMMARY,
            FULL,
        };

    /**
     * A public read-only list of all the '<em><b>Element Set Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final List<ElementSetType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Element Set Type</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static ElementSetType get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            ElementSetType result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Element Set Type</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static ElementSetType getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            ElementSetType result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Element Set Type</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static ElementSetType get(int value) {
        switch (value) {
            case BRIEF_VALUE: return BRIEF;
            case SUMMARY_VALUE: return SUMMARY;
            case FULL_VALUE: return FULL;
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
    private ElementSetType(int value, String name, String literal) {
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
    
} //ElementSetType
