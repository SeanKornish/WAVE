package DataModel;

import java.util.Date;

/**
 * Created by renim on 14/07/2017.
 *  Represents the CTF challenge for mellivora CTF Engine in a JSON WAVE data model entry
 */
public class DMCTFChallenge {

    private String title;
    private String description;
    private boolean exposed;
    private Date availableFrom;
    private Date availableUntil;
    private String flag;
    private boolean caseInsensitive;
    private boolean automark;
    private int points;
    private int attempts;
    private long timeBetweenAttepmts;
    private String mustCompleteAfter;

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

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public boolean isCaseInsensitive() {
        return caseInsensitive;
    }

    public void setCaseInsensitive(boolean caseInsensitive) {
        this.caseInsensitive = caseInsensitive;
    }

    public boolean isAutomark() {
        return automark;
    }

    public void setAutomark(boolean automark) {
        this.automark = automark;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public long getTimeBetweenAttepmts() {
        return timeBetweenAttepmts;
    }

    public void setTimeBetweenAttepmts(long timeBetweenAttepmts) {
        this.timeBetweenAttepmts = timeBetweenAttepmts;
    }

    public String getMustCompleteAfter() {
        return mustCompleteAfter;
    }

    public void setMustCompleteAfter(String mustCompleteAfter) {
        this.mustCompleteAfter = mustCompleteAfter;
    }
}
