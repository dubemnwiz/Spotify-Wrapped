package com.example.spotifysdk;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

public class Editaccount extends AppCompatActivity {
    EditText username, password, repassword;
    Button insert, update, delete, view;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_account); // Change this line to use fragment_account.xml

        Button back = findViewById(R.id.backBtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.nav_slideshow);
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
                String nameTXT = username.getText().toString();
                Boolean checkudeletedata = DB.deletedata(nameTXT);
                if(checkudeletedata==true)
                    Toast.makeText(Editaccount.this, "Entry Deleted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Editaccount.this, "Entry Not Deleted", Toast.LENGTH_SHORT).show();
            }        });

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






    }

}
