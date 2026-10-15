package HATEOAS.Services;

import HATEOAS.Models.Foo;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class IFooService {

    private final Map<Long, Foo> database = new ConcurrentHashMap<>();
    public Foo findOne(Long id) {
        return database.get(id);
    };

    public Foo create(Foo resource) {
        database.put(resource.getId(), resource);
        return resource;
    }
}