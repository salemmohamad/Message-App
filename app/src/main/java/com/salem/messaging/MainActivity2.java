package com.salem.messaging;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.salem.messaging.Adapter.GroupAdapter;
import com.salem.messaging.Module.GroupModul;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity2 extends AppCompatActivity {


    //By: Salem M.Salem

    public static final int RC_SIGN_IN=1;

    ArrayList<GroupModul> list;
    ListView listView;
    Toolbar toolbar;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ChildEventListener childEventListener;
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("Groups");
        setSupportActionBar(toolbar);

        firebaseDatabase= FirebaseDatabase.getInstance();
        auth= FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        databaseReference=firebaseDatabase.getReference().child(user.getDisplayName()).child("Groups");

        Intent intent = getIntent();
        databaseReference.push().setValue(new GroupModul(intent.getStringExtra("month"),intent.getStringExtra("day"),
                intent.getStringExtra("title")));

        listView=findViewById(R.id.listviewG);
        list= new ArrayList<>();


        final GroupAdapter groupAdapter= new GroupAdapter(this,list);
        listView.setAdapter(groupAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent= new Intent(MainActivity2.this,MainActivity3.class);
                intent.putExtra("number",String.valueOf(position));
                intent.putExtra("day",groupAdapter.getItem(position).getDay());
                intent.putExtra("month",groupAdapter.getItem(position).getMonth());
                intent.putExtra("title",groupAdapter.getItem(position).getTitle());
                startActivity(intent);
            }
        });

        childEventListener= new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                GroupModul modul= snapshot.getValue(GroupModul.class);
                groupAdapter.add(modul);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        databaseReference.addChildEventListener(childEventListener);

        authStateListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
                if(user!=null){

                }else{
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(
                            new AuthUI.IdpConfig.EmailBuilder().build(),
                            new AuthUI.IdpConfig.GoogleBuilder().build()))
                            .build(),
                            RC_SIGN_IN);
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            if(requestCode==RESULT_CANCELED){
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.about:
                Toast.makeText(this, "Created By Team 4", Toast.LENGTH_LONG).show();
                break;

            case R.id.close:
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                break;

            case R.id.add:
                Intent intent1= new Intent(MainActivity2.this,MainActivity4.class);
                startActivity(intent1);
                break;

            case R.id.signOut:
                AuthUI.getInstance().signOut(this);
                break;

            default:

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        auth.removeAuthStateListener(authStateListener);
    }

}