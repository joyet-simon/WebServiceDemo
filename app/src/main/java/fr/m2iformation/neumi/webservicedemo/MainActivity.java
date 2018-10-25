package fr.m2iformation.neumi.webservicedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText etCodePays;
    TextView tvPays;
    ListView lvPays;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> arrayList;
    TextView tvListPays;
    Spinner sCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etCodePays = findViewById(R.id.etCodePays);
        tvPays = findViewById(R.id.tvPays);
        arrayList = new ArrayList<String>();
        lvPays = findViewById(R.id.lvPays);
        tvListPays = findViewById(R.id.tvListPays);
        sCode = findViewById(R.id.sCode);
        createSpinner();


    }

    public void searchOne(View view) {
        String code = etCodePays.getText().toString();
        String url = "http://demo@services.groupkt.com/country/get/iso2code/" + code;
        try {
            HttpClient client = new HttpClient(url);
            client.start();
            client.join();
            String rep = client.getResponse();
            JSONObject json = new JSONObject(rep);
            JSONObject restReponse = json.getJSONObject("RestResponse");
            JSONObject result = restReponse.getJSONObject("result");
            String name = result.getString("name");
            tvPays.setText(name);
        } catch (InterruptedException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void searchAll(View view) {
        String url = "http://demo@services.groupkt.com/country/get/all";
        try {
            HttpClient client = new HttpClient(url);
            client.start();
            client.join();
            String rep = client.getResponse();
            JSONObject json = new JSONObject(rep);
            JSONObject restReponse = json.getJSONObject("RestResponse");
            JSONArray result = restReponse.getJSONArray("result");
            for (int i = 0; i < result.length(); i++) {
                JSONObject pays = result.getJSONObject(i);
                String name = pays.getString("name");
                arrayList.add(name);
            }
            arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
            lvPays.setAdapter(arrayAdapter);
            tvListPays.setVisibility(View.VISIBLE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void createSpinner() {
        List<String> listCode = new ArrayList<>();
        String url = "http://demo@services.groupkt.com/country/get/all";
        try {
            HttpClient client = new HttpClient(url);
            client.start();
            client.join();
            String rep = client.getResponse();
            JSONObject json = new JSONObject(rep);
            JSONObject restReponse = json.getJSONObject("RestResponse");
            JSONArray result = restReponse.getJSONArray("result");
            listCode.add("");
            for (int i = 0; i < result.length(); i++) {
                JSONObject pays = result.getJSONObject(i);
                String code = pays.getString("alpha2_code");
                listCode.add(code);
            }
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listCode);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sCode.setAdapter(adapter);
            sCode.setOnItemSelectedListener(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        etCodePays.setText(item);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
