package com.coffee.sashie.horadriccube;

import ch.njol.skript.Skript;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.*;
import ch.njol.skript.lang.function.Functions;
import ch.njol.skript.lang.function.JavaFunction;
import com.coffee.sashie.horadriccube.utils.ElementType;
import org.bukkit.event.Event;

import java.util.HashSet;
import java.util.TreeMap;
import static com.coffee.sashie.horadriccube.utils.HoradricLogger.*;


/**
 * This abstract class serves as a utility for registering various Skript elements.
 * It provides methods for registering effects, expressions, properties, events, conditions, sections, and functions.
 * Each method logs the registration of the respective element to a TreeMap for tracking purposes.
 */

@SuppressWarnings("unused")
abstract class Elements {
    /**
     * TreeMap to store the registration of different Skript elements.
     * The key is the element type (EFFECTS, EXPRESSIONS, etc.) and the value is a HashSet of registered elements.
     */
    protected static final TreeMap<ElementType, HashSet<String>> __map = new TreeMap<>();


    /**
     * Registers a new effect with Skript.
     *
     * @param c       The class of the effect.
     * @param patterns The patterns to register the effect with.
     * @param <E>     The type of the effect.
     */
    static <E extends Effect> void registerEffect(Class<E> c, String... patterns) {
        __map.getOrDefault(ElementType.EFFECTS, new HashSet<>()).add(c.getSimpleName());
        for (int i = 0; i < patterns.length; i++) patterns[i] = "[" + HoradricCube.PROJECT_PREFIX +"] " + patterns[i];
        Skript.registerEffect(c, patterns);
    }
    /**
     * Registers a new property expression with Skript.
     *
     * @param <T> The type of the expression's result.
     * @param expressionClass The class of the expression.
     * @param type The class of the expression's result.
     * @param property The name of the property.
     * @param fromType The type of the object from which the property is accessed.
     *
     * @since 1.0.0
     */
    static <T> void registerProperty(Class<? extends Expression<T>> expressionClass, Class<T> type, String property, String fromType) {
        __map.get(ElementType.EXPRESSIONS).add(expressionClass.getSimpleName() + "_" + property + "_" + fromType);
        Skript.registerExpression(expressionClass, type, ExpressionType.PROPERTY, "[the] " + property + " of %" + fromType + "%", "%" + fromType + "%'[s] " + property);
    }

    /**
     * Registers a new expression with Skript.
     *
     * @param <E> The type of the expression.
     * @param <T> The type of the expression's result.
     * @param c       The class of the expression.
     * @param returnType The class of the expression's result.
     * @param type The type of the expression (e.g., PROPERTY, BOOLEAN, etc.).
     * @param patterns The patterns to register the expression with.
     *
     * @since 1.0.0
     */
    public static <E extends Expression<T>, T> void registerExpression(Class<E> c, Class<T> returnType, ExpressionType type, String... patterns) {
        __map.get(ElementType.EXPRESSIONS).add(c.getSimpleName());
        for (int i = 0; i < patterns.length; i++) {
            patterns[i] = "[" + HoradricCube.PROJECT_PREFIX + "] " + patterns[i];
        }
        Skript.registerExpression(c, returnType, type, patterns);
    }

    /**
     * Registers a new property expression with Skript.
     *
     * @param c the of the new simple property expression
     * @param returnType The class of the expression's result.
     * @param property The name of the property.
     * @param fromType The type of the object from which the property is accessed.
     *
     * @since 1.0.0
     *
     * @apiNote This method is used to register custom property expression with Skript.
     *          The section is added to the internal map of registered simple property expression,
     *          and then registered with the Skript engine using the PropertyExpression.register method.
     *          The patterns provided are prefixed with the project prefix to ensure uniqueness.
     *
     * @throws IllegalArgumentException If the provided class is null or the patterns array is null or empty.
     *
     * @see Skript
     * @see PropertyExpression
     */
    public static <T> void registerPropertyExpression(Class<? extends Expression<T>> c, Class<T> returnType, String property, String fromType) {
        __map.get(ElementType.EXPRESSIONS).add(c.getSimpleName() + "_" + property + "_" + fromType);
        PropertyExpression.register(c, returnType, property, fromType);
    }

    /**
     * Registers a new simple property expression with Skript.
     *
     * @param c the of the new simple property expression
     * @param returnType The class of the expression's result.
     * @param property The name of the property.
     * @param fromType The type of the object from which the property is accessed.
     *
     * @since 1.0.0
     *
     * @apiNote This method is used to register custom simple property expression with Skript.
     *          The section is added to the internal map of registered simple property expression,
     *          and then registered with the Skript engine using the PropertyExpression.register method.
     *          The patterns provided are prefixed with the project prefix to ensure uniqueness.
     *
     * @throws IllegalArgumentException If the provided class is null or the patterns array is null or empty.
     *
     * @see Skript
     * @see PropertyExpression
     */
    public static <T> void registerSimplePropertyExpression(Class<? extends Expression<T>> c, Class<T> returnType, String property, String fromType) {
        __map.get(ElementType.EXPRESSIONS).add(c.getSimpleName() + "_" + property + "_" + fromType);
        PropertyExpression.register(c, returnType, property, fromType);
    }


    /**
     * Registers a new event with Skript.
     *
     * @param name The name of the event.
     * @param c The class of the event.
     * @param event The class of the event's result.
     * @param description The description of the event.
     * @param examples The example usage of the event.
     * @param version The version of the event.
     * @param patterns The patterns to register the section with.
     *
     * @since 1.0.0
     *
     * @apiNote This method is used to register custom event with Skript.
     *          The section is added to the internal map of registered event,
     *          and then registered with the Skript engine using the Skript.registerEvent method.
     *          The patterns provided are prefixed with the project prefix to ensure uniqueness.
     *
     * @throws IllegalArgumentException If the provided class is null or the patterns array is null or empty.
     *
     * @see Skript
     * @see SkriptEvent
     * @see Event
     */

    public static void registerEvent(String name, Class<? extends SkriptEvent> c, Class<? extends Event> event, String description, String examples, String version, String... patterns) {
        __map.get(ElementType.EVENTS).add(name + c.getSimpleName());
        for (int i = 0; i < patterns.length; i++) patterns[i] = "["+ HoradricCube.PROJECT_PREFIX+"] " + patterns[i];
        Skript.registerEvent(name, c, event, patterns)
                .since(version)
                .examples(examples)
                .description(description);
    }

    /**
     * Registers a new condition with Skript.
     *
     * @param <E> The type of the condition.
     * @param patterns The patterns to register the section with.
     *
     * @since 1.0.0
     *
     * @apiNote This method is used to register custom condition with Skript.
     *          The section is added to the internal map of registered condition,
     *          and then registered with the Skript engine using the Skript.registerCondition method.
     *          The patterns provided are prefixed with the project prefix to ensure uniqueness.
     *
     * @throws IllegalArgumentException If the provided class is null or the patterns array is null or empty.
     *
     * @see Skript
     * @see Condition
     */
    public static <E extends Condition> void registerCondition(Class<E> c, String... patterns) {
        __map.get(ElementType.CONDITIONS).add(c.getSimpleName());
        for (int i = 0; i < patterns.length; i++) patterns[i] = "["+ HoradricCube.PROJECT_PREFIX+"] " + patterns[i];
        Skript.registerCondition(c, patterns);
    }

    /**
     * Registers a new section with Skript.
     *
     * @param <E> The type of the section.
     * @param requestClass The class of the section.
     * @param patterns The patterns to register the section with.
     *
     * @since 1.0.0
     *
     * @apiNote This method is used to register custom sections with Skript.
     *          The section is added to the internal map of registered sections,
     *          and then registered with the Skript engine using the Skript.registerSection method.
     *          The patterns provided are prefixed with the project prefix to ensure uniqueness.
     *
     * @throws IllegalArgumentException If the provided class is null or the patterns array is null or empty.
     *
     * @see Skript
     * @see Section
     */
    public static <E extends Section> void registerSection(Class<E> requestClass, String... patterns) {
        __map.get(ElementType.SECTIONS).add(requestClass.getSimpleName());
        for (int i = 0; i < patterns.length; i++) {
            patterns[i] = "[" + HoradricCube.PROJECT_PREFIX + "] " + patterns[i];
        }
        Skript.registerSection(requestClass, patterns);
    }

    /**
     * Registers a new Java function with Skript.
     *
     * @param fn The Java function to register.
     * @return The registered Java function.
     *
     * @since 1.0.0
     *
     * @apiNote This method is used to register custom Java functions with Skript.
     *          The function is added to the internal map of registered functions,
     *          and then registered with the Skript engine using the Functions.registerFunction method.
     *
     * @throws IllegalArgumentException If the provided function is null.
     *
     * @see JavaFunction
     * @see Functions
     */
    public static JavaFunction<?> registerFunction(JavaFunction<?> fn) {
        __map.get(ElementType.FUNCTIONS).add(fn.toString());
        return Functions.registerFunction(fn);
    }

    public static void printEnabledElements() {
        __map.forEach((type, elements) -> info("&8&l - &7Registered " + coloredElement(type) + "&f " + elements.size()));
    }


}
