package com.qardapp.qard.database;

import java.util.Date;

import com.qardapp.qard.Services;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FriendsDatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "qard.db";
	private static final int DATABASE_VERSION = 9;
	
	// Database table
	public static final String TABLE_FRIENDS = "friends";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TITLE = "title"; // Mr. Mrs. PHd, NA
	public static final String COLUMN_FIRST_NAME = "first_name";
	public static final String COLUMN_LAST_NAME = "last_name";
	public static final String COLUMN_USER_ID = "user_id";
	public static final String COLUMN_PROFILE_PIC_LOC = "picture_loc";
	public static final String COLUMN_CONFIRMED = "confirmed";
	public static final String COLUMN_HIDE_CONFIRMED = "hide_confirmed";
	public static final String COLUMN_DATE_ADDED = "date_added";

	// Service Table
	public static final String TABLE_SERVICES = "services";
	public static final String COLUMN_SERVICE_ID = "service_id";
	public static final String COLUMN_SERVICE_NAME = "service_name";
	public static final String COLUMN_SERVICE_PRIORITY = "service_priority";
	public static final String COLUMN_SERVICE_PIC_LOC = "service_pic_loc";


	// Friend_Service Join Table
	public static final String TABLE_FRIEND_SERVICES = "friend_services";
	public static final String COLUMN_FS_FRIEND_ID = "fs_friend_id";
	public static final String COLUMN_FS_SERVICE_ID = "fs_service_id";
	public static final String COLUMN_FS_DATA = "fs_data";


	private static final String FRIENDS_TABLE_CREATE = "create table " 
			+ TABLE_FRIENDS
			+ "(" 
			+ COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_TITLE + " text , "
			+ COLUMN_FIRST_NAME + " text , " 
			+ COLUMN_LAST_NAME + " text , " 
			+ COLUMN_USER_ID + " integer , " 
			+ COLUMN_PROFILE_PIC_LOC + " text , " 
			+ COLUMN_CONFIRMED + " boolean default false, " 
			+ COLUMN_HIDE_CONFIRMED + " boolean default false, " 
			+ COLUMN_DATE_ADDED + " long  " 
			+ ");";
	
	private static final String SERVICES_TABLE_CREATE = "create table " 
			+ TABLE_SERVICES
			+ "(" 
			+ COLUMN_SERVICE_ID + " integer primary key autoincrement, " 
			+ COLUMN_SERVICE_NAME + " text , " 
			+ COLUMN_SERVICE_PRIORITY + " integer , " 
			+ COLUMN_SERVICE_PIC_LOC + " text " 
			+ ");";
	
	private static final String FRIEND_SERVICES_TABLE_CREATE = "create table " 
			+ TABLE_FRIEND_SERVICES
			+ "(" 
			+ COLUMN_FS_FRIEND_ID + " integer not null, " 
			+ COLUMN_FS_SERVICE_ID + " integer not null, " 
			+ COLUMN_FS_DATA + " text default ''" 
			+ ");";
		  
	public FriendsDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		//context.deleteDatabase(DATABASE_NAME);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(FRIENDS_TABLE_CREATE);
		db.execSQL(SERVICES_TABLE_CREATE);
		db.execSQL(FRIEND_SERVICES_TABLE_CREATE);
		
		//Services
		
		for (Services service :Services.values()) {
			db.execSQL(insertService(service.id, service.name, service.priority, ""));
		}
		
		db.execSQL(insertFriend(0, "", "Me", "Me", 0, "", new Date().getTime()));

		
		//Friends
		db.execSQL(insertFriend(1, "", "John", "Doe", 1, "", new Date().getTime()));
		db.execSQL(insertFriend(2, "", "Mary", "Jane", 2, "", new Date().getTime()));
		db.execSQL(insertFriend(3, "", "Bob", "Q", 3, "", new Date().getTime()));
		db.execSQL(insertFriend(4, "", "Tim", "W", 4, "", new Date().getTime()));
		db.execSQL(insertFriend(5, "", "Paul", "T", 5, "", new Date().getTime()));
		
		// Friend Services
		db.execSQL(insertFriendService(0, Services.PHONE.id, "4161234567"));
		db.execSQL(insertFriendService(0, Services.ADDRESS.id, "32 Yonge St."));
		//db.execSQL(insertFriendService(0, Services.FACEBOOK.id, "me123"));
		
		db.execSQL(insertFriendService(1, Services.PHONE.id, "4169285304"));
		db.execSQL(insertFriendService(1, Services.ADDRESS.id, "32 Yonge St."));
		db.execSQL(insertFriendService(1, Services.FACEBOOK.id, "johndoe31"));
		
		db.execSQL(insertFriendService(2, Services.PHONE.id, "4161234456"));
		db.execSQL(insertFriendService(2, Services.ADDRESS.id, "123 Dundas St."));
		db.execSQL(insertFriendService(2, Services.FACEBOOK.id, "maryjane231"));
		
	//	db.execSQL(insertFriendService(3, Services.PHONE.id, ""));
	//	db.execSQL(insertFriendService(3, Services.ADDRESS.id, ""));
		db.execSQL(insertFriendService(3, Services.FACEBOOK.id, "511723429"));
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIENDS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIEND_SERVICES);
	    onCreate(db);
	}

	
	private String insertFriend(int id, String title, String first_name,
			String last_name, int user_id, String picture_loc, long date_added) {
		String insert = "INSERT INTO " + TABLE_FRIENDS + " (" + COLUMN_ID + ","
				+ COLUMN_TITLE + "," + COLUMN_FIRST_NAME + ","
				+ COLUMN_LAST_NAME + "," + COLUMN_USER_ID + ","
				+ COLUMN_PROFILE_PIC_LOC + "," + COLUMN_DATE_ADDED
				+ ") VALUES ('" + id + "','" + title + "','" + first_name
				+ "','" + last_name + "','" + user_id + "','" + picture_loc
				+ "','" + date_added + "')";
		return insert;
	}

	private String insertService(int id, String name, int priority,
			String pic_loc) {
		String insert = "INSERT INTO " + TABLE_SERVICES + " ("
				+ COLUMN_SERVICE_ID + "," + COLUMN_SERVICE_NAME + ","
				+ COLUMN_SERVICE_PRIORITY + "," + COLUMN_SERVICE_PIC_LOC
				+ ") VALUES ('" + id + "','" + name + "','" + priority + "','"
				+ pic_loc + "')";
		return insert;
	}
	
	private String insertFriendService(int friend_id, int service_id,
			String data) {
		String insert = "INSERT INTO " + TABLE_FRIEND_SERVICES + " ("
				+ COLUMN_FS_FRIEND_ID + "," + COLUMN_FS_SERVICE_ID + ","
				+ COLUMN_FS_DATA + ") VALUES ('" + friend_id + "','"
				+ service_id + "','" + data + "')";
		return insert;
	}
}
