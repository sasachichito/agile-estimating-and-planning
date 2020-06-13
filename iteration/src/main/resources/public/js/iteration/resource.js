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

        var edit = this.parts.edit(function() {iResourceInstance().editResource(this.closest('.main-entry'))});
        var trash = this.parts.trash(function() {iResourceInstance().deleteResource(this.closest('.main-entry'))});

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

        var subEntryLv2 = this.createSubEntryLv2(resourceEntry.memberSet);

        return this.parts.subEntry(
            "1", [
                subTable, subEntryLv2
            ]
        );
    }

    createSubTable(resourceEntry) {
        var header = this.parts.tableHeader(
            ["リソース稼働開始日", "リソース稼働終了日"]
        );

        var recode = this.parts.tableRecode(
            [resourceEntry.periodStart, resourceEntry.periodEnd]
        )

        return this.parts.table(
            this.parts.tableHead([header]),
            this.parts.tableBody([recode])
        );
    }

    createSubEntryLv2(memberSet) {
        var header = this.parts.tableHeader(
            ["開発者名", "1日あたりの稼働時間", "1時間あたりの単価"]
        );

        var recodes = memberSet.map(member =>
            this.parts.tableRecode(
                [member.name, member.workingHoursPerDay, member.unitCostPerHour]
            )
        );

        return this.parts.subEntry(
            "2", [
                this.parts.table(
                    this.parts.tableHead([header]),
                    this.parts.tableBody(recodes)
                )
            ]
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
                function() {iResourceInstance().erase()},
                function() {iResourceInstance().send()},
            )
        );
    }

    makePostInput() {
        var memberObject = new Object();
        memberObject.name = "";
        memberObject.workingHoursPerDay = "";
        memberObject.unitCostPerHour = "";

        var memberSet = new Array();
        memberSet.push(memberObject);

        var resourceEntry = new Object;
        resourceEntry.periodStart = "";
        resourceEntry.periodEnd = "";
        resourceEntry.memberSet = memberSet;

        var resourceEntryList = new Array();
        resourceEntryList.push(resourceEntry);

        this.section.inputBlock1().appendChild(
            this.makeIResourceEntryList(resourceEntryList)
        );

        this.section.inputBlock1().appendChild(
            this.parts.operationBlock(
                function() {iResourceInstance().erase()},
                function() {iResourceInstance().send()},
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
                this.makeIMemberSet(resourceEntry.memberSet)
                ]
            )
        );
        var plusButton = this.parts.plusButton(function() {iResourceInstance().addResourceEntryInput(this.closest('#resourceEntryList'), this.closest('div'))});
        return this.parts.div(
            "level-1",
            "resourceEntryList",
            [label].concat(iDivs).concat([plusButton])
        );
    }

    makeIMemberSet(memberSet) {
        var label = this.parts.label("メンバーセット");

        var iDivs = memberSet.map(member =>
            this.parts.div("i", "", [
                this.parts.minusButton(),
                this.parts.div("level-3", "",
                    [this.parts.input(member.name, "name", "", "開発者名")]),
                this.parts.div("level-3", "",
                    [this.parts.input(member.workingHoursPerDay, "workingHoursPerDay", "", "1日あたりの稼働時間")]),
                this.parts.div("level-3", "",
                    [this.parts.input(member.unitCostPerHour, "unitCostPerHour", "", "1時間あたりの単価")])
                ]
            )
        );

        var plusButton = this.parts.plusButton(function() {iResourceInstance().addMemberInput(this.closest('.member-set'), this.closest('div'))});
        return this.parts.div(
            "level-2 member-set",
            "",
            [label].concat(iDivs).concat([plusButton])
        );
    }

    addMemberInput(memberSetEl, plusButtonEl) {
        var newInput = this.parts.div("i", "", [
            this.parts.minusButton(),
            this.parts.div("level-3", "",
                [this.parts.input("", "name", "", "開発者名")]),
            this.parts.div("level-3", "",
                [this.parts.input("", "workingHoursPerDay", "", "1日あたりの稼働時間")]),
            this.parts.div("level-3", "",
                [this.parts.input("", "unitCostPerHour", "", "1時間あたりの単価")])
            ]
        );
        memberSetEl.insertBefore(newInput, plusButtonEl);
    }

    addResourceEntryInput(resourceEntryListEL, plusButtonEl) {
        var newInput = this.parts.div("i", "", [
            this.parts.minusButton(),
            this.parts.div("level-2", "",
                [this.parts.input("", "periodStart", "", "リソース稼働開始日", function() {this.type='date'})]),
            this.parts.div("level-2", "",
                [this.parts.input("", "periodEnd", "", "リソース稼働終了日", function() {this.type='date'})]),
            this.makeIMemberSet([])
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
            resourceEntry.memberSet = this.makeMemberSet(utilInstance().firstElByClass(i, "member-set"));

            resourceEntryList.push(resourceEntry);
        });
        return resourceEntryList;
    }

    makeMemberSet(memberSetDiv) {
        var memberSet = new Array();
        Array.from(memberSetDiv.querySelectorAll(':scope > .i')).forEach(i => {
            var memberObject = new Object();
            memberObject.name = utilInstance().valueOfInputByClass(i, "name");
            memberObject.workingHoursPerDay = utilInstance().valueOfInputByClass(i, "workingHoursPerDay");
            memberObject.unitCostPerHour = utilInstance().valueOfInputByClass(i, "unitCostPerHour");

            memberSet.push(memberObject);
        });
        return memberSet;
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
