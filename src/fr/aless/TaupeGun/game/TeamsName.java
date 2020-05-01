package fr.aless.TaupeGun.game;

public enum TeamsName {

    ALPHA("Alpha"),
    BRAVO("Bravo"),
    CHARLIE("Charlie"),
    DELTA("Delta"),
    ECHO("Echo"),
    FOXTROT("Foxtrot"),
    GOLF("Golf"),
    HOTEL("Hotel"),
    INDIA("India"),
    JULIET("Juliet"),
    KILO("Kilo"),
    LIMA("Lima"),
    MIKE("Mike"),
    NOVEMBER("November"),
    OSCAR("Oscar"),
    PAPA("Papa"),
    QUEBEC("Quebec"),
    ROMEO("Romeo"),
    SIERRA("Sierra"),
    TANGO("Tango"),
    VICTOR("Victor"),
    WHISKEY("Whiskey"),
    XRAY("X-Ray"),
    YANKEE("Yankee"),
    ZULU("Zulu");

    private final String value;

    TeamsName(final String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
