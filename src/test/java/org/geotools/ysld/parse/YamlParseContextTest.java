package org.geotools.ysld.parse;

import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.hamcrest.Matchers.describedAs;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

import org.geotools.ysld.YamlObject;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.BaseMatcher;
import org.junit.Test;

import com.google.common.base.Optional;

public class YamlParseContextTest {

    @Test
    public void testDocHints() throws Exception {
        YamlParseContext ctxt = new YamlParseContext();
        YamlParseHandler handler = createMock(YamlParseHandler.class);
        YamlObject obj1 = createMock(YamlObject.class);
        
        replay(handler);
        
        ctxt.push(obj1, handler);
        
        assertThat(ctxt.getDocHint("testHint1"), not(isPresent()));
        ctxt.setDocHint("testHint1", "th1v1");
        assertThat((Optional<String>)ctxt.getDocHint("testHint1"), optionalOf(is("th1v1")));
        ctxt.setDocHint("testHint1", "th1v2");
        assertThat((String)ctxt.getDocHint("testHint1").get(), is("th1v2"));
        
        assertThat(ctxt.getDocHint("testHint2"), not(isPresent()));
        ctxt.setDocHint("testHint2", "th2v1");
        assertThat((String)ctxt.getDocHint("testHint2").get(), is("th2v1"));
        assertThat((String)ctxt.getDocHint("testHint1").get(), is("th1v2"));
        
        verify(handler);
    }
    
    
    static Matcher<Optional<?>> isPresent() {
        return describedAs("is present", Matchers.<Optional<?>>hasProperty("present", is(true)));
    }
    static <T> Matcher<Optional<T>> optionalOf(final Matcher<T> valueMatcher) {
        return new BaseMatcher<Optional<T>>(){

            @SuppressWarnings("unchecked")
            @Override
            public boolean matches(Object arg0) {
                if (((Optional<T>) arg0).isPresent()) {
                    return valueMatcher.matches(((Optional<T>) arg0).get());
                }
                return false;
            }

            @Override
            public void describeTo(Description arg0) {
                arg0.appendText("Optional with value (");
                arg0.appendDescriptionOf(valueMatcher);
                arg0.appendText(")");
            }
            
        };
    }
    
}
