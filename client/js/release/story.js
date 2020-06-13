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
        var url = this.baseUrl + "stories";
        var json = await this.webApi.get(url);
        this.viewList(json);
    }

    viewList(json) {
        var stories = JSON.parse(json);
        console.log(stories);

        stories.forEach(story => {
            var mainEntry = this.createMainEntry(story);
            this.section.listBlock1().appendChild(mainEntry);
        });
    }

    createMainEntry(story) {
        var mainTable = this.createMainTable(story.storyId, story.storyTitle, story.storyPoint.estimate50pct, story.storyPoint.estimate90pct);

        var subEntryLv1s = this.createSubEntryLv1(story.links);

        return this.parts.mainEntry(
            [mainTable].concat(subEntryLv1s)
        );
    }

    createMainTable(storyId, storyTitle, estimate50pct, estimate90pct) {
        var header = this.parts.tableHeader(
            ["", "ストーリーID", "ストーリータイトル", "50%見積もり", "90%見積もり"]
        );

        var edit = this.parts.edit(function() {rStoryInstance().editStory(this.closest('.main-entry'))});
        var trash = this.parts.trash(function() {rStoryInstance().deleteStory(this.closest('.main-entry'))});

        var td1 = this.parts.td("", "", "", [edit, trash]);
        var td2 = this.parts.td(storyId, "storyId", "", []);
        var td3 = this.parts.td(storyTitle, "storyTitle", "", []);
        var td4 = this.parts.td(estimate50pct, "estimate50pct", "", []);
        var td5 = this.parts.td(estimate90pct, "estimate90pct", "", []);

        var recode = this.parts.tableRecodeFromTds([td1, td2, td3, td4, td5]);

        return this.parts.table(
            this.parts.tableHead([header]),
            this.parts.tableBody([recode])
        );
    }

    createSubEntryLv1(links) {
        var header = this.parts.tableHeader(
            ["リンク・メモ"]
        );

        var recodes = links.map(link =>
            this.parts.tableRecode([link])
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
        var storyId = mainEntry.getElementsByClassName("storyId")[0].innerText;
        this.putMode(storyId);
    }

    async postMode() {
        this.section.clearInputBlock1();
        this.makePostInput();
    }

    async putMode(storyId) {
        this.section.clearInputBlock1();
        var url = this.baseUrl + "stories/" + storyId;

        var json = await this.webApi.get(url);
        this.makePutInput(JSON.parse(json));
    }

    makePutInput(story) {
        this.section.inputBlock1().appendChild(
            this.parts.div(
                "level-1",
                "",
                [this.parts.input(story.storyId, "", "storyId", "ストーリーID")]
            )
        );

        this.section.inputBlock1().appendChild(
            this.parts.div(
                "level-1",
                "",
                [this.parts.input(story.storyTitle, "", "storyTitle", "ストーリータイトル")]
            )
        );

        this.section.inputBlock1().appendChild(
            this.makeStoryPoint(story.storyPoint));

        this.section.inputBlock1().appendChild(
            this.makeILinks(story.links));

        this.section.inputBlock1().appendChild(
            this.parts.operationBlock(
                function() {rStoryInstance().erase()},
                function() {rStoryInstance().send()},
            )
        );
    }

    makePostInput() {
        this.section.inputBlock1().appendChild(
            this.parts.div(
                "level-1",
                "",
                [this.parts.input("", "", "storyTitle", "ストーリータイトル")]
            )
        );

        var storyPoint = new Object();
        storyPoint.estimate50pct = "";
        storyPoint.estimate90pct = "";
        this.section.inputBlock1().appendChild(
            this.makeStoryPoint(storyPoint)
        );

        this.section.inputBlock1().appendChild(
            this.makeILinks([])
        );

        this.section.inputBlock1().appendChild(
            this.parts.operationBlock(
                function() {rStoryInstance().erase()},
                function() {rStoryInstance().send()},
            )
        );
    }

    makeStoryPoint(storyPoint) {
        var label = this.parts.label("ストーリーポイント");

        var div1 = this.parts.div(
            "level-2",
            "",
            [this.parts.input(storyPoint.estimate50pct, "", "estimate50pct", "50%見積もり")]
        );

        var div2 = this.parts.div(
            "level-2",
            "",
            [this.parts.input(storyPoint.estimate50pct, "", "estimate90pct", "90%見積もり")]
        )

        return this.parts.div(
            "level-1",
            "storyPoint",
            [label, div1, div2]
        );
    }

    makeILinks(links) {
        var label = this.parts.label("リンク・メモリスト");
        var iDivs = links.map(link =>
            this.parts.div("i", "", [
                this.parts.minusButton(),
                this.parts.div("level-2", "", [
                    this.parts.input(link, "link", "", "リンク・メモ")])
            ])
        );
        var plusButton = this.parts.plusButton(function() {rStoryInstance().addLinkInput(this.closest('#links'), this.closest('div'))});
        return this.parts.div(
            "level-1",
            "links",
            [label].concat(iDivs).concat([plusButton])
        );
    }

    addLinkInput(linksEL, plusButtonEl) {
        var divs = [
            this.parts.minusButton(),
            this.parts.div("level-2", "", [
                this.parts.input("", "link", "", "リンク・メモ")]
            )
        ];

        var newInput = this.parts.div("i", "", divs);
        linksEL.insertBefore(newInput, plusButtonEl);
    }

    /*
     *  Input Section: POST and PUT
     */
    isPostMode() {
        return document.getElementById("storyId") == null;
    }

    getPostUrl() {
        return this.baseUrl + "stories";
    }

    getPostObjectList() {
        var storyObject = new Object();
        storyObject.storyTitle = utilInstance().valueOfInputById("storyTitle");

        var storyPointObject = new Object();
        storyPointObject.estimate50pct = utilInstance().valueOfInputById("estimate50pct");
        storyPointObject.estimate90pct = utilInstance().valueOfInputById("estimate90pct");

        storyObject.storyPoint = storyPointObject;

        storyObject.links = this.makeLinks();
        return [storyObject];
    }

    getPutObject() {
        var storyObject = new Object();
        storyObject.storyId = utilInstance().valueOfInputById("storyId");
        storyObject.storyTitle = utilInstance().valueOfInputById("storyTitle");

        var storyPointObject = new Object();
        storyPointObject.estimate50pct = utilInstance().valueOfInputById("estimate50pct");
        storyPointObject.estimate90pct = utilInstance().valueOfInputById("estimate90pct");

        storyObject.storyPoint = storyPointObject;

        storyObject.links = this.makeLinks();
        return storyObject;
    }

    getPutUrl() {
        return this.baseUrl + "stories/" + utilInstance().valueOfInputById("storyId");
    }

    makeLinks() {
        var links = new Array();

        var linksDiv = document.getElementById("links");
        Array.from(linksDiv.querySelectorAll(':scope > .i')).forEach(i => {
            var link = utilInstance().valueOfInputByClass(i, "link");
            links.push(link);
        });
        return links;
    }

    /*
     *  Input Section: DELETE
     */
    async deleteStory(mainEntry) {
        var storyId = mainEntry.getElementsByClassName("storyId")[0].innerText;
        var url = this.baseUrl + "stories/" + storyId;
        await this.webApi.doDelete(url);
        this.refreshList();
    }
};
