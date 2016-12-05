/**
 * Created by hp on 8-11-2016.
 */


$(document).ready(function(){
    $('#collapsable').click(function (e){
        var chevState = $(e.target).siblings("i.indicator").toggleClass('glyphicon-chevron-down glyphicon-chevron-up');
        $("i.indicator").not(chevState).removeClass("glyphicon-chevron-down").addClass("glyphicon-chevron-up");
    });

});

function filterSelected () {
    var filter = document.querySelector('input[name = "filter"]:checked').value;

    switch(filter) {
        case "none":
            document.getElementById('image').className = "";
            document.getElementById('inputfilter').value = "NONE";
            break;
        case "sepia":
            document.getElementById('image').className = "sepia";
            document.getElementById('inputfilter').value = "SEPIA";
            break;
        case "bw":
            document.getElementById('image').className = "bw";
            document.getElementById('inputfilter').value = "BW";
            break;
        case "inverted":
            document.getElementById('image').className = "invert";
            document.getElementById('inputfilter').value = "INVERTED";
            break;
        case "blur":
            document.getElementById('image').className = "blur";
            document.getElementById('inputfilter').value = "BLURRED";
            break;
        case "dark":
            document.getElementById('image').className = "darken";
            document.getElementById('inputfilter').value = "DARK";
            break;
    }
}