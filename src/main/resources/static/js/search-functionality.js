"use strict";

let city = "";
let baseUrl = window.location.pathname;
let isCitySidebarSearchClicked = false;
let startPrice = 0;
let endPrice = 0;
let startArea = 0;
let endArea = 0;

$(document).ready(function () {

    // Initialize bootstrap select plugin (needed for icon support in select option)
    $('.selectpicker').selectpicker({
        iconBase: 'fa',
        tickIcon: 'fa-check'
    });

    $(document).bind('keypress', function (e) {
        if (e.key === "Enter" && isCitySidebarSearchClicked) {
            e.preventDefault();
            city = $.trim($("#inputSearchQuery").val());
            populateCitiesTextArea();
            $("#inputSearchQuery").val('');
            $("#inputSearchQuery").autocomplete("close");
        }
    });

    // Initialize autocomplete Value source
    $("#inputSearchQuery")
        .autocomplete({
            source: function (request, response) {
                $.ajax({
                    url: "/GroupProject/autocomplete-utility/city/" + $.trim(request.term) + "/",
                    dataType: "json",
                    success: function (data) {
                        response(data);
                    }
                });
            },
            open: function () {
                $('.ui-autocomplete').css('z-index', 3000);
            },
            minLength: 3,
            select: function (event, ui) {
                event.preventDefault();
                city = $.trim(ui.item.value);
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
        })
        .on('click', function () {
            isCitySidebarSearchClicked = true;
        })
        .on('focusout', function () {
            isCitySidebarSearchClicked = false;
        })

    $("#inputStartingPrice").on("change", function () {
        let startingPriceInput = $.trim($("#inputStartingPrice").val());
        if ($.isNumeric(startingPriceInput)) startPrice = startingPriceInput;
    })

    $("#inputEndingPrice").on("change", function () {
        let endingPriceInput = $.trim($("#inputEndingPrice").val());
        if ($.isNumeric(endingPriceInput)) endPrice = endingPriceInput;
    })

    $("#inputStartingArea").on("change", function () {
        let startingAreaInput = $.trim($("#inputStartingArea").val());
        if ($.isNumeric(startingAreaInput)) startArea = startingAreaInput;
    })

    $("#inputEndingArea").on("change", function () {
        let endingAreaInput = $.trim($("#inputEndingArea").val());
        if ($.isNumeric(endingAreaInput)) endArea = endingAreaInput;
    })

    $("#searchButton").on("click", function (event) {
        event.preventDefault();
        window.location.href = baseUrl + `search?city=${city}&startPrice=${startPrice}&endPrice=${endPrice}&startArea=${startArea}&endArea=${endArea}`;
    })

    $(document).on('click', '.badge_x', function () {
        $(this).parent().remove();
    })

});

function populateCitiesTextArea() {
    $("#cityTextArea").empty();
    $("#cityTextArea").append(
        `
            <span class="badge badge-pill badge-secondary mx-2">${city} <span class="btn badge badge-light badge-pill badge_x">X</span></span>
            `)
}


