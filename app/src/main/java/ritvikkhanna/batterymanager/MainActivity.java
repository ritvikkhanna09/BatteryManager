package ritvikkhanna.batterymanager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab_add,fab_show;
    ListView lv;
    public ArrayList<String> names=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv=(ListView) findViewById(R.id.lv);
        //this.displayData();
        this.retreiveData();
        fab_show = (FloatingActionButton)findViewById(R.id.fab_showdd);
        fab_add = (FloatingActionButton)findViewById(R.id.fab_add);
        fab_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        fab_add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addMembers();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object o = lv.getItemAtPosition(i);
                String str=(String)o;
                String[] parts = str.split("-------------->");
                String name = parts[0];
                String battery = parts[1];
                //changeBattery(name,battery);
                showChangeLangDialog(name,battery);
                Toast.makeText(getApplicationContext(), name+","+battery , Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showChangeLangDialog(final String name, final String battery) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this,R.style.MyDialogTheme);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);
        final TextView edt = (TextView) dialogView.findViewById(R.id.text1);
        edt.setText(battery);
        final FloatingActionButton addt=(FloatingActionButton)dialogView.findViewById(R.id.fab_add);
        final FloatingActionButton subt=(FloatingActionButton)dialogView.findViewById(R.id.fab_sub);
        dialogBuilder.setTitle("Change Battery");
        dialogBuilder.setMessage(name);
        addt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a = edt.getText().toString();
                int bat_temp=Integer.parseInt(a);
                bat_temp++;
                edt.setText(Integer.toString(bat_temp));
            }
        });
        subt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a = edt.getText().toString();
                int bat_temp=Integer.parseInt(a);
                bat_temp--;
                edt.setText(Integer.toString(bat_temp));
            }
        });
        dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do something with edt.getText().toString();
                String a = edt.getText().toString();
                //updateBattery(name,a);
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }


    //updating battery
    void updateBattery(String name,String battery){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();
        final DatabaseReference childRef = myRef.child("members");

        childRef.orderByChild("name").equalTo(name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //floating action button onClick calls this function
    void addMembers(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();
        final DatabaseReference childRef = myRef.child("members").push();
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

                    childRef.setValue(member);
                }
                catch(Exception e){
                    Toast.makeText(getApplicationContext(), e.toString() , Toast.LENGTH_SHORT).show();
                }
            }
        });
        alertDialog.setNegativeButton("Cancel", null);
        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }

    void retreiveData(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                getUpdates(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                getUpdates(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void getUpdates(DataSnapshot ds){
        //names.clear();
        for (DataSnapshot data : ds.getChildren()) {
            Member m = new Member(data.getValue(Member.class).name, data.getValue(Member.class).battery);

            if(!names.contains(m.dis)){
            names.add(m.dis);}
        }
        ArrayAdapter adapter=new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1,names);
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    public static class Member{
        String name;
        String battery;
        String dis;
        public Member(){

        }
        public Member(String name, String battery){
            this.name=name;
            this.battery=battery;
            this.dis=name+"-------------->"+battery;
        }
    }
    //Extra
    private void displayData(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();
        try {
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    names.clear();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Member m = new Member(data.getValue(Member.class).name, data.getValue(Member.class).battery);
                        names.add(m.name);
                    }
                    ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, names);
                    lv.setAdapter(adapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }catch (Exception ae){
            Toast.makeText(getApplicationContext(), ae.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
