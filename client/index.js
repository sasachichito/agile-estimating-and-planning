function toggleOff() {
    var releaseToggle = document.getElementById("release-toggle");
    var iterationToggle = document.getElementById("iteration-toggle");
    releaseToggle.classList.remove("active");
    iterationToggle.classList.remove("active");
}

function sideMenuOff() {
    var sideMenu = document.getElementById("sidemenu");
    sideMenu.classList.remove("release");
    sideMenu.classList.remove("iteration");
}

function releaseActive() {
    sideMenuOff();
    toggleOff();

    document.getElementById("sidemenu").classList.add("release");
    document.getElementById("release-toggle").classList.add("active");

    makeReleaseNav();
    contentToRStory();
}

function iterationActive() {
    sideMenuOff();
    toggleOff();

    document.getElementById("sidemenu").classList.add("iteration");
    document.getElementById("iteration-toggle").classList.add("active");

    makeIterationNav();
    contentToStory();
}

function makeReleaseNav() {
    var navList = [
        ["STORY", "javascript:contentToRStory()", "side-story"],
        ["SCOPE", "javascript:contentToRScope()", "side-scope"],
        ["RESOURCE", "javascript:contentToRResource()", "side-resource"],
        ["PLAN", "javascript:contentToRPlan()", "side-plan"],
        ["BURN", "javascript:contentToRBurn()", "side-burn"],
        ["DASHBOARD", "javascript:contentToRDashBoard()", "side-dashboard"]
    ];
    this.buildNav(navList);
}

function makeIterationNav() {
    var navList = [
        ["STORY", "javascript:contentToStory()", "side-story"],
        ["SCOPE", "javascript:contentToScope()", "side-scope"],
        ["RESOURCE", "javascript:contentToResource()", "side-resource"],
        ["PLAN", "javascript:contentToPlan()", "side-plan"],
        ["BURN", "javascript:contentToBurn()", "side-burn"],
        ["DASHBOARD", "javascript:contentToDashBoard()", "side-dashboard"]
    ];
    this.buildNav(navList);
}

function buildNav(navList) {
    var ul = document.getElementById("side-menu-list");
    ul.textContent = null;

    navList.forEach(nav => {
        var arrow = document.createElement('div');
        arrow.className = "arrow-right";

        var right = document.createElement('div');
        right.className = "right-box";
        right.appendChild(arrow);

        var left = document.createElement('div');
        left.className = "left-box";
        left.innerText = nav[0];

        var a = document.createElement('a');
        a.href = nav[1];
        a.appendChild(left);
        a.appendChild(right);

        var li = document.createElement('li');
        li.id = nav[2];
        li.className = "nav-item";
        li.appendChild(a);

        ul.appendChild(li);
    });
}

function contentToRStory() {
    changeActiveSideMenuTo("side-story");
    rStoryInstance().initContent();
}

function contentToRScope() {
    changeActiveSideMenuTo("side-scope");
    rScopeInstance().initContent();
}

function contentToRResource() {
    changeActiveSideMenuTo("side-resource");
    rResourceInstance().initContent();
}

function contentToRPlan() {
    changeActiveSideMenuTo("side-plan");
    rPlanInstance().initContent();
}

function contentToRDashBoard() {
    changeActiveSideMenuTo("side-dashboard");
    rDashboardInstance().initContent();
}

function contentToRBurn() {
    changeActiveSideMenuTo("side-burn");
    rBurnInstance().initContent();
}

/* Iteration */
function contentToStory() {
    changeActiveSideMenuTo("side-story");
    iStoryInstance().initContent();
}

function contentToScope() {
    changeActiveSideMenuTo("side-scope");
    iScopeInstance().initContent();
}

function contentToResource() {
    changeActiveSideMenuTo("side-resource");
    iResourceInstance().initContent();
}

function contentToPlan() {
    changeActiveSideMenuTo("side-plan");
    iPlanInstance().initContent();
}

function contentToDashBoard() {
    changeActiveSideMenuTo("side-dashboard");
    iDashboardInstance().initContent();
}

function contentToBurn() {
    changeActiveSideMenuTo("side-burn");
    iBurnInstance().initContent();
}

function changeActiveSideMenuTo(menuId) {
    var activeClassName = "active";

    var list = document.getElementById("side-menu-list").children;

    Array.from(list)
      .filter(oneMenu => oneMenu.classList.contains(activeClassName))
      .forEach(activeMenu => activeMenu.classList.remove(activeClassName));

    Array.from(list)
      .filter(oneMenu => oneMenu.id == menuId)
      .forEach(oneMenu => oneMenu.classList.add(activeClassName));
}