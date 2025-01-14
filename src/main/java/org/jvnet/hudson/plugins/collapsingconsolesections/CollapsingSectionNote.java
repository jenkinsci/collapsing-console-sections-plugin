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

import java.lang.reflect.Array;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import net.sf.json.JSONObject;

import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.NoExternalUse;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import hudson.Extension;
import hudson.MarkupText;
import hudson.console.ConsoleAnnotationDescriptor;
import hudson.console.ConsoleAnnotator;
import hudson.console.ConsoleNote;
import hudson.util.FormValidation;

/**
 *
 * @author dty
 */
public class CollapsingSectionNote extends ConsoleNote {
    
    @Nonnull
    private String sectionDisplayName;
    @Nonnull
    private String sectionStartPattern;
    @Nonnull
    private String sectionEndPattern;
    private boolean collapseOnlyOneLevel;
    private boolean collapseSection;
    private boolean allowNesting;

    @DataBoundConstructor
    public CollapsingSectionNote(@Nonnull String sectionDisplayName, @Nonnull String sectionStartPattern, @Nonnull String sectionEndPattern, boolean collapseOnlyOneLevel, boolean collapseSection, boolean allowNesting) {
        this.sectionDisplayName = sectionDisplayName;
        this.sectionStartPattern = sectionStartPattern;
        this.sectionEndPattern = sectionEndPattern;
        this.collapseOnlyOneLevel = collapseOnlyOneLevel;
        this.collapseSection = collapseSection;
        this.allowNesting = allowNesting;
    }

    @Deprecated
    public CollapsingSectionNote(String sectionDisplayName, String sectionStartPattern, String sectionEndPattern, boolean collapseOnlyOneLevel) {
        this(sectionDisplayName, sectionStartPattern, sectionEndPattern, collapseOnlyOneLevel, false, false);
    }

    @Nonnull
    public String getSectionDisplayName() {
        return sectionDisplayName;
    }

    @Nonnull
    public String getSectionStartPattern() {
        return sectionStartPattern;
    }

    @Nonnull
    public String getSectionEndPattern() {
        return sectionEndPattern;
    }

    /**
     * Check if the section should be collapsed by default.
     * @return {@code} true if the section should be collapsed.
     * @since 1.6.0
     */
    public boolean isCollapseSection() {
        return collapseSection;
    }

    public boolean isCollapseOnlyOneLevel() {
        return collapseOnlyOneLevel;
    }

    @Nonnull
    public SectionDefinition getDefinition() {
        return new SectionDefinition(sectionDisplayName, sectionStartPattern, sectionEndPattern, collapseOnlyOneLevel, collapseSection, allowNesting);
    }

    @Override
    public ConsoleAnnotator annotate(Object context, MarkupText text, int charPos) {
        return null;
    }


    @Extension
    public static final class DescriptorImpl extends ConsoleAnnotationDescriptor {
        
        @CheckForNull
        private CollapsingSectionNote[] sections;
        private boolean numberingEnabled;
        @Nonnull
        private transient CollapsingSectionsConfiguration configuration;

        @SuppressFBWarnings(value = "NP_NONNULL_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR", justification = "configuration is always initialized in load()")
        public DescriptorImpl() {
            load();
        }
        
        @Override
        public String getDisplayName() {
            return "Console Section";
        }

        @Nonnull
        public CollapsingSectionNote[] getSections() {
            if (sections != null) {
                return sections.clone();
            }

            return new CollapsingSectionNote[0];
        }

        @Nonnull
        public SectionDefinition[] getSectionDefinitions() {
            return configuration.getSectionDefinitions();
        }

        public void setSections(CollapsingSectionNote... sections) {
            this.sections = sections.clone();
        }

        public boolean isNumberingEnabled() {
            return numberingEnabled;
        }
        
        @Nonnull
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
        
        @Restricted(NoExternalUse.class)
        public FormValidation doCheckSectionStartPattern(@QueryParameter String sectionStartPattern) {
            try {
                Pattern.compile(sectionStartPattern);
            } catch (PatternSyntaxException exception) {
                return FormValidation.error(exception.getDescription());
            }
            return FormValidation.ok();
        }
        
        @Restricted(NoExternalUse.class)
        public FormValidation doCheckSectionEndPattern(@QueryParameter String sectionEndPattern) {
            return doCheckSectionStartPattern(sectionEndPattern);
        }
     }
}
