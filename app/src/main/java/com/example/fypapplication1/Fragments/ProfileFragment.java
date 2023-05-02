//Student Name: Megan Cash
//Student Number: C19317723
package com.example.fypapplication1.Fragments;

import static android.app.Activity.RESULT_OK;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fypapplication1.CreateGroupChatActivity;
import com.example.fypapplication1.MainActivity;
import com.example.fypapplication1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ProfileFragment extends Fragment {

    //Firebase
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    //Path for storage
    String storagePath = "Users_Profile_Imgs/";


    //Views from xml
    ImageView avatarTv;
    TextView nameTv, emailTv, phoneTv, coursecodeTv, yearTv, aboutMeTv, dobTv;
    FloatingActionButton fab;

    //permissions for editing user profile picture
    private static final int CAMERA_REQUEST_CODE=100;
    private static final int STORAGE_REQUEST_CODE=200;
    private static final int IMAGE_PICK_GALLERY_CODE=300;
    private static final int IMAGE_PICK_CAMERA_CODE=400;
    //permission arrays
    String cameraPermissions[];
    String storagePermissions[];

    //uri of images
    Uri image_uri;

    //check profile photo
    String profilePhoto;


    //progress dialog
    ProgressDialog pd;



    //Empty constructor
    public ProfileFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        storageReference = getInstance().getReference(); //Firebase storage reference

        //permission arrays
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //views
        avatarTv=view.findViewById(R.id.avatarTv);
        nameTv=view.findViewById(R.id.nameTv);
        emailTv=view.findViewById(R.id.emailTv);
        phoneTv=view.findViewById(R.id.phoneTv);
        yearTv = view.findViewById(R.id.yearTV);
        coursecodeTv=view.findViewById(R.id.coursecodeTV);
        aboutMeTv=view.findViewById(R.id.aboutmeTV);
        dobTv=view.findViewById(R.id.dobTV);
        fab=view.findViewById(R.id.fab);

        //progress dialog
        pd = new ProgressDialog(getActivity());


        //Get user info
        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    //get data
                    String name = ""+ ds.child("name").getValue();
                    String email = ""+ ds.child("email").getValue();
                    String phone = ""+ ds.child("phone").getValue();
                    String image = ""+ ds.child("image").getValue();
                    String aboutMe = ""+ ds.child("information").getValue();
                    String year = ""+ ds.child("college year").getValue();
                    String coursecode = ""+ ds.child("coursecode").getValue();
                    String dob = ""+ ds.child("dob").getValue();

                    //set data
                    nameTv.setText(name);
                    emailTv.setText(email);
                    phoneTv.setText(phone);
                    aboutMeTv.setText(aboutMe);
                    yearTv.setText(year);
                    coursecodeTv.setText(coursecode);
                    dobTv.setText(dob);

                    try {
                        //Set the image once recieved
                        Picasso.get().load(image).into(avatarTv);
                    }
                    catch (Exception e) {
                        //Set default image if there is an exception
                        Picasso.get().load(R.drawable.add_image_icon).into(avatarTv);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Floating action Button click
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfileDialog();
            }
        });

        return view;
    }

    private boolean checkStoragePermission(){
        //To check if the storage permission is or isnt enabled, true will be returned if it is enabled, false will be returned if its not.
        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission() {
        //Runtime storage permissions
        requestPermissions(storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission(){
        //To check if the storage permission is or isnt enabled, true will be returned if it is enabled, false will be returned if its not.
        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission() {
        //Runtime storage permissions
        requestPermissions(cameraPermissions, CAMERA_REQUEST_CODE);
    }

    private void editProfileDialog() {
        //Options for the dialog pop up
        String options[] = {"Edit Profile Picture","Edit Name","Edit Phone Number", "Edit Date of Birth", "Edit about me Information", "Edit Course Code", "Edit College Year"};
        //Alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //Set title
        builder.setTitle("Edit your profile:");
        //set items to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //for dialog item clicks
                if (which == 0) {
                    //If edit profile picture is clicked
                    pd.setMessage("Updating your profile picture..");
                    profilePhoto="image";
                    profilePictureDialog();
                } else if (which == 1) {
                    //If edit name is clicked
                    pd.setMessage("Updating your name..");
                    namePhoneUpdateDialog("name");
                }else if (which == 2) {
                    //if edit phone number is clicked
                    pd.setMessage("Updating your phone number..");
                    namePhoneUpdateDialog("phone");
                }else if (which == 3) {
                    //if edit dob is clicked
                    pd.setMessage("Updating your date of birth..");
                    namePhoneUpdateDialog("dob");
                }else if (which == 4) {
                    //if edit about me information is clicked
                    pd.setMessage("Updating your information..");
                    namePhoneUpdateDialog("information");
                }else if (which == 5) {
                    //if edit course code is clicked
                    pd.setMessage("Updating your course code..");
                    namePhoneUpdateDialog("coursecode");
                }else if (which == 6) {
                    //if edit college year is clicked
                    pd.setMessage("Updating your college year..");
                    namePhoneUpdateDialog("year");
                }
            }
        });
        //Create dialog and display
        builder.create().show();
    }

    //Key contains value name and phone
    private void namePhoneUpdateDialog(final String key) {
        //customDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Update " + key);
        //Set layout of dialog
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10,10,10,10);
        EditText editText = new EditText(getActivity());
        editText.setHint("Enter " + key);
        linearLayout.addView(editText);

        builder.setView(linearLayout);

        //Add button for dialog
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //input text from edit text
                String value = editText.getText().toString().trim();
                //Validation
                if (!TextUtils.isEmpty(value)) {
                    pd.show();
                    HashMap<String, Object> result = new HashMap<>();
                    result.put(key,value);

                    databaseReference.child(user.getUid()).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //Updated, dismiss progress
                                    pd.dismiss();
                                    Toast.makeText(getActivity(), "Updated!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    pd.dismiss();
                                    Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });
                } else {
                    Toast.makeText(getActivity(), "Please enter "+ key, Toast.LENGTH_SHORT).show();
                }
            }
        });
        //cancel button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        //Create and show dialog
        builder.create().show();

    }

    private void profilePictureDialog() {
       //Display dialog to select profile picture from android camera and gallery
        //Options for the dialog pop up
        String options[] = {"Camera","Photo Gallery"};
        //Alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //Set title
        builder.setTitle("Add a new profile picture:");
        //set items to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //for dialog item clicks
                if (which == 0) {
                    //when camera is clicked

                    if (!checkCameraPermission()){
                        requestCameraPermission();
                    } else {
                        pickFromCamera();
                    }

                } else if (which == 1) {
                    //when photo gallery is clicked
                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        pickFromPhotoGallery();
                    }

                }
            }
        });
        //Create dialog and display
        builder.create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //For when the user selects allow or deny permission

        switch (requestCode) {
            case CAMERA_REQUEST_CODE:{
                //To check if camera and storage permissions are allowed
                if (grantResults.length>0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && writeStorageAccepted){
                        //If camera and storage permissions are enabled
                        pickFromCamera();
                    } else {
                        //If they are not enabled
                        Toast.makeText(getActivity(), "Enable camera and storage permissions", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE: {
                //To check if gallery and storage permissions are allowed
                if (grantResults.length>0) {
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAccepted){
                        //If camera and storage permissions are enabled
                        pickFromPhotoGallery();
                    } else {
                        //If they are not enabled
                        Toast.makeText(getActivity(), "Enable storage permissions", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //For when an image from camera/gallery is picked
        if (resultCode == RESULT_OK) {

            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                //image is picked from gallery, get uri of image
                image_uri = data.getData();

                uploadProfilePhoto(image_uri);

            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                //image is picked from camera, get uri of image
                uploadProfilePhoto(image_uri);

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    private void uploadProfilePhoto(final Uri uri) {
        String filePathAndName = storagePath+ ""+ profilePhoto + "_" + user.getUid();
        StorageReference storageReference2 = storageReference.child(filePathAndName);
        storageReference2.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //Image has successfully been uploaded to storage
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        Uri downloadUri = uriTask.getResult();

                        //Check if image is uploaded or not and url is recieved
                        if (uriTask.isSuccessful()) {
                            //Image uploaded
                            //add or update url in users database
                            HashMap<String, Object> results = new HashMap<>();
                            results.put(profilePhoto, downloadUri.toString());

                            databaseReference.child(user.getUid()).updateChildren(results)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            //url is added successfully
                                            pd.dismiss();
                                            Toast.makeText(getActivity(), "Your profile Picture has been updated!", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            //error adding url to the database
                                            pd.dismiss();
                                            Toast.makeText(getActivity(), "Your profile Picture has not been updated!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            //error
                            pd.dismiss();
                            Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Image has unsuccessfully been uploaded to storage
                        pd.dismiss();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void pickFromPhotoGallery(){
        Intent photoGalleryIntent = new Intent(Intent.ACTION_PICK);
        photoGalleryIntent.setType("image/*");
        startActivityForResult(photoGalleryIntent, IMAGE_PICK_GALLERY_CODE);


    }
    private void pickFromCamera(){
        //Intent for picking image from android camera
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Temporary Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temporary Description");

        //Set image uri
        image_uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        //Start camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);

    }
    private void checkUserStatus() {
        //get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            //User is signed in stay here
            //mProfileTv.setText(user.getEmail());
        } else {
            //user not signed in, go to main activity
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true); //to show menu option in fragment
        super.onCreate(savedInstanceState);
    }

    //Inflate options menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        menu.findItem(R.id.action_create_group).setVisible(false);
        menu.findItem(R.id.action_add_participant).setVisible(false);
        menu.findItem(R.id.action_create_event).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_information).setVisible(false);

        super.onCreateOptionsMenu(menu, inflater);
    }
    //Handle menu item clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //get item id
        int id = item.getItemId();
        if (id == R.id.action_logout){
            firebaseAuth.signOut();
            checkUserStatus();
        }
        else if (id==R.id.action_create_group) {
            startActivity(new Intent(getActivity(), CreateGroupChatActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}