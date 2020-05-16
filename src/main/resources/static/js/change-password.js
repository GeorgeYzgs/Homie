"use strict";

let url = "/GroupProject"
let passMustContain = $('meta[name=password_must_contain]').attr("content");
let passLength = $('meta[name=password_length]').attr("content");
let passDigitsSymbols = $('meta[name=password_digits_symbols]').attr("content");
let passLowercase = $('meta[name=password_lowercase]').attr("content");
let passNotMatch = $('meta[name=passwords_not_match]').attr("content");

function resetInputField(inputField) {
    inputField.removeClass("border-success").removeClass("border-danger");
    inputField.siblings("[data-target=error-list]").empty();
}

function resetPassStrengthBar(passwordStrengthBar) {
    passwordStrengthBar.css("width", 0 + "%").attr("aria-valuenow", 0);
}

function updatePassStrengthBar(event) {
    let inputField = $(event.currentTarget);
    let scoreList = scorePassword(inputField.val());
    let score = scoreList[0];
    let scoreMessages = getStrengthenPasswordMessages(scoreList[1]);
    let passwordStrengthBar = $("#passwordStrength");
    let errorList = $(event.currentTarget).parent().siblings("[data-target=error-list]");
    console.log(errorList);
    let messageHeader = passMustContain;

    passwordStrengthBar.css("width", ((score > 100) ? 100 : score) + "%").attr("aria-valuenow", ((score > 100) ? 100 : score));
    errorList.empty()
    if (score > 80) {
        inputField.removeClass("border-danger").addClass("border-success");
        passwordStrengthBar.removeClass("bg-danger").removeClass("bg-warning").addClass("bg-success");
        if (Array.isArray(scoreMessages) && scoreMessages.length) {
            errorList.append("<li>" + messageHeader + '</li>');
            for (let i = 0; i < scoreMessages.length; i++) {
                errorList.append("<li>" + scoreMessages[i] + '</li>');
            }
        }
        return true;
    } else if (score > 35) {
        inputField.removeClass("border-danger").addClass("border-success");
        passwordStrengthBar.removeClass("bg-danger").removeClass("bg-success").addClass("bg-warning");
        if (Array.isArray(scoreMessages) && scoreMessages.length) {
            errorList.append("<li>" + messageHeader + '</li>');
            for (let i = 0; i < scoreMessages.length; i++) {
                errorList.append("<li>" + scoreMessages[i] + '</li>');
            }
        }
        return true;
    } else if (score >= 0) {
        if (!inputField.hasClass("border-danger")) inputField.addClass("border-danger");
        passwordStrengthBar.removeClass("bg-warning").removeClass("bg-success").addClass("bg-danger");
        if (Array.isArray(scoreMessages) && scoreMessages.length) {
            errorList.append("<li>" + messageHeader + '</li>');
            for (let i = 0; i < scoreMessages.length; i++) {
                errorList.append("<li>" + scoreMessages[i] + '</li>');
            }
        }
        return false;
    }
}

function getStrengthenPasswordMessages(variations) {
    let messages = [];
    if (variations === undefined) {
        messages.push(passLength)
    } else {
        if (!variations.length) messages.push(passLength);
        if (!variations.lower) messages.push(passLowercase);
        if (!variations.digits && !variations.nonWords) messages.push(passDigitsSymbols);
    }
    return messages;
}


function scorePassword(pass) {
    let score = 0;
    let groupings = 1;

    // bonus points for mixing it up
    let variations = {
        lower: /[a-z]/.test(pass),
        digits: /\d/.test(pass),
        nonWords: /\W/.test(pass),
        length: (pass.length >= 8)
    }
    if (!pass)
        return [score, variations];

    let variationCount = 0;
    for (let check in variations) {
        variationCount += (variations[check] === true) ? 1 : 0;
    }
    if (variationCount < Object.keys(variations).length - groupings) {
        score += (variationCount + 1) * 10;
        return [score, variations];
    } else {
        score += (variationCount - 1) * 10;
        // award every unique letter until 5 repetitions
        let letters = {};
        for (let i = 0; i < pass.length; i++) {
            letters[pass[i]] = (letters[pass[i]] || 0) + 1;
            score += 5.0 / letters[pass[i]];
        }
        return [score, variations];
    }
}


$(document).ready(function () {
    let form = $("#updateForm");
    let passwordInput = $("#password");
    let password2Input = $("#password2");
    let showPassBtn = $("#showPassword");
    let isPassword2Enabled = false;
    let submitButton = $("#registerButton");

    let isPasswordValid = false;
    let arePasswordMatched = false;

    showPassBtn.on("click", function (event) {
        let inputField = $(event.currentTarget).siblings("input");

        if (inputField.hasClass('eye-closed')) {
            inputField.attr('type', 'text');
        } else if (inputField.hasClass('eye-open')) {
            inputField.attr('type', 'password');
        }
        inputField.toggleClass("eye-open eye-closed");
    })


    passwordInput.on("keyup", function (event) {
        if (passwordInput.val() !== "") {
            password2Input.trigger("keyup");
            isPasswordValid = updatePassStrengthBar(event);
        } else {
            resetInputField($(event.currentTarget));
            resetPassStrengthBar($("#passwordStrength"));
        }
    })
        .on("focusout", function (event) {
            isPasswordValid = updatePassStrengthBar(event);
        })

    password2Input.on("focusin", function () {
        isPassword2Enabled = true
    })
        .on("keyup focusout", function (event) {
            if (isPassword2Enabled) {
                let errorList = password2Input.siblings("[data-target=error-list]")
                errorList.empty()
                if (passwordInput.val() === password2Input.val()) {
                    if (!password2Input.hasClass("border-success")) {
                        password2Input.removeClass("border-danger").addClass("border-success");
                    }
                    arePasswordMatched = true;
                } else {
                    if (!password2Input.hasClass("border-danger")) {
                        password2Input.removeClass("border-success").addClass("border-danger");
                    }
                    errorList.append("<li>" + passNotMatch + "</li>");
                    arePasswordMatched = false;
                }
            }
        })


    submitButton.on('click', function (event) {
        event.preventDefault();
        let isFormValid = isPasswordValid && arePasswordMatched;
        if (!isFormValid) {
            $("#registerButton").effect("shake", {distance: 10, times: 2}, 400);
            usernameInput.focusout();
            emailInput.focusout();
            passwordInput.focusout();
            password2Input.focusout();
        } else {
            form.submit();
        }
    })

});