var webSocket = null;
var tryTime = 0;
var token = GetQueryString('token');
var id = token.split("_")[0];
var img = null;
var serverURL = "http://localhost:8080/FuckChat/";

function sendtext() {
    if (webSocket != null && webSocket.readyState == 1) {
        $('#chat').append('<li class="me">' + msg.value + '</li>');
        webSocket.send(msg.value);
        msg.value = "";
        scrollToLocation();
    } else {
        $('#chat').append('<li class="sys">You are offline.</li>');
    }
}

function GetQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]);
    return null;
}

function scrollToLocation() {
    var mainContainer = $('#chat'),
        scrollToContainer = mainContainer.find('li:last');
    mainContainer.scrollTop(
        scrollToContainer.offset().top - mainContainer.offset().top + mainContainer.scrollTop()
    );
}

function initSocket() {
    if (!window.WebSocket) {
        alert("您的浏览器不支持websocket！");
        return false;
    }
    webSocket = new WebSocket('ws://' + serverURL.split('//')[1] + "chat/" + token);

    webSocket.onmessage = function(res) {
        var data = JSON.parse(res.data);
        var msg = data.data;
        if (data.code == 1) {
            if (msg.type == "text")
                if (msg.from == id)
                    $('#chat').append('<li class="me"><span class="head"></span>' + msg.context + '</li>');
                else
                    $('#chat').append('<li class="to"><span class="head"></span>' + msg.from + ' : ' + msg.context + '</li>');
            else if (msg.type == "img")
                if (msg.from == id)
                    $('#chat').append('<li class="me"><img class="img" src=' + msg.context + '></li>');
                else
                    $('#chat').append('<li class="to">' + msg.from + ':<br><img class="img" src=' + msg.context + '></li>');
            else if (msg.type == "vid")
                if (msg.from == id) {
                    var target = $('#chat').find('#uploading:first');
                    if (!target[0])
                        $('#chat').append('<li class="me"><video height="100%" width="100%" onclick="this.play()"><source src=' + msg.context + '></video></li>');
                    else {
                        target.empty();
                        target.append('<video height="100%" width="100%" onclick="this.play()"><source src=' + msg.context + '></video>');
                        target.attr('id', '');
                    }
                }
                else
                    $('#chat').append('<li class="to">' + msg.from + ':<br><video height="100%" width="100%" onclick="this.play()"><source src=' + msg.context + '></video></li>');
            scrollToLocation();
        } else if (data.code == -1) {
            $('#chat').append('<li class="sys">' + msg.context + '</li>');
        }
    };

    webSocket.onerror = function(event) {
        console.log(event);
    };

    webSocket.onopen = function(event) {
        console.log(event);
    };

    webSocket.onclose = function() {
        if (tryTime < 10) {
            if (tryTime == 0) $('#chat').append('<li class="sys">Try connecting...</li>');
            setTimeout(function() {
                webSocket = null;
                tryTime++;
                initSocket();
            }, 500);
        } else {
            tryTime = 0;
        }
    };

    webSocket.onerror = function() {
        $('#chat').append('<li class="sys">Connecting Error!</li>');
    };
}

$(document).ready(function() {
    initSocket();
    img = $('#img');
    $('#chat').empty;
    $('#userId').val(id);
    $('body').height($(window).height());
    $('#chat').height($(window).height() - $('.input-area').height() - 40);
    $('#submit_form').attr('action', serverURL + 'imgUpload');
    img.change(function() {
        if (img.val() != '') {
            $('#submit_form').submit();
            $('#chat').append('<li class="me" id="uploading">Uploading...</li>');
            scrollToLocation();
        }
        img.val("");
    });
});