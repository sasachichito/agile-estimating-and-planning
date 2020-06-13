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
        var url = this.baseUrl + "plans";
        var json = await this.webApi.get(url);
        this.viewList(json);
    }

    viewList(json) {
        var plans = JSON.parse(json);
        console.log(plans);

        plans.forEach(plan => {
            var mainEntry = this.createMainEntry(plan);
            this.section.listBlock1().appendChild(mainEntry);
        });
    }

    createMainEntry(plan) {
        var mainTable = this.createMainTable(plan.planId, plan.planTitle, plan.scopeId, plan.resourceId, plan.periodStart, plan.periodEnd);

        return this.parts.mainEntry(
            [mainTable]
        );
    }

    createMainTable(planId, planTitle, scopeId, resourceId, periodStart, periodEnd) {
        var header = this.parts.tableHeader(
            ["", "プランID", "プランタイトル", "スコープID", "リソースID", "開始日", "終了日"]
        );

        var edit = this.parts.edit(function() {rPlanInstance().editPlan(this.closest('.main-entry'))});
        var trash = this.parts.trash(function() {rPlanInstance().deletePlan(this.closest('.main-entry'))});

        var td1 = this.parts.td("", "", "", [edit, trash]);
        var td2 = this.parts.td(planId, "planId", "", []);
        var td3 = this.parts.td(planTitle, "planTitle", "", []);
        var td4 = this.parts.td(scopeId, "scopeId", "", []);
        var td5 = this.parts.td(resourceId, "resourceId", "", []);
        var td6 = this.parts.td(periodStart, "periodStart", "", []);
        var td7 = this.parts.td(periodEnd, "periodEnd", "", []);

        var recode = this.parts.tableRecodeFromTds([td1, td2, td3, td4, td5, td6, td7]);

        return this.parts.table(
            this.parts.tableHead([header]),
            this.parts.tableBody([recode])
        );
    }

    /*
     *  Input Section: READ
     */
    editPlan(mainEntry) {
        var planId = mainEntry.getElementsByClassName("planId")[0].innerText;
        this.putMode(planId);
    }

    async postMode() {
        this.section.clearInputBlock1();
        this.makePostInput();
    }

    async putMode(planId) {
        this.section.clearInputBlock1();
        var url = this.baseUrl + "plans/" + planId;

        var json = await this.webApi.get(url);
        this.makePutInput(JSON.parse(json));
    }

    makePutInput(plan) {
        this.section.inputBlock1().appendChild(
            this.parts.div(
                "level-1",
                "",
                [this.parts.input(plan.planId, "", "planId", "プランID")]
            )
        );

        this.section.inputBlock1().appendChild(
            this.parts.div(
                "level-1",
                "",
                [this.parts.input(plan.planTitle, "", "planTitle", "プランタイトル")]
            )
        );

        this.section.inputBlock1().appendChild(
            this.parts.div(
                "level-1",
                "",
                [this.parts.input(plan.scopeId, "", "scopeId", "スコープID")]
            )
        );

        this.section.inputBlock1().appendChild(
            this.parts.div(
                "level-1",
                "",
                [this.parts.input(plan.resourceId, "", "resourceId", "リソースID")]
            )
        );

        this.section.inputBlock1().appendChild(
            this.parts.div(
                "level-1",
                "",
                [this.parts.input(plan.periodStart, "", "periodStart", "開始日", function() {this.type='date'})]
            )
        );

        this.section.inputBlock1().appendChild(
            this.parts.div(
                "level-1",
                "",
                [this.parts.input(plan.periodEnd, "", "periodEnd", "終了日", function() {this.type='date'})]
            )
        );

        this.section.inputBlock1().appendChild(
            this.parts.operationBlock(
                function() {rPlanInstance().erase()},
                function() {rPlanInstance().send()},
            )
        );
    }

    makePostInput() {
        this.section.inputBlock1().appendChild(
            this.parts.div(
                "level-1",
                "",
                [this.parts.input("", "", "planTitle", "プランタイトル")]
            )
        );

        this.section.inputBlock1().appendChild(
            this.parts.div(
                "level-1",
                "",
                [this.parts.input("", "", "scopeId", "スコープID")]
            )
        );

        this.section.inputBlock1().appendChild(
            this.parts.div(
                "level-1",
                "",
                [this.parts.input("", "", "resourceId", "リソースID")]
            )
        );

        this.section.inputBlock1().appendChild(
            this.parts.operationBlock(
                function() {rPlanInstance().erase()},
                function() {rPlanInstance().send()},
            )
        );
    }

    /*
     *  Input Section: POST and PUT
     */
    isPostMode() {
        return document.getElementById("planId") == null;
    }

    getPostObjectList() {
        var planObject = new Object();
        planObject.planTitle = utilInstance().valueOfInputById("planTitle");
        planObject.scopeId = utilInstance().valueOfInputById("scopeId");
        planObject.resourceId = utilInstance().valueOfInputById("resourceId");
        return [planObject];
    }

    getPostUrl() {
        return this.baseUrl + "plans";
    }

    getPutObject() {
        var planObject = new Object();
        planObject.planId = utilInstance().valueOfInputById("planId");
        planObject.planTitle = utilInstance().valueOfInputById("planTitle");
        planObject.scopeId = utilInstance().valueOfInputById("scopeId");
        planObject.resourceId = utilInstance().valueOfInputById("resourceId");
        planObject.periodStart = utilInstance().valueOfInputById("periodStart");
        planObject.periodEnd = utilInstance().valueOfInputById("periodEnd");
        return planObject;
    }

    getPutUrl() {
        return this.baseUrl + "plans/" + utilInstance().valueOfInputById("planId");
    }

    /*
     *  Input Section: DELETE
     */
    async deletePlan(mainEntry) {
        var planId = mainEntry.getElementsByClassName("planId")[0].innerText;
        var url = this.baseUrl + "plans/" + planId;
        await this.webApi.doDelete(url);
        this.refreshList();
    }
};
