export function makeListInputFrame() {
    var content = document.getElementById("content");
    content.textContent = null;

    var listSection = document.createElement('section');
    var inputSection = document.createElement('section');

    listSection.className = "list-section";
    inputSection.className = "input-section";

    var listBlock1 = document.createElement('div');
    var inputBlock1 = document.createElement('div');

    listBlock1.className = "block1";
    inputBlock1.className = "block1";

    listSection.appendChild(listBlock1);
    inputSection.appendChild(inputBlock1);

    content.appendChild(listSection);
    content.appendChild(inputSection);
}

export function makeSearchResultFrame() {
    var content = document.getElementById("content");
    content.textContent = null;

    var searchSection = document.createElement('section');
    var resultSection = document.createElement('section');

    searchSection.className = "search-section";
    resultSection.className = "result-section";

    var searchBlock1 = document.createElement('div');
    var resultBlock1 = document.createElement('div');

    searchBlock1.className = "block1";
    resultBlock1.className = "block1";

    searchSection.appendChild(searchBlock1);
    resultSection.appendChild(resultBlock1);

    content.appendChild(searchSection);
    content.appendChild(resultSection);
}

export function clearListBlock1() {
    var listBlock1 = this.listBlock1();
    listBlock1.textContent = null;
}

export function clearInputBlock1() {
    var inputBlock1 = this.inputBlock1();
    inputBlock1.textContent = null;
}

export function clearResultBlock1() {
    var resultBlock1 = this.resultBlock1();
    resultBlock1.textContent = null;
}

export function listBlock1() {
    var content = document.getElementById("content");
    var listSection = content.getElementsByClassName("list-section")[0];
    return listSection.getElementsByClassName("block1")[0];
}

export function inputBlock1() {
    var content = document.getElementById("content");
    var inputSection = content.getElementsByClassName("input-section")[0];
    return inputSection.getElementsByClassName("block1")[0];
}

export function searchBlock1() {
    var content = document.getElementById("content");
    var listSection = content.getElementsByClassName("search-section")[0];
    return listSection.getElementsByClassName("block1")[0];
}

export function resultBlock1() {
    var content = document.getElementById("content");
    var inputSection = content.getElementsByClassName("result-section")[0];
    return inputSection.getElementsByClassName("block1")[0];
}