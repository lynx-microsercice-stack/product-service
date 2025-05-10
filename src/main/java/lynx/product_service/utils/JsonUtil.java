package lynx.product_service.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lynx.product_service.exception.GeneralException;

public class JsonUtil {
    private JsonUtil() {}
    private static final ObjectMapper om = new ObjectMapper();

    public static String prettyPrinter(Object obj) {
        try {
            return om.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            throw new GeneralException("Failed to convert object to JSON");
        }
    }
}
