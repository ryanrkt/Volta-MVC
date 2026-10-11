package volta.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class TypeConverter {

    private static final Map<Class<?>, Function<String, Object>> CONVERTERS = new HashMap<>();

    static {
        // String
        CONVERTERS.put(String.class, value -> value);

        // Integer (primitif et objet)
        Function<String, Object> intConverter = value -> 
            (value != null && !value.trim().isEmpty()) ? Integer.parseInt(value.trim()) : 0;
        CONVERTERS.put(int.class, intConverter);
        CONVERTERS.put(Integer.class, intConverter);

        // Double (primitif et objet)
        Function<String, Object> doubleConverter = value -> 
            (value != null && !value.trim().isEmpty()) ? Double.parseDouble(value.trim()) : 0.0;
        CONVERTERS.put(double.class, doubleConverter);
        CONVERTERS.put(Double.class, doubleConverter);

        // Boolean (primitif et objet)
        Function<String, Object> booleanConverter = value -> 
            value != null && (value.equalsIgnoreCase("true") || value.equals("1") || value.equalsIgnoreCase("on"));
        CONVERTERS.put(boolean.class, booleanConverter);
        CONVERTERS.put(Boolean.class, booleanConverter);

        // Long (primitif et objet)
        Function<String, Object> longConverter = value -> 
            (value != null && !value.trim().isEmpty()) ? Long.parseLong(value.trim()) : 0L;
        CONVERTERS.put(long.class, longConverter);
        CONVERTERS.put(Long.class, longConverter);
    }

    public static Object convert(String value, Class<?> targetType) {
        Function<String, Object> converter = CONVERTERS.get(targetType);
        
        if (converter != null) {
            try {
                return converter.apply(value);
            } catch (Exception e) {
                return getDefaultValue(targetType);
            }
        }
        
        return null;
    }

    private static Object getDefaultValue(Class<?> targetType) {
        if (targetType.equals(int.class) || targetType.equals(Integer.class)) return 0;
        if (targetType.equals(double.class) || targetType.equals(Double.class)) return 0.0;
        if (targetType.equals(boolean.class) || targetType.equals(Boolean.class)) return false;
        if (targetType.equals(long.class) || targetType.equals(Long.class)) return 0L;
        return null;
    }
}