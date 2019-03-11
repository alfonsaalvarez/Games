function registrar() {
	var request = new XMLHttpRequest();
	request.open("post", "registrar.jsp");
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	request.onreadystatechange = function() {
		if (request.readyState == 4) {
			var respuesta=JSON.parse(request.responseText);
			if (respuesta.result == "OK") {
				sessionStorage.setItem('email', email.value);
				window.location.href = "index.html";
			} else
				mensajeRegistro.innerHTML = respuesta.mensaje;
		}
	};
	var p = {
		email : email.value,
		pwd1 : pwd1.value,
		pwd2 : pwd2.value
	};
	request.send("p=" + JSON.stringify(p));
}

function login() {
	var request = new XMLHttpRequest();
	request.open("post", "login.jsp");
	request.setRequestHeader("Content-Type",
			"application/x-www-form-urlencoded");
	request.onreadystatechange = function() {
		if (request.readyState == 4) {
			var respuesta = JSON.parse(request.responseText);
			if (respuesta.result == "OK") {
				if (respuesta.reg == "si") {
					crearCookie();
					sessionStorage.setItem('email', email.value);
					window.location.href = "panel.html";
				} else {
					sessionStorage.setItem('email', email.value);
					window.location.href = "Tablero.html";
				}
			} else
				mensajeRegistro.innerHTML = respuesta.mensaje;
		}
	};
	var p = {
		email : email.value,
		pwd1 : pwd1.value
	};
	request.send("p=" + JSON.stringify(p));
}

function logout() {
	var request = new XMLHttpRequest();
	request.open("post", "logout.jsp");
	request.setRequestHeader("Content-Type",
			"application/x-www-form-urlencoded");
	request.onreadystatechange = function() {
		if (request.readyState == 4) {
			var respuesta = JSON.parse(request.responseText);
			if (respuesta.result == "OK") {
				if (respuesta.reg == "si") {
					logoutGoogle();
				}	
			}
			window.location.href = "index.html";	
		}
	};
	
	request.send();	
}


function cambiarPass () {
	var request = new XMLHttpRequest();
	request.open("post", "cambioContrasena.jsp");
	request.setRequestHeader("Content-Type",
			"application/x-www-form-urlencoded");
	request.onreadystatechange = function() {
		if (request.readyState == 4) {
			var respuesta = JSON.parse(request.responseText);
			if (respuesta.result == "OK") {
				window.location.href = "panel.html";
			}
		}
	};
	
	var p = {
		email : sessionStorage.getItem('email'),
		pwd1 : pwd1.value,
		pwd1New : pwd1New.value,
		pwd2New : pwd2New.value
	};
	
	request.send("p=" + JSON.stringify(p));
}

function ranking2 () {
	var request = new XMLHttpRequest();
	request.open("post", "ranking.jsp");
	request.setRequestHeader("Content-Type",
			"application/x-www-form-urlencoded");
	request.onreadystatechange = function() {
		if (request.readyState == 4) {
			//Se coge la respuesta y el numero de usuarios, y tambien la tabla
			var respuesta = JSON.parse(request.responseText);
			var numero = respuesta.numero[0];
			var table = document.getElementById("ranking").getElementsByTagName('tbody')[0];
			
			//Se borran las filas anteriores
			$("#ranking tbody tr").remove();
					
			//Se recorre el bucle el número de usuarios que haya en orden
			for (var i = 1; i <= numero; i++) {
				var usuario = "user_" + i;
				//Se coge el primer objeto y se parsea
				var obj = respuesta [usuario];
				var re = JSON.parse (obj);
				
				//Se inserta una fila nueva en la tabla
				var row = table.insertRow (table.rows.length);
				
				//Se inserta la primera columna de la fila
				var celda1 = row.insertCell(0);
				var textoCelda1 = document.createTextNode (re.email);
				celda1.appendChild(textoCelda1);
				
				//Se inserta la segunda columna de la fila
				var celda2 = row.insertCell(1);
				var textoCelda2 = document.createTextNode (re.victorias);
				celda2.appendChild(textoCelda2);
			}
			
		}
	};
	
	var p = {
		email : sessionStorage.getItem('email'),
	};
	
	request.send("p=" + JSON.stringify(p));
	
}

function cambiarAvatar () {
	var request = new XMLHttpRequest();
	request.open("post", "cambioAvatar.jsp");
	request.setRequestHeader("Content-Type",
			"application/x-www-form-urlencoded");
	request.onreadystatechange = function() {
		if (request.readyState == 4) {
			var respuesta = JSON.parse(request.responseText);
			if (respuesta.result == "OK") {
				window.location.href = "panel.html";
			} else {
				console.log (respuesta.mensaje);
			}
		}
	};
	
	var p = {
		email : sessionStorage.getItem('email'),
		ruta: actual
	};
	
	request.send("p=" + JSON.stringify(p));
	
}

function crearCookie() {
	var clave = document.getElementById("email").value;
	var valor = document.getElementById("pwd1").value;
	valor = window.btoa(valor);
	var diasexpiracion = 53;
	var d = new Date();
	d.setTime(d.getTime() + (diasexpiracion * 24 * 60 * 60 * 1000));
	var expires = "expires=" + d.toUTCString();
	document.cookie = clave + "=" + valor + "; " + expires;
}

function getDatos() {
	var email = document.getElementById("email").value;
	document.getElementById("pwd1").value = obtenerCookie(email);
}

function obtenerCookie(clave) {
	var name = clave + "=";
	var ca = document.cookie.split(';');
	for (var i = 0; i < ca.length; i++) {
		var c = ca[i];
		while (c.charAt(0) == ' ')
			c = c.substring(1);
		if (c.indexOf(name) == 0)
			return atob(c.substring(name.length, c.length));
	}
	return "";
}

function estaConectado() {
	var request = new XMLHttpRequest();	
	request.open("get", "../estaConectado.jsp");
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	request.onreadystatechange=function() {
		if (request.readyState==4) {
			var respuesta=JSON.parse(request.responseText);
			if (respuesta.result!="OK") {
				alert("No estás conectado y no tienes permiso para esta página");
			}
		}
	};
	request.send();	
}

function onSignIn(googleUser) {
    var profile = googleUser.getBasicProfile();
    var email=profile.getEmail();
    loginGoogle(email);
}

function loginGoogle(email) {
	var request = new XMLHttpRequest();
	request.open("post", "loginGoogle.jsp");
	request.setRequestHeader("Content-Type",
			"application/x-www-form-urlencoded");
	request.onreadystatechange = function() {
		if (request.readyState == 4) {
			var respuesta = JSON.parse(request.responseText);
			if (respuesta.result == "OK") {
				sessionStorage.setItem('email', email);
				window.location.href = "panel.html";
			} else {
				logoutGoogle();
				mensajeRegistro.innerHTML = respuesta.mensaje;
			}
		}
	};
	
	var p = {
		email : email,
	};
	request.send("p=" + JSON.stringify(p));
}

function logoutGoogle() {
    gapi.auth2.getAuthInstance().disconnect();
}

function recuperarPWD(){
	var request = new XMLHttpRequest();
	request.open("post", "recuperarPWD.jsp");
	request.setRequestHeader("Content-Type",
			"application/x-www-form-urlencoded");
	request.onreadystatechange = function() {
		if (request.readyState == 4) {
			var respuesta = JSON.parse(request.responseText);
			if (respuesta.result != "OK") {
				alert("No se puede realizar la recuperación");
			} else {
				window.location.href = 'index.html';
			}
		}
	};
	
	var p = {
		email : txtEmail.value,
	};
	
	request.send("p=" + JSON.stringify(p));
}

function nuevaPWD() {
	var request = new XMLHttpRequest();
	request.open("post", "nuevaPWD.jsp");
	request.setRequestHeader("Content-Type",
			"application/x-www-form-urlencoded");
	
	var token = window.location.href;
	token = token.split("=");
	
	request.onreadystatechange = function() {
		if (request.readyState == 4) {
			var respuesta = JSON.parse(request.responseText);
			if (respuesta.result == "OK") {
				window.location.href = "index.html";
			}
		}
	};
	
	var p = {
		pwd1 : pwd1New.value,
		pwd2 : pwd2New.value,
		token : token[1]
	};
	
	request.send("p=" + JSON.stringify(p));
}