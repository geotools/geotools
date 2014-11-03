package org.geotools.ysld;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.opengis.style.ChannelSelection;
import org.opengis.style.SelectedChannelType;

public enum Band {
    GRAY("gray") {
        
        @Override
        public org.opengis.style.SelectedChannelType getFrom(org.opengis.style.ChannelSelection sel) {
            return sel.getGrayChannel();
        }

        @Override
        public void setTo(org.geotools.styling.ChannelSelection sel, org.geotools.styling.SelectedChannelType chan) {
            sel.setGrayChannel(chan);
        }
    },
    RED("red") {
        @Override
        public SelectedChannelType getFrom(ChannelSelection sel) {
            return sel.getRGBChannels()[0];
        }

        @Override
        public void setTo(org.geotools.styling.ChannelSelection sel,
                org.geotools.styling.SelectedChannelType chan) {
            org.geotools.styling.SelectedChannelType channels[] = sel.getRGBChannels();
            channels[0]=chan;
            sel.setRGBChannels(channels);
        }
    },
    GREEN("green") {
        @Override
        public SelectedChannelType getFrom(ChannelSelection sel) {
            return sel.getRGBChannels()[1];
        }

        @Override
        public void setTo(org.geotools.styling.ChannelSelection sel,
                org.geotools.styling.SelectedChannelType chan) {
            org.geotools.styling.SelectedChannelType channels[] = sel.getRGBChannels();
            channels[1]=chan;
            sel.setRGBChannels(channels);
        }
    },
    BLUE("blue") {
        @Override
        public SelectedChannelType getFrom(ChannelSelection sel) {
            return sel.getRGBChannels()[2];
        }

        @Override
        public void setTo(org.geotools.styling.ChannelSelection sel,
                org.geotools.styling.SelectedChannelType chan) {
            org.geotools.styling.SelectedChannelType channels[] = sel.getRGBChannels();
            channels[2]=chan;
            sel.setRGBChannels(channels);
        }
    };
    
    public static final List<Band> RGB = Collections.unmodifiableList(Arrays.asList(RED, GREEN, BLUE));
    
    abstract public org.opengis.style.SelectedChannelType getFrom(org.opengis.style.ChannelSelection sel);
    abstract public void setTo(org.geotools.styling.ChannelSelection sel, org.geotools.styling.SelectedChannelType chan);

    public final String key;

    private Band(String key) {
        this.key = key;
    }
}
