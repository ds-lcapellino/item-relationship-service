//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.irs.semanticshub;

import static net.catenax.irs.configuration.RestTemplateConfig.OAUTH_REST_TEMPLATE;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Semantics Hub Rest Client
 */
interface SemanticsHubClient {

    /**
     * Return Json Schema of requsted model by urn
     * @param urn of the model
     * @return Json Schema
     */
    Map<String, Object> getModelJsonSchema(String urn);

}

/**
 * Semantics Hub Rest Client Stub used in local environment
 */
@Service
@Profile({ "local", "stubtest" })
class SemanticsHubClientLocalStub implements SemanticsHubClient {

    @Override
    public Map<String, Object> getModelJsonSchema(final String urn) {
        final Map<String, Object> schemaMap = new HashMap<>();
        schemaMap.put("$schema", "http://json-schema.org/draft-07/schema#");
        schemaMap.put("type", "integer");

        return schemaMap;
    }
}

/**
 * Semantics Hub Rest Client Implementation
 */
@Service
@Profile({ "!local && !stubtest" })
class SemanticsHubClientImpl implements SemanticsHubClient {

    private final RestTemplate restTemplate;
    private final String semanticsHubUrl;

    /* package */ SemanticsHubClientImpl(@Qualifier(OAUTH_REST_TEMPLATE) final RestTemplate restTemplate,
            @Value("${semanticsHub.url:}") final String semanticsHubUrl) {
        this.restTemplate = restTemplate;
        this.semanticsHubUrl = semanticsHubUrl;
    }

    @Override
    public Map<String, Object> getModelJsonSchema(final String urn) {
        final UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(semanticsHubUrl);
        uriBuilder.path("/models/").path(urn).path("/json-schema");

        return restTemplate.getForObject(uriBuilder.build().toUri(), Map.class);
    }
}
