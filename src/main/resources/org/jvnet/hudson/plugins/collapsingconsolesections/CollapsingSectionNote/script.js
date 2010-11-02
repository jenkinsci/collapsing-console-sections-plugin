function doToggle(o)
{
    var section = o.parentNode.parentNode;
    if (section.nextElementSibling.className == "collapsed") {
        section.nextElementSibling.className = "expanded";
        o.innerHTML = "Hide Details";
    } else {
        section.nextElementSibling.className = "collapsed";
        o.innerHTML = "Show Details";
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
        if (typeof object == "string")
                object = document.getElementById(object);
        offsets.x += object.offsetLeft;
        offsets.y += object.offsetTop;
        do {
                object = object.offsetParent;
                if (! object)
                        break;
                offsets.x += object.offsetLeft;
                offsets.y += object.offsetTop;
        } while(object.tagName.toUpperCase() != "BODY");
        return offsets;
}

    function initFloatingSection() {
        var d = document.getElementById("console-section-container");
        if (d==null) return;

        window.onscroll = function() {
            var offsets = getoffsets(d);
            var floatSection = d.childNodes[0];

            if (offsets.y - window.scrollY <= 5) {
                if (floatSection.className != "scrollDetached") {
                    floatSection.className = "scrollDetached";
                }

                floatSection.style["left"] = -window.scrollX + offsets.x + "px";
            } else {
                if (floatSection.className != "scrollAttached") {
                    floatSection.className = "scrollAttached";
                }
            }
        }
    }

    function loadOutline() {
        if (outline!=null)  return false;   // already loaded

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

    function handle(e) {
        if (loadOutline()) {
            queue.push(e);
        } else {
            var id = "console-section-"+(iota++);
            outline.appendChild(parseHtml("<li><a href='#"+id+"'>"+e.childNodes[0].data+"</a></li>"))

            if (document.all)
                e.innerHTML = '<a name="' + id + '"/>' + e.innerHtml;  // IE8 loses "name" attr in appendChild
            else {
                var a = document.createElement("a");
                a.setAttribute("name",id);
                e.parentNode.insertBefore(a, e);
            }
        }
    }

    Behaviour.register({
        // insert <a name="..."> for each console section and put it into the outline
        "div.collapseHeader" : function(e) {
            handle(e);
        }
    });
}());
