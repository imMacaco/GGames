package me.macaco.gg;






import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
 
/**
 *
 * @Author Jake
 */
public class ArenaManager {
   
	
	public static final int vidas = 150;
    private static ArenaManager am = new ArenaManager();
   
    //Usefull for getting the ArenaManager, like so: ArenaManager.getManager()
    public static ArenaManager getManager() {
        return am;
    }

   
   
    //A method for getting one of the Arenas out of the list by name:
    public Arena getArena(String name) {
        for (Arena a: Arena.arenaObjects) { //For all of the arenas in the list of objects
            if (a.getName().equals(name)) { //If the name of an arena object in the list is equal to the one in the parameter...
                return a; //Return that object
            }
        }
        return null; //No objects were found, return null
    }
    public boolean getPlayerArena(String nome) {
    	for (Arena a: Arena.arenaObjects) {
    		if (a.getPlayers().contains(nome)){
    			return true;
    		}
    	}
		return false;
    }
    //A method for adding players
    public void addPlayers(Player player, String arenaName) {
       
        if (getArena(arenaName) != null) { //If the arena exsists
           
            Arena arena = getArena(arenaName); //Create an arena for using in this method
           
            if (!arena.isFull()) { //If the arena is not full
               
                if (!arena.isInGame()) {
                   
                    //Every check is complete, arena is joinable
                    player.getInventory().clear(); //Clear the players inventory
                    player.setHealth(player.getMaxHealth()); //Heal the player
                    player.setFireTicks(0); //Heal the player even more ^ ^ ^
                   
                    //Teleport to the arena's join location
                    player.teleport(arena.getJoinLocation());
                   
                    //Add the player to the arena list
                    arena.getPlayers().add(player.getName()); //Add the players name to the arena
                   
                    int playersLeft = arena.getMinPlayers() - arena.getPlayers().size(); //How many players needed to start
                    //Send the arena's players a message
                    arena.sendMessage(ChatColor.BLUE + player.getName() + " has joined the arena! We only need " + playersLeft + " to start the game!");
                   
                   
                    if (playersLeft == 0) { //IF there are 0 players needed to start the game
                        startArena(arenaName); //Start the arena, see the method way below :)
                    }
                   
               
            } else { //Specifiend arena is in game, send the player an error message
                    player.sendMessage(ChatColor.RED + "The arena you are looking for is currently full!");
                   
                }
            } else { //Specified arena is full, send the player an error message
                player.sendMessage(ChatColor.RED + "The arena you are looking for is currently full!");
            }
           
        } else { //The arena doesn't exsist, send the player an error message
            player.sendMessage(ChatColor.RED + "The arena you are looking for could not be found!");
        }
       
    }
   
   
    //A method for removing players
    public void removePlayer(Player player, String arenaName) {
       
        if (getArena(arenaName) != null) { //If the arena exsists
           
             Arena arena = getArena(arenaName); //Create an arena for using in this method
           
            if (arena.getPlayers().contains(player.getName())) { //If the arena has the player already
               
                //Every check is complete, arena is leavable
                player.getInventory().clear(); //Clear the players inventory
                player.setHealth(player.getMaxHealth()); //Heal the player
                player.setFireTicks(0); //Heal the player even more ^ ^ ^
                   
                    //Teleport to the arena's join location
                    player.teleport(arena.getEndLocation());
                   
                     //remove the player to the arena list
                    arena.getPlayers().remove(player.getName()); //Removes the players name to the arena
                   
                    //Send the arena's players a message
                    arena.sendMessage(ChatColor.BLUE + player.getName() + " has left the Arena! There are " + arena.getPlayers().size() + "players currently left!");
                   
               
               
               
            } else { //Specified arena doesn't have the player, send the player an error message
                player.sendMessage(ChatColor.RED + "Your not in the arena your looking for!");
               
            }
           
           
        } else { //The arena doesn't exsist, send the player an error message
            player.sendMessage(ChatColor.RED + "The arena you are looking for could not be found!");
        }
    }
       
       
        //A method for starting an Arena:
        public void startArena(String arenaName) {
        	
            if (getArena(arenaName) != null) { //If the arena exsists
            	
                 final Arena arena = getArena(arenaName); //Create an arena for using in this method
                 
                 
                 //Set ingame
                 arena.setInGame(true);
                 
                 for (final String s: arena.getPlayers()) {//Loop through every player in the arena
                	Bukkit.getPlayer(s).setScoreboard(GGames.instance.lifesBoard);
                	Score score = GGames.instance.o.getScore(Bukkit.getOfflinePlayer(ChatColor.AQUA + Bukkit.getPlayer(s).getName()));
                	score.setScore(vidas);
                	GGames.instance.getServer().getScheduler().scheduleSyncRepeatingTask(GGames.instance, new Runnable() {
	                	int countdown = 30; //This is how long(in secs) you want the countdown to be!
	                	
	                	//Making it so the player can see the scoreboard
	                	public void run() {
             				
             				countdown --; //Taking away 1 from countdown every 1 second
             				
             				
             				if(countdown == 30 || countdown == 20 || countdown == 10){
	             				Bukkit.getPlayer(s).sendMessage(ChatColor.GRAY + "[" + ChatColor.GOLD + "X-Run" + ChatColor.GRAY + "] A Arena ira iniciar em " + ChatColor.GOLD + countdown + ChatColor.GRAY + " segundos");
	             			}
             				if(countdown <= 5){
	             				Bukkit.getPlayer(s).sendMessage(ChatColor.GRAY + "[" + ChatColor.GOLD + "X-Run" + ChatColor.GRAY + "] A Arena ira iniciar em " + ChatColor.GOLD + countdown + ChatColor.GRAY + " segundos");
	             			}
	             			
	             			if(countdown == 0){
	             				GGames.instance.getServer().getScheduler().cancelAllTasks();
	             				Bukkit.getPlayer(s).sendMessage(ChatColor.GRAY + "[" + ChatColor.GOLD + "X-Run" + ChatColor.GRAY + "] A Arena comecou!");
	             				countdown = 30;
	             				Bukkit.getPlayer(s).teleport(arena.getStartLocation()); //Teleports the player to the arena start location
	             				 //Registering the objective needed for the timer
	             				arena.sendMessage(ChatColor.GOLD + "The arena has BEGUN!");
	             			 }
             			}
             		}, 0L, 20L); //Repeating it every second                     
                    GGames.instance.getServer().getScheduler().scheduleSyncRepeatingTask(GGames.instance, new Runnable() {

               				@Override
               				public void run() {
               					Score score = GGames.instance.o.getScore(Bukkit.getOfflinePlayer(ChatColor.AQUA + Bukkit.getPlayer(s).getName()));
               	    			score.setScore(vidas - GGamesListener.x);
               					
               				}
                       		
                       	}, 0L, 20L); //Repeating it every second  
                       	
                       }
                       
                     
                     
                 }
                 
                 
             }
           
        
        
 			
 		 
        public void endArena(String arenaName) {
           
            if (getArena(arenaName) != null) { //If the arena exsists
                 
                 Arena arena = getArena(arenaName); //Create an arena for using in this method
                 
                 //Send them a message
                 arena.sendMessage(ChatColor.GOLD + "The arena has ended :(");
                 
                 //Set ingame
                 arena.setInGame(false);
                 
                 for (String s: arena.getPlayers()) {//Loop through every player in the arena
                     
                     //Teleport them:
                     
                     Player player = Bukkit.getPlayer(s); //Create a player by the name
                player.teleport(arena.getEndLocation());
                     
                player.getInventory().clear(); //Clear the players inventory
                player.setHealth(player.getMaxHealth()); //Heal the player
                player.setFireTicks(0); //Heal the player even more ^ ^ ^
               
                //Remove them all from the list
                arena.getPlayers().remove(player.getName());
                     
                 }
               
               
            }
        }
           
           
            //And our final method, loading each arena
            //This will be resonsible for creating each arena from the config, and creating an object to represent it
            //Call this method in your main class, onEnable
           
       
        public void loadArenas() {
           
            //I just create a quick Config Variable, obviously don't do this.
            //Use your own config file
            FileConfiguration fc = GGames.instance.getConfig(); //If you just use this code, it will erorr, its null. Read the notes above, USE YOUR OWN CONFIGURATION FILE
           
            //Youll get an error here, FOR THE LOVE OF GAWD READ THE NOTES ABOVE!!!
            for (String keys: fc.getConfigurationSection("arenas").getKeys(false)) { //For each arena name in the arena file
               
                //Now lets get all of the values, and make an Arena object for each:
                //Just to help me remember: Arena myArena = new Arena("My Arena", joinLocation, startLocation, endLocation, 17)
               
                World world = Bukkit.getWorld("arenas." + keys + ".world");
               
                //Arena's name is keys
               
                double joinX = fc.getDouble("arenas." + "keys." + "joinX");
                double joinY = fc.getDouble("arenas." + "keys." + "joinY");
                double joinZ = fc.getDouble("arenas." + "keys." + "joinZ");
                Location joinLocation = new Location(world, joinX, joinY, joinZ);
               
                double startX = fc.getDouble("arenas." + "keys." + "startX");
                double startY = fc.getDouble("arenas." + "keys." + "startY");
                double startZ = fc.getDouble("arenas." + "keys." + "startZ");
               
                Location startLocation = new Location(world, startX, startY, startZ);
               
                double endX = fc.getDouble("arenas." + "keys." + "endX");
                double endY = fc.getDouble("arenas." + "keys." + "endX");
                double endZ = fc.getDouble("arenas." + "keys." + "endX");
               
                Location endLocation = new Location(world, endX, endY, endZ);
                int minPlayers = fc.getInt("arenas." + keys + ".minPlayers");
                int maxPlayers = fc.getInt("arenas." + keys + ".maxPlayers");
               
                //Now lets create an object to represent it:
                Arena arena = new Arena(keys, joinLocation, startLocation, endLocation, 1, 17);
               
            }
           
 
    }
       
        //Our final method, create arena!
        public void createArena(String arenaName, Location joinLocation, Location startLocation, Location endLocation, int minPlayers, int maxPlayers) {
           
            //Now, lets create an arena object to represent it:
            Arena arena = new Arena(arenaName, joinLocation, startLocation, endLocation, minPlayers, maxPlayers);
           
            //Now here is where you would save it all to a file, again, im going to create a null FileConfiguration, USE YOUR OWN!!!
            FileConfiguration fc = GGames.instance.getConfig(); //USE YOUR OWN PUNK
           
            fc.set("arenas." + arenaName, null); //Set its name
            //Now sets the other values
           
            String path = "arenas." + arenaName + "."; //Shortcut
            //Sets the paths
            fc.set(path + "joinX", joinLocation.getX());
            fc.set(path + "joinY", joinLocation.getY());
            fc.set(path + "joinZ", joinLocation.getZ());
           
            fc.set(path + "startX", startLocation.getX());
            fc.set(path + "startY", startLocation.getY());
            fc.set(path + "startZ", startLocation.getZ());
           
            fc.set(path + "endX", endLocation.getX());
            fc.set(path + "endY", endLocation.getY());
            fc.set(path + "endZ", endLocation.getZ());
           
            fc.set(path + "maxPlayers", maxPlayers);
            fc.set(path + "minPlayers", minPlayers);
           
            //Now save it up down here
            GGames.instance.saveConfig();
        }
}