package me.macaco.gg;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.server.v1_6_R3.ScoreboardBaseCriteria;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class GGames extends JavaPlugin{
	public static GGames instance;
	public final static Logger log = Logger.getLogger("Minecraft");
	public Objective o; 
	public Scoreboard lifesBoard = null;
	public Objective lifesObj = null;
	   
	public void onEnable() {
		GGames.instance = this;
		this.saveDefaultConfig();
		this.getServer().getPluginManager().registerEvents(new GGamesListener(), this);
		FileConfiguration fc = this.getConfig();
		ArenaManager.getManager().loadArenas();
		this.log("O Plugin foi ligado com sucesso!", Level.INFO);
		
		//Scoreboard
		Scoreboard board = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
		o = board.registerNewObjective("lifes", "dummy");
		o.setDisplaySlot(DisplaySlot.SIDEBAR);
		o.setDisplayName(ChatColor.GREEN + "Lifes Remaining:");
		this.lifesBoard = board;
		this.lifesObj = o;
		
		 
	}
	
	public void onDisable() {
		this.log("O Plugin foi desligado com sucesso!", Level.INFO);
	}
	
	public void log(String s, Level l) {
		GGames.log.log(l, "[GG] " + s);
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		FileConfiguration fc = this.getConfig();
		Player player = (Player) sender;
		Location entrarpos = player.getLocation() ;
		Location startpos = player.getLocation() ;
		Location sairpos = player.getLocation() ;
		if (cmd.getName().equalsIgnoreCase("gg")) {
			if (args.length < 1){
	               player.sendMessage(ChatColor.RED + "Argumentos insufecientes");
	            } else if (args[0].equalsIgnoreCase("join")){
	                if (args.length < 2){
	                	player.sendMessage(ChatColor.RED + "Argumentos insufecientes");
	                }
	                else{
	                	//We would have to check the arguments first, but lets just be lazy! :p
						ArenaManager.getManager().addPlayers(player, args[1]); //Would use our method made in our ArenaManager class, args[0] is the first argument they typed
	                }
	            } else if (args[0].equalsIgnoreCase("setarena")){
	            	if (args.length < 2){
	            		player.sendMessage(ChatColor.RED + "Falta o nome da arena");
	            	}
	            	else {
	            		if (args[1].equalsIgnoreCase("joinloc")){
	            			entrarpos = player.getLocation();
	            			player.sendMessage(ChatColor.GREEN + "Position setted");
	            			
	            		}
	            		else if (args[1].equalsIgnoreCase("startloc")){
	            			startpos = player.getLocation();
	    					player.sendMessage(ChatColor.GREEN + "Position setted");
	            		}
	            		else if (args[1].equalsIgnoreCase("endloc")){
	            			sairpos = player.getLocation();
	    					player.sendMessage(ChatColor.GREEN + "Position setted");
	            		}
	            	}
	            } else if (args[0].equalsIgnoreCase("create")){
	            	if (args.length < 2){
	            		player.sendMessage(ChatColor.RED + "Falta o nome da arena");
	            	}
	            	else{
	            		String nomedaarena = args[1];
						
						ArenaManager.getManager().createArena(nomedaarena, entrarpos, startpos, sairpos, 2, 1);
						player.sendMessage(ChatColor.GREEN + "Arena criada com sucesso!");
	                //nrpg info
	            }
	            } else if (args[0].equalsIgnoreCase("leave")){
	            	ArenaManager.getManager().removePlayer(player, args[1]);
	            	
	            }
		
			}
		return false;
	}
}
