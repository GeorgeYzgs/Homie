"use strict";

$(document).ready(function () {
// $('#recipeCarousel').carousel({
//     interval: 200
// })

    $('.carousel .carousel-item').each(function () {
        let minPerSlide = 7;
        let next = $(this).next();
        if (!next.length) {
            next = $(this).siblings(':first');
        }
        next.children(':first-child').clone().appendTo($(this));

        for (let i = 0; i < minPerSlide; i++) {
            next = next.next();
            if (!next.length) {
                next = $(this).siblings(':first');
            }

            next.children(':first-child').clone().appendTo($(this));
        }
    });

    $(".card").hover(
        function () {
            $(this).addClass('shadow').css('cursor', 'pointer');
        }, function () {
            $(this).removeClass('shadow');
        }
    );

});
