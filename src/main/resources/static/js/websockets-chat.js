"use strict";
let pathname = window.location.pathname.slice(0, -1);

let token = $("meta[name='_csrf']").attr("content");
let headerName = $("meta[name='_csrf_header']").attr("content");
let headers = {};
headers[headerName] = token;
let socket;
let stompClient;
let isConnected;

let selfUsername = "";
let selfSessionId = "";
let assignedModUsername = "";
let assignedModSessionId = "";
let currentRecipientSessionId = "";
let chatContainer = $("#chat-content");
let chatStatusMsg = $("#chatStatusMessage");
let chatInput = $("#chatInput");
let sendMsgBtn = $("#sendMsgBtn");
let hasChatMessages = false;

let chatName = $("meta[name='chat']").attr("content");
let chatPlaceholder = $("meta[name='chat_input_placeholder']").attr("content");

function connectChatWebSocket() {
    socket = new SockJS(pathname + '/chat')
    stompClient = Stomp.over(socket);
    stompClient.connect(headers, function (frame) {
        console.log("Frame:")
        console.log(frame)
        isConnected = true;
        // if (frame["headers"]["user-name"]) currentUsername = frame["headers"]["user-name"];
        chatContainer.empty();
        chatContainer.append(chatStatusMsg);
        stompClient.subscribe('/user/queue/online', function (message) {
            console.log(message)
            let payload = JSON.parse(message.body);
            let status = payload["status"];
            let metadata = payload["metadata"];
            switch (status) {
                case "USER_IDENTIFIER":

                    selfSessionId = metadata["userDetails"]["sessionId"]
                    selfUsername = metadata["userDetails"]["username"]
                    break;
                case "CHAT_CONNECTED":
                case "MOD_CONNECTED":
                    if (selfSessionId !== metadata["moderator"]["sessionId"]) {
                        assignedModUsername = metadata["moderator"]["username"];
                        assignedModSessionId = metadata["moderator"]["sessionId"];
                        chatStatusMsg.empty().append(`
                            <p class="font-weight-normal text-wrap text-secondary text-center">${payload["message"]}</p>
                            `);
                    } else {
                        chatStatusMsg.empty().append(`
                            <p class="font-weight-normal text-wrap text-secondary text-center">${payload["message"]}</p>
                            `);
                    }
                    chatInput.attr("disabled", false).attr("placeholder", chatPlaceholder);
                    break;
                case "MOD_DISCONNECTED":
                case "CHAT_DISCONNECTED":
                    if (hasChatMessages) {
                        chatContainer.append(`
                            <div class="row justify-content-center">
                                <p class="font-weight-normal text-wrap text-secondary text-center">${payload["message"]}</p>
                            </div>`);
                    } else {
                        chatStatusMsg.empty().append(`
                            <p class="font-weight-normal text-wrap text-secondary text-center">${payload["message"]}</p>
                        `);
                    }
                    chatInput.attr("placeholder", "").attr("disabled", true);
                    break;

            }
            }
        )
        stompClient.subscribe('/user/queue/specific-user', function (response) {
            console.log(response)
            let payload = JSON.parse(response.body);
            currentRecipientSessionId = payload["from"];
            chatContainer.append(`
            <div class="row justify-content-start">
                <p class="font-weight-normal text-wrap text-secondary msg-receive">${payload["message"]}</p>
            </div>`);
            hasChatMessages = true;
            scrollDownChat();
        })
    });
}


$(document).ready(function () {
    let isChatConnected = false;

    $("#chatBtn, #minimizeChat").on('click', function () {
        $("#chatContainer").toggleClass("fade-down");
        if (!isChatConnected) {
            isChatConnected = true;
            connectChatWebSocket();
        }
    })

    sendMsgBtn.on("click", function (event) {
        sendMessage($.trim(chatInput.val()));
        chatInput.val("");
    })


    $(document).bind('keypress', function (e) {
        if (e.key === "Enter") {
            if (!$("#chatContainer").hasClass("fade-down")) sendMsgBtn.trigger('click');
        }
    });
});


function sendMessage(message) {
    if (!isConnected) connectChatWebSocket();
    appendOutgoingMessage(message);
    let outgoingMsg = {
        'from': selfSessionId,
        'to': (assignedModSessionId) ? assignedModSessionId : currentRecipientSessionId,
        'message': message
    };
    stompClient.send("/app/chat", {}, JSON.stringify(outgoingMsg));
}


function appendOutgoingMessage(message) {
    let html =
        `
    <div class="row justify-content-end">
        <p class="msg-send badge badge-pill text-wrap text-right font-weight-normal ">${message}</p>
    </div>
    `;
    chatContainer.append(html);
    scrollDownChat()
}

function scrollDownChat() {
    chatContainer.animate({scrollTop: chatContainer[0].scrollHeight}, 500);
}

// let msg = {
//     // 'from': "Me",
//     'to': "pablo_esc",
//     'text': "Hi Pablo!"
// };



