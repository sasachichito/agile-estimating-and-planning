import iteration from "../abstract/iteration.js";

export default class extends iteration {

    constructor(aWebApi, aSection, aParts) {
        super(aWebApi, aSection, aParts);
    }

    /*
     *  List Section
     */
    async refreshList() {
        this.section.clearListBlock1();
        var url = this.baseUrl + "burns";
        var json = await this.webApi.get(url);
        this.viewList(json);
    }

    viewList(json) {
        var burns = JSON.parse(json);

        burns.forEach(burn => {
            var mainEntry = this.createMainEntry(burn);
            this.section.listBlock1().appendChild(mainEntry);
        });
    }

    createMainEntry(burn) {
        var mainTable = this.createMainTable(burn.burnId, burn.taskId, burn.burnDate);

        return this.parts.mainEntry(
            [mainTable]
        );
    }

    createMainTable(burnId, taskId, burnDate) {
        var header = this.parts.tableHeader(
            ["", "バーンID", "タスクID", "バーン日"]
        );

        var edit = this.parts.edit(function() {iBurnInstance().editBurn(this.closest('.main-entry'))});
        var trash = this.parts.trash(function() {iBurnInstance().deleteBurn(this.closest('.main-entry'))});

        var td1 = this.parts.td("", "", "", [edit, trash]);
        var td2 = this.parts.td(burnId, "burnId", "", []);
        var td3 = this.parts.td(taskId, "taskId", "", []);
        var td4 = this.parts.td(burnDate, "burnDate", "", []);

        var recode = this.parts.tableRecodeFromTds([td1, td2, td3, td4]);

        return this.parts.table(
            this.parts.tableHead([header]),
            this.parts.tableBody([recode])
        );
    }

    /*
     *  Input Section: READ
     */
    editBurn(mainEntry) {
        var burnId = mainEntry.getElementsByClassName("burnId")[0].innerText;
        this.putMode(burnId);
    }

    async postMode() {
        this.section.clearInputBlock1();
        this.makePostInput();
    }

    async putMode(burnId) {
        this.section.clearInputBlock1();
        var url = this.baseUrl + "burns/" + burnId;

        var json = await this.webApi.get(url);
        this.makePutInput(JSON.parse(json));
    }

    makePutInput(burn) {
        this.section.inputBlock1().appendChild(
            this.parts.div(
                "level-1",
                "",
                [this.parts.input(burn.burnId, "", "burnId", "バーンID")]
            )
        );

        this.section.inputBlock1().appendChild(
            this.parts.div(
                "level-1",
                "",
                [this.parts.input(burn.taskId, "", "taskId", "タスクID")]
            )
        );

        this.section.inputBlock1().appendChild(
            this.parts.div(
                "level-1",
                "",
                [this.parts.input(burn.burnDate, "", "burnDate", "バーン日", function() {this.type='date'})]
            )
        );

        this.section.inputBlock1().appendChild(
            this.parts.operationBlock(
                function() {iBurnInstance().erase()},
                function() {iBurnInstance().send()},
            )
        );
    }

    makePostInput() {
        this.section.inputBlock1().appendChild(
            this.parts.div(
                "level-1",
                "",
                [this.parts.input("", "", "taskId", "タスクID")]
            )
        );

        this.section.inputBlock1().appendChild(
            this.parts.operationBlock(
                function() {iBurnInstance().erase()},
                function() {iBurnInstance().send()},
            )
        );
    }

    /*
     *  Input Section: POST and PUT
     */
    isPostMode() {
        return document.getElementById("burnId") == null;
    }

    getPostObjectList() {
        var burnObject = new Object();
        burnObject.taskId = utilInstance().valueOfInputById("taskId");
        return [burnObject];
    }

    getPostUrl() {
        return this.baseUrl + "burns/tasks/";
    }

    getPutObject() {
        var burnObject = new Object();
        burnObject.burnId = utilInstance().valueOfInputById("burnId");
        burnObject.taskId = utilInstance().valueOfInputById("taskId");
        burnObject.burnDate = utilInstance().valueOfInputById("burnDate");
        return burnObject;
    }

    getPutUrl() {
        return this.baseUrl + "burns/" + utilInstance().valueOfInputById("burnId");
    }

    /*
     *  Input Section: DELETE
     */
    async deleteBurn(mainEntry) {
        var burnId = mainEntry.getElementsByClassName("burnId")[0].innerText;
        var url = this.baseUrl + "burns/" + burnId;
        await this.webApi.doDelete(url);
        this.refreshList();
    }
};
