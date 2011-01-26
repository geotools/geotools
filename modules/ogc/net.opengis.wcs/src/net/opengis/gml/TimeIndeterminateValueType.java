/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.gml;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.AbstractEnumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Time Indeterminate Value Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * <!-- begin-model-doc -->
 * 
 *         This enumerated data type specifies values for indeterminate positions.
 * <!-- end-model-doc -->
 * @see net.opengis.gml.GmlPackage#getTimeIndeterminateValueType()
 * @model extendedMetaData="name='TimeIndeterminateValueType'"
 * @generated
 */
public final class TimeIndeterminateValueType extends AbstractEnumerator {
    /**
	 * The '<em><b>After</b></em>' literal value.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>After</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @see #AFTER_LITERAL
	 * @model name="after"
	 * @generated
	 * @ordered
	 */
    public static final int AFTER = 0;

    /**
	 * The '<em><b>Before</b></em>' literal value.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Before</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @see #BEFORE_LITERAL
	 * @model name="before"
	 * @generated
	 * @ordered
	 */
    public static final int BEFORE = 1;

    /**
	 * The '<em><b>Now</b></em>' literal value.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Now</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @see #NOW_LITERAL
	 * @model name="now"
	 * @generated
	 * @ordered
	 */
    public static final int NOW = 2;

    /**
	 * The '<em><b>Unknown</b></em>' literal value.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Unknown</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @see #UNKNOWN_LITERAL
	 * @model name="unknown"
	 * @generated
	 * @ordered
	 */
    public static final int UNKNOWN = 3;

    /**
	 * The '<em><b>After</b></em>' literal object.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #AFTER
	 * @generated
	 * @ordered
	 */
    public static final TimeIndeterminateValueType AFTER_LITERAL = new TimeIndeterminateValueType(AFTER, "after", "after");

    /**
	 * The '<em><b>Before</b></em>' literal object.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #BEFORE
	 * @generated
	 * @ordered
	 */
    public static final TimeIndeterminateValueType BEFORE_LITERAL = new TimeIndeterminateValueType(BEFORE, "before", "before");

    /**
	 * The '<em><b>Now</b></em>' literal object.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #NOW
	 * @generated
	 * @ordered
	 */
    public static final TimeIndeterminateValueType NOW_LITERAL = new TimeIndeterminateValueType(NOW, "now", "now");

    /**
	 * The '<em><b>Unknown</b></em>' literal object.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #UNKNOWN
	 * @generated
	 * @ordered
	 */
    public static final TimeIndeterminateValueType UNKNOWN_LITERAL = new TimeIndeterminateValueType(UNKNOWN, "unknown", "unknown");

    /**
	 * An array of all the '<em><b>Time Indeterminate Value Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private static final TimeIndeterminateValueType[] VALUES_ARRAY =
        new TimeIndeterminateValueType[] {
			AFTER_LITERAL,
			BEFORE_LITERAL,
			NOW_LITERAL,
			UNKNOWN_LITERAL,
		};

    /**
	 * A public read-only list of all the '<em><b>Time Indeterminate Value Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public static final List VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
	 * Returns the '<em><b>Time Indeterminate Value Type</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
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
	 * @generated
	 */
    public static TimeIndeterminateValueType get(int value) {
		switch (value) {
			case AFTER: return AFTER_LITERAL;
			case BEFORE: return BEFORE_LITERAL;
			case NOW: return NOW_LITERAL;
			case UNKNOWN: return UNKNOWN_LITERAL;
		}
		return null;
	}

    /**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private TimeIndeterminateValueType(int value, String name, String literal) {
		super(value, name, literal);
	}

} //TimeIndeterminateValueType
