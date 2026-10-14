package HATEOAS.Utils;

public class LinkUtil {
    public static String createLinkHeader(String uri, String rel) {
        return "<" + uri + ">; rel=\"" + rel + "\"";
    }
}