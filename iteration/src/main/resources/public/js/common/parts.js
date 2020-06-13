export function table(head, body) {
    var table = document.createElement('table');
    table.appendChild(head);
    table.appendChild(body);

    return table;
}

export function tableHead(headers = []) {
    var head = document.createElement('tHead');
    headers.forEach(header => {
        head.appendChild(header);
    });
    return head;
}

export function tableBody(recodes = []) {
    var body = document.createElement('tBody');
    recodes.forEach(recode => {
        body.appendChild(recode);
    });
    return body;
}

export function tableHeader(header = []) {
    var tr = document.createElement('tr');

    header.forEach(data => {
        var th = document.createElement('th');
        th.innerText = data;
        tr.appendChild(th);
    });
    return tr;
}

export function tableRecode(recode = []) {
    var tr = document.createElement('tr');

    recode.forEach(data => {
        var td = document.createElement('td');
        td.innerText = data;
        tr.appendChild(td);
    });
    return tr;
}

export function tableRecodeFromTds(recode = []) {
    var tr = document.createElement('tr');
    recode.forEach(td => {
        tr.appendChild(td);
    });
    return tr;
}

export function td(value, className, id, children) {
    var td = document.createElement('td');
    td.innerText = value;
    td.className = className;
    td.id = id;

    children.forEach(child => {
        td.appendChild(child);
    });
    return td;
}

export function subEntry(lv, children) {
    var subEntry = document.createElement('div');
    subEntry.className = "sub-entry-level-" + lv;

    children.forEach(child => {
        subEntry.appendChild(child);
    })
    return subEntry;
}

export function mainEntry(children) {
    var mainEntry = document.createElement('div');
    mainEntry.className = "main-entry";

    children.forEach(child => {
        mainEntry.appendChild(child);
    })
    return mainEntry;
}

export function edit(func) {
    var edit = document.createElement('i');
    edit.className = "fas fa-edit";
    edit.onclick = func;
    return edit;
}

export function trash(func) {
    var trash = document.createElement('i');
    trash.className = "fas fa-trash";
    trash.onclick = func;
    return trash;
}

export function div(className, id, children) {
    var div = document.createElement('div');
    div.className = className;
    div.id = id;

    children.forEach(child => {
        div.appendChild(child);
    });
    return div;
}

export function input(value, className, id, placeholder, onFocus = "") {
    var input = document.createElement('input');
    input.value = value;
    input.className = className;
    input.id = id;
    input.placeholder = placeholder;
    input.onfocus = onFocus;
    return input;
}

export function label(value) {
    var label = document.createElement('label');
    label.innerText = value;
    return label;
}

export function plusButton(onclickFunc) {
    var div = document.createElement('div');
    div.className = "plus";

    var i = document.createElement('i');
    i.className = "fas fa-plus fa-2x";

    var button = document.createElement('a');
    button.className = "plus-button";
    button.onclick = onclickFunc;

    button.appendChild(i);
    div.appendChild(button);

    return div;
}

export function minusButton() {
    var div = document.createElement('div');

    var i = document.createElement('i');
    i.className = "fas fa-minus fa-2x";

    var button = document.createElement('a');
    button.className = "minus-button";
    button.onclick = function() {utilInstance().removeElement(this.closest(".i"))};

    button.appendChild(i);
    div.appendChild(button);

    return div;
}

export function eraseButton(onclickFunc) {
    var div = document.createElement('div');
    div.className = "erase";

    var i = document.createElement('i');
    i.className = "fas fa-eraser fa-3x";

    var button = document.createElement('a');
    button.className = "erase-button";
    button.onclick = onclickFunc;

    button.appendChild(i);
    div.appendChild(button);

    return div;
}

export function sendButton(onclickFunc) {
    var div = document.createElement('div');
    div.className = "send";

    var i = document.createElement('i');
    i.className = "fas fa-cloud-upload-alt fa-3x";

    var button = document.createElement('a');
    button.className = "send-button";
    button.onclick = onclickFunc;

    button.appendChild(i);
    div.appendChild(button);

    return div;
}

export function operationBlock(eraseFunc, sendFunc) {
    var div = document.createElement('div');
    div.className = "operation-block";

    // erase
    var erase = document.createElement('i');
    erase.className = "fas fa-eraser fa-3x";

    var eraseButton = document.createElement('a');
    eraseButton.className = "erase-button";
    eraseButton.onclick = eraseFunc;
    eraseButton.appendChild(erase);

    // send
    var send = document.createElement('i');
    send.className = "fas fa-cloud-upload-alt fa-3x";

    var sendButton = document.createElement('a');
    sendButton.className = "send-button";
    sendButton.onclick = sendFunc;

    sendButton.appendChild(send);

    div.appendChild(eraseButton);
    div.appendChild(sendButton);

    return div;
}

export function errMessage(message) {
    var div = document.createElement('div');
    div.className = "err-message-box";

    var a = document.createElement('a');
    a.innerText = message;
    a.className = "err-message";

    div.appendChild(a);
    return div;
}
