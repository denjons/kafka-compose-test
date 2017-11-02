package org.kafka.test.client;


import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;

/**
 * Client for basic post and get requests
 *
 * @author dennisjonsson
 *
 */
public class BasicClient {

    Logger logger = Logger.getLogger(BasicClient.class);

    /**
     * Get request
     *
     * @param url
     *            url
     * @param accept
     *            accept header
     * @param headers
     *            additional headers
     * @return Response object
     */
    public Response getFor(String url, MediaType accept, MultivaluedMap<String, Object> headers) {

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(url);

        Response requestResult;
        if (headers == null) {
            requestResult = target.request(accept).headers(headers).get();
        } else {
            requestResult = target.request(accept).get();
        }

        System.out.println("Got response: " + requestResult.getStatus());
        System.out.println(requestResult.toString());

        return requestResult;
    }

    /**
     * Get request
     *
     * @param url
     *            url
     * @param accept
     *            accpet-header
     * @return Response object
     */
    public Response getFor(String url, MediaType accept) {
        return getFor(url, accept, null);
    }

    /**
     * Post request
     *
     * @param body
     *            request body
     * @param url
     *            url
     * @param accept
     *            accept-header
     * @param headers
     *            other headers
     * @return Response object
     */
    public Response postFor(String body, String url, MediaType accept, MediaType produce, MultivaluedMap<String, Object> headers) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(url);

        Response requestResult;

        logger.info("posting: " + body);

        if (headers != null) {
            requestResult = target.request(accept).headers(headers)
                    .post(Entity.entity(body, produce));
        } else {
            requestResult = target.request(accept).post(Entity.entity(body, produce));
        }

        System.out.println("Got response: " + requestResult.getStatus());
        System.out.println(requestResult.toString());

        return requestResult;
    }

    /**
     * get request
     *
     * @param body
     *            post body
     * @param url
     *            url
     * @param accept
     *            accept-header
     * @return Response object
     */
    public Response postFor(String body, String url, MediaType accept, MediaType produce) {
        return postFor(body, url, accept, produce ,null);

    }
}
