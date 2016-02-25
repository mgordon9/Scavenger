package com.project.ece150.scavenger.remote.parser;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Base64;

import com.project.ece150.scavenger.IObjective;
import com.project.ece150.scavenger.Objective;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

/**
 * Helper object for parsing JSON to IObjective objects
 */
public class ObjectiveParser {
    public ObjectiveParser() {
    }

    public List<IObjective> parseJSONToObjectives(JSONArray json) {

        LinkedList<IObjective> list = new LinkedList<IObjective>();

        for(int i=0; i<json.length(); i++) {
            try {
                JSONObject jObjective = json.getJSONObject(i);

                list.push(parseJSONToObjective(jObjective));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    public IObjective parseJSONToObjective(JSONObject jObjective) {

        Objective objective = new Objective();

        try {
            // parse objective
            objective.setObjectiveid(jObjective.getString("keyname"));
            objective.setTitle(jObjective.getString("title"));
            objective.setInfo(jObjective.getString("info"));
            objective.setLatitude(Double.parseDouble(jObjective.getString("latitude")));
            objective.setLongitude(Double.parseDouble(jObjective.getString("longitude")));
            objective.setOwner(jObjective.getString("owner"));

            // parse image
            objective.setImage(parseStringToBitmap(jObjective.getString("image")));

            // parse thumbnail
            objective.setThumbnail(parseStringToBitmap(jObjective.getString("thumbnail")));

//          TODO: Parse OtherConfirmedUsers
//          objective.setOtherConfirmedUsers(jObjective.getString("otherConfirmedUsers"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return objective;
    }

    public JSONObject parseObjectiveToJSON(IObjective objective) {

        // parse objective
        JSONObject jObjective = new JSONObject();
        try {
            jObjective.put("keyname", objective.getObjectiveid());
            jObjective.put("title", objective.getTitle());
            jObjective.put("info", objective.getInfo());
            jObjective.put("latitude", objective.getLatitude());
            jObjective.put("longitude", objective.getLongitude());
            jObjective.put("owner", objective.getOwner());
            jObjective.put("otherConfirmedUsers", "<tbd>");
            jObjective.put("activity", "<tbd>");

            // parse image
            jObjective.put("image", parseBitmapToString(objective.getImage()));

            // resize and parse image to thumbnail
            Bitmap thumbnail = resize(objective.getImage(), 50, 50);
            jObjective.put("thumbnail", parseBitmapToString(thumbnail));

        } catch(Exception e) {
            e.printStackTrace();
        }

        return jObjective;
    }

    private Bitmap parseStringToBitmap(String encodedImage) {

        if(encodedImage != null) {
            byte[] decodedImageBytes = Base64.decode(encodedImage, Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(decodedImageBytes, 0, decodedImageBytes.length);

            return decodedImage;
        }

        return null;
    }

    private String parseBitmapToString(Bitmap decodedImage) {

        if(decodedImage != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            decodedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] byteArrayImage = baos.toByteArray();
            return Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
        }

        return null;
    }

    // taken from http://stackoverflow.com/questions/15440647/scaled-bitmap-maintaining-aspect-ratio
    private Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > 1) {
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }
}
