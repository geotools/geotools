/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.swing.dndlist;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.awt.GraphicsEnvironment;

import org.geotools.swing.control.DnDList;
import org.geotools.swing.control.DnDListModel;
import org.junit.Test;

public class DnDListTest {

    boolean isHeadless(){
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment(); 
        return ge.isHeadless();
    }
    @Test
    public void testDnDList() {
        if( isHeadless() ) return;

        DnDList<String> list = new DnDList<String>();
        assertNotNull( list );
    }

    @Test
    public void testDnDListDnDListModelOfT() {
        if( isHeadless() ) return;

        DnDListModel<String> model = new DnDListModel<String>();
        model.addItem("one");
        model.addItem("two");
        
        DnDList<String> list = new DnDList<String>( model );
        assertSame( model, list.getModel() );
        
        try {
           list = new DnDList<String>( null );
           fail( "Expected illegal argument exception");
        }
        catch( IllegalArgumentException expected ){            
        }
    }

    @Test
    public void testGetModel() {
        if( isHeadless() ) return;

        DnDListModel<String> model = new DnDListModel<String>();
        model.addItem("one");
        model.addItem("two");
        
        DnDList<String> list = new DnDList<String>( model );
        assertSame( model, list.getModel() );
    }

}
