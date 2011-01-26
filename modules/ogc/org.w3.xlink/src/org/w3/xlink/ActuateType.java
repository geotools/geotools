/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.w3.xlink;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.AbstractEnumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Actuate Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.w3.xlink.XlinkPackage#getActuateType()
 * @model extendedMetaData="name='actuate_._type'"
 * @generated
 */
public final class ActuateType extends AbstractEnumerator {
    /**
	 * The '<em><b>On Load</b></em>' literal value.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>On Load</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @see #ON_LOAD_LITERAL
	 * @model name="onLoad"
	 * @generated
	 * @ordered
	 */
    public static final int ON_LOAD = 0;

    /**
	 * The '<em><b>On Request</b></em>' literal value.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>On Request</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @see #ON_REQUEST_LITERAL
	 * @model name="onRequest"
	 * @generated
	 * @ordered
	 */
    public static final int ON_REQUEST = 1;

    /**
	 * The '<em><b>Other</b></em>' literal value.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Other</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @see #OTHER_LITERAL
	 * @model name="other"
	 * @generated
	 * @ordered
	 */
    public static final int OTHER = 2;

    /**
	 * The '<em><b>None</b></em>' literal value.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>None</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @see #NONE_LITERAL
	 * @model name="none"
	 * @generated
	 * @ordered
	 */
    public static final int NONE = 3;

    /**
	 * The '<em><b>On Load</b></em>' literal object.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #ON_LOAD
	 * @generated
	 * @ordered
	 */
    public static final ActuateType ON_LOAD_LITERAL = new ActuateType(ON_LOAD, "onLoad", "onLoad");

    /**
	 * The '<em><b>On Request</b></em>' literal object.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #ON_REQUEST
	 * @generated
	 * @ordered
	 */
    public static final ActuateType ON_REQUEST_LITERAL = new ActuateType(ON_REQUEST, "onRequest", "onRequest");

    /**
	 * The '<em><b>Other</b></em>' literal object.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #OTHER
	 * @generated
	 * @ordered
	 */
    public static final ActuateType OTHER_LITERAL = new ActuateType(OTHER, "other", "other");

    /**
	 * The '<em><b>None</b></em>' literal object.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #NONE
	 * @generated
	 * @ordered
	 */
    public static final ActuateType NONE_LITERAL = new ActuateType(NONE, "none", "none");

    /**
	 * An array of all the '<em><b>Actuate Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private static final ActuateType[] VALUES_ARRAY =
        new ActuateType[] {
			ON_LOAD_LITERAL,
			ON_REQUEST_LITERAL,
			OTHER_LITERAL,
			NONE_LITERAL,
		};

    /**
	 * A public read-only list of all the '<em><b>Actuate Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public static final List VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
	 * Returns the '<em><b>Actuate Type</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public static ActuateType get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			ActuateType result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

    /**
	 * Returns the '<em><b>Actuate Type</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public static ActuateType getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			ActuateType result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

    /**
	 * Returns the '<em><b>Actuate Type</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public static ActuateType get(int value) {
		switch (value) {
			case ON_LOAD: return ON_LOAD_LITERAL;
			case ON_REQUEST: return ON_REQUEST_LITERAL;
			case OTHER: return OTHER_LITERAL;
			case NONE: return NONE_LITERAL;
		}
		return null;
	}

    /**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private ActuateType(int value, String name, String literal) {
		super(value, name, literal);
	}

} //ActuateType
