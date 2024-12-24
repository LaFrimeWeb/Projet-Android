package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    // Déclaration des variables
    TextView welcomeText, taskSummary;
    ImageButton addTaskButton, fabMain, fabSortAlphabetically, fabLogout;
    ListView taskListView;

    ArrayList<Task> taskList;
    TaskAdapter taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialisation des composants de l'interface utilisateur
        welcomeText = findViewById(R.id.welcomeText);
        taskListView = findViewById(R.id.taskListView);
        taskSummary = findViewById(R.id.taskSummary);
        fabMain = findViewById(R.id.fabMain);
        fabSortAlphabetically = findViewById(R.id.fabSortAlphabetically);
        fabLogout = findViewById(R.id.fabLogout);
        addTaskButton = findViewById(R.id.addTaskButton); // Lien avec le bouton "Add Task"

        // Récupération du nom d'utilisateur passé via l'intent
        String username = getIntent().getStringExtra("USERNAME");
        if (username != null && !username.isEmpty()) {
            welcomeText.setText("Bienvenue, " + username + "!");
        } else {
            welcomeText.setText("Bienvenue !");
        }

        // Initialisation de la liste des tâches et de l'adaptateur
        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(this, taskList, this::updateTaskSummary);
        taskListView.setAdapter(taskAdapter);

        // Gestion des boutons flottants
        fabMain.setOnClickListener(view -> {
            if (fabSortAlphabetically.getVisibility() == View.GONE) {
                fabSortAlphabetically.setVisibility(View.VISIBLE);
                fabLogout.setVisibility(View.VISIBLE);  // Affiche le bouton de déconnexion
            } else {
                fabSortAlphabetically.setVisibility(View.GONE);
                fabLogout.setVisibility(View.GONE);  // Masque le bouton de déconnexion
            }
        });

        // Trier par ordre alphabétique
        fabSortAlphabetically.setOnClickListener(view -> {
            taskList.sort((task1, task2) -> task1.getName().compareToIgnoreCase(task2.getName()));
            taskAdapter.notifyDataSetChanged();
            Toast.makeText(HomeActivity.this, "Trié par ordre alphabétique", Toast.LENGTH_SHORT).show();
        });


        // Déconnexion via le bouton flottant
        fabLogout.setOnClickListener(view -> {
            Toast.makeText(HomeActivity.this, "Déconnexion réussie", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // Mise à jour initiale du résumé des tâches
        updateTaskSummary();

        // Action pour ajouter une tâche
        addTaskButton.setOnClickListener(view -> {
            String taskName = ((EditText) findViewById(R.id.taskInput)).getText().toString(); // Récupère le nom de la tâche
            if (!taskName.isEmpty()) {
                Task newTask = new Task(taskName, false); // Crée une nouvelle tâche avec le nom et un état "non complété"
                taskList.add(newTask); // Ajoute la tâche à la liste
                taskAdapter.notifyDataSetChanged(); // Notifie l'adaptateur que la liste a changé
                ((EditText) findViewById(R.id.taskInput)).setText(""); // Vide le champ de texte
                updateTaskSummary(); // Mets à jour le résumé des tâches
            } else {
                Toast.makeText(HomeActivity.this, "Veuillez entrer un nom pour la tâche", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Met à jour le résumé des tâches affichant les tâches terminées par rapport au total.
     */
    private void updateTaskSummary() {
        int completedTasks = 0;
        for (Task task : taskList) {
            if (task.isCompleted()) {
                completedTasks++;
            }
        }
        taskSummary.setText("Tâches terminées : " + completedTasks + "/" + taskList.size());
    }
}
