package com.mad.appetit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Profile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private static final String MyPREF = "User_Data";
    private static final String CheckPREF = "First Run";
    private static final String Name = "keyName";
    private static final String Surname = "keySurname";
    private static final String Address = "keyAddress";
    private static final String Description = "keyDescription";
    private static final String Email = "keyEmail";
    private static final String Phone = "keyPhone";
    private static final String Photo = "keyPhoto";
    private static final String FirstRun = "keyRun";

    ImageView imgView;

    View view;

    private SharedPreferences first_check;

    public Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        setHasOptionsMenu(true);
    }

    public void checkFirst(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        SharedPreferences user_data = getContext().getSharedPreferences(MyPREF, 0);
        if(((user_data.getString(Name, null))) == null) {
            Intent i = new Intent(getContext(),EditProfile.class);
            startActivity(i);
        }

        ((TextView)view.findViewById(R.id.name)).setText(user_data.getString(Name, ""));
        ((TextView)view.findViewById(R.id.address)).setText(user_data.getString(Address, ""));
        ((TextView)view.findViewById(R.id.description)).setText(user_data.getString(Description, ""));
        ((TextView)view.findViewById(R.id.mail)).setText(user_data.getString(Email, ""));
        ((TextView)view.findViewById(R.id.phone)).setText(user_data.getString(Phone, ""));

        imgView = view.findViewById(R.id.profile_image);

        try {
            if(!user_data.getString(Photo,"").equals(""))
                setPhoto(user_data.getString(Photo, ""));
        } catch (IOException e) {
            e.printStackTrace();
        }



        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);


        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.add:
                Intent i = new Intent(getContext(),EditProfile.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setPhoto(String photoPath) throws IOException {
        File imgFile = new File(photoPath);

        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        myBitmap = adjustPhoto(myBitmap, photoPath);

        imgView.setImageBitmap(myBitmap);
    }

    private Bitmap adjustPhoto(Bitmap bitmap, String photoPath) throws IOException {
        ExifInterface ei = new ExifInterface(photoPath);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        Bitmap rotatedBitmap = null;
        switch(orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = rotateImage(bitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = rotateImage(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = rotateImage(bitmap, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = bitmap;
        }

        return rotatedBitmap;
    }

    private static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d("RESUME","resuming");
        SharedPreferences user_data = getContext().getSharedPreferences(MyPREF, 0);

        ((TextView)view.findViewById(R.id.name)).setText(user_data.getString(Name, ""));
        ((TextView)view.findViewById(R.id.address)).setText(user_data.getString(Address, ""));
        ((TextView)view.findViewById(R.id.description)).setText(user_data.getString(Description, ""));
        ((TextView)view.findViewById(R.id.mail)).setText(user_data.getString(Email, ""));
        ((TextView)view.findViewById(R.id.phone)).setText(user_data.getString(Phone, ""));

        try {
            if(!user_data.getString(Photo,"").equals(""))
                setPhoto(user_data.getString(Photo, ""));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
