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

import hudson.MarkupText;
import hudson.Util;
import hudson.console.ConsoleAnnotator;
import hudson.model.Run;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;

public class CollapsingSectionAnnotator extends ConsoleAnnotator<Object> {
    private List<SectionDefinition> sections;
    private Stack<SectionDefinition> currentSections;

    public CollapsingSectionAnnotator(SectionDefinition... sections) {
        this.sections = Arrays.asList(sections);
        this.currentSections = new Stack<SectionDefinition>();
    }
    
    @Override
    public ConsoleAnnotator annotate(Object context, MarkupText text) {
        if (!(context instanceof Run)) {
            return null;
        }
        
        while (!currentSections.empty()) {
            SectionDefinition currentSection = currentSections.peek();
            if (currentSection.getSectionEndPattern().matcher(text.getText().trim()).matches()) {
                text.addMarkup(text.getText().length(), "</div>");
                currentSections.pop();
            } else {
                break;
            }
        }

        for (SectionDefinition section : sections) {
            Matcher m = section.getSectionStartPattern().matcher(text.getText().trim());
            if (m.matches()) {
                text.addMarkup(0, "<div class=\"collapseHeader\">" + Util.escape(section.getSectionDisplayName(m)) + "<div class=\"collapseAction\"><p onClick=\"doToggle(this)\">Hide Details</p></div></div><div class=\"expanded\">");
                currentSections.push(section);
            }
        }

        return this;
    }
}
