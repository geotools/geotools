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

import java.util.Iterator;
import java.util.List;

import org.geotools.data.wfs.v1_0_0.Action.UpdateAction;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.visitor.ClientTransactionAccessor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;

public class WFSTransactionAccessor implements ClientTransactionAccessor {

    private List actions;
    private static FilterFactory ff = CommonFactoryFinder.getFilterFactory( null );
    
	WFSTransactionAccessor(List actions){
        this.actions=actions;
	}
	
    /**
     * Returns all the filters indicating deleted feature anded together.  This is used to tell the server what features
     * to NOT return.
     * 
     * @return all the filters indicating deleted feature anded together. 
     */
	public Filter getDeleteFilter() {
		List l = actions;
		Iterator i = l.iterator();
		Filter deleteFilter=null;
		while(i.hasNext()){
			Action a = (Action)i.next();
			if(a.getType() == Action.DELETE){
				
				if( deleteFilter==null )
					deleteFilter=a.getFilter();
				else
					deleteFilter=  ff.and( deleteFilter, a.getFilter());
			}
		}
		return deleteFilter;
	}

	
    /**
     * Returns all the filters of updates that affect the attribute in the expression ored together.
     * 
     * @param attributePath the xpath identifier of the attribute.
     * @return all the filters of updates that affect the attribute in the expression ORed together.
     */
    public Filter getUpdateFilter(String attributePath) {
        Iterator i = actions.iterator();
        Filter updateFilter=null;
        while(i.hasNext()){
        	Action a = (Action)i.next();
        	if(a.getType() == Action.UPDATE){
        		UpdateAction ua = (UpdateAction)a;
        		if(ua.getProperty(attributePath)!=null){
        			if( updateFilter==null )
        				updateFilter=a.getFilter();
        			else
        				updateFilter=ff.and( updateFilter, a.getFilter());
        		}
        	}
        }
		return updateFilter;
	}
	
}
