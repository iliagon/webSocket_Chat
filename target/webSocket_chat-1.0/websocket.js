var ws;


function connect() {

    var username = document.getElementById("username").value;

    var host = document.location.host;
    var pathname = document.location.pathname;

    ws = new WebSocket("ws://" + host + pathname + "chat/" + username);

    ws.onmessage = function (event) {
        var log = document.getElementById("log");
        console.log(event.data);
        var message = JSON.parse(event.data);
        if (message.type === "members") {
            membersInit(message.list);
        }
        else if (message.type === "message")
        log.innerHTML += message.from + " : " + message.content + "\n";
    };
}

function membersInit(list) {
    document.getElementsByClassName("checkboxDiv")[0].innerHTML = '';
    list.forEach(function (entry) {
        console.log(entry);
        var checkboxAndUser = document.createElement('div');
        checkboxAndUser.className = "checkboxAndUser";
        checkboxAndUser.id = entry;

        var checkbox = document.createElement('input');
        checkbox.type = "checkbox";
        checkbox.className = "checkboxClass";

        var user = document.createElement('div');
        user.innerHTML = entry;
        checkboxAndUser.appendChild(user);
        checkboxAndUser.appendChild(checkbox);

        document.getElementsByClassName("checkboxDiv")[0].appendChild(checkboxAndUser);
    });

}

function send() {
    var content = document.getElementById("msg").value;
    var json = JSON.stringify({
        "content": content
    });

    ws.send(json);
}