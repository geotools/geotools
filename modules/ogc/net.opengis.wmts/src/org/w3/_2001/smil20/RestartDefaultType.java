/**
 */
package org.w3._2001.smil20;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Restart Default Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.w3._2001.smil20.Smil20Package#getRestartDefaultType()
 * @model extendedMetaData="name='restartDefaultType'"
 * @generated
 */
public enum RestartDefaultType implements Enumerator {
    /**
     * The '<em><b>Never</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #NEVER_VALUE
     * @generated
     * @ordered
     */
    NEVER(0, "never", "never"),

    /**
     * The '<em><b>Always</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #ALWAYS_VALUE
     * @generated
     * @ordered
     */
    ALWAYS(1, "always", "always"),

    /**
     * The '<em><b>When Not Active</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #WHEN_NOT_ACTIVE_VALUE
     * @generated
     * @ordered
     */
    WHEN_NOT_ACTIVE(2, "whenNotActive", "whenNotActive"),

    /**
     * The '<em><b>Inherit</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #INHERIT_VALUE
     * @generated
     * @ordered
     */
    INHERIT(3, "inherit", "inherit");

    /**
     * The '<em><b>Never</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Never</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #NEVER
     * @model name="never"
     * @generated
     * @ordered
     */
    public static final int NEVER_VALUE = 0;

    /**
     * The '<em><b>Always</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Always</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #ALWAYS
     * @model name="always"
     * @generated
     * @ordered
     */
    public static final int ALWAYS_VALUE = 1;

    /**
     * The '<em><b>When Not Active</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>When Not Active</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #WHEN_NOT_ACTIVE
     * @model name="whenNotActive"
     * @generated
     * @ordered
     */
    public static final int WHEN_NOT_ACTIVE_VALUE = 2;

    /**
     * The '<em><b>Inherit</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Inherit</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #INHERIT
     * @model name="inherit"
     * @generated
     * @ordered
     */
    public static final int INHERIT_VALUE = 3;

    /**
     * An array of all the '<em><b>Restart Default Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final RestartDefaultType[] VALUES_ARRAY =
        new RestartDefaultType[] {
            NEVER,
            ALWAYS,
            WHEN_NOT_ACTIVE,
            INHERIT,
        };

    /**
     * A public read-only list of all the '<em><b>Restart Default Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final List<RestartDefaultType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Restart Default Type</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param literal the literal.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static RestartDefaultType get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            RestartDefaultType result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Restart Default Type</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param name the name.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static RestartDefaultType getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            RestartDefaultType result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Restart Default Type</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the integer value.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static RestartDefaultType get(int value) {
        switch (value) {
            case NEVER_VALUE: return NEVER;
            case ALWAYS_VALUE: return ALWAYS;
            case WHEN_NOT_ACTIVE_VALUE: return WHEN_NOT_ACTIVE;
            case INHERIT_VALUE: return INHERIT;
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
    private RestartDefaultType(int value, String name, String literal) {
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
    
} //RestartDefaultType
