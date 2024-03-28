package hunter;

public enum Participant {
    FUGITIVE("Fugitive"),
    HUNTER("Hunter"),
    NONE("None");

    private final String displayTitle;

    Participant(String displayTitle) {
        this.displayTitle = displayTitle;
    }

    public String getDisplayTitle() {
        return displayTitle;
    }
}
