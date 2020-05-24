"use strict"

let baseUrl = window.location.pathname;
let url = window.location.href;

let city = "";
let isCitySidebarSearchClicked = false;
let sqMeter = $('meta[name=squareMeter]').attr("content");

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
let currentPage = 1;
let totalPages = 0;

function getSearchResults() {
    $.ajax({
        type: "GET",
        url: baseUrl.replace("/search", "/async/search"),
        data: {
            city: city,
            category: category,
            heating: heating,
            sort: sortBy,
            startPrice: startPrice,
            endPrice: endPrice,
            startArea: startArea,
            endArea: endArea,
            startRooms: startRooms,
            endRooms: endRooms,
            startFloors: startFloors,
            endFloors: endFloors,
            page: currentPage
        },
        success: function (response) {
            $("#resultsContainer").empty();
            currentPage = (response.currentPage > 0) ? response.currentPage : 1;
            totalPages = response.totalPages;
            setPagination();
            response.properties.forEach(p => $("#resultsContainer").append(populateResults(p)));
            $("#priceApply, #areaApply, #roomsApply, #floorsApply").removeClass("show");
            history.pushState({asyncUrl: this.url}, "", this.url.replace('/async', ''));
        }
    });
}

function populateResults(property) {
    let html = `
 <a class="btn btn-light pt-4" href="${baseUrl.replace('/search', '/view/' + property.id)}">
                <div class="row border-top py-3">
                    <div class="col-sm-4">
                        <img class="rounded object-fit_cover img-responsive"
                             height="250" src="https://q-cf.bstatic.com/images/hotel/max1024x768/189/189426432.jpg">
                    </div>
                    <div class="col-sm-7 text-left">
                        <h5 class="font-weight-bold">
                            ${property.category + ' ' + property.area + sqMeter}
                        </h5>
                        <p class="mb-0" >
                            ${property.address.city}  ${property.address.state}
                        </p>
                        <p class="pt-0">
                            ${truncateString(property.description, 150)}
                        </p>
                        <p class="pt-1">
                        <span class="border rounded bg-green text-white p-1 font-weight-bold">
                        ${property.price}&euro;/μήνα
                        </span>
                            <span class="pl-1">${property.area} τμ</span>
                            <span class="pl-1">${(property.price / property.area).toFixed(2)}<span>&euro;</span>/τμ</span>
                            <span class="pl-1">${property.numberOfRooms} δωμάτια</span>
                        </p>
                    </div>
                </div>
            </div>
`
    return html;
}

function setPagination() {
    let html = `<ul class="pagination justify-content-end pagination-sm">`;
    let hasPrevious = function () {
        return (currentPage > 1 ? "" : ' style="visibility: hidden;"');
    }
    let hasNext = function () {
        return (currentPage < totalPages ? "" : ' style="visibility: hidden;"');
    }

    html += `
         <li class="page-item">
            <a class="page-link text-secondary bg-light border-0" ${hasPrevious()} data-page="${currentPage - 1}" aria-label="Previous">
                <span aria-hidden="true">&laquo;</span>
                <span class="sr-only">Previous</span>
            </a>
        </li>
        `

    for (let i = 1; i <= totalPages; i++) {
        let isDisabled = function () {
            return (i === currentPage ? "disabled" : "");
        }
        let isTextDark = function () {
            return (i === currentPage ? "text-dark" : "text-secondary");
        }
        html += `
        <li class="page-item ${isDisabled()}"><a class="page-link ${isTextDark()} bg-light border-0" data-page="${i}">${i}</a></li>
        `
    }

    html += `
        <li class="page-item">
            <a class="page-link text-secondary bg-light border-0" ${hasNext()} data-page="${currentPage + 1}" aria-label="Next">
                <span aria-hidden="true">&raquo;</span>
                <span class="sr-only">Next</span>
            </a>
        </li>
        `;
    html += `</ul>`
    $("#pageNavContainer").empty().append(html);
}

function truncateString(str, num) {
    if (str.length <= num) {
        return str
    }
    return str.slice(0, num) + '...'
}

function populateCitiesTextArea() {
    getSearchResults();
    $("#cityTextArea").empty();
    $("#cityTextArea").append(
        `
            <span class="badge badge-pill badge-secondary mx-2">${city} <span class="btn badge badge-light badge-pill badge_x">X</span></span>
            `)
}

function updateGlobalVariables() {
    let inputStartPrice = $.trim($("#inputStartingPrice").val());
    let inputEndPrice = $.trim($("#inputEndingPrice").val());
    let inputStartArea = $.trim($("#inputStartingArea").val());
    let inputEndArea = $.trim($("#inputEndingArea").val());
    let inputStartRooms = $.trim($("#inputStartingRooms").val());
    let inputEndRooms = $.trim($("#inputEndingRooms").val());
    let inputStartFloors = $.trim($("#inputStartingFloors").val());
    let inputEndFloors = $.trim($("#inputEndingFloors").val());

    category = $("#categorySelect").find(":selected").val();
    heating = $("#heatingSelect").find(":selected").val();
    sortBy = $("#sortBySelect").find(":selected").val();
    startPrice = ($.isNumeric(inputStartPrice)) ? inputStartPrice : 0;
    endPrice = ($.isNumeric(inputEndPrice)) ? inputEndPrice : 0;
    startArea = ($.isNumeric(inputStartArea)) ? inputStartArea : 0;
    endArea = ($.isNumeric(inputEndArea)) ? inputEndArea : 0;
    startRooms = ($.isNumeric(inputStartRooms)) ? inputStartRooms : 0;
    endRooms = ($.isNumeric(inputEndRooms)) ? inputEndRooms : 0;
    startFloors = ($.isNumeric(inputStartFloors)) ? inputStartFloors : 0;
    endFloors = ($.isNumeric(inputEndFloors)) ? inputEndFloors : 0;
    if ($('meta[name=currentPage]').attr("content")) currentPage = $('meta[name=currentPage]').attr("content");
    if ($('meta[name=totalPages]').attr("content")) totalPages = $('meta[name=totalPages]').attr("content");
    if ($('meta[name=searchCity]').attr("content")) {
        city = $('meta[name=searchCity]').attr("content");
        populateCitiesTextArea();
    }
    setPagination();
}

$(document).ready(function () {
    let currentState = history.state;
    if (currentState == null && !url.includes("/custom-search")) {
        history.replaceState({asyncUrl: url.replace("/search", "/async/search")}, "", url);
    }
    updateGlobalVariables();

    $(document).bind('keypress', function (e) {
        if (e.key === "Enter" && isCitySidebarSearchClicked) {
            city = $.trim($("#inputSearchQuery").val());
            populateCitiesTextArea();
            $('#inputSearchQuery').autocomplete('close');
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

    $(document).on('click', '.badge_x', function () {
        city = ""
        $(this).parent().remove();
        getSearchResults()
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
        sortBy = $(this).find(":selected").val();
        getSearchResults()
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

    $("#pageNavContainer").on('click', ".page-link", function (event) {
        event.preventDefault();
        currentPage = $(event.currentTarget).data("page");
        getSearchResults();
    })
})

$(window).bind('popstate', function (event) {
    let state = event.originalEvent.state;
    console.log(state)
    if (state !== null) {
        let asyncUrl = state.asyncUrl;
        $.ajax({
            type: "GET",
            url: asyncUrl,
            timeout: 3000,
            success: function (response) {
                $("#resultsContainer").empty();
                currentPage = (response.currentPage > 0) ? response.currentPage : 1;
                totalPages = response.totalPages;
                setPagination();
                response.properties.forEach(p => $("#resultsContainer").append(populateResults(p)));
                $("#priceApply, #areaApply, #roomsApply, #floorsApply").removeClass("show");
            }
        });
    }
});