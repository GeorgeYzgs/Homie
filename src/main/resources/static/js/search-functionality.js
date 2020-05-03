"use strict";

let responseJson;
let roomtypesArray;
let hotelsArray;
let priceValues = [];
let filterSet = new Set();
let locationSet = new Set();

$(document).ready(function () {

    // Initialize bootstrap select plugin (needed for icon support in select option)
    $('.selectpicker').selectpicker({
        iconBase: 'fa',
        tickIcon: 'fa-check'
    });

    // Ajax call to get the json file and populate the html
    $.ajax(
        {
            url: "../data.json",
            type: 'GET',
            dataType: 'json',
            success: function (response) {
                responseJson = response;
                populateHtml();

            }
        }
    )
});

// Function that populates the search options and add event listeners
function populateHtml() {
    // responseJson = response;
    roomtypesArray = responseJson[0].roomtypes;
    hotelsArray = responseJson[1].entries;

    // Flatten filters Array of Key/Value pairs to list of Values
    hotelsArray.forEach(
        hotel => hotel.filters = hotel.filters
            .reduce((filters, item) => {
                filters.push(item.name);
                return filters;
            }, []));

    // Populate Type Select
    $("#roomTypeSelect").empty();
    for (let i = 0; i < roomtypesArray.length; i++) {
        $("#roomTypeSelect").append(`<option>${roomtypesArray[i].name}</option>`);
    }

    // Populate Price Range
    for (let i = 0; i < hotelsArray.length; i++) {
        priceValues.push(hotelsArray[i].price);
    }
    $('#priceRange').attr('min', Math.min(...priceValues));
    $('#priceRange').attr('max', Math.max(...priceValues));
    $('#priceRangeValue').text("Max: " + Math.max(...priceValues) + "€");
    $('#priceRange').attr("value", Math.max(...priceValues));
    $("#priceRange").on('input change', function () {
        $('#priceRangeValue').text("Max: " + $(this).val() + "€");
    });

    // Populate Hotel Location Select 
    for (let i = 0; i < hotelsArray.length; i++) {
        locationSet.add(hotelsArray[i].city);
    }
    $('#optionsHotelLocation').empty();
    $("#optionsHotelLocation").append(`<option>All</option>`)
    locationSet.forEach((city) => $("#optionsHotelLocation").append(`<option>${city}</option>`));

    // Initialize autocomplete Value source
    $("#inputDestination").autocomplete({
        source: Array.from(locationSet)
    });

    // Populate Sort By filters
    for (let i = 0; i < hotelsArray.length; i++) {
        for (let j = 0; j < hotelsArray[i].filters.length; j++) {
            filterSet.add(hotelsArray[i].filters[j]);
        }
    }
    $("#optionsSortBy").empty().append(`<option>All</option>`);
    filterSet.forEach((filter) => $("#optionsSortBy").append(`<option>${filter}</option>`));

    // Populate Hotel results with all the hotels from the json file 
    addHotels(hotelsArray);

    /*
    *********************
    Event listeners setup
    *********************
    */
    // On change listener for Property Type Select
    $("#optionsPropertyType").on('change', function () {
        let currentPropertyType = $("#optionsPropertyType").find(":selected").val();
        if (currentPropertyType == 0) {
            addHotels(hotelsArray);
        } else {
            const hotels = hotelsArray.filter(hotel => hotel.rating == currentPropertyType);
            addHotels(hotels);
        }
    })

    // On change listener for Price Range
    $("#priceRange").on('change', function () {
        let maxPrice = $(this).val();
        addHotels(hotelsArray.filter(hotel => hotel.price <= maxPrice));
    })

    // On change listener for Guest Rating Select
    $("#optionsGuestRating").on('change', function () {
        let guestRatingRange = $(this).val();
        switch (guestRatingRange) {
            case "0":
                addHotels(hotelsArray);
                break;
            case "1":
                addHotels(hotelsArray.filter(hotel => hotel.ratings.no >= 8.5));
                break;
            case "2":
                addHotels(hotelsArray.filter(hotel => parseFloat(hotel.ratings.no) >= 7.0 && parseFloat(hotel.ratings.no) < 8.5));
                break;
            case "3":
                addHotels(hotelsArray.filter(hotel => parseFloat(hotel.ratings.no) >= 6.0 && parseFloat(hotel.ratings.no < 7.0)));
                break;
            case "4":
                addHotels(hotelsArray.filter(hotel => parseFloat(hotel.ratings.no) >= 2.0 && parseFloat(hotel.ratings.no) < 6.0));
                break;
            case "5":
                addHotels(hotelsArray.filter(hotel => parseFloat(hotel.ratings.no) >= 0.0 && parseFloat(hotel.ratings.no) < 2.0));
                break;
        }
    })

    // On change listener for Hotel Location Select
    $("#optionsHotelLocation").on('change', function () {
        let cityLocation = $("#optionsHotelLocation option:selected").text();
        if (cityLocation === "All") {
            addHotels(hotelsArray);
        } else {
            addHotels(hotelsArray.filter(hotel => hotel.city === cityLocation));
        }

    })

    // On click listener for Search Button
    $("#searchButton").on('click', function (event) {
        event.preventDefault();
        let searchValue = $("#inputDestination").val();
        let searchValueTrimmed = $.trim(searchValue);
        addHotels(hotelsArray.filter(hotel => hotel.city.toLowerCase().search(searchValueTrimmed.toLowerCase()) > -1));
    })

    $("#optionsSortBy").on('change', function () {
        let sortByOption = $("#optionsSortBy option:selected").text();
        if (sortByOption === "All") {
            addHotels(hotelsArray);
        } else {
            addHotels(hotelsArray.filter(hotel => hotel.filters.includes(sortByOption)));
        }
    })
}

// Function to populate the list of hotels in the results part of html
function addHotels(hotels) {
    $("#hotelsParentMain").empty();
    if (Array.isArray(hotels) && hotels.length == 0) {
        $("#hotelsParentMain").append(
            `<p class="text-center">
            Nothing Found
            </p>`);
    } else {
        for (let i = 0; i < hotels.length; i++) {
            const currentHotel = hotels[i];
            $("#hotelsParentMain").append(
                `
        <div class="row mt-3 py-3 border">
        <div class="col-sm-3 ">
            <img src="${currentHotel.thumbnail}"
                class="rounded object-fit_cover" width="250" height="250"
                >
        </div>
        <div class="col-sm-6 border-right">
            <h3 class="font-weight-bold">
                ${currentHotel.hotelName}
            </h3>
            <p>
                ${addStars(currentHotel.rating, 5)}</span> Hotel</p>
            <p class="pt-1">
                ${currentHotel.city}
            </p>
            <p class="pt-1">
                <span class="border rounded bg-success text-white p-1 font-weight-bold">
                    ${parseFloat(currentHotel.ratings.no).toFixed(1)}
                </span>
                <span class="pl-1">
                    ${currentHotel.ratings.text}
                </span>
            </p>
        </div>
        <div class="col-sm-3 align-self-end">
            <h3 class="row justify-content-center pb-5">
                <span class="text-success font-weight-bold">
                    ${currentHotel.price}
                    <span>€</span>
                </span>
            </h3>
            <div class="row justify-content-center">
                <button type="button" class="btn btn-success align-bottom font-weight-bold">View Deal</button>
            </div>
        </div>
    </div>
        `
            );
        }
    }
}

// Function to add custom number of star icons. Returns the html in string format. 
function addStars(currentRating, bestRating) {

    let html = '';
    for (let i = 0; i < currentRating; i++) {
        html += `<span class="fa fa-star checked"></span>`
    }

    let emptyStars = bestRating - currentRating;
    for (let i = 0; i < emptyStars; i++) {
        html += `<span class="fa fa-star unchecked"></span>`
    }
    return html;
}