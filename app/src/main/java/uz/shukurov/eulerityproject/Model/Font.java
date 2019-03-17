package uz.shukurov.eulerityproject.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Font {

    private String family;
    private String url;
    private boolean bold;
    private boolean italic;

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public boolean isItalic() {
        return italic;
    }

    public void setItalic(boolean italic) {
        this.italic = italic;
    }

    // Decodes font json into font model object
    public static Font fromJson(JSONObject jsonObject){
        Font font = new Font();
        try {
            font.url = jsonObject.getString("url");
            font.family = jsonObject.getString("family");
            font.bold = jsonObject.getBoolean("bold");
            font.italic = jsonObject.getBoolean("italic");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return font;
    }

    // Creates ArrayList of Font objects
    public static ArrayList<Font> fromJson(JSONArray jsonArray){

        JSONObject fontJson;

        ArrayList<Font> fonts = new ArrayList<Font>(jsonArray.length());

        for(int i=0; i<jsonArray.length();i++){
            try {
                fontJson = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }

            Font font = Font.fromJson(fontJson);
            if (font != null){
                fonts.add(font);
            }

        }

        return fonts;
    }


}

