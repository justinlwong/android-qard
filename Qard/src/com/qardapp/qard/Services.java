package com.qardapp.qard;

import android.app.Activity;

import com.qardapp.qard.friends.profile.services.DefaultServiceManager;
import com.qardapp.qard.friends.profile.services.EmailServiceManager;
import com.qardapp.qard.friends.profile.services.FacebookServiceManager;
import com.qardapp.qard.friends.profile.services.PhoneServiceManager;
import com.qardapp.qard.friends.profile.services.ServiceManager;
import com.qardapp.qard.friends.profile.services.WhatsAppServiceManager;

public enum Services {
	PHONE("Phone Number", 1, R.drawable.service_phone, R.drawable.service_phone_disabled,
			null, null, null, null, null, null, null, null),
	SKYPE("Skype", 2, R.drawable.service_skype, R.drawable.service_skype_disabled,
			null, null, null, null, null, null, null, null),

	EMAIL("E-mail", 3, R.drawable.service_email, R.drawable.service_email_disabled,
			null, null, null, null, null, null, null, null),		

	WHATSAPP("WhatsApp", 4, R.drawable.service_whatsapp, R.drawable.service_whatsapp_disabled,
			null, null, null, null, null, null, null, null),			
			
	FACEBOOK("Facebook", 5, R.drawable.service_facebook, R.drawable.service_facebook_disabled,
			null, null, null, null, null, null, null, null),
			
	GOOGLEPLUS("Google+", 6, R.drawable.service_gplus, R.drawable.service_gplus_disabled,
			null, null, "https://www.googleapis.com/plus/v1/people/me?access_token=", "1096476372365.apps.googleusercontent.com", null, "http://localhost", null, null),

	FOURSQUARE("Foursquare", 7, R.drawable.service_foursquare, R.drawable.service_foursquare_disabled,
					"id", "http://api.foursquare.com/", "https://api.foursquare.com/v2/users/self?oauth_token=", "RG1XWTHCCDCD0ASO0OU5VF1STSTQWEYGUZRWHBXK14J2CMLQ", "N0CKV2ZY2NKNJ2FJKC4PA1KN2EWHVBG4KH24NGAYNSNFOGQG", "oauth://foursquare", null, "https://api.foursquare.com/v2/users/"),
	
	LINKEDIN("LinkedIn", 8, R.drawable.service_linkedin, R.drawable.service_linkedin_disabled,
			"id", "http://api.linkedin.com/", "http://api.linkedin.com/v1/people/~:(id,first-name,last-name,industry,email-address,site-standard-profile-request)?format=json", "a3dolo9iq9fd", "hKyo855beQ9ggLXZ","oauth://linkedin","r_fullprofile r_emailaddress w_messages", "http://api.linkedin.com/v1/people/~/mailbox"),
							
	TWITTER("Twitter", 9, R.drawable.service_twitter, R.drawable.service_twitter_disabled,
			"id_str", "http://api.twitter.com/", "https://api.twitter.com/1.1/account/verify_credentials.json", "ZtN4h8Ps9cyNIadDK6cg", "RCE4Gls0cKcdpe19VYLOC4rg7zHcgV0n5f6rPnaV4VU", "oauth://twitter", null, "https://api.twitter.com/1.1/friendships/create.json"),
			
	INSTAGRAM("Instagram", 10, R.drawable.service_instagram, R.drawable.service_instagram_disabled,
			"id", "http://api.instagram.com/", "https://api.instagram.com/v1/users/self/?access_token=", "b4ff6552928a495a983827d421a09473", "4f2ec0d2154541da9cd158b42752f706", "oauth://instagram", "basic", null),

	FLICKR("Flickr", 11, R.drawable.service_flickr, R.drawable.service_flickr_disabled,
			"nsid", "http://api.flickr.com/", "http://api.flickr.com/services/rest/?method=flickr.urls.getUserProfile&format=json&nojsoncallback=1", "13d1fc4d1d2f7924916217597ed71ce8", "af718ebdf5f25310", "oauth://flickr", null, null),
	
	TUMBLR("Tumblr", 12, R.drawable.service_tumblr, R.drawable.service_tumblr_disabled,
			null, "http://api.tumblr.com/v2/", "http://api.tumblr.com/v2/user/info", "ly15mymFMKc75b95H1PTX5R7A4WoOY2ywXpR3UvpvimhdmzmDg", "jmJATGVoGWcGWpf0TtuyFf9bCwxybsvWziI7uT4Y63mebBisoG", "oauth://tumblr", null, null),
	
	YOUTUBE("Youtube", 13, R.drawable.service_youtube, R.drawable.service_youtube_disabled,
			null, null, "https://gdata.youtube.com/feeds/api/users/default?access_token=", "1096476372365.apps.googleusercontent.com", null, "http://localhost", null, null),
			
	BLOGGER("Blogger", 14, R.drawable.service_blogger, R.drawable.service_blogger_disabled,
			null, null, "https://www.googleapis.com/blogger/v3/users/self/blogs", "1096476372365.apps.googleusercontent.com", null, "http://localhost", null, null),

	PINTEREST("Pinterest", 15, R.drawable.service_pinterest, R.drawable.service_pinterest_disabled,
			null, null, null, null, null, null, null, null),
			
	WEBPAGE("Web page", 16, R.drawable.service_nexcircle, R.drawable.service_nexcircle_disabled,
			null, null, null, null, null, null, null, null);

	//TEXT("Text", 2, R.drawable.whatsapp, R.drawable.whatsapp_gray, null, null, null, null, null, null, null, null),
	//ADDRESS("Address", 3, R.drawable.ic_launcher, R.drawable.ic_launcher_gray, null, null, null, null, null, null, null, null),
	//LINE("Line", 11, R.drawable.line, R.drawable.line_gray, null, null, null, null, null, null, null, null),

	
	public int id;
	public int priority;
	public String name;
	public int imageId;
	public int grayImageId;
	public String authURL;
	public String userQuery;
	public String apiKey;
	public String apiSecret;
	public String idFieldName;
	public String callbackURL;
	public String scope;
	public String addUser;

	private Services (String name, int id, int imageId, int grayImageId, String idFieldName, String authURL, String userQuery, String apiKey, String apiSecret, String callbackURL, String scope, String addUser) {
		this.name = name;
		this.id = id;
		this.priority = id;
		this.imageId = imageId;
		this.grayImageId = grayImageId;
		this.idFieldName = idFieldName;
		this.authURL = authURL;
		this.userQuery = userQuery;
		this.apiKey = apiKey;
		this.apiSecret = apiSecret;
		this.callbackURL = callbackURL;
		this.scope = scope;
		this.addUser = addUser;
		
	}
	
	public ServiceManager getManager(Activity a, BaseFragment bf ) {
		if (id == Services.PHONE.id){
		    return new PhoneServiceManager(a, bf);
		}  else if (id == Services.EMAIL.id) {
			return new EmailServiceManager(a);
		} else if (id == Services.WHATSAPP.id) {
			return new WhatsAppServiceManager(a);
		} else if (id == Services.FACEBOOK.id) {
            return new FacebookServiceManager(a);	
		} else if (id == Services.GOOGLEPLUS.id || id == Services.FOURSQUARE.id || id == Services.LINKEDIN.id || id == Services.TWITTER.id || id == Services.INSTAGRAM.id || id == Services.FLICKR.id || id == Services.TUMBLR.id || id == Services.YOUTUBE.id || id == Services.BLOGGER.id) {
			return new DefaultServiceManager(a, imageId, id);
		} 
		return null;
	}
	
}
