package edu.usf.cutr.opentripplanner.android;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import edu.usf.cutr.opentripplanner.android.listeners.OnFragmentListener;
import edu.usf.cutr.opentripplanner.android.sqlite.MarkerDBHelper;
import edu.usf.cutr.opentripplanner.android.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class MarkerCreationActivity extends Activity implements OnItemSelectedListener {
	
	private EditText mTitleText;
	private Spinner mTypeSpinner;
	private EditText mContributorText;
	private EditText mLatitudeText;
	private EditText mLongitudeText;
    private EditText mDescriptionText;
    private Long mRowId;
    private MarkerDBHelper mDbHelper;
    private String mType;
    
    //private OnFragmentListener fragmentListener;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDbHelper = new MarkerDBHelper(this);
        mDbHelper.open();
        
        setContentView(R.layout.edit_marker);
        
        mTitleText = (EditText) findViewById(R.id.title);     
        mTypeSpinner = (Spinner) findViewById(R.id.type_spinner);
        ArrayAdapter<CharSequence> marker_adapter = ArrayAdapter.createFromResource(this,
                R.array.marker_types, android.R.layout.simple_spinner_item);
        marker_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTypeSpinner.setOnItemSelectedListener(this);
        mContributorText = (EditText) findViewById(R.id.contributor);
        mLatitudeText = (EditText) findViewById(R.id.latitude);
        mLongitudeText = (EditText) findViewById(R.id.longitude);
        mDescriptionText = (EditText) findViewById(R.id.description);
        
        Button btnConfirm = (Button) findViewById(R.id.confirm);
        
        mRowId = (savedInstanceState == null) ? null :
            (Long) savedInstanceState.getSerializable(MarkerDBHelper.COLUMN_ID);
		if (mRowId == null) {
			Bundle extras = getIntent().getExtras();
			mRowId = extras != null ? extras.getLong(MarkerDBHelper.COLUMN_ID): null;
		}
		
		btnConfirm.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                //setResult(RESULT_OK);
                finish();
            }

        });
    }
    
    
    
    
    /**
	 * @return the fragmentListener
	 */
	
	
	@Override
	public void onPause() {
        super.onPause();
        saveState();
    }

	@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        
        saveState();
        outState.putSerializable(MarkerDBHelper.COLUMN_ID, mRowId);
    }
	
	@Override
	public void onDestroy(){
		super.onDestroy();
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}

    private void saveState() {
    	
    	Integer latitude = Integer.valueOf(mLatitudeText.getText().toString());
    	Integer longitude = Integer.valueOf(mLongitudeText.getText().toString());
        String title = mTitleText.getText().toString();
 
        String contributor = mContributorText.getText().toString();
        String description = mDescriptionText.getText().toString();
        Bundle extras = getIntent().getExtras(); 
        if(extras !=null)
        {
        latitude = extras.getInt("Latitude");
        longitude = extras.getInt("Longitude");
        }

        long id = mDbHelper.createMarker(title, mType, contributor, description, latitude, longitude);
            if (id > 0) {
                mRowId = id;
            }
        
    }




	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		
		mType = parent.getItemAtPosition(position).toString();
		
	}




	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}
