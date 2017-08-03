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
        final EditText et_members = new EditText(MainActivity.this);
        et_members.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        alertDialog.setView(et_members);
        alertDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String new_member = et_members.getText().toString();
                Member member= new Member(new_member, "10");
                try {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference();
                    DatabaseReference childRef = myRef.child("members").push();
                    childRef.setValue(member);
                }
                catch(Exception e){
                    Toast.makeText(getApplicationContext(), e.toString() , Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getApplicationContext(), member.battery , Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.setNegativeButton("Cancel", null);
        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }
    public static class Member{
        String name;
        String battery;
        public Member(){

        }
        public Member(String name, String battery){
            this.name=name;
            this.battery=battery;
        }
    }
}
