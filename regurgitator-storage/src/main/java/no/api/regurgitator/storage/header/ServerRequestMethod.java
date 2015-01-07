package no.api.regurgitator.storage.header;

public enum ServerRequestMethod {

    /**
     * The OPTIONS getMethod represents a request for information about the communication options available on the
     * request/response chain identified by the Request-URI. This getMethod allows the client to determine the
     * options and/or requirements associated with a resource, or the capabilities of a server, without implying a
     * resource action or initiating a resource retrieval.
     */
    OPTIONS,

    /**
     * The GET getMethod means retrieve whatever information (in the form of an entity) is identified by the
     * Request-URI.  If the Request-URI refers to a data-producing process, it is the produced data which shall be
     * returned as the entity in the response and not the source text of the process, unless that text happens to be
     * the output of the process.
     */
    GET,

    /**
     * The HEAD getMethod is identical to GET except that the server MUST NOT return a message-body in the
     * response.
     */
    HEAD,

    /**
     * The POST getMethod is used to request that the origin server accept the entity enclosed in the request as a
     * new subordinate of the resource identified by the Request-URI in the Request-Line.
     */
    POST,

    /**
     * The PUT getMethod requests that the enclosed entity be stored under the supplied Request-URI.
     */
    PUT,

    /**
     * The PATCH getMethod requests that a set of changes described in the request entity be applied to the resource
     * identified by the Request-URI.
     */
    PATCH,

    /**
     * The DELETE getMethod requests that the origin server delete the resource identified by the Request-URI.
     */
    DELETE,

    /**
     * The TRACE getMethod is used to invoke a remote, application-layer loop- back of the request message.
     */
    TRACE,

    /**
     * This specification reserves the getMethod name CONNECT for use with a proxy that can dynamically switch to
     * being a tunnel
     */
    CONNECT;

    /**
     * Find matching RequestMethod by String.
     *
     * @param method The string that will be used to find the correct RequestMethod.
     * @return The RequestMethod matching the given string. Defaults to RequestMethod.GET if no match.
     */
    public static ServerRequestMethod findRequestMethodFromString(String method) {
        ServerRequestMethod defaultRequestMethod = ServerRequestMethod.GET;
        if (method == null) {
            return defaultRequestMethod;
        }
        for (ServerRequestMethod requestMethod : ServerRequestMethod.values()) {
            if (requestMethod.name().equals(method)) {
                return requestMethod;
            }
        }
        return defaultRequestMethod;
    }

}