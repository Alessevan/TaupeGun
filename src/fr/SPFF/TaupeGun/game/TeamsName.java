package fr.SPFF.TaupeGun.game;

public enum TeamsName {

    ALPHA("Balancoire"),
    BRAVO("Pigeon"),
    CHARLIE("Yolo"),
    DELTA("Wololo"),
    ECHO("Éclatax"),
    FOXTROT("Eméçabezunmax"),
    GOLF("Meh"),
    HOTEL("Patafix"),
    INDIA("Intermitants"),
    JULIET("Rhododendron"),
    KILO("Ornithorynx"),
    LIMA("PèreClochard"),
    MIKE("TI-83Premium"),
    NOVEMBER("Niquelaire"),
    OSCAR("PainAuChocolat"),
    PAPA("Skibilipapa"),
    QUEBEC("Chocolatine"),
    ROMEO("RapideEtFurieux"),
    SIERRA("Stfu"),
    TANGO("Thug"),
    UNIFORM("TheRapists"),
    VICTOR("HandSpinner");

    private final String value;

    TeamsName(final String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
