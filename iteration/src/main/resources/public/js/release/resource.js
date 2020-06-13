import release from "../abstract/release.js";

export default class extends release {

    constructor(aWebApi, aSection, aParts) {
        super(aWebApi, aSection, aParts);
    }

    /*
     *  List Section
     */
    async refreshList() {
        this.section.clearListBlock1();
        var url = this.baseUrl + "resources";
        var json = await this.webApi.get(url);
        this.viewList(json);
    }

    viewList(json) {
        var resources = JSON.parse(json);
        console.log(resources);

        resources.forEach(resource => {
            var mainEntry = this.createMainEntry(resource);
            this.section.listBlock1().appendChild(mainEntry);
        });
    }

    createMainEntry(resource) {
        var mainTable = this.createMainTable(resource.resourceId);

        var subEntryLv1s = resource.resourceEntryList.map(resourceEntry =>
            this.createSubEntryLv1(resourceEntry)
        );

        return this.parts.mainEntry(
            [mainTable].concat(subEntryLv1s)
        );
    }

    createMainTable(resourceId) {
        var header = this.parts.tableHeader(
            ["", "リソースID"]
        );

        var edit = this.parts.edit(function() {rResourceInstance().editResource(this.closest('.main-entry'))});
        var trash = this.parts.trash(function() {rResourceInstance().deleteResource(this.closest('.main-entry'))});

        var td1 = this.parts.td("", "", "", [edit, trash]);
        var td2 = this.parts.td(resourceId, "resourceId", "", []);

        var recode = this.parts.tableRecodeFromTds([td1, td2]);

        return this.parts.table(
            this.parts.tableHead([header]),
            this.parts.tableBody([recode])
        );
    }

    createSubEntryLv1(resourceEntry) {
        var subTable = this.createSubTable(resourceEntry);

        return this.parts.subEntry(
            "1", [
                subTable
            ]
        );
    }

    createSubTable(resourceEntry) {
        var header = this.parts.tableHeader(
            ["リソース稼働開始日", "リソース稼働終了日", "1日あたりの消費ストーリーポイント", "1日あたりの費用"]
        );

        var recode = this.parts.tableRecode(
            [resourceEntry.periodStart, resourceEntry.periodEnd, resourceEntry.velocity.storyPoint, resourceEntry.velocity.cost]
        )

        return this.parts.table(
            this.parts.tableHead([header]),
            this.parts.tableBody([recode])
        );
    }

    /*
     *  Input Section: READ
     */
    editResource(mainEntry) {
        var resourceId = mainEntry.getElementsByClassName("resourceId")[0].innerText;
        this.putMode(resourceId);
    }

    async postMode() {
        this.section.clearInputBlock1();
        this.makePostInput();
    }

    async putMode(resourceId) {
        this.section.clearInputBlock1();
        var url = this.baseUrl + "resources/" + resourceId;

        var json = await this.webApi.get(url);
        this.makePutInput(JSON.parse(json));
    }

    makePutInput(resource) {
        this.section.inputBlock1().appendChild(
            this.parts.div(
                "level-1",
                "",
                [this.parts.input(resource.resourceId, "", "resourceId", "リソースID")]
            )
        );

        this.section.inputBlock1().appendChild(
            this.makeIResourceEntryList(resource.resourceEntryList)
        );

        this.section.inputBlock1().appendChild(
            this.parts.operationBlock(
                function() {rResourceInstance().erase()},
                function() {rResourceInstance().send()},
            )
        );
    }

    makePostInput() {
        var resourceEntry = new Object;
        resourceEntry.periodStart = "";
        resourceEntry.periodEnd = "";

        var velocity = new Object;
        velocity.storyPoint = "";
        velocity.cost = "";

        resourceEntry.velocity = velocity;

        var resourceEntryList = new Array();
        resourceEntryList.push(resourceEntry);

        this.section.inputBlock1().appendChild(
            this.makeIResourceEntryList(resourceEntryList)
        );

        this.section.inputBlock1().appendChild(
            this.parts.operationBlock(
                function() {rResourceInstance().erase()},
                function() {rResourceInstance().send()},
            )
        );
    }

    makeIResourceEntryList(resourceEntryList) {
        var label = this.parts.label("リソースエントリーリスト");
        var iDivs = resourceEntryList.map(resourceEntry =>
            this.parts.div("i", "", [
                this.parts.minusButton(),
                this.parts.div("level-2", "",
                    [this.parts.input(resourceEntry.periodStart, "periodStart", "", "リソース稼働開始日", function() {this.type='date'})]),
                this.parts.div("level-2", "",
                    [this.parts.input(resourceEntry.periodEnd, "periodEnd", "", "リソース稼働終了日", function() {this.type='date'})]),
                this.makeIVelocity(resourceEntry.velocity)
                ]
            )
        );
        var plusButton = this.parts.plusButton(function() {rResourceInstance().addResourceEntryInput(this.closest('#resourceEntryList'), this.closest('div'))});
        return this.parts.div(
            "level-1",
            "resourceEntryList",
            [label].concat(iDivs).concat([plusButton])
        );
    }

    makeIVelocity(velocity) {
        var label = this.parts.label("ベロシティ");

        var div1 = this.parts.div(
            "level-3",
            "",
            [this.parts.input(velocity.storyPoint, "storyPoint", "", "1日あたりの消費ストーリーポイント")]
        );

        var div2 = this.parts.div(
            "level-3",
            "",
            [this.parts.input(velocity.cost, "cost", "", "1日あたりの費用")]
        )

        return this.parts.div(
            "level-2",
            "velocity",
            [label, div1, div2]
        );
    }

    addResourceEntryInput(resourceEntryListEL, plusButtonEl) {
        var velocity = new Object;
        velocity.storyPoint = "";
        velocity.cost = "";

        var newInput = this.parts.div("i", "", [
            this.parts.minusButton(),
            this.parts.div("level-2", "",
                [this.parts.input("", "periodStart", "", "リソース稼働開始日", function() {this.type='date'})]),
            this.parts.div("level-2", "",
                [this.parts.input("", "periodEnd", "", "リソース稼働終了日", function() {this.type='date'})]),
            this.makeIVelocity(velocity)
            ]
        );
        resourceEntryListEL.insertBefore(newInput, plusButtonEl);
    }

    /*
     *  Input Section: POST and PUT
     */
    isPostMode() {
        return document.getElementById("resourceId") == null;
    }

    getPostObjectList() {
        var resourceObject = new Object();
        resourceObject.resourceEntryList = this.makeResourceEntryList();
        return [resourceObject];
    }

    getPostUrl() {
        return this.baseUrl + "resources";
    }

    getPutObject() {
        var resourceObject = new Object();
        resourceObject.resourceId = utilInstance().valueOfInputById("resourceId");
        resourceObject.resourceEntryList = this.makeResourceEntryList();
        return resourceObject;
    }

    getPutUrl() {
        return this.baseUrl + "resources/" + utilInstance().valueOfInputById("resourceId");
    }

    makeResourceEntryList() {
        var resourceEntryList = new Array();

        var resourceEntryListDiv = document.getElementById("resourceEntryList");
        Array.from(resourceEntryListDiv.querySelectorAll(':scope > .i')).forEach(i => {
            var resourceEntry = new Object;
            resourceEntry.periodStart = utilInstance().valueOfInputByClass(i, "periodStart");
            resourceEntry.periodEnd = utilInstance().valueOfInputByClass(i, "periodEnd");

            var velocity = new Object;
            velocity.storyPoint = utilInstance().valueOfInputByClass(i, "storyPoint");
            velocity.cost = utilInstance().valueOfInputByClass(i, "cost");

            resourceEntry.velocity = velocity;
            resourceEntryList.push(resourceEntry);
        });
        return resourceEntryList;
    }

    /*
     *  Input Section: DELETE
     */
    async deleteResource(mainEntry) {
        var resourceId = mainEntry.getElementsByClassName("resourceId")[0].innerText;
        var url = this.baseUrl + "resources/" + resourceId;
        await this.webApi.doDelete(url);
        this.refreshList();
    }
};
