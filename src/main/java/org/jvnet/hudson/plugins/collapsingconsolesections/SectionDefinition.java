/*
 * The MIT License
 *
 * Copyright (c) 2010, Yahoo! Inc. and contributors
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

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.annotation.Nonnull;

/**
 *
 * @author dty
 */
public class SectionDefinition implements Serializable {
    private String name;
    private Pattern start;
    private Pattern end;
    private boolean collapseOnlyOneLevel;
    private boolean collapseSection;

    /**
     * @deprecated Use {@link #SectionDefinition(java.lang.String, java.lang.String, java.lang.String, boolean, boolean)} instead.
     */
    @Deprecated
    public SectionDefinition(String sectionName, String sectionStartPattern, String sectionEndPattern) {
        this(sectionName, sectionStartPattern, sectionEndPattern, false, false);
    }

    /**
     * @deprecated Use {@link #SectionDefinition(java.lang.String, java.lang.String, java.lang.String, boolean, boolean)} instead.
     */
    @Deprecated
    public SectionDefinition(String sectionName, String sectionStartPattern, String sectionEndPattern, boolean collapseOnlyOneLevel) {
        this(sectionName, sectionStartPattern, sectionEndPattern, collapseOnlyOneLevel, false);
    }

    /**
     * Creates a new section definition.
     * @param sectionName Name of the section
     * @param sectionStartPattern Regular expression pattern for the section start
     * @param sectionEndPattern Regular expression pattern for the section end
     * @param collapseOnlyOneLevel If {@code true}, only one level will be collapsed by the end pattern
     * @param collapseSection If {@code true}, the section should be collapsed by default
     * @throws PatternSyntaxException One of the patterns is invalid
     */
    public SectionDefinition(@Nonnull String sectionName, @Nonnull String sectionStartPattern, @Nonnull String sectionEndPattern, boolean collapseOnlyOneLevel, boolean collapseSection) throws PatternSyntaxException {
        name = sectionName;
        start = Pattern.compile(sectionStartPattern);
        end = Pattern.compile(sectionEndPattern);
        this.collapseOnlyOneLevel = collapseOnlyOneLevel;
        this.collapseSection = collapseSection;
    }

    @Nonnull
    public String getSectionDisplayName() {
        return name;
    }

    @Nonnull
    public String getSectionDisplayName(@Nonnull Matcher m) {
        @SuppressWarnings("RedundantStringConstructorCall")
        String result = name;
        if (m.matches()) {
            for (int i = 0; i <= m.groupCount(); i++) {
                final String group = m.group(i);
                if (group != null) {
                    result = result.replaceAll("\\{" + i + "\\}", group);
                } else {
                    result = result.replaceAll("\\{" + i + "\\}", "");
                }
            }
        }

        return result;
    }

    @Nonnull
    public Pattern getSectionStartPattern() {
        return start;
    }

    @Nonnull
    public Pattern getSectionEndPattern() {
        return end;
    }
    public boolean isCollapseSection() {
        return collapseSection;
    }
    public boolean isCollapseOnlyOneLevel() {
        return collapseOnlyOneLevel;
    }
}
