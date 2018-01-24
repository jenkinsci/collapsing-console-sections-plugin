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
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import javax.annotation.Nonnull;

public class CollapsingSectionAnnotator extends ConsoleAnnotator<Object> {
    
    @Nonnull
    private List<SectionDefinition> sections;
    
    @Nonnull
    private Stack<SectionDefinition> currentSections;
    
    @Nonnull
    private Stack<StackLevel> numberingStack;
    
    @Nonnull
    private CollapsingSectionsConfiguration configs;

    public CollapsingSectionAnnotator(@Nonnull CollapsingSectionsConfiguration configs) {
        this.configs = configs;
        this.sections = Arrays.asList(configs.getSectionDefinitions());       
        this.currentSections = new Stack<SectionDefinition>();
        this.numberingStack = new Stack<StackLevel>();
        numberingStack.add(new StackLevel());
    }
    
    @Override
    public ConsoleAnnotator annotate(Object context, MarkupText text) {
        if (!(context instanceof Run)) {
            return null;
        }
        
        while (!currentSections.empty()) {
            SectionDefinition currentSection = currentSections.peek();
            if (currentSection.getSectionEndPattern().matcher(text.getText().trim()).matches()) {
                popSection(text);
                if (currentSection.isCollapseOnlyOneLevel()) {
                    break;
                }
            } else {
                break;
            }
        }

        for (SectionDefinition section : sections) {
            Matcher m = section.getSectionStartPattern().matcher(text.getText().trim());
            if (m.matches()) {
                pushSection(text, m, section);
            }
        }
        return this;
    }
    
    /**
     * Generates level prefix for further display.
     * @return LEVEL_MARKER for each upper level
     */
    @Nonnull
    private String getCurrentLevelPrefix() {
        StringBuilder str= new StringBuilder();
        if (configs.isNumberingEnabled()) {
            for (int i=0; i<currentSections.size()+1; i++) {
                str.append(numberingStack.get(i).getCounter());
                str.append(".");
            }
            str.append(' ');
        }
        return str.toString();
    }
    
    private void pushSection(@Nonnull MarkupText text, @Nonnull Matcher m, @Nonnull SectionDefinition section) {
        numberingStack.peek().increment();  
        text.addMarkup(0, "<div class=\"section\" data-level=\""+getCurrentLevelPrefix()+"\"><div class=\"collapseHeader\">" + getCurrentLevelPrefix() + Util.escape(section.getSectionDisplayName(m)) + "<div class=\"collapseAction\"><p onClick=\"doToggle(this)\">" + ((section.isCollapseSection()) ? "Show Details" : "Hide Details") +"</p></div></div><div class=\"" + ((section.isCollapseSection()) ? "collapsed" : "expanded") + "\">");
        numberingStack.add(new StackLevel());
        currentSections.push(section);
    }
    
    private void popSection(@Nonnull MarkupText text) {
        text.addMarkup(text.getText().length(), "</div></div>");
        currentSections.pop();
        numberingStack.pop();
    }
    
    /**Enumerates stack levels for the numbering*/
    private static class StackLevel implements Serializable {
        int counter = 0;
        
        public void increment() {
            counter++;
        }

        public int getCounter() {
            return counter;
        }    
    }
}
