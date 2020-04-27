package fr.SPFF.TaupeGun.game;

import fr.SPFF.TaupeGun.plugin.TaupeGunPlugin;

public class TaupeGunManager {

    private final TaupeGunPlugin main;
    private State state;

    public TaupeGunManager() {
        this.main = TaupeGunPlugin.getInstance();
        this.state = State.WAITING;
    }

    public enum State {
        WAITING,
        STARTED,
        FINISHED;
    }

    public State getState() {
        return this.state;
    }
}
