package e.bartek.logowanie;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComputeActivity extends AppCompatActivity implements View.OnClickListener{

    private Button buttonGoBack;
    ListView listViewCompute;
    List<Account> accountList;
    DatabaseReference databaseUsers;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compute);
        buttonGoBack = (Button) findViewById(R.id.buttonGoBack);
        buttonGoBack.setOnClickListener(this);
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        accountList = new ArrayList<>();
        listViewCompute = (ListView) findViewById(R.id.listViewCompute);
        firebaseAuth = FirebaseAuth.getInstance();
        //average = getIntent().getExtras.getInt("dupa");

    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseUsers.addValueEventListener(new ValueEventListener() {
            //@Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                float average;
                average = getIntent().getExtras().getFloat("average_ID");
               // average =5;
                Map<String,Float> map = new HashMap<String, Float>();
                Float overall=Float.valueOf(0);

                for(DataSnapshot accountSnapshot : dataSnapshot.getChildren()){

                    String xd = firebaseAuth.getCurrentUser().getEmail();
                    //String xd = accountSnapshot.getChildren().getClass().getName();
                    //Integer integer = firebaseAuth.

                    String description= accountSnapshot.getValue(UserInformation.class).getEmail();
                    String cost = accountSnapshot.getValue(UserInformation.class).getAddress();
                    Float cost1 = Float.valueOf(cost);
                    Float cost2 = cost1 - average;
                    Float useful = map.containsKey(description) ? map.get(description) : 0;
                    useful += cost1;
                    map.put(description,useful);

                    overall += cost1;
                   // Account account = new Account(description, cost2);
                    //accountList.add(account);
                }
                Float avg = Float.valueOf(map.size());


                for(Map.Entry me : map.entrySet()){
                    Account account = new Account(me.getKey().toString(), Float.valueOf(me.getValue().toString())-overall/avg);
                    accountList.add(account);

                }
                //listViewCompute.setBackgroundColor(Color.GREEN);

                AccountList adapter = new AccountList(ComputeActivity.this, accountList);
                listViewCompute.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v == buttonGoBack){
            finish();
            startActivity(new Intent(this, ProfileActivity.class));


        }
    }
}
