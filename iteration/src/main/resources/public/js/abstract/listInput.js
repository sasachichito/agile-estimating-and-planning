export default class {
    constructor(aWebApi, aSection, aParts) {
        this.webApi = aWebApi;
        this.section = aSection;
        this.parts = aParts;
    }

    initContent() {
        this.section.makeListInputFrame();
        this.refreshList();
        this.postMode();
    }

    refreshList() {
        throw new Error('You have to implement the method');
    }

    postMode() {
        throw new Error('You have to implement the method');
    }

    viewErr(message) {
        utilInstance().rmIfExistClass(this.section.inputBlock1(), "err-message-box");
        this.section.inputBlock1().insertBefore(
            this.parts.errMessage(message),
            utilInstance().firstElByClass(this.section.inputBlock1(), "operation-block")
        );
    }

    erase() {
        this.postMode();
    }

    async send() {
        if (this.isPostMode()) {
            this.post();
        } else {
            this.put();
        }
    }

    async post() {
        var response = await this.webApi.post(
            this.getPostUrl(),
            this.getPostObjectList()
        );
        if (response.ok) {
            this.refreshList();
            this.postMode();
            return;
        }
        var message  = await this.webApi.retrieveErrMessage(response);
        this.viewErr(message);
    }

    async put() {
        var response = await this.webApi.put(
            this.getPutUrl(),
            this.getPutObject()
        );
        if (response.ok) {
            this.refreshList();
            this.postMode();
            return;
        }
        var message  = await this.webApi.retrieveErrMessage(response);
        this.viewErr(message);
    }

    isPostMode() {
        throw new Error('You have to implement the method');
    }

    getPostObjectList() {
        throw new Error('You have to implement the method');
    }

    getPostUrl() {
        throw new Error('You have to implement the method');
    }

    getPutObject() {
        throw new Error('You have to implement the method');
    }

    getPutUrl() {
        throw new Error('You have to implement the method');
    }
}