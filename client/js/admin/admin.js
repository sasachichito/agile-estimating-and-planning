import config from "../common/config.js";

export default class {

    constructor(aWebApi) {
        this.webApi = aWebApi;
        this.releaseExportURL = config.releaseBaseURL + "admin/export";
        this.iterationExportURL = config.iterationBaseURL + "admin/export";

        this.releaseImportURL = config.releaseBaseURL + "admin/import";
        this.iterationImportURL = config.iterationBaseURL + "admin/import";
    }

    async fileExport() {
        var object = new Object();
        var releaseJson = await this.webApi.get(this.releaseExportURL);
        var iterationJson = await this.webApi.get(this.iterationExportURL);

        object.release = JSON.parse(releaseJson);
        object.iteration = JSON.parse(iterationJson);

        var content = JSON.stringify(object, null, 4);
        var blob = new Blob([ content ], { "type" : "text/plain" })
        var url = URL.createObjectURL(blob);
        document.getElementById("export").href = url;

        var now = new Date();
        var year = now.getFullYear();
        var month = ("0" + (now.getMonth() + 1)).slice(-2);
        var date = ("0" + now.getDate()).slice(-2);
        var hour = ("0" + now.getHours()).slice(-2);
        var min = ("0" + now.getMinutes()).slice(-2);
        document.getElementById("export").download = "plan_" + year + month + date + hour + min + ".json";

        document.getElementById("export").click();
        document.getElementById("export").href = "javascript:adminInstance().fileExport()";
    }

    async fileImport() {
        var file = document.getElementById("getFile");
        var fileList = file.files;

        var reader = new FileReader();

        reader.readAsText(fileList[0]);

        reader.onload = function() {adminInstance().import(JSON.parse(reader.result))};
    }

    async import(object) {
        await this.webApi.put(this.releaseImportURL, object.release);
        await this.webApi.put(this.iterationImportURL, object.iteration);
        document.getElementById("getFile").value = "";
        releaseActive();
    }
}