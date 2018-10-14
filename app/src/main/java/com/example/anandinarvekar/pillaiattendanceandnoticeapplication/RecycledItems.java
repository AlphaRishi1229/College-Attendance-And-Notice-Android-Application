package com.example.anandinarvekar.pillaiattendanceandnoticeapplication;

import android.widget.CheckBox;

public class RecycledItems {

    public String Name;
    public String Roll;
    public boolean isSelected;

    public RecycledItems(String Name, String Roll)
    {
        this.Name = Name;
        this.Roll = Roll;
    }
    public RecycledItems(String Name, String Roll, Boolean isSelected)
    {
        this.Name = Name;
        this.Roll = Roll;
        this.isSelected = isSelected;
    }

    public String getName() {
        return Name;
    }

    public String getRoll() {
        return Roll;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }
}
