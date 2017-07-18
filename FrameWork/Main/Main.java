package Main;

import DataModel.DataModelReader;

/**
 * Created by renim on 14/07/2017.
 */
public class Main {

    public static void main(String [] args) {
        DataModelReader dataModelReader = new DataModelReader(args[0]);
        dataModelReader.read();
    }
}
