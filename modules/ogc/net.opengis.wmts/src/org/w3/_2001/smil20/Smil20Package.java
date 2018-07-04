/**
 */
package org.w3._2001.smil20;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * <!-- begin-model-doc -->
 * 
 *    <div xmlns="http://www.w3.org/1999/xhtml">
 *     <h1>About the XML namespace</h1>
 * 
 *     <div class="bodytext">
 *      <p>
 *       This schema document describes the XML namespace, in a form
 *       suitable for import by other schema documents.
 *      </p>
 *      <p>
 *       See <a href="http://www.w3.org/XML/1998/namespace.html">
 *       http://www.w3.org/XML/1998/namespace.html</a> and
 *       <a href="http://www.w3.org/TR/REC-xml">
 *       http://www.w3.org/TR/REC-xml</a> for information 
 *       about this namespace.
 *      </p>
 *      <p>
 *       Note that local names in this namespace are intended to be
 *       defined only by the World Wide Web Consortium or its subgroups.
 *       The names currently defined in this namespace are listed below.
 *       They should not be used with conflicting semantics by any Working
 *       Group, specification, or document instance.
 *      </p>
 *      <p>   
 *       See further below in this document for more information about <a href="#usage">how to refer to this schema document from your own
 *       XSD schema documents</a> and about <a href="#nsversioning">the
 *       namespace-versioning policy governing this schema document</a>.
 *      </p>
 *     </div>
 *    </div>
 *   
 * 
 *    <div xmlns="http://www.w3.org/1999/xhtml">
 *    
 *     <h3>Father (in any context at all)</h3> 
 * 
 *     <div class="bodytext">
 *      <p>
 *       denotes Jon Bosak, the chair of 
 *       the original XML Working Group.  This name is reserved by 
 *       the following decision of the W3C XML Plenary and 
 *       XML Coordination groups:
 *      </p>
 *      <blockquote>
 *        <p>
 * 	In appreciation for his vision, leadership and
 * 	dedication the W3C XML Plenary on this 10th day of
 * 	February, 2000, reserves for Jon Bosak in perpetuity
 * 	the XML name "xml:Father".
 *        </p>
 *      </blockquote>
 *     </div>
 *    </div>
 *   
 * 
 *    <div id="usage" xml:id="usage" xmlns="http://www.w3.org/1999/xhtml">
 *     <h2>
 *       <a name="usage">About this schema document</a>
 *     </h2>
 * 
 *     <div class="bodytext">
 *      <p>
 *       This schema defines attributes and an attribute group suitable
 *       for use by schemas wishing to allow <code>xml:base</code>,
 *       <code>xml:lang</code>, <code>xml:space</code> or
 *       <code>xml:id</code> attributes on elements they define.
 *      </p>
 *      <p>
 *       To enable this, such a schema must import this schema for
 *       the XML namespace, e.g. as follows:
 *      </p>
 *      <pre>
 *           &lt;schema . . .&gt;
 *            . . .
 *            &lt;import namespace="http://www.w3.org/XML/1998/namespace"
 *                       schemaLocation="http://www.w3.org/2001/xml.xsd"/&gt;
 *      </pre>
 *      <p>
 *       or
 *      </p>
 *      <pre>
 *            &lt;import namespace="http://www.w3.org/XML/1998/namespace"
 *                       schemaLocation="http://www.w3.org/2009/01/xml.xsd"/&gt;
 *      </pre>
 *      <p>
 *       Subsequently, qualified reference to any of the attributes or the
 *       group defined below will have the desired effect, e.g.
 *      </p>
 *      <pre>
 *           &lt;type . . .&gt;
 *            . . .
 *            &lt;attributeGroup ref="xml:specialAttrs"/&gt;
 *      </pre>
 *      <p>
 *       will define a type which will schema-validate an instance element
 *       with any of those attributes.
 *      </p>
 *     </div>
 *    </div>
 *   
 * 
 *    <div id="nsversioning" xml:id="nsversioning" xmlns="http://www.w3.org/1999/xhtml">
 *     <h2>
 *       <a name="nsversioning">Versioning policy for this schema document</a>
 *     </h2>
 *     <div class="bodytext">
 *      <p>
 *       In keeping with the XML Schema WG's standard versioning
 *       policy, this schema document will persist at
 *       <a href="http://www.w3.org/2009/01/xml.xsd">
 *        http://www.w3.org/2009/01/xml.xsd</a>.
 *      </p>
 *      <p>
 *       At the date of issue it can also be found at
 *       <a href="http://www.w3.org/2001/xml.xsd">
 *        http://www.w3.org/2001/xml.xsd</a>.
 *      </p>
 *      <p>
 *       The schema document at that URI may however change in the future,
 *       in order to remain compatible with the latest version of XML
 *       Schema itself, or with the XML namespace itself.  In other words,
 *       if the XML Schema or XML namespaces change, the version of this
 *       document at <a href="http://www.w3.org/2001/xml.xsd">
 *        http://www.w3.org/2001/xml.xsd 
 *       </a> 
 *       will change accordingly; the version at 
 *       <a href="http://www.w3.org/2009/01/xml.xsd">
 *        http://www.w3.org/2009/01/xml.xsd 
 *       </a> 
 *       will not change.
 *      </p>
 *      <p>
 *       Previous dated (and unchanging) versions of this schema 
 *       document are at:
 *      </p>
 *      <ul>
 *       <li>
 *           <a href="http://www.w3.org/2009/01/xml.xsd">
 * 	http://www.w3.org/2009/01/xml.xsd</a>
 *         </li>
 *       <li>
 *           <a href="http://www.w3.org/2007/08/xml.xsd">
 * 	http://www.w3.org/2007/08/xml.xsd</a>
 *         </li>
 *       <li>
 *           <a href="http://www.w3.org/2004/10/xml.xsd">
 * 	http://www.w3.org/2004/10/xml.xsd</a>
 *         </li>
 *       <li>
 *           <a href="http://www.w3.org/2001/03/xml.xsd">
 * 	http://www.w3.org/2001/03/xml.xsd</a>
 *         </li>
 *      </ul>
 *     </div>
 *    </div>
 *   
 * <!-- end-model-doc -->
 * @see org.w3._2001.smil20.Smil20Factory
 * @model kind="package"
 * @generated
 */
public interface Smil20Package extends EPackage {
    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "smil20";

    /**
     * The package namespace URI.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "http://www.w3.org/2001/SMIL20/";

    /**
     * The package namespace name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_PREFIX = "smil20";

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    Smil20Package eINSTANCE = org.w3._2001.smil20.impl.Smil20PackageImpl.init();

    /**
     * The meta object id for the '{@link org.w3._2001.smil20.impl.AnimateColorPrototypeImpl <em>Animate Color Prototype</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.w3._2001.smil20.impl.AnimateColorPrototypeImpl
     * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getAnimateColorPrototype()
     * @generated
     */
    int ANIMATE_COLOR_PROTOTYPE = 0;

    /**
     * The feature id for the '<em><b>Accumulate</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_COLOR_PROTOTYPE__ACCUMULATE = 0;

    /**
     * The feature id for the '<em><b>Additive</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_COLOR_PROTOTYPE__ADDITIVE = 1;

    /**
     * The feature id for the '<em><b>Attribute Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_COLOR_PROTOTYPE__ATTRIBUTE_NAME = 2;

    /**
     * The feature id for the '<em><b>Attribute Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_COLOR_PROTOTYPE__ATTRIBUTE_TYPE = 3;

    /**
     * The feature id for the '<em><b>By</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_COLOR_PROTOTYPE__BY = 4;

    /**
     * The feature id for the '<em><b>From</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_COLOR_PROTOTYPE__FROM = 5;

    /**
     * The feature id for the '<em><b>To</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_COLOR_PROTOTYPE__TO = 6;

    /**
     * The feature id for the '<em><b>Values</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_COLOR_PROTOTYPE__VALUES = 7;

    /**
     * The number of structural features of the '<em>Animate Color Prototype</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_COLOR_PROTOTYPE_FEATURE_COUNT = 8;

    /**
     * The number of operations of the '<em>Animate Color Prototype</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_COLOR_PROTOTYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.w3._2001.smil20.impl.AnimateMotionPrototypeImpl <em>Animate Motion Prototype</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.w3._2001.smil20.impl.AnimateMotionPrototypeImpl
     * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getAnimateMotionPrototype()
     * @generated
     */
    int ANIMATE_MOTION_PROTOTYPE = 1;

    /**
     * The feature id for the '<em><b>Accumulate</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_MOTION_PROTOTYPE__ACCUMULATE = 0;

    /**
     * The feature id for the '<em><b>Additive</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_MOTION_PROTOTYPE__ADDITIVE = 1;

    /**
     * The feature id for the '<em><b>By</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_MOTION_PROTOTYPE__BY = 2;

    /**
     * The feature id for the '<em><b>From</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_MOTION_PROTOTYPE__FROM = 3;

    /**
     * The feature id for the '<em><b>Origin</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_MOTION_PROTOTYPE__ORIGIN = 4;

    /**
     * The feature id for the '<em><b>To</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_MOTION_PROTOTYPE__TO = 5;

    /**
     * The feature id for the '<em><b>Values</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_MOTION_PROTOTYPE__VALUES = 6;

    /**
     * The number of structural features of the '<em>Animate Motion Prototype</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_MOTION_PROTOTYPE_FEATURE_COUNT = 7;

    /**
     * The number of operations of the '<em>Animate Motion Prototype</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_MOTION_PROTOTYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.w3._2001.smil20.impl.AnimatePrototypeImpl <em>Animate Prototype</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.w3._2001.smil20.impl.AnimatePrototypeImpl
     * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getAnimatePrototype()
     * @generated
     */
    int ANIMATE_PROTOTYPE = 2;

    /**
     * The feature id for the '<em><b>Accumulate</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_PROTOTYPE__ACCUMULATE = 0;

    /**
     * The feature id for the '<em><b>Additive</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_PROTOTYPE__ADDITIVE = 1;

    /**
     * The feature id for the '<em><b>Attribute Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_PROTOTYPE__ATTRIBUTE_NAME = 2;

    /**
     * The feature id for the '<em><b>Attribute Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_PROTOTYPE__ATTRIBUTE_TYPE = 3;

    /**
     * The feature id for the '<em><b>By</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_PROTOTYPE__BY = 4;

    /**
     * The feature id for the '<em><b>From</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_PROTOTYPE__FROM = 5;

    /**
     * The feature id for the '<em><b>To</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_PROTOTYPE__TO = 6;

    /**
     * The feature id for the '<em><b>Values</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_PROTOTYPE__VALUES = 7;

    /**
     * The number of structural features of the '<em>Animate Prototype</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_PROTOTYPE_FEATURE_COUNT = 8;

    /**
     * The number of operations of the '<em>Animate Prototype</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_PROTOTYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.w3._2001.smil20.impl.DocumentRootImpl <em>Document Root</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.w3._2001.smil20.impl.DocumentRootImpl
     * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getDocumentRoot()
     * @generated
     */
    int DOCUMENT_ROOT = 3;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__MIXED = 0;

    /**
     * The feature id for the '<em><b>XMLNS Prefix Map</b></em>' map.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__XMLNS_PREFIX_MAP = 1;

    /**
     * The feature id for the '<em><b>XSI Schema Location</b></em>' map.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = 2;

    /**
     * The feature id for the '<em><b>Animate</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__ANIMATE = 3;

    /**
     * The feature id for the '<em><b>Animate Color</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__ANIMATE_COLOR = 4;

    /**
     * The feature id for the '<em><b>Animate Motion</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__ANIMATE_MOTION = 5;

    /**
     * The feature id for the '<em><b>Set</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__SET = 6;

    /**
     * The number of structural features of the '<em>Document Root</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT_FEATURE_COUNT = 7;

    /**
     * The number of operations of the '<em>Document Root</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.w3._2001.smil20.impl.SetPrototypeImpl <em>Set Prototype</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.w3._2001.smil20.impl.SetPrototypeImpl
     * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getSetPrototype()
     * @generated
     */
    int SET_PROTOTYPE = 4;

    /**
     * The feature id for the '<em><b>Attribute Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_PROTOTYPE__ATTRIBUTE_NAME = 0;

    /**
     * The feature id for the '<em><b>Attribute Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_PROTOTYPE__ATTRIBUTE_TYPE = 1;

    /**
     * The feature id for the '<em><b>To</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_PROTOTYPE__TO = 2;

    /**
     * The number of structural features of the '<em>Set Prototype</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_PROTOTYPE_FEATURE_COUNT = 3;

    /**
     * The number of operations of the '<em>Set Prototype</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_PROTOTYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.w3._2001.smil20.AccumulateType <em>Accumulate Type</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.w3._2001.smil20.AccumulateType
     * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getAccumulateType()
     * @generated
     */
    int ACCUMULATE_TYPE = 5;

    /**
     * The meta object id for the '{@link org.w3._2001.smil20.AdditiveType <em>Additive Type</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.w3._2001.smil20.AdditiveType
     * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getAdditiveType()
     * @generated
     */
    int ADDITIVE_TYPE = 6;

    /**
     * The meta object id for the '{@link org.w3._2001.smil20.AttributeTypeType <em>Attribute Type Type</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.w3._2001.smil20.AttributeTypeType
     * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getAttributeTypeType()
     * @generated
     */
    int ATTRIBUTE_TYPE_TYPE = 7;

    /**
     * The meta object id for the '{@link org.w3._2001.smil20.CalcModeType <em>Calc Mode Type</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.w3._2001.smil20.CalcModeType
     * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getCalcModeType()
     * @generated
     */
    int CALC_MODE_TYPE = 8;

    /**
     * The meta object id for the '{@link org.w3._2001.smil20.FillDefaultType <em>Fill Default Type</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.w3._2001.smil20.FillDefaultType
     * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getFillDefaultType()
     * @generated
     */
    int FILL_DEFAULT_TYPE = 9;

    /**
     * The meta object id for the '{@link org.w3._2001.smil20.FillTimingAttrsType <em>Fill Timing Attrs Type</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.w3._2001.smil20.FillTimingAttrsType
     * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getFillTimingAttrsType()
     * @generated
     */
    int FILL_TIMING_ATTRS_TYPE = 10;

    /**
     * The meta object id for the '{@link org.w3._2001.smil20.RestartDefaultType <em>Restart Default Type</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.w3._2001.smil20.RestartDefaultType
     * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getRestartDefaultType()
     * @generated
     */
    int RESTART_DEFAULT_TYPE = 11;

    /**
     * The meta object id for the '{@link org.w3._2001.smil20.RestartTimingType <em>Restart Timing Type</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.w3._2001.smil20.RestartTimingType
     * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getRestartTimingType()
     * @generated
     */
    int RESTART_TIMING_TYPE = 12;

    /**
     * The meta object id for the '{@link org.w3._2001.smil20.SyncBehaviorDefaultType <em>Sync Behavior Default Type</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.w3._2001.smil20.SyncBehaviorDefaultType
     * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getSyncBehaviorDefaultType()
     * @generated
     */
    int SYNC_BEHAVIOR_DEFAULT_TYPE = 13;

    /**
     * The meta object id for the '{@link org.w3._2001.smil20.SyncBehaviorType <em>Sync Behavior Type</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.w3._2001.smil20.SyncBehaviorType
     * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getSyncBehaviorType()
     * @generated
     */
    int SYNC_BEHAVIOR_TYPE = 14;

    /**
     * The meta object id for the '<em>Accumulate Type Object</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.w3._2001.smil20.AccumulateType
     * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getAccumulateTypeObject()
     * @generated
     */
    int ACCUMULATE_TYPE_OBJECT = 15;

    /**
     * The meta object id for the '<em>Additive Type Object</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.w3._2001.smil20.AdditiveType
     * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getAdditiveTypeObject()
     * @generated
     */
    int ADDITIVE_TYPE_OBJECT = 16;

    /**
     * The meta object id for the '<em>Attribute Type Type Object</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.w3._2001.smil20.AttributeTypeType
     * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getAttributeTypeTypeObject()
     * @generated
     */
    int ATTRIBUTE_TYPE_TYPE_OBJECT = 17;

    /**
     * The meta object id for the '<em>Calc Mode Type Object</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.w3._2001.smil20.CalcModeType
     * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getCalcModeTypeObject()
     * @generated
     */
    int CALC_MODE_TYPE_OBJECT = 18;

    /**
     * The meta object id for the '<em>Fill Default Type Object</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.w3._2001.smil20.FillDefaultType
     * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getFillDefaultTypeObject()
     * @generated
     */
    int FILL_DEFAULT_TYPE_OBJECT = 19;

    /**
     * The meta object id for the '<em>Fill Timing Attrs Type Object</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.w3._2001.smil20.FillTimingAttrsType
     * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getFillTimingAttrsTypeObject()
     * @generated
     */
    int FILL_TIMING_ATTRS_TYPE_OBJECT = 20;

    /**
     * The meta object id for the '<em>Non Negative Decimal Type</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.math.BigDecimal
     * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getNonNegativeDecimalType()
     * @generated
     */
    int NON_NEGATIVE_DECIMAL_TYPE = 21;

    /**
     * The meta object id for the '<em>Restart Default Type Object</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.w3._2001.smil20.RestartDefaultType
     * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getRestartDefaultTypeObject()
     * @generated
     */
    int RESTART_DEFAULT_TYPE_OBJECT = 22;

    /**
     * The meta object id for the '<em>Restart Timing Type Object</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.w3._2001.smil20.RestartTimingType
     * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getRestartTimingTypeObject()
     * @generated
     */
    int RESTART_TIMING_TYPE_OBJECT = 23;

    /**
     * The meta object id for the '<em>Sync Behavior Default Type Object</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.w3._2001.smil20.SyncBehaviorDefaultType
     * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getSyncBehaviorDefaultTypeObject()
     * @generated
     */
    int SYNC_BEHAVIOR_DEFAULT_TYPE_OBJECT = 24;

    /**
     * The meta object id for the '<em>Sync Behavior Type Object</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.w3._2001.smil20.SyncBehaviorType
     * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getSyncBehaviorTypeObject()
     * @generated
     */
    int SYNC_BEHAVIOR_TYPE_OBJECT = 25;


    /**
     * Returns the meta object for class '{@link org.w3._2001.smil20.AnimateColorPrototype <em>Animate Color Prototype</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Animate Color Prototype</em>'.
     * @see org.w3._2001.smil20.AnimateColorPrototype
     * @generated
     */
    EClass getAnimateColorPrototype();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.AnimateColorPrototype#getAccumulate <em>Accumulate</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Accumulate</em>'.
     * @see org.w3._2001.smil20.AnimateColorPrototype#getAccumulate()
     * @see #getAnimateColorPrototype()
     * @generated
     */
    EAttribute getAnimateColorPrototype_Accumulate();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.AnimateColorPrototype#getAdditive <em>Additive</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Additive</em>'.
     * @see org.w3._2001.smil20.AnimateColorPrototype#getAdditive()
     * @see #getAnimateColorPrototype()
     * @generated
     */
    EAttribute getAnimateColorPrototype_Additive();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.AnimateColorPrototype#getAttributeName <em>Attribute Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Attribute Name</em>'.
     * @see org.w3._2001.smil20.AnimateColorPrototype#getAttributeName()
     * @see #getAnimateColorPrototype()
     * @generated
     */
    EAttribute getAnimateColorPrototype_AttributeName();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.AnimateColorPrototype#getAttributeType <em>Attribute Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Attribute Type</em>'.
     * @see org.w3._2001.smil20.AnimateColorPrototype#getAttributeType()
     * @see #getAnimateColorPrototype()
     * @generated
     */
    EAttribute getAnimateColorPrototype_AttributeType();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.AnimateColorPrototype#getBy <em>By</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>By</em>'.
     * @see org.w3._2001.smil20.AnimateColorPrototype#getBy()
     * @see #getAnimateColorPrototype()
     * @generated
     */
    EAttribute getAnimateColorPrototype_By();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.AnimateColorPrototype#getFrom <em>From</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>From</em>'.
     * @see org.w3._2001.smil20.AnimateColorPrototype#getFrom()
     * @see #getAnimateColorPrototype()
     * @generated
     */
    EAttribute getAnimateColorPrototype_From();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.AnimateColorPrototype#getTo <em>To</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>To</em>'.
     * @see org.w3._2001.smil20.AnimateColorPrototype#getTo()
     * @see #getAnimateColorPrototype()
     * @generated
     */
    EAttribute getAnimateColorPrototype_To();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.AnimateColorPrototype#getValues <em>Values</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Values</em>'.
     * @see org.w3._2001.smil20.AnimateColorPrototype#getValues()
     * @see #getAnimateColorPrototype()
     * @generated
     */
    EAttribute getAnimateColorPrototype_Values();

    /**
     * Returns the meta object for class '{@link org.w3._2001.smil20.AnimateMotionPrototype <em>Animate Motion Prototype</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Animate Motion Prototype</em>'.
     * @see org.w3._2001.smil20.AnimateMotionPrototype
     * @generated
     */
    EClass getAnimateMotionPrototype();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.AnimateMotionPrototype#getAccumulate <em>Accumulate</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Accumulate</em>'.
     * @see org.w3._2001.smil20.AnimateMotionPrototype#getAccumulate()
     * @see #getAnimateMotionPrototype()
     * @generated
     */
    EAttribute getAnimateMotionPrototype_Accumulate();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.AnimateMotionPrototype#getAdditive <em>Additive</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Additive</em>'.
     * @see org.w3._2001.smil20.AnimateMotionPrototype#getAdditive()
     * @see #getAnimateMotionPrototype()
     * @generated
     */
    EAttribute getAnimateMotionPrototype_Additive();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.AnimateMotionPrototype#getBy <em>By</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>By</em>'.
     * @see org.w3._2001.smil20.AnimateMotionPrototype#getBy()
     * @see #getAnimateMotionPrototype()
     * @generated
     */
    EAttribute getAnimateMotionPrototype_By();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.AnimateMotionPrototype#getFrom <em>From</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>From</em>'.
     * @see org.w3._2001.smil20.AnimateMotionPrototype#getFrom()
     * @see #getAnimateMotionPrototype()
     * @generated
     */
    EAttribute getAnimateMotionPrototype_From();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.AnimateMotionPrototype#getOrigin <em>Origin</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Origin</em>'.
     * @see org.w3._2001.smil20.AnimateMotionPrototype#getOrigin()
     * @see #getAnimateMotionPrototype()
     * @generated
     */
    EAttribute getAnimateMotionPrototype_Origin();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.AnimateMotionPrototype#getTo <em>To</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>To</em>'.
     * @see org.w3._2001.smil20.AnimateMotionPrototype#getTo()
     * @see #getAnimateMotionPrototype()
     * @generated
     */
    EAttribute getAnimateMotionPrototype_To();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.AnimateMotionPrototype#getValues <em>Values</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Values</em>'.
     * @see org.w3._2001.smil20.AnimateMotionPrototype#getValues()
     * @see #getAnimateMotionPrototype()
     * @generated
     */
    EAttribute getAnimateMotionPrototype_Values();

    /**
     * Returns the meta object for class '{@link org.w3._2001.smil20.AnimatePrototype <em>Animate Prototype</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Animate Prototype</em>'.
     * @see org.w3._2001.smil20.AnimatePrototype
     * @generated
     */
    EClass getAnimatePrototype();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.AnimatePrototype#getAccumulate <em>Accumulate</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Accumulate</em>'.
     * @see org.w3._2001.smil20.AnimatePrototype#getAccumulate()
     * @see #getAnimatePrototype()
     * @generated
     */
    EAttribute getAnimatePrototype_Accumulate();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.AnimatePrototype#getAdditive <em>Additive</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Additive</em>'.
     * @see org.w3._2001.smil20.AnimatePrototype#getAdditive()
     * @see #getAnimatePrototype()
     * @generated
     */
    EAttribute getAnimatePrototype_Additive();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.AnimatePrototype#getAttributeName <em>Attribute Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Attribute Name</em>'.
     * @see org.w3._2001.smil20.AnimatePrototype#getAttributeName()
     * @see #getAnimatePrototype()
     * @generated
     */
    EAttribute getAnimatePrototype_AttributeName();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.AnimatePrototype#getAttributeType <em>Attribute Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Attribute Type</em>'.
     * @see org.w3._2001.smil20.AnimatePrototype#getAttributeType()
     * @see #getAnimatePrototype()
     * @generated
     */
    EAttribute getAnimatePrototype_AttributeType();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.AnimatePrototype#getBy <em>By</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>By</em>'.
     * @see org.w3._2001.smil20.AnimatePrototype#getBy()
     * @see #getAnimatePrototype()
     * @generated
     */
    EAttribute getAnimatePrototype_By();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.AnimatePrototype#getFrom <em>From</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>From</em>'.
     * @see org.w3._2001.smil20.AnimatePrototype#getFrom()
     * @see #getAnimatePrototype()
     * @generated
     */
    EAttribute getAnimatePrototype_From();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.AnimatePrototype#getTo <em>To</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>To</em>'.
     * @see org.w3._2001.smil20.AnimatePrototype#getTo()
     * @see #getAnimatePrototype()
     * @generated
     */
    EAttribute getAnimatePrototype_To();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.AnimatePrototype#getValues <em>Values</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Values</em>'.
     * @see org.w3._2001.smil20.AnimatePrototype#getValues()
     * @see #getAnimatePrototype()
     * @generated
     */
    EAttribute getAnimatePrototype_Values();

    /**
     * Returns the meta object for class '{@link org.w3._2001.smil20.DocumentRoot <em>Document Root</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Document Root</em>'.
     * @see org.w3._2001.smil20.DocumentRoot
     * @generated
     */
    EClass getDocumentRoot();

    /**
     * Returns the meta object for the attribute list '{@link org.w3._2001.smil20.DocumentRoot#getMixed <em>Mixed</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.w3._2001.smil20.DocumentRoot#getMixed()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_Mixed();

    /**
     * Returns the meta object for the map '{@link org.w3._2001.smil20.DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the map '<em>XMLNS Prefix Map</em>'.
     * @see org.w3._2001.smil20.DocumentRoot#getXMLNSPrefixMap()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_XMLNSPrefixMap();

    /**
     * Returns the meta object for the map '{@link org.w3._2001.smil20.DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the map '<em>XSI Schema Location</em>'.
     * @see org.w3._2001.smil20.DocumentRoot#getXSISchemaLocation()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_XSISchemaLocation();

    /**
     * Returns the meta object for the containment reference '{@link org.w3._2001.smil20.DocumentRoot#getAnimate <em>Animate</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Animate</em>'.
     * @see org.w3._2001.smil20.DocumentRoot#getAnimate()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Animate();

    /**
     * Returns the meta object for the containment reference '{@link org.w3._2001.smil20.DocumentRoot#getAnimateColor <em>Animate Color</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Animate Color</em>'.
     * @see org.w3._2001.smil20.DocumentRoot#getAnimateColor()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_AnimateColor();

    /**
     * Returns the meta object for the containment reference '{@link org.w3._2001.smil20.DocumentRoot#getAnimateMotion <em>Animate Motion</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Animate Motion</em>'.
     * @see org.w3._2001.smil20.DocumentRoot#getAnimateMotion()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_AnimateMotion();

    /**
     * Returns the meta object for the containment reference '{@link org.w3._2001.smil20.DocumentRoot#getSet <em>Set</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Set</em>'.
     * @see org.w3._2001.smil20.DocumentRoot#getSet()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Set();

    /**
     * Returns the meta object for class '{@link org.w3._2001.smil20.SetPrototype <em>Set Prototype</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Set Prototype</em>'.
     * @see org.w3._2001.smil20.SetPrototype
     * @generated
     */
    EClass getSetPrototype();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.SetPrototype#getAttributeName <em>Attribute Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Attribute Name</em>'.
     * @see org.w3._2001.smil20.SetPrototype#getAttributeName()
     * @see #getSetPrototype()
     * @generated
     */
    EAttribute getSetPrototype_AttributeName();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.SetPrototype#getAttributeType <em>Attribute Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Attribute Type</em>'.
     * @see org.w3._2001.smil20.SetPrototype#getAttributeType()
     * @see #getSetPrototype()
     * @generated
     */
    EAttribute getSetPrototype_AttributeType();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.SetPrototype#getTo <em>To</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>To</em>'.
     * @see org.w3._2001.smil20.SetPrototype#getTo()
     * @see #getSetPrototype()
     * @generated
     */
    EAttribute getSetPrototype_To();

    /**
     * Returns the meta object for enum '{@link org.w3._2001.smil20.AccumulateType <em>Accumulate Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>Accumulate Type</em>'.
     * @see org.w3._2001.smil20.AccumulateType
     * @generated
     */
    EEnum getAccumulateType();

    /**
     * Returns the meta object for enum '{@link org.w3._2001.smil20.AdditiveType <em>Additive Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>Additive Type</em>'.
     * @see org.w3._2001.smil20.AdditiveType
     * @generated
     */
    EEnum getAdditiveType();

    /**
     * Returns the meta object for enum '{@link org.w3._2001.smil20.AttributeTypeType <em>Attribute Type Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>Attribute Type Type</em>'.
     * @see org.w3._2001.smil20.AttributeTypeType
     * @generated
     */
    EEnum getAttributeTypeType();

    /**
     * Returns the meta object for enum '{@link org.w3._2001.smil20.CalcModeType <em>Calc Mode Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>Calc Mode Type</em>'.
     * @see org.w3._2001.smil20.CalcModeType
     * @generated
     */
    EEnum getCalcModeType();

    /**
     * Returns the meta object for enum '{@link org.w3._2001.smil20.FillDefaultType <em>Fill Default Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>Fill Default Type</em>'.
     * @see org.w3._2001.smil20.FillDefaultType
     * @generated
     */
    EEnum getFillDefaultType();

    /**
     * Returns the meta object for enum '{@link org.w3._2001.smil20.FillTimingAttrsType <em>Fill Timing Attrs Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>Fill Timing Attrs Type</em>'.
     * @see org.w3._2001.smil20.FillTimingAttrsType
     * @generated
     */
    EEnum getFillTimingAttrsType();

    /**
     * Returns the meta object for enum '{@link org.w3._2001.smil20.RestartDefaultType <em>Restart Default Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>Restart Default Type</em>'.
     * @see org.w3._2001.smil20.RestartDefaultType
     * @generated
     */
    EEnum getRestartDefaultType();

    /**
     * Returns the meta object for enum '{@link org.w3._2001.smil20.RestartTimingType <em>Restart Timing Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>Restart Timing Type</em>'.
     * @see org.w3._2001.smil20.RestartTimingType
     * @generated
     */
    EEnum getRestartTimingType();

    /**
     * Returns the meta object for enum '{@link org.w3._2001.smil20.SyncBehaviorDefaultType <em>Sync Behavior Default Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>Sync Behavior Default Type</em>'.
     * @see org.w3._2001.smil20.SyncBehaviorDefaultType
     * @generated
     */
    EEnum getSyncBehaviorDefaultType();

    /**
     * Returns the meta object for enum '{@link org.w3._2001.smil20.SyncBehaviorType <em>Sync Behavior Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>Sync Behavior Type</em>'.
     * @see org.w3._2001.smil20.SyncBehaviorType
     * @generated
     */
    EEnum getSyncBehaviorType();

    /**
     * Returns the meta object for data type '{@link org.w3._2001.smil20.AccumulateType <em>Accumulate Type Object</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Accumulate Type Object</em>'.
     * @see org.w3._2001.smil20.AccumulateType
     * @model instanceClass="org.w3._2001.smil20.AccumulateType"
     *        extendedMetaData="name='accumulate_._type:Object' baseType='accumulate_._type'"
     * @generated
     */
    EDataType getAccumulateTypeObject();

    /**
     * Returns the meta object for data type '{@link org.w3._2001.smil20.AdditiveType <em>Additive Type Object</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Additive Type Object</em>'.
     * @see org.w3._2001.smil20.AdditiveType
     * @model instanceClass="org.w3._2001.smil20.AdditiveType"
     *        extendedMetaData="name='additive_._type:Object' baseType='additive_._type'"
     * @generated
     */
    EDataType getAdditiveTypeObject();

    /**
     * Returns the meta object for data type '{@link org.w3._2001.smil20.AttributeTypeType <em>Attribute Type Type Object</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Attribute Type Type Object</em>'.
     * @see org.w3._2001.smil20.AttributeTypeType
     * @model instanceClass="org.w3._2001.smil20.AttributeTypeType"
     *        extendedMetaData="name='attributeType_._type:Object' baseType='attributeType_._type'"
     * @generated
     */
    EDataType getAttributeTypeTypeObject();

    /**
     * Returns the meta object for data type '{@link org.w3._2001.smil20.CalcModeType <em>Calc Mode Type Object</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Calc Mode Type Object</em>'.
     * @see org.w3._2001.smil20.CalcModeType
     * @model instanceClass="org.w3._2001.smil20.CalcModeType"
     *        extendedMetaData="name='calcMode_._type:Object' baseType='calcMode_._type'"
     * @generated
     */
    EDataType getCalcModeTypeObject();

    /**
     * Returns the meta object for data type '{@link org.w3._2001.smil20.FillDefaultType <em>Fill Default Type Object</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Fill Default Type Object</em>'.
     * @see org.w3._2001.smil20.FillDefaultType
     * @model instanceClass="org.w3._2001.smil20.FillDefaultType"
     *        extendedMetaData="name='fillDefaultType:Object' baseType='fillDefaultType'"
     * @generated
     */
    EDataType getFillDefaultTypeObject();

    /**
     * Returns the meta object for data type '{@link org.w3._2001.smil20.FillTimingAttrsType <em>Fill Timing Attrs Type Object</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Fill Timing Attrs Type Object</em>'.
     * @see org.w3._2001.smil20.FillTimingAttrsType
     * @model instanceClass="org.w3._2001.smil20.FillTimingAttrsType"
     *        extendedMetaData="name='fillTimingAttrsType:Object' baseType='fillTimingAttrsType'"
     * @generated
     */
    EDataType getFillTimingAttrsTypeObject();

    /**
     * Returns the meta object for data type '{@link java.math.BigDecimal <em>Non Negative Decimal Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Non Negative Decimal Type</em>'.
     * @see java.math.BigDecimal
     * @model instanceClass="java.math.BigDecimal"
     *        extendedMetaData="name='nonNegativeDecimalType' baseType='http://www.eclipse.org/emf/2003/XMLType#decimal' minInclusive='0.0'"
     * @generated
     */
    EDataType getNonNegativeDecimalType();

    /**
     * Returns the meta object for data type '{@link org.w3._2001.smil20.RestartDefaultType <em>Restart Default Type Object</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Restart Default Type Object</em>'.
     * @see org.w3._2001.smil20.RestartDefaultType
     * @model instanceClass="org.w3._2001.smil20.RestartDefaultType"
     *        extendedMetaData="name='restartDefaultType:Object' baseType='restartDefaultType'"
     * @generated
     */
    EDataType getRestartDefaultTypeObject();

    /**
     * Returns the meta object for data type '{@link org.w3._2001.smil20.RestartTimingType <em>Restart Timing Type Object</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Restart Timing Type Object</em>'.
     * @see org.w3._2001.smil20.RestartTimingType
     * @model instanceClass="org.w3._2001.smil20.RestartTimingType"
     *        extendedMetaData="name='restartTimingType:Object' baseType='restartTimingType'"
     * @generated
     */
    EDataType getRestartTimingTypeObject();

    /**
     * Returns the meta object for data type '{@link org.w3._2001.smil20.SyncBehaviorDefaultType <em>Sync Behavior Default Type Object</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Sync Behavior Default Type Object</em>'.
     * @see org.w3._2001.smil20.SyncBehaviorDefaultType
     * @model instanceClass="org.w3._2001.smil20.SyncBehaviorDefaultType"
     *        extendedMetaData="name='syncBehaviorDefaultType:Object' baseType='syncBehaviorDefaultType'"
     * @generated
     */
    EDataType getSyncBehaviorDefaultTypeObject();

    /**
     * Returns the meta object for data type '{@link org.w3._2001.smil20.SyncBehaviorType <em>Sync Behavior Type Object</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Sync Behavior Type Object</em>'.
     * @see org.w3._2001.smil20.SyncBehaviorType
     * @model instanceClass="org.w3._2001.smil20.SyncBehaviorType"
     *        extendedMetaData="name='syncBehaviorType:Object' baseType='syncBehaviorType'"
     * @generated
     */
    EDataType getSyncBehaviorTypeObject();

    /**
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    Smil20Factory getSmil20Factory();

    /**
     * <!-- begin-user-doc -->
     * Defines literals for the meta objects that represent
     * <ul>
     *   <li>each class,</li>
     *   <li>each feature of each class,</li>
     *   <li>each operation of each class,</li>
     *   <li>each enum,</li>
     *   <li>and each data type</li>
     * </ul>
     * <!-- end-user-doc -->
     * @generated
     */
    interface Literals {
        /**
         * The meta object literal for the '{@link org.w3._2001.smil20.impl.AnimateColorPrototypeImpl <em>Animate Color Prototype</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.w3._2001.smil20.impl.AnimateColorPrototypeImpl
         * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getAnimateColorPrototype()
         * @generated
         */
        EClass ANIMATE_COLOR_PROTOTYPE = eINSTANCE.getAnimateColorPrototype();

        /**
         * The meta object literal for the '<em><b>Accumulate</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_COLOR_PROTOTYPE__ACCUMULATE = eINSTANCE.getAnimateColorPrototype_Accumulate();

        /**
         * The meta object literal for the '<em><b>Additive</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_COLOR_PROTOTYPE__ADDITIVE = eINSTANCE.getAnimateColorPrototype_Additive();

        /**
         * The meta object literal for the '<em><b>Attribute Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_COLOR_PROTOTYPE__ATTRIBUTE_NAME = eINSTANCE.getAnimateColorPrototype_AttributeName();

        /**
         * The meta object literal for the '<em><b>Attribute Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_COLOR_PROTOTYPE__ATTRIBUTE_TYPE = eINSTANCE.getAnimateColorPrototype_AttributeType();

        /**
         * The meta object literal for the '<em><b>By</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_COLOR_PROTOTYPE__BY = eINSTANCE.getAnimateColorPrototype_By();

        /**
         * The meta object literal for the '<em><b>From</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_COLOR_PROTOTYPE__FROM = eINSTANCE.getAnimateColorPrototype_From();

        /**
         * The meta object literal for the '<em><b>To</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_COLOR_PROTOTYPE__TO = eINSTANCE.getAnimateColorPrototype_To();

        /**
         * The meta object literal for the '<em><b>Values</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_COLOR_PROTOTYPE__VALUES = eINSTANCE.getAnimateColorPrototype_Values();

        /**
         * The meta object literal for the '{@link org.w3._2001.smil20.impl.AnimateMotionPrototypeImpl <em>Animate Motion Prototype</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.w3._2001.smil20.impl.AnimateMotionPrototypeImpl
         * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getAnimateMotionPrototype()
         * @generated
         */
        EClass ANIMATE_MOTION_PROTOTYPE = eINSTANCE.getAnimateMotionPrototype();

        /**
         * The meta object literal for the '<em><b>Accumulate</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_MOTION_PROTOTYPE__ACCUMULATE = eINSTANCE.getAnimateMotionPrototype_Accumulate();

        /**
         * The meta object literal for the '<em><b>Additive</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_MOTION_PROTOTYPE__ADDITIVE = eINSTANCE.getAnimateMotionPrototype_Additive();

        /**
         * The meta object literal for the '<em><b>By</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_MOTION_PROTOTYPE__BY = eINSTANCE.getAnimateMotionPrototype_By();

        /**
         * The meta object literal for the '<em><b>From</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_MOTION_PROTOTYPE__FROM = eINSTANCE.getAnimateMotionPrototype_From();

        /**
         * The meta object literal for the '<em><b>Origin</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_MOTION_PROTOTYPE__ORIGIN = eINSTANCE.getAnimateMotionPrototype_Origin();

        /**
         * The meta object literal for the '<em><b>To</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_MOTION_PROTOTYPE__TO = eINSTANCE.getAnimateMotionPrototype_To();

        /**
         * The meta object literal for the '<em><b>Values</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_MOTION_PROTOTYPE__VALUES = eINSTANCE.getAnimateMotionPrototype_Values();

        /**
         * The meta object literal for the '{@link org.w3._2001.smil20.impl.AnimatePrototypeImpl <em>Animate Prototype</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.w3._2001.smil20.impl.AnimatePrototypeImpl
         * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getAnimatePrototype()
         * @generated
         */
        EClass ANIMATE_PROTOTYPE = eINSTANCE.getAnimatePrototype();

        /**
         * The meta object literal for the '<em><b>Accumulate</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_PROTOTYPE__ACCUMULATE = eINSTANCE.getAnimatePrototype_Accumulate();

        /**
         * The meta object literal for the '<em><b>Additive</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_PROTOTYPE__ADDITIVE = eINSTANCE.getAnimatePrototype_Additive();

        /**
         * The meta object literal for the '<em><b>Attribute Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_PROTOTYPE__ATTRIBUTE_NAME = eINSTANCE.getAnimatePrototype_AttributeName();

        /**
         * The meta object literal for the '<em><b>Attribute Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_PROTOTYPE__ATTRIBUTE_TYPE = eINSTANCE.getAnimatePrototype_AttributeType();

        /**
         * The meta object literal for the '<em><b>By</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_PROTOTYPE__BY = eINSTANCE.getAnimatePrototype_By();

        /**
         * The meta object literal for the '<em><b>From</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_PROTOTYPE__FROM = eINSTANCE.getAnimatePrototype_From();

        /**
         * The meta object literal for the '<em><b>To</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_PROTOTYPE__TO = eINSTANCE.getAnimatePrototype_To();

        /**
         * The meta object literal for the '<em><b>Values</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_PROTOTYPE__VALUES = eINSTANCE.getAnimatePrototype_Values();

        /**
         * The meta object literal for the '{@link org.w3._2001.smil20.impl.DocumentRootImpl <em>Document Root</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.w3._2001.smil20.impl.DocumentRootImpl
         * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getDocumentRoot()
         * @generated
         */
        EClass DOCUMENT_ROOT = eINSTANCE.getDocumentRoot();

        /**
         * The meta object literal for the '<em><b>Mixed</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOCUMENT_ROOT__MIXED = eINSTANCE.getDocumentRoot_Mixed();

        /**
         * The meta object literal for the '<em><b>XMLNS Prefix Map</b></em>' map feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__XMLNS_PREFIX_MAP = eINSTANCE.getDocumentRoot_XMLNSPrefixMap();

        /**
         * The meta object literal for the '<em><b>XSI Schema Location</b></em>' map feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = eINSTANCE.getDocumentRoot_XSISchemaLocation();

        /**
         * The meta object literal for the '<em><b>Animate</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__ANIMATE = eINSTANCE.getDocumentRoot_Animate();

        /**
         * The meta object literal for the '<em><b>Animate Color</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__ANIMATE_COLOR = eINSTANCE.getDocumentRoot_AnimateColor();

        /**
         * The meta object literal for the '<em><b>Animate Motion</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__ANIMATE_MOTION = eINSTANCE.getDocumentRoot_AnimateMotion();

        /**
         * The meta object literal for the '<em><b>Set</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__SET = eINSTANCE.getDocumentRoot_Set();

        /**
         * The meta object literal for the '{@link org.w3._2001.smil20.impl.SetPrototypeImpl <em>Set Prototype</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.w3._2001.smil20.impl.SetPrototypeImpl
         * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getSetPrototype()
         * @generated
         */
        EClass SET_PROTOTYPE = eINSTANCE.getSetPrototype();

        /**
         * The meta object literal for the '<em><b>Attribute Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SET_PROTOTYPE__ATTRIBUTE_NAME = eINSTANCE.getSetPrototype_AttributeName();

        /**
         * The meta object literal for the '<em><b>Attribute Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SET_PROTOTYPE__ATTRIBUTE_TYPE = eINSTANCE.getSetPrototype_AttributeType();

        /**
         * The meta object literal for the '<em><b>To</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SET_PROTOTYPE__TO = eINSTANCE.getSetPrototype_To();

        /**
         * The meta object literal for the '{@link org.w3._2001.smil20.AccumulateType <em>Accumulate Type</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.w3._2001.smil20.AccumulateType
         * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getAccumulateType()
         * @generated
         */
        EEnum ACCUMULATE_TYPE = eINSTANCE.getAccumulateType();

        /**
         * The meta object literal for the '{@link org.w3._2001.smil20.AdditiveType <em>Additive Type</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.w3._2001.smil20.AdditiveType
         * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getAdditiveType()
         * @generated
         */
        EEnum ADDITIVE_TYPE = eINSTANCE.getAdditiveType();

        /**
         * The meta object literal for the '{@link org.w3._2001.smil20.AttributeTypeType <em>Attribute Type Type</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.w3._2001.smil20.AttributeTypeType
         * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getAttributeTypeType()
         * @generated
         */
        EEnum ATTRIBUTE_TYPE_TYPE = eINSTANCE.getAttributeTypeType();

        /**
         * The meta object literal for the '{@link org.w3._2001.smil20.CalcModeType <em>Calc Mode Type</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.w3._2001.smil20.CalcModeType
         * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getCalcModeType()
         * @generated
         */
        EEnum CALC_MODE_TYPE = eINSTANCE.getCalcModeType();

        /**
         * The meta object literal for the '{@link org.w3._2001.smil20.FillDefaultType <em>Fill Default Type</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.w3._2001.smil20.FillDefaultType
         * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getFillDefaultType()
         * @generated
         */
        EEnum FILL_DEFAULT_TYPE = eINSTANCE.getFillDefaultType();

        /**
         * The meta object literal for the '{@link org.w3._2001.smil20.FillTimingAttrsType <em>Fill Timing Attrs Type</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.w3._2001.smil20.FillTimingAttrsType
         * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getFillTimingAttrsType()
         * @generated
         */
        EEnum FILL_TIMING_ATTRS_TYPE = eINSTANCE.getFillTimingAttrsType();

        /**
         * The meta object literal for the '{@link org.w3._2001.smil20.RestartDefaultType <em>Restart Default Type</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.w3._2001.smil20.RestartDefaultType
         * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getRestartDefaultType()
         * @generated
         */
        EEnum RESTART_DEFAULT_TYPE = eINSTANCE.getRestartDefaultType();

        /**
         * The meta object literal for the '{@link org.w3._2001.smil20.RestartTimingType <em>Restart Timing Type</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.w3._2001.smil20.RestartTimingType
         * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getRestartTimingType()
         * @generated
         */
        EEnum RESTART_TIMING_TYPE = eINSTANCE.getRestartTimingType();

        /**
         * The meta object literal for the '{@link org.w3._2001.smil20.SyncBehaviorDefaultType <em>Sync Behavior Default Type</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.w3._2001.smil20.SyncBehaviorDefaultType
         * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getSyncBehaviorDefaultType()
         * @generated
         */
        EEnum SYNC_BEHAVIOR_DEFAULT_TYPE = eINSTANCE.getSyncBehaviorDefaultType();

        /**
         * The meta object literal for the '{@link org.w3._2001.smil20.SyncBehaviorType <em>Sync Behavior Type</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.w3._2001.smil20.SyncBehaviorType
         * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getSyncBehaviorType()
         * @generated
         */
        EEnum SYNC_BEHAVIOR_TYPE = eINSTANCE.getSyncBehaviorType();

        /**
         * The meta object literal for the '<em>Accumulate Type Object</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.w3._2001.smil20.AccumulateType
         * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getAccumulateTypeObject()
         * @generated
         */
        EDataType ACCUMULATE_TYPE_OBJECT = eINSTANCE.getAccumulateTypeObject();

        /**
         * The meta object literal for the '<em>Additive Type Object</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.w3._2001.smil20.AdditiveType
         * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getAdditiveTypeObject()
         * @generated
         */
        EDataType ADDITIVE_TYPE_OBJECT = eINSTANCE.getAdditiveTypeObject();

        /**
         * The meta object literal for the '<em>Attribute Type Type Object</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.w3._2001.smil20.AttributeTypeType
         * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getAttributeTypeTypeObject()
         * @generated
         */
        EDataType ATTRIBUTE_TYPE_TYPE_OBJECT = eINSTANCE.getAttributeTypeTypeObject();

        /**
         * The meta object literal for the '<em>Calc Mode Type Object</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.w3._2001.smil20.CalcModeType
         * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getCalcModeTypeObject()
         * @generated
         */
        EDataType CALC_MODE_TYPE_OBJECT = eINSTANCE.getCalcModeTypeObject();

        /**
         * The meta object literal for the '<em>Fill Default Type Object</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.w3._2001.smil20.FillDefaultType
         * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getFillDefaultTypeObject()
         * @generated
         */
        EDataType FILL_DEFAULT_TYPE_OBJECT = eINSTANCE.getFillDefaultTypeObject();

        /**
         * The meta object literal for the '<em>Fill Timing Attrs Type Object</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.w3._2001.smil20.FillTimingAttrsType
         * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getFillTimingAttrsTypeObject()
         * @generated
         */
        EDataType FILL_TIMING_ATTRS_TYPE_OBJECT = eINSTANCE.getFillTimingAttrsTypeObject();

        /**
         * The meta object literal for the '<em>Non Negative Decimal Type</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.math.BigDecimal
         * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getNonNegativeDecimalType()
         * @generated
         */
        EDataType NON_NEGATIVE_DECIMAL_TYPE = eINSTANCE.getNonNegativeDecimalType();

        /**
         * The meta object literal for the '<em>Restart Default Type Object</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.w3._2001.smil20.RestartDefaultType
         * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getRestartDefaultTypeObject()
         * @generated
         */
        EDataType RESTART_DEFAULT_TYPE_OBJECT = eINSTANCE.getRestartDefaultTypeObject();

        /**
         * The meta object literal for the '<em>Restart Timing Type Object</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.w3._2001.smil20.RestartTimingType
         * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getRestartTimingTypeObject()
         * @generated
         */
        EDataType RESTART_TIMING_TYPE_OBJECT = eINSTANCE.getRestartTimingTypeObject();

        /**
         * The meta object literal for the '<em>Sync Behavior Default Type Object</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.w3._2001.smil20.SyncBehaviorDefaultType
         * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getSyncBehaviorDefaultTypeObject()
         * @generated
         */
        EDataType SYNC_BEHAVIOR_DEFAULT_TYPE_OBJECT = eINSTANCE.getSyncBehaviorDefaultTypeObject();

        /**
         * The meta object literal for the '<em>Sync Behavior Type Object</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.w3._2001.smil20.SyncBehaviorType
         * @see org.w3._2001.smil20.impl.Smil20PackageImpl#getSyncBehaviorTypeObject()
         * @generated
         */
        EDataType SYNC_BEHAVIOR_TYPE_OBJECT = eINSTANCE.getSyncBehaviorTypeObject();

    }

} //Smil20Package
