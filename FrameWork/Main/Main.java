package Main;

import DataModel.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.OutputDocument;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;

import net.htmlparser.jericho.Source;


/**
 * Created by renim on 14/07/2017.
 */
public class Main {

    private static String input;
    private static String inputDir;
    private static String output;
    private static String dest;
    private static String webDest;
    private static DataModelReader dataModelReader;

    /**
     * contains the files for the skeleton website
     */
    private static String skeleton = "sampleSkeleton";

    /**
     * the name of the base/welcome file, which will hold links to the vulnerable modules
     */
    private static String base = "index.jsp";


    public static void main(String [] args) {

        // read arguments
        int i = 0;
        while (i < args.length) {
            switch (args[i]) {
                case "-i":
                case "-input":
                case "-in":
                    input = args[++i];
                    break;
                case "-o":
                case "-output":
                case "-out":
                    output = args[++i];
                    break;
                case "-d":
                case "-dest":
                case "-destination":
                    dest = args[++i];
                    break;
            }
            i++;
        }

        // if no arguments are provided use sample data
        if (input == null) {
            input = "repo/WAVE/sampleData/XSS-sample.json";
        }
        if (output == null) {
            output = "repo/WAVE/sampleData/XSS-sample-DMWout.json";
        }
        if (dest == null) {
            dest = "sampleOut/";
        }

        inputDir = input.substring(0,input.lastIndexOf('/'));

        // clear destination directory
        File destination = new File(dest);

        webDest = dest + "/src/main/webapp";

        File webDestination = new File(webDest);
        clearDest(webDestination);

        // copy skeleton into destination folder
        copySkeleton(webDestination);

        // read data model input
        dataModelRead();

        // extract files
        extractUIFiles();
        extractCBFiles();
        extractConfigFiles();

        // add link to home page
        addVulnLink(webDestination);

        // generate sql insert for mellivora CTF
        generateSQLInserts();


    }

    private static void clearDest(File destination) {
        try {
            FileUtils.cleanDirectory(destination);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to clear destination directory: " + dest);
        }
    }

    private static void copySkeleton(File destination) {
        try {
            FileUtils.copyDirectory(new File(skeleton), destination);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to copy skeleton files");

        }
    }

    private static void dataModelRead() {
        dataModelReader = new DataModelReader(input);
        dataModelReader.read();
    }

    private static void extractUIFiles() {
        ArrayList<DMUIFile> uiFiles = dataModelReader.getUiFiles();
        for (DMUIFile uiFile : uiFiles) {
            // files can be extracted in different folders based on technology
            try {
                ZipFile zipFile = new ZipFile(inputDir + File.separator + uiFile.getFile().toString());
                zipFile.extractAll(webDest);
            } catch (ZipException e) {
                e.printStackTrace();
                System.out.println("Unable to extract UI files: " + uiFile.getFile());
            }
        }
    }

    private static void extractCBFiles() {
        ArrayList<DMCodeBehindFile> cbFiles = dataModelReader.getCbFiles();
        for (DMCodeBehindFile cbFile : cbFiles) {
            // files can be extracted in different folders based on technology
            try {
                ZipFile zipFile = new ZipFile(inputDir + File.separator + cbFile.getFile().toString());
                zipFile.extractAll(webDest);
            } catch (ZipException e) {
                e.printStackTrace();
                System.out.println("Unable to extract CB files: " + cbFile.getFile());
            }
        }
    }

    private static void extractConfigFiles() {
        try {
            ZipFile zipFile = new ZipFile(inputDir + File.separator + dataModelReader.getConfig().getFiles());
            zipFile.extractAll(webDest);
        } catch (ZipException e) {
            e.printStackTrace();
            System.out.println("Unable to extract config files: " + dataModelReader.getConfig().getFiles());
        }
    }

    private static void addVulnLink(File destination) {
        File htmlFile = new File(destination + File.separator + base);

        try {
            Source HTMLSource = new Source(htmlFile);

            OutputDocument outputDocument = new OutputDocument(HTMLSource);
            Element sidebarLinks = HTMLSource.getElementById("sidebarLinksStart");
            String link = "\n\t\t\t<a href=\"" + dataModelReader.getConfig().getVulnURL()
                    + "\">" + dataModelReader.getConfig().getVulnLink() + "</a>";
            outputDocument.insert(sidebarLinks.getEnd(), link);

            Writer htmlWriter = new FileWriter(htmlFile);
            outputDocument.writeTo(htmlWriter);

            htmlWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to modify html welcome file: " + htmlFile.toString());
        }
    }


    private static void generateSQLInserts() {
        long now = new Date().getTime() / 1000;
        DMCTFCategory category = dataModelReader.getCategory();

        // write SQL to file
        try(  PrintWriter sql = new PrintWriter(dest + "sqlInsertMellivora.txt")  ){
            sql.print("BEGIN;\n");
            sql.print(generateSQLCategory(now, category));
            sql.print(generateSQLChallenge(now, category));
            sql.print(generateSQLHints(now));
            sql.print("COMMIT;\n");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static String generateSQLCategory(long now, DMCTFCategory category) {
        return "INSERT IGNORE INTO categories (added, added_by, title, description, exposed, " +
                "available_from, available_until) " + "VALUES (" + now + ", 1, \'" +
                StringEscapeUtils.escapeSql(category.getTitle()) + "\', \'" +
                StringEscapeUtils.escapeSql(category.getDescription()) + "\', " + (category.isExposed() ? 1 : 0) +
                ", " + category.getAvailableFrom().getTime() + ", " + category.getAvailableUntil().getTime() + ");\n";
    }

    private static String generateSQLChallenge(long now, DMCTFCategory category) {
        DMCTFChallenge challenge = dataModelReader.getChallenge();
        return "\nINSERT INTO challenges (added, added_by, title, category, description, " +
                "exposed, available_from, available_until, flag, points) VALUES (" + now + ", 1, \'" +
                StringEscapeUtils.escapeSql(challenge.getTitle()) + "\', (SELECT id FROM categories WHERE title =\'" +
                StringEscapeUtils.escapeSql(category.getTitle()) + "\'), \'" +
                StringEscapeUtils.escapeSql(challenge.getDescription()) + "\', " + (challenge.isExposed() ? 1 : 0) +
                ", " + challenge.getAvailableFrom().getTime() + ", " + challenge.getAvailableUntil().getTime() +
                ", \'" + StringEscapeUtils.escapeSql(challenge.getFlag()) + "\', " + challenge.getPoints() + ");\n";
    }

    private static String generateSQLHints(long now) {
        StringBuilder sqlHints = new StringBuilder();
        for (DMCTFHint hint : dataModelReader.getHints()) {
            sqlHints.append("\nINSERT INTO hints (challenge, added, added_by, visible, body) " +
                    "VALUES (");
            sqlHints.append("(SELECT MAX(id) FROM challenges), ");
            sqlHints.append(now);
            sqlHints.append(", 1, ");
            sqlHints.append((hint.isVisible() ? 1 : 0));
            sqlHints.append(", \'");
            sqlHints.append(StringEscapeUtils.escapeSql(hint.getBody()));
            sqlHints.append("\');\n");
        }
        return sqlHints.toString();
    }



    private static void dataModelWrite() {
        DataModeWriter dataModeWriter = new DataModeWriter(output);
        dataModeWriter.setDetails(dataModelReader.getDetails());
        dataModeWriter.setConfig(dataModelReader.getConfig());
        dataModeWriter.setMetadata(dataModelReader.getMetadata());
        dataModeWriter.setUiFiles(dataModelReader.getUiFiles());
        dataModeWriter.setCbFiles(dataModelReader.getCbFiles());
        dataModeWriter.setTestFiles(dataModelReader.getTestFiles());
        dataModeWriter.setRemFiles(dataModelReader.getRemFiles());
        dataModeWriter.setCategory(dataModelReader.getCategory());
        dataModeWriter.setChallenge(dataModelReader.getChallenge());
        dataModeWriter.setHints(dataModelReader.getHints());

        dataModeWriter.write();
    }
}
