package com.example.lab8_doanttlt;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Spinner spin;
    ListView listview;
    Button button;
    private HashMap<String, Student> hashmap = new HashMap<String, Student>();
    private ArrayList<String> list = new ArrayList<String>();
    private ArrayList<String> major = new ArrayList<String>();
    private static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spin = (Spinner) findViewById(R.id.spinnerMajor);
        button = (Button) findViewById(R.id.button);
        listview = (ListView) findViewById(R.id.listView);
//        AddStudentFromFileCSV();
        AddStudentFromFirebase();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, major);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spin.setAdapter(adapter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Print(spin.getSelectedItem().toString());
            }
        });
    }

    public void AddStudentFromFileCSV() {
        try {
            String splitBy = ",";
            FileInputStream in = this.openFileInput("Data.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            major.add("All");

            while (br != null) {
                String line = br.readLine();
                String[] value = line.split(splitBy);
                hashmap.put(value[0], new Student(value[0], value[1], value[2]));
                if (!major.contains(value[2])) {
                    major.add(value[2]);
                }
            }
            br.close();
        } catch (Exception e) {
            System.out.println("" + e.getMessage());
        }
    }

    public void AddStudentFromFirebase() {
        try {
            FirebaseFirestore.setLoggingEnabled(true);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference doc = db.collection("DoAnTTLT");

            major.add("All");
            doc.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> st = document.getData();
                            hashmap.put(st.get("ID").toString(), new Student(st.get("ID").toString(), st.get("Name").toString(), st.get("Major").toString()));
                            if (!major.contains(st.get("Major").toString())) {
                                major.add(st.get("Major").toString());
                            }
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });

        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void Print(String s) {
        list.clear();
        if (s.equals("All")) {
            for (Student x : hashmap.values()) {
                list.add(x.toString());
            }
        } else {
            for (Student x : hashmap.values()) {
                if (s.equals(x.getMajor())) {
                    list.add(x.toString());
                }
            }
        }

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter1);
    }
}