/**
 * Created by hp on 8-11-2016.
 */


$(document).ready(function(){
    $('#collapsable').click(function (e){
        var chevState = $(e.target).siblings("i.indicator").toggleClass('glyphicon-chevron-down glyphicon-chevron-up');
        $("i.indicator").not(chevState).removeClass("glyphicon-chevron-down").addClass("glyphicon-chevron-up");
    });
});