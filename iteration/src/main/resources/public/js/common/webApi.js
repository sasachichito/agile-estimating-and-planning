export function get(url) {
    return fetch(url, {method: "GET",})
        .then(response => response.text())
        .then(text => {
            return text;
        });
}

export function doDelete(url) {
     return fetch(url, {method: "DELETE",})
         .then(response => response.text())
         .then(text => {
             return text;
         });
}

export function post(url, object) {
    return fetch(url, {
        method: "POST",
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(object)})
    .then(response => {
        return response;
    })
    .catch(console.error);
}

export function put(url, object) {
    return fetch(url, {
        method: "PUT",
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(object)})
    .then(response => {
        return response;
    });
}

export function retrieveErrMessage(response) {
    return response.json()
        .then(json => {
            return json.message;
        });
}