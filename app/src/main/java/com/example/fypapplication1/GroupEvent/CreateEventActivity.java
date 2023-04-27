package com.example.fypapplication1.GroupEvent;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import com.example.fypapplication1.Models.Event;
import com.example.fypapplication1.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class CreateEventActivity extends AppCompatActivity {
    static final private String TAG = CreateEventActivity.class.getSimpleName();
    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        /*if (getIntent().hasExtra("event")) {
            event = getIntent().getParcelableExtra("event");
        } else {
            event = new Event();
            String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser(), "Firebase user is null").getUid();
            event.getOrganizersIds().add(userId);
            event.getParticipantsIds().add(userId);
        }

        dataBinding.textDatePicked.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) onDateTime(v);
        });
        dataBinding.textTimePicked.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) onDateTime(v);
        });

    }

    public void onDateTime(View view) {
        if (view == dataBinding.textDatePicked) {
            handleDate();
        } else if (view == dataBinding.textTimePicked) {
            handleTime();
        }
    }

    public static String timestampToDateString(Long timestamp) {
        if (timestamp == null) {
            return null;
        }
        return SimpleDateFormat.getDateInstance(DateFormat.SHORT).format(new Date(timestamp));
    }

    public static String timestampToTimeString(Long timestamp) {
        if (timestamp == null) {
            return null;
        }
        return SimpleDateFormat.getTimeInstance(DateFormat.SHORT).format(new Date(timestamp));
    }

    public void onAddChip(View view) {
        Editable input = null;
        if (view == dataBinding.foodChipButton) {
            input = dataBinding.textInputFood.getText();
        } else if (view == dataBinding.drinksChipButton) {
            input = dataBinding.textInputDrinks.getText();
        }
        if (input == null || input.length() == 0) {
            return;
        }
        String name = input.toString().trim();
    }

    public void onSubmit(View view) {
        if (inputIsValid()) {
            DocumentReference firestoreDocument;
            if (event.getId() == null || event.getId().isEmpty()) {
                firestoreDocument = FirebaseFirestore.getInstance().collection("parties").document();
            } else {
                firestoreDocument = FirebaseFirestore.getInstance().collection("parties").document(event.getId());
            }
            firestoreDocument.set(event)
                    .addOnSuccessListener(aVoid -> {
                        Log.i(TAG, "Party persisted.");
                        Toast.makeText(CreateEventActivity.this.getApplicationContext(), "Party created!", Toast.LENGTH_SHORT).show();
                        CreateEventActivity.this.finish();
                    })
                    .addOnFailureListener(e -> {
                        e.printStackTrace();
                        Log.e(TAG, "Persisting party failed");
                        Snackbar.make(dataBinding.getRoot(), "Saving failed", Snackbar.LENGTH_SHORT).show();
                    });
        }
    }

    private void handleDate() {
        // Get current date or set one chosen previously
        final Calendar calendar = Calendar.getInstance();
        if (event.getTimestamp() != null) {
            calendar.setTimeInMillis(event.getTimestamp());
        }
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, R.style.DialogTheme,
                (view, year, monthOfYear, dayOfMonth) -> {
                    calendar.set(year, monthOfYear, dayOfMonth);
                    event.setTimestamp(calendar.getTimeInMillis());
                },
                currentYear, currentMonth, currentDay
        );
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
        dataBinding.textDatePicked.setError(null);
    }

    private void handleTime() {
        // Get current time or set one chosen previously
        final Calendar calendar = Calendar.getInstance();
        if (event.getTimestamp() != null) {
            calendar.setTimeInMillis(event.getTimestamp());
        }
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        new TimePickerDialog(
                this, R.style.DialogTheme,
                (view, hourOfDay, minute) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    event.setTimestamp(calendar.getTimeInMillis());
                },
                currentHour, currentMinute, true
        ).show();
    }

    private void setupChips(ChipGroup chipGroup, Collection<String> defaultNames, List<String> selectedNames) {
        Set<String> allChips = new LinkedHashSet<>(defaultNames);
        allChips.addAll(selectedNames);
        for (String chipText : allChips) {
            addChipView(chipGroup, selectedNames, chipText);
        }
    }

    private void addChipView(ChipGroup chipGroup, List<String> selectedNames, String name) {
        Chip chip = new Chip(this);

        ChipDrawable drawable = ChipDrawable.createFromAttributes(this, null, 0, R.style.CustomChipStyle);  //change to CustomChip style in the future
        chip.setChipDrawable(drawable);
        int paddingDp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 10,
                getResources().getDisplayMetrics()
        );
        chip.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
        chip.setText(name);

        if (selectedNames.contains(name)) {
            chip.setChecked(true);
            chip.setOnLongClickListener(v -> {
                selectedNames.remove(name);
                chipGroup.removeView(chip);
                return true;
            });
        }
        chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedNames.add(buttonView.getText().toString());
            } else {
                selectedNames.remove(buttonView.getText().toString());
            }
        });
        chipGroup.addView(chip);
    }

    private boolean isChipNameUnique(Collection<String> defaultChips, Collection<String> selectedChips, String name) {
        HashSet<String> allChips = new HashSet<>(defaultChips);
        allChips.addAll(selectedChips);
        return !allChips.contains(name);
    }

    private boolean inputIsValid() {
        validateName();
        validateDate();
        if (dataBinding.textPartyName.getError() != null || dataBinding.textDatePicked.getError() != null) {
            return false;
        }

        return true;
    }

    private void validateName() {
        if (dataBinding.textPartyName.getText().toString().trim().isEmpty()) {
            dataBinding.textPartyName.setError("Name can't be empty");
            dataBinding.CreatePartyScrollView.smoothScrollTo(0, 0);
        } else {
            dataBinding.textPartyName.setError(null);
        }
    }

    private void validateDate() {
        if (dataBinding.textDatePicked.getText().toString().trim().isEmpty()) {
            dataBinding.textDatePicked.setError("Choose date");
            dataBinding.CreatePartyScrollView.smoothScrollTo(0, 0);
        } else {
            dataBinding.textDatePicked.setError(null);
        }*/
    }
}