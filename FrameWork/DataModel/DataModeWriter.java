package DataModel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by renim on 19/07/2017.
 * Writes the provided data to a JSON WAVE file
 * Assumes correct input for now
 */
public class DataModeWriter {

    private File dataFile;
    private DMDetails details;
    private DMMetadata metadata;
    private ArrayList<DMUIFile> uiFiles;
    private ArrayList<DMCodeBehindFile> cbFiles;
    private ArrayList<DMTestFile> testFiles;
    private ArrayList<DMRemediationCodeFile>remFiles;
    private DMCTFCategory category;
    private DMCTFChallenge challenge;
    private ArrayList<DMCTFHint> hints;


    public DataModeWriter(String dataFile) {
        this.dataFile = new File(dataFile);
    }

    public void write() {
        JSONObject jsonOut = new JSONObject();

        jsonOut.put("details", writeDetails());
        jsonOut.put("metadata", metadata.getMetadata());
        jsonOut.put("UI", writeUIFiles());
        jsonOut.put("code-behind", writeCBFiles());
        jsonOut.put("test", writeTestFiles());
        jsonOut.put("remediation_code", writeRemFiles());
        jsonOut.put("ctf", writeCTFData());

        try (FileWriter writer = new FileWriter(dataFile)) {
            jsonOut.writeJSONString(writer);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private JSONObject writeDetails() {
        JSONObject jsonDetails = new JSONObject();
        jsonDetails.put("description", details.getDescription());
        jsonDetails.put("technology", details.getTechnology());
        JSONArray attackHints = new JSONArray();
        attackHints.addAll(details.getAttack_hints());
        jsonDetails.put("attack_hints", attackHints);
        JSONArray remHints = new JSONArray();
        remHints.addAll(details.getRemediation_hints());
        jsonDetails.put("remediation_hints", remHints);
        jsonDetails.put("notes", details.getNotes());
        return jsonDetails;
    }

    private JSONArray writeUIFiles() {
        JSONArray jsonUIFiles = new JSONArray();
        for (DMUIFile uiFile : uiFiles) {
            JSONObject jsonUIFile = new JSONObject();
            jsonUIFile.put("technology", uiFile.getTechnology());
            jsonUIFile.put("file", uiFile.getFile().getName());
            jsonUIFiles.add(jsonUIFile);
        }
        return jsonUIFiles;
    }

    private JSONArray writeCBFiles() {
        JSONArray jsonCBFiles = new JSONArray();
        for (DMCodeBehindFile cbFile : cbFiles) {
            JSONObject jsonCBFile = new JSONObject();
            jsonCBFile.put("client-server", cbFile.getClient_server());
            jsonCBFile.put("file", cbFile.getFile().getName());
            jsonCBFiles.add(jsonCBFile);
        }
        return jsonCBFiles;
    }

    private JSONArray writeTestFiles() {
        JSONArray jsonTestFiles = new JSONArray();
        for (DMTestFile testFile : testFiles) {
            JSONObject jsonTestFile = new JSONObject();
            jsonTestFile.put("technology", testFile.getTechnology());
            jsonTestFile.put("file", testFile.getFile().getName());
            jsonTestFile.put("url", testFile.getBaseURL().toExternalForm());
            jsonTestFile.put("start-path", testFile.getStartPath());
            jsonTestFiles.add(jsonTestFile);
        }
        return jsonTestFiles;
    }

    private JSONArray writeRemFiles() {
        JSONArray jsonRemFiles = new JSONArray();
        for (DMRemediationCodeFile remFile : remFiles) {
            JSONObject jsonRemFile = new JSONObject();
            jsonRemFile.put("technology", remFile.getTechnology());
            jsonRemFile.put("client-server", remFile.getClient_server());
            jsonRemFile.put("file", remFile.getFile().getName());
            jsonRemFiles.add(jsonRemFile);
        }
        return jsonRemFiles;
    }

    private JSONObject writeCTFData() {
        JSONObject jsonCTF = new JSONObject();
        jsonCTF.put("category", writeCategory());
        jsonCTF.put("challenge", writeChallenge());
        jsonCTF.put("hints", writeHints());
        return jsonCTF;
    }

    private JSONObject writeCategory() {
        JSONObject jsonCategory = new JSONObject();
        jsonCategory.put("title", category.getTitle());
        jsonCategory.put("description", category.getDescription());
        jsonCategory.put("exposed", category.isExposed());
        jsonCategory.put("available-from", category.getAvailableFrom().getTime());
        jsonCategory.put("available-until", category.getAvailableUntil().getTime());
        return jsonCategory;
    }

    private JSONObject writeChallenge() {
        JSONObject jsonChallenge = new JSONObject();
        jsonChallenge.put("title", challenge.getTitle());
        jsonChallenge.put("description", challenge.getDescription());
        jsonChallenge.put("exposed", challenge.isExposed());
        jsonChallenge.put("available-from", challenge.getAvailableFrom().getTime());
        jsonChallenge.put("available-until", challenge.getAvailableUntil().getTime());
        jsonChallenge.put("flag", challenge.getFlag());
        jsonChallenge.put("case-insensitive", challenge.isCaseInsensitive());
        jsonChallenge.put("automark", challenge.isAutomark());
        jsonChallenge.put("points", challenge.getPoints());
        jsonChallenge.put("attempts", challenge.getAttempts());
        jsonChallenge.put("time-between-attempts", challenge.getTimeBetweenAttepmts());
        jsonChallenge.put("must-complete-after", challenge.getMustCompleteAfter());
        return jsonChallenge;
    }

    private JSONArray writeHints() {
        JSONArray jsonCTFHints = new JSONArray();
        for (DMCTFHint hint : hints) {
            JSONObject jsonCTFHint = new JSONObject();
            jsonCTFHint.put("body", hint.getBody());
            jsonCTFHint.put("visible", hint.isVisible());
            jsonCTFHints.add(jsonCTFHint);
        }
        return jsonCTFHints;
    }





    public void setDataFile(File dataFile) {
        this.dataFile = dataFile;
    }

    public void setDetails(DMDetails details) {
        this.details = details;
    }

    public void setMetadata(DMMetadata metadata) {
        this.metadata = metadata;
    }

    public void setUiFiles(ArrayList<DMUIFile> uiFiles) {
        this.uiFiles = uiFiles;
    }

    public void setCbFiles(ArrayList<DMCodeBehindFile> cbFiles) {
        this.cbFiles = cbFiles;
    }

    public void setTestFiles(ArrayList<DMTestFile> testFiles) {
        this.testFiles = testFiles;
    }

    public void setRemFiles(ArrayList<DMRemediationCodeFile> remFiles) {
        this.remFiles = remFiles;
    }

    public void setCategory(DMCTFCategory category) {
        this.category = category;
    }

    public void setChallenge(DMCTFChallenge challenge) {
        this.challenge = challenge;
    }

    public void setHints(ArrayList<DMCTFHint> hints) {
        this.hints = hints;
    }
}
