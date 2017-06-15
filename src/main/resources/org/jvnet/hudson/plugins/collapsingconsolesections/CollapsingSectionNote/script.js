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

(function() {
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
            var u = new Ajax.Updater(document.getElementById("side-panel"),
                rootURL+"/descriptor/org.jvnet.hudson.plugins.collapsingconsolesections.CollapsingSectionNote/outline",
                {insertion: Insertion.Bottom, onComplete: function() {
                    if (!u.success())   return; // we can't us onSuccess because that kicks in before onComplete
                    outline = document.getElementById("console-section-body");
                    initFloatingSection();
                    loading = false;
                    queue.each(handle);
                }});
        }
        return true;
    }

    function generateOutlineSection($sectionElt){

        var id = "console-section-"+(iota++);
    	// add target link in output log
        var $a =jQuery("<a name='"+id+"' />");
        $a.prependTo($sectionElt);  
    	
    	// create outline element
    	var $collapseHeader = $sectionElt.children("DIV.collapseHeader")
        var $elt = jQuery("<li/>")
        $elt.append(jQuery("<a href= '#"+id+"'>"+justtext($collapseHeader)+"</a>"));

    	//check children sections
    	var level = $sectionElt.parents("div.section").length;
        var childrenSections = $sectionElt.find("div.section")
        childrenSections = childrenSections.filter(
        		function( index ) {
        			isDirectChild =  jQuery(this).parents("div.section").length == (level +1)
        			return isDirectChild; 
        		}
        )
        if(childrenSections.length){            	
        	var $newParentUl =  jQuery("<ul/>");	
        	$newParentUl.data("name" , "UL  :"+$sectionElt.data("level"))
        	childrenSections.each(function(childIndex, child){
            	//console.log("trigger child "+jQuery(child).data("level")+" from " + $sectionElt.data("level"))
        		var $childElt = generateOutlineSection(jQuery(child))
        		//console.log("Adding : "+$childElt.html() + "  to "+ jQuery("<div></div>").append( $newParentUl.clone() ).html())
        		$newParentUl.append($childElt)
        		//console.log("Added : "+jQuery("<div></div>").append( $newParentUl.clone() ).html())
        	})
        	$elt.append($newParentUl);
        }
        return $elt
    }
    
    
    function handle(e) {    	
    	var $sectionElt = jQuery(e);
        if (loadOutline()) {
       		queue.push(e);
        } else {        
            newElt = generateOutlineSection($sectionElt)
            jQuery(outline).append(newElt);  
        }
    }
    
    function justtext(elt) {
    	  
    	return jQuery(elt).clone()
    			.children()
    			.remove()
    			.end()
    			.text();
    };

    Behaviour.register({
        // insert <a name="..."> for each console section and put it into the outline
        "div.section" : function(e) {
        	level = jQuery(e).parents("div.section").length;
        	//only treat top level section
        	if(level == 0){
      			handle(e);
        	}
        }
    });
}());
