package edu.usf.cutr.opentripplanner.android;


import edu.usf.cutr.opentripplanner.android.MarkerList.Markers;
import android.app.ListActivity;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MarkerListProviderActivity extends ListActivity {
	
	 public static final int MENU_ITEM_DELETE = Menu.FIRST;
	 public static final int MENU_ITEM_INSERT = Menu.FIRST + 1;
	 
	 private static final String[] PROJECTION = new String[] {
         Markers._ID, 
         Markers.TITLE,
	 };
	 
	 private static final int COLUMN_INDEX_TITLE = 1;
	 
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        setDefaultKeyMode(DEFAULT_KEYS_SHORTCUT);

	        Intent intent = getIntent();
	        if (intent.getData() == null) {
	            intent.setData(Markers.CONTENT_URI);
	        }

	        getListView().setOnCreateContextMenuListener(this);
	        
	        Cursor cursor = managedQuery(getIntent().getData(), PROJECTION, null, null,
	                Markers.DEFAULT_SORT_ORDER);

	        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.markerlist_item, cursor,
	                new String[] { Markers.TITLE }, new int[] { android.R.id.text1 });
	        setListAdapter(adapter);
	    }
	 
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        super.onCreateOptionsMenu(menu);

	        // This is our one standard application action -- inserting a
	        // new note into the list.
	        menu.add(0, MENU_ITEM_INSERT, 0, R.string.marker_menu_insert)
	                .setShortcut('3', 'a')
	                .setIcon(android.R.drawable.ic_menu_add);

	        // Generate any additional actions that can be performed on the
	        // overall list.  In a normal install, there are no additional
	        // actions found here, but this allows other applications to extend
	        // our menu with their own actions.
	        Intent intent = new Intent(null, getIntent().getData());
	        intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
	        menu.addIntentOptions(Menu.CATEGORY_ALTERNATIVE, 0, 0,
	                new ComponentName(this, MarkerListProviderActivity.class), null, intent, 0, null);

	        return true;
	    }

	    @Override
	    public boolean onPrepareOptionsMenu(Menu menu) {
	        super.onPrepareOptionsMenu(menu);
	        final boolean haveItems = getListAdapter().getCount() > 0;

	        // If there are any notes in the list (which implies that one of
	        // them is selected), then we need to generate the actions that
	        // can be performed on the current selection.  This will be a combination
	        // of our own specific actions along with any extensions that can be
	        // found.
	        if (haveItems) {
	            // This is the selected item.
	            Uri uri = ContentUris.withAppendedId(getIntent().getData(), getSelectedItemId());

	            // Build menu...  always starts with the EDIT action...
	            Intent[] specifics = new Intent[1];
	            specifics[0] = new Intent(Intent.ACTION_EDIT, uri);
	            MenuItem[] items = new MenuItem[1];

	            // ... is followed by whatever other actions are available...
	            Intent intent = new Intent(null, uri);
	            intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
	            menu.addIntentOptions(Menu.CATEGORY_ALTERNATIVE, 0, 0, null, specifics, intent, 0,
	                    items);

	            // Give a shortcut to the edit action.
	            if (items[0] != null) {
	                items[0].setShortcut('1', 'e');
	            }
	        } else {
	            menu.removeGroup(Menu.CATEGORY_ALTERNATIVE);
	        }

	        return true;
	    }

	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	        case MENU_ITEM_INSERT:
	            // Launch activity to insert a new item
	            startActivity(new Intent(Intent.ACTION_INSERT, getIntent().getData()));
	            return true;
	        }
	        return super.onOptionsItemSelected(item);
	    }

	 
	 @Override
	    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
	        AdapterView.AdapterContextMenuInfo info;
	        try {
	             info = (AdapterView.AdapterContextMenuInfo) menuInfo;
	        } catch (ClassCastException e) {
	            Log.e("Markers", "bad menuInfo", e);
	            return;
	        }

	        Cursor cursor = (Cursor) getListAdapter().getItem(info.position);
	        if (cursor == null) {
	            // For some reason the requested item isn't available, do nothing
	            return;
	        }

	        // Setup the menu header
	        menu.setHeaderTitle(cursor.getString(COLUMN_INDEX_TITLE));

	        // Add a menu item to delete the note
	        menu.add(0, MENU_ITEM_DELETE, 0, R.string.marker_delete);
	    }
	 
	 @Override
	    public boolean onContextItemSelected(MenuItem item) {
	        AdapterView.AdapterContextMenuInfo info;
	        try {
	             info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
	        } catch (ClassCastException e) {
	            Log.e("Markers", "bad menuInfo", e);
	            return false;
	        }

	        switch (item.getItemId()) {
	            case MENU_ITEM_DELETE: {
	                Uri markerUri = ContentUris.withAppendedId(getIntent().getData(), info.id);
	                getContentResolver().delete(markerUri, null, null);
	                return true;
	            }
	        }
	        return false;
	    }

	    @Override
	    protected void onListItemClick(ListView l, View v, int position, long id) {
	        Uri uri = ContentUris.withAppendedId(getIntent().getData(), id);
	        
	        String action = getIntent().getAction();
	        if (Intent.ACTION_PICK.equals(action) || Intent.ACTION_GET_CONTENT.equals(action)) {
	            // The caller is waiting for us to return a note selected by
	            // the user.  The have clicked on one, so return it now.
	            setResult(RESULT_OK, new Intent().setData(uri));
	        } else {
	            // Launch activity to view/edit the currently selected item
	            startActivity(new Intent(Intent.ACTION_EDIT, uri));
	        }
	    }
}
