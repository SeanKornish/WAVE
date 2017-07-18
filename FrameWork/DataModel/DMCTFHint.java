package DataModel;

/**
 * Created by renim on 14/07/2017.
 *  Represents the CTF hint for mellivora CTF Engine in a JSON WAVE data model entry
 */
public class DMCTFHint {

    private String body;
    private boolean visible;
    private DMCTFChallenge challenge;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public DMCTFChallenge getChallenge() {
        return challenge;
    }

    public void setChallenge(DMCTFChallenge challenge) {
        this.challenge = challenge;
    }
}
