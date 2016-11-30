var items = ["test1", "test2", "test3", "test4"];
var item = "";
var count = 1;
var node = document.createElement("span");
var quantity;
var buttonUp;
var amountOfItems = 0;
var totalCost = 0;
var itemCost = 0;
var parentNode2;
var countID = 0;
var itemID = 0;

// GUI elements
var element = document.createElement("div");
var labelAmountOfItems = document.createElement("label");
var labelAmountOfItemsShow = document.createElement("label");
var labelTotalPrice = document.createElement("label");
var labelTotalPriceShow = document.createElement("label");

document.addEventListener("DOMContentLoaded", function(event) { 
	//setupInformation();
  	//document.getElementById("itemAmount").innerHTML = " 0 - items";

  	// GUI componenten inladen voor het berekenen van de prijs
  	//createGuiPriceComponents();
  	// Totale kosten berekenen
  	//calculateAmount();

  	var size = items.length;
  	if(size > 0){
  		document.getElementById("itemAmount").innerHTML= size + " - items";
  	}
});

function addItems(ele){
	// Aanmaken van een item in de navigatiebalk cart
	var lineBreak = document.createElement("br");
	var node = document.createElement("span");
    var textnode = document.createTextNode("Product");
    var pricenode = document.createTextNode(" $14,-");
    var img = document.createElement('img');
    // ID geven en ophogen per aangemaakte item
    countID += 1;
    node.id = countID;
	//img.src = 'http://www.bajiroo.com/wp-content/uploads/2016/01/weird-unusual-haircuts-pics-pictures-images-photos-1-600x692.png';
	//img.style.height = "20px";
	//img.style.width = "20px";
    //node.appendChild(img);
    //node.appendChild(textnode);
    //node.appendChild(pricenode);
    //document.getElementById("item").appendChild(node);
    //document.getElementById("item").appendChild(lineBreak);

    // Ophogen van een item in de quantity label.
    for(var i = 0; i < items.length; i++){
	    if(ele.target.id == i){
			quantity.id = ele.target.id;
			console.log(quantity.id);
			count = parseInt(document.getElementById("label"+quantity.id).innerHTML);
			count += 1;
			document.getElementById("label"+quantity.id).innerHTML = count;
	    }
    }

    // Ophalen van de items in de span element
    var i = document.getElementById("item").appendChild(node);
    // Toevoegen van de item naar de array
    items.push(i);
    // Toevoegen van de hoeveelheid producten
    console.log(quantity);
    // Ophalen van de items
    //getItems();
    // Berekenen van de totale prijs
    //calculateAmount();
}

function removeItems(ele){
	if(count > 0){
		console.log(quantity.id);
		count = parseInt(document.getElementById("label"+quantity.id).innerHTML);
		count -= 1; 
		document.getElementById("label"+quantity.id).innerHTML = count;

		var i = document.getElementById("item").appendChild(node);
		if(i){
			//alert(i.target.id);
			i.parentNode.removeChild(i);
		}
		//i.parentNode.removeChild(i); NOG TE FIXE.. Toegevoegde specifieke element verwijderen
    	items.splice(i, 1);
		getItems();
	}
	else{
		alert("Cannot remove less than 0 zero items");
	}
	// Berekenen van de totale prijs
	calculateAmount();
}

function getItems(){
  var size = items.length;
  if(size > 0){
  	document.getElementById("itemAmount").innerHTML= size + " - items";
  }
}

function setupInformation(){
	for(var i = 0; i < items.length; i++){
		var lineBreak = document.createElement("br");
		var node = document.createElement("div");
	    var textnode = document.createTextNode("Picture of a product");
	    var priceValuta  = document.createTextNode(" $");
	    var pricenode = document.createTextNode(parseInt("14"));
	    //var price = parseInt(pricenode).text();
	    var img = document.createElement('img');
	    var buttonDown = document.createElement("button");
	    buttonUp = document.createElement("button");
	    quantity = document.createElement("label");

		img.src = 'https://www.yellowoctopus.com.au/media/catalog/product/cache/1/image/500x500/9df78eab33525d08d6e5fb8d27136e95/y/e/yellow-octopus-mugshot-mug.jpg';
		img.style.height = "50px";
		img.style.width = "50px";
		quantity.id = "label"+i;
		quantity.innerHTML = "1";
		buttonDown.innerHTML  = "-";
		buttonUp.innerHTML  = "+";
		buttonUp.id = i;

	    node.appendChild(img);
	    node.appendChild(textnode);
	    node.appendChild(priceValuta); // Shows the valuta before the actual price
	    node.appendChild(pricenode); // Shows the actual price of the item
	    node.appendChild(buttonDown);
	    node.appendChild(quantity);
	    node.appendChild(buttonUp);
	    document.getElementById("collapse1").appendChild(node);
	    document.getElementById("collapse1").appendChild(lineBreak);

	    buttonUp.onclick = addItems;
	    buttonDown.onclick = removeItems;  
	}
}

function createGuiPriceComponents(){
	labelAmountOfItems.innerHTML = "Amount of items: ";
	labelAmountOfItemsShow.innerHTML = parseInt("0");
	labelTotalPrice.innerHTML = "Total price: $";
	labelTotalPriceShow.innerHTML = parseInt("0");

	element.appendChild(labelAmountOfItems);
	element.appendChild(labelAmountOfItemsShow);
	element.appendChild(labelTotalPrice)
	element.appendChild(labelTotalPriceShow);
	document.getElementById("costs").appendChild(element);
}

function calculateAmount(){
	// Berekenen van de totale kosten
	itemCost = 14;
	amountOfItems = items.length;
	totalCost = amountOfItems * itemCost;

	labelAmountOfItemsShow.innerHTML = parseInt(amountOfItems);
	labelTotalPriceShow.innerHTML = parseInt(totalCost);
}