package com.example.rahmanda.livre2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Score extends AppCompatActivity {

    TextView tvTeam1, tvTeam2, tvSet1, tvSet2, tvSet3, tvSet4, tvSet5, tvSet6,tvTitleSet, tvScore1, tvScore2;
    Button btnScore1plus, btnScore2plus, btnScore1min, btnScore2min,btnShare;

    DatabaseReference databaseScore, databaseSets, databaseMatch;

    String shareBody = "share via";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        Deklaration();

        Intent i = getIntent();
        final String token = i.getStringExtra(Match.TOKEN);

        switch (token){
            case "0" :
                btnScore1plus.setVisibility(View.INVISIBLE);
                btnScore2plus.setVisibility(View.INVISIBLE);
                btnScore1min.setVisibility(View.INVISIBLE);
                btnScore2min.setVisibility(View.INVISIBLE);
                btnShare.setVisibility(View.INVISIBLE);
                break;
            case  "1" :
                btnScore1plus.setVisibility(View.VISIBLE);
                btnScore2plus.setVisibility(View.VISIBLE);
                btnScore1min.setVisibility(View.VISIBLE);
                btnScore2min.setVisibility(View.VISIBLE);
                btnShare.setVisibility(View.VISIBLE);
                break;
            default:
                btnScore1plus.setVisibility(View.INVISIBLE);
                btnScore2plus.setVisibility(View.INVISIBLE);
                btnScore1min.setVisibility(View.INVISIBLE);
                btnScore2min.setVisibility(View.INVISIBLE);
                btnShare.setVisibility(View.INVISIBLE);
        }


        Intent intent = getIntent();
        String id = intent.getStringExtra(Match.MATCH_ID);
        String team1 = intent.getStringExtra(Match.TEAM1_NAME);
        String team2 = intent.getStringExtra(Match.TEAM2_NAME);
        String locationidm = intent.getStringExtra(Match.LOCATION_IDM);

        tvTeam1.setText(team1);
        tvTeam2.setText(team2);

        databaseSets = FirebaseDatabase.getInstance().getReference("sets").child(locationidm).child(id);

        AddScoreTeam();
    }


    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        String id = intent.getStringExtra(Match.MATCH_ID);
        String locationidm = intent.getStringExtra(Match.LOCATION_IDM);

        databaseSets = FirebaseDatabase.getInstance().getReference("sets").child(locationidm).child(id);


            databaseScore = FirebaseDatabase.getInstance().getReference("score").child(locationidm).child(id);
            databaseScore.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ModalScore scoreview = dataSnapshot.getValue(ModalScore.class);
                int intscore1 = scoreview.getScore1();
                int intscore2 = scoreview.getScore2();

                ModalScore score = new ModalScore(intscore1, intscore2);
                databaseScore.setValue(score);

                tvScore1.setText(String .valueOf(score.getScore1()));
                tvScore2.setText(String .valueOf(score.getScore2()));


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        if(tvSet1.getText().equals("")){
            tvSet1.setText(tvScore1.getText());
        } else if (tvSet2.getText().equals("")){
            tvSet2.setText(tvScore1.getText());
        } else {
            tvSet3.setText(tvScore1.getText());
        }
        if(tvSet4.getText().equals("")){
            tvSet4.setText(tvScore2.getText());
        } else if (tvSet5.getText().equals("")){
            tvSet5.setText(tvScore2.getText());
        } else {
            tvSet6.setText(tvScore2.getText());
        }

        databaseSets.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ModalSets setsview = dataSnapshot.getValue(ModalSets.class);
                String set1 = setsview.getSet1();
                String set2 = setsview.getSet2();
                String set3 = setsview.getSet3();
                String set4 = setsview.getSet4();
                String set5 = setsview.getSet5();
                String set6 = setsview.getSet6();

                ModalSets sets = new ModalSets(set1, set2, set3, set4, set5, set6);
                databaseSets.setValue(sets);

                tvSet1.setText(sets.getSet1());
                tvSet2.setText(sets.getSet2());
                tvSet3.setText(sets.getSet3());
                tvSet4.setText(sets.getSet4());
                tvSet5.setText(sets.getSet5());
                tvSet6.setText(sets.getSet6());

                if(tvSet1.getText().equals("") && tvSet4.getText().equals("")){
                    tvTitleSet.setText("Set 1");

                } else if (tvSet2.getText().equals("") && tvSet5.getText().equals("")){
                    tvTitleSet.setText("Set 2");

                } else {
                    tvTitleSet.setText("Set 3");

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void Deklaration (){
        tvTeam1 = (TextView) findViewById(R.id.tvTeam1);
        tvTeam2 = (TextView) findViewById(R.id.tvTeam2);
        tvSet1 = (TextView) findViewById(R.id.tvSet1);
        tvSet2 = (TextView) findViewById(R.id.tvSet2);
        tvSet3 = (TextView) findViewById(R.id.tvSet3);
        tvSet4 = (TextView) findViewById(R.id.tvSet4);
        tvSet5 = (TextView) findViewById(R.id.tvSet5);
        tvSet6 = (TextView) findViewById(R.id.tvSet6);
        tvTitleSet = (TextView) findViewById(R.id.tvTitleSet);
        tvScore1 = (TextView) findViewById(R.id.tvScore1);
        tvScore2 = (TextView) findViewById(R.id.tvScore2);


        btnScore1min = (Button) findViewById(R.id.btnScore1min);
        btnScore1plus = (Button) findViewById(R.id.btnScore1plus);
        btnScore2plus = (Button) findViewById(R.id.btnScore2plus);
        btnScore2min = (Button) findViewById(R.id.btnScore2min);
        btnShare = (Button) findViewById(R.id.btnShare);
    }

    public void AddScoreTeam(){

        btnScore1plus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = getIntent();
                String id = intent.getStringExtra(Match.MATCH_ID);
                String locationidm = intent.getStringExtra(Match.LOCATION_IDM);

                databaseScore = FirebaseDatabase.getInstance().getReference("score").child(locationidm).child(id);
                databaseScore.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ModalScore scoreview = dataSnapshot.getValue(ModalScore.class);
                        int intscore1 = scoreview.getScore1();
                        int intscore2 = scoreview.getScore2();

                        ModalScore score = new ModalScore(intscore1+1, intscore2);
                        databaseScore.setValue(score);

                        tvScore1.setText(String .valueOf(score.getScore1()));
                        tvScore2.setText(String .valueOf(score.getScore2()));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        btnScore1min.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = getIntent();
                String id = intent.getStringExtra(Match.MATCH_ID);
                String locationidm = intent.getStringExtra(Match.LOCATION_IDM);

                databaseScore = FirebaseDatabase.getInstance().getReference("score").child(locationidm).child(id);
                databaseScore.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ModalScore scoreview = dataSnapshot.getValue(ModalScore.class);
                        int intscore1 = scoreview.getScore1();
                        int intscore2 = scoreview.getScore2();

                        ModalScore score = new ModalScore(intscore1-1, intscore2);
                        databaseScore.setValue(score);

                        tvScore1.setText(String .valueOf(score.getScore1()));
                        tvScore2.setText(String .valueOf(score.getScore2()));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        btnScore2plus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = getIntent();
                String id = intent.getStringExtra(Match.MATCH_ID);
                String locationidm = intent.getStringExtra(Match.LOCATION_IDM);

                databaseScore = FirebaseDatabase.getInstance().getReference("score").child(locationidm).child(id);
                databaseScore.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ModalScore scoreview = dataSnapshot.getValue(ModalScore.class);
                        int intscore1 = scoreview.getScore1();
                        int intscore2 = scoreview.getScore2();

                        ModalScore score = new ModalScore(intscore1, intscore2+1);
                        databaseScore.setValue(score);

                        tvScore1.setText(String .valueOf(score.getScore1()));
                        tvScore2.setText(String .valueOf(score.getScore2()));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        btnScore2min.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = getIntent();
                String id = intent.getStringExtra(Match.MATCH_ID);
                String locationidm = intent.getStringExtra(Match.LOCATION_IDM);

                databaseScore = FirebaseDatabase.getInstance().getReference("score").child(locationidm).child(id);
                databaseScore.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ModalScore scoreview = dataSnapshot.getValue(ModalScore.class);
                        int intscore1 = scoreview.getScore1();
                        int intscore2 = scoreview.getScore2();

                        ModalScore score = new ModalScore(intscore1, intscore2-1);
                        databaseScore.setValue(score);

                        tvScore1.setText(String .valueOf(score.getScore1()));
                        tvScore2.setText(String .valueOf(score.getScore2()));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    public void share(View view){
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(android.content.Intent.EXTRA_SUBJECT, "My App");
        share.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(share, "share via"));
    }

    public void endMatch(View view) {
        Intent intent = getIntent();
        String id = intent.getStringExtra(Match.MATCH_ID);
        String locationidm = intent.getStringExtra(Match.LOCATION_IDM);
        String team1 = intent.getStringExtra(Match.TEAM1_NAME);
        String team2 = intent.getStringExtra(Match.TEAM2_NAME);
        String set1, set2, set3, set4, set5, set6;

        databaseMatch = FirebaseDatabase.getInstance().getReference("match").child(locationidm);
        databaseSets = FirebaseDatabase.getInstance().getReference("sets").child(locationidm).child(id);


        if(tvSet1.getText().equals("") && tvSet4.getText().equals("")){
            tvTitleSet.setText("Set 1");

            tvSet1.setText(tvScore1.getText());
            tvSet4.setText(tvScore2.getText());

            set1 = tvSet1.getText().toString();
            set2 = tvSet2.getText().toString();
            set3 = tvSet3.getText().toString();
            set4 = tvSet4.getText().toString();
            set5 = tvSet5.getText().toString();
            set6 = tvSet6.getText().toString();

            ModalSets sets = new ModalSets(set1, set2, set3, set4, set5, set6);
            databaseSets.setValue(sets);

        } else if (tvSet2.getText().equals("") && tvSet5.getText().equals("")){
            tvTitleSet.setText("Set 2");

            tvSet2.setText(tvScore1.getText());
            tvSet5.setText(tvScore2.getText());

            set1 = tvSet1.getText().toString();
            set2 = tvSet2.getText().toString();
            set3 = tvSet3.getText().toString();
            set4 = tvSet4.getText().toString();
            set5 = tvSet5.getText().toString();
            set6 = tvSet6.getText().toString();

            ModalSets sets = new ModalSets(set1, set2, set3, set4, set5, set6);
            databaseSets.setValue(sets);

        } else {

            tvTitleSet.setText("Set 3");
            tvSet3.setText(tvScore1.getText());
            tvSet6.setText(tvScore2.getText());

            set1 = tvSet1.getText().toString();
            set2 = tvSet2.getText().toString();
            set3 = tvSet3.getText().toString();
            set4 = tvSet4.getText().toString();
            set5 = tvSet5.getText().toString();
            set6 = tvSet6.getText().toString();

            ModalSets sets = new ModalSets(set1, set2, set3, set4, set5, set6);
            databaseSets.setValue(sets);

        }


    }

    public void btnBackScore (View view) {
       Toast.makeText(getApplicationContext(), "Not working, please use button back on your phone", Toast.LENGTH_SHORT);
    }
}
