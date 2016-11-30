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
    var filters = document.getElementsByName('filter').value;
    var filter = document.querySelector('input[name = "filter"]:checked').value;

    switch(filter) {
        case "none":
            document.getElementById('image').className = "";
            break;
        case "sepia":
            document.getElementById('image').className = "sepia";
            break;
        case "bw":
            document.getElementById('image').className = "bw";
            break;
        case "inverted":
            document.getElementById('image').className = "invert";
            break;
        case "blur":
            document.getElementById('image').className = "blur";
            break;
        case "dark":
            document.getElementById('image').className = "darken";
            break;
    }
}