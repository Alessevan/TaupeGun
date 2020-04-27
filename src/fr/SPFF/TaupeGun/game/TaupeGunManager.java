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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TaupeGunManager {

    private final TaupeGunPlugin main;

    private State state;
    private List<TeamsColor> teamsColorList;
    private int timer;
    private int task;

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
            teamsList.forEach(teams -> teams.getPlayers().clear());
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
                teams.getPlayers().add(player);
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
        // Tâche qui s'exécute tous les 10emes de secondes.
        this.task = this.main.getServer().getScheduler().scheduleSyncRepeatingTask(this.main, () -> {
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
                            start.setY(0);
                            final Location stop = mate.getLocation().getBlock().getLocation().clone();
                            stop.setY(0);
                            final int distance = (int) Math.ceil(Math.sqrt(Math.pow(start.getX() - stop.getX(), 2) + Math.pow(start.getZ() - stop.getZ(), 2)));
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
            }
            else if(this.timer == 10 * 60 * 80){
                if(this.main.getWorld().getWorldBorder().getSize() > 100 && this.timer % 10 == 0){
                    this.main.getWorld().getWorldBorder().setSize(this.main.getWorld().getWorldBorder().getSize() - 0.5, 1);
                }
            }
            this.timer++;
        }, 0L, 2L);
    }
}
