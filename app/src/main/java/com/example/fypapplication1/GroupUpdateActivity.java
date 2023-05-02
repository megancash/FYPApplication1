//Student Name: Megan Cash
//Student Number: C19317723
package com.example.fypapplication1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class GroupUpdateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_update);


        /*DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Groups");

        BottomSheetDialog editGroupTitle, editGroupDesc, EditGroupIcon;
        ProgressDialog progressDialog;

        String groupId, myGroupRole;

        private static final int CAMERA_REQUEST_CODE = 100;
        private static final int STORAGE_REQUEST_CODE = 200;

        private static final int IMAGE_PICK_CAMERA_CODE = 300;
        private static final int IMAGE_PICK_GALLERY_CODE = 400;

        private String[] cameraPermissions;
        private String[] storagePermissions;

        private Uri imageUri = null;

        @Override
        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            binding = ActivityEditGroupInfoBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            Intent intent = getIntent();
            groupId = intent.getStringExtra("GroupId");
            myGroupRole = intent.getStringExtra("myGroupRole");

            Log.d("TAG", "onCreateGroup: GroupId in EditGroupInfo: " + groupId);
            Log.d("TAG", "onCreateGroup: myGroupRole in EditGroupInfo: " + myGroupRole);

            binding.btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            binding.editGroupTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View view = getLayoutInflater().inflate(R.layout.bottom_sheet_edit_group_title, null);

                    ((View) view.findViewById(R.id.btnCancel)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            editGroupTitle.dismiss();
                        }
                    });
                    final EditText edNewTitle = view.findViewById(R.id.edNewTitle);
                    ((View) view.findViewById(R.id.btnSave)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (edNewTitle.getText().toString().isEmpty()) {
                                Toast.makeText(GroupUpdateActivity.this, "Enter group title first. Group title can't be empty.", Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                //updateBio(edUserBio.getText().toString());
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("groupTitle", "" + edNewTitle.getText().toString());

                                ref.child(groupId).updateChildren(hashMap);
                                editGroupTitle.dismiss();

                                Toast.makeText(GroupUpdateActivity.this, "Group name has been updated successfully.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    editGroupTitle = new BottomSheetDialog(GroupUpdateActivity.this);
                    editGroupTitle.setContentView(view);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Objects.requireNonNull(editGroupTitle.getWindow()).addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    }

                    editGroupTitle.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            editGroupTitle = null;
                        }
                    });

                    editGroupTitle.show();
                }
            });

            binding.editGroupDesc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View view = getLayoutInflater().inflate(R.layout.bottom_sheet_edit_group_desc, null);

                    ((View) view.findViewById(R.id.btnCancel)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            editGroupDesc.dismiss();
                        }
                    });
                    final EditText edNewDesc = view.findViewById(R.id.edNewDesc);
                    ((View) view.findViewById(R.id.btnSave)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("groupDescription", "" + edNewDesc.getText().toString());

                            ref.child(groupId).updateChildren(hashMap);
                            editGroupTitle.dismiss();

                            Toast.makeText(GroupUpdateActivity.this, "Group description has been updated successfully.", Toast.LENGTH_SHORT).show();
                        }
                    });

                    editGroupDesc = new BottomSheetDialog(GroupUpdateActivity.this);
                    editGroupDesc.setContentView(view);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Objects.requireNonNull(editGroupDesc.getWindow()).addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    }

                    editGroupDesc.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            editGroupDesc = null;
                        }
                    });

                    editGroupDesc.show();
                }
            });

        }*/
    }
}