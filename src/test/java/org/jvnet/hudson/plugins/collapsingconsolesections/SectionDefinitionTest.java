/*
 * The MIT License
 *
 * Copyright (c) 2010, Yahoo! Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jvnet.hudson.plugins.collapsingconsolesections;

import java.util.regex.Matcher;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dty
 */
public class SectionDefinitionTest {
    @Test
    public void testSectionDisplayNameWithGroupCapture() throws Exception {
        SectionDefinition def = new SectionDefinition("Section Heading", "Updating.+", "At revision \\d+.+");
        Matcher m = def.getSectionStartPattern().matcher("Updating https://svn.dev.java.net/svn/hudson/trunk/hudson/plugins/collapsing-console-sections");
        assertEquals("Section Heading", def.getSectionDisplayName(m));

        def = new SectionDefinition("Section Heading {1}", "Updating (.+)://svn.dev.java.net/svn/hudson/trunk/hudson.+", "At revision \\d+.+");
        m = def.getSectionStartPattern().matcher("Updating https://svn.dev.java.net/svn/hudson/trunk/hudson/plugins/collapsing-console-sections");
        assertEquals("Section Heading https", def.getSectionDisplayName(m));

        def = new SectionDefinition("Section Heading \\{1\\}", "Updating (.+)://svn.dev.java.net/svn/hudson/trunk/hudson.+", "At revision \\d+.+");
        m = def.getSectionStartPattern().matcher("Updating https://svn.dev.java.net/svn/hudson/trunk/hudson/plugins/collapsing-console-sections");
        assertEquals("Section Heading \\{1\\}", def.getSectionDisplayName(m));

        def = new SectionDefinition("Section Heading {1} {2}", "Updating (.+)://(.+)/svn/hudson/trunk/hudson.+", "At revision \\d+.+");
        m = def.getSectionStartPattern().matcher("Updating https://svn.dev.java.net/svn/hudson/trunk/hudson/plugins/collapsing-console-sections");
        assertEquals("Section Heading https svn.dev.java.net", def.getSectionDisplayName(m));

        def = new SectionDefinition("Section Heading {1} {2} {1}", "Updating (.+)://(.+)/svn/hudson/trunk/hudson.+", "At revision \\d+.+");
        m = def.getSectionStartPattern().matcher("Updating https://svn.dev.java.net/svn/hudson/trunk/hudson/plugins/collapsing-console-sections");
        assertEquals("Section Heading https svn.dev.java.net https", def.getSectionDisplayName(m));
    }
}