
$(document).ready(function(){
	// UPLOAD PAGE
	$(".btnCreateAlbum").click(function(){
		alert("Choose existing pictures to create album");
    });

    $(".photographer").click(function() {
    	$(".selected").removeClass("selected");
        $(".photographerid").prop('disabled',true);
		$(this).addClass("selected");
        var targetdiv = (this).getElementsByClassName("photographerid")[0];
        targetdiv.disabled = false;

        return false;
    });
});