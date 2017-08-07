package DataModel;

/**
 * Created by renim on 04/08/2017.
 */
public class DMConfig {

    private String baseURL;
    private String vulnURL;
    private String vulnLink;
    private String files;

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    public String getVulnURL() {
        return vulnURL;
    }

    public void setVulnURL(String vulnURL) {
        this.vulnURL = vulnURL;
    }

    public String getVulnLink() {
        return vulnLink;
    }

    public void setVulnLink(String vulnLink) {
        this.vulnLink = vulnLink;
    }

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
    }
}
