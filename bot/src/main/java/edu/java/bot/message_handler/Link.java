package edu.java.bot.message_handler;

import java.net.URI;

public record Link(String scheme, String host, int port, String path, String query, String fragment) {
    public static Link parse(URI url) {
        return new Link(
            url.getScheme(),
            url.getHost(),
            url.getPort(),
            url.getPath(),
            url.getQuery(),
            url.getFragment()
        );
    }
}
