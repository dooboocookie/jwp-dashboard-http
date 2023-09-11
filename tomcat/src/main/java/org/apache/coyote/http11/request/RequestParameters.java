package org.apache.coyote.http11.request;

import org.apache.coyote.http11.header.Headers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.apache.coyote.http11.header.EntityHeader.CONTENT_TYPE;

public class RequestParameters {

    public static final String FORM_DATA_CONTENT_TYPE = "application/x-www-form-urlencoded";

    private final Map<String, String> values;

    private RequestParameters(final Map<String, String> values) {
        this.values = values;
    }

    public static RequestParameters of(final RequestLine requestLine,
                                       final Headers headers,
                                       final String body) {
        final String requestParameters = extractRequestParameters(requestLine, headers, body);
        final Map<String, String> requestQueryParameters = new HashMap<>();
        if (requestParameters == null || "".equals(requestParameters)) {
            return new RequestParameters(requestQueryParameters);
        }
        final String[] queryStringsNameAndValue = requestParameters.split("&");
        for (String queryString : queryStringsNameAndValue) {
            final String[] queryStringNameAndValue = queryString.split("=");
            final String name = queryStringNameAndValue[0];
            final String value = queryStringNameAndValue[1];
            requestQueryParameters.put(name, value);
        }
        return new RequestParameters(requestQueryParameters);
    }

    private static String extractRequestParameters(final RequestLine requestLine,
                                                   final Headers headers,
                                                   final String body) {
        final String queryString = requestLine.getQueryString();
        if (queryString != null && !"".equals(queryString)) {
            return queryString;
        }
        String value = headers.getValue(CONTENT_TYPE);
        if (FORM_DATA_CONTENT_TYPE.equalsIgnoreCase(value)) {
            return body;
        }
        return "";
    }

    public Optional<String> getValue(final String key) {
        return Optional.ofNullable(values.get(key));
    }

    public Map<String, String> getValues() {
        return values;
    }

    @Override
    public String toString() {
        return "RequestParameters{" +
                "requestParameters=" + values +
                '}';
    }
}
