package com.jbackup.application.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Model class for data of a file.
 */
public class FileData {

    private final StringProperty path;
    private final StringProperty size;
    private final StringProperty changeDate;
    
    private final long lastModified;

    /**
     * Default constructor.
     */
    public FileData() {
        this(null, null, null, 0);
    }

    /**
     * Constructor with some initial data.
     *
     * @param firstName
     * @param lastName
     */
    public FileData(String path, String size, String changeDate, long lastModified) {
        this.path = new SimpleStringProperty(path);
        this.size = new SimpleStringProperty(size);
        this.changeDate = new SimpleStringProperty(changeDate);
        this.lastModified = lastModified;
    }

    public String getPath() {
        return path.get();
    }

    public void setPath(String path) {
        this.path.set(path);
    }

    public StringProperty pathProperty() {
        return path;
    }

    public String getSize() {
        return size.get();
    }

    public void setSize(String size) {
        this.size.set(size);
    }

    public StringProperty sizeProperty() {
        return size;
    }

    public String getChangeDate() {
        return changeDate.get();
    }

    public void setChangeDate(String changeDate) {
        this.changeDate.set(changeDate);
    }

    public StringProperty changeDateProperty() {
        return changeDate;
    }
    
    public long getLastModified() {
    	return lastModified;
    }
}