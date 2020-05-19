"use strict"

let baseUrl = "/GroupProject"

let city = "";
let isCitySidebarSearchClicked = false;

let category = "ALL";
let heating = "ALL";
let sortBy = "ALL";
let startPrice = 0;
let endPrice = 0;
let startArea = 0;
let endArea = 0;
let startRooms = 0;
let endRooms = 0;
let startFloors = 0;
let endFloors = 0;

function getSearchResults() {
    $.ajax({
        type: "GET",
        url: baseUrl + "/async/search",
        data: {
            city: city,
            category: category,
            heating: heating,
            sortBy: sortBy,
            startPrice: startPrice,
            endPrice: endPrice,
            startArea: startArea,
            endArea: endArea,
            startRooms: startRooms,
            endRooms: endRooms,
            startFloors: startFloors,
            endFloors: endFloors
        },
        success: function (response) {
            console.log(response);
            history.pushState({asyncUrl: this.url}, "", this.url.replace('/async', ''));
            $("#priceApply, #areaApply, #roomsApply, #floorsApply").removeClass("show");
        }
    });
}

function populateCitiesTextArea() {
    getSearchResults();
    $("#cityTextArea").empty();
    $("#cityTextArea").append(
        `
            <span class="badge badge-pill badge-secondary mx-2">${city} <span class="btn badge badge-light badge-pill badge_x">X</span></span>
            `)
}

$(document).ready(function () {

    $(document).bind('keypress', function (e) {
        if (e.key === "Enter" && isCitySidebarSearchClicked) {
            city = $.trim($("#inputSearchQuery").val());
            populateCitiesTextArea();
            $("#inputSearchQuery").val('');
        }
    });

    // Initialize autocomplete Value source
    $("#inputSearchQuery")
        .autocomplete({
            source: function (request, response) {
                $.ajax({
                    url: "/GroupProject/autocomplete-utility/city/" + $.trim(request.term) + "/",
                    dataType: "json",
                    // data: {
                    //     q: request.term
                    // },
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
                city = ui.item.value;
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

    $(document).on('click', '.badge_x', function () {
        $(this).parent().remove();
    })


    $("#categorySelect").on("change", function (event) {
        category = $(this).find(":selected").val();
        getSearchResults();
    })

    $("#heatingSelect").on("change", function (event) {
        heating = $(this).find(":selected").val();
        getSearchResults();
    })

    $("#sortBySelect").on("change", function (event) {
        console.log($(this).find(":selected").val());
    })


    $("#inputStartingPrice, #inputEndingPrice").on("change keyup", function (event) {
        $("#priceApply").addClass("show");
    });

    $("#priceApplyBtn").on("click", function (event) {
        event.preventDefault();
        let startingPriceInput = $.trim($("#inputStartingPrice").val());
        let endingPriceInput = $.trim($("#inputEndingPrice").val());
        if ($.isNumeric(startingPriceInput)) {
            startPrice = startingPriceInput;
        }
        if ($.isNumeric(endingPriceInput)) {
            endPrice = endingPriceInput;
        }
        getSearchResults();
    })

    $("#inputStartingArea, #inputEndingArea").on("change keyup", function (event) {
        $("#areaApply").addClass("show");
    });

    $("#areaApplyBtn").on("click", function (event) {
        event.preventDefault();
        let startingAreaInput = $.trim($("#inputStartingArea").val());
        let endingAreaInput = $.trim($("#inputEndingArea").val());
        if ($.isNumeric(startingAreaInput)) {
            startArea = startingAreaInput;
        }
        if ($.isNumeric(endingAreaInput)) {
            endArea = endingAreaInput;
        }
        getSearchResults();
    })

    $("#inputStartingRooms, #inputEndingRooms").on("change keyup", function (event) {
        $("#roomsApply").addClass("show");
    });

    $("#roomsApplyBtn").on("click", function (event) {
        event.preventDefault();
        let startingRoomsInput = $.trim($("#inputStartingRooms").val());
        let endingRoomsInput = $.trim($("#inputEndingRooms").val());
        if ($.isNumeric(startingRoomsInput)) {
            startRooms = startingRoomsInput;
        }
        if ($.isNumeric(endingRoomsInput)) {
            endRooms = endingRoomsInput;
        }
        getSearchResults();
    })

    $("#inputStartingFloors, #inputEndingFloors").on("change keyup", function (event) {
        $("#floorsApply").addClass("show");
    });

    $("#floorsApplyBtn").on("click", function (event) {
        event.preventDefault();
        let startingFloorsInput = $.trim($("#inputStartingFloors").val());
        let endingFloorsInput = $.trim($("#inputEndingFloors").val());
        if ($.isNumeric(startingFloorsInput)) {
            startFloors = startingFloorsInput;
        }
        if ($.isNumeric(endingFloorsInput)) {
            endFloors = endingFloorsInput;
        }
        getSearchResults();
    })

})