package DataModel;

import java.util.List;

/**
 * Created by renim on 14/07/2017.
 * Represents the vulnerability details in a JSON WAVE data model entry
 */
public class DMDetails {

    private String description;
    private String technology;
    private String notes;
    private List<String> attack_hints;
    private List<String> remediation_hints;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<String> getAttack_hints() {
        return attack_hints;
    }

    public void setAttack_hints(List<String> attack_hints) {
        this.attack_hints = attack_hints;
    }

    public List<String> getRemediation_hints() {
        return remediation_hints;
    }

    public void setRemediation_hints(List<String> remediation_hints) {
        this.remediation_hints = remediation_hints;
    }
}
