package com.diezel.cryptofeed.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * A generic parent object which implementation model classes can inherit from to provide them with any common or shared
 * properties and methods.
 * WARNING: Properties in this class make NO guarantee to be actual values. Please DO check for null references.
 *
 * @author dzale
 */
@JsonIgnoreProperties(ignoreUnknown = true)   // Ignore any unknown properties in JSON input w/o exception.
public abstract class DiezelModel {

    /**
     * This objects primary key "id" or row number in the database. It helps model objects to have this field when
     * mapping to / from domain objects using ModelMapper, otherwise it may get confused between other fields with the
     * characters id in them, in which case a set of custom rules need to be added for each occurrence.
     */
    @JsonIgnore
    protected Long id;

    /**
     * If the model is part of a heirarchy, this object reference points to the model's parent object.
     */
    @JsonIgnore
    protected Object parent;

    public DiezelModel() {
        this.id = null;
        this.parent = null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Object getParent() {
        return parent;
    }

    public void setParent(Object parent) {
        this.parent = parent;
    }

    /**
     * Iterates over the list of model objects and sets their parent object reference.
     *
     * @param children a list of DiezelModel objects
     * @param parent   the parent of the objects in the list
     */
    protected void setChildrenParentReference(List<? extends DiezelModel> children, Object parent) {
        if (children != null)
            children.forEach(child -> child.setParent(parent));
    }

    public boolean isEntity() {
        return this.id == null ? false : true;
    }

}
