package com.qardapp.qard;

public enum Services {
	PHONE("Phone Number", 1, R.drawable.phone, R.drawable.phone_gray),
	TEXT("Text", 2, R.drawable.whatsapp, R.drawable.whatsapp_gray),
	ADDRESS("Address", 3, R.drawable.ic_launcher, R.drawable.ic_launcher_gray),
	FACEBOOK("Facebook", 4, R.drawable.facebook, R.drawable.facebook_gray),
	GMAIL("E-mail", 5, R.drawable.gmail, R.drawable.gmail_gray),
	TWITTER("Twitter", 6, R.drawable.twitter, R.drawable.twitter_gray),
	FOURSQUARE("Foursquare", 7, R.drawable.foursquare, R.drawable.foursquare_gray),
	FLICKR("Flickr", 8, R.drawable.flickr, R.drawable.flickr_gray),
	GOOGLEPLUS("Google+", 9, R.drawable.gplus, R.drawable.gplus_gray),
	INSTAGRAM("Instagram", 10, R.drawable.instagram, R.drawable.instagram_gray),
	LINE("Line", 11, R.drawable.line, R.drawable.line_gray),
	LINKEDIN("LinkedIn", 12, R.drawable.linkedin, R.drawable.linkedin_gray),
	PINTEREST("Pinterest", 13, R.drawable.pinterest, R.drawable.pinterest_gray),
	TUMBLR("Tumblr", 14, R.drawable.tumblr, R.drawable.tumblr_gray),
	WHATSAPP("WhatsApp", 15, R.drawable.whatsapp, R.drawable.whatsapp_gray);

	public int id;
	public int priority;
	public String name;
	public int imageId;
	public int grayImageId;
	
	private Services (String name, int id, int imageId, int grayImageId) {
		this.name = name;
		this.id = id;
		this.priority = id;
		this.imageId = imageId;
		this.grayImageId = grayImageId;
	}
	
}
