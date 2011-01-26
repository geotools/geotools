/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.styling;

import org.geotools.util.Utilities;
import org.opengis.style.StyleVisitor;


/**
 * ChannelSelectionImpl
 *
 * @author iant
 * @source $URL$
 */
public class ChannelSelectionImpl 
    implements ChannelSelection {
    private SelectedChannelType gray;
    private SelectedChannelType red;
    private SelectedChannelType blue;
    private SelectedChannelType green;

    public SelectedChannelType getGrayChannel() {
        return gray;
    }

    /**
     * Retrieves the RGB channel that were selected.
     * 
     * <strong> Note that in case there is no RGB selection the returned
     * {@link SelectedChannelType} array will contain null elements.
     * 
     * @return {@link SelectedChannelType} array that contains the
     *         {@link SelectedChannelType} elements for the RGB channels.
     */
    public SelectedChannelType[] getRGBChannels() {
        return new SelectedChannelType[] { red, green, blue };
    }

    public SelectedChannelType[] getSelectedChannels() {
    	SelectedChannelType[] ret=null;
    	if (gray != null) {
        	ret = new SelectedChannelType[] { gray };
        } else if(red!=null||green!=null||blue!=null){
        	ret = new SelectedChannelType[] { red, green, blue };
        }

        return ret;
    }

    public void setGrayChannel(SelectedChannelType gray) {
        this.gray = gray;
    }
    public void setGrayChannel(org.opengis.style.SelectedChannelType gray ){
        this.gray = new SelectedChannelTypeImpl( gray );
    }

    public void setRGBChannels(SelectedChannelType[] channels) {
        if (channels.length != 3) {
            throw new IllegalArgumentException(
                "Three channels are required in setRGBChannels, got "
                + channels.length);
        }
        red = channels[0];
        green = channels[1];
        blue = channels[2];
    }

    public void setRGBChannels(SelectedChannelType red,
        SelectedChannelType green, SelectedChannelType blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }
    public void setRGBChannels(org.opengis.style.SelectedChannelType red, org.opengis.style.SelectedChannelType green, org.opengis.style.SelectedChannelType blue ){
        this.red = new SelectedChannelTypeImpl( red );
        this.green = new SelectedChannelTypeImpl( green );
        this.blue = new SelectedChannelTypeImpl( blue );
    }

    public void setSelectedChannels(SelectedChannelType[] channels) {
        if (channels.length == 1) {
            gray = channels[0];
        } else if (channels.length == 3) {
            red = channels[0];
            green = channels[1];
            blue = channels[2];
        } else {
            throw new IllegalArgumentException(
                "Wrong number of elements in setSelectedChannels, expected 1 or 3, got "
                + channels.length);
        }
    }

    public Object accept(StyleVisitor visitor, Object data) {
        return visitor.visit(this,data);

    }

    public void accept(org.geotools.styling.StyleVisitor visitor) {
        visitor.visit(this);
    }

	public void accept(org.opengis.style.StyleVisitor visitor) {
		visitor.visit( this,null );
	}
	
    @Override
    public int hashCode() {
        final int PRIME = 1000003;
        int result = 0;

        if (gray != null){
            result = (PRIME * result) + gray.hashCode();
        }

        if (red != null) {
            result = (PRIME * result) + red.hashCode();
        }
        
        if (blue != null) {
            result = (PRIME * result) + blue.hashCode();
        }

        if (green != null) {
            result = (PRIME * result) + green.hashCode();
        }
        
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
    	if (this == obj) {
            return true;
        }

        if (obj instanceof ChannelSelectionImpl) {
        	ChannelSelectionImpl other = (ChannelSelectionImpl) obj;

            return Utilities.equals(gray, other.gray)
            && Utilities.equals(red, other.red)
            && Utilities.equals(blue, other.blue)
            && Utilities.equals(green, other.green);
        }

        return false;
    }

    static ChannelSelectionImpl cast(org.opengis.style.ChannelSelection channel) {
        if( channel == null ){
            return null;
        }
        else if( channel instanceof ChannelSelectionImpl){
            return (ChannelSelectionImpl) channel;
        }
        else {
            ChannelSelectionImpl copy = new ChannelSelectionImpl();
            if( channel.getGrayChannel() != null ){
                copy.setGrayChannel( channel.getGrayChannel());
            }
            else {
                org.opengis.style.SelectedChannelType[] rgb = channel.getRGBChannels();
                copy.setRGBChannels( rgb[0], rgb[1], rgb[2]);
            }
            return copy;
        }
    }

}
