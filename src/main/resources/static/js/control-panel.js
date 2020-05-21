"use strict"
// TODO Find better way to get base url
// let baseUrl = "/GroupProject"

// let baseUrl = window.location.hostname;

function getCurrentHref() {
    let currentHref = $("#controlPanelToolbar").find("li.active a").attr("href");
    let currentAsyncHref = $("#controlPanelToolbar").find("li.active a").data("async-href");
    return [{asyncUrl: currentAsyncHref}, {currentHref: currentHref},];
}

function clearNavigationActiveClass() {
    $("#controlPanelToolbar").find("li").removeClass("active");
}

function showPersonalDetails(userDetails, href) {
    $("#controlPanelToolbar").find("[href='" + href + "']").parent().addClass("active");
    $("#mainParent").empty();
    $("#mainParent").append(userDetails)
}

$(document).ready(function () {
    let currentState = history.state;
    if (currentState == null) {
        let currentHrefList = getCurrentHref();
        console.log(currentHrefList)
        history.replaceState(currentHrefList[0], "", currentHrefList[1].currentHref)
    }

    $('.async-nav-link').on("click", function (event) {
        //This stops the browser from actually following the link
        event.preventDefault();

        let asyncUrl = $(this).data("async-href");

        let regularUrl = $(this).attr("href");
        //This function would get content from the server and insert it into the id="content" element
        $.ajax({
            type: "GET",
            url: asyncUrl,
            timeout: 3000,
            success: function (response) {
                clearNavigationActiveClass();
                showPersonalDetails(response, regularUrl);
            }
        });
        //This is where we update the address bar with the 'url' parameter
        history.pushState({asyncUrl: asyncUrl}, "", regularUrl);

        // $(window).bind('popstate', function(event){
        //     window.history.back()
        // });
    })
    $(window).bind('popstate', function (event) {
        let state = event.originalEvent.state;
        if (state !== null) {
            let regularUrl = document.location.pathname;
            let asyncUrl = state.asyncUrl;
            $.ajax({
                type: "GET",
                url: asyncUrl,
                timeout: 3000,
                success: function (response) {
                    clearNavigationActiveClass();
                    showPersonalDetails(response, regularUrl);
                }
            });
        }
    });
});
