"use strict"

$("#chatDialogueModal").draggable({
    handle: ".modal-header"
});

$("#chatBtn, #minimizeChat").on('click', function () {
    $("#chatContainer").toggleClass("fade-down");
})