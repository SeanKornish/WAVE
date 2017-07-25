package DataModel;

import java.io.File;

/**
 * Created by renim on 14/07/2017.
 * Represents the code to remedy the vulnerability in a JSON WAVE data model entry
 */
public class DMRemediationCodeFile {

    private String client_server;
    private String technology;
    private File file;

    public String getClient_server() {
        return client_server;
    }

    public void setClient_server(String client_server) {
        this.client_server = client_server;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
