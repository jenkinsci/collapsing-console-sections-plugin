# Release Notes (archive)

### Newer versions

See [GitHub Releases](https://github.com/jenkinsci/collapsing-console-sections-plugin/releases)

### Version 1.7.0 (Jan 23, 2018)

-   [![(plus)](/docs/images/add.svg) PR
    \#15](https://github.com/jenkinsci/collapsing-console-sections-plugin/pull/15) -
    Improve the outline layout

### Version 1.6.0 (June 11, 2017)

-   ![(plus)](/docs/images/add.svg) [JENKINS-26630](https://issues.jenkins-ci.org/browse/JENKINS-26630) -
    Add option for collapsing sections by default
-   ![(error)](/docs/images/error.svg) [JENKINS-25201](https://issues.jenkins-ci.org/browse/JENKINS-25201)
    - Make console sections non-floating if the content exceed the
    window height
-   [![(info)](/docs/images/information.svg) JENKINS-23121](https://issues.jenkins-ci.org/browse/JENKINS-23121) -
    Add regular expression field validation to the configuration page
-   ![(info)](/docs/images/information.svg) Update Jenkins core requirement to 1.609.3

### Version 1.5.0 (June 13, 2016)

-   ![(info)](/docs/images/information.svg) The new baseline is 1.532.x
-   ![(error)](/docs/images/error.svg)
    [JENKINS-30690](https://issues.jenkins-ci.org/browse/JENKINS-30690) -
    Lack of section definitions causes NPE and an empty console
-   ![(error)](/docs/images/error.svg) [PR
    \#5](https://github.com/jenkinsci/collapsing-console-sections-plugin/pull/5) -
    Fix cursor type in section headers from hand to pointer
-   ![(info)](/docs/images/information.svg) Migration to the new Jenkins plugin parent POM

### Version 1.4.1 (November 04, 2013)

-   Hot-fix for non-serializable classes issue
    ([JENKINS-20304](https://issues.jenkins-ci.org/browse/JENKINS-20304))

### Version 1.4 (October 26, 2013)

-   Indenting of sections on navigation pane
-   Automatic numbering of sections
-   Recursive termination of sections by a single log line can be
    disabled
-   Fixed a bug with sections appearance in Internet Explorer
    ([JENKINS-15568](https://issues.jenkins-ci.org/browse/JENKINS-15568))

### Version 1.3 (November 11, 2010)

-   [JENKINS-8078](https://issues.jenkins-ci.org/browse/JENKINS-8078) - Work around problem that causes SCM polling logs to not be displayed.

### Version 1.2 (November 4, 2010)

-   Allow content of captured groups of the starting regular expression
    to be used in the section header.
-   Escape section header on output to prevent XSS exploits.

### Version 1.1 (November 3, 2010)

-   Fixed bug where navigation widget overlaps into console area when
    floating.

### Version 1.0 (November 2, 2010)

-   Initial version
