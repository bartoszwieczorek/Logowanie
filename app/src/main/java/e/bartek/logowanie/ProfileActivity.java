package e.bartek.logowanie;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import java.util.Date;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {


    //firebase auth object
    private FirebaseAuth firebaseAuth;

    //view objects
    private TextView textViewUserEmail;
    private Button buttonLogout, buttonSave, buttonCompute;

    private DatabaseReference databaseReference;
    private EditText editTextName, editTextAddress;
    DatabaseReference databaseUsers;
    ListView listViewUsers;

    List<UserInformation> userInformationList;

    public Float average;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();

        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        listViewUsers = (ListView) findViewById(R.id.listViewUsers);

        userInformationList = new ArrayList<>();

        //if the user is not logged in
        //that means current user will return null
        if(firebaseAuth.getCurrentUser() == null){
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, LoginActivity.class));
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        buttonCompute = (Button) findViewById(R.id.buttonCompute);



        //getting current user
        FirebaseUser user = firebaseAuth.getCurrentUser();

        //initializing views
        textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);

        //displaying logged in user name
        textViewUserEmail.setText("Welcome "+user.getEmail());

        //adding listener to button
        buttonLogout.setOnClickListener(this);
        buttonSave.setOnClickListener(this);
        buttonCompute.setOnClickListener(this);

        listViewUsers.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                UserInformation userInformation =  userInformationList.get(position);
                showUpdateDialog(userInformation.getId(), userInformation.getName());
                return false;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userInformationList.clear();
                Float mean;
                Float p;
                p=Float.valueOf(0);
                Integer a=0;

                for(DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                    UserInformation userInformation = userSnapshot.getValue(UserInformation.class);

                    userInformationList.add(userInformation);
                    String mean1= userSnapshot.getValue(UserInformation.class).getAddress();
                        mean = Float.valueOf(mean1);
                        p += mean;
                        a++;
                        average = p / a;
                }
                UserInformationList adapter = new UserInformationList(ProfileActivity.this, userInformationList);
                listViewUsers.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void showUpdateDialog(final String userInformationId, final String userInformationName){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog,null);
        dialogBuilder.setView(dialogView);
        final EditText editTextUpdate1 = (EditText) dialogView.findViewById(R.id.editTextUpdate1);
        final EditText editTextUpdate2 = (EditText) dialogView.findViewById(R.id.editTextUpdate2);
        //final TextView textViewUpdate = (TextView) dialogView.findViewById(R.id.textViewUpdate);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdate);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDelete);

        dialogBuilder.setTitle("Updating Information " + userInformationName);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextUpdate1.getText().toString().trim();
                String cost = editTextUpdate2.getText().toString().trim();
                String email = firebaseAuth.getCurrentUser().getEmail();

                if(TextUtils.isEmpty(name)){
                    editTextUpdate1.setError("Description required");
                    return;
                }
                updateUserInformation(name, cost, userInformationId, email);
                alertDialog.dismiss();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser(userInformationId);
            }
        });



    }

    private void deleteUser(String userInformationId) {
        DatabaseReference drUsers = FirebaseDatabase.getInstance().getReference("users").child(userInformationId);
        drUsers.removeValue();
        Toast.makeText(this, "Information is deleted", Toast.LENGTH_LONG).show();
    }

    private boolean updateUserInformation(String name, String add, String id, String email){

        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("users").child(id);
        UserInformation userInformation = new UserInformation(name, add, id, email);
        databaseReference2.setValue(userInformation);
        Toast.makeText(this, "Information updated successfully", Toast.LENGTH_LONG).show();
        return true;
    }

    private void saveUserInformation(){

        String name = editTextName.getText().toString().trim();
        String add = editTextAddress.getText().toString().trim();

        if(!TextUtils.isEmpty(name)) {
           String id = databaseUsers.push().getKey();
            String email = firebaseAuth.getCurrentUser().getEmail();


            UserInformation userInformation = new UserInformation(name, add, id, email);
            databaseUsers.child(id).setValue(userInformation);



            //FirebaseUser user = firebaseAuth.getCurrentUser();
            //databaseReference.child(user.getUid()).setValue(userInformation);

            Toast.makeText(this, "Information saved", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "You should enter a description", Toast.LENGTH_LONG).show();
        }

    }



    @Override
    public void onClick(View view) {
        //if logout is pressed
        if(view == buttonLogout){
            //logging out the user
            firebaseAuth.signOut();
            //closing activity
            finish();
            //starting login activity
            startActivity(new Intent(this, LoginActivity.class));
        }

        if(view == buttonSave){
            saveUserInformation();
        }

        if(view == buttonCompute){
            finish();
            //startActivity(new Intent(this, ComputeActivity.class));
            Intent intent = new Intent(this, ComputeActivity.class);
            intent.putExtra("average_ID",average);
            startActivity(intent);
        }
    }
}
