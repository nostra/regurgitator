package no.api.regurgitator.storage.header;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tool methods for converting between Netty.io HttpHeaders and the internal Headers object.
 */
public class NettyHttpHeadersUtil {

    private NettyHttpHeadersUtil() {
        throw new UnsupportedOperationException();
    }

    /**
     * Convert Netty HttpHeaders to Headers object.
     *
     * @param httpHeaders Instance of an {@link io.netty.handler.codec.http.HttpHeaders} object.
     * @return Headers object with the values from the given httpHeaders object.
     */
    public static Headers convert(HttpHeaders httpHeaders) {
        Map<String, List<String>> map = new HashMap<>();
        for (Map.Entry<String, String> entry: httpHeaders.entries()) {
            if (map.containsKey(entry.getKey())) {
                map.get(entry.getKey()).add(entry.getValue());
            } else {
                List<String> values = new ArrayList<>();
                values.add(entry.getValue());
                map.put(entry.getKey(), values);
            }
        }
        return new Headers(map);
    }

    /**
     * Append all headers from headers object onto response object.
     * @param headers Headers object as loaded from storage.
     * @param response The Netty response that the headers should be added to.
     */
    public static void appendHeadersToResponse(Headers headers, HttpResponse response) {
        for (String header : headers.getHeaderNames()) {
            response.headers().add(header, headers.getHeaderValues(header));
        }
    }

}
