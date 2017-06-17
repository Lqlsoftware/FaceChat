var webSocket = null;
var tryTime = 0;
var token = GetQueryString('token');
var id = "";
var img = null;

function sendtext() {
    if (webSocket != null && webSocket.readyState == 1) {
        var l = $('#chat').find('li:last');
        $('#chat').append('<li class="me">' + msg.value + '</li>');
        var ln = $('#chat').find('li:last');
        ln.offset().top = l.offset().top + l.height;
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
    webSocket = new WebSocket("ws://localhost:8080/FuckChat/chat/" + token);

    webSocket.onmessage = function(res) {
        var a = res.data;
        var reg = new RegExp("^sys:.*$");
        // if (typeof a == "string") {
        if (a.match(reg) != null) {
            var b = a.split('sys:');
            $('#chat').append('<li class="sys">' + b[1] + '</li>');
        } else if (a.match(new RegExp("^.*:img.*$")) != null) {
            var b = a.split(':img');
            if (b[0] == id)
                $('#chat').append('<li class="me"><img class="img" src=' + b[1] + '></li>');
            else
                $('#chat').append('<li class="to">' + b[0] + ':<br><img class="img" src=' + b[1] + '></li>');
        } else if (a.match(new RegExp("^.*:vid.*$")) != null) {
            var b = a.split(':vid');
            if (b[0] == id)
                $('#chat').append('<li class="me"><video height="100%" width="100%" onclick="this.play()"><source src=' + b[1] + '></video></li>');
            else
                $('#chat').append('<li class="to">' + b[0] + ':<br><video height="100%" width="100%" onclick="this.play()"><source src=' + b[1] + '></video></li>');
        } else {
            var b = a.split(':');
            if (b[0] == id)
                $('#chat').append('<li class="me"><span class="head"></span>' + b[1] + '</li>');
            else
                $('#chat').append('<li class="to"><span class="head"></span>' + b[0] + ':' + b[1] + '</li>');
        }
        scrollToLocation();
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
    img = $("#img");
    $('#chat').empty;
    $('#userId').val(id);
    $("body").height($(window).height());
    $("#chat").height($(window).height() - $(".input-area").height() - 40);
    img.change(function() {
        if (img.val() != '') $("#submit_form").submit();
        img.val("");
    });
});