package DataModel;

import java.io.File;

/**
 * Created by renim on 14/07/2017.
 * Represents the vulnerable code in a JSON WAVE data model entry
 */
public class DMCodeBehindFile {

    private String client_server;
    private File file;

    public String getClient_server() {
        return client_server;
    }

    public void setClient_server(String client_server) {
        this.client_server = client_server;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
