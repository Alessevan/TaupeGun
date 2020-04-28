package fr.SPFF.TaupeGun.game;

import fr.SPFF.TaupeGun.plugin.TaupeGunPlugin;
import fr.SPFF.TaupeGun.utils.ActionbarUtils;
import fr.SPFF.TaupeGun.utils.MathUtils;
import fr.SPFF.TaupeGun.utils.Message;
import fr.SPFF.TaupeGun.utils.MiscUtils;
import net.minecraft.server.v1_15_R1.PacketPlayOutNamedSoundEffect;
import net.minecraft.server.v1_15_R1.SoundCategory;
import net.minecraft.server.v1_15_R1.SoundEffects;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;

public class TaupeGunManager {

    private final TaupeGunPlugin main;

    private State state;
    private List<TeamsColor> teamsColorList;
    private Map<Player, Scoreboard> playerScoreboardMap;
    private int timer;
    private int task;

    private int minutes;
    private int seconds;

    public TaupeGunManager() {
        this.main = TaupeGunPlugin.getInstance();
        init();
    }

    public void init(){
        this.state = State.WAITING;
        this.timer = 0;
        final int size = this.main.getFileManager().getFile("data").getInt("teams");
        for(int i = 0; i < size; i++){
            teamsColorList.add(TeamsColor.fromValue(new Random().nextInt(TeamsColor.values().length)));
        }
        for(int i = 0; i < size; i++){
            new Teams(false);
        }
        this.playerScoreboardMap = new HashMap<>();
    }

    public enum State {
        WAITING,
        STARTED,
        FINISHED;
    }

    public State getState() {
        return this.state;
    }

    public int getTimer(){
        return this.timer;
    }

    public List<TeamsColor> getAvailableColors(){
        return this.teamsColorList;
    }

    public void start(){
        this.state = State.STARTED;
        List<Player> players = new ArrayList<>(this.main.getServer().getOnlinePlayers());
        final boolean random = this.main.getFileManager().getFile("data").getBoolean("random");
        final List<Teams> teamsList = MiscUtils.shuffleTeams(Teams.getTeams());
        if(random) {
            players = MiscUtils.shufflePlayers(players);
            teamsList.forEach(Teams::removeAllPlayers);
        }
        for(final Player player : players){
            player.getInventory().clear();
            player.setHealth(20);
            player.setGameMode(GameMode.SURVIVAL);
            player.setFoodLevel(20);
            player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 10));
            final PlayerTaupe playerTaupe = new PlayerTaupe(player);
            Teams teams = Teams.getPlayerTeam(player);
            if(random){
                teams = (Teams) teamsList.parallelStream().filter(team -> !team.isFull()).toArray()[new Random().nextInt(teamsList.size())];
                teams.addPlayer(player);
            }
            playerTaupe.setTeam(teams);
        }
        for(final Teams team : teamsList){
            final Location location = new Location(this.main.getWorld(), MathUtils.randomRange(-990, 990), 255, MathUtils.randomRange(-990, 990));
            team.getPlayers().forEach(player -> player.teleport(location));
        }
        this.main.getWorld().getWorldBorder().setSize(1000);
        this.main.getWorld().getWorldBorder().setCenter(new Location(this.main.getWorld(), 0, 0, 0));
        this.main.getWorld().setFullTime(0);
        this.minutes = 30;
        this.seconds = 0;
        // Tâche qui s'exécute tous les 10emes de secondes.
        this.task = this.main.getServer().getScheduler().scheduleSyncRepeatingTask(this.main, () -> {
            for(final Player player : this.main.getServer().getOnlinePlayers()){
                generateScoreBoard(player);
            }
            if(this.timer < 10 * 60 * 30){
                for(final Teams teams : Teams.getTeams()){
                    for(final Player player : teams.getPlayers()) {
                        final List<Player> playerList = new ArrayList<>();
                        for (final Player mate : teams.getPlayers()) {
                            if (player.equals(mate)) continue;
                            playerList.add(player);
                        }
                        StringBuilder stringBuilder = new StringBuilder();
                        for (final Player mate : playerList) {
                            final Location start = player.getLocation().clone();
                            final Location stop = mate.getLocation().getBlock().getLocation().clone();
                            final int distance = MathUtils.getFlatDistance(start, stop);
                            stringBuilder.append("§b§l").append(mate.getDisplayName()).append(" ").append(distance).append(" ").append(MathUtils.getDirection(start, stop)).append("              ");
                        }
                        ActionbarUtils.sendActionBar(player, stringBuilder.toString());
                    }
                }
            }
            else if(this.timer == 10 * 60 * 30){
                for(final Player pls : this.main.getServer().getOnlinePlayers()){
                    final PacketPlayOutNamedSoundEffect sound = new PacketPlayOutNamedSoundEffect(SoundEffects.ENTITY_CAT_DEATH, SoundCategory.BLOCKS, pls.getLocation().getX(), pls.getLocation().getY(), pls.getLocation().getZ(), 1, 1);
                    ((CraftPlayer) pls).getHandle().playerConnection.sendPacket(sound);
                    if(PlayerTaupe.getPlayerTaupe(pls) != null){
                        pls.setHealth(20);
                        Message.create("&3&lTaupe Gun &8&l» &7Vous avez été soigné.").sendMessage(pls);
                    }
                }
                Teams taupes = new Teams(true);
                for(final Teams teams : Teams.getTeams()){
                    final PlayerTaupe playerTaupe = PlayerTaupe.getPlayerTaupe(teams.getPlayers().get(new Random().nextInt(teams.getPlayers().size())));
                    if(taupes.isFull()) taupes = new Teams(true);
                    taupes.getPlayers().add(playerTaupe.getPlayer());
                    playerTaupe.setTaupe(taupes);
                }
                for(final Teams teams : Teams.getTeams()){
                    if(!teams.getColor().equals(TeamsColor.TAUPE)) continue;
                    for(final Player taupe : teams.getPlayers()) {
                        final List<Player> playerList = new ArrayList<>();
                        final Message message = Message.create("&3&lTaupe Gun &7&l» &7Vous êtes une &cTaupe&7. Vos alliés sont : ");
                        for (final Player player : teams.getPlayers()) {
                            if (player.equals(taupe)) continue;
                            playerList.add(player);
                        }
                        for (final Player player : playerList) {
                            message.append("&c").append(player.getDisplayName()).append("&7").append((player.equals(playerList.get(playerList.size() - 1)) ? "." : ", "));
                        }
                        message.sendMessage(taupe);
                    }
                }
                Message.create("&3&lTaupe Gun &8&l» &7Les taupes ont reçus leur ordre de mission.").broadcast();

                for(final Teams teams : Teams.getTeams()){
                    final PlayerTaupe playerTaupe = PlayerTaupe.getPlayerTaupe(teams.getPlayers().get(new Random().nextInt(teams.getPlayers().size())));
                    if(taupes.isFull()) taupes = new Teams(true);
                    taupes.getPlayers().add(playerTaupe.getPlayer());
                    playerTaupe.setTaupe(taupes);
                }
                this.seconds = 0;
                this.minutes = 50;
            }
            else if(this.timer == 10 * 60 * 80){
                if(this.main.getWorld().getWorldBorder().getSize() > 100 && this.timer % 10 == 0){
                    this.main.getWorld().getWorldBorder().setSize(this.main.getWorld().getWorldBorder().getSize() - 0.5, 1);
                }
            }
            if(this.timer % 10 == 0){
                this.seconds--;
                if(this.seconds < 0){
                    this.minutes--;
                    this.seconds = 59;
                }
            }
            if(Teams.getTeams().size() == 0){
                this.stop();
                return;
            }
            this.timer++;
        }, 0L, 2L);
    }

    public void stop(){
        if(this.task != -1){
            this.main.getServer().getScheduler().cancelTask(this.task);
            this.task = -1;
        }
        this.playerScoreboardMap.forEach((player, scoreboard) -> {
            scoreboard.getObjectives().forEach(Objective::unregister);
            scoreboard.clearSlot(DisplaySlot.SIDEBAR);
            PlayerTaupe.getPlayerTaupeList().remove(PlayerTaupe.getPlayerTaupe(player));
            Teams.getTeams().remove(Teams.getPlayerTeam(player));
        });
        this.playerScoreboardMap.clear();
        this.state = State.WAITING;
    }

    private void generateScoreBoard(final Player player){
        final Location start = player.getLocation();
        final Location stop = new Location(this.main.getWorld(), 0, 0, 0);
        if(this.playerScoreboardMap.containsKey(player)) {
            this.playerScoreboardMap.get(player).getObjective("taupeGun").unregister();
        }
        else {
            this.playerScoreboardMap.put(player, this.main.getServer().getScoreboardManager().getNewScoreboard());
        }
        final Scoreboard scoreboard = this.playerScoreboardMap.get(player);
        final Objective objective = scoreboard.registerNewObjective("taupeGun", "dummy", "§c§lTaupe Gun");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.getScore("§0").setScore(100);
        objective.getScore("§8§m-----------------").setScore(99);
        objective.getScore("§1").setScore(98);
        objective.getScore("§cCentre : §7" + MathUtils.getFlatDistance(start, stop) + " " + MathUtils.getDirection(start, stop)).setScore(96);
        objective.getScore("§2").setScore(95);
        objective.getScore("§0§8§m-----------------").setScore(94);
        objective.getScore("§3").setScore(93);
        objective.getScore("§cBordure : §7" + (int) Math.ceil(this.main.getWorld().getWorldBorder().getSize())).setScore(92);
        objective.getScore("§4").setScore(91);
        objective.getScore("§1§8§m-----------------").setScore(90);
        objective.getScore("§5").setScore(89);
        if(this.timer < 30 * 60 * 10){
            objective.getScore("§cTaupes dans : §7" + (this.minutes < 10 ? "0" + this.minutes : this.minutes) + ":" + (this.seconds < 10 ? "0" + this.seconds : this.seconds)).setScore(88);
        }
        else if(this.timer < 80 * 60 * 10) {
            objective.getScore("§cReduction de la bordure :").setScore(88);
            objective.getScore("       §7" + (this.minutes < 10 ? "0" + this.minutes : this.minutes) + ":" + (this.seconds < 10 ? "0" + this.seconds : this.seconds)).setScore(87);
        }
        player.setScoreboard(scoreboard);

    }

}
