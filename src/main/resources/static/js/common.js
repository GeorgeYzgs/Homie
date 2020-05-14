$(document).ready(function () {
        updateLangSelectBtn();
    }
)

function updateLangSelectBtn() {
    let current_locale = $('meta[name=current_locale]').attr("content");
    $('#languageDropdown div a').each(function () {
        $(this).removeClass("active");
    })
    $(`#languageDropdown div [data-locale_code=${current_locale}]`).addClass("active");
    console.log($(`#languageDropdown div [data-locale_code=${current_locale}]`));
    $("#languageDropdown").find('button').empty();
    $("#languageDropdown").find('button').append($("#languageDropdown").find('.active').html());
}

