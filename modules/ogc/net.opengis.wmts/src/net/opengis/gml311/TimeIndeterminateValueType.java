/**
 */
package net.opengis.gml311;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Time Indeterminate Value Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * <!-- begin-model-doc -->
 * This enumerated data type specifies values for indeterminate positions.
 * <!-- end-model-doc -->
 * @see net.opengis.gml311.Gml311Package#getTimeIndeterminateValueType()
 * @model extendedMetaData="name='TimeIndeterminateValueType'"
 * @generated
 */
public enum TimeIndeterminateValueType implements Enumerator {
    /**
     * The '<em><b>After</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #AFTER_VALUE
     * @generated
     * @ordered
     */
    AFTER(0, "after", "after"),

    /**
     * The '<em><b>Before</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #BEFORE_VALUE
     * @generated
     * @ordered
     */
    BEFORE(1, "before", "before"),

    /**
     * The '<em><b>Now</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #NOW_VALUE
     * @generated
     * @ordered
     */
    NOW(2, "now", "now"),

    /**
     * The '<em><b>Unknown</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #UNKNOWN_VALUE
     * @generated
     * @ordered
     */
    UNKNOWN(3, "unknown", "unknown");

    /**
     * The '<em><b>After</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>After</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #AFTER
     * @model name="after"
     * @generated
     * @ordered
     */
    public static final int AFTER_VALUE = 0;

    /**
     * The '<em><b>Before</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Before</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #BEFORE
     * @model name="before"
     * @generated
     * @ordered
     */
    public static final int BEFORE_VALUE = 1;

    /**
     * The '<em><b>Now</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Now</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #NOW
     * @model name="now"
     * @generated
     * @ordered
     */
    public static final int NOW_VALUE = 2;

    /**
     * The '<em><b>Unknown</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Unknown</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #UNKNOWN
     * @model name="unknown"
     * @generated
     * @ordered
     */
    public static final int UNKNOWN_VALUE = 3;

    /**
     * An array of all the '<em><b>Time Indeterminate Value Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final TimeIndeterminateValueType[] VALUES_ARRAY =
        new TimeIndeterminateValueType[] {
            AFTER,
            BEFORE,
            NOW,
            UNKNOWN,
        };

    /**
     * A public read-only list of all the '<em><b>Time Indeterminate Value Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final List<TimeIndeterminateValueType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Time Indeterminate Value Type</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param literal the literal.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static TimeIndeterminateValueType get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            TimeIndeterminateValueType result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Time Indeterminate Value Type</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param name the name.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static TimeIndeterminateValueType getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            TimeIndeterminateValueType result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Time Indeterminate Value Type</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the integer value.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static TimeIndeterminateValueType get(int value) {
        switch (value) {
            case AFTER_VALUE: return AFTER;
            case BEFORE_VALUE: return BEFORE;
            case NOW_VALUE: return NOW;
            case UNKNOWN_VALUE: return UNKNOWN;
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
    private TimeIndeterminateValueType(int value, String name, String literal) {
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
    
} //TimeIndeterminateValueType
