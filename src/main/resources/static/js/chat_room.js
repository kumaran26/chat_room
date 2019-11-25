function login() {

}

document.onkeydown = function (event) {
	var e = event || window.event || arguments.callee.caller.arguments[0];
	e.keyCode === 13 && login();
};