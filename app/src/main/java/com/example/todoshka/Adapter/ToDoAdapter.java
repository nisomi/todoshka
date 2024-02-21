package com.example.todoshka.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.activity.result.IntentSenderRequest;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoshka.MainActivity;
import com.example.todoshka.Model.ToDoModel;
import com.example.todoshka.R;
import com.example.todoshka.Utils.AddNewTask;
import com.example.todoshka.Utils.DataBaseHandler;

import java.util.Arrays;
import java.util.List;
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

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {

    private List<ToDoModel> todoList;
    private MainActivity activity;
    private DataBaseHandler db;

    public ToDoAdapter(DataBaseHandler db, MainActivity activity){
        this.db = db;
        this.activity = activity;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent,false);

        return new ViewHolder(itemView);
    }

    public Context getContext() {
        return activity;
    }

    public void onBindViewHolder (ViewHolder holder, int position) {
        db.openDatabase();
        ToDoModel item = todoList.get(position);
        holder.task.setChecked(toBoolean(item.getStatus()));
        if (toBoolean(item.getStatus())){
            int myColor = ContextCompat.getColor(getContext(), R.color.DimGray);
            holder.task.setText(item.getTask());
            holder.task.setTextColor(myColor);
        }
        else{
            int myColor = ContextCompat.getColor(getContext(), R.color.text);
            holder.task.setText(item.getTask());
            holder.task.setTextColor(myColor);
        }
        holder.task.setText(item.getTask());
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    int myColor = ContextCompat.getColor(getContext(), R.color.DimGray);
                    db.updateStatus(item.getId(),1);
                    compoundButton.setTextColor(myColor);
                    KonfettiView konfettiView = activity.findViewById(R.id.konfettiView);
                    EmitterConfig emitterConfig = new Emitter(5L, TimeUnit.SECONDS).perSecond(50);
                    Party party = new PartyFactory(emitterConfig)
                            .angle(270)
                            .spread(90)
                            .setSpeedBetween(1f, 5f)
                            .timeToLive(1000L)
                            .shapes(new nl.dionsegijn.konfetti.core.models.Shape.Rectangle(0.2f))
                            .sizes(new Size(12,5f,0.2f))
                            .position(0.0, 0.0, 1.0, 0.0)
                            .build();
                    explode(konfettiView);


                }
                else{
                    int myColor = ContextCompat.getColor(getContext(), R.color.text);
                    db.updateStatus(item.getId(),0);
                    compoundButton.setTextColor(myColor);
                }
            }
        });
    }

    public void deleteItem(int position) {
        ToDoModel item = todoList.get(position);
        db.deleteTask(item.getId());
        todoList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position){
        ToDoModel item = todoList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
    }
    public int getItemCount() {
        return todoList.size();
    }

    private boolean toBoolean(int n) {
        return n!=0;
    }

    public void setTasks(List<ToDoModel> todoList){
        this.todoList = todoList;
        notifyDataSetChanged();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox task;
        ViewHolder (View view) {
            super(view);
            task=view.findViewById(R.id.todoCheckBox);
        }
    }
    public void explode(KonfettiView konfettiView) {
        EmitterConfig emitterConfig = new Emitter(100L, TimeUnit.MILLISECONDS).max(100);
        Shape.DrawableShape drawableShape = null;
        Drawable drawable = ContextCompat.getDrawable(activity, R.drawable.ic_four_points_star);
        drawableShape = new Shape.DrawableShape(drawable, true);
        konfettiView.start(
//                new PartyFactory(emitterConfig)
//                        .angle(Angle.RIGHT)
//                        .sizes(Size.Companion.getSMALL())
//                        .spread(5)
//                        .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE, drawableShape))
//                        .colors(Arrays.asList(Color.RED, Color.YELLOW, 0xfaa600))
//                        .setSpeedBetween(0f, 10f)
//                        .position(0.0,0.0,0.05,1.0)
//                        .build(),
//                new PartyFactory(emitterConfig)
//                        .angle(Angle.LEFT)
//                        .spread(5)
//                        .sizes(Size.Companion.getSMALL())
//                        .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE))
//                        .colors(Arrays.asList(Color.RED, Color.YELLOW, 0xfaa600))
//                        .setSpeedBetween(0f, 10f)
//                        .position(0.95,0.0,1.0,1.0)
//                        .build(),
                new PartyFactory(emitterConfig)
                        .angle(Angle.RIGHT)
                        .sizes(Size.Companion.getLARGE())
                        .spread(Spread.WIDE)
                        .shapes(Arrays.asList(drawableShape))
                        .colors(Arrays.asList(Color.YELLOW, 0xfaa600, 0xff9300))
                        .setSpeedBetween(0f, 10f)
                        .position(0.0,0.0,0.5,1.0)
                        .build(),
        new PartyFactory(emitterConfig)
                .angle(Angle.LEFT)
                .spread(Spread.WIDE)
                .sizes(Size.Companion.getLARGE())
                .shapes(Arrays.asList(drawableShape))
                .colors(Arrays.asList(Color.YELLOW, 0xfaa600, 0xff9300))
                .setSpeedBetween(0f, 10f)
                .position(0.5,0.0,1.0,1.0)
                .build()
//
        );
    }
}
