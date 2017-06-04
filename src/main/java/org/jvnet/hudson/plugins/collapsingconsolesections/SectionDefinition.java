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
     * @deprecated Use version with sections collapsing instead
     */
    public SectionDefinition(String sectionName, String sectionStartPattern, String sectionEndPattern) {
        this(sectionName, sectionStartPattern, sectionEndPattern, false, false);
    }

    public SectionDefinition(String sectionName, String sectionStartPattern, String sectionEndPattern, boolean collapseOnlyOneLevel, boolean collapseSection) {
        name = sectionName;
        start = Pattern.compile(sectionStartPattern);
        end = Pattern.compile(sectionEndPattern);
        this.collapseOnlyOneLevel = collapseOnlyOneLevel;
        this.collapseSection = collapseSection;
    }

    public String getSectionDisplayName() {
        return name;
    }

    public String getSectionDisplayName(Matcher m) {
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

    public Pattern getSectionStartPattern() {
        return start;
    }

    public Pattern getSectionEndPattern() {
        return end;
    }
    public boolean getCollapseSection() {
        return collapseSection;
    }
    public boolean isCollapseOnlyOneLevel() {
        return collapseOnlyOneLevel;
    }
}
