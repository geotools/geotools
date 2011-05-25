/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data.wfs.v1_0_0;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.LenientBuilder;
import org.geotools.filter.visitor.DuplicatingFilterVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.identity.FeatureId;


/**
 * This interface represents pending actions within a transaction.
 *
 * @author dzwiers
 *
 * @source $URL$
 */
public interface Action {
	/**
	 * Action mask for an Insert Action
	 */
    public static final int INSERT = 1;
	/**
	 * Action mask for an Update Action
	 */
    public static final int UPDATE = 2;
	/**
	 * Action mask for a Delete Action
	 */
    public static final int DELETE = 4;

    /**
     * @return The Type of Action ... one of the three Constants
     */
    public int getType();

    /**
     * @return The Filter on which to inflict the Action
     */
    public Filter getFilter();

    /**
     * @return The FeatureType name for which this Action is intended
     */
    public String getTypeName();

    /**
     * Represents an Update Action
     * 
     * @author dzwiers
     */
    public static class UpdateAction implements Action {
        private final Filter filter;
        private final Map properties;
        private final String typeName;

        /**
         * Makes an UpdateAction Filter is copied so any further changes will not be included in filter of action.
         * 
         * @param typeName The TypeName
         * @param f Filter which this update affects
         * @param properties The properties to update.  Entries must be <String, Object>  where String is an attribute to 
         * update and Object is the new Value.
         */
        public UpdateAction(String typeName, Filter f, Map properties) {
            DuplicatingFilterVisitor duplicator = new DuplicatingFilterVisitor();
            
            DuplicatingFilterVisitor visitor=new DuplicatingFilterVisitor();
            filter = (Filter) f.accept(visitor, null );
            this.properties = new HashMap(properties);
            this.typeName = typeName;
        }

        /**
         * @return @see Action#UPDATE
         */
        public int getType() {
            return UPDATE;
        }

        /**
         * Returns the property if found ... this method will not create a 
         * NullPointerException if properties is null.
         * 
         * @param name String the property key
         * @return Object The property if found, null other wise. 
         */
        public Object getProperty(String name) {
            return (properties == null) ? null : properties.get(name);
        }

        /**
         * Returns the property names if they exist ... this method will not create a 
         * NullPointerException if properties is null.
         * 
         * @return A list of the keys. 
         */
        public String[] getPropertyNames() {
            return properties==null?new String[0]:(String[]) properties.keySet().toArray(new String[properties.keySet()
                                                                               .size()]);
        }

        /**
         * @return a clone of the properties map, null if it does not exist. 
         */
        public Map getProperties() {
            return properties==null?null:new HashMap(properties);
        }

        /**
         * @return Filter the Filter
         */
        public Filter getFilter() {
            return filter;
        }

        /**
         * @return String the TypeName
         */
        public String getTypeName() {
            return typeName;
        }

		public void update(SimpleFeature feature) {
			if( !filter.evaluate(feature) )
				throw new IllegalArgumentException(feature+"is not affected by this update, only call update on features that" +
						"the Action applies to!");
            String[] propNames = getPropertyNames();

            for (int j = 0; j < propNames.length;
                    j++) {
                try {
                    feature.setAttribute(propNames[j],
                        getProperty(propNames[j]));
                } catch (IllegalAttributeException e) {
                    NoSuchElementException ee = new NoSuchElementException(e.getMessage());
                    ee.initCause(e);
                    throw ee;
                }
            }
		}
		
		public String toString() {
			return "UPDATE "+filter+" "+properties;
		}
    }

    /**
     * Represents a Delete Action for a Transaction
     * 
     * @author dzwiers
     */
    public static class DeleteAction implements Action {
        private final Filter filter;
        private final String typeName;

        /**
         * Represents a Delete Action.
         * Filter is copied so any further changes will not be included in filter of action.
         * 
         * @param typeName TypeName
         * @param f Filter of Features to Delete
         */
        public DeleteAction(String typeName, Filter f) {
            DuplicatingFilterVisitor visitor = new DuplicatingFilterVisitor();
            filter = (Filter) f.accept(visitor, null);
            this.typeName = typeName;
        }

        /**
         * @return @see Action#DELETE
         */
        public int getType() {
            return DELETE;
        }

        /**
         * @return the TypeName
         */
        public String getTypeName() {
            return typeName;
        }

        /**
         * @return the Filter
         */
        public Filter getFilter() {
            return filter;
        }
        
		public String toString() {
			return "REMOVE "+filter;
		}

    }

    /**
     * Represents an Insert Action
     * 
     * @author dzwiers
     */
    public static class InsertAction implements Action {
        private final SimpleFeature feature;

        /**
         * Creates an insert action for the Feature specified.  The feature is copied to
         * prevent inadvertant side effect of modifing the feature after an insert.
         * 
         * @param f Feature to add
         */
        public InsertAction(SimpleFeature f) {
        	SimpleFeature feature;
            try {
                // WARNNING: deep copy
                feature = LenientBuilder.copy( f );
			} catch (IllegalAttributeException e) {
				org.geotools.util.logging.Logging.getLogger("org.geotools.data.wfs").warning("Failed to duplicate feature:"+f);
				feature=f;
			}
			this.feature=feature;
        }

        /**
         * @return @see Action#INSERT
         */
        public int getType() {
            return INSERT;
        }

        /**
         * @return The Feature to add
         */
        public SimpleFeature getFeature() {
            return feature;
        }

        /**
         * @see org.geotools.wfs.v_1_0_0.data.Action#getTypeName()
         */
        public String getTypeName() {
            return (feature == null) ? null 
                    : feature.getType().getName().getLocalPart();
        }

        /**
         * @see org.geotools.wfs.v_1_0_0.data.Action#getFilter()
         */
        public Filter getFilter() {
            if( feature == null ) {
                return Filter.EXCLUDE;
            }
            String fid = feature.getID();
            if( fid != null ){
                FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
                FeatureId identifier = ff.featureId( fid );
                HashSet<FeatureId> fids = new HashSet<FeatureId>();
                fids.add( identifier );                
                Filter filter = ff.id( fids );
         
                return filter;
            }
            else {
                return Filter.EXCLUDE;
            }
        }
		public String toString() {
			return "INSERT "+feature;
		}
    }
}
