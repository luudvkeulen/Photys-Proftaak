/**
 * Created by M on 12/21/2016.
 */

$(document).ready(function(){
    // Admin pages
    // Add new product with name, price and description
    var newProduct = null;
    $(".btnAddProduct").click(function(){
        if(newProduct != null){
            alert("Add product before making a new product");
        }else{
            newProduct = document.createElement("div");
            var productName = document.createElement("label");
            var linebreak = document.createElement("br");
            var linebreak2 = document.createElement("br");
            var linebreak3 = document.createElement("br");
            var linebreak4 = document.createElement("br");
            var productNameInsert = document.createElement("input");
            var productPrice = document.createElement("label");
            var productPriceInsert = document.createElement("input");
            var productDescription = document.createElement("label");
            var productDescriptionInsert = document.createElement("input");
            var productImage = document.createElement("img");
            var addNewProduct = document.createElement("input");

            productImage.src = "http://downloadicons.net/sites/default/files/product-icon-27962.png";
            productImage.style.marginLeft = "15px"
            productImage.width = "100";
            productImage.height = "100";
            productName.innerHTML = "Name";
            productPrice.innerHTML = "Price";
            productDescription.innerHTML = "Description";
            productNameInsert.name = "productName";
            productPriceInsert.name = "productPrice";
            productDescriptionInsert.name = "productDescription";
            productName.style.marginLeft = "15px"
            productPrice.style.marginLeft = "15px"
            productNameInsert.setAttribute("type", "text");
            productPriceInsert.setAttribute("type", "text");
            productNameInsert.setAttribute("class", "form-control");
            productPriceInsert.setAttribute("class", "form-control");
            productDescriptionInsert.setAttribute("class", "form-control");
            addNewProduct.innerHTML = "add product";
            addNewProduct.setAttribute("class", "btn btn-primary");
            addNewProduct.setAttribute("type", "submit");
            addNewProduct.id = "btnAddFinishedProduct";

            newProduct.appendChild(productImage);
            newProduct.appendChild(linebreak);
            newProduct.appendChild(productName);
            newProduct.appendChild(productNameInsert);
            newProduct.appendChild(linebreak2);
            newProduct.appendChild(productDescription);
            newProduct.appendChild(productDescriptionInsert);
            newProduct.appendChild(linebreak3);
            newProduct.appendChild(productPrice);
            newProduct.appendChild(productPriceInsert);
            newProduct.appendChild(linebreak4);
            newProduct.appendChild(addNewProduct);
            document.getElementById("product").appendChild(newProduct);
        }
    });
});