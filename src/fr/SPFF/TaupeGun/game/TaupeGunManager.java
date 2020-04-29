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
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class TaupeGunManager {

    private final TaupeGunPlugin main;

    private State state;
    public List<TeamsColor> teamsColorList;
    private Map<Player, Scoreboard> playerScoreboardMap;
    private int timer;
    private int task;
    public int taupeTime = 10 * 60;
    private int chronoTask;

    private int minutes;
    private int seconds;

    public TaupeGunManager() {
        this.main = TaupeGunPlugin.getInstance();
    }

    public void init() {
        this.state = State.WAITING;
        this.timer = 0;
        final int size = this.main.getFileManager().getFile("data").getInt("teams");
        for (final Team team : this.main.getScoreboard().getTeams()) {
            team.unregister();
        }
        this.teamsColorList = Arrays.asList(TeamsColor.getColors());
        this.teamsColorList = MiscUtils.shuffleColor(this.teamsColorList);
        for (int i = 0; i < size; i++) {
            new Teams(false);
        }
        this.playerScoreboardMap = new HashMap<>();

        final Consumer<Player> playerConsumer = player1 -> {
            player1.teleport(this.main.getFileManager().getFile("data").getLocation("spawn"));
            player1.setGameMode(GameMode.ADVENTURE);
            player1.setHealth(20);
            player1.setTotalExperience(0);
            player1.getInventory().clear();
            player1.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
        };
        for (final Player player : this.main.getServer().getOnlinePlayers()) {
            if (player.isDead()) {
                this.main.getServer().getScheduler().scheduleSyncDelayedTask(this.main, () -> {
                    player.spigot().respawn();
                    playerConsumer.accept(player);
                }, 2L);
            } else {
                playerConsumer.accept(player);
            }
        }
    }

    public void chronoStart() {
        AtomicInteger timer = new AtomicInteger(25);
        this.chronoTask = this.main.getServer().getScheduler().runTaskTimerAsynchronously(this.main, () -> {
            if (timer.get() < 0) {
                final int seconds = ((int) Math.ceil(timer.get() * 2D / 10D));
                for (final Player player : this.main.getServer().getOnlinePlayers()) {
                    ActionbarUtils.sendActionBar(player, "§7Démarrage dans " + seconds + " seconde" + (seconds > 9 ? "s" : ""));
                }
            } else if (timer.get() == 0) {
                Message.create("&3&lTaupe Gun &8&l» &7Que la partie commence !").broadcast();
                this.main.getServer().getScheduler().runTask(this.main, this::start);
                this.main.getServer().getScheduler().cancelTask(this.chronoTask);
                return;
            }
            timer.getAndDecrement();
        }, 0L, 10L).getTaskId();
    }

    public State getState() {
        return this.state;
    }

    public int getTimer() {
        return this.timer;
    }

    public List<TeamsColor> getAvailableColors(){
        return this.teamsColorList;
    }

    public void start() {
        this.main.getServer().getScheduler().runTaskAsynchronously(this.main, () -> {
            this.state = State.STARTED;
            List<Player> players = new ArrayList<>(this.main.getServer().getOnlinePlayers());
            final boolean random = this.main.getFileManager().getFile("data").getBoolean("random");
            final List<Teams> teamsList = MiscUtils.shuffleTeams(Teams.getTeams());
            if (random) {
                players = MiscUtils.shufflePlayers(players);
                teamsList.forEach(Teams::removeAllPlayers);
            }
            for (final Player player : players) {
                this.main.getServer().getScheduler().runTask(this.main, () -> {
                    player.getInventory().clear();
                    player.setHealth(20);
                    player.setGameMode(GameMode.SURVIVAL);
                    player.setFoodLevel(20);
                    player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 10));
                });
                final PlayerTaupe playerTaupe = new PlayerTaupe(player);
                Teams teams = null;
                if (random) {
                    teams = (Teams) teamsList.parallelStream().filter(team -> !team.isFull()).toArray()[new Random().nextInt(Math.abs(teamsList.parallelStream().filter(team -> !team.isFull()).toArray().length))];
                    teams.addPlayer(player);
                } else {
                    if (!Teams.getPlayerTeam(player).isPresent()) continue;
                    teams = Teams.getPlayerTeam(player).get();
                }
                final Teams team = teams;
                this.main.getServer().getScheduler().runTask(this.main, () ->
                        team.show(player));
                playerTaupe.setTeam(teams);
            }
            for (final Teams team : teamsList) {
                final Location location = new Location(this.main.getWorld(), this.main.getWorld().getSpawnLocation().getX() + MathUtils.randomRange(-990, 990), 200, this.main.getWorld().getSpawnLocation().getZ() + MathUtils.randomRange(-990, 990));
                this.main.getServer().getScheduler().runTask(this.main, () ->
                        team.getPlayers().forEach(player -> player.teleport(location)));
            }
        });
        this.main.getWorld().getWorldBorder().setCenter(this.main.getWorld().getSpawnLocation());
        this.main.getWorld().getWorldBorder().setSize(1000 * 2);
        this.main.getWorld().setGameRule(GameRule.NATURAL_REGENERATION, false);
        this.main.getWorld().setFullTime(0);
        this.minutes = 1;
        this.seconds = 0;
        // Tâche qui s'exécute tous les 10emes de secondes.
        this.task = this.main.getServer().getScheduler().scheduleSyncRepeatingTask(this.main, () -> {
            for (final Player player : this.main.getServer().getOnlinePlayers()) {
                generateScoreBoard(player);
            }
            if (this.timer < this.taupeTime) {
                for (final Teams teams : Teams.getTeams()) {
                    final List<Player> playerList = teams.getPlayers();
                    for (final Player player : playerList) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (final Player mate : playerList) {
                            if (!player.equals(mate)) {
                                final Location start = player.getLocation().clone();
                                final Location stop = mate.getLocation().getBlock().getLocation().clone();
                                final int distance = MathUtils.getFlatDistance(start, stop);
                                stringBuilder.append("§b§l").append(mate.getDisplayName()).append(" ").append(distance).append(" ").append(MathUtils.getDirection(start, stop)).append(mate.equals(playerList.get(playerList.size() - 1)) ? "" : "              ");
                            }
                        }
                        ActionbarUtils.sendActionBar(player, stringBuilder.toString());
                    }
                }
            } else if (this.timer == this.taupeTime) {
                for (final Player pls : this.main.getServer().getOnlinePlayers()) {
                    final PacketPlayOutNamedSoundEffect sound = new PacketPlayOutNamedSoundEffect(SoundEffects.ENTITY_CAT_DEATH, SoundCategory.BLOCKS, pls.getLocation().getX(), pls.getLocation().getY(), pls.getLocation().getZ(), 1, 1);
                    ((CraftPlayer) pls).getHandle().playerConnection.sendPacket(sound);
                    if (PlayerTaupe.getPlayerTaupe(pls) != null) {
                        pls.setHealth(20);
                        Message.create("&3&lTaupe Gun &8&l» &7Vous avez été soigné.").sendMessage(pls);
                    }
                }
                List<PlayerTaupe> addTaupes = new ArrayList<>();
                for (final Teams teams : Teams.getTeams()) {
                    final PlayerTaupe playerTaupe = PlayerTaupe.getPlayerTaupe(teams.getPlayers().parallelStream().findAny().get());
                    addTaupes.add(playerTaupe);
                }
                Teams taupes = new Teams(true);
                for (final PlayerTaupe playerTaupe : addTaupes) {
                    if (taupes.isFull()) taupes = new Teams(true);
                    taupes.getPlayers().add(playerTaupe.getPlayer());
                    playerTaupe.setTaupe(taupes);
                }
                for (final Teams team : Teams.getTeams()) {
                    if (!team.getColor().equals(TeamsColor.TAUPE)) continue;
                    for (final Player taupe : team.getPlayers()) {
                        final List<Player> playerList = new ArrayList<>();
                        final Message message = Message.create("&3&lTaupe Gun &8&l» &7Vous êtes une &cTaupe&7. Vos alliés sont : ");
                        for (final Player player : team.getPlayers()) {
                            if (player.equals(taupe)) continue;
                            playerList.add(player);
                        }
                        for (final Player player : playerList) {
                            message.append("&c").append(player.getDisplayName()).append("&7").append((player.equals(playerList.get(playerList.size() - 1)) ? "." : ", "));
                        }
                        message.sendMessage(taupe);
                        continue;
                    }
                }
                Message.create("&3&lTaupe Gun &8&l» &7Les taupes ont reçus leur ordre de mission.").broadcast();

                this.seconds = 0;
                this.minutes = 50;
            }
            else if(this.timer == 10 * 60 * 80){
                if(this.main.getWorld().getWorldBorder().getSize() > 100 && this.timer % 10 == 0){
                    this.main.getWorld().getWorldBorder().setSize(this.main.getWorld().getWorldBorder().getSize() - 0.5, 1);
                }
            }
            if (this.timer % 10 == 0) {
                this.seconds--;
                if (this.seconds < 0) {
                    this.minutes--;
                    this.seconds = 59;
                }
            }
            if (this.ifVictory()) {
                this.main.getServer().getScheduler().cancelTask(this.task);
                this.task = -1;
                final Message message = Message.create("&3&lTaupe Gun &7&l» &7Fin de la partie. Victoire de l'équipe " + TeamsColor.getColor(Teams.getTeams().get(0).getColor().getValue()) + Teams.getTeams().get(0).getName()).append(" ");
                final List<Player> playerList = new ArrayList<>(Teams.getTeams().get(0).getPlayers());
                for (final Player player : playerList) {
                    message.append(TeamsColor.getColor(Teams.getTeams().get(0).getColor().getValue()) + "").append(player.getDisplayName()).append("&7").append((player.equals(playerList.get(playerList.size() - 1)) ? "." : ", "));
                }
                message.broadcast();
                this.stop();
                return;
            }
            this.timer++;
        }, 0L, 2L);
    }

    public void stop() {
        if (this.task != -1) {
            this.main.getServer().getScheduler().cancelTask(this.task);
            this.task = -1;
        }
        this.playerScoreboardMap.forEach((player, scoreboard) -> {
            scoreboard.getObjectives().forEach(Objective::unregister);
            scoreboard.clearSlot(DisplaySlot.SIDEBAR);
            PlayerTaupe.getPlayerTaupeList().remove(PlayerTaupe.getPlayerTaupe(player));
        });
        List<Teams> teams = new ArrayList<>();
        for (Teams value : Teams.getTeams()) {
            teams.add(value);
        }
        for (Teams team : teams) {
            team.destroy();
        }
        this.playerScoreboardMap.clear();
        this.init();
    }

    private boolean ifVictory() {
        if (Teams.getTeams().size() > 2)
            return false;
        boolean b = false;
        for (final Teams teams : Teams.getTeams()) {
            if (this.timer > this.taupeTime) {
                if (teams.isTaupe() && Teams.getTeams().size() == 2) {
                    for (final Player player : teams.getPlayers()) {
                        if (PlayerTaupe.getPlayerTaupe(player).isReveal())
                            return false;
                    }
                    b = true;
                }
            } else if (Teams.getTeams().size() == 2)
                return false;
        }
        return b;
    }

    private void generateScoreBoard(final Player player) {
        final Location start = player.getLocation();
        final Location stop = new Location(this.main.getWorld(), 0, 0, 0);
        if (this.playerScoreboardMap.containsKey(player)) {
            this.playerScoreboardMap.get(player).getObjective("taupeGun").unregister();
            this.playerScoreboardMap.get(player).getObjective("taupeLife").unregister();
        } else {
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
        objective.getScore("§cBordure : §7" + (int) Math.ceil(this.main.getWorld().getWorldBorder().getSize() / 2)).setScore(92);
        objective.getScore("§4").setScore(91);
        objective.getScore("§1§8§m-----------------").setScore(90);
        objective.getScore("§5").setScore(89);
        if (this.timer < this.taupeTime) {
            objective.getScore("§cTaupes dans : §7" + (this.minutes < 10 ? "0" + this.minutes : this.minutes) + ":" + (this.seconds < 10 ? "0" + this.seconds : this.seconds)).setScore(88);
        } else if (this.timer < 80 * 60 * 10) {
            objective.getScore("§cReduction de la bordure :").setScore(88);
            objective.getScore("       §7" + (this.minutes < 10 ? "0" + this.minutes : this.minutes) + ":" + (this.seconds < 10 ? "0" + this.seconds : this.seconds)).setScore(87);
        }
        final Objective life = scoreboard.registerNewObjective("taupeLife", "health", "taupeLife");
        life.setRenderType(RenderType.HEARTS);
        life.setDisplaySlot(DisplaySlot.PLAYER_LIST);
        player.setScoreboard(scoreboard);

    }

    public enum State {
        WAITING,
        STARTED
    }

}
