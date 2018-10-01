package com.etrade.eo.core.json.serializers;

/**
 * An interface to represent all the various type adapters that can be supplied to a
 * {@link com.google.gson.GsonBuilder} because... for some reason... Gson does not have one.
 */
public interface GsonTypeAdapter<T> {
    /**
     * Return the class type the adapter is for.
     * @return The type to adapt in Gson, e.g. BigDecimal.class
     */
    Class<T> getType();

    /**
     * At the time of writing, the appropriate Gson type adapter interfaces are the following.
     * <p><ul>
     *     <li>{@link com.google.gson.TypeAdapter}
     *     <li>{@link com.google.gson.InstanceCreator}
     *     <li>{@link com.google.gson.JsonDeserializer}
     *     <li>{@link com.google.gson.JsonSerializer}
     * </ul></p>
     *
     * @see <a href="https://google.github.io/gson/apidocs/com/google/gson/GsonBuilder.html#registerTypeAdapter-java.lang.reflect.Type-java.lang.Object-">
     *     https://google.github.io/gson/apidocs/com/google/gson/GsonBuilder.html</a>
     * @return A Gson type adapter
     */
    Object getTypeAdapter();
}
