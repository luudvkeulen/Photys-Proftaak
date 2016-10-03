
$(document).ready(function(){
	// UPLOAD PAGE
	$(".btnCreateAlbum").click(function(){
		alert("Choose existing pictures to create album");
	})

    $(".photographer").click(function() {
    	$(".selected").removeClass("selected");
		$(this).addClass("selected");
        return false;
    });
});