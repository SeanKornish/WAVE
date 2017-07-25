package DataModel;

import java.util.Date;

/**
 * Created by renim on 14/07/2017.
 *  Represents the CTF category for mellivora CTF Engine in a JSON WAVE data model entry
 */
public class DMCTFCategory {

    private String title;
    private String description;
    private boolean exposed;
    private Date availableFrom;
    private Date availableUntil;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isExposed() {
        return exposed;
    }

    public void setExposed(boolean exposed) {
        this.exposed = exposed;
    }

    public Date getAvailableFrom() {
        return availableFrom;
    }

    public void setAvailableFrom(Date availableFrom) {
        this.availableFrom = availableFrom;
    }

    public Date getAvailableUntil() {
        return availableUntil;
    }

    public void setAvailableUntil(Date availableUntil) {
        this.availableUntil = availableUntil;
    }
}
