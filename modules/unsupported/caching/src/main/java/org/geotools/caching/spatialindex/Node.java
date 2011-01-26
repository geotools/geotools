// Spatial Index Library
//
// Copyright (C) 2002  Navel Ltd.
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation;
// version 2.1 of the License.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
// Contact information:
//  Mailing address:
//    Marios Hadjieleftheriou
//    University of California, Riverside
//    Department of Computer Science
//    Surge Building, Room 310
//    Riverside, CA 92521
//
//  Email:
//    marioh@cs.ucr.edu
package org.geotools.caching.spatialindex;

public interface Node extends Entry {

	/**
	 * 
	 * @return the number of child nodes
	 */
	public int getChildrenCount();

	/**
	 * 
	 * @return the amount of data stored in the node
	 */
    public int getDataCount();

    /**
     * 
     * @param index
     * @return the child node identifier at the given index
     * @throws IndexOutOfBoundsException
     */
    public NodeIdentifier getChildIdentifier(int index) throws IndexOutOfBoundsException;

    /**
     * 
     * @return the id of the node
     */
    public NodeIdentifier getIdentifier();
    
    /**
     * 
     * @param id sets the id of the node
     */
    public void setIdentifier (NodeIdentifier id);


    public int getLevel();

    public boolean isIndex();

    /**
     * If the node is a leaf node (has no children)
     * @return
     */
    public boolean isLeaf();

    /**
     * Clears all the data in the node.
     */
    public void clear();
} // INode
