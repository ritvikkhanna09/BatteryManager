package ritvikkhanna.batterymanager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab_add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fab_add = (FloatingActionButton)findViewById(R.id.fab_add);
        fab_add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addMembers();
            }
        });
    }

    void addMembers(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this,R.style.MyDialogTheme);
        alertDialog.setTitle("Add Member");
        alertDialog.setMessage("Enter Name");
        final EditText edittext_members = new EditText(MainActivity.this);
        edittext_members.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        alertDialog.setView(edittext_members);
        alertDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String new_member = edittext_members.getText().toString();
                final Person person = new Person(new_member,"0");
                //Adding values
                Toast.makeText(getApplicationContext(), person.name , Toast.LENGTH_SHORT).show();
                final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                rootRef.child("members").setValue(person);
//                addFirebase(new_member);
            }
        });
        alertDialog.setNegativeButton("Cancel", null);
        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }

    void addFirebase(final String name){
        final Person person = new Person(name,"0");
        //Adding values
        Toast.makeText(getApplicationContext(), person.name , Toast.LENGTH_SHORT).show();
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.child("members").setValue(person);
//        final DatabaseReference memberRef= rootRef.child("member");
//        memberRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Toast.makeText(getApplicationContext(), person.name , Toast.LENGTH_SHORT).show();
//                memberRef.setValue(person);
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }
    @IgnoreExtraProperties
    public class Person {

        public String name;
        public String battery;

        // Default constructor required for calls to
        // DataSnapshot.getValue(User.class)


        public Person(String name, String battery) {
            this.name = name;
            this.battery = battery;
        }
    }

}
