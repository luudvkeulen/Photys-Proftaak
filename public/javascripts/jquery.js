
$(document).ready(function(){
	/*
	$(".btnLogin").click(function(){
	   window.location.href='C:\\Fontys\\S4\\PTS4\\Week 3\\Account.html';
	})
	*/

	$(".btnEdit").click(function(){
	   window.location.href='C:\\Fontys\\S4\\PTS4\\Week 3\\Edit.html';
	})

	$(".btnAdd").click(function(){
	   window.location.href='C:\\Fontys\\S4\\PTS4\\Week 3\\Cart.html';
	})

	$(".btnCart").click(function(){
	   window.location.href='C:\\Fontys\\S4\\PTS4\\Week 3\\Cart.html';
	})

	// CART PAGE
	$(".btnPayment").click(function(){
	   window.location.href='C:\\Fontys\\S4\\PTS4\\Week 3\\Payment.html';
	})

	$(".btnAccount").click(function(){
	   window.location.href='C:\\Fontys\\S4\\PTS4\\Week 3\\Account.html';
	})

	$("#granny").click(function(){
	   window.location.href='C:\\Fontys\\S4\\PTS4\\Week 3\\Edit.html';
	})

	// UPLOAD PAGE
	$(".btnCreateAlbum").click(function(){
		alert("Choose existing pictures to create album");
	})

	$(".btnUpload").click(function(){
		window.location.href='C:\\Fontys\\S4\\PTS4\\Week 3\\Upload.html';
	})

	// PHOTOGRAPHER LOGIN
	$(".btnLogin").click(function(){
		var naam = $("#inputEmail").val();
		if(naam == "photographer"){
			window.location.href='C:\\Fontys\\S4\\PTS4\\Week 3\\Photgrapher.html';
		}
		else if(naam == "user"){
			window.location.href='C:\\Fontys\\S4\\PTS4\\Week 3\\Account.html';
		}
		else if(naam == "admin"){
			window.location.href='C:\\Fontys\\S4\\PTS4\\Week 3\\Admin.html';
		}
		else{
			alert("Name or password is incorrect");
		}
	})

	// ADMIN PAGE
	$("#pic01").click(function(){
		$("#pic01").css("background-color", "rgb(66, 164, 244)");
		//window.location.href='C:\\Fontys\\S4\\PTS4\\Week 3\\PhotographerTest2.html';
		alert(photographerID);
	})
	$("#pic02").click(function(){
		$("#pic02").css("background-color", "lightblue");
		//window.location.href='C:\\Fontys\\S4\\PTS4\\Week 3\\PhotographerTest2.html';
		alert(photographerID);
	})
	$("#pic03").click(function(){
		var color = $("#pic03").css("background-color", "lightblue");
		//window.location.href='C:\\Fontys\\S4\\PTS4\\Week 3\\Photgrapher.html';
		alert(photographerID);
	})
	$("#pic04").click(function(){
		var color = $("#pic04").css("background-color", "lightblue");
		//window.location.href='C:\\Fontys\\S4\\PTS4\\Week 3\\Photgrapher.html';
		alert(photographerID);
	})

	$(".btnCheckProfile").click(function(){
		window.location.href='C:\\Fontys\\S4\\PTS4\\Week 3\\PhotographerTest1.html';
	})

	$(".btnAcceptPhotographer").click(function(){
		$("#pic01").hide("slow");
	})
});