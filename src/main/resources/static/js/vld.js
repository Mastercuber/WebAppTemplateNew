/**
 *  --------------------------------------------------------------------------------------------------------------------
 *  Voldemord JavaScript Library v0.2
 *
 *  Note: The module needs the DOM. To prevent working with an empty DOM
 *        please include this script at the bottom of the body tag
 *
 *  Uses JQuery
 *  -> Includes Sizzle.js
 *  -> https://sizzlejs.com/ (CSS Selectors Engine)
 *
 *  Inlcude jquery first!
 *  Then include common.js, or whatever is the name of this Script
 *
 *  Use vld, window.vld, document.vld, voldemord, window.voldemord, document.voldemord
 *  --------------------------------------------------------------------------------------------------------------------
 */

/**
 *  Globals
 *  These Variables and Functions exist even if the document is not ready!
 */
var isHover = false;

/**
 *  Event Handling Module
 */
$(document).ready(function () {
    /**
     *  Local Listeners Definition (LLD)
     *  Listeners for different 'class', 'id', 'elements', on window or document etc..
     */
    // Click listener
    $("#submitMessageForm").click(function() {

    });
    // Other listeners
    $(document.getElementById('content').children[0]).mouseover(function () {
        isHover = true;
        var buttons = $('#content').children().first()[0].getElementsByTagName('button');
        var indicators = $('#content').children().first()[0].getElementsByTagName('div')[0];

        buttons[0].className = buttons[0].className.replace('w3-hide', 'w3-show');
        buttons[1].className = buttons[1].className.replace('w3-hide', 'w3-show');
        indicators.className = indicators.className.replace('w3-hide', 'w3-show');
    });
    $(document.getElementById('content').children[0]).mouseout(function () {
        isHover = false;
        var buttons = $('#content').children().first()[0].getElementsByTagName('button');
        var indicators = $('#content').children().first()[0].getElementsByTagName('div')[0];

        buttons[0].className = buttons[0].className.replace('w3-show', 'w3-hide');
        buttons[1].className = buttons[1].className.replace('w3-show', 'w3-hide');
        indicators.className = indicators.className.replace('w3-show', 'w3-hide');
    });
    /**
     *  Global Listeners Definition (GLD)
     * 	Listener for doing work around ajax requests
     *
     *  Note: You can set the 'global' attribute in the Ajax request to FALSE if you DO NOT want these listeners to
     *	listen for this ajax request
     */

    $(document).bind("ajaxStart "), function (event) {
        // This event is triggered if an Ajax request is started and no other Ajax requests are currently running.
    }.bind("ajaxSend", function(event){
        // This global event is also triggered before the request is run.
    }).bind("ajaxSuccess", function (event) {
        // This event is also only called if the request was successful.
    }).bind("ajaxComplete", function(event){
        // This event behaves the same as the complete event and will be triggered every time an Ajax request finishes.
    }).bind("ajaxError", function (event) {
        // This global event behaves the same as the local error event.
    }).bind("ajaxStop", function (event) {
        // This global event is triggered if there are no more Ajax requests being processed.
    });


    /*
     var event = new Event('build1');
     var event2 = new CustomEvent('build1', { 'document' : document, 'windowEl' : window });

     event2.windowEl;
     event2.document;

     // Listen for the event.
     document.body.children[0].addEventListener('build1', function () {
     console.log('build1 is fired!');
     });

     // Dispatch the event.
     debugger
     document.body.dispatchEvent(event2);*/
});

/**
 *  Voldemord Module for AMD, CommonJS or global + global Translations object
 */
// Define an anonymouse function
(function(root, factory) {
    //"use strict"; // 1 strict thing: non-strict-mode = call/apply/bind context is replaced by global object if context is null or undefined
    //                 strict-mode = context will not be replaced if null or undefined
    if (typeof define === 'function' && define.amd) {
        // AMD
        define( "voldemord", [], factory );
    } else if ((typeof module === 'object' || typeof module === 'function') && typeof module.exports === 'object') {
        // CommonJS
        module.exports = factory();
    }

    // Browser globals (Note: root is window)
    // vld, witch comes out of the factory, needs to be exposed
    // to the window object (root) for direct access AND to the document
    if (typeof root === 'object') {
        window.voldemord = window.vld = document.vld = document.voldemord = $.vld = $.voldemord = factory();
    }

    /**
     *  Global Variables and Functions
     *  This mean: direct access in console
     *  Suggestion: load a translation file and make it global, for internationalisation
     */
    root.Translations = {
        welcome : 'Willkommen'
    };

// Execute this function.
// this = window = root, anonymouse Function = factory
}(this, function() {

    /** ----------------------------------
     *  Responsive methods and components
     *  ----------------------------------
     *  - Constructor
     *  - Prototypes
     */
    var Responsive = function () {
        // Properties for Responsive

        // Initialisation for Responsive
        // Set min-heihgt to height and height to auto to get responsive behavior.
        // Use responsive attribute on tag level (No value, just key is enough)
        $('[responsive]').each(function (index, element) {
            $(element).css('min-height', $(element).height());
            $(element).css('height', 'auto');
        });

        $.each($('#sixElementContainer').children(), function (index, child) {
            var grandChilds = child.children;
            $.each(grandChilds, function (index, grandChild) {
                $(grandChild).width($(grandChild).width() - 45);
            });
        });
    }

    Responsive.prototype = {
        placeResponsiveNavbar: function(event, childIndex) {
            $('#responsiveNavbarItems').css('padding-top', 55);
        },
        toggleResponsiveNavbar: function(responsiveNavbarId) {
            var responsiveNavbar = document.getElementById(responsiveNavbarId);
            if (responsiveNavbar == null) {
                console.log('Provide a responsive navbar id. This is the content witch is hidden by default and shown when you click the burger');
                return false;
            }
            if (responsiveNavbar.className.indexOf("w3-show") == -1) {
                responsiveNavbar.className += " w3-show";
            } else {
                responsiveNavbar.className = responsiveNavbar.className.replace(" w3-show", "");
            }
        }
    };

    /**
     *  ----------------------------------
     *  Search and filter tools
     *  ----------------------------------
     *
     *  Filter in a table:
     *  - Search a table element at most 20 times.
     *  -- Log if the tries where more than 20 times.
     *  - Process the first table element found!
     *  - display if the filter matches and hide if not.
     *  - useful for onkeyup/onclick/etc. events
     *
     *  - Constructor
     *  - Prototypes
     */
    var Filter = function () {
        // Properties for Filter

        // Initialisation for Filter

    }

    Filter.prototype = {
        filterTableByIdAndEvent: function (event, tableId) {
            if (event == null) {
                console.log('Please use this method only as eventHandler')
                return;
            }
            var input, filter, table = null, tr, td, i, j, k, parent, childnodes;
            input = event.currentTarget;
            filter = input.value.toUpperCase();
            table = document.getElementById(tableId);

            if (table == null || !table.classList.contains("w3-table-all")) {
                console.log("No table is available! Maybe the wrong id?");
                return;
            }

            tr = table.getElementsByTagName("tr");
            for (i = 0; i < tr.length; i++) {
                td = tr[i].getElementsByTagName("td");
                for (j = 0; j < td.length; j++) {
                    if (td[j]) {
                        if (td[j].innerHTML.toUpperCase().indexOf(filter) > -1) {
                            tr[i].style.display = "";
                            break;
                        } else {
                            tr[i].style.display = "none";
                        }
                    }
                }
            }
        },
    };

    /**--------------------------------------------------
     * Carousels, Slideshows and other Image view methods
     * --------------------------------------------------
     *  - Constructor
     *  - Prototypes
     */
    var Slideshow = function(){
        // Properties for Slideshow
        this.slideIndex = 0;

        // Initialisation for Slideshow
    };

    Slideshow.prototype = {
        plusDivs: function(n) {
            this.showDivs(this.slideIndex += n);
        },
        showDivs: function(slideIndex) {
            var i;
            var slides = document.getElementsByClassName("slide");

            if ((slideIndex+1) > slides.length) {
                this.slideIndex = 0;
                slideIndex = 0;
            }
            if (slideIndex < 0) {
                this.slideIndex = slides.length-1;
                slideIndex = slides.length-1;
            }
            for (i = 0; i < slides.length; i++) {
                slides[i].style.display = "none";
            }
            $(slides[slideIndex]).toggle();
            this.slideIndex = slideIndex;
            this.currentDiv(slideIndex);
        },
        // For marking the indicator in white
        currentDiv: function(index) {
            var children = $('#indicators').children();

            $.each(children, function (childIndex, child) {
                child.className = child.className.replace('w3-white','');
                if (child.className.indexOf('w3-transparent') == -1) {
                    child.className += ' w3-transparent';
                }
            });

            var activeChild = children[index];

            if (activeChild) {
                children[index].className = children[index].className.replace('w3-transparent', 'w3-white');
            } else {
                children[0].className = children[0].className.replace('w3-transparent', 'w3-white');
                this.slideIndex = 0;
            }
        },
        // Has to be called ones and then executes every 6 seconds, maybe webworker?
        carousel: function() {
            if (!isHover) {
                this.showDivs(this.slideIndex + 1);
            }
            setTimeout(this.carousel.bind(this), 6000); // Change image every 6 seconds
        }
    };


    /** @ToDo Kommentar scrheiben
     *  ----------------------------------
     *   Tools
     *  ----------------------------------
     *
     *  :
     *  - Search a table element at most 20 times.
     *  -- Log if the tries where more than 20 times.
     *  - Process the first table element found!
     *  - display if the filter matches and hide if not.
     *  - useful for onkeyup/onclick/etc. events
     *
     *  - Constructor
     *  - Prototypes
     **/
    var Audio = function () {
        // Properties for Audio

        this.microphoneStream = null;
        // With MediaRecorder we have a start, stop and RequestData Method
        // The mediaRecorder.ondataavailable(function(e){ e.data }) has to be declared to handle the data pieces
        this.mediaRecorder = null;

        this.fileReader = new FileReader();

        // Initialisation for Audio

    };
    Audio.prototype = {
        // Blob Management
        getUrlFromBlob: function (blob) {
            return (window.URL || window.webkitURL).createObjectURL(blob);
        },
        getBase64FromBlob: function (blob) {
            this.fileReader.onload = function () {
                return this.fileReader.result;
            }
            this.fileReader.readAsDataURL(blob);
        },
        initAudio: function () {
            console.log('Audio get initialized!');

            this.getStream(true, false);
        },
        initVideo: function () {
            console.log('Video get initialized!');

            this.getStream(false, true);
        },
        getStream: function (audio, video) {
            if (!navigator.getUserMedia)
                navigator.getUserMedia = navigator.webkitGetUserMedia || navigator.mozGetUserMedia;
            if (!navigator.cancelAnimationFrame)
                navigator.cancelAnimationFrame = navigator.webkitCancelAnimationFrame || navigator.mozCancelAnimationFrame;
            if (!navigator.requestAnimationFrame)
                navigator.requestAnimationFrame = navigator.webkitRequestAnimationFrame || navigator.mozRequestAnimationFrame;

            function gotStream(stream) {
                this.microphoneStream = stream;
                this.mediaRecorder = new MediaRecorder(stream);
            }

            navigator.getUserMedia({
                "audio": audio,
                "video": video
            }, gotStream.bind(this), function(e) {
                alert('Error getting audio');
                console.log(e);
            });
        }
    };

    // Constructor of the module
    var vld = function () {
        // Properties for vld
        this.version = "0.2"; // Define a local copy of voldemord vld;

        // Initialisation for vld
        this.document = document;
        this.window = window;
        this.navigator = navigator;
        this.console = console;
        this.parseJSON = JSON.parse;
        this.stringifyJSON = JSON.stringify;
    };

    // Glue the module together
    // Prototypes
    vld.prototype = {
        Responsive: new Responsive(),
        Filter: new Filter(),
        Slideshow: new Slideshow(),
        Audio: new Audio()
    };

    // Instanciate and return the module
    return new vld();
}));