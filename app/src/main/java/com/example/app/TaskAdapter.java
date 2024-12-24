package com.example.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class TaskAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<Task> tasks;
    private final Runnable updateTaskSummary;

    public TaskAdapter(Context context, ArrayList<Task> tasks, Runnable updateTaskSummary) {
        this.context = context;
        this.tasks = tasks;
        this.updateTaskSummary = updateTaskSummary;
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Object getItem(int position) {
        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Crée une nouvelle vue si nécessaire ou réutilise une vue existante
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.task_item, parent, false);
        }

        // Récupère la tâche à la position spécifiée
        Task task = tasks.get(position);

        // Récupère les éléments de la vue
        TextView taskName = convertView.findViewById(R.id.taskName);
        CheckBox checkBox = convertView.findViewById(R.id.checkBoxTask);
        ImageButton deleteButton = convertView.findViewById(R.id.deleteButton);

        // Remplir la vue avec les données de la tâche
        taskName.setText(task.getName());
        checkBox.setChecked(task.isCompleted());

        // Gère la case à cocher
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setCompleted(isChecked);
            updateTaskSummary.run(); // Mise à jour du résumé des tâches
        });

        // Gère le bouton de suppression
        deleteButton.setOnClickListener(v -> {
            tasks.remove(position); // Supprimer la tâche
            notifyDataSetChanged(); // Actualiser la liste
            updateTaskSummary.run(); // Met à jour le résumé des tâches
        });

        return convertView;
    }
}
