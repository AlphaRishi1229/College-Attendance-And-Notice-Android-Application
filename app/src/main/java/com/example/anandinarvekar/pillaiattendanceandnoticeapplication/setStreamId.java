package com.example.anandinarvekar.pillaiattendanceandnoticeapplication;


public class setStreamId {

    private String id;
    private String name;

    public setStreamId(String id , String name)
    {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof setStreamId)
        {
            setStreamId streamID = (setStreamId)obj;
            if (streamID.getName().equals(name) && streamID.getId().equals(id))
            {
                return true;
            }
        }

        return false;
    }
}

