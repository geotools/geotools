package org.geotools.data.efeature;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.geotools.data.efeature.internal.EFeatureVoidIDFactory;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.NameImpl;
import org.geotools.feature.type.AttributeTypeImpl;
import org.geotools.feature.type.Types;
import org.opengis.feature.Attribute;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;

/**
 * 
 * @author kengu
 *
 */
@SuppressWarnings("deprecation")
public class EFeatureAttributeInfo extends EStructureInfo<EFeatureInfo> {
    
    protected String eName;
    
    protected String eNsURI;
    
    protected String eFolderName;
    
    protected String eFeatureName;
        
    protected boolean eIsID;

    protected WeakReference<EAttribute> eAttribute;

    protected Map<Object, Object> userData;

    private AttributeDescriptorDelegate eDescriptor;
    
    // ----------------------------------------------------- 
    //  Constructors
    // -----------------------------------------------------

    /**
     * Default constructor
     */
    protected EFeatureAttributeInfo() { /*NOP*/ }
    
    /**
     * Structure copy constructor.
     * <p>
     * This method copies the structure into given context. 
     * </p>
     * <b>NOTE</b>: This method only adds a one-way reference from 
     * copied instance to given {@link EFeatureContext context}. 
     * No reference is added from the context to this attribute. 
     * </p>  
     * @param eAttributeInfo - copy from this {@link EFeatureAttributeInfo} instance
     * @param eFeatureInfo - copy into this structure
     */
    protected EFeatureAttributeInfo(EFeatureAttributeInfo eAttributeInfo, EFeatureInfo eFeatureInfo) {
        //
        // Forward (copies context, state and hints)
        //
        super(eAttributeInfo, eFeatureInfo);        
        //
        // Copy context path
        //
        this.eNsURI = eAttributeInfo.eNsURI;
        this.eFolderName = eAttributeInfo.eFolderName;
        this.eFeatureName = eAttributeInfo.eName();        
        //
        // Copy attribute 
        //
        this.eName = eAttributeInfo.eName;
        this.eAttribute = new WeakReference<EAttribute>(eAttributeInfo.eAttribute());
        //
        // Copy other attributes 
        //
        this.eIsID = eAttributeInfo.eIsID;
        this.isAvailable = eAttributeInfo.isAvailable;
    }

    // ----------------------------------------------------- 
    //  EFeatureAttribute methods
    // -----------------------------------------------------
    
    public boolean isID() {
        return eIsID;
    }

    public String eName() {
        return eName;
    }
    
    public String eNsURI() {
        return eNsURI;
    }
    
    public String eFolderName() {
        return eFolderName;
    }
    
    public String eFeatureName() {
        return eFeatureName;
    }    
    
    @Override
    protected EFeatureInfo eParentInfo(boolean checkIsValid) {
        //
        // Get EFeatureContext structure
        //
        EFeatureContextInfo eContextInfo = eContext(checkIsValid).eStructure();
        //
        // Try complete structure first
        //
        EFeaturePackageInfo ePackageInfo = eContextInfo.
            eGetPackageInfo(eNsURI);
        //
        // Full structure exists?
        //
        if(ePackageInfo!=null) {
            return ePackageInfo.
                eGetFolderInfo(eFolderName).
                    eGetFeatureInfo(eFeatureName);
        }
        //
        // No it dosn't, try EFeature cache
        //
        return eContextInfo.eFeatureInfoCache().get(eNsURI, eFolderName, eFeatureName);        
    }

    public EAttribute eAttribute() {
        return eAttribute!=null ? eAttribute.get() : null;
    }

    public AttributeDescriptor getDescriptor() {
        if (isAvailable() && eDescriptor == null) {
            eDescriptor = new AttributeDescriptorDelegate();
        }
        return eDescriptor;
    }

    public EFeatureStatus validate(boolean isID, EAttribute eAttribute) {
        //
        // Invalidate structure
        //
        doInvalidate(false);
        //
        // 1) Verify that reference information exist
        //
        if (this.eName == null) {
            return failure(this, eName(), "Attribute mismatch: EAttribute name not specified");
        }
        //
        // 2) Verify attribute name
        //
        String eName = eAttribute.getName();
        if (!this.eName.equals(eName)) {
            return failure(this, eName(), "Attribute mismatch: Found + " + eName + ", expected " + this.eName);
        }
        //
        // Get feature structure
        //
        EFeatureInfo eInfo = eParentInfo(false);
        //
        // 3) Verify that the EFeatureID factory instance create IDs for given attribute?
        //
        //
        if(isID && eInfo!=null) {
            //
            // Get EFeatureIDFactory instance
            //
            EFeatureIDFactory eFactory = eContext(false).eIDFactory();
            //
            // Verify that the EFeatureID factory instance exists and 
            // creates IDs for this attribute
            //
            if(eFactory==null) {
                return failure(this, eName(), "Attribute mismatch: " +
                        "No EFeatureIDFactory found");            
            }
            else if(!(eFactory instanceof EFeatureVoidIDFactory || eFactory.creates(eAttribute))) {
                return failure(this, eName(), "Attribute mismatch: " +
                		"EFeatureIDFactory does not create IDs for attribute " + eName);
            }
        }
        //
        // Set valid state
        //
        this.eIsID = isID;

        // Confirm that structure is valid
        //
        return structureIsValid(eName());

    }

    /**
     * Validate attribute against this structure.
     * 
     * @param attribute - attribute to be validated
     * @return <code>true</code> if valid.
     */
    public EFeatureStatus validate(Attribute attribute) {
        // TODO: Better validation against information
        if (!getDescriptor().equals(attribute.getDescriptor())) {
            return attributeIsInvalid();
        }
        // Confirm that attribute is valid
        //
        return attributeIsValid();
    }

    /**
     * Validate {@link Property#getValue() attribute value} against this structure.
     * 
     * @param value - attribute value to be validated
     * @return <code>true</code> if valid.
     */
    public EFeatureStatus validate(Object value) {
        try {
            // Validate value type
            //
            Types.validate(getDescriptor(), value);
        } catch (IllegalAttributeException e) {
            return valueIsInvalid(e);
        }

        // Confirm that attribute value is valid
        //
        return valueIsValid();
    }

    // ----------------------------------------------------- 
    //  EStructureInfo implementation
    // -----------------------------------------------------

    @Override
    protected void doInvalidate(boolean deep) { 
        this.eIsID = false;
    }

    @Override
    protected void doDispose() {        
        userData.clear();        
        userData = null;
        eAttribute = null;
        eDescriptor = null;        
    }

    // ----------------------------------------------------- 
    //  AttributeDescriptor implementation
    // -----------------------------------------------------

    protected class AttributeDescriptorDelegate implements AttributeDescriptor {
        private AttributeType attributeType;

        public Name getName() {
            return new NameImpl(eName);
        }

        public int getMinOccurs() {
            return eAttribute == null ? -1 : limit(eAttribute().getLowerBound());
        }

        public int getMaxOccurs() {
            return eAttribute == null ? -1 : limit(eAttribute().getUpperBound());
        }

        public boolean isNillable() {
            return eAttribute == null ? false : !eAttribute().isRequired();
        }

        public Map<Object, Object> getUserData() {
            if (userData == null) {
                userData = new HashMap<Object, Object>();
            }
            return userData;
        }

        public AttributeType getType() {
            if (isAvailable() && attributeType == null) {
                //
                // Get value instance class
                //
                final Class<?> cls = eAttribute().getEAttributeType().getInstanceClass();
                //
                // Create anonymous attribute type implementation
                //
                attributeType = new AttributeTypeImpl(getName(), cls, eIsID, false,
                        Collections.<Filter> emptyList(), null, null) {
                    @Override
                    public Object parse(Object value) throws IllegalArgumentException {
                        return DataBuilder.parse(cls, value);
                    }
                };

            }
            return attributeType;
        }

        public String getLocalName() {
            return getName().getLocalPart();
        }

        public Object getDefaultValue() {
            return null;
        }
    }

    // ----------------------------------------------------- 
    //  Protected EFeatureAttributeInfo helper methods
    // -----------------------------------------------------
    
    // ----------------------------------------------------- 
    //  Helper methods
    // -----------------------------------------------------
    
    /**
     * Create {@link EFeatureAttributeInfo} from given {@link EAttribute}
     * @param eFactory
     * @param eContextID
     * @param isID
     * @param eAttribute
     * @return
     */
    protected static EFeatureAttributeInfo create(
            EFeatureInfo eFeatureInfo, boolean isID, EAttribute eAttribute) {
        
        //
        // Create new instance
        //
        EFeatureAttributeInfo eInfo = new EFeatureAttributeInfo();
        //
        // Set construction hints
        //
        eInfo.eHints = eFeatureInfo.eHints;
        //
        // Set context
        //
        eInfo.eFactory = eFeatureInfo.eFactory;
        eInfo.eContext = eFeatureInfo.eContext;
        eInfo.eContextID = eFeatureInfo.eContextID;
        //
        // Set context path
        //
        eInfo.eNsURI = eFeatureInfo.eNsURI;
        eInfo.eFolderName = eFeatureInfo.eFolderName;
        eInfo.eFeatureName = eFeatureInfo.eName();        
        //
        // Set other members
        //
        eInfo.eIsID = isID;
        eInfo.eName = eAttribute.getName();
        eInfo.eAttribute = new WeakReference<EAttribute>(eAttribute);
        //
        // Finished
        //
        return eInfo;
    }

    protected static int limit(int value) {
        return value > 0 ? value : -1;
    }

    protected final EFeatureStatus attributeIsValid() {
        StackTraceElement trace = EFeatureUtils.getStackTraceElement(0,1);
        return success("Attribute is valid: " 
                + trace.getClassName()+ "[" + eName() + "]#" + trace.getMethodName());
    }

    protected final EFeatureStatus attributeIsInvalid() {
        StackTraceElement trace = EFeatureUtils.getStackTraceElement(0,1);
        return failure(this, eName(), "Attribute is invalid: "
                + trace.getClassName()+ "[" + eName() + "]#" + trace.getMethodName());
    }

    protected final EFeatureStatus valueIsValid() {
        StackTraceElement trace = EFeatureUtils.getStackTraceElement(0,1);
        return success("Attribute value is valid: " 
                + trace.getClassName()+ "[" + eName() + "]#" + trace.getMethodName());
    }

    protected final EFeatureStatus valueIsInvalid(Throwable cause) {
        StackTraceElement trace = EFeatureUtils.getStackTraceElement(0,1);
        return failure(this, eName(), "Attribute value is invalid: " 
                + trace.getClassName()+ "[" + eName() + "]#" + trace.getMethodName());
    }

}
