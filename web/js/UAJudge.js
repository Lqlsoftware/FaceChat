var Tip;

function judgeUA() {
    var ua = navigator.userAgent.toLowerCase();
    if (ua.match(/MicroMessenger/i) == "micromessenger") {
        Tip = $('<div id="Tip"><p><img src="live_weixin.png" style="max-width: 100%; height: auto;" alt="微信打开"/></p></div>');
        return true;
    } else if (window.navigator.standalone === true) {
        Tip = $('<div id="Tip"><p align="center"><img src="add_to_screen.png" style="max-width: 100%; position: absolute; bottom: 0px;" alt="添加到主屏幕"/></p></div>');
        return true;
    } else {
        return false;
    }
}

$(document).ready(function() {
    var result = judgeUA();

    if (result) {
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
        $("#Tip p").css({
            "text-align": "center",
            "margin-top": "10%",
            "padding-left": "5%",
            "padding-right": "5%",
            "width": "100%",
            "display": "flex",
            "justify-content": "center",
            "align-items": "center"
        });
    }
});