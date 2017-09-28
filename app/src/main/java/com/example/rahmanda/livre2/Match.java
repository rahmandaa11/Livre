package com.example.rahmanda.livre2;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Match extends AppCompatActivity {

    public static final String MATCH_ID = "matchid";
    public static final String TEAM1_NAME = "team1name";
    public static final String TEAM2_NAME = "team2name";
    public static final String LOCATION_IDM = "location_idm";
    public static final String TOKEN = "tokenMatch";

    TextView tvTitle;
    TextView tvSubtitle;

    ListView listViewMatch;
    DatabaseReference databaseMatch;
    DatabaseReference databaseScore, databaseSets;
    Button btnNewMatch;

    List<ModalMatch> matches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

       tvTitle = (TextView) findViewById(R.id.tvTitleLocation);
       tvSubtitle = (TextView) findViewById(R.id.tvTitleRegion);
        btnNewMatch = (Button) findViewById(R.id.btnNewmatch);

        listViewMatch = (ListView) findViewById(R.id.listViewMatch);

        matches = new ArrayList<>();

        Intent intent = getIntent();
        final String id = intent.getStringExtra(Home.LOCATION_ID);
        String locationname = intent.getStringExtra(Home.LOCATION_NAME);
        tvTitle.setText(locationname);

        databaseMatch = FirebaseDatabase.getInstance().getReference("match").child(id);

        Intent i = getIntent();
        final String token = i.getStringExtra(Home.TOKEN);

        switch (token){
            case "0" :
                btnNewMatch.setVisibility(View.INVISIBLE);
                break;
            case  "1" :
                btnNewMatch.setVisibility(View.VISIBLE);
                break;
            default:
                btnNewMatch.setVisibility(View.INVISIBLE);
        }

        initSpinnerSport();

        //tutorial 3
        listViewMatch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ModalMatch match = matches.get(i);

                Intent intent = new Intent(getApplicationContext(), Score.class);
                intent.putExtra(TOKEN, token);
                intent.putExtra(MATCH_ID, match.getMatchId());
                intent.putExtra(TEAM1_NAME, match.getTeam1());
                intent.putExtra(TEAM2_NAME, match.getTeam2());
                intent.putExtra(LOCATION_IDM, id);


                startActivity(intent);

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseMatch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                matches.clear();
                for (DataSnapshot matchSnapshot : dataSnapshot.getChildren()){
                    ModalMatch match = matchSnapshot.getValue(ModalMatch.class);
                    matches.add(match);
                }

                AdapterMatch matchAdapter = new AdapterMatch(Match.this, matches);
                listViewMatch.setAdapter(matchAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initSpinnerSport(){
        final Spinner spinnerSport = (Spinner) findViewById(R.id.spinnerSport);
        // Spinner Drop down elements
        final ArrayList<String> languages = new ArrayList<String>();
        languages.add("Badminton");
        languages.add("Basket (ComingSoon)");
        languages.add("Voli (ComingSoon)");

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getApplicationContext(), languages);
        spinnerSport.setAdapter(customSpinnerAdapter);
        spinnerSport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

   public void newMatch(View view){
       showAddMatchDialog();
   }

   private void showAddMatchDialog(){
       AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);  //Dialog
       LayoutInflater inflater = getLayoutInflater();  //Dialog

       final View dialogView = inflater.inflate(R.layout.add_match_dialog, null); //Dialog
       dialogBuilder.setView(dialogView);  //Dialog

       final Spinner spinnerSport = (Spinner) dialogView.findViewById(R.id.spinnerSport);    //Dialog
       final EditText editTextTeam1 = (EditText) dialogView.findViewById(R.id.editTextTeam1);
       final EditText editTextTeam2 = (EditText) dialogView.findViewById(R.id.editTextTeam2);   //Dialog
       final Button buttonAddMatch = (Button) dialogView.findViewById(R.id.buttonAddMatch);

       final ArrayList<String> languages = new ArrayList<String>();
       languages.add("Badminton");
       languages.add("Basket (ComingSoon)");
       languages.add("Voli (ComingSoon)");

       CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getApplicationContext(), languages);
       spinnerSport.setAdapter(customSpinnerAdapter);


       final AlertDialog alertDialog = dialogBuilder.create();   //Dialog
       alertDialog.show(); //Dialog

       buttonAddMatch.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String team1 = editTextTeam1.getText().toString().trim();
               String team2 = editTextTeam2.getText().toString().trim();

               if (!TextUtils.isEmpty(team1)){
                   String idm = databaseMatch.push().getKey();

                   ModalMatch match = new ModalMatch(idm, team1, team2);

                   databaseMatch.child(idm).setValue(match);

                   Intent intent = getIntent();
                   final String id = intent.getStringExtra(Home.LOCATION_ID);

                   databaseScore = FirebaseDatabase.getInstance().getReference("score").child(id).child(idm);
                   int score1 = 0;
                   int score2 = 0;

                   ModalScore score = new ModalScore(score1, score2);
                   databaseScore.setValue(score);

                   databaseSets = FirebaseDatabase.getInstance().getReference("sets").child(id).child(idm);
                   String set1 = "", set2 = "", set3 = "", set4 = "", set5 = "", set6 = "";

                   ModalSets sets = new ModalSets(set1, set2, set3, set4, set5, set6);
                   databaseSets.setValue(sets);

                   alertDialog.dismiss();

               } else {
                   Toast.makeText(Match.this, "Track team should not be empty", Toast.LENGTH_SHORT).show();
               }
           }
       });

   }

    public void btnBackMatch(View view){
    }

}
