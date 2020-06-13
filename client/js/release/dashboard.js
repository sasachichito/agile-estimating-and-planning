import release from "../abstract/release.js";

export default class extends release {

    constructor(aWebApi, aSection, aParts) {
        super(aWebApi, aSection, aParts);
    }

    async initContent() {
        this.section.makeSearchResultFrame();
        await this.createSearchSection();
        this.viewChart();
    }

    async createSearchSection() {
        var planId = await this.firstPlanIdIfExist();
        this.section.searchBlock1().appendChild(
            this.parts.div(
                "",
                "",
                [this.parts.input(planId, "", "planId", "プランID")]
            )
        );

        this.section.searchBlock1().appendChild(this.chartButton());
        this.section.searchBlock1().appendChild(this.milestoneButton());
    }

    async firstPlanIdIfExist() {
        var url = this.baseUrl + "plans";
        var json = await this.webApi.get(url);
        var plan = JSON.parse(json)[0];
        if (plan == null) {
            return "";
        }
        return plan.planId;
    }

    chartButton() {
        var div = document.createElement('div');
        div.className = "chart";
        div.style.cssText = "text-align: center;";

        var i = document.createElement('i');
        i.className = "fas fa-chart-line fa-2x";
        i.style.cssText = "display: block;";
        i.onclick = function() {rDashboardInstance().viewChart()};

        var a = document.createElement('a');
        a.innerText = "バーンダウンチャート";
        a.style.cssText = "display: block;";

        div.appendChild(i);
        div.appendChild(a);

        return div;
    }

    milestoneButton() {
        var div = document.createElement('div');
        div.className = "milestone";
        div.style.cssText = "text-align: center;";

        var i = document.createElement('i');
        i.className = "fas fa-list fa-2x";
        i.style.cssText = "display: block;";
        i.onclick = function() {rDashboardInstance().viewMilestone()};

        var a = document.createElement('a');
        a.innerText = "マイルストーンリスト";
        a.style.cssText = "display: block;";

        div.appendChild(i);
        div.appendChild(a);

        return div;
    }

    async viewChart() {
        this.section.clearResultBlock1();

        var canvas = document.createElement('canvas');
        canvas.id = "iteration-dashboard-line-chart";
        this.section.resultBlock1().appendChild(canvas);

        var planId = utilInstance().valueOfInputById("planId");
        var url = this.baseUrl + "charts/burndown/line/" + planId;
        var json = await this.webApi.get(url);
        this.buildChart(json);
    }

    buildChart(jsonText) {
	    console.log(jsonText);
	    var jsonObj = JSON.parse(jsonText);

        var myLineChart = new Chart(document.getElementById("iteration-dashboard-line-chart"), {
            type: 'line',
            data: {
              labels: jsonObj.period,
              datasets: [
                {
                  label: 'Initial Plan',
                  lineTension: 0,
                  borderDash: [8, 5],
                  data: jsonObj.initialPlan,
                  borderColor: "rgba(0,0,255,1)",
                  backgroundColor: "rgba(0,0,0,0)"
                },
                {
                  label: 'Changed Plan',
                  lineTension: 0,
                  data: jsonObj.changedPlan,
                  borderColor: "rgba(0,255,0,1)",
                  backgroundColor: "rgba(0,0,0,0)"
                },
                {
                  label: 'Actual Result',
                  lineTension: 0,
                  data: jsonObj.actualResult,
                  borderColor: "rgba(255,0,0,1)",
                  backgroundColor: "rgba(0,0,0,0)"
                }
              ],
            },
            options: {
              title: {
                display: true,
                text: 'Burndown Line Chart'
              },
              scales: {
                yAxes: [{
                  ticks: {
                    suggestedMin: 0,
                    stepSize: 5,
                    callback: function(value, index, values){
                      return  value +  'h'
                    }
                  }
                }]
              },
            }
        });
    }

    async viewMilestone() {
        this.section.clearResultBlock1();

        var planId = utilInstance().valueOfInputById("planId");
    	var url = this.baseUrl + "plans/" + planId + "/milestone-list/";
    	var json = await this.webApi.get(url);
    	var jsonObj = JSON.parse(json);

        var milestoneListData = jsonObj.milestoneList;

        var burnsJson = await this.webApi.get(this.baseUrl + "burns");
        var burns = JSON.parse(burnsJson);

        milestoneListData.forEach(story => {
            var mainEntry = this.createMainEntry(story, burns);
            this.section.resultBlock1().appendChild(mainEntry);
        });
    }

    createMainEntry(story, burns) {
        var mainHeader = this.parts.tableHeader(
            ["ストーリーID", "ストーリータイトル", "マイルストーン"]
        );

        var isBurned = burns.some(burn => {
            return burn.storyId == story.storyId;
        });

        var button;
        if(isBurned) {
            button = this.makeExtinguishButton();
        } else {
            button = this.makeBurnButton();
        }

        var td1 = this.parts.td(story.storyId, "storyId", "", []);
        var td2 = this.parts.td(story.storyTitle, "storyTitle", "", []);
        var td3 = this.parts.td(story.milestone, "storyMilestone", "", []);
        var td4 = this.parts.td("", "button", "", [button]);

        var mainRecode = this.parts.tableRecodeFromTds([td1, td2, td3, td4]);

        var mainTable = this.parts.table(
            this.parts.tableHead([mainHeader]),
            this.parts.tableBody([mainRecode])
        );

        return this.parts.mainEntry(
            [mainTable]
        );
    }

    makeBurnButton() {
        var button = document.createElement('i');
        button.className = "fas fa-dumpster-fire";
        button.onclick = function() {rDashboardInstance().burn(this.closest('tr'))};
        button.style.cssText = "color: #FFC107;" + "font-size: 2rem;" + "vertical-align: middle;";
        return button;
    }

    makeExtinguishButton() {
        var button = document.createElement('i');
        button.className = "fas fa-fire-extinguisher";
        button.onclick = function() {rDashboardInstance().extinguish(this.closest('tr'))};
        button.style.cssText = "color: #37474F;" + "font-size: 2rem;" + "vertical-align: middle;";
        return button;
    }

    buildMilestone(jsonText) {
        console.log(jsonText);
        var jsonObj = JSON.parse(jsonText);

        var milestoneList = document.getElementById('milestone-list');
        while (milestoneList.firstChild) {
            milestoneList.removeChild(milestoneList.firstChild);
        }

        var milestoneListData = jsonObj.milestoneList;
        milestoneListData.forEach(story => {
            var storyEntry = document.createElement("li");
            storyEntry.style.cssText = "margin: 1rem 0;";
            storyEntry.textContent = "storyId:" + story.storyId + " " + story.storyTitle + " " + story.milestone;

            milestoneList.appendChild(storyEntry);
        });
    }

    async extinguish(storyTr) {
        var storyId = utilInstance().firstElByClass(storyTr, "storyId").innerText
        var burnsJson = await this.webApi.get(this.baseUrl + "burns");
        var burns = JSON.parse(burnsJson);

        var burn = burns.find(burn => burn.storyId == storyId);
        if (burn == null) {
            return;
        }

        await this.webApi.doDelete(this.baseUrl + "burns/" + burn.burnId);

        var oldButton = utilInstance().firstElByClass(storyTr, "button");
        storyTr.replaceChild(
            this.parts.td("", "button", "", [this.makeBurnButton()]),
            oldButton
        );
    }

    async burn(storyTr) {
        var object = new Object();
        object.storyId = utilInstance().firstElByClass(storyTr, "storyId").innerText;
        await this.webApi.post(this.baseUrl + "burns/", [object]);

        var oldButton = utilInstance().firstElByClass(storyTr, "button");
        storyTr.replaceChild(
            this.parts.td("", "button", "", [this.makeExtinguishButton()]),
            oldButton
        );
    }
};
