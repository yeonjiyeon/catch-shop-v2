package com.sparta.week05.models;

import lombok.Getter;
import org.json.JSONObject;

@Getter
public class ItemDto {

    private String title;
    private String image;
    private String link;
    private int lprice;

    public ItemDto(JSONObject itemJson) {
        this.title = itemJson.getString("title");
        this.image = itemJson.getString("image");
        this.link = itemJson.getString("link");
        this.lprice = itemJson.getInt("lprice");
    }
}
