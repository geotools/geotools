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
package org.geotools.validation.relate;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.ResourceBundle;

import org.geotools.validation.DefaultIntegrityValidationBeanInfo;

/**
 * RelateIntegrityBeanInfo<br>
 * @author bowens<br>
 * Created Apr 27, 2004<br>
 * @source $URL$
 * @version <br>
 * 
 * <b>Puropse:</b><br>
 * <p>
 * DOCUMENT ME!!
 * </p>
 * 
 * <b>Description:</b><br>
 * <p>
 * DOCUMENT ME!!
 * </p>
 * 
 * <b>Usage:</b><br>
 * <p>
 * DOCUMENT ME!!
 * </p>
 */
public class RelateIntegrityBeanInfo extends DefaultIntegrityValidationBeanInfo
{

	/**
	 * 
	 */
	public RelateIntegrityBeanInfo()
	{
		super();
	}

	 /** 
	  * 
	  * 
	  * (non-Javadoc)
	  * @see java.beans.BeanInfo#getPropertyDescriptors()
	 **/
	public PropertyDescriptor[] getPropertyDescriptors() 
	{
		PropertyDescriptor[] pd2 = super.getPropertyDescriptors();
		ResourceBundle resourceBundle = getResourceBundle();
		if (pd2 == null) {
			pd2 = new PropertyDescriptor[0];
		}

		PropertyDescriptor[] pd = new PropertyDescriptor[pd2.length + 1];
		int i = 0;

		for (; i < pd2.length; i++)
			pd[i] = pd2[i];

		try {
			pd[i] = createPropertyDescriptor("de9imString", resourceBundle);
			pd[i].setExpert(false);
		
		} catch (IntrospectionException e) {
			pd = pd2;

			// TODO error, log here
			e.printStackTrace();
		}

		return pd;
	}
}
