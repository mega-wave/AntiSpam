package megawave.antispam.configuration;

import java.util.List;

public interface Configuration {

    Object get(String path);

    String getString(String path);

    Integer getInt(String path);

    Long getLong(String path);

    Double getDouble(String path);

    List<Object> getList(String path);

    List<String> getListString(String path);

}