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
        var url = this.baseUrl + "scopes";
        var json = await this.webApi.get(url);
        this.viewList(json);
    }

    viewList(json) {
        var scopes = JSON.parse(json);
        console.log(scopes);

        scopes.forEach(scope => {
            var mainEntry = this.createMainEntry(scope);
            this.section.listBlock1().appendChild(mainEntry);
        });
    }

    createMainEntry(scope) {
        var mainTable = this.createMainTable(scope.scopeId, scope.scopeTitle);

        var subEntryLv1s = this.createSubEntryLv1(scope.storyIdList);

        return this.parts.mainEntry(
            [mainTable].concat(subEntryLv1s)
        );
    }

    createMainTable(scopeId, scopeTitle) {
        var header = this.parts.tableHeader(
            ["", "スコープID", "スコープタイトル"]
        );

        var edit = this.parts.edit(function() {rScopeInstance().editStory(this.closest('.main-entry'))});
        var trash = this.parts.trash(function() {rScopeInstance().deleteScope(this.closest('.main-entry'))});

        var td1 = this.parts.td("", "", "", [edit, trash]);
        var td2 = this.parts.td(scopeId, "scopeId", "", []);
        var td3 = this.parts.td(scopeTitle, "scopeTitle", "", []);

        var recode = this.parts.tableRecodeFromTds([td1, td2, td3]);

        return this.parts.table(
            this.parts.tableHead([header]),
            this.parts.tableBody([recode])
        );
    }

    createSubEntryLv1(storyIdList) {
        var header = this.parts.tableHeader(
            ["ストーリーID"]
        );

        var recodes = storyIdList.map(storyId =>
            this.parts.tableRecode(
                [storyId]
            )
        );

        return this.parts.subEntry(
            "1", [
                this.parts.table(
                    this.parts.tableHead([header]),
                    this.parts.tableBody(recodes)
                )
            ]
        );
    }

    /*
     *  Input Section: Display
     */
    editStory(mainEntry) {
        var scopeId = mainEntry.getElementsByClassName("scopeId")[0].innerText;
        this.putMode(scopeId);
    }

    async postMode() {
        this.section.clearInputBlock1();
        this.makePostInput();
    }

    async putMode(scopeId) {
        this.section.clearInputBlock1();
        var url = this.baseUrl + "scopes/" + scopeId;

        var json = await this.webApi.get(url);
        this.makePutInput(JSON.parse(json));
    }

    makePutInput(scope) {
        this.section.inputBlock1().appendChild(
            this.parts.div(
                "level-1",
                "",
                [this.parts.input(scope.scopeId, "", "scopeId", "ストーリーID")]
            )
        );

        this.section.inputBlock1().appendChild(
            this.parts.div(
                "level-1",
                "",
                [this.parts.input(scope.scopeTitle, "", "scopeTitle", "スコープタイトル")]
            )
        );

        this.section.inputBlock1().appendChild(
            this.makeIStoryIdList(scope.storyIdList)
        );

        this.section.inputBlock1().appendChild(
            this.parts.operationBlock(
                function() {rScopeInstance().erase()},
                function() {rScopeInstance().send()},
            )
        );
    }

    makePostInput() {
        this.section.inputBlock1().appendChild(
            this.parts.div(
                "level-1",
                "",
                [this.parts.input("", "", "scopeTitle", "スコープタイトル")]
            )
        );

        this.section.inputBlock1().appendChild(
            this.makeIStoryIdList([""])
        );

        this.section.inputBlock1().appendChild(
            this.parts.operationBlock(
                function() {rScopeInstance().erase()},
                function() {rScopeInstance().send()},
            )
        );
    }

    makeIStoryIdList(storyIdList) {
        var label = this.parts.label("ストーリーIDリスト");
        var iDivs = storyIdList.map(storyId =>
            this.parts.div("i", "", [
                this.parts.minusButton(),
                this.parts.div("level-2", "", [
                    this.parts.input(storyId, "storyId", "", "ストーリーID")
                ])
            ])
        );
        var plusButton = this.parts.plusButton(function() {rScopeInstance().addStoryIdInput(this.closest('#storyIdList'), this.closest('div'))});
        return this.parts.div(
            "level-1",
            "storyIdList",
            [label].concat(iDivs).concat([plusButton])
        );
    }

    addStoryIdInput(storyIdListEL, plusButtonEl) {
        var newInput = this.parts.div("i", "", [
                this.parts.minusButton(),
                this.parts.div("level-2", "", [
                    this.parts.input("", "storyId", "", "ストーリーID")
                ])
            ]
        );
        storyIdListEL.insertBefore(newInput, plusButtonEl);
    }

    /*
     *  Input Section: POST and PUT
     */
    isPostMode() {
        return document.getElementById("scopeId") == null;
    }

    getPostObjectList() {
        var scopeObject = new Object();
        scopeObject.scopeTitle = utilInstance().valueOfInputById("scopeTitle");
        scopeObject.storyIdList = this.makeStoryIdList();
        return [scopeObject];
    }

    getPostUrl() {
        return this.baseUrl + "scopes";
    }

    getPutObject() {
        var scopeObject = new Object();
        scopeObject.scopeId = utilInstance().valueOfInputById("scopeId");
        scopeObject.scopeTitle = utilInstance().valueOfInputById("scopeTitle");
        scopeObject.storyIdList = this.makeStoryIdList();
        return scopeObject;
    }

    getPutUrl() {
        return this.baseUrl + "scopes/" + utilInstance().valueOfInputById("scopeId");
    }

    makeStoryIdList() {
        var storyIdList = new Array();

        var storyIdListDiv = document.getElementById("storyIdList");
        Array.from(storyIdListDiv.querySelectorAll(':scope > .i')).forEach(i => {
            if (utilInstance().valueOfInputByClass(i, "storyId") === "") {
                return;
            }
            storyIdList.push(
                utilInstance().valueOfInputByClass(i, "storyId"),
            );
        });
        return storyIdList;
    }

    /*
     *  Input Section: DELETE
     */
    async deleteScope(mainEntry) {
        var url = this.baseUrl + "scopes/" + mainEntry
            .getElementsByClassName("scopeId")[0].innerText;
        await this.webApi.doDelete(url);
        this.refreshList();
    }
};
