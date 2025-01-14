document.addEventListener("DOMContentLoaded", function () {
    // created on demand
    var outline = null;
    var loading = false;

    var queue = []; // console sections are queued up until we load outline.

    function loadOutline() {
        if (outline !== null) return false; // already loaded

        if (!loading) {
            loading = true;
            fetch(
                rootURL + "/descriptor/org.jvnet.hudson.plugins.collapsingconsolesections.CollapsingSectionNote/outline",
            ).then(
                (resp) => {
                    resp.text().then((responseText) => {
                        const sidePanel = document.getElementById("side-panel");
                        sidePanel.insertAdjacentHTML("beforeend", responseText);

                        outline = document.getElementById("console-section-body");
                        loading = false;
                        queue.forEach(handle);
                    });
                },
            );
        }
        return true;
    }

    function generateOutlineSection(sectionElt) {
        var id = "console-section-" + (iota++);
        // add target link in output log
        var targetLink = document.createElement("a");
        targetLink.name = id;
        sectionElt.prepend(targetLink);

        // create outline element
        var collapseHeader = sectionElt.querySelector("summary.collapseHeader");
        var listElt = document.createElement("ul");
        var elt = document.createElement("li");
        listElt.appendChild(elt);

        var link = document.createElement("a");
        link.href = "#" + id;
        link.textContent = justtext(collapseHeader);
        elt.appendChild(link);

        // check children sections
        var level = -1;
        var currentElement = sectionElt;
        var sectionsSelector = "details.collapsingSection";
        while (currentElement.closest(sectionsSelector)) {
            currentElement = currentElement.closest(sectionsSelector).parentElement;
            level++;
        }
        var childrenSections = sectionElt.querySelectorAll(sectionsSelector);
        childrenSections = Array.from(childrenSections).filter(
            function (section) {
                var parentLevel = -1;
                var parentElement = section.closest(sectionsSelector).parentElement;
                while (parentElement.closest(sectionsSelector)) {
                    parentElement = parentElement.closest(sectionsSelector).parentElement;
                    parentLevel++;
                }
                return parentLevel == level;
            },
        );
        if (childrenSections.length) {
            childrenSections.forEach(function (child) {
                var childElt = generateOutlineSection(child);
                elt.appendChild(childElt);
            });
        }
        return listElt;
    }

    function handle(e) {
        var sectionElt = e;
        if (loadOutline()) {
            queue.push(e);
        } else {
            var newElt = generateOutlineSection(sectionElt);
            outline.appendChild(newElt);
        }
    }

    function handleSidePanelChanges() {
        const sidePanel = document.getElementById("side-panel");
        const collapsePanel = sidePanel.children.namedItem("console-section-container");
        if (collapsePanel === null) return;

        // Move the collapsible section headers to the last position
        if (sidePanel.lastElementChild !== collapsePanel) {
            sidePanel.lastElementChild.after(collapsePanel);
        }
    }

    function justtext(elt) {
        var clone = elt.cloneNode(true);
        const childNodes = Array.from(clone.childNodes);
        childNodes.forEach((node) => {
            if (node.nodeType === Node.ELEMENT_NODE) {
                clone.removeChild(node);
            }
        });
        return clone.textContent;
    };

    Behaviour.register({
        // insert <a name="..."> for each console section and put it into the outline
        "details.collapsingSection": function (e) {
            var level = -1;
            var currentElement = e;
            var sectionsSelector = "details.collapsingSection";
            while (currentElement && currentElement.closest(sectionsSelector)) {
                currentElement = currentElement.closest(sectionsSelector).parentElement;
                level++;
            }
            // only treat top level section
            if (level == 0) {
                handle(e);
            }
        },
    });

    new MutationObserver(handleSidePanelChanges)
        .observe(document.getElementById("side-panel"), { childList: true });
});
