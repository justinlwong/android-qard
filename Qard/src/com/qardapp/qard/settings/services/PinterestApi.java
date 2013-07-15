package com.qardapp.qard.settings.services;
import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.OAuthConfig;
import org.scribe.utils.OAuthEncoder;
import org.scribe.utils.Preconditions;

/**
 * @author Julio Gutierrez
 * 
 *         Sep 6, 2012
 */
public class PinterestApi extends DefaultApi20
{
  private static final String AUTHORIZE_URL = "https://pinterest.com/oauth/authorize/?client_id=%s&redirect_uri=%s";

  private static final String SCOPED_AUTHORIZE_URL = AUTHORIZE_URL + "&scope=%s";

  @Override
  public String getAuthorizationUrl(OAuthConfig config)
  {
    Preconditions.checkValidUrl(config.getCallback(), "Must provide a valid url as callback. Pinterest does not support OOB");

    if (config.hasScope())
    {
      return String.format(SCOPED_AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()), OAuthEncoder.encode(config.getScope()));
    }
    else
    {
      return String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));
    }
  }

  @Override
  public String getAccessTokenEndpoint()
  {
    return "https://api.pinterest.com/v2/oauth/access_token";
  }

}