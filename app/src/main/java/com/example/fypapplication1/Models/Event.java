//Student Name: Megan Cash
//Student Number: C19317723
package com.example.fypapplication1.Models;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

public class Event {


    private String id;
    private List<String> organisersId = new ArrayList<>();
    private List<String> participantsId = new ArrayList<>();
    private String name;
    private String description;
    private Long timestamp;
    private String location;
    private String additionalInformation;


    //Empty constructor
    public Event() {
    }

    //Constructor
    public Event(String id, List<String> organisersId, List<String> participantsId, String name, String description, Long timestamp, String location, String additionalInformation) {
        this.id = id;
        this.organisersId = organisersId;
        this.participantsId = participantsId;
        this.name = name;
        this.description = description;
        this.timestamp = timestamp;
        this.location = location;
        this.additionalInformation = additionalInformation;
    }

    //Getter & Setter Methods
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getOrganisersId() {
        return organisersId;
    }

    public void setOrganisersId(List<String> organisersId) {
        this.organisersId = organisersId;
    }

    public List<String> getParticipantsId() {
        return participantsId;
    }

    public void setParticipantsId(List<String> participantsId) {
        this.participantsId = participantsId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }
}



