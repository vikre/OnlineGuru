package no.ntnu.online.onlineguru.plugin.plugins.die;

import no.fictive.irclib.event.container.Event;
import no.fictive.irclib.event.container.command.PrivMsgEvent;
import no.fictive.irclib.event.model.EventType;
import no.ntnu.online.onlineguru.plugin.control.EventDistributor;
import no.ntnu.online.onlineguru.plugin.model.Plugin;
import no.ntnu.online.onlineguru.plugin.model.PluginWithDependencies;
import no.ntnu.online.onlineguru.plugin.plugins.chanserv.control.ChanServ;
import no.ntnu.online.onlineguru.utils.WandRepository;


public class Die implements Plugin, PluginWithDependencies {

	private ChanServ chanserv;
	private String[] dependencies = new String[] { "ChanServ" };
	private WandRepository wandRepository;
	
	public String getDescription() {
		return "Dies.";
	}

	public void incomingEvent(Event e) {
		switch(e.getEventType()) {
			case PRIVMSG:
				PrivMsgEvent pme = (PrivMsgEvent)e;
				if(pme.isPrivateMessage()) {
					if(pme.getMessage().equalsIgnoreCase("die")) {
						if(chanserv.isNickLoggedIn(pme.getSender())) {
							wandRepository.quit(pme.getNetwork());
						} else {
							wandRepository.sendMessageToTarget(pme.getNetwork(), pme.getSender(), "You are not logged in.");
						}
					} 
				}
				break;
		}
	}

	public void addEventDistributor(EventDistributor eventDistributor) {
		eventDistributor.addListener(this, EventType.PRIVMSG);
	}

	public void addWand(WandRepository wandRepository) {
		this.wandRepository = wandRepository;
	}

	public String[] getDependencies() {
		return dependencies;
	}

	public void loadDependency(Plugin plugin) {
		if(plugin instanceof ChanServ) {
			chanserv = (ChanServ)plugin;
		}
	}
}