"use strict";

let url = "/GroupProject"
let passMustContain = $('meta[name=password_must_contain]').attr("content");
let passLength = $('meta[name=password_length]').attr("content");
let passDigits = $('meta[name=password_digits]').attr("content");
let passLowercase = $('meta[name=password_lowercase]').attr("content");
let passUppercase = $('meta[name=password_uppercase]').attr("content");
let passSymbols = $('meta[name=password_symbols]').attr("content");
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
    let errorList = $(event.currentTarget).siblings("[data-target=error-list]");
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
    } else if (score > 55) {
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
    if (!variations.length) messages.push(passLength);
    if (!variations.digits) messages.push(passDigits);
    if (!variations.lower) messages.push(passLowercase);
    if (!variations.upper) messages.push(passUppercase);
    if (!variations.nonWords) messages.push(passSymbols);
    return messages;
}


function scorePassword(pass) {
    let score = 0;
    if (!pass)
        return score;

    // bonus points for mixing it up
    let variations = {
        digits: /\d/.test(pass),
        lower: /[a-z]/.test(pass),
        upper: /[A-Z]/.test(pass),
        nonWords: /\W/.test(pass),
        length: (pass.length >= 8)
    }

    let variationCount = 0;
    for (let check in variations) {
        variationCount += (variations[check] === true) ? 1 : 0;
    }
    if (variationCount < Object.keys(variations).length) {
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

function checkPassStrength(pass) {
    let score = scorePassword(pass);
    if (score > 80)
        return "strong";
    if (score > 60)
        return "good";
    if (score >= 30)
        return "weak";

    return "";
}


function showValidationMessages(event, response) {
    let inputField = $(event.currentTarget);
    let errorList = $(event.currentTarget).siblings("[data-target=error-list]");
    let inputName = inputField.attr('name')
    if (response.status === "error") {
        let errors = response["errors"][inputName];
        if (errors !== undefined) {
            if (!inputField.hasClass("border-danger")) {
                inputField.addClass("border-danger");
            }
            errorList.empty()
            for (let i = 0; i < errors.length; i++) {
                if (errors[i] != null) {
                    errorList.append("<li>" + errors[i] + '</li>');
                }
            }
        }
        return true;
    } else {
        inputField.removeClass("border-danger").addClass("border-success");
        errorList.empty();
        return false;
    }
}


$(document).ready(function () {

    let form = $("#registerForm");
    let emailInput = $("#email");
    let usernameInput = $("#username");
    let passwordInput = $("#password");
    let password2Input = $("#password2");
    let isPassword2Enabled = false;

    let isUsernameValid = false
    let isEmailValid = false;
    let isPasswordValid = false;
    let arePasswordMatched = false;

    form.submit(function (event) {
        // event.preventDefault();

        let isFormValid = isUsernameValid && isEmailValid && isPasswordValid && arePasswordMatched;
        if (!isFormValid) {
            $("#registerButton").effect("shake", {distance: 10, times: 2}, 400);
        } else {
            $.ajax({
                type: "POST",
                url: url + "/register",
                timeout: 3000,
                data: form.serialize(), // serializes the form's elements.
                success: function (response) {
                    // if (response.status === "error") {
                    //     let emailErrors = response.errors.email;
                    //     if (emailErrors !== undefined) {
                    //
                    //         if (!emailInput.hasClass("border-danger")) {
                    //             emailInput.addClass("border-danger");
                    //         }
                    //         emailErrorList.empty()
                    //         for (let i = 0; i < emailErrors.length; i++) {
                    //             if (emailErrors[i] != null) {
                    //                 emailErrorList.append("<li>" + emailErrors[i] + '</li>');
                    //             }
                    //         }
                    //     } else {
                    //         emailInput.removeClass("border-danger").addClass("border-success");
                    //         emailErrorList.empty();
                    //     }
                    // }
                }
            })
        }
    })

    usernameInput.on("focusout", function (event) {
        $.ajax({
            type: "POST",
            url: url + "/validate/check-username",
            timeout: 3000,
            data: {
                "_csrf": $("input[name*='_csrf']").val(),
                "username": $.trim(usernameInput.val())
            },
            success: function (response) {
                showValidationMessages(event, response) ? isUsernameValid = false : isUsernameValid = true;
            }
        })
    });

    emailInput.on("focusout", function (event) {
        $.ajax({
            type: "POST",
            url: url + "/validate/check-email",
            timeout: 3000,
            data: {
                "_csrf": $("input[name*='_csrf']").val(),
                "email": $.trim(emailInput.val())
            },
            success: function (response) {
                showValidationMessages(event, response) ? isEmailValid = false : isEmailValid = true;
            }
        })
    })

    passwordInput.on("keyup focusout", function (event) {
        if (passwordInput.val() !== "") {
            password2Input.trigger("keyup");
            isPasswordValid = updatePassStrengthBar(event);
        } else {
            resetInputField($(event.currentTarget));
            resetPassStrengthBar($("#passwordStrength"));
        }
    })

    password2Input.on("focusin", function () {
        isPassword2Enabled = true
    })
        .on("keyup", function (event) {
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

});