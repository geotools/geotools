/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.fes20;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Version Action Tokens</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see net.opengis.fes20.Fes20Package#getVersionActionTokens()
 * @model extendedMetaData="name='VersionActionTokens'"
 * @generated
 */
public enum VersionActionTokens implements Enumerator {
    /**
     * The '<em><b>FIRST</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #FIRST_VALUE
     * @generated
     * @ordered
     */
    FIRST(0, "FIRST", "FIRST"),

    /**
     * The '<em><b>LAST</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #LAST_VALUE
     * @generated
     * @ordered
     */
    LAST(1, "LAST", "LAST"),

    /**
     * The '<em><b>PREVIOUS</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #PREVIOUS_VALUE
     * @generated
     * @ordered
     */
    PREVIOUS(2, "PREVIOUS", "PREVIOUS"),

    /**
     * The '<em><b>NEXT</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #NEXT_VALUE
     * @generated
     * @ordered
     */
    NEXT(3, "NEXT", "NEXT"),

    /**
     * The '<em><b>ALL</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #ALL_VALUE
     * @generated
     * @ordered
     */
    ALL(4, "ALL", "ALL");

    /**
     * The '<em><b>FIRST</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>FIRST</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #FIRST
     * @model
     * @generated
     * @ordered
     */
    public static final int FIRST_VALUE = 0;

    /**
     * The '<em><b>LAST</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>LAST</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #LAST
     * @model
     * @generated
     * @ordered
     */
    public static final int LAST_VALUE = 1;

    /**
     * The '<em><b>PREVIOUS</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>PREVIOUS</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #PREVIOUS
     * @model
     * @generated
     * @ordered
     */
    public static final int PREVIOUS_VALUE = 2;

    /**
     * The '<em><b>NEXT</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>NEXT</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #NEXT
     * @model
     * @generated
     * @ordered
     */
    public static final int NEXT_VALUE = 3;

    /**
     * The '<em><b>ALL</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>ALL</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #ALL
     * @model
     * @generated
     * @ordered
     */
    public static final int ALL_VALUE = 4;

    /**
     * An array of all the '<em><b>Version Action Tokens</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final VersionActionTokens[] VALUES_ARRAY =
        new VersionActionTokens[] {
            FIRST,
            LAST,
            PREVIOUS,
            NEXT,
            ALL,
        };

    /**
     * A public read-only list of all the '<em><b>Version Action Tokens</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final List<VersionActionTokens> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Version Action Tokens</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static VersionActionTokens get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            VersionActionTokens result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Version Action Tokens</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static VersionActionTokens getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            VersionActionTokens result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Version Action Tokens</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static VersionActionTokens get(int value) {
        switch (value) {
            case FIRST_VALUE: return FIRST;
            case LAST_VALUE: return LAST;
            case PREVIOUS_VALUE: return PREVIOUS;
            case NEXT_VALUE: return NEXT;
            case ALL_VALUE: return ALL;
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
    private VersionActionTokens(int value, String name, String literal) {
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
    
} //VersionActionTokens
