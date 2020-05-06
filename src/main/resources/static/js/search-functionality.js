"use strict";

let citiesSet = new Set();
let isCitySidebarSearchClicked = false;

$(document).ready(function () {

    // Initialize bootstrap select plugin (needed for icon support in select option)
    $('.selectpicker').selectpicker({
        iconBase: 'fa',
        tickIcon: 'fa-check'
    });

    $(document).bind('keypress', function (e) {
        if (e.key === "Enter" && isCitySidebarSearchClicked) {
            populateCitiesTextArea($("#inputSearchQuery").val());
            $("#inputSearchQuery").val('');
        }
    });

    // Initialize autocomplete Value source
    $("#inputSearchQuery").autocomplete({
        source: function (request, response) {
            $.ajax({
                url: "/GroupProject/autocomplete-utility/city/" + request.term + "/",
                dataType: "json",
                // data: {
                //     q: request.term
                // },
                success: function (data) {
                    response(data);
                }
            });
        },
        minLength: 3,
        select: function (event, ui) {
            event.preventDefault();
            citiesSet.add(ui.item.value);
            populateCitiesTextArea();
            $("#inputSearchQuery").val('');
        }
    })
        .on("focus", function () {
            /* the element with the search results */
            //
            let uid = $("#ui-id-" + ($(this).autocomplete("instance").uuid + 1));
            if (uid.html().length === 0) {
                /* same as $(this).autocomplete("search", this.value); */
                $(this).keydown();
            } else {
                setTimeout(() => uid.show(), 200)

            }
        });

    $("#inputSearchQuery")
        .on('click', function () {
            isCitySidebarSearchClicked = true;
        })
        .on('focusout', function () {
            isCitySidebarSearchClicked = false;
        })

    $(document).on('click', '.badge_x', function () {
        $(this).parent().remove();
    })

});

function populateCitiesTextArea() {
    $("#cityTextArea").empty();
    citiesSet.forEach(city =>
        $("#cityTextArea").append(
            `
            <span class="badge badge-pill badge-secondary mx-2">${city} <span class="btn badge badge-light badge-pill badge_x">X</span></span>
            `))
}

/*
*********************
Event listeners setup
*********************
*/
