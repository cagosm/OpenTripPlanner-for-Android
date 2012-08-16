package edu.usf.cutr.opentripplanner.android;
//package edu.usf.cutr.opentripplanner.android;
//
//
//
//import edu.usf.cutr.opentripplanner.android.MarkerList.Markers;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.EditText;
//import edu.usf.cutr.opentripplanner.android.listeners.OnFragmentListener;
//import edu.usf.cutr.opentripplanner.android.sqlite.MarkerDbProvider;
//import edu.usf.cutr.opentripplanner.android.R;
//import android.app.Activity;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//
//public class MarkerCreationActivityOld extends Activity {
//	
//	private EditText mTitleText;
//	private EditText mTypeText;
//	private EditText mContributorText;
//    private EditText mDescriptionText;
//    private Long mRowId;
//    private MarkerDbProvider mDbHelper;
//    
//    //private OnFragmentListener fragmentListener;
//    
//    
//
//    
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        mDbHelper = new MarkerDbProvider(this);
//        mDbHelper.open();
//        
//        setContentView(R.layout.edit_marker);
//        
//        mTitleText = (EditText) findViewById(R.id.title);
//        mTypeText = (EditText) findViewById(R.id.type);
//        mContributorText = (EditText) findViewById(R.id.contributor);
//        mDescriptionText = (EditText) findViewById(R.id.description);
//        
//        Button btnConfirm = (Button) findViewById(R.id.confirm);
//        
//        mRowId = (savedInstanceState == null) ? null :
//            (Long) savedInstanceState.getSerializable(MarkerDbProvider.KEY_ROWID);
//		if (mRowId == null) {
//			Bundle extras = getIntent().getExtras();
//			mRowId = extras != null ? extras.getLong(MarkerDbProvider.KEY_ROWID): null;
//		}
//		
//		btnConfirm.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View view) {
//                //setResult(RESULT_OK);
//                finish();
//            }
//
//        });
//    }
//    
//    
//    
//    
//    /**
//	 * @return the fragmentListener
//	 */
//	
//	
//	@Override
//	public void onPause() {
//        super.onPause();
//        saveState();
//    }
//
//	@Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        
//        saveState();
//        outState.putSerializable(MarkerDbProvider.KEY_ROWID, mRowId);
//    }
//	
//	@Override
//	public void onDestroy(){
//		super.onDestroy();
//	}
//	
//	@Override
//	public void onResume() {
//		super.onResume();
//	}
//
//    private void saveState() {
//    	
//    	Integer latitude = null;
//    	Integer longitude = null;
//        String title = mTitleText.getText().toString();
//        String type = mTypeText.getText().toString();
//        String contributor = mContributorText.getText().toString();
//        String description = mDescriptionText.getText().toString();
//        Bundle extras = getIntent().getExtras(); 
//        if(extras !=null)
//        {
//        latitude = extras.getInt("Latitude");
//        longitude = extras.getInt("Longitude");
//        }
//
//        long id = mDbHelper.insert(title, type, contributor, description, latitude, longitude);
//            if (id > 0) {
//                mRowId = id;
//            }
//        
//    }
//}
