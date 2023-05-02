//Student Name: Megan Cash
//Student Number: C19317723
package com.example.fypapplication1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fypapplication1.Adapters.AdapterParticipantAdd;
import com.example.fypapplication1.Models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class GroupInformationActivity extends AppCompatActivity {

    //permissions
    private static final int CAMERA_REQUEST_CODE =100;
    private static final int STORAGE_REQUEST_CODE =200;
    private static final int IMAGE_PICK_CAMERA_CODE =300;
    private static final int IMAGE_PICK_GALLERY_CODE =400;
    //array of permissions
    private String[] cameraPermissions;
    private String[] storagePermissions;
    //picked image uri
    private Uri image_uri = null;


    String groupId, uid;
    String myGroupRole = "", role;
    ArrayList<User> userList;
    AdapterParticipantAdd adapterParticipantAdd;

    //Firebase
    FirebaseAuth auth;
    FirebaseUser user;
    //FirebaseFirestore firestore;
    FirebaseDatabase database;

    DatabaseReference databasereference = FirebaseDatabase.getInstance().getReference("Groups");
    BottomSheetDialog updateTitleBSD, updateDescriptionBSD, bottomSheetDialog;
    private int IMAGE_GALLERY_REQUEST = 111;
    private Uri imageUri;
    ProgressDialog progressDialog;

    RecyclerView participantsRecyclerview;
    TextView participantsListTitle, leaveGroupchatButton,addParticipantsButton, deleteGroupchatButton, groupCreatedBy, groupDescription, groupHeading;
    ImageView updateDescription, updateImage, updateTitle, groupImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_information);

        //Firebase
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(this );
        groupId = getIntent().getStringExtra("groupId");

        //Views
        groupHeading= findViewById(R.id.groupHeading);
        groupDescription= findViewById(R.id.groupDescription);
        groupCreatedBy= findViewById(R.id.groupCreatedBy);
        groupImage = findViewById(R.id.groupImage);
        updateTitle= findViewById(R.id.UpdateTitle);
        updateImage= findViewById(R.id.UpdateImage);
        updateDescription= findViewById(R.id.UpdateDescription);
        addParticipantsButton= findViewById(R.id.addParticipantsButton);
        deleteGroupchatButton= findViewById(R.id.deleteGroupchatButton);
        leaveGroupchatButton= findViewById(R.id.leaveGroupchatButton);
        participantsListTitle = findViewById(R.id.participantsListTitle);
        participantsRecyclerview = findViewById(R.id.participantsRecyclerview);

        loadGroupInformation();
        loadMyGroupRoles();

        addParticipantsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupInformationActivity.this, AddParticipantsActivity.class);
                intent.putExtra("groupId", groupId);
                startActivity(intent);
            }
        });



        leaveGroupchatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(GroupInformationActivity.this).setTitle("Leave Group").setMessage("Are you sure that you want to leave the group.").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            DatabaseReference databasereference = FirebaseDatabase.getInstance().getReference("Groups");
                            databasereference.child(groupId).child("Participants").child(user.getUid()).setValue(null);
                            Intent intent = new Intent(GroupInformationActivity.this, MainActivity.class);
                            startActivity(intent);
                        } catch (Exception e) {
                        }

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        });
        deleteGroupchatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(GroupInformationActivity.this).setTitle("Delete Group")
                        .setMessage("Deleting the group will delete all the messages and media sent in the group chat \n\nAre you sure that you want to delete the group.")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    DatabaseReference databasereference = FirebaseDatabase.getInstance().getReference("Groups");
                                    databasereference.child(groupId).setValue(null);
                                    Intent intent = new Intent(GroupInformationActivity.this, MainActivity.class);
                                    startActivity(intent);
                                } catch (Exception e) {
                                }

                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });

    }


    private void loadGroupInformation() {
        DatabaseReference databasereference = FirebaseDatabase.getInstance().getReference("Groups");
        databasereference.orderByChild("groupId").equalTo(groupId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    //get group info
                    String groupId = "" + ds.child("groupId").getValue();
                    String groupTitle1 = "" + ds.child("groupTitle").getValue();
                    String groupDescription1 = "" + ds.child("groupDescription").getValue();
                    String groupImage1 = "" + ds.child("groupImage").getValue();
                    String createdBy = "" + ds.child("createdBy").getValue();
                    String timestamp = "" + ds.child("timestamp").getValue();

                    Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                    calendar.setTimeInMillis(Long.parseLong(timestamp));
                    String dateTime = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();

                    loadCreatorInfo(createdBy, dateTime);

                    //set group info
                    groupHeading.setText(groupTitle1);
                    groupDescription.setText(groupDescription1);

                    try {
                        Picasso.get().load(groupImage1).placeholder(R.drawable.group_chat_icon).into(groupImage);

                    } catch (Exception e) {
                        groupImage.setImageResource(R.drawable.group_chat_icon);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void loadCreatorInfo(String createdBy, String dateTime) {

       DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
       reference.orderByChild("uid").addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               for (DataSnapshot ds: snapshot.getChildren()) {
                   String groupId = ""+ds.child("groupId").getValue();
                   String groupTitle = ""+ds.child("groupTitle").getValue();
                   String groupDescription= ""+ds.child("groupDescription").getValue();
                   String groupIcon = ""+ds.child("groupIcon").getValue();
                   String timestamp = ""+ds.child("timestamp").getValue();
                   String createdBy = ""+ds.child("createdBy").getValue();


                       Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                       calendar.setTimeInMillis(Long.parseLong(timestamp));
                       String dateTime = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });


    }

    private void loadMyGroupRoles() {

        DatabaseReference databasereference = FirebaseDatabase.getInstance().getReference("Groups");
        databasereference.child(groupId).child("Participants").orderByChild("uid").equalTo(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    myGroupRole = "" + ds.child("role").getValue();

                    switch (myGroupRole) {
                        case "participant":
                            leaveGroupchatButton.setVisibility(View.VISIBLE);
                            addParticipantsButton.setVisibility(View.GONE);
                            deleteGroupchatButton.setVisibility(View.GONE);
                            break;
                        case "admin":
                            leaveGroupchatButton.setVisibility(View.VISIBLE);
                            deleteGroupchatButton.setVisibility(View.GONE);
                            break;
                        case "creator":
                            deleteGroupchatButton.setVisibility(View.VISIBLE);
                            leaveGroupchatButton.setVisibility(View.GONE);
                            break;
                    }
                }
                loadParticipants();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void loadParticipants() {
        userList = new ArrayList<>();

        DatabaseReference databasereference = FirebaseDatabase.getInstance().getReference("Groups");
        databasereference.child(groupId).child("Participants").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    String uid = ""+ds.child("uid").getValue();
                    DatabaseReference databasereference = FirebaseDatabase.getInstance().getReference("Users");
                    databasereference.orderByChild("uid").equalTo(uid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds: snapshot.getChildren()) {
                                User user = ds.getValue(User.class);
                                userList.add(user);
                            }
                            adapterParticipantAdd = new AdapterParticipantAdd(GroupInformationActivity.this, userList, groupId, myGroupRole);
                            participantsRecyclerview.setAdapter(adapterParticipantAdd);
                            participantsListTitle.setText("Participants ("+userList.size()+")");
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            Log.d("TAG", "onActivityResult: imageUri: "+imageUri);

            if (imageUri != null)
            {
                progressDialog.setMessage("Uploading");
                progressDialog.show();

                StorageReference uploadReference = FirebaseStorage.getInstance().getReference().child("Group_Profile/").child(FirebaseAuth.getInstance().getUid());
                uploadReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!urlTask.isSuccessful());
                        Uri downloadUrl = urlTask.getResult();

                        final String sdownload_url = String.valueOf(downloadUrl);

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("groupImage", sdownload_url);

                        progressDialog.dismiss();
                        databasereference.child(groupId).updateChildren(hashMap);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"upload Failed",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
            }

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                groupImage.setImageBitmap(bitmap);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        if (requestCode == 440 && resultCode == RESULT_OK){
            uploadToFirebase();
        }
    }

    private void uploadToFirebase() {
        if (imageUri!=null){
            progressDialog.setMessage("Uploading...");
            progressDialog.show();

            StorageReference uploadReference = FirebaseStorage.getInstance().getReference().child("Group_Profile/").child(FirebaseAuth.getInstance().getUid());
            uploadReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful());
                    Uri downloadUrl = urlTask.getResult();

                    final String sdownload_url = String.valueOf(downloadUrl);

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("groupImage", sdownload_url);

                    progressDialog.dismiss();
                    databasereference.child(groupId).updateChildren(hashMap);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),"upload Failed",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }
    }
}