package megawave.antispam.configuration;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class DefaultConfiguration<T> {
    final Path path;
    protected T type;
    final Class<T> tClass;

    protected DefaultConfiguration(Path path, Class<T> tClass) throws IOException {
        this.path = path;
        this.tClass =  tClass;
        type = load();

    }

    public T load() throws IOException {
        return new Yaml().loadAs(Files.newInputStream(path), tClass);

    }

}
