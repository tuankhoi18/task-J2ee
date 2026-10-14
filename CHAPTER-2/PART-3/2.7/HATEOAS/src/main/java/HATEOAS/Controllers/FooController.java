package HATEOAS.Controllers;

import HATEOAS.Events.ResourceCreated;
import HATEOAS.Events.SingleResourceRetrieved;
import HATEOAS.Models.Foo;
import HATEOAS.Services.IFooService;
import HATEOAS.Utils.LinkUtil;
import com.google.common.base.Preconditions;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriTemplate;

import java.net.URI;

@RestController
@RequestMapping(value = "/foos")
public class FooController {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private IFooService service;

    @GetMapping(value = "foos/{id}")
    public Foo findById(@PathVariable("id") Long id, HttpServletResponse response) {
        Foo resourceById = Preconditions.checkNotNull(service.findOne(id));

        eventPublisher.publishEvent(new SingleResourceRetrieved(this, response));
        return resourceById;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Foo resource, HttpServletResponse response) {
        Preconditions.checkNotNull(resource);
        Long newId = service.create(resource).getId();

        eventPublisher.publishEvent(new ResourceCreated(this, response, newId));
    }

    @GetMapping("/")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void adminRoot(final HttpServletRequest request, final HttpServletResponse response) {
        String rootUri = request.getRequestURL().toString();

        URI fooUri = new UriTemplate("{rootUri}{resource}").expand(rootUri, "foos");
        String linkToFoos = LinkUtil.createLinkHeader(fooUri.toASCIIString(), "collection");
        response.addHeader("Link", linkToFoos);
    }
}