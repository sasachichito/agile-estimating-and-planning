export default class {
    removeElement(e) {
        e.remove();
    }

    valueOfInputById(id) {
        var el = document.getElementById(id);
        if (el == null) {
            return "";
        }
        return el.value;
    }

    valueOfInputByClass(parent, className) {
        var el = parent.getElementsByClassName(className)[0];
        if (el == null) {
            return "";
        }
        return el.value;
    }

    firstElByClass(parent, className) {
        return parent.getElementsByClassName(className)[0];
    }

    rmIfExistClass(parent, className) {
        var el = parent.getElementsByClassName(className)[0];
        if (el != null) {
            el.remove();
        }
    }
}