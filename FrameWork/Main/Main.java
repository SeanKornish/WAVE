package Main;

import DataModel.DataModeWriter;
import DataModel.DataModelReader;

/**
 * Created by renim on 14/07/2017.
 */
public class Main {

    private static String input;
    private static String output;

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

        // read input data
        DataModelReader dataModelReader = new DataModelReader(input);
        dataModelReader.read();

        // write input data to output
        DataModeWriter dataModeWriter = new DataModeWriter(output);
        dataModeWriter.setDetails(dataModelReader.getDetails());
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
