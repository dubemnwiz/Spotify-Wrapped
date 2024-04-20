package com.example.spotifysdk;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import android.content.DialogInterface;
import android.widget.ToggleButton;

import com.example.spotifysdk.databinding.ActivityMainBinding;
import com.example.spotifysdk.ui.settings.SettingsFragment;
import com.google.android.material.navigation.NavigationView;

public class Editaccount extends AppCompatActivity {
    EditText username, password, repassword;
    Button insert, update, delete, view;
    ToggleButton togglePasswordVisibility, toggleRepasswordVisibility;
    DBHelper DB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_account); // Change this line to use fragment_account.xml

        Button back = findViewById(R.id.backBtn);
        back.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(Editaccount.this, MainActivity2.class);
                                        startActivity(intent);
                                        //Navigation.findNavController(view).navigate(R.id.nav_slideshow);
                                    }
                                }
        );




        username = findViewById(R.id.Editusername);
        password = findViewById(R.id.Editpassword);
        repassword = findViewById(R.id.Reeditpassword);
        insert = findViewById(R.id.btnInsert);
        update = findViewById(R.id.btnUpdate);
        delete = findViewById(R.id.btnDelete);
        view = findViewById(R.id.btnView);

        togglePasswordVisibility = findViewById(R.id.togglePasswordVisibility);
        toggleRepasswordVisibility = findViewById(R.id.toggleRepasswordVisibility);

        DB = new DBHelper(this);
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernameTXT = username.getText().toString();
                String passwordTXT = password.getText().toString();
                String repasswordTXT = repassword.getText().toString();


                Boolean checkinsertdata = DB.insertData(usernameTXT, passwordTXT);
                if(checkinsertdata==true)
                    Toast.makeText(Editaccount.this, "New Entry Inserted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Editaccount.this, "New Entry Not Inserted", Toast.LENGTH_SHORT).show();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernameTXT = username.getText().toString();
                String passwordTXT = password.getText().toString();

                Boolean checkupdatedata = DB.updateuserdata(usernameTXT, passwordTXT);
                if(checkupdatedata==true)
                    Toast.makeText(Editaccount.this, "Entry Updated", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Editaccount.this, "New Entry Not Updated", Toast.LENGTH_SHORT).show();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Editaccount.this);
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure you want to delete your account?");
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String nameTXT = username.getText().toString();
                        Boolean checkDeletedData = DB.deletedata(nameTXT);
                        if (checkDeletedData) {
                            Toast.makeText(Editaccount.this, "Account Deleted", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Editaccount.this, LoginActivity.class); // Adjust LoginActivity to your actual login activity
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Editaccount.this, "Failed to delete account", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Do nothing or add logic to handle going back to the previous screen
                    }
                });
                builder.show();
            }
        });

        // Set click listener for "View Data" button
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor res = DB.getdata();
                Toast.makeText(Editaccount.this, "No Entry Exists", Toast.LENGTH_SHORT).show();
                Log.d("ViewData", "Data retrieved");
                if (res.getCount() == 0) {
                    Toast.makeText(Editaccount.this, "No Entry Exists", Toast.LENGTH_SHORT).show();
                } else {
                    StringBuffer buffer = new StringBuffer();
                    while (res.moveToNext()) {
                        buffer.append("Username: ").append(res.getString(0)).append("\n");
                        buffer.append("Password: ").append(res.getString(1)).append("\n\n");
                    }
                    Log.d("ViewData", "Data retrieved from database: \n" + buffer.toString());

                    // Display data (e.g., in AlertDialog) or perform any other action as needed
                    // For example, you can display the data in an AlertDialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(Editaccount.this);
                    builder.setCancelable(true);
                    builder.setTitle("User Entries");
                    builder.setMessage(buffer.toString());
                    builder.show();
                }
            }
        });

        // Set click listener for togglePasswordVisibility
        togglePasswordVisibility.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Toggle password visibility
                if (isChecked) {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                // Move cursor to the end of the text
                password.setSelection(password.getText().length());
            }
        });


        // Set click listener for toggleRepasswordVisibility
        toggleRepasswordVisibility.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Toggle repassword visibility
                if (isChecked) {
                    repassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    repassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                // Move cursor to the end of the text
                repassword.setSelection(repassword.getText().length());
            }
        });



    }
}