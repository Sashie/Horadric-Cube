package com.coffee.sashie.horadriccube.skript;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Checker;
import ch.njol.util.Kleenean;
import me.sashie.skriptyaml.SkriptYaml;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Does YAML Path Exist")
@Description("Checks if one or more paths exist in a cached YAML file using said ID." +
		"\n  - First input is the path." +
		"\n  - Second input is the ID." +
		"\n  - If multiple paths are checked at once it will return false on the first one found to not exist.")
@Examples({
		"set skript-yaml value \"test.test\" from \"config\" to \"test\"",
		"set skript-yaml value \"test2.test2\" from \"config\" to \"test\"",
		" ",
		"yaml path \"test.test\" and \"test2.test2\" in \"config\" exists:",
		"\tbroadcast \"this works\"",
		"yaml path \"test.test\" and \"boop.boop\" in \"config\" exists:",
		"\tbroadcast \"this will fail\""
})
@Since("1.2.1")
public class CondYamlPathExists extends Condition {

	static {
		Skript.registerCondition(CondYamlPathExists.class, 
				"[skript-]y[a]ml [(node|path)[s]] %strings% (of|in|from) %string% exists", 
				"[skript-]y[a]ml [(node|path)[s]] %strings% (of|in|from) %string% does(n't| not) exist");
	}

	private Expression<String> path;
	private Expression<String> file;

	@Override
	public boolean check(final Event event) {
		return path.check(event, new Checker<String>() {
			@Override
			public boolean check(final String s) {
				if (!SkriptYaml.YAML_STORE.containsKey(file.getSingle(event)))
					return false;
				if (path.isSingle())
					return SkriptYaml.YAML_STORE.get(file.getSingle(event)).getAllKeys().contains(path.getSingle(event));
				else {
					boolean check;
					for (String p : path.getAll(event)) {
						check = SkriptYaml.YAML_STORE.get(file.getSingle(event)).getAllKeys().contains(p);
						if (!check) {
							return false;
						}
					}
					return true;
				}
			}
		}, isNegated());
	}

	@Override
	public String toString(final @Nullable Event event, final boolean debug) {
		return "yaml path " + path.toString(event, debug) + " in " +  file.toString(event, debug) + (isNegated() ? (path.isSingle() ? " does" : " do") + " not exist" : "exist");
	}

	@SuppressWarnings({"unchecked"})
	@Override
	public boolean init(final Expression<?>[] exprs, final int matchedPattern, final Kleenean isDelayed, final ParseResult parseResult) {
		path = (Expression<String>) exprs[0];
		file = (Expression<String>) exprs[1];
		setNegated(matchedPattern == 1);
		return true;
	}
}