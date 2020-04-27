package fr.SPFF.TaupeGun.game;

import fr.SPFF.TaupeGun.plugin.TaupeGunPlugin;
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
        for(int i = 0; i < 6; i++){
            teamsColorList.add(TeamsColor.fromValue(i));
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
        List<Teams> teamsList = new ArrayList<>();
        List<Teams> taupesList = new ArrayList<>();
        for(int i = 0; i < 6; i++){
            teamsList.add(new Teams(false));
        }
        List<Player> players = new ArrayList<>(this.main.getServer().getOnlinePlayers());
        if(this.main.getFileManager().getFile("data").getBoolean("random"))
            players = MiscUtils.shufflePlayers(players);
            teamsList = MiscUtils.shuffleTeams(teamsList);
        for(final Player player : players){
            player.getInventory().clear();
            player.setHealth(20);
            player.setGameMode(GameMode.SURVIVAL);
            player.setFoodLevel(20);
            player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 10));
            final PlayerTaupe playerTaupe = new PlayerTaupe(player);
            final Teams teams = teamsList.get(new Random().nextInt(teamsList.size()));
            playerTaupe.setTeam(teams);
            teams.getPlayers().add(player);
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
            if(this.timer == 10 * 60 * 30){
                for(final Player pls : this.main.getServer().getOnlinePlayers()){
                    final PacketPlayOutNamedSoundEffect sound = new PacketPlayOutNamedSoundEffect(SoundEffects.ENTITY_CAT_DEATH, SoundCategory.BLOCKS, pls.getLocation().getX(), pls.getLocation().getY(), pls.getLocation().getZ(), 1, 1);
                    ((CraftPlayer) pls).getHandle().playerConnection.sendPacket(sound);
                    if(PlayerTaupe.getPlayerTaupe(pls) != null){
                        pls.setHealth(20);
                        Message.create("&3&lTaupe Gun &8&l» &7Vous avez été soigné.").sendMessage(pls);
                    }
                    Message.create("&3&lTaupe Gun &8&l» &7Les taupes ont reçus leur ordre de mission.").broadcast();
                }
            }
            if(this.timer == 10 * 60 * 80){
                if(this.main.getWorld().getWorldBorder().getSize() > 100 && this.timer % 10 == 0){
                    this.main.getWorld().getWorldBorder().setSize(this.main.getWorld().getWorldBorder().getSize() - 0.5, 1);
                }
            }
            this.timer++;
        }, 0L, 2L);
    }
}
