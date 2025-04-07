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
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 * @author dty
 */
class SectionDefinitionTest {

    private static final String MAVEN_SECTION_NAME = "{3}{5}:{7}";
    private static final String MAVEN_PLUGIN_SECTION_END_PATTERN = "^\\[INFO\\] $";
    private static final String MAVEN_PLUGIN_SECTION_START_PATTERN = "\\[INFO\\] --- ((maven-([a-z]*)-plugin)|(([a-z]*)-maven-plugin)):([^:]*):([^ ]*) (.*)---";

    @Test
    void testSectionDisplayNameWithGroupCapture() {
        SectionDefinition def = new SectionDefinition("Section Heading", "Updating.+", "At revision \\d+.+", false, false);
        Matcher m = def.getSectionStartPattern().matcher("Updating https://svn.dev.java.net/svn/hudson/trunk/hudson/plugins/collapsing-console-sections");
        assertEquals("Section Heading", def.getSectionDisplayName(m));

        def = new SectionDefinition("Section Heading {1}", "Updating (.+)://svn.dev.java.net/svn/hudson/trunk/hudson.+", "At revision \\d+.+", false, false);
        m = def.getSectionStartPattern().matcher("Updating https://svn.dev.java.net/svn/hudson/trunk/hudson/plugins/collapsing-console-sections");
        assertEquals("Section Heading https", def.getSectionDisplayName(m));

        def = new SectionDefinition("Section Heading \\{1\\}", "Updating (.+)://svn.dev.java.net/svn/hudson/trunk/hudson.+", "At revision \\d+.+", false, false);
        m = def.getSectionStartPattern().matcher("Updating https://svn.dev.java.net/svn/hudson/trunk/hudson/plugins/collapsing-console-sections");
        assertEquals("Section Heading \\{1\\}", def.getSectionDisplayName(m));

        def = new SectionDefinition("Section Heading {1} {2}", "Updating (.+)://(.+)/svn/hudson/trunk/hudson.+", "At revision \\d+.+", false, false);
        m = def.getSectionStartPattern().matcher("Updating https://svn.dev.java.net/svn/hudson/trunk/hudson/plugins/collapsing-console-sections");
        assertEquals("Section Heading https svn.dev.java.net", def.getSectionDisplayName(m));

        def = new SectionDefinition("Section Heading {1} {2} {1}", "Updating (.+)://(.+)/svn/hudson/trunk/hudson.+", "At revision \\d+.+", false, false);
        m = def.getSectionStartPattern().matcher("Updating https://svn.dev.java.net/svn/hudson/trunk/hudson/plugins/collapsing-console-sections");
        assertEquals("Section Heading https svn.dev.java.net https", def.getSectionDisplayName(m));
    }

    @Test
    void testMavenPluginDefinition() {
        testMavenMojoPlugin("[INFO] --- maven-jar-plugin:2.3.1:jar (default-jar) @ sardine ---", "jar:jar");
        testMavenMojoPlugin("[INFO] --- cobertura-maven-plugin:2.4:instrument (report:cobertura) @ sardine ---", "cobertura:instrument");
    }

    /**
     * @param haystack
     * @param expected
     */
    private static void testMavenMojoPlugin(final String haystack, final String expected) {
        final SectionDefinition  def = new SectionDefinition(MAVEN_SECTION_NAME, MAVEN_PLUGIN_SECTION_START_PATTERN, MAVEN_PLUGIN_SECTION_END_PATTERN, false, false);
        final Matcher m = def.getSectionStartPattern().matcher(haystack);
        assertEquals(expected, def.getSectionDisplayName(m));
    }
}