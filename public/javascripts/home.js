$(document).ready(function(){
    var $grid = $('.masonry-container').masonry({});

    $grid.imagesLoaded().progress( function() {
        $grid.masonry('layout');
    });
});