package DataModel;

/**
 * Created by renim on 14/07/2017.
 * Represents the metadata in a JSON WAVE data model entry
 */
public class DMMetadata {

    private String metadata;

    public DMMetadata(String metadata) {
        this.metadata = metadata;
    }

    public String getMetadata() {
        return metadata;
    }
}
