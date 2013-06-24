package com.qardapp.qard.database;


import java.util.List;

import com.qardapp.qard.Services;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class FriendsProvider extends ContentProvider {

	private FriendsDatabaseHelper database;

	// Used for the UriMacher
	private static final int FRIENDS = 10;
	private static final int FRIEND = 20;
	private static final int FRIEND_SERVICE = 30;
	private static final int SERVICES = 40;
	private static final int SERVICE_DATA = 50;

	private static final String AUTHORITY = "com.qardapp.qard.database";

	private static final String BASE_PATH = "qard";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + BASE_PATH);
	
	public static final Uri MY_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + BASE_PATH + "/0");

	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/friends";
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/friend";

	// Valid URI
	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		sURIMatcher.addURI(AUTHORITY, BASE_PATH, FRIENDS);
		sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", FRIEND);
		sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#/service/#", FRIEND_SERVICE);
		sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/services", SERVICES);
		sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/service_data", SERVICE_DATA);

	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		int rowsDeleted = 0;
		switch (uriType) {
		// Deletes all friends
		case FRIENDS:
			rowsDeleted = sqlDB.delete(FriendsDatabaseHelper.TABLE_FRIENDS, selection,
					selectionArgs);
			break;
			//Deletes a specific friend
		case FRIEND:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsDeleted = sqlDB.delete(FriendsDatabaseHelper.TABLE_FRIENDS,
						FriendsDatabaseHelper.COLUMN_ID + "=" + id, 
						null);
			} else {
				rowsDeleted = sqlDB.delete(FriendsDatabaseHelper.TABLE_FRIENDS,
						FriendsDatabaseHelper.COLUMN_ID + "=" + id 
						+ " and " + selection,
						selectionArgs);
			}
			break;
		case FRIEND_SERVICE:
			rowsDeleted = sqlDB.delete(FriendsDatabaseHelper.TABLE_FRIEND_SERVICES, selection,
					selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsDeleted;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		long id = 0;
		switch (uriType) {
		// Insert receipt
		case FRIENDS:
		case FRIEND:
			id = sqlDB.insert(FriendsDatabaseHelper.TABLE_FRIENDS, null, values);
			getContext().getContentResolver().notifyChange(uri, null);
			return Uri.parse(BASE_PATH + "/" + id);
			// Insert receipt item
		case FRIEND_SERVICE:
			id = sqlDB.insert(FriendsDatabaseHelper.TABLE_FRIEND_SERVICES, null, values);
			getContext().getContentResolver().notifyChange(uri, null);
			return Uri.parse(BASE_PATH + "/" + values.getAsString(FriendsDatabaseHelper.COLUMN_FS_FRIEND_ID));
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		//  getContext().getContentResolver().notifyChange(uri, null);
		//  return Uri.parse(BASE_PATH + "/" + id);
	}

	@Override
	public boolean onCreate() {
		database = new FriendsDatabaseHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		String limit = null;	    

		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		// Returns all receipts
		case FRIENDS:
			queryBuilder.setTables(FriendsDatabaseHelper.TABLE_FRIENDS + " LEFT OUTER JOIN " +
					FriendsDatabaseHelper.TABLE_FRIEND_SERVICES  +
					" ON (" + FriendsDatabaseHelper.TABLE_FRIENDS +
					"." + FriendsDatabaseHelper.COLUMN_ID + " = "+ FriendsDatabaseHelper.TABLE_FRIEND_SERVICES +
					"." + FriendsDatabaseHelper.COLUMN_FS_FRIEND_ID +" AND " + FriendsDatabaseHelper.TABLE_FRIEND_SERVICES +
					"." + FriendsDatabaseHelper.COLUMN_FS_SERVICE_ID +" = " + Services.PHONE.id +")");
			if (sortOrder == null)
				sortOrder = FriendsDatabaseHelper.COLUMN_FIRST_NAME + " ASC, " 
						+ FriendsDatabaseHelper.COLUMN_LAST_NAME + " ASC ";	
			// NOTE: Putting Me in FRIENDS for now to test
			queryBuilder.appendWhere(FriendsDatabaseHelper.COLUMN_ID + "!= -1");
			break;
			// Returns a specific receipt
		case FRIEND:
			queryBuilder.setTables(FriendsDatabaseHelper.TABLE_FRIENDS + " LEFT OUTER JOIN " +
					FriendsDatabaseHelper.TABLE_FRIEND_SERVICES  +
					" ON (" + FriendsDatabaseHelper.TABLE_FRIENDS +
					"." + FriendsDatabaseHelper.COLUMN_ID + " = "+ FriendsDatabaseHelper.TABLE_FRIEND_SERVICES +
					"." + FriendsDatabaseHelper.COLUMN_FS_FRIEND_ID +") LEFT OUTER JOIN " +
					FriendsDatabaseHelper.TABLE_SERVICES  +
					" ON (" + FriendsDatabaseHelper.TABLE_FRIEND_SERVICES +
					"." + FriendsDatabaseHelper.COLUMN_FS_SERVICE_ID + " = "+ FriendsDatabaseHelper.TABLE_SERVICES +
					"." + FriendsDatabaseHelper.COLUMN_SERVICE_ID +")");
			// Adding the ID to the original query
			queryBuilder.appendWhere(FriendsDatabaseHelper.COLUMN_ID + "="
					+ uri.getLastPathSegment());
			
			if (sortOrder == null)
				sortOrder = FriendsDatabaseHelper.COLUMN_SERVICE_PRIORITY + " ASC";	  

			break;
			// Returns the last receipt
		case SERVICE_DATA:
			queryBuilder.setTables(FriendsDatabaseHelper.TABLE_FRIENDS + " LEFT OUTER JOIN " +
					FriendsDatabaseHelper.TABLE_FRIEND_SERVICES  +
					" ON (" + FriendsDatabaseHelper.TABLE_FRIENDS +
					"." + FriendsDatabaseHelper.COLUMN_ID + " = "+ FriendsDatabaseHelper.TABLE_FRIEND_SERVICES +
					"." + FriendsDatabaseHelper.COLUMN_FS_FRIEND_ID +") LEFT OUTER JOIN " +
					FriendsDatabaseHelper.TABLE_SERVICES  +
					" ON (" + FriendsDatabaseHelper.TABLE_FRIEND_SERVICES +
					"." + FriendsDatabaseHelper.COLUMN_FS_SERVICE_ID + " = "+ FriendsDatabaseHelper.TABLE_SERVICES +
					"." + FriendsDatabaseHelper.COLUMN_SERVICE_ID +")");
			break;
		case SERVICES:
			queryBuilder.setTables(FriendsDatabaseHelper.TABLE_SERVICES);
			if (sortOrder == null)
				sortOrder = FriendsDatabaseHelper.COLUMN_SERVICE_PRIORITY + " ASC";	      
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		SQLiteDatabase db = database.getWritableDatabase();

		Cursor cursor = queryBuilder.query(db, projection, selection,
				selectionArgs, null, null, sortOrder, limit);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		int rowsUpdated = 0;
		switch (uriType) {
		// Update the friends
		case FRIENDS:
			rowsUpdated = sqlDB.update(FriendsDatabaseHelper.TABLE_FRIENDS, 
					values, 
					selection,
					selectionArgs);
			break;
			// Updates the specific friend
		case FRIEND:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = sqlDB.update(FriendsDatabaseHelper.TABLE_FRIENDS, 
						values,
						FriendsDatabaseHelper.COLUMN_ID + "=" + id, 
						null);
			} else {
				rowsUpdated = sqlDB.update(FriendsDatabaseHelper.TABLE_FRIENDS, 
						values,
						FriendsDatabaseHelper.COLUMN_ID + "=" + id 
						+ " and " 
						+ selection,
						selectionArgs);
			}
			break;
		case FRIEND_SERVICE:
			List<String> paths = uri.getPathSegments();
			String service_id = paths.get(paths.size()-1);
			String friend_id = paths.get(paths.size()-3);
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = sqlDB.update(FriendsDatabaseHelper.TABLE_FRIEND_SERVICES, 
						values,
						FriendsDatabaseHelper.COLUMN_FS_FRIEND_ID + "=" + friend_id + " AND "
								+ FriendsDatabaseHelper.COLUMN_FS_SERVICE_ID + "=" + service_id+ " ", 
								null);
			} else {
				rowsUpdated = sqlDB.update(FriendsDatabaseHelper.TABLE_FRIENDS, 
						values,
						FriendsDatabaseHelper.COLUMN_FS_FRIEND_ID + "=" + friend_id + " AND "
								+ FriendsDatabaseHelper.COLUMN_FS_SERVICE_ID + "=" + service_id+ " "
								+ " and " 
								+ selection,
								selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}

}
