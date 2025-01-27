package no.ntnu.online.onlineguru.utils.settingsreader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * 
 * This class is made to read settingsfiles.
 * Settingsfiles should be in the following format;
 * 
 * [block]
 * field=setting
 * another=setting
 * [another]
 * block=here
 * more=settings
 * 
 * SettingsReader does not check any structure of the file other than [block] and field=setting.
 * The settings will be returned to the caller in an ArrayList containing Settings objects.
 * There will be one Settings objeckt ber [block] in the conf file.
 * Each Settings object contains a HashMap with the values from the file. 
 * Settings has methods for getting the fields form the HashMap.
 * 
 * The reason for making this is that many Plugins might require settings spread across 
 * multiple servers, so a generic SettingsReader might be a good idea.
 * 
 * @author Håvard Slettvold
 *
 */

public class SettingsReader {
	
	static Logger logger = Logger.getLogger(SettingsReader.class);
	
	public static ArrayList<Settings> readSettings(String settings_file) {
		ArrayList<Settings> settingsList = new ArrayList<Settings>();
			
		File file = new File(settings_file);
			
		try {
				
			if(!file.exists()) {
				return null;
			}
				
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = null;

			Settings settings = null;
				
			while((line = reader.readLine()) != null) {
				line = line.trim();
				// In the config files "#" is used to comment
				if(line.startsWith("#")) continue;
				
				// If a new block is found, add the current buildup of Settings to the list and make a new one
				if(line.matches("^\\[.*\\]$")) {
					if(settings != null) {
						settingsList.add(settings);
					}
					settings = new Settings();
				}
				
				// No new block; keep adding settings
				else {
					if(settings != null) {
						if(line.contains("=")) {
							String[] lineParts = line.split("=");
							// This check basically means that if "=" is found more than once on a line or a 
							// settings field is empty, it will produce an error and shut down.
							if (lineParts.length > 2 || lineParts.length < 2) {
								logger.warn("Malformed settings file: '"+settings_file+"' on line: '"+line+"'");
							}
							else {
								settings.addSetting(lineParts[0], lineParts[1]);
							}
						}
					}
				}
			}
			if(settings != null) settingsList.add(settings);
				
			reader.close();
			
		} catch (IOException e) {
            logger.error("I/O error", e.getCause());
		}
			
		return settingsList;
	}
	
}
