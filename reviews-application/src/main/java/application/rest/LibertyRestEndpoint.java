/*******************************************************************************
 * Copyright (c) 2017 Istio Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package application.rest;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.StringReader;

@Path("/")
public class LibertyRestEndpoint extends Application {

    // HTTP headers to propagate for distributed tracing are documented at
    // https://istio.io/docs/tasks/telemetry/distributed-tracing/overview/#trace-context-propagation
    private final static String[] headers_to_proagate = {"x-request-id","x-b3-traceid","x-b3-spanid","x-b3-sampled","x-b3-flags",
      "x-ot-span-context","x-datadog-trace-id","x-datadog-parent-id","x-datadog-sampled", "end-user","user-agent"};

    private String getJsonResponse (String productId, int starsReviewer1, int starsReviewer2) {
    	String result = "{";
    	result += "\"id\": \"" + productId + "\",";
    	result += "\"reviews\": [";

    	// reviewer 1:
    	result += "{";
    	result += "  \"reviewer\": \"Reviewer1\",";
    	result += "  \"text\": \"An extremely entertaining play by Shakespeare. The slapstick humour is refreshing!\"";
    	result += "},";
    	
    	// reviewer 2:
    	result += "{";
    	result += "  \"reviewer\": \"Reviewer2\",";
    	result += "  \"text\": \"Absolutely fun and entertaining. The play lacks thematic depth when compared to other plays by Shakespeare.\"";
    	result += "}";
    	
    	result += "]";
    	result += "}";

    	return result;
    }
    
    @GET
    @Path("/health")
    public Response health() {
        return Response.ok().type(MediaType.APPLICATION_JSON).entity("{\"status\": \"Reviews is healthy\"}").build();
    }

    @GET
    @Path("/reviews/{productId}")
    public Response bookReviewsById(@PathParam("productId") int productId, @Context HttpHeaders requestHeaders) {
      String jsonResStr = getJsonResponse(Integer.toString(productId), -1, -1);
      return Response.ok().type(MediaType.APPLICATION_JSON).entity(jsonResStr).build();
    }
}
