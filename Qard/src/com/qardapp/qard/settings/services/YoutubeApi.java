package com.qardapp.qard.settings.services;

import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuth20ServiceImpl;
import org.scribe.oauth.OAuthService;
import org.scribe.utils.OAuthEncoder;

import android.util.Log;
 
public class YoutubeApi extends DefaultApi20 {
 
  private static final String URL = "https://accounts.google.com/o/oauth2/auth?"+
	  "client_id=%s&" +
	  "redirect_uri=%s&"+
	  "scope=https://gdata.youtube.com&"+
	  "response_type=code&"+
	  "access_type=offline";
  
  @Override
  public Verb getAccessTokenVerb() {
    return Verb.POST;
  }
 
  @Override
  public String getAccessTokenEndpoint() {
    return "https://accounts.google.com/o/oauth2/token";
  }
 
  @Override
  public String getAuthorizationUrl(OAuthConfig config) {
    return String.format(URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));
  }
 
  /**
   * Not sure if it's a Scribe bug or an Instagram oddity. We have to send the 
   * various parameters in the POST body (Scribe sends them as query string)
   * and we must include the 'grant_type'.
   */
  @Override
  public OAuthService createService(final OAuthConfig config) {
    return new OAuth20ServiceImpl(this, config) {
      @Override
      public Token getAccessToken(Token requestToken, Verifier verifier) {
        OAuthRequest request = new OAuthRequest(getAccessTokenVerb(), getAccessTokenEndpoint());
        
        request.addBodyParameter("grant_type", "authorization_code");
        request.addBodyParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
        request.addBodyParameter(OAuthConstants.CODE, verifier.getValue());
        request.addBodyParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
        
        if (config.hasScope()) request.addBodyParameter(OAuthConstants.SCOPE, config.getScope());
        
        Response response = request.send();
        String token = ""; 
        Log.d("here",response.getBody());
        try {
			JSONObject mainObject = new JSONObject(response.getBody());
			token = mainObject.getString("access_token");
			Log.d("here",token);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Token t = new Token(token, "9999");
        return t;
        
        
      }
    };
  }
}