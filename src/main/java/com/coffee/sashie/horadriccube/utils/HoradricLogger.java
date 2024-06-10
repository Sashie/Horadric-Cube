package com.coffee.sashie.horadriccube.utils;

import ch.njol.skript.config.Node;
import ch.njol.skript.util.Version;
import com.coffee.sashie.horadriccube.HoradricCube;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.MissingResourceException;
import java.util.logging.Logger;

@SuppressWarnings("unused")
public class HoradricLogger extends Logger {
    /**
     * Protected method to construct a logger for a named subsystem.
     * <p>
     * The logger will be initially configured with a null Level
     * and with useParentHandlers set to true.
     *
     * @param name               A name for the logger.  This should
     *                           be a dot-separated name and should normally
     *                           be based on the package name or class name
     *                           of the subsystem, such as java.net
     *                           or javax.swing.  It may be null for anonymous Loggers.
     * @param resourceBundleName name of ResourceBundle to be used for localizing
     *                           messages for this logger.  May be null if none
     *                           of the messages require localization.
     * @throws MissingResourceException if the resourceBundleName is non-null and
     *                                  no corresponding resource can be found.
     */
    protected HoradricLogger(String name, String resourceBundleName) {
        super(name, resourceBundleName);
    }

    public static HoradricLogger getLogger(String name) {
        return new HoradricLogger(name, null);
    }

    /**
     * Overrides the info method of Logger to customize the console output.
     * It replaces the "[NBTAPI]" string with a color-coded version and filters out certain messages.
     *
     * @param msg The message to be logged.
     */
    @Override
    public void info(String msg) {
        if (msg.contains("[NBTAPI]")) {
            msg = msg.replace("[NBTAPI]", "&#adfa6eN&#53db88B&#00b797T&#009294A&#006c7eP&#2a4858I &r");
        }
        if (msg.contains("gson") || (msg.contains("Trying to find NMS support") || (msg.contains("bStats")))) {
            return;
        }

        Bukkit.getConsoleSender().sendMessage(ColorWrapper.translate(HoradricCube.PROJECT_PREFIX + msg));
    }

    /**
     * Overrides the info method of Logger to customize the console output.
     * @param msg The message.
     * @param times how many times the message should be repeated.
     * @return The message
     */
    @SuppressWarnings("SameParameterValue")
    static String times(final String msg, int times) {
        return String.valueOf(msg).repeat(Math.max(0, times));
    }

    static String formatString(String msg, Object ...args) {
        return String.format(msg, args);
    }

    public static String formatString(String m, boolean c, Object... a) {
        return c ? formatString(m + times("%s", a.length), a) : formatString(m, a);
    }

    static Component convert(final String msg, Object ...args) {
        return ColorWrapper.translate(formatString(msg, args));
    }


    @SuppressWarnings("SameParameterValue")
    static void error(Throwable throwable, CommandSender sender, Node node, Object ...args) {
        if (sender == null) sender = Bukkit.getConsoleSender();
        sender.sendMessage("");
        String msg_ = formatString("&4%s &c&lHoradricCube error handling &4%s", times("-", 27), times("-", 27));
        sender.sendMessage(convert(msg_));
        sender.sendMessage(convert("  &c&lReason -> &e%s", throwable.getLocalizedMessage()));
        if (node != null) sender.sendMessage(convert("  &c&lSkript-Node -> &e%s", node));
        if (args != null && args.length > 0 && args[0] != null)
            sender.sendMessage(convert("  &cMessage -> &e%s", formatString("", true, args)));
        sender.sendMessage(convert(times("&4-", msg_.length() - 8)));

        sender.sendMessage("");
        int i = throwable.getStackTrace().length;
        for (var st : throwable.getStackTrace()) {
            int line = st.getLineNumber();
            String cls = st.getFileName();
            String msg = st.getMethodName();
            sender.sendMessage(convert("  &e%s. &#eb6565%s &7(&f%s:%s&7)", i, msg, cls, line));
            i--;
        }
        sender.sendMessage(convert("&4%s &c&lEnd of error handling &4%s\n", times("-", 27), times("-", 27)));
    }

    public static void error(Throwable throwable, CommandSender sender, Object... arguments) {
        error(throwable, sender, null, arguments);
    }

    public static void error(Throwable throwable, Object... arguments) {
        error(throwable, null, arguments);
    }

    public static void simpleError(Object msg, Object... arguments) {
        simpleError(msg, null, arguments);
    }

    public static void simpleError(Object msg, CommandSender sender, Object... arguments) {
        if (sender == null) sender = Bukkit.getConsoleSender();
        if (msg instanceof Object[] objects) {
            var st = Arrays.stream(objects).map(Object::toString).toArray(String[]::new);
            sender.sendMessage(convert(HoradricCube.PROJECT_PREFIX + String.join(" ", st), arguments));
        } else {
            sender.sendMessage(convert(HoradricCube.PROJECT_PREFIX + msg.toString(), arguments));
        }
    }

    public static boolean versionError(Version userVersion, Version neededVersion, boolean disablePlugin, PluginManager manager, JavaPlugin plugin) {
        if (userVersion.isSmallerThan(new Version(2, 8, 0))) {
            Bukkit.getConsoleSender().sendMessage(convert("Running on older version than 2.8, please consider update"));
        }

        if (userVersion.isSmallerThan(neededVersion)) {
            Bukkit.getConsoleSender().sendMessage(convert(HoradricCube.PROJECT_PREFIX + "&cError " + "&c-----------------------------------------------------------------------------------------------------------"));
            Bukkit.getConsoleSender().sendMessage(convert(HoradricCube.PROJECT_PREFIX + "&cError " + "&cThis version doesn't support a older version of skript " + userVersion));
            Bukkit.getConsoleSender().sendMessage(convert(HoradricCube.PROJECT_PREFIX + "&cError " + "&eUse older version &fhttps://github.com/SkJsonTeam/skJson/releases/tag/2.8.6"));
            Bukkit.getConsoleSender().sendMessage(convert(HoradricCube.PROJECT_PREFIX + "&cError " + "Or update skript to &f2.7+"));
            Bukkit.getConsoleSender().sendMessage(convert(HoradricCube.PROJECT_PREFIX + "&cError " + "&c-----------------------------------------------------------------------------------------------------------"));
            if (disablePlugin) manager.disablePlugin(plugin);
            return false;
        }
        return true;
    }

    public static void warn(Object msg, CommandSender sender, Object... arguments) {
        if (sender == null) sender = Bukkit.getConsoleSender();
        if (msg instanceof Object[] objects) {
            var st = Arrays.stream(objects).map(Object::toString).toArray(String[]::new);
            sender.sendMessage(convert(HoradricCube.PROJECT_PREFIX + "&e&lWARN &e" + String.join(" ", st), arguments));
        } else {
            sender.sendMessage(convert(HoradricCube.PROJECT_PREFIX + "&e&lWARN &e" + msg.toString(), arguments));
        }
    }

    public static void warn(Object msg, Object... arguments) {
        warn(msg, null, arguments);
    }

    public static void info(Object msg, CommandSender sender, Object... arguments) {
        if (sender == null) sender = Bukkit.getConsoleSender();
        if (msg instanceof Object[] objects) {
            var st = Arrays.stream(objects).map(Object::toString).toArray(String[]::new);
            sender.sendMessage(convert(HoradricCube.PROJECT_PREFIX + "&r" + String.join(" ", st), arguments));
        } else {
            sender.sendMessage(convert(HoradricCube.PROJECT_PREFIX + "&r" + msg.toString(), arguments));
        }
    }

    public static void info(Object msg, Object... arguments) {
        info(msg, null, arguments);
    }

    public static String coloredElement(ElementType input) {
        return switch (input) {
            case EXPRESSIONS -> "&aExpressions";
            case EFFECTS -> "&bEffects";
            case EVENTS -> "&5Events";
            case SECTIONS -> "&fSections";
            case CONDITIONS -> "&4Conditions";
            case FUNCTIONS -> "&7Functions";
            case STRUCTURES -> "&9Structures";
        };
    }


}
