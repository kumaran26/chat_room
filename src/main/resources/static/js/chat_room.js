function login() {
    var username = $('#username').val();
    if (!username) return;
    location.href = '/index?username=' + username;
}

document.onkeydown = function(event) {
    var e = event || window.event || arguments.callee.caller.arguments[0];
    e.keyCode === 13 && login();
};