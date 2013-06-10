package com.qardapp.qard;

public enum Services {
	PHONE("Phone Number", 1, R.drawable.phone, R.drawable.phone_gray, null, null, null, null, null, null, null, null),
	TEXT("Text", 2, R.drawable.whatsapp, R.drawable.whatsapp_gray, null, null, null, null, null, null, null, null),
	ADDRESS("Address", 3, R.drawable.ic_launcher, R.drawable.ic_launcher_gray, null, null, null, null, null, null, null, null),
	FACEBOOK("Facebook", 4, R.drawable.facebook, R.drawable.facebook_gray, null, null, null, null, null, null, null, null),
	GMAIL("E-mail", 5, R.drawable.gmail, R.drawable.gmail_gray, null, null, null, null, null, null, null, null),
	TWITTER("Twitter", 6, R.drawable.twitter, R.drawable.twitter_gray, "id_str", "http://api.twitter.com/", "https://api.twitter.com/1/account/verify_credentials.json", "ZtN4h8Ps9cyNIadDK6cg", "RCE4Gls0cKcdpe19VYLOC4rg7zHcgV0n5f6rPnaV4VU", "oauth://twitter", null, "https://api.twitter.com/1.1/friendships/create.json"),
	FOURSQUARE("Foursquare", 7, R.drawable.foursquare, R.drawable.foursquare_gray, "id", "http://api.foursquare.com/", "https://api.foursquare.com/v2/users/self?oauth_token=", "RG1XWTHCCDCD0ASO0OU5VF1STSTQWEYGUZRWHBXK14J2CMLQ", "N0CKV2ZY2NKNJ2FJKC4PA1KN2EWHVBG4KH24NGAYNSNFOGQG", "oauth://foursquare", null, "https://api.foursquare.com/v2/users/"),
	FLICKR("Flickr", 8, R.drawable.flickr, R.drawable.flickr_gray, "nsid", "http://api.flickr.com/", "http://api.flickr.com/services/rest/?method=flickr.urls.getUserProfile&format=json&nojsoncallback=1", "13d1fc4d1d2f7924916217597ed71ce8", "af718ebdf5f25310", "oauth://flickr", null, null),
	GOOGLEPLUS("Google+", 9, R.drawable.gplus, R.drawable.gplus_gray, null, null, null, "AIzaSyDWPh56YCBmiENeFomBZy1U4fiRJvdM8cI", null, null, null, null),
	INSTAGRAM("Instagram", 10, R.drawable.instagram, R.drawable.instagram_gray, null, null, null, null, null, null, null, null),
	LINE("Line", 11, R.drawable.line, R.drawable.line_gray, null, null, null, null, null, null, null, null),
	LINKEDIN("LinkedIn", 12, R.drawable.linkedin, R.drawable.linkedin_gray, "id", "http://api.linkedin.com/", "http://api.linkedin.com/v1/people/~:(id,first-name,last-name,industry,email-address)?format=json", "a3dolo9iq9fd", "hKyo855beQ9ggLXZ","oauth://linkedin","r_fullprofile r_emailaddress w_messages", "http://api.linkedin.com/v1/people/~/mailbox"),
	PINTEREST("Pinterest", 13, R.drawable.pinterest, R.drawable.pinterest_gray, null, null, null, null, null, null, null, null),
	TUMBLR("Tumblr", 14, R.drawable.tumblr, R.drawable.tumblr_gray, null, null, null, null, null, null, null, null),
	WHATSAPP("WhatsApp", 15, R.drawable.whatsapp, R.drawable.whatsapp_gray, null, null, null, null, null, null, null, null);

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
	
}
