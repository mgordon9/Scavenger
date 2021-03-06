package com.project.ece150.scavenger;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.project.ece150.scavenger.remote.IRemoteClientObserver;
import com.project.ece150.scavenger.remote.RemoteClient;

import java.io.File;
import java.net.URI;
import java.util.List;


public class CreateObjectiveFragment extends Fragment
        implements IRemoteClientObserver {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    String mAccountName;

    RemoteClient mRemoteClient;
    LocationClient mLocationClient;
    MainActivity mMainActivity;

    ProgressDialog mProgress;

    // Image vars.
    private ImageView mImageView;
    private Uri mImageUri;
    private Bitmap mImageBitmap;

    public CreateObjectiveFragment() {
    }

    public void initialize(RemoteClient remoteClient, LocationClient locationClient, String accountName, MainActivity mainActivity) {
        mRemoteClient = remoteClient;
        mLocationClient = locationClient;
        mAccountName = accountName;
        mMainActivity = mainActivity;

        mRemoteClient.registerObserver(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_createobjective, container, false);

        // Init ImageView.
        mImageView = (ImageView) rootView.findViewById(R.id.imageView);

        // Take Picture.
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo;
        try {
            photo = this.createTemporaryFile("picture", ".jpg");
            photo.delete();

            mImageUri = Uri.fromFile(photo);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
            //start camera intent
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        }
        catch(Exception e) {
            Log.v("abc", "Can't create file to take picture!");
            Toast.makeText(getActivity(), "Please check SD card! Image shot is impossible!", 10000);
        }

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private File createTemporaryFile(String part, String ext) throws Exception {
        File tempDir= Environment.getExternalStorageDirectory();
        tempDir=new File(tempDir.getAbsolutePath()+"/.temp/");
        if(!tempDir.exists())
        {
            tempDir.mkdir();
        }
        return File.createTempFile(part, ext, tempDir);
    }

    public Bitmap grabImage(ImageView imageView)
    {
        getActivity().getContentResolver().notifyChange(mImageUri, null);
        ContentResolver cr = getActivity().getContentResolver();
        Bitmap bitmap;
        try
        {
            bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, mImageUri);
            imageView.setImageBitmap(bitmap);
            mImageBitmap = bitmap;

            return bitmap;
        }
        catch (Exception e)
        {
            Toast.makeText(getActivity(), "Failed to load", Toast.LENGTH_SHORT).show();
            Log.d("abc", "Failed to load", e);
        }

        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_IMAGE_CAPTURE) {
            ImageView imageView = (ImageView) getView().findViewById(R.id.imageView);

            Bitmap image = grabImage(imageView);

            // Create Button.
            Button button = (Button) getView().findViewById(R.id.buttonSubmit);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Objective objective = parseToObjective(mImageBitmap);
                    storeInDatabase(objective, mImageBitmap);

                    mProgress = new ProgressDialog(getActivity());
                    mProgress.setMessage("Creating Objective...");
                    mProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    mProgress.setIndeterminate(true);
                    mProgress.setProgress(0);
                    mProgress.show();
                }
            });
        }
    }

    public Objective parseToObjective(Bitmap image) {
        Objective objective = new Objective();

        objective.setObjectiveid("apptest");

        EditText title = (EditText) getView().findViewById(R.id.editTextTitle);
        objective.setTitle(title.getText().toString());

        EditText description = (EditText)getView().findViewById(R.id.editTextDescription);
        objective.setInfo(description.getText().toString());

        Double latitude = mLocationClient.getCurrentPosition().latitude;
        objective.setLatitude(latitude);

        Double longitude = mLocationClient.getCurrentPosition().longitude;
        objective.setLongitude(longitude);

        objective.setOwner(mAccountName);

        objective.setImage(image);

        return objective;
    }

    public void storeInDatabase(Objective objective, Bitmap image) {
        mRemoteClient.initObjectivesCreateRequest(objective);
    }

    @Override
    public void onUserGetReceived(IUser user) {

    }

    @Override
    public void onObjectivesGetReceived(List<IObjective> objectives) {
    }

    @Override
    public void onObjectiveGetReceived(IObjective objective) {

    }

    @Override
    public void onObjectiveCreated() {
        // Hide Keyboard
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        // Show ProgressBar
        mProgress.dismiss();

        // Go to MapFragment
        mMainActivity.selectFragment(mMainActivity.getMapFragment());
    }
}