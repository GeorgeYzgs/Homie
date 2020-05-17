"use strict"
// TODO Find better way to get base url
let baseUrl = "/GroupProject"

// let baseUrl = window.location.hostname;

function clearNavigationActiveClass() {
    $("#controlPanelToolbar").find("li").removeClass("active");
    console.log($("#controlPanelToolbar").find("a"))
}


function showPersonalDetails(userDetails, href) {
    $("#controlPanelToolbar").find("[href='" + href + "']").parent().addClass("active");
    $("#mainParent").empty();
    $("#mainParent").append(userDetails)
}

$(document).ready(function () {

    $('.async-nav-link').on("click", function (event) {
        //This stops the browser from actually following the link
        event.preventDefault();

        let url = $(this).attr("href");
        //This function would get content from the server and insert it into the id="content" element
        $.ajax({
            type: "GET",
            url: baseUrl + '/async/' + url.split("/")[3],
            timeout: 3000,
            success: function (response) {
                // console.log(response);
                clearNavigationActiveClass();
                showPersonalDetails(response, url);
            }
        });
        //This is where we update the address bar with the 'url' parameter
        history.pushState({}, "", url);
    })
});
