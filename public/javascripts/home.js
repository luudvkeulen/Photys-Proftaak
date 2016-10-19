$(document).ready(function(){
    // var m = new Masonry($('.masonry-container').get()[0], {
    //     itemSelector: ".item"
    // });

    var $grid = $('.masonry-container').masonry({
        // options...
    });

    $grid.imagesLoaded().progress( function() {
        $grid.masonry('layout');
    });
});