"use strict";
let pathname = window.location.pathname.slice(0, -1);

let token = $("meta[name='_csrf']").attr("content");
let headerName = $("meta[name='_csrf_header']").attr("content");
let headers = {};
headers[headerName] = token;
let socket;
let stompClient;
let isConnected;

let currentUsername = "";
let currentSessionId = "";
let assignedModUsername = "";
let assignedModSessionId = "";
let currentRecipient = "";
let chatContainer = $("#chat-content");
let chatStatusMsg = $("#chatStatusMessage");
let chatInput = $("#chatInput");
let sendMsgBtn = $("#sendMsgBtn");

function connectChatWebSocket() {
    socket = new SockJS(pathname + '/chat')
    stompClient = Stomp.over(socket);
    stompClient.connect(headers, function (frame) {
        console.log("Frame:")
        console.log(frame)
        isConnected = true;
        if (frame["headers"]["user-name"]) currentUsername = frame["headers"]["user-name"];
        chatContainer.empty();
        chatContainer.append(chatStatusMsg);
        stompClient.subscribe('/user/queue/online', function (message) {
                console.log(message)
                let payload = JSON.parse(message.body);
                let status = payload["status"];
                let metadata = payload["metadata"];
                // currentSessionId = payload["to"]
                if (status === "CONNECTED") {
                    if (metadata) {
                        if (currentUsername !== metadata["moderator"]["username"]) {
                            assignedModUsername = metadata["moderator"]["username"];
                            assignedModSessionId = metadata["moderator"]["sessionId"];
                        }
                    }
                    chatStatusMsg.empty().append(`
                    <p class="font-weight-normal text-wrap text-secondary">${payload["message"]}</p>
                `);
                    chatInput.attr("disabled", false);
                } else if (status === "DISCONNECTED") {
                    chatStatusMsg.empty().append(`
                    <p class="font-weight-normal text-wrap text-secondary">${payload["message"]}</p>
                `)
                    chatInput.attr("disabled", true);
                } else if (status === "UserIdentifier") {
                    let metadata = payload["metadata"]["userdetails"];
                    currentSessionId = metadata["sessionId"]
                    if (metadata["username"]) currentUsername = metadata["username"];
                }
            }
        )
        stompClient.subscribe('/user/queue/specific-user', function (response) {
            console.log(response)
            let payload = JSON.parse(response.body);
            currentRecipient = payload["from"];
            chatContainer.append(`
            <div class="row justify-content-start">
                <p class="font-weight-normal text-wrap text-secondary">${payload["message"]}</p>
            </div>`);
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
        'from': (currentSessionId) ? currentSessionId : currentUsername,
        'to': (assignedModSessionId) ? assignedModSessionId : currentRecipient,
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



