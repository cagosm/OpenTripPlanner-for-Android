package edu.usf.cutr.opentripplanner.android;

import android.net.Uri;
import android.provider.BaseColumns;


public final class MarkerList {
    public static final String AUTHORITY 
    	= "edu.usf.cutr.opentripplanner.android.providers.MarkerList";

    private MarkerList() {}
    
    public static final class Markers implements BaseColumns {
        
        private Markers() {}

        public static final Uri CONTENT_URI 
        	= Uri.parse("content://" + AUTHORITY + "/markers");

        public static final String CONTENT_TYPE 
        	= "vnd.android.cursor.dir/vnd.google.marker";

        public static final String CONTENT_ITEM_TYPE 
        	= "vnd.android.cursor.item/vnd.google.marker";

        public static final String DEFAULT_SORT_ORDER = "modified DESC";

        public static final String TITLE = "title";
        
        public static final String CONTRIBUTOR = "contributor";

        public static final String DESCRIPTION = "description";

        public static final String TYPE = "type";
        
        public static final String LATITUDE = "latitude";
        
        public static final String LONGITUDE = "longitude";
        
        public static final String CREATED_DATE = "created";

        public static final String MODIFIED_DATE = "modified";
    }

}
