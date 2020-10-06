package com.salem.messaging;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.salem.messaging.Module.MessageModul;
import com.salem.messaging.Adapter.MessageAdapter;
import com.wafflecopter.multicontactpicker.ContactResult;
import com.wafflecopter.multicontactpicker.LimitColumn;
import com.wafflecopter.multicontactpicker.MultiContactPicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class MainActivity3 extends AppCompatActivity {

    public static final int RC_SIGN_IN = 1;

    ListView mMessageListView;
    ProgressBar mProgressBar;
    Toolbar toolbar;
    ArrayList<MessageModul> msgList;
    EditText mMessageEditText;
    TextView contactsList;
    Button mSendButton;
    ImageButton contacts;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ChildEventListener childEventListener;
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseUser user;

    public static List<ContactResult> results=new ArrayList<>();
    Intent intent;
    private static final int CONTACT_PICKER_REQUEST = 202 ;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.READ_CONTACTS
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Chat");
        setSupportActionBar(toolbar);

        intent = getIntent();
        firebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        databaseReference = firebaseDatabase.getReference().child(user.getDisplayName()).child("Messages").child(intent.getStringExtra("number"));

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mMessageListView = (ListView) findViewById(R.id.messageListView);
        mMessageEditText = (EditText) findViewById(R.id.messageEditText);
        mSendButton = (Button) findViewById(R.id.sendButton);
        contacts=findViewById(R.id.photoPickerButton);
        contactsList=findViewById(R.id.textView);
        contactsList.setVisibility(View.INVISIBLE);

        msgList = new ArrayList<>();
        setSupportActionBar(toolbar);

        final MessageAdapter arrayAdapter = new MessageAdapter(this, msgList);
        mMessageListView.setAdapter(arrayAdapter);

        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)});

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String msg=mMessageEditText.getText().toString();
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                String formattedDate = df.format(c.getTime());
                databaseReference.push().setValue(new MessageModul(msg, formattedDate));
                mMessageEditText.setText("");

                try {

                        for (int j = 0; j < results.size(); j++) {

                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(results.get(j).getPhoneNumbers().get(0).getNumber(), null, msg, null, null);
                            Toast.makeText(MainActivity3.this, "Successfully sent", Toast.LENGTH_SHORT).show();
                        }


                }catch (Exception e){
                   e.printStackTrace();
                    Toast.makeText(MainActivity3.this, "Failed", Toast.LENGTH_SHORT).show();
                }

            }
        });

        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMessageListView.setAlpha(0.2f);
                mMessageEditText.setAlpha(0.2f);
                contacts.setAlpha(0.2f);
                StringBuilder stringBuilder= new StringBuilder("");
                for(int i =0;i<results.size();i++){
                    stringBuilder.append(results.get(i).getDisplayName()+"\n");
                }
                contactsList.setText(stringBuilder.toString());
                contactsList.setVisibility(View.VISIBLE);

            }
        });

        contactsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMessageListView.setAlpha(1f);
                mMessageEditText.setAlpha(1f);
                contacts.setAlpha(1f);
                contactsList.setVisibility(View.INVISIBLE);
            }
        });


        childEventListener= new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MessageModul modul= snapshot.getValue(MessageModul.class);
                arrayAdapter.add(modul);
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
        if(requestCode == CONTACT_PICKER_REQUEST){
            if(resultCode == RESULT_OK) {
                results = MultiContactPicker.obtainResult(data);
                Log.d("MyTag", results.get(0).getDisplayName());
            } else if(resultCode == RESULT_CANCELED){
                System.out.println("User closed the picker without selecting items.");
            }
        }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.about:
                Toast.makeText(this, "Created By Team 4", Toast.LENGTH_LONG).show();
                break;

            case R.id.close:
                finish();
                System.exit(0);
                break;

            case R.id.add:
                xxx();
                break;

            case R.id.scheduled:
                Intent i = new Intent(MainActivity3.this,EditScheduled.class);
                i.putExtra("month1",intent.getStringExtra("month"));
                i.putExtra("day1",intent.getStringExtra("day"));
                i.putExtra("title1",intent.getStringExtra("title"));
                i.putExtra("number1",intent.getStringExtra("number"));
                startActivity(i);
                break;

            default:

        }
        return super.onOptionsItemSelected(item);
    }

    public void xxx(){
        new MultiContactPicker.Builder(MainActivity3.this) //Activity/fragment context
                .hideScrollbar(false) //Optional - default: false
                .showTrack(true) //Optional - default: true
                .searchIconColor(Color.WHITE) //Option - default: White
                .setChoiceMode(MultiContactPicker.CHOICE_MODE_MULTIPLE) //Optional - default: CHOICE_MODE_MULTIPLE
                .handleColor(ContextCompat.getColor(MainActivity3.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                .bubbleColor(ContextCompat.getColor(MainActivity3.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                .bubbleTextColor(Color.WHITE) //Optional - default: White
                .setTitleText("Select Contacts") //Optional - default: Select Contacts
                .setLoadingType(MultiContactPicker.LOAD_ASYNC) //Optional - default LOAD_ASYNC (wait till all loaded vs stream results)
                .limitToColumn(LimitColumn.NONE) //Optional - default NONE (Include phone + email, limiting to one can improve loading time)
                .setActivityAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                        android.R.anim.fade_in,
                        android.R.anim.fade_out) //Optional - default: No animation overrides
                .showPickerForResult(CONTACT_PICKER_REQUEST);
    }
}


