package edu.java.bot;

import edu.java.bot.message_handler.Link;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import java.net.URI;

public class LinkTest {
    @Test
    public void testParse() {
        URI url = URI.create("https://www.example.com:8080/path/to/resource?param=value#fragment");
        Link link = Link.parse(url);

        Assertions.assertEquals("https", link.scheme());
        Assertions.assertEquals("www.example.com", link.host());
        Assertions.assertEquals(8080, link.port());
        Assertions.assertEquals("/path/to/resource", link.path());
        Assertions.assertEquals("param=value", link.query());
        Assertions.assertEquals("fragment", link.fragment());
    }
}
