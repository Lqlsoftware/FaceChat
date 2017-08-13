var webSocket = null;
var tryTime = 0;
var token = GetQueryString('token');
var id = token.split("_")[0];
var img = null;
var numvalue = 0;
var serverURL = "http://localhost:8080/FuckChat/";
var isMain = false;

function getLocalTime(nS) {
    return new Date(parseInt(nS)).toLocaleString().replace(/:\d{1,2}$/, ' ');
}

function sendtext() {
    var mydate = new Date();
    var myday = mydate.getDate();
    var hour = mydate.getHours();
    var minute = mydate.getMinutes();
    if (webSocket != null && webSocket.readyState == 1) {
        if (msg.value != "") {
            $('#chat').append('<li class="me" >' + getLocalTime(mydate.getTime()) + '</li>' + '<br>' + '<li class="me" time="' + mydate.getTime() + '">' + msg.value + '</li>');
            webSocket.send(msg.value);
            msg.value = "";
            scrollToLocation();
        }
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

function setUserConfig(config) {
    var background = config.background;
    var myHead = config.head;
    $('#convo').css({
        "background": "url(bg/" + background + ") no-repeat",
        "background-size": "100% auto",
        "background-position": "center",
        "background-color": "none"
    })
    $('.chat-thread.me{' +
        'animation: me 0.15s 1 ease-in;' +
        '-moz-animation: me 0.15s 1 ease-in;' +
        '-webkit-animation: me 0.15s 1 ease-in;' +
        'float: right;' +
        'margin-right: 80px;' +
        'margin-bottom: 10px;' +
        'color: #0AD5C1;' +
        'background-color: rgba(25, 147, 147, 0.2);' +
        '}' +
        '.chat-thread .me:before {' +
        'right: -80px;' +
        'background-image:url(bg/' + myHead + ');' +
        '.chat-thread .me:after {' +
        'border-right: 15px solid transparent;' +
        'right: -15px;' +
        '}'
    ).appendTo('head');
}

function onMessage(msg) {
    var time_difference = msg.timestamp - $('#chat').find('li:last').attr("time");
    var time_show = false;
    if (time_difference >= 1800000) time_show = true;
    console.log(time_show);
    if (msg.type == "text")
        if (msg.from == id)
            $('#chat').append('<li class="me" >' + getLocalTime(msg.timestamp) + '</li>' + '<br>' + '<li class="me" time="' + msg.timestamp + '"><span class="head"></span>' + msg.context + '</li>');
        else
            $('#chat').append('<li class="from" >' + msg.from + ' ' + getLocalTime(msg.timestamp) + '</li>' + '<br>' + '<li class="to" time="' + msg.timestamp + '"><span class="head"></span>' + msg.context + '</li>');
    else if (msg.type == "img") {
        if (msg.from == id) {
            var target = $('#chat').find('#uploading:first');
            if (!target[0])
                $('#chat').append('<li class="me" >' + getLocalTime(msg.timestamp) + '</li>' + '<br>' + '<li class="me" time="' + msg.timestamp + '"><img src=' + msg.context + ' class="img" data-source="' + msg.context + '"></li>');
            else {
                target.empty();
                target.append('<li class="me" time="' + msg.timestamp + '">' + getLocalTime(msg.timestamp) + '</li>' + '<br>' + '<img src=' + msg.context + ' class="img" data-source="' + msg.context + '">');
                target.attr('id', '');
            }
        } else
            $('#chat').append('<li class="from" >' + msg.from + ' ' + getLocalTime(msg.timestamp) + '</li>' + '<br>' + '<li class="to" time="' + msg.timestamp + '">' + ':<br><img src=' + msg.context + ' class="img" data-source="' + msg.context + '"></li>');
        $(function() {
            $('#lightbox').lightbox({
                ifChange: true
            });
        });
    } else if (msg.type == "vid") {
        if (msg.from == id) {
            var target = $('#chat').find('#uploading:first');
            if (!target[0])
                $('#chat').append('<li class="me" >' + getLocalTime(msg.timestamp) + '</li>' + '<br>' + '<li class="me" time="' + msg.timestamp + '"><video height="100%" width="100%" onclick="this.play()"><source src=' + msg.context + '></video></li>');
            else {
                target.empty();
                target.append('<video height="100%" width="100%" onclick="this.play()"><source src=' + msg.context + '></video>');
                target.attr('id', '');
            }
        } else
            $('#chat').append('<li class="from" >' + msg.from + ' ' + getLocalTime(msg.timestamp) + '</li><br><li class="to" time="' + msg.timestamp + '">' + ':<br><video height="100%" width="100%" onclick="this.play()"><source src=' + msg.context + '></video></li>');
    }
    scrollToLocation();
}

function onSysMessage(data) {
    if (data.code == -1) {
        $('#chat').append('<li class="sys">' + msg.context + '</li>');
    } else if (data.code == -2) {
        $('#chat').append('<li class="sys">' + msg.context + '</li>');
        var Tip = $('<div id="Tip"><input type="text" id="phone" placeholder="Phone"><input type="password" id="password" placeholder="Password"><input type="button" id="sm" value="Sign in"></div>');
        var winHeight = typeof window.innerHeight != 'undefined' ? window.innerHeight : document.documentElement.clientHeight;
        $("body").append(Tip);
        $("#Tip").css({
            "position": "fixed",
            "left": "0",
            "top": "0",
            "height": winHeight,
            "width": "100%",
            "z-index": "1000",
            "background-color": "rgba(0,0,0,0.8)",
            "filter": "alpha(opacity=80)",
            "display": "flex",
            "justify-content": "center",
            "align-items": "center"
        });
        $("#Tip input").css({
            "padding-left": "10%",
            "border": "1px solid #FFF",
            "border-radius": "5px",
            "padding-right": "10%",
            "width": "70%",
            "display": "flex",
        });
        $("#Tip input[id='sm']").css({
            "border": "3px solid #0bd38a",
        });
        $('#sm').onclick = function() {
            $.ajax({
                type: "POST",
                url: "http://lqlsoftware.top/test/tokenLogin",
                data: { "username": $("#phone").val(), "password": $("#password").val(), "token": token },
                success: function(data) {
                    if (data.code == 1 && data.errMsg == "") {
                        $("#Tip").remove();
                        initSocket();
                    } else {
                        $("#phone").val("");
                        $("#password").val("");
                        $("#phone").attr("placeholder", data.errMsg);
                    }
                },
                dataType: "json"
            });
        }
    }
}

function initSocket() {
    var mydate = new Date();
    var daytime = mydate.getTime();
    if (!window.WebSocket) {
        alert("您的浏览器不支持websocket！");
        return false;
    }

    webSocket = new WebSocket('ws://' + serverURL.split('//')[1] + "chat/" + token);

    webSocket.onmessage = function(res) {
        var data = JSON.parse(res.data);
        var msg = data.data;

        if (data.code == 2) {
            // 用户个人设置
            setUserConfig(msg.context);
        } else if (data.code == 1) {
            // 收到消息
            onMessage(msg);
        } else {
            // 收到系统消息
            onSysMessage(data);
        }
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
    $('body').height($(window).height());
    $('#chat').height($(window).height() - $('.input-area').height() - 40);
    img.change(function() {
        if (img.val() !== '') {
            uploadFile();
            $('#chat').append('<li class="me" id="uploading"><div class="circleChart"></div></li>');
            $(".circleChart").circleChart({
                size: $('body').width() / 5,
                value: 0,
                text: false,
                animate: false,
                onDraw: function(el, circle) {
                    circle.text(numvalue + '%');
                }
            });
            scrollToLocation();
        }
        img.val("");
    });
});

function uploadFile() {
    var fd = new FormData();
    fd.append("img", document.getElementById('img').files[0]);
    fd.append("userId", id);
    var xhr = new XMLHttpRequest();
    xhr.upload.addEventListener("progress", uploadProgress, false);
    xhr.addEventListener("load", uploadComplete, false);
    // xhr.addEventListener("error", uploadFailed, false);
    // xhr.addEventListener("abort", uploadCanceled, false);
    xhr.open("POST", './imgUpload');
    xhr.send(fd);
}

function uploadProgress(evt) {
    var uploading = $('#uploading');
    if (evt.lengthComputable) {
        numvalue = Math.round(evt.loaded * 100 / evt.total);
        $(".circleChart").circleChart({
            value: numvalue
        });
    } else {
        document.getElementById('uploading').innerHTML = '上传失败';
    }
}

function uploadComplete(evt) {
    /* 服务器端返回响应时候触发event事件*/
    numvalue = 0;
}