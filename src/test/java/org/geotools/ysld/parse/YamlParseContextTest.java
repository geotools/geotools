package org.geotools.ysld.parse;

import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;

import org.geotools.ysld.YamlObject;
import org.junit.Test;

public class YamlParseContextTest {

    @Test
    public void testDocHints() throws Exception {
        YamlParseContext ctxt = new YamlParseContext();
        YamlParseHandler handler = createMock(YamlParseHandler.class);
        YamlObject obj1 = createMock(YamlObject.class);
        
        replay(handler);
        
        ctxt.push(obj1, handler);
        
        assertThat(ctxt.getDocHint("testHint1"), nullValue());
        ctxt.setDocHint("testHint1", "th1v1");
        assertThat((String)ctxt.getDocHint("testHint1"), is("th1v1"));
        ctxt.setDocHint("testHint1", "th1v2");
        assertThat((String)ctxt.getDocHint("testHint1"), is("th1v2"));
        
        assertThat(ctxt.getDocHint("testHint2"), nullValue());
        ctxt.setDocHint("testHint2", "th2v1");
        assertThat((String)ctxt.getDocHint("testHint2"), is("th2v1"));
        assertThat((String)ctxt.getDocHint("testHint1"), is("th1v2"));
        
        verify(handler);
    }
    
}
