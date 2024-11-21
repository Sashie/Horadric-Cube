package com.coffee.sashie.horadriccube.utils;

import ch.njol.skript.Skript;
import ch.njol.skript.effects.Delay;
import ch.njol.skript.events.bukkit.ScriptEvent;
import ch.njol.skript.events.bukkit.SkriptStopEvent;
import ch.njol.skript.lang.TriggerItem;
import com.coffee.sashie.horadriccube.HoradricCube;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

/**
 * Effects that extend this class are ran asynchronously. Next trigger item will
 * be ran in main server thread, as if there had been a delay before.
 * <p>
 * Majority of Skript and Minecraft APIs are not thread-safe, so be careful.
 */
public abstract class AsyncEffectOld extends Delay {

	@Override
	@Nullable
	protected TriggerItem walk(Event e) {
		debug(e, true);
		if (e.getClass().isAssignableFrom(SkriptStopEvent.class) || e.getClass().isAssignableFrom(ScriptEvent.class)) {	// Because a bukkit task can't be created on server stop and these are triggered then
			execute(e);
			if (getNext() != null)
				TriggerItem.walk(getNext(), e);
		} else {
			HoradricCube.getInstance().getSkriptAdapter().addDelayedEvent(e);
			Bukkit.getScheduler().runTaskAsynchronously(Skript.getInstance(), new Runnable() {
				@Override
				public void run() {
					execute(e); // Execute this effect
					if (getNext() != null) {
						Bukkit.getScheduler().runTask(Skript.getInstance(), new Runnable() {
							@Override
							public void run() { // Walk to next item synchronously
								TriggerItem.walk(getNext(), e);

							}
						});
					}
				}
			});
		}
		return null;
	}
}