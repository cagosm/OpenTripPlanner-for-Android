package edu.usf.cutr.opentripplanner.android.fragments;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import edu.usf.cutr.opentripplanner.android.listeners.OnFragmentListener;
import edu.usf.cutr.opentripplanner.android.sqlite.MarkerDbAdapter;
import edu.usf.cutr.opentripplanner.android.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class MarkerCreationFragment extends Fragment {
	
	private EditText mTitleText;
	private EditText mTypeText;
	private EditText mContributorText;
    private EditText mDescriptionText;
    private Long mRowId;
    private MarkerDbAdapter mDbHelper;
    
    private OnFragmentListener fragmentListener;
    
    private View formView = null;
    
    public MarkerCreationFragment() {
    	
    }

    @Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			setFragmentListener((OnFragmentListener) activity);
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnFragmentListener");
		}
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        formView = inflater.inflate(R.layout.edit_marker, container);

        return formView;
    }
    
    @Override
	public void onActivityCreated(Bundle savedState) {
		super.onActivityCreated(savedState);
		
		mDbHelper = new MarkerDbAdapter(this.getActivity());
        mDbHelper.open();
        
        mTitleText = (EditText) formView.findViewById(R.id.title);
        mTypeText = (EditText) formView.findViewById(R.id.type);
        mContributorText = (EditText) formView.findViewById(R.id.contributor);
        mDescriptionText = (EditText) formView.findViewById(R.id.description);
        
        Button btnConfirm = (Button) formView.findViewById(R.id.confirm);
        
        final OnFragmentListener ofl = this.getFragmentListener();
		final MarkerCreationFragment mcf = this;
		OnClickListener oclCreateMarker = new OnClickListener() {
			@Override
			public void onClick(View formView) {
				if( (mTitleText.getText() != null) && 
					(mTypeText.getText() != null)  &&
					(mContributorText.getText() != null)  &&
					(mDescriptionText.getText() != null) )
					
					ofl.onSwitchedToMainFragment(mcf);
			}
		};
		btnConfirm.setOnClickListener(oclCreateMarker);
        		
		
    }
    
    
    /**
	 * @return the fragmentListener
	 */
	public OnFragmentListener getFragmentListener() {
		return fragmentListener;
	}

	/**
	 * @param fragmentListener the fragmentListener to set
	 */
	public void setFragmentListener(OnFragmentListener fragmentListener) {
		this.fragmentListener = fragmentListener;
	}
	
	@Override
	public void onPause() {
        super.onPause();
        saveState();
    }

    

    private void saveState() {
        String title = mTitleText.getText().toString();
        String type = mTitleText.getText().toString();
        String contributor = mContributorText.getText().toString();
        String description = mDescriptionText.getText().toString();
        Integer latitude = this.getArguments().getInt("Latitude");
        Integer longitude = this.getArguments().getInt("Longitude");

            long id = mDbHelper.createMarker(title, type, contributor, description, latitude, longitude);
            if (id > 0) {
                mRowId = id;
            }
        
    }
}
