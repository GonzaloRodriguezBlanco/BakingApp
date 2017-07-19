/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.data.local.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "steps",
        indices = {
                @Index(value = {"recipe_id"})
        },
        foreignKeys = @ForeignKey(
                entity = RecipeEntity.class,
                parentColumns = "id",
                childColumns = "recipe_id"
        )
)
public class StepEntity {
    @PrimaryKey
    @ColumnInfo(name = "id")
    private long mId;

    @ColumnInfo(name = "shortDescription")
    private String mShortDescription;

    @ColumnInfo(name = "description")
    private String mDescription;

    @ColumnInfo(name = "videoURL")
    private String mVideoUrl;

    @ColumnInfo(name = "thumbnailURL")
    private String mThumbnailUrl;

    @ColumnInfo(name = "recipe_id")
    private long mRecipeId;

    public StepEntity(long id,
                      String shortDescription,
                      String description,
                      String videoUrl,
                      String thumbnailUrl,
                      long recipeId) {
        this.mId = id;
        this.mShortDescription = shortDescription;
        this.mDescription = description;
        this.mVideoUrl = videoUrl;
        this.mThumbnailUrl = thumbnailUrl;
        this.mRecipeId = recipeId;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public String getShortDescription() {
        return mShortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.mShortDescription = mShortDescription;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public String getVideoUrl() {
        return mVideoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.mVideoUrl = videoUrl;
    }

    public String getThumbnailUrl() {
        return mThumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.mThumbnailUrl = thumbnailUrl;
    }

    public long getRecipeId() {
        return mRecipeId;
    }

    public void setRecipeId(long recipeId) {
        this.mRecipeId = recipeId;
    }
}
