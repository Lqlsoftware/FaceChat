addEventListener("load", function() {
    setTimeout(hideURLbar, 0);
}, false);

function hideURLbar() {
    window.scrollTo(0, 1);
}

document.ontouchmove = function(e) {
    e.preventDefault();
}

$('#sm').click(function() {
    if ($("#id").val() != '' && $("#pw").val() != '') {
        $.ajax({
            type: "POST",
            url: "/login",
            data: {username:$("#id").val(), password:$("#pw").val()},
            dataType: "json",
            success: function(data){
                self.location.href = "chat.html?token=" + data.get('token');
            },
            error: function (data) {
                alert(data.get('errMsg'));
            }
        });
    }
});