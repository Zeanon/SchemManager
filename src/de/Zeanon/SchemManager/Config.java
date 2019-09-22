package de.Zeanon.SchemManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.plugin.Plugin;

import net.md_5.bungee.api.ChatColor;
 
/**
 * @author Jonas Braun, Zeanon
 */
public class Config {
	private File configFile;
	private File datafolder;
	private FileConfiguration config = null;
	private boolean loaded = false;
	private boolean blankConfig = true;
	private Plugin plugin;
	private File resourcefile;
	private InputStream configInputStream;
	private String configname;
	private String slash;

	/**
	 * Constructor for Config class
	 * @param String configname is the path, where the configfile will be (relative to the plugin Folder)
	 * @param String resourcefile is the filename in the resources folder(src/resources) that should be used
	 * @param InputStream inputstream is a inputstream from another Config, which should be used to create the Config with instead of getting a resourcefile
	 * @param JavaPlugin plugin Instance of JavaPlugin, used to get plugins datafolder
	 */
	
	public Config(String configname, Plugin plugin) {
		this.plugin = plugin;
		this.configname = configname;
		this.datafolder = null;
		this.initConfigFiles();
		this.blankConfig = true;

		if (!this.configFile.exists()) {
			this.loadDefault();
		}
		FileConfiguration cfg = new YamlConfiguration();
		try {
			cfg.load(this.configFile);
		} catch (Exception e) {
		}
		this.config = cfg;
		
		if (this.load()) {
			this.loaded = true;
		}
		this.loaded = true;
	}
	
	public Config(String configname, String resourcefile, Plugin plugin) {
		this.plugin = plugin;
		this.configname = configname;
		this.datafolder = null;
		this.resourcefile= new File(resourcefile);
		this.initConfigFiles();
		this.blankConfig = false;
		
		this.configInputStream = Config.class.getClassLoader()
				.getResourceAsStream("resources/" + this.resourcefile.getName());
		
		if (!this.configFile.exists()) {
			this.loadDefault();
		}
		FileConfiguration cfg = new YamlConfiguration();
		try {
			cfg.load(this.configFile);
		} catch (Exception e) {
		}
		this.config = cfg;
		
		if (this.load()) {
			this.loaded = true;
		}
		this.loaded = true;
	}
	
	public Config(String configname, InputStream inputstream, Plugin plugin) {
		this.plugin = plugin;
		this.configname = configname;
		this.datafolder = null;
		this.initConfigFiles();
		this.blankConfig = false;
		
		this.configInputStream = inputstream;
		
		if (!this.configFile.exists()) {
			this.loadDefault();
		}
		FileConfiguration cfg = new YamlConfiguration();
		try {
			cfg.load(this.configFile);
		} catch (Exception e) {
		}
		this.config = cfg;
		
		if (this.load()) {
			this.loaded = true;
		}
		this.loaded = true;
	}
	
	private void initConfigFiles() {
		File pluginFolder = this.plugin.getDataFolder();
		slash = null;
		if (plugin.getDataFolder().getAbsolutePath().contains("/")) {
			slash = "/";
		}
		if (plugin.getDataFolder().getAbsolutePath().contains("\\")) {
			slash = "\\\\";
		}
		if (!this.configname.contains("/")) {
			File configfile = new File(pluginFolder.getAbsolutePath(), configname);
			this.datafolder = pluginFolder;
			this.configFile = configfile;
		}
		
		if (this.configname.contains("/")) {
			ArrayList<String> parts = new ArrayList<String>(Arrays.asList(configname.split(slash)));
			String configFolderPath = parts.get(0);
			int i = 1;
			while (i < parts.size() -1) {
				configFolderPath = configFolderPath + slash + parts.get(i);
				i++;
			}
			File configFolder = new File(pluginFolder.getAbsolutePath() + slash + configFolderPath);
			File configfile = new File(configFolder, parts.get(parts.size() - 1));
			this.datafolder = configFolder;
			this.configFile = configfile;
		}
	}
	
	public boolean fileExists(String configname, Plugin plugin){
		this.plugin = plugin;
		this.initConfigFiles();
		return (this.datafolder.exists() && this.configFile.exists());
	}
	
	private boolean loadDefault() {
		try {
			if (!this.datafolder.exists()) {
				this.datafolder.mkdirs();
			}
			if (!this.configFile.exists()) {
				this.configFile.createNewFile();
			}
			if (!this.blankConfig) {
				FileUtils.copyInputStreamToFile(this.configInputStream, this.configFile);
				return true;
			}
			if (this.blankConfig) {
				return true;
			}
			return true;
		} catch (IOException e) {
			return false;
		}
	}
		
	public boolean hasKey(String path) {
		if(this.getString(path) != null) {
			return true;
		}
		return false;
	}

	private boolean load() {
		if (!this.configFile.exists()) {
			if (!this.loadDefault()) {
				System.out.println("["+this.plugin.getName()+"] >> [Configs] >> " + this.configFile.getName()+" could not be loaded" );
				return false;
			}
		}
		FileConfiguration cfg = new YamlConfiguration();
		try {
			cfg.load(this.configFile);
		} catch (Exception e) {
			System.out.println("["+this.plugin.getName()+"] >> [Configs] >> " + this.configFile.getName()+" could not be loaded" );
			return false;
		}
		this.config = cfg;
		System.out.println("["+this.plugin.getName()+"] >> [Configs] >> " + this.configname+" loaded" );
		this.loaded = true;
		return true;
	}

	public boolean isLoaded() {
		return this.loaded;
	}
	
	public boolean reload() {
		return this.load();
	}
	
	public InputStream getInputStream() {
		return this.configInputStream;
	}
	
	public void setLocation(String path, Location loc) {
		this.config.set(path + ".world", loc.getWorld().getName());
		this.config.set(path + ".x", loc.getX());
		this.config.set(path + ".y", loc.getY());
		this.config.set(path + ".z", loc.getZ());
		this.config.set(path + ".yaw", loc.getYaw());
		this.config.set(path + ".pitch", loc.getPitch());
		try {
			this.config.save(this.configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Location getLocation(String path) {
		World world = Bukkit.getWorld("world");
		Double x = this.config.getDouble(path + ".x");
		Double y = this.config.getDouble(path + ".y");
		Double z = this.config.getDouble(path + ".z");
		Float yaw =  0.f;
		Float pitch = 0.f;
		if (this.config.getString(path + ".yaw") != null) {
			yaw = (float) this.config.getDouble(path + ".yaw");
		}
		if (this.config.getString(path + ".pitch") != null) {
			pitch = (float) this.config.getDouble(path + ".pitch");
		}
		if (this.config.getString(path + ".world") != null) {
			world = Bukkit.getWorld(this.config.getString(path + ".world"));
		}
		Location location = new Location(world, x, y, z, yaw, pitch);
		return location;
	}
	
	public String getReplacedMessage(String path, String placeholder, String replacement, String placeholder2, String replacement2) {
		String rawmessage = ChatColor.translateAlternateColorCodes('&', this.config.getString(path));
		return rawmessage.replaceAll(placeholder, replacement).replaceAll(placeholder2, replacement2);
	}
	
	public String getSignLine(String path, String placeholder, String replacement, String placeholder2, String replacement2, String placeholder3, String replacement3) {
		String rawmessage = ChatColor.translateAlternateColorCodes('&', this.config.getString(path));
		return rawmessage.replaceAll(placeholder, replacement).replaceAll(placeholder2, replacement2).replaceAll(placeholder3, replacement3);
	}
	
	public String getItemName(String path, String placeholder, String replacement) {
		String rawname = ChatColor.translateAlternateColorCodes('&', this.config.getString(path));
		return rawname.replaceAll(placeholder, replacement);
	}
	
	public String getPlacement(String path, String placeholder, String replacement, String placeholder2, String replacement2) {
		String rawmessage = ChatColor.translateAlternateColorCodes('&', this.config.getString(path));
		return rawmessage.replaceAll(placeholder, replacement).replaceAll(placeholder2, replacement2);
	}
	
	public Material getMaterial(String path) {
		String material = this.config.getString(path).toUpperCase();
		Material block = Material.getMaterial(material);
		return block;
	}
	
	public void setMaterial(String path, String mat) {
		this.config.set(path, mat.toUpperCase());
	}

	public ConfigurationSection createSection(String path) {
		this.config.createSection(path);
		try {
			this.config.save(this.configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this.config.getConfigurationSection(path);
	}

	public ConfigurationSection getSection(String path) {
		return this.config.getConfigurationSection(path);
	}

	public void deletePath(String path) {
		this.config.set(path, null);
		try {
			this.config.save(this.configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void save() {
		try {
			this.config.save(this.configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setString(String path, String value) {
		this.config.set(path, value);
		try {
			this.config.save(this.configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getString(String path) {
		return this.config.getString(path);
	}
	
	public void setInt(String path, int value){
		this.config.set(path, value);
		try {
			this.config.save(this.configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getInt(String path){
		return this.config.getInt(path);
	}

	public void setDouble(String path, double value){
		this.config.set(path, value);
		try {
			this.config.save(this.configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public double getDouble(String path){
		return this.config.getDouble(path);
	}
	
	public void setBoolean(String path, boolean value){
		this.config.set(path, value);
		try {
			this.config.save(this.configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean getBoolean(String path){
		return this.config.getBoolean(path);
	}
	
	public void setList(String path, List<?> list) {
		this.config.set(path, list);
		try {
			this.config.save(this.configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<?> getList(String path) {
		return this.config.getList(path);
	}
	
	public void setHeader(String value){
		this.config.options().header(value);
		try {
			this.config.save(this.configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setFloat(String path, Float value) {
		this.config.set(path, value);
		try {
			this.config.save(this.configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Float getFloat(String path) {
		return (float) this.config.getDouble(path);
	}
	
	public Set<String> getKeys(String path, boolean deep) {
		return this.config.getConfigurationSection(path).getKeys(deep);
	}
	
	public UUID getUUID(String path) {
		return UUID.fromString(this.config.getString(path));
	}
	
	public FileConfiguration getConfig() {
		return this.config;
	}
	
	public String getConfigname() {
		return this.configname;
	}
	
	public boolean clear() {
		if (this.configFile.exists()) {
			this.configFile.delete();
		}
		try {
			if (!this.datafolder.exists()) {
				this.datafolder.mkdirs();
			}
			if (!this.configFile.exists()) {
				this.configFile.createNewFile();
			}
			if (!this.blankConfig) {
				FileUtils.copyInputStreamToFile(this.configInputStream, this.configFile);
				return true;
			}
			if (this.blankConfig) {
				return true;
			}
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	public void deleteConfig() {
		this.configFile.delete();
	}
}