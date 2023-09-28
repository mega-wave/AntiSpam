package megawave.antispam.configuration;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class ConfigurationImp extends DefaultConfiguration<Map> implements Configuration {


    public ConfigurationImp(Path path) throws IOException {
        super(path, Map.class);

    }

    public Object get(String path) {
        return type.get(path);
    }

    @Override
    public String getString(String path) {
        return (String) get(path);
    }

    @Override
    public Integer getInt(String path) {
        return (Integer) get(path);
    }

    @Deprecated
    @Override
    public Long getLong(String path) {
        return (Long) type.get(path);
    }

    @Override
    public Double getDouble(String path) {
        return(Double) get(path);
    }

    @Override
    public List getList(String path) {
        return (List) get(path);
    }

    @Override
    public List<String> getListString(String path) {
        return (List<String>) getList(path);
    }


}
