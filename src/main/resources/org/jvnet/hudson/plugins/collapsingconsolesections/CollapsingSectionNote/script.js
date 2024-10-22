// The doToggle method is used by CollapsingSectionAnnotator.java
// eslint-disable-next-line no-unused-vars
function doToggle(o)
{
    var section = o.parentNode.parentNode;
    if (section.nextElementSibling) {
        if (section.nextElementSibling.className === "collapsed") {
            section.nextElementSibling.className = "expanded";
            o.innerHTML = "Hide Details";
        } else {
            section.nextElementSibling.className = "collapsed";
            o.innerHTML = "Show Details";
        }
    } else {
	if (section.nextSibling.className === "collapsed") {
            section.nextSibling.className = "expanded";
            o.innerHTML = "Hide Details";
        } else {
            section.nextSibling.className = "collapsed";
            o.innerHTML = "Show Details";
       }
    }
}

document.addEventListener("DOMContentLoaded", function () {
    // created on demand
    var outline = null;
    var loading = false;

    var queue = []; // console sections are queued up until we load outline.

    function getoffsets(object, offsets) {
        if (! offsets) {
                offsets = new Object();
                offsets.x = offsets.y = 0;
        }
        if (typeof object === "string")
                object = document.getElementById(object);
        offsets.x += object.offsetLeft;
        offsets.y += object.offsetTop;
        do {
                object = object.offsetParent;
                if (! object)
                        break;
                offsets.x += object.offsetLeft;
                offsets.y += object.offsetTop;
        } while(object.tagName.toUpperCase() !== "BODY");
        return offsets;
}

    function initFloatingSection() {
        var d = document.getElementById("console-section-container");
        if (d === null) return;

        window.onscroll = function() {
            var offsets = getoffsets(d);
            var floatSection = d.childNodes[0];

            // if the height of the floatSection exceeds the window then keep it attached
            // detached would make some items inaccessible
            if (offsets.y - window.scrollY <= 5 && floatSection.offsetHeight <= window.innerHeight) {
                if (floatSection.className !== "scrollDetached") {
                    floatSection.className = "scrollDetached";
                    floatSection.style.width = d.offsetWidth + "px";
                }

                floatSection.style["left"] = -window.scrollX + offsets.x + "px";
            } else {
                if (floatSection.className !== "scrollAttached") {
                    floatSection.className = "scrollAttached";
                }
            }
        }
    }

    function loadOutline() {
        if (outline !== null)  return false;   // already loaded

        if (!loading) {
            loading = true;
            fetch(
                rootURL+"/descriptor/org.jvnet.hudson.plugins.collapsingconsolesections.CollapsingSectionNote/outline"
            ).then(
                (resp) => {
                    resp.text().then( (responseText) => {
                        const sidePanel = document.getElementById("side-panel");
                        sidePanel.insertAdjacentHTML("beforeend", responseText);

                        outline = document.getElementById("console-section-body");
                        initFloatingSection();
                        loading = false;
                        queue.forEach(handle);
                    })
                }
            );
        }
        return true;
    }

    function generateOutlineSection(sectionElt){

        var id = "console-section-"+(iota++);
    	// add target link in output log
        var targetLink = document.createElement("a");
        targetLink.name = id;
        sectionElt.prepend(targetLink);

    	// create outline element
    	var collapseHeader = sectionElt.querySelector("DIV.collapseHeader")
        var elt = document.createElement("li")
        var link = document.createElement("a");
        link.href = "#" + id;
        link.textContent = justtext(collapseHeader);
        elt.appendChild(link);

    	//check children sections
    	var level = -1;
    	var currentElement = sectionElt;
    	while (currentElement.closest("div.section")) {
    		currentElement = currentElement.closest("div.section").parentElement;
    		level++;
    	}
        var childrenSections = sectionElt.querySelectorAll("div.section")
        childrenSections = Array.from(childrenSections).filter(
        		function( section ) {
        			var parentLevel = -1;
        			var parentElement = section.closest("div.section").parentElement;
        			while (parentElement.closest("div.section")) {
        				parentElement = parentElement.closest("div.section").parentElement;
        				parentLevel++;
        			}
        			return parentLevel == level;
        		}
        )
        if(childrenSections.length){            	
                var newParentUl = document.createElement("ul");	
                newParentUl.dataset.name = "UL  :"+sectionElt.dataset.level
                childrenSections.forEach(function(child) {
                    var childElt = generateOutlineSection(child)
                    newParentUl.appendChild(childElt)
                });
                elt.appendChild(newParentUl);
        }
        return elt
    }
    
    
    function handle(e) {    	
    	var sectionElt = e;
        if (loadOutline()) {
       		queue.push(e);
        } else {        
            var newElt = generateOutlineSection(sectionElt)
            outline.appendChild(newElt);  
        }
    }
    
    function justtext(elt) {
    	  
    	var clone = elt.cloneNode(true);
    	const childNodes = Array.from(clone.childNodes);
    	childNodes.forEach(node => {
    		if (node.nodeType === Node.ELEMENT_NODE) {
    			clone.removeChild(node);
    		}
    	});
    	return clone.textContent;
    };

    Behaviour.register({
        // insert <a name="..."> for each console section and put it into the outline
        "div.section" : function(e) {
                var level = -1;
                var currentElement = e;
                while (currentElement && currentElement.closest("div.section")) {
                    currentElement = currentElement.closest("div.section").parentElement;
                    level++;
                }
        	//only treat top level section
        	if(level == 0){
      			handle(e);
        	}
        }
    });
});
