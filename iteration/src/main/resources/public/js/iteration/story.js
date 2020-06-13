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
        var mainTable = this.createMainTable(story.storyId, story.storyTitle);

        var subEntryLv1s = this.createSubEntryLv1(story.taskList);

        return this.parts.mainEntry(
            [mainTable].concat(subEntryLv1s)
        );
    }

    createMainTable(storyId, storyTitle) {
        var header = this.parts.tableHeader(
            ["", "ストーリーID", "ストーリータイトル"]
        );

        var edit = this.parts.edit(function() {iStoryInstance().editStory(this.closest('.main-entry'))});
        var trash = this.parts.trash(function() {iStoryInstance().deleteStory(this.closest('.main-entry'))});

        var td1 = this.parts.td("", "", "", [edit, trash]);
        var td2 = this.parts.td(storyId, "storyId", "", []);
        var td3 = this.parts.td(storyTitle, "storyTitle", "", []);

        var recode = this.parts.tableRecodeFromTds([td1, td2, td3]);

        return this.parts.table(
            this.parts.tableHead([header]),
            this.parts.tableBody([recode])
        );
    }

    createSubEntryLv1(taskList) {
        var header = this.parts.tableHeader(
            ["タスクID", "タスク名", "50%見積もり", "90%見積もり"]
        );

        var recodes = taskList.map(task =>
            this.parts.tableRecode(
                [task.taskId, task.taskName, task.estimate50Pct, task.estimate90Pct]
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
            this.makeITaskList(story.taskList));

        this.section.inputBlock1().appendChild(
            this.parts.operationBlock(
                function() {iStoryInstance().erase()},
                function() {iStoryInstance().send()},
            )
        );
    }

    makePostInput() {
        var task = new Object;
        task.taskId = "";
        task.taskName = "";
        task.estimate50Pct = "";
        task.estimate90Pct = "";

        var taskList = new Array();
        taskList.push(task);

        this.section.inputBlock1().appendChild(
            this.parts.div(
                "level-1",
                "",
                [this.parts.input("", "", "storyTitle", "ストーリータイトル")]
            )
        );

        this.section.inputBlock1().appendChild(
            this.makeITaskListNoId(taskList)
        );

        this.section.inputBlock1().appendChild(
            this.parts.operationBlock(
                function() {iStoryInstance().erase()},
                function() {iStoryInstance().send()},
            )
        );
    }

    makeITaskList(taskList) {
        var label = this.parts.label("タスクリスト");
        var iDivs = taskList.map(task =>
            this.parts.div("i", "", [
                this.parts.minusButton(),
                this.parts.div("level-2", "", [
                    this.parts.input(task.taskId, "taskId", "", "タスクID")]
                ),
                this.parts.div("level-2", "", [
                    this.parts.input(task.taskName, "taskName", "", "タスク名")]
                ),
                this.parts.div("level-2", "", [
                    this.parts.input(task.estimate50Pct, "estimate50Pct", "", "50%見積もり")]
                ),
                this.parts.div("level-2", "", [
                    this.parts.input(task.estimate90Pct, "estimate90Pct", "", "90%見積もり")])
            ])
        );
        var plusButton = this.parts.plusButton(function() {iStoryInstance().addTaskInput(this.closest('#taskList'), this.closest('div'))});
        return this.parts.div(
            "level-1",
            "taskList",
            [label].concat(iDivs).concat([plusButton])
        );
    }

    makeITaskListNoId(taskList) {
        var label = this.parts.label("タスクリスト");
        var iDivs = taskList.map(task =>
            this.parts.div("i", "", [
                this.parts.minusButton(),
                this.parts.div("level-2", "", [
                    this.parts.input(task.taskName, "taskName", "", "タスク名")]
                ),
                this.parts.div("level-2", "", [
                    this.parts.input(task.estimate50Pct, "estimate50Pct", "", "50%見積もり")]
                ),
                this.parts.div("level-2", "", [
                    this.parts.input(task.estimate90Pct, "estimate90Pct", "", "90%見積もり")])
            ])
        );
        var plusButton = this.parts.plusButton(function() {iStoryInstance().addTaskInput(this.closest('#taskList'), this.closest('div'))});
        return this.parts.div(
            "level-1",
            "taskList",
            [label].concat(iDivs).concat([plusButton])
        );
    }

    addTaskInput(taskListEL, plusButtonEl) {
        var divs = [
            this.parts.minusButton(),
//            this.parts.div("level-2", "", [
//                this.parts.input("", "taskId", "", "タスクID")]
//            ),
            this.parts.div("level-2", "", [
                this.parts.input("", "taskName", "", "タスク名")]
            ),
            this.parts.div("level-2", "", [
                this.parts.input("", "estimate50Pct", "", "50%見積もり")]
            ),
            this.parts.div("level-2", "", [
                this.parts.input("", "estimate90Pct", "", "90%見積もり")])
        ];

//        if (this.isPostMode()) {
//            divs.splice(1, 1);
//        }
        var newInput = this.parts.div("i", "", divs);
        taskListEL.insertBefore(newInput, plusButtonEl);
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
        storyObject.taskList = this.makeTaskListNoId();
        return [storyObject];
    }

    getPutObject() {
        var storyObject = new Object();
        storyObject.storyId = utilInstance().valueOfInputById("storyId");
        storyObject.storyTitle = utilInstance().valueOfInputById("storyTitle");
        storyObject.taskList = this.makeTaskList();
        return storyObject;
    }

    getPutUrl() {
        return this.baseUrl + "stories/" + utilInstance().valueOfInputById("storyId");
    }

    makeTaskList() {
        var taskList = new Array();

        var taskListDiv = document.getElementById("taskList");
        Array.from(taskListDiv.querySelectorAll(':scope > .i')).forEach(i => {
            var task = new Object;
            task.taskId = utilInstance().valueOfInputByClass(i, "taskId");
            task.taskName = utilInstance().valueOfInputByClass(i, "taskName");
            task.estimate50Pct = utilInstance().valueOfInputByClass(i, "estimate50Pct");
            task.estimate90Pct = utilInstance().valueOfInputByClass(i, "estimate90Pct");

            taskList.push(task);
        });
        return taskList;
    }

    makeTaskListNoId() {
        var taskList = new Array();

        var taskListDiv = document.getElementById("taskList");
        Array.from(taskListDiv.querySelectorAll(':scope > .i')).forEach(i => {
            var task = new Object;
            task.taskName = utilInstance().valueOfInputByClass(i, "taskName");
            task.estimate50Pct = utilInstance().valueOfInputByClass(i, "estimate50Pct");
            task.estimate90Pct = utilInstance().valueOfInputByClass(i, "estimate90Pct");

            taskList.push(task);
        });
        return taskList;
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
