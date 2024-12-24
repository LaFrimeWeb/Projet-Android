package com.example.app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class registerActivity extends AppCompatActivity {

    EditText nom, prenom, username, email, password, confirmPassword;
    Button register, back;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialisation des éléments de l'interface
        nom = findViewById(R.id.editTextNom);
        prenom = findViewById(R.id.editTextPrenom);
        username = findViewById(R.id.editTextUsername);
        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        confirmPassword = findViewById(R.id.editTextConfirmPassword);
        register = findViewById(R.id.buttonRegister);
        back = findViewById(R.id.buttonBack);

        // Initialisation de la base de données
        db = new DatabaseHelper(this);

        // Gestion du clic sur le bouton S'inscrire
        register.setOnClickListener(view -> {
            String nomText = nom.getText().toString();
            String prenomText = prenom.getText().toString();
            String usernameText = username.getText().toString();
            String emailText = email.getText().toString();
            String passwordText = password.getText().toString();
            String confirmPasswordText = confirmPassword.getText().toString();

            if (nomText.isEmpty() || prenomText.isEmpty() || usernameText.isEmpty() || emailText.isEmpty() || passwordText.isEmpty() || confirmPasswordText.isEmpty()) {
                Toast.makeText(registerActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            } else if (!passwordText.equals(confirmPasswordText)) {
                Toast.makeText(registerActivity.this, "Les mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show();
            } else if (db.checkUserExists(emailText, usernameText)) {
                Toast.makeText(registerActivity.this, "Email ou nom d'utilisateur déjà utilisé", Toast.LENGTH_SHORT).show();
            } else {
                boolean success = db.insertUser(nomText, prenomText, usernameText, emailText, passwordText);
                if (success) {
                    Toast.makeText(registerActivity.this, "Inscription réussie", Toast.LENGTH_SHORT).show();
                    finish(); // Retourne à l'activité précédente après une inscription réussie
                } else {
                    Toast.makeText(registerActivity.this, "Erreur lors de l'inscription", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Gestion du clic sur le bouton Retour
        back.setOnClickListener(view -> finish());
    }
}
