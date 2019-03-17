package uz.shukurov.eulerityproject;

import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import uz.shukurov.eulerityproject.Model.CreatingFontFamilies;
import uz.shukurov.eulerityproject.Model.Font;
import uz.shukurov.eulerityproject.Model.FontFamily;
import uz.shukurov.eulerityproject.other.RequestCode;

public class MainActivity extends AppCompatActivity {

    ArrayList<Font> fonts;
    CreatingFontFamilies families = new CreatingFontFamilies();
    String fontName;

    //Initializing
    private TextView mTextView;
    private Button mButton;
    private CheckBox mChItalic;
    private CheckBox mChBold;
    private TextView tvPostResponse;
    private Spinner spinner;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, RequestCode.getRequestUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Get Response:",response);

                        parseJson(response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error here:",error.toString());
            }
        });


        // Add the request to the RequestQueue.
        queue.add(stringRequest);


        //Storage read and write permission request
        permissionRequest();


        givingView();

    }

    private void givingView() {
        mTextView = findViewById(R.id.textView);
        mButton = findViewById(R.id.button);
        tvPostResponse = findViewById(R.id.post_request_response);
        spinner = findViewById(R.id.spinner);
    }

    private void parseJson(String jsonStr) {

        Log.d("JSON",jsonStr);
        ArrayList<Font> fonts2 = null;
        try {
            JSONArray jsonarray = new JSONArray(jsonStr);

            fonts2 = Font.fromJson(jsonarray);
            fonts = fonts2;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //The worst algorithm you have ever seen :)
        quickSimpleStupidAlgorithmToSortFontsByFamilies();



        ArrayAdapter<FontFamily> adapter = new ArrayAdapter<FontFamily>(MainActivity.this, android.R.layout.simple_spinner_item,families.getFontFamily());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final EditText editText = findViewById(R.id.editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mTextView.setText(editText.getText());
            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                getSelectedFont();
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FontFamily a = (FontFamily) spinner.getSelectedItem();
                jsonObjectRequest(a);
            }
        });


    }

    private void getSelectedFont() {

        mChBold = findViewById(R.id.checkBox);
        mChItalic = findViewById(R.id.checkBox2);

        mChBold.setChecked(false);
        mChItalic.setChecked(false);
        mChItalic.setEnabled(true);
        mChBold.setEnabled(true);


        FontFamily selectedFont = (FontFamily) spinner.getSelectedItem();

        if(selectedFont.getRegular() != null){
            download(RequestCode.base_url + selectedFont.getRegular().getUrl());
            loadfont(RequestCode.base_url + selectedFont.getRegular().getUrl());
        }

        else if(selectedFont.getBold() != null){
            download(RequestCode.base_url + selectedFont.getBold().getUrl());
            loadfont(RequestCode.base_url + selectedFont.getBold().getUrl());
        }

        else if(selectedFont.getItalic() != null){
            download(RequestCode.base_url + selectedFont.getItalic().getUrl());
            loadfont(RequestCode.base_url + selectedFont.getItalic().getUrl());
        }


        if(selectedFont.getItalic() == null ){
            mChItalic.setEnabled(false);
        }


        if(selectedFont.getBold() == null){
            mChBold.setEnabled(false);
        }


        if (selectedFont.getRegular() == null && selectedFont.getItalic() != null){
            mChItalic.setChecked(true);
            mChItalic.setEnabled(false);
        }


        if (selectedFont.getRegular() == null && selectedFont.getBold() != null){
            mChBold.setChecked(true);
            mChBold.setEnabled(false);
        }

        if (selectedFont.getBoldAndItalic() == null && selectedFont.getItalic() != null && selectedFont.getBold() != null){
            mChBold.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mChItalic.setEnabled(!isChecked);
                }
            });

            mChItalic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mChBold.setEnabled(!isChecked);
                }
            });

        }

    }

    private void jsonObjectRequest(final FontFamily selectedFont) {

         String postUrl;

        if(mChItalic.isChecked() && mChBold.isChecked())
            postUrl = selectedFont.getBoldAndItalic().getUrl();
        else if (mChItalic.isChecked())
            postUrl = selectedFont.getItalic().getUrl();
        else if (mChBold.isChecked())
            postUrl = selectedFont.getBold().getUrl();
        else
            postUrl = selectedFont.getRegular().getUrl();



        Map<String, String> params = new HashMap();
        params.put("appid", RequestCode.APPID);
        params.put("textTyped",mTextView.getText().toString());
        params.put("url", postUrl);
        params.put("fontFamilyName", selectedFont.getName());
        params.put("italic", String.valueOf(mChItalic.isChecked()));
        params.put("bold", String.valueOf(mChItalic.isChecked()));

        JSONObject parameters = new JSONObject(params);

        final String stage = postUrl;
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, RequestCode.postRequestUrl, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //TODO: handle success

                tvPostResponse.setText("Response: " + response.toString() +"\n" +selectedFont.getName() + stage);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //TODO: handle failure
                tvPostResponse.setText("That didn't work!");
            }
        });

        Volley.newRequestQueue(this).add(jsonRequest);
    }

    private void displayToast(String text){

        Toast.makeText(MainActivity.this,text,Toast.LENGTH_LONG).show();

    }


    private void quickSimpleStupidAlgorithmToSortFontsByFamilies() {

        HashMap<String, ArrayList<Font>> a = sortProductsByCategory(fonts);

        ArrayList<FontFamily> mFontFamilies = families.getFontFamily();

        families.test();
        for (int i=0; i < families.getCount();i++) {
            Log.d("YEAHBaby", mFontFamilies.get(i).getName());
        }

    }

    private HashMap<String, ArrayList<Font>> sortProductsByCategory (ArrayList<Font> arrayList) {

        HashMap<String, ArrayList<Font>> map = new HashMap<String, ArrayList<Font>>();

        for(Font fonts: arrayList) {

            // If the key does not exist in the hashmap
            if(!map.containsKey(fonts.getFamily())) {
                ArrayList<Font> listInHash = new ArrayList<Font>();
                listInHash.add(fonts);
                map.put(fonts.getFamily(), listInHash);
                //if FontFamily object doesn't exist
                families.addFamily(new FontFamily(fonts.getFamily()));
                families.addFont(fonts.getFamily(),fonts);

            } else {
                // add the product to the arraylist that corresponds to the key
                ArrayList<Font> listInHash = map.get(fonts.getFamily());
                listInHash.add(fonts);
                // add font to the corresponding font-family
                families.addFont(fonts.getFamily(),fonts);
            }
        }

        return map;
    }

    // Download the font from the URL
    private void download (String url) {
        new DownloadFileFromURL().execute(url);
    }

    // Load font if available on SD
    private void loadfont (String url) {

        fontName = url.substring(url.lastIndexOf("/")+1, url.length());

        File file = new File("/mnt/sdcard/"+fontName);
        if(file.exists()){
            // Display font from SD
            Typeface typeFace = Typeface.createFromFile(
                    new File(Environment.getExternalStorageDirectory(), "/"+fontName));
            mTextView.setTypeface(typeFace);

        }
        else {

            download(url);
        }
    }

    public String getNameOfTheFontFromUrl(String url){

        if(url.equals(null)){
            return RequestCode.base_url;
        }
        return url.substring(url.lastIndexOf("/")+1, url.length());

    }

    private void permissionRequest() {

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //TODO show explanation
            } else {

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        RequestCode.MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE);

            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestCode.MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                } else {
                    // permission denied, boo!
                }
                return;
            }
        }
    }

    // File download process from URL
    private class DownloadFileFromURL extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                int lenghtOfFile = conection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                OutputStream output = new FileOutputStream("/sdcard/"+getNameOfTheFontFromUrl(url.toString()));
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress(""+(int)((total*100)/lenghtOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }
        @Override
        protected void onPostExecute(String file_url) {
            // Display the custom font after the File was downloaded !

               loadfont(fontName);
        }
    }

}
