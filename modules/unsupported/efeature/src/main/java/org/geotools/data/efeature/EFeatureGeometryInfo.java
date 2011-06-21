package org.geotools.data.efeature;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.geotools.feature.type.AttributeTypeImpl;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.GeometryType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.util.InternationalString;

import com.vividsolutions.jts.geom.Geometry;

/**
 * This class defines a {@link EFeature} {@link Geometry geometry} {@link EAttribute attribute}.
 *   
 * @author kengu
 *
 */
public class EFeatureGeometryInfo extends EFeatureAttributeInfo {
    
    protected String srid;

    protected String geometryClassName;

    protected boolean isDefaultGeometry;

    protected CoordinateReferenceSystem crs;

    private GeometryDescriptorDelegate descriptor;
    
 // ----------------------------------------------------- 
    //  Constructors
    // -----------------------------------------------------

    /**
     * Default constructor
     */
    protected EFeatureGeometryInfo() {
        super();
    }
    
    /**
     * Structure copy constructor.
     * <p>
     * This method copies the structure into given context. 
     * </p>
     * <b>NOTE</b>: This method only adds a one-way reference from 
     * copied instance to given {@link EFeatureContext context}. 
     * No reference is added from the context to this attribute. 
     * </p>  
     * @param eGeometryInfo - copy from this {@link EFeatureGeometryInfo} instance
     * @param eFeatureInfo - copy into this structure
     */
    protected EFeatureGeometryInfo(EFeatureGeometryInfo eGeometryInfo, EFeatureInfo eFeatureInfo) {
        //
        // Forward to EFeatureAttributeInfo
        //
        super(eGeometryInfo, eFeatureInfo);
        //
        // Set geometry members 
        //
        this.crs = eGeometryInfo.crs;
        this.srid = eGeometryInfo.srid;
        this.geometryClassName = eGeometryInfo.geometryClassName;
        this.isDefaultGeometry = eGeometryInfo.isDefaultGeometry;
    }    
    
    // ----------------------------------------------------- 
    //  EFeatureGeometryInfo methods
    // -----------------------------------------------------

    public String getSRID() {
        return srid;
    }

    public boolean isDefaultGeometry() {
        return isDefaultGeometry;
    }

    /**
     * Get name of class extending {@link Geometry}.
     * 
     * @return a {@link Geometry} name.
     */
    public String getGeometryClassName() {
        return geometryClassName;
    }

    @Override
    public GeometryDescriptor getDescriptor() {
        if (isAvailable() && descriptor == null) {
            descriptor = new GeometryDescriptorDelegate();
        }
        return descriptor;
    }    
    
    @Override
    public EFeatureStatus validate(boolean isID, EAttribute eAttribute) {
        //
        // Initialize
        //
        EFeatureStatus s;
        //
        // Invalidate structure
        //
        doInvalidate(false);

        // Forward to super class
        //
        if ((s = super.validate(isID, eAttribute)).isSuccess()) {
            // 1) Verify geometry type
            //
            String eType = eAttribute.getEType().getInstanceClassName();
            if (this.geometryClassName != eType) {
                return failure(this, eName(), "Geometry type mismatch: Found + " + eType + ", expected "
                        + this.geometryClassName);
            }

            // Confirm that structure is valid
            //
            return structureIsValid(eName());

        }

        // Invalidate structure again.
        //
        doInvalidate(false);

        // Is invalid
        //
        return s;

    }

    //-----------------------------------------------------
    //  Methods for staying in-sync with EFeatureInfo parent 
    // -----------------------------------------------------

    protected void setSRID(String srid, CoordinateReferenceSystem crs) {
        this.crs = crs;
        this.srid = srid;
    }

    protected void setIsDefaultGeometry(boolean isDefault) {
        this.isDefaultGeometry = isDefault;
    }

    // ----------------------------------------------------- 
    //  EStructureInfo implementation
    // -----------------------------------------------------

    @Override
    protected void doDispose() {
        super.doDispose();
        descriptor = null;
        crs = null;
    }

    // ----------------------------------------------------- 
    //  GeometryDescriptor implementation
    // -----------------------------------------------------

    protected class GeometryDescriptorDelegate extends AttributeDescriptorDelegate implements
            GeometryDescriptor {
        private GeometryType type;

        @Override
        public GeometryType getType() {
            if (isAvailable() && type == null) {
                // Get value instance class
                //
                final Class<?> cls = eAttribute().getEAttributeType().getInstanceClass();

                // Create anonymous attribute type implementation
                //
                type = new GeometryTypeDelegate(getName(), cls, eIsID, false,
                        Collections.<Filter> emptyList(), null, null);

            }
            return type;
        }

        public CoordinateReferenceSystem getCoordinateReferenceSystem() {
            return isAvailable() ? getType().getCoordinateReferenceSystem() : null;
        }

    }

    protected class GeometryTypeDelegate extends AttributeTypeImpl implements GeometryType {

        private final Class<?> binding;

        public GeometryTypeDelegate(Name name, Class<?> binding, boolean identified,
                boolean isAbstract, List<Filter> restrictions, AttributeType superType,
                InternationalString description) {

            // Forward
            //
            super(name, binding, identified, isAbstract, restrictions, superType, description);

            // Enable value parsing
            //
            this.binding = binding;
        }

        public CoordinateReferenceSystem getCoordinateReferenceSystem() {
            return crs;
        }

        @Override
        public Object parse(Object value) throws IllegalArgumentException {
            return DataBuilder.parse(binding, value);
        }

    }

    // ------------------------------------------------------- 
    //  Helper methods 
    // -------------------------------------------------------

    /**
     * @param eFeatureInfo
     * @param eIsID
     * @param isDefault
     * @param srid
     * @param crs
     * @param eAttribute
     */
    protected static EFeatureGeometryInfo create(
            EFeatureInfo eFeatureInfo, 
            boolean isDefault, String srid,
            CoordinateReferenceSystem crs, EAttribute eAttribute) {
        
        //
        // Create new instance
        //
        EFeatureGeometryInfo eInfo = new EFeatureGeometryInfo();
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
        // Set other attribute members
        //        
        eInfo.eIsID = false;
        eInfo.eName = eAttribute.getName();
        eInfo.eAttribute = new WeakReference<EAttribute>(eAttribute);
        //
        // Set geometry members
        //        
        eInfo.crs = crs;
        eInfo.srid = srid;
        eInfo.isDefaultGeometry = isDefault;
        eInfo.geometryClassName = eAttribute.getEAttributeType().getInstanceClassName();
        //
        // Finished
        //
        return eInfo;
    }

}
