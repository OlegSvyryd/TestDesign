/**
 * Created by aquarius on 8/25/2015.
 */
angular.module('ContactsValues', [])
    .value('ContactsValues', {
        spinneropts : {
            lines: 10 // The number of lines to draw
            , length: 10 // The length of each line
            , width: 8 // The line thickness
            , radius: 18 // The radius of the inner circle
            , scale: 1 // Scales overall size of the spinner
            , corners: 1 // Corner roundness (0..1)
            , color: 'green' // #rgb or #rrggbb or array of colors
            , opacity: 0.35 // Opacity of the lines
            , rotate: 0 // The rotation offset
            , direction: 1 // 1: clockwise, -1: counterclockwise
            , speed: 2 // Rounds per second
            , trail: 53 // Afterglow percentage
            , fps: 20 // Frames per second when using setTimeout() as a fallback for CSS
            , zIndex: 2e9 // The z-index (defaults to 2000000000)
            , className: 'spinner' // The CSS class to assign to the spinner
            , top: '50%' // Top position relative to parent
            , left: '80%' // Left position relative to parent
            , shadow: false // Whether to render a shadow
            , hwaccel: false // Whether to use hardware acceleration
            , position: 'absolute' // Element positioning
        },
        recaptcha : {
            key: '6Les_AsTAAAAAB2_jkT_xBMudI07A5Jmn0kbYods'
        },
        googlemap: {
            latitude : 49.863400
            ,
            longitude : 24.044500
            ,
            zoom : 16
            ,
            markerhint : 'Наше розташування!'
        },
		workshedule : [
		{ day :	"MONDAY",
		  time : "9:00 to 18:00"
		},
		{ day :	"TUESDAY",
		  time : "9:00 to 18:00"
		},
		{ day :	"WEDNESDAY",
		  time : "9:00 to 18:00"
		},
		{ day :	"THURSDAY",
		  time : "9:00 to 18:00"
		},
		{ day :	"FRIDAY",
		  time : "9:00 to 18:00"
		},
		{ day :	"SATURDAY",
		  time : "9:00 to 18:00"
		},
		{ day :	"SUNDAY",
		  time : "9:00 to 18:00"
		},
		]
    });
