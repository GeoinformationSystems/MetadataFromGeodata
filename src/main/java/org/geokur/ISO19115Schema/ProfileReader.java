/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19115Schema;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class ProfileReader {
    public static Profile profile;

    public ProfileReader() {}

    public static void setProfile(String profileFilename) {
        // read profile json file

        Gson gson = new Gson();
        try {
            JsonReader reader = new JsonReader(new FileReader(profileFilename));
            profile = gson.fromJson(reader, Profile.class);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
