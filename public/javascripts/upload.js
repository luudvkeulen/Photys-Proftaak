function controlSelection() {
    $('.ui-autocomplete-input').prop('disabled', true).val('');
    $('.tagit-choice').remove();
    $('.ui-widget-content').css('opacity','.2');
    $('.emails-group').css('display','none');
    if (document.getElementById('rb1').checked) {
        document.getElementById("sel1").disabled = false;
        document.getElementById("tbDesc").disabled = true;
        document.getElementById("tbName").disabled = true;
        document.getElementById("cb1").disabled = true;
        document.getElementById("cb1").checked = false;
        //$("#emails").tagit({ readOnly: true });
    } else {
        document.getElementById("sel1").disabled = true;
        document.getElementById("tbDesc").disabled = false;
        document.getElementById("tbName").disabled = false;
        document.getElementById("cb1").disabled = false;
        document.getElementById("cb1").checked = false;
        //$("#emails").tagit({ readOnly: false });
    }
}



$(document).ready(function () {
    controlSelection();


    $('#cb1').change(function () {
        if($(this).is(':checked')){
            $('.emails-group').css('display','block');
            $('.ui-widget-content').css('opacity','1');
            $('.ui-autocomplete-input').prop('disabled', false);
        }else {
            $('.emails-group').css('display','none');
            $('.ui-autocomplete-input').prop('disabled', true).val('');
            $('.tagit-choice').remove();
            $('.ui-widget-content').css('opacity','.2');
        }
    });

    $('input[type=radio]').change(function () {
        // When any radio button on the page is selected,
        // then deselect all other radio buttons.
        $('input[type=radio]:checked').not(this).prop('checked', false);
        controlSelection();
        console.log($('#emails').tagit('assignedTags'));

    });

    $('#tbPrice').keyup(function (e) {
        var tbprice = document.getElementById('tbPrice');
        var fieldLength = tbprice.value.length;

        var code = (e.keyCode || e.which);

        if ((Math.round(parseFloat(tbprice.value) * 100) / 100) > 9.99) {
            tbprice.value = (Math.round(parseFloat(tbprice.value) * 100) / 100) / 10;
        }

        if ((Math.round(parseFloat(tbprice.value) * 100) / 100) < 0.01) {
            tbprice.value = 0.01;
        }

        if (code == 37 || code == 38 || code == 39 || code == 40 || code == 188) {
            return;
        }

        if (!fieldLength <= 3) {
            var str = tbprice.value;
            str = str.substring(0, 4);
            tbprice.value = str;
        }

        $("#emails").data("ui-tagit").tagInput.addClass("smoothness");
    });

    var emailRegEx = new RegExp(/^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i);
    $('#emails').tagit({
        beforeTagAdded: function (event, ui) {
            if ( !emailRegEx.test(ui.tagLabel) ) {
                ui.tag.addClass('invalid-tag');
                return false;
            } else {
                ui.tag.removeClass('invalid-tag');
            }
            return true;
        },
        fieldName: "Emails"
    });



    $('.ui-autocomplete-input').prop('disabled', true).val('');
    $('.tagit-choice').remove();
    $('.ui-widget-content').css('opacity','.2');
});


$(document).on('click', '#close-preview', function () {
    $('.image-preview').popover('hide');
    // Hover befor close the preview
    $('.image-preview').hover(
        function () {
            $('.image-preview').popover('show');
        },
        function () {
            $('.image-preview').popover('hide');
        }
    );
});


$(function () {
    // Create the close button
    var closebtn = $('<button/>', {
        type: "button",
        text: 'x',
        id: 'close-preview',
        style: 'font-size: initial;',
    });
    closebtn.attr("class", "close pull-right");
    // Set the popover default content
    $('.image-preview').popover({
        trigger: 'manual',
        html: true,
        title: "<strong>Preview</strong>" + $(closebtn)[0].outerHTML,
        content: "There's no image",
        placement: 'bottom'
    });
    // Clear event
    $('.image-preview-clear').click(function () {
        $('.image-preview').attr("data-content", "").popover('hide');
        $('.image-preview-filename').val("");
        $('.image-preview-clear').hide();
        $('.image-preview-input input:file').val("");
        $(".image-preview-input-title").text("Browse");
    });
    // Create the preview image
    $(".image-preview-input input:file").change(function () {
        var img = $('<img/>', {
            id: 'dynamic',
            width: 250,
            height: 200
        });
        var file = this.files[0];
        var reader = new FileReader();
        // Set preview image into the popover data-content
        reader.onload = function (e) {
            $(".image-preview-input-title").text("Change");
            $(".image-preview-clear").show();
            $(".image-preview-filename").val(file.name);
            img.attr('src', e.target.result);
            $(".image-preview").attr("data-content", $(img)[0].outerHTML).popover("show");
        }
        reader.readAsDataURL(file);
    });
});