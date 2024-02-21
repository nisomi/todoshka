package com.example.todoshka;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todoshka.Adapter.ToDoAdapter;
import com.example.todoshka.Model.ToDoModel;
import com.example.todoshka.Utils.AddNewTask;
import com.example.todoshka.Utils.DataBaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import nl.dionsegijn.konfetti.core.Angle;
import nl.dionsegijn.konfetti.core.Party;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.Position;
import nl.dionsegijn.konfetti.core.Spread;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.core.models.Size;
import nl.dionsegijn.konfetti.xml.KonfettiView;

public class MainActivity extends AppCompatActivity implements DialogCloseListener{

    private TextView current_data;
    private EditText edit_text;
    private RecyclerView tasks;
    private ToDoAdapter tasksAdapter;
    private DataBaseHandler db;
    private List<ToDoModel> tasksList;
    private FloatingActionButton fab;

    private final static String FILE_NAME = "content.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        current_data = findViewById(R.id.current_data);
        edit_text = findViewById(R.id.editText);
        tasksList = new ArrayList<>();

        db = new DataBaseHandler(this);
        db.openDatabase();

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat date = new SimpleDateFormat("EEE, d MMM");

        current_data.setText(date.format(cal.getTime()).toString());

        tasks = findViewById(R.id.tasks);
        tasks.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new ToDoAdapter(db,this);
        tasks.setAdapter(tasksAdapter);

        fab = findViewById(R.id.fab);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecyclerItemTouchHelper(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasks);

        tasksList = db.getAllTasks();
        Collections.reverse(tasksList);

        tasksAdapter.setTasks(tasksList);

        edit_text = findViewById(R.id.editText);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        edit_text.setText(prefs.getString("autoSave", ""));

        edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                prefs.edit().putString("autoSave", s.toString()).commit();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences pref;
        edit_text = findViewById(R.id.editText);
        String text;
        pref = getSharedPreferences("info", MODE_PRIVATE);
        text =  pref.getString("text", "");
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("text", edit_text.getText().toString());
        editor.commit();
    }

    @Override
    public void handleDialogClose(DialogInterface dialog){
        tasksList = db.getAllTasks();
        Collections.reverse(tasksList);
        tasksAdapter.setTasks(tasksList);
        tasksAdapter.notifyDataSetChanged();
    }
}