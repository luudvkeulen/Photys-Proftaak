$(document).ready(function () {
    controlSelection();
});

function controlSelection() {
    var select = document.getElementById("sel1");
    var setting = select.options[select.selectedIndex].value;

    if (setting == "email") {
        var obj1 = document.getElementById("inputSelect1");
        var newO1 = document.createElement('input');
        newO1.setAttribute('id', 'inputSelect1');
        newO1.setAttribute('name', 'inputSelect1');
        newO1.setAttribute('type', 'text');
        newO1.setAttribute('class', 'form-control');
        newO1.placeholder = "Emailadres";
        newO1.setAttribute('name', obj1.getAttribute('name'));
        newO1.addEventListener("onblur", function () {
            newO1.parentNode.replaceChild(obj1, newO1)
        }, false);
        obj1.parentNode.replaceChild(newO1, obj1);

        var obj2 = document.getElementById("inputSelect2");
        var newO2 = document.createElement('input');
        newO2.setAttribute('id', 'inputSelect2');
        newO2.setAttribute('name', 'inputSelect2');
        newO2.setAttribute('type', 'text');
        newO2.setAttribute('class', 'form-control');
        newO2.placeholder = "";
        newO2.disabled = true;
        newO2.setAttribute('name', obj2.getAttribute('name'));
        newO2.addEventListener("onblur", function () {
            newO2.parentNode.replaceChild(obj2, newO2)
        }, false);
        obj2.parentNode.replaceChild(newO2, obj2);

    } else if (setting == "name") {

        var obj1 = document.getElementById("inputSelect1");
        var newO1 = document.createElement('input');
        newO1.setAttribute('id', 'inputSelect1');
        newO1.setAttribute('name', 'inputSelect1');
        newO1.setAttribute('type', 'text');
        newO1.setAttribute('class', 'form-control');
        newO1.placeholder = "First name";
        newO1.setAttribute('name', obj1.getAttribute('name'));
        newO1.addEventListener("onblur", function () {
            newO1.parentNode.replaceChild(obj1, newO1)
        }, false);
        obj1.parentNode.replaceChild(newO1, obj1);

        var obj2 = document.getElementById("inputSelect2");
        var newO2 = document.createElement('input');
        newO2.setAttribute('id', 'inputSelect2');
        newO2.setAttribute('name', 'inputSelect2');
        newO2.setAttribute('type', 'text');
        newO2.setAttribute('class', 'form-control');
        newO2.placeholder = "Last name";
        newO2.disabled = false;
        newO2.setAttribute('name', obj2.getAttribute('name'));
        newO2.addEventListener("onblur", function () {
            newO2.parentNode.replaceChild(obj2, newO2)
        }, false);
        obj2.parentNode.replaceChild(newO2, obj2);

    } else if (setting == "password") {

        var obj1 = document.getElementById("inputSelect1");
        var newO1 = document.createElement('input');
        newO1.setAttribute('id', 'inputSelect1');
        newO1.setAttribute('name', 'inputSelect1');
        newO1.setAttribute('type', 'password');
        newO1.setAttribute('class', 'form-control');
        newO1.setAttribute('minlength', '6');
        newO1.placeholder = "Old password";
        newO1.setAttribute('name', obj1.getAttribute('name'));
        newO1.addEventListener("onblur", function () {
            newO1.parentNode.replaceChild(obj1, newO1)
        }, false);
        obj1.parentNode.replaceChild(newO1, obj1);

        var obj2 = document.getElementById("inputSelect2");
        var newO2 = document.createElement('input');
        newO2.setAttribute('id', 'inputSelect2');
        newO2.setAttribute('name', 'inputSelect2');
        newO2.setAttribute('type', 'password');
        newO2.setAttribute('class', 'form-control');
        newO2.setAttribute('minlength', '6');
        newO2.placeholder = "New password";
        newO2.setAttribute('name', obj2.getAttribute('name'));
        newO2.addEventListener("onblur", function () {
            newO2.parentNode.replaceChild(obj2, newO2)
        }, false);
        obj2.parentNode.replaceChild(newO2, obj2);

    } else if (setting == "zipcode") {

        var obj1 = document.getElementById("inputSelect1");
        var newO1 = document.createElement('input');
        newO1.setAttribute('id', 'inputSelect1');
        newO1.setAttribute('name', 'inputSelect1');
        newO1.setAttribute('type', 'text');
        newO1.setAttribute('class', 'form-control');
        newO1.placeholder = "Zipcode";
        newO1.setAttribute('name', obj1.getAttribute('name'));
        newO1.addEventListener("onblur", function () {
            newO1.parentNode.replaceChild(obj1, newO1)
        }, false);
        obj1.parentNode.replaceChild(newO1, obj1);

        var obj2 = document.getElementById("inputSelect2");
        var newO2 = document.createElement('input');
        newO2.setAttribute('id', 'inputSelect2');
        newO2.setAttribute('name', 'inputSelect2');
        newO2.setAttribute('type', 'text');
        newO2.setAttribute('class', 'form-control');
        newO2.placeholder = "";
        newO2.disabled = true;
        newO2.setAttribute('name', obj2.getAttribute('name'));
        newO2.addEventListener("onblur", function () {
            newO2.parentNode.replaceChild(obj2, newO2)
        }, false);
        obj2.parentNode.replaceChild(newO2, obj2);

    } else if (setting == "adres") {

        var obj1 = document.getElementById("inputSelect1");
        var newO1 = document.createElement('input');
        newO1.setAttribute('id', 'inputSelect1');
        newO1.setAttribute('name', 'inputSelect1');
        newO1.setAttribute('type', 'text');
        newO1.setAttribute('class', 'form-control');
        newO1.placeholder = "Street";
        newO1.setAttribute('name', obj1.getAttribute('name'));
        newO1.addEventListener("onblur", function () {
            newO1.parentNode.replaceChild(obj1, newO1)
        }, false);
        obj1.parentNode.replaceChild(newO1, obj1);

        var obj2 = document.getElementById("inputSelect2");
        var newO2 = document.createElement('input');
        newO2.setAttribute('id', 'inputSelect2');
        newO2.setAttribute('name', 'inputSelect2');
        newO2.setAttribute('type', 'text');
        newO2.setAttribute('class', 'form-control');
        newO2.placeholder = "Housenumber";
        newO2.disabled = false;
        newO2.setAttribute('name', obj2.getAttribute('name'));
        newO2.addEventListener("onblur", function () {
            newO2.parentNode.replaceChild(obj2, newO2)
        }, false);
        obj2.parentNode.replaceChild(newO2, obj2);

    } else if (setting == "phonenr") {

        var obj1 = document.getElementById("inputSelect1");
        var newO1 = document.createElement('input');
        newO1.setAttribute('id', 'inputSelect1');
        newO1.setAttribute('name', 'inputSelect1');
        newO1.setAttribute('type', 'text');
        newO1.setAttribute('class', 'form-control');
        newO1.placeholder = "Phonenumber";
        newO1.setAttribute('name', obj1.getAttribute('name'));
        newO1.addEventListener("onblur", function () {
            newO1.parentNode.replaceChild(obj1, newO1)
        }, false);
        obj1.parentNode.replaceChild(newO1, obj1);

        var obj2 = document.getElementById("inputSelect2");
        var newO2 = document.createElement('input');
        newO2.setAttribute('id', 'inputSelect2');
        newO2.setAttribute('name', 'inputSelect2');
        newO2.setAttribute('type', 'text');
        newO2.setAttribute('class', 'form-control');
        newO2.placeholder = "";
        newO2.disabled = true;
        newO2.setAttribute('name', obj2.getAttribute('name'));
        newO2.addEventListener("onblur", function () {
            newO2.parentNode.replaceChild(obj2, newO2)
        }, false);
        obj2.parentNode.replaceChild(newO2, obj2);
        
    }
}