package no.ntnu.online.onlineguru.plugin.plugins.urlhandler;

import java.util.Scanner;

import no.fictive.irclib.event.container.Event;
import no.fictive.irclib.event.container.command.PrivMsgEvent;
import no.fictive.irclib.event.model.EventType;
import no.ntnu.online.onlineguru.plugin.control.EventDistributor;
import no.ntnu.online.onlineguru.plugin.model.Plugin;
import no.ntnu.online.onlineguru.utils.Wand;

public class URLHandler implements Plugin {
	
	private Wand wand;
	
	public URLHandler() {
		
	}

	public void addEventDistributor(EventDistributor eventDistributor) {
		eventDistributor.addListener(this, EventType.PRIVMSG);
	}

	public void addWand(Wand wand) {
		this.wand = wand;
	}

	public String[] getDependencies() {
		return null;
	}

	public String getDescription() {
		return "Returns information about an URL.";
	}

	public void incomingEvent(Event e) {
		PrivMsgEvent pme = (PrivMsgEvent)e;
		
		if(pme.isChannelMessage()) {
			String channel = pme.getTarget();
			
			Scanner scanner = new Scanner(pme.getMessage());
			String message;
			
			while(scanner.hasNext()) {
				message = scanner.next();
				if(message.startsWith("http") || message.startsWith("www")) {
					if(message.startsWith("www")) {
						message = "http://" + message;
					}
					if(message.startsWith("http://open.spotify.com/")) {
						return;
					}
					new Entry(wand, pme.getNetwork(), message, channel);
				}
			}
		}
	}

	public void loadDependency(Plugin plugin) {
		//Not needed
	}
}
