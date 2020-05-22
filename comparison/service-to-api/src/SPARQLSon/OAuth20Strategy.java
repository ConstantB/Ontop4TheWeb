package SPARQLSon;


import java.util.HashMap;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

public class OAuth20Strategy implements GetJSONStrategy {

	OAuthService service;
	Token accessToken;
	String token;

	public OAuth20Strategy() {
		
	}

	public OAuthRequest createOAuthRequest(String url) {
		OAuthRequest request = new OAuthRequest(Verb.GET, url);
		System.out.println("REQUEST CREATED: " + request);
		return request;
	}

	private String sendRequestAndGetResponse(OAuthRequest request) {
		// System.out.println("Service: " + this.service);
		// System.out.println("Querying " + request.getSanitizedUrl() + " ...");
		this.service.signRequest(this.accessToken, request);
		//System.out.println("ACCESS TOKEN " + this.accessToken);
		//System.out.println("REQUEST " + request);
		Response response = request.send();
		System.out.println(this.accessToken.toString());
		System.out.println(response.getBody());
		return response.getBody();
	}

	public String readURL(String url) {
		// System.out.println("Read URL: " + url);
		OAuthRequest request = createOAuthRequest(url);
		return sendRequestAndGetResponse(request);
	}

	@Override
	public void set_params(HashMap<String, String> params) {
		this.token = params.get("token");
		
		this.service =
				new ServiceBuilder().provider(TwoStepOAuth.class).apiKey(this.token).build();
	}

}
