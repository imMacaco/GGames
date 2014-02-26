package me.macaco.gg;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scoreboard.Score;

public class GGamesListener implements Listener {
	int x = 150;
	@EventHandler
	public void onPlayerDies(PlayerDeathEvent event){
		
		Player player = event.getEntity().getPlayer();
		
		if(ArenaManager.getManager().getPlayerArena(player.getName()) == true){
			x = x + 1;
			
			Score score = GGames.instance.o.getScore(Bukkit.getOfflinePlayer(ChatColor.AQUA + player.getName()));
   			score.setScore(ArenaManager.vidas - x);
		    
		}
		
	}
}
