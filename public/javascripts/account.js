$(document).ready(function () {
    controlSelection();
});

function controlSelection() {
    var select = document.getElementById("sel1");
    var setting = select.options[select.selectedIndex].value;

    if (setting == "email") {
        document.getElementById("inputSelect1").placeholder = "Emailadres";
        document.getElementById("inputSelect2").placeholder = "";
        document.getElementById("inputSelect2").disabled = true;
    } else if (setting == "name") {
        document.getElementById("inputSelect1").placeholder = "First name";
        document.getElementById("inputSelect2").disabled = false;
        document.getElementById("inputSelect2").placeholder = "Last name";
    } else if (setting == "password") {
        document.getElementById("inputSelect1").placeholder = "Old password";
        document.getElementById("inputSelect2").disabled = false;
        document.getElementById("inputSelect2").placeholder = "New password";
    } else if (setting == "zipcode") {
        document.getElementById("inputSelect1").placeholder = "Zipcode";
        document.getElementById("inputSelect2").placeholder = "";
        document.getElementById("inputSelect2").disabled = true;
    } else if (setting == "adres") {
        document.getElementById("inputSelect1").placeholder = "Street";
        document.getElementById("inputSelect2").disabled = false;
        document.getElementById("inputSelect2").placeholder = "Housenumber";
    } else if (setting == "phonenr") {
        document.getElementById("inputSelect1").placeholder = "Phonenumber";
        document.getElementById("inputSelect2").placeholder = "";
        document.getElementById("inputSelect2").disabled = true;
    }
}