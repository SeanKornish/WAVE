package Main;

import DataModel.DataModeWriter;
import DataModel.DataModelReader;

/**
 * Created by renim on 14/07/2017.
 */
public class Main {

    public static void main(String [] args) {
        DataModelReader dataModelReader = new DataModelReader(args[0]);
        dataModelReader.read();

        DataModeWriter dataModeWriter = new DataModeWriter(args[1]);
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
