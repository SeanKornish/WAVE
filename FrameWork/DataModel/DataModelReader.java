package DataModel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by renim on 13/07/2017.
 * Reads the JSON WAVE files to extract the vulnerability data
 * Assumes correct input for now
 */
public class DataModelReader {

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


    public DataModelReader(String dataFile) {
        this.dataFile = new File(dataFile);
        uiFiles = new ArrayList<>();
        cbFiles = new ArrayList<>();
        testFiles = new ArrayList<>();
        remFiles = new ArrayList<>();
        hints = new ArrayList<>();

    }

    public void read() {
        JSONParser parser = new JSONParser();

        try {
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(dataFile));
            readDetails(jsonObject);
            metadata = new DMMetadata(jsonObject.get("metadata").toString());

            JSONArray jsonUIFiles = (JSONArray) jsonObject.get("UI");
            for (Object jsonUIFile : jsonUIFiles ) {
                readUIFile((JSONObject) jsonUIFile);
            }

            JSONArray jsonCBFiles = (JSONArray) jsonObject.get("code-behind");
            for (Object jsonCBFile : jsonCBFiles ) {
                readCBFile((JSONObject) jsonCBFile);
            }

            JSONArray jsonTestFiles = (JSONArray) jsonObject.get("test");
            for (Object jsonTestFile : jsonTestFiles ) {
                readTestFile((JSONObject) jsonTestFile);
            }

            JSONArray jsonRemFiles = (JSONArray) jsonObject.get("remediation_code");
            for (Object jsonRemFile : jsonRemFiles ) {
                readRemFile((JSONObject) jsonRemFile);
            }
            JSONObject jsonCTF = (JSONObject) jsonObject.get("ctf");
            readCTFCat(jsonCTF);
            readCTFChallenge(jsonCTF);
            JSONArray jsonCTFHints = (JSONArray) jsonCTF.get("hints");
            for (Object jsonCTFHint : jsonCTFHints ) {
                readCTFHint((JSONObject) jsonCTFHint);
            }


        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

    }

    private void readDetails(JSONObject jsonObject) {
        JSONObject jsonDetails = (JSONObject) jsonObject.get("details");
        details = new DMDetails();
        details.setDescription(jsonDetails.get("description").toString());
        details.setTechnology(jsonDetails.get("technology").toString());

        details.setAttack_hints((JSONArray) jsonDetails.get("attack_hints"));
        details.setRemediation_hints((JSONArray) jsonDetails.get("remediation_hints"));

        details.setNotes(jsonDetails.get("notes").toString());
    }

    private void readUIFile(JSONObject jsonUIFile) {
        DMUIFile uiFile = new DMUIFile();
        uiFile.setFile(new File(jsonUIFile.get("file").toString()));
        uiFile.setTechnology(jsonUIFile.get("technology").toString());
        uiFiles.add(uiFile);
    }

    private void readCBFile(JSONObject jsonCBFile) {
        DMCodeBehindFile cbFile = new DMCodeBehindFile();
        cbFile.setFile(new File(jsonCBFile.get("file").toString()));
        cbFile.setClient_server(jsonCBFile.get("client-server").toString());
        cbFiles.add(cbFile);
    }

    private void readTestFile(JSONObject jsonTestFile) {
        DMTestFile testFile = new DMTestFile();
        testFile.setFile(new File(jsonTestFile.get("file").toString()));
        testFile.setTechnology(jsonTestFile.get("technology").toString());
        testFile.setBaseURL(jsonTestFile.get("url").toString());
        testFiles.add(testFile);
    }

    private void readRemFile(JSONObject jsonRemFile) {
        DMRemediationCodeFile remFile = new DMRemediationCodeFile();
        remFile.setFile(new File(jsonRemFile.get("file").toString()));
        remFile.setTechnology(jsonRemFile.get("technology").toString());
        remFile.setClient_server(jsonRemFile.get("client-server").toString());
        remFiles.add(remFile);

    }

    private void readCTFCat(JSONObject jsonCTF) {
        JSONObject jsonCTFCat = (JSONObject) jsonCTF.get("category");
        category = new DMCTFCategory();
        category.setTitle(jsonCTFCat.get("title").toString());
        category.setDescription(jsonCTFCat.get("description").toString());
        category.setExposed((boolean) jsonCTFCat.get("exposed"));
        category.setAvailableFrom(new Date((long) jsonCTFCat.get("available-from")));
        category.setAvailableUntil(new Date((long) jsonCTFCat.get("available-until")));
    }


    private void readCTFChallenge(JSONObject jsonCTF) {
        JSONObject jsonCTFChallenge = (JSONObject) jsonCTF.get("challenge");
        challenge = new DMCTFChallenge();
        challenge.setTitle(jsonCTFChallenge.get("title").toString());
        challenge.setDescription(jsonCTFChallenge.get("description").toString());
        challenge.setExposed((boolean) jsonCTFChallenge.get("exposed"));
        challenge.setAvailableFrom(new Date((long) jsonCTFChallenge.get("available-from")));
        challenge.setAvailableUntil(new Date((long) jsonCTFChallenge.get("available-until")));
        challenge.setFlag(jsonCTFChallenge.get("flag").toString());
        challenge.setCaseInsensitive((boolean) jsonCTFChallenge.get("case-insensitive"));
        challenge.setAutomark((boolean) jsonCTFChallenge.get("automark"));
        challenge.setPoints(((Long) jsonCTFChallenge.get("points")).intValue());
        challenge.setAttempts(((Long) jsonCTFChallenge.get("attempts")).intValue());
        challenge.setTimeBetweenAttepmts((long) jsonCTFChallenge.get("time-between-attempts"));
        challenge.setMustCompleteAfter(jsonCTFChallenge.get("must-complete-after").toString());
    }

    private void readCTFHint(JSONObject jsonCTFHint) {
        DMCTFHint hint = new DMCTFHint();
        hint.setBody(jsonCTFHint.get("body").toString());
        hint.setVisible((boolean) jsonCTFHint.get("visible"));
        hint.setChallenge(challenge);
        hints.add(hint);
    }
}
