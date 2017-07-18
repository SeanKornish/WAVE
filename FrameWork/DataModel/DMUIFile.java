package DataModel;

import java.io.File;

/**
 * Created by renim on 14/07/2017.
 * Represents a UI file in a JSON WAVE data model entry
 */
public class DMUIFile {

    private String technology;
    private File file;

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
