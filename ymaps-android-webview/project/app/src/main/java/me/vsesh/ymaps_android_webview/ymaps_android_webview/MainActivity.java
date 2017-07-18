package me.vsesh.ymaps_android_webview.ymaps_android_webview;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerFrom;
    private Spinner spinnerTo;
    private WebView myWebView;
    private TextView tvRouteInformation;

    private String getAddress(String place) {
        if (place == null)
            return "";
        switch (place) {
            case "Радиорынок":
                return "Кручинина 69";
            case "ОДНТ":
                return "Ростов-на-Дону, Карла Маркса 5";
            case "Аэропорт":
                return " Шолохова, 270/1";
            case "Новочеркасск":
                return "Новочеркасск";
            case "Склад":
                return "Таганрогская 120";
            default:
                return "Мечникова 39";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final String options1[] = {"Радиорынок", "ОДНТ", "Аэропорт", "Новочеркасск", "Склад"};
        final String options2[] = {"Радиорынок", "ОДНТ", "Аэропорт", "Новочеркасск", "Склад"};
        ArrayAdapter<String> adapter_from = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.spinner_item,
                options1);
        adapter_from.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adapter_to = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.spinner_item,
                options2);
        adapter_to.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerFrom = (Spinner) findViewById(R.id.spinner_from);
        spinnerFrom.setAdapter(adapter_from);
        spinnerFrom.setSelection(0);
        spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                from = options1[i];
                updateMap();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerTo = (Spinner) findViewById(R.id.spinner_to);
        spinnerTo.setAdapter(adapter_to);
        spinnerTo.setSelection(0);
        spinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                to = options2[i];
                updateMap();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        tvRouteInformation = (TextView)findViewById(R.id.tv_route_information);

        myWebView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.addJavascriptInterface(new WebViewJavaScriptInterface(this), "app");

        try {
            InputStream is = getAssets().open("index1.html");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();

            String htmlText = new String(buffer);
            htmlText = htmlText.replace("{{from}}", "'" + getAddress("Аэропорт") + "'");
            htmlText = htmlText.replace("{{to}}", "'" + getAddress("Новочеркасск") + "'");
            myWebView.loadDataWithBaseURL(
                    "http://ru.yandex.api.yandexmapswebviewexample.ymapapp",
                    htmlText,
                    "text/html",
                    "UTF-8",
                    null
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        // webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String from = null;
    private String to = null;

    private void updateMap() {
        if (from == null || to == null)
            return;

        final String param = "javascript:buildRoute('" + getAddress(from) + "', '" + getAddress(to) + "')";
        myWebView.loadUrl(param);
        myWebView.setWebViewClient(new WebViewClient(){
            public void onPageFinished(WebView view, String url){
                view.loadUrl(param);
            }
        });

    }


    public class WebViewJavaScriptInterface{

        private Context context;

        /*
         * Need a reference to the context in order to sent a post message
         */
        public WebViewJavaScriptInterface(Context context){
            this.context = context;
        }

        /*
         * This method can be called from Android. @JavascriptInterface
         * required after SDK version 17.
         */
        @JavascriptInterface
        public void makeToast(String message, boolean lengthLong){
            Toast.makeText(context, message, (lengthLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT)).show();
        }

        @JavascriptInterface
        public void showDetails(int length, int duration) {
            String formattedLength = "Length (per kilometers): " + String.valueOf(length / 1000);
            String formattedDuration = "Duration (per minutes): " + String.valueOf(duration / 60);
            tvRouteInformation.setText(formattedLength + "\n" + formattedDuration);
//            Toast.makeText(context, "Length" + length + " duration " + duration, Toast.LENGTH_LONG).show();
        }
    }

}