package Main;

import DataModel.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.OutputDocument;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;

import net.htmlparser.jericho.Source;


/**
 * Created by renim on 14/07/2017.
 */
public class Main {

    private static DataModelReader dataModelReader;
    /**
     * data model entry input file
     */
    private static String input;
    /**
     * data model entry input folder
     * used to read the input code files
     */
    private static String inputDir;
    /**
     * data model output directory
     */
    private static String output;
    /**
     * the directory where the SQL insert file is generated
     */
    private static String dest;
    /**
     * the directory where the web app is generated
     */
    private static String webDest;
    /**
     * contains the files for the skeleton website
     */
    private static String skeleton = "repo/WAVE/base-example";

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

        // clear web destination directory

        System.out.print("Clearing destination directory... ");
        // due to the way maven organizes directories the web application for the war file need to be in src/main/webapp
        // inside the main maven archetype directory
        webDest = dest + "/src/main/webapp";
        File webDestination = new File(webDest);
        clearDest(webDestination);
        System.out.println("Done");

        // copy skeleton into destination folder
        System.out.print("Copying skeleton into destination folder... ");
        copySkeleton(webDestination);
        System.out.println("Done");

        // read data model input
        System.out.print("Reading JSON data... ");
        dataModelRead();
        System.out.println("Done");

        // copy remedial files into destination folder
        System.out.print("Copying remediation files into destination folder... ");
        copyRem(webDestination);
        System.out.println("Done");

        // extract files
        System.out.print("Extracting UI files into destination folder... ");
        extractUIFiles();
        System.out.println("Done");
        System.out.print("Extracting code-behind files into destination folder... ");
        extractCBFiles();
        System.out.println("Done");
        System.out.print("Extracting config files into destination folder... ");
        extractConfigFiles();
        System.out.println("Done");

        // add link to home page
        System.out.print("Adding link to landing page... ");
        addVulnLink(webDestination);
        System.out.println("Done");

        // add download link to remediation page
        System.out.print("Adding link to landing page... ");
        addRemLink(webDestination);
        System.out.println("Done");

        // generate sql insert for mellivora CTF
        System.out.print("Generating SQL INSERT statements for mellivora CTF... ");
        generateSQLInserts();
        System.out.println("Done");

    }

    /**
     * Deletes all the files in the (web) destination directory
     * @param destination
     */
    private static void clearDest(File destination) {
        try {
            FileUtils.cleanDirectory(destination);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to clear destination directory: " + dest);
        }
    }

    /**
     * copies skeleton code from the skeleton folder (provided as a field)
     * into the (web) destination folder
     * @param destination
     */
    private static void copySkeleton(File destination) {
        try {
            FileUtils.copyDirectory(new File(skeleton), destination);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to copy skeleton files");
        }
    }

    /**
     * creates a Zip file with all the remediation files and copies it to the web application destination
     * @param destination
     */
    private static void copyRem(File destination) {
        ArrayList<DMRemediationCodeFile> remFiles = dataModelReader.getRemFiles();
            try {
                ZipFile zip = new ZipFile(destination.getPath() +
                        File.separator + dataModelReader.getConfig().getVulnLink() + ".zip");
                for (DMRemediationCodeFile remFile : remFiles) {
                    zip.addFile(new File(inputDir + File.separator + remFile.getFile().getName()), new ZipParameters());
                }
            } catch (ZipException e) {
                e.printStackTrace();
                System.out.println("Unable to copy remediation code zip files");

            }

    }

    /**
     * reads the data from the input file
     */
    private static void dataModelRead() {
        dataModelReader = new DataModelReader(input);
        dataModelReader.read();
    }

    /**
     * extracts the UI zip files into the (web) destination folder
     */
    private static void extractUIFiles() {
        ArrayList<DMUIFile> uiFiles = dataModelReader.getUiFiles();
        for (DMUIFile uiFile : uiFiles) {
            // files can be extracted in different folders based on technology (HTML, CSS, JS, etc)
            try {
                ZipFile zipFile = new ZipFile(inputDir + File.separator + uiFile.getFile().toString());
                zipFile.extractAll(webDest);
            } catch (ZipException e) {
                e.printStackTrace();
                System.out.println("Unable to extract UI files: " + uiFile.getFile());
            }
        }
    }

    /**
     * extracts the code-behind zip files into the (web) destination folder
     */
    private static void extractCBFiles() {
        ArrayList<DMCodeBehindFile> cbFiles = dataModelReader.getCbFiles();
        for (DMCodeBehindFile cbFile : cbFiles) {
            // files can be extracted in different folders based on technology (client side/server side)
            try {
                ZipFile zipFile = new ZipFile(inputDir + File.separator + cbFile.getFile().toString());
                zipFile.extractAll(webDest);
            } catch (ZipException e) {
                e.printStackTrace();
                System.out.println("Unable to extract CB files: " + cbFile.getFile());
            }
        }
    }

    /**
     * extracts the config zip file into the (web) destination folder
     */
    private static void extractConfigFiles() {
        try {
            ZipFile zipFile = new ZipFile(inputDir + File.separator + dataModelReader.getConfig().getFiles());
            zipFile.extractAll(webDest);
        } catch (ZipException e) {
            e.printStackTrace();
            System.out.println("Unable to extract config files: " + dataModelReader.getConfig().getFiles());
        }
    }

    /**
     * adds a link into the landing page (index.jsp for jsp web apps),
     * which leads to the vulnerable module
     * @param destination
     */
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

    /**
     * adds a link into the landing page (index.jsp for jsp web apps),
     * which leads to the remediation code for this module
     * @param destination
     */
    private static void addRemLink(File destination) {
        File htmlFile = new File(destination + File.separator + base);

        try {
            Source HTMLSource = new Source(htmlFile);

            OutputDocument outputDocument = new OutputDocument(HTMLSource);
            Element downloadLinks = HTMLSource.getElementById("downloadStart");
            String link = "\n\t\t\t<a href=\"" + destination.getPath() + File.separator
                    + dataModelReader.getConfig().getVulnLink() + ".zip"
                    + "\">" + dataModelReader.getConfig().getVulnLink() + "</a>";
            outputDocument.insert(downloadLinks.getEnd(), link);

            Writer htmlWriter = new FileWriter(htmlFile);
            outputDocument.writeTo(htmlWriter);

            htmlWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to modify html welcome file: " + htmlFile.toString());
        }
    }


    /**
     * writes the SQL insert statements for mellivora CTF in an output file
     */
    private static void generateSQLInserts() {
        // mellivora uses MySQL which allows multiple INSERT statements only inside a transaction
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

    /**
     * generates a string containing the SQL insert statement for the mellivora CTF category
     * as specified in the data model input
     * @param now current date
     * @param category the DMCTFCategory to be inserted
     * @return SQL Insert String
     */
    private static String generateSQLCategory(long now, DMCTFCategory category) {
        // assumes that the database is configured to hold only unique category titles (for usability)
        return "INSERT IGNORE INTO categories (added, added_by, title, description, exposed, " +
                "available_from, available_until) " + "VALUES (" + now + ", 1, \'" +
                StringEscapeUtils.escapeSql(category.getTitle()) + "\', \'" +
                StringEscapeUtils.escapeSql(category.getDescription()) + "\', " + (category.isExposed() ? 1 : 0) +
                ", " + category.getAvailableFrom().getTime() + ", " + category.getAvailableUntil().getTime() + ");\n";
    }

    /**
     * generates a string containing the SQL insert statement for the mellivora CTF challenge
     * as specified in the data model input
     * @param now current date
     * @param category the DMCTFCategory which the challenge will belong to
     * @return SQL Insert String
     */
    private static String generateSQLChallenge(long now, DMCTFCategory category) {
        // assumes the category title is provided in the data model CTF category
        DMCTFChallenge challenge = dataModelReader.getChallenge();
        return "\nINSERT INTO challenges (added, added_by, title, category, description, " +
                "exposed, available_from, available_until, flag, points) VALUES (" + now + ", 1, \'" +
                StringEscapeUtils.escapeSql(challenge.getTitle()) + "\', (SELECT id FROM categories WHERE title =\'" +
                StringEscapeUtils.escapeSql(category.getTitle()) + "\'), \'" +
                StringEscapeUtils.escapeSql(challenge.getDescription()) + "\', " + (challenge.isExposed() ? 1 : 0) +
                ", " + challenge.getAvailableFrom().getTime() + ", " + challenge.getAvailableUntil().getTime() +
                ", \'" + StringEscapeUtils.escapeSql(challenge.getFlag()) + "\', " + challenge.getPoints() + ");\n";
    }

    /**
     * generates a string containing the SQL insert statement for the mellivora CTF hints
     * as specified in the data model input
     * @param now current date
     * @return SQL Insert String
     */
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
