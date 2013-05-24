package com.qardapp.qard;

public enum Services {
	PHONE("Phone Number", 1, R.drawable.phone),
	TEXT("Text", 2, R.drawable.whatsapp),
	ADDRESS("Address", 3, R.drawable.ic_launcher),
	FACEBOOK("Facebook", 4, R.drawable.facebook),
	GMAIL("E-mail", 5, R.drawable.gmail),
	TWITTER("Twitter", 6, R.drawable.twitter),
	FOURSQUARE("Foursquare", 7, R.drawable.foursquare),
	FLICKR("Flickr", 8, R.drawable.flickr),
	GOOGLEPLUS("Google+", 9, R.drawable.gplus),
	INSTAGRAM("Instagram", 10, R.drawable.instagram),
	LINE("Line", 11, R.drawable.line),
	LINKEDIN("LinkedIn", 12, R.drawable.linkedin),
	PINTEREST("Pinterest", 13, R.drawable.pinterest),
	TUMBLR("Tumblr", 14, R.drawable.tumblr),
	WHATSAPP("WhatsApp", 15, R.drawable.whatsapp);

	public int id;
	public int priority;
	public String name;
	public int imageId;
	
	private Services (String name, int id, int imageId) {
		this.name = name;
		this.id = id;
		this.priority = id;
		this.imageId = imageId;
	}
	
}
