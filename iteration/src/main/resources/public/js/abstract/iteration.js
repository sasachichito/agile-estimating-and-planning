import listInput from "./listInput.js";
import config from "../common/config.js";

export default class extends listInput {
    constructor(aWebApi, aSection, aParts) {
        super(aWebApi, aSection, aParts);

        this.baseUrl = config.iterationBaseURL;
    }
}