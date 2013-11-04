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

import hudson.Extension;
import hudson.MarkupText;
import hudson.console.ConsoleAnnotationDescriptor;
import hudson.console.ConsoleAnnotator;
import hudson.console.ConsoleNote;
import java.lang.reflect.Array;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

/**
 *
 * @author dty
 */
public class CollapsingSectionNote extends ConsoleNote {
    private String sectionDisplayName;
    private String sectionStartPattern;
    private String sectionEndPattern;
    private boolean collapseOnlyOneLevel;
    
    @DataBoundConstructor
    public CollapsingSectionNote(String sectionDisplayName, String sectionStartPattern, String sectionEndPattern, boolean collapseOnlyOneLevel) {
        this.sectionDisplayName = sectionDisplayName;
        this.sectionStartPattern = sectionStartPattern;
        this.sectionEndPattern = sectionEndPattern;
        this.collapseOnlyOneLevel = collapseOnlyOneLevel;
    }

    public String getSectionDisplayName() {
        return sectionDisplayName;
    }

    public String getSectionStartPattern() {
        return sectionStartPattern;
    }

    public String getSectionEndPattern() {
        return sectionEndPattern;
    }

    public boolean isCollapseOnlyOneLevel() {
        return collapseOnlyOneLevel;
    }
    
    public SectionDefinition getDefinition() {
        return new SectionDefinition(sectionDisplayName, sectionStartPattern, sectionEndPattern, collapseOnlyOneLevel);
    }
    
    @Override
    public ConsoleAnnotator annotate(Object context, MarkupText text, int charPos) {
        return null;
    }


    @Extension
    public static final class DescriptorImpl extends ConsoleAnnotationDescriptor {
        private CollapsingSectionNote[] sections;
        private boolean numberingEnabled;
        private transient CollapsingSectionsConfiguration configuration;

        public DescriptorImpl() {
            load();
        }
        
        public String getDisplayName() {
            return "Console Section";
        }

        public CollapsingSectionNote[] getSections() {
            if (sections != null) {
                return sections.clone();
            }

            return (CollapsingSectionNote[]) Array.newInstance(CollapsingSectionNote.class, 0);
        }

        public SectionDefinition[] getSectionDefinitions() {
            return configuration.getSectionDefinitions();
        }

        public void setSections(CollapsingSectionNote... sections) {
            this.sections = sections.clone();
        }

        public boolean isNumberingEnabled() {
            return numberingEnabled;
        }
        
        public CollapsingSectionsConfiguration getConfiguration() {
            return configuration;
        }

        @Override
        public synchronized void load() {
            super.load();
            // Enable configuration cache
            configuration = new CollapsingSectionsConfiguration(sections, numberingEnabled);
        }
          
        @Override
        @SuppressWarnings("unchecked") // cast to T[]
        public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
            setSections(req.bindJSONToList(clazz, json.get("consolesection")).toArray((CollapsingSectionNote[]) Array.newInstance(clazz, 0)));
            numberingEnabled = json.getBoolean("numberingEnabled");
            configuration = new CollapsingSectionsConfiguration(sections, numberingEnabled);
            save();
            
            return true;
        }
     }
}
