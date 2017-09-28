package com.example.rahmanda.livre2;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Region;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class Home extends AppCompatActivity {

    public static final String LOCATION_NAME = "locationname";  //tutorial 3,  variable yang akan di intent ke activity lain
    public static final String LOCATION_ID = "locationid";  //tutorial 3
    public static final String TOKEN = "tokenHome";

    DatabaseReference databaseRegion;

    ListView listViewRegion;
    List<ModalRegion> regions;
    Button btnNewMatch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btnNewMatch = (Button) findViewById(R.id.btnNewmatch);

        databaseRegion = FirebaseDatabase.getInstance().getReference("region");

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitleTextColor(Color.parseColor("#ffffff"));

        Intent i = getIntent();
        final String token = i.getStringExtra(MainActivity.TOKEN);

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

        //spinner custom
        initSpinnerCostum();

        listViewRegion = (ListView) findViewById(R.id.ListViewLocation);

        regions = new ArrayList<>();

        //tutorial 3
        listViewRegion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ModalRegion region = regions.get(i);

                Intent intent = new Intent(getApplicationContext(), Match.class);
                intent.putExtra(TOKEN, token);
                intent.putExtra(LOCATION_ID, region.getLocationId());
                intent.putExtra(LOCATION_NAME, region.getLocationName());

                startActivity(intent);

            }
        }); //tutorial 3

        listViewRegion.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {  //fungsi jika listview di tekan lama
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                switch (token){
                    case "0" :

                        break;
                    case  "1" :
                        ModalRegion region = regions.get(i);
                        showDialogDelete(region.getLocationId(), region.getLocationName());
                        break;
                    default:
                }
                return false;
            }
        });


    }




    @Override
    protected void onStart() {
        super.onStart();

        databaseRegion.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                regions.clear();
                for (DataSnapshot regionSnapshot : dataSnapshot.getChildren()){
                    ModalRegion region = regionSnapshot.getValue(ModalRegion.class);

                    regions.add(region);
                }

                AdapterRegion adapter = new AdapterRegion(Home.this, regions);
                listViewRegion.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.moreoption, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout_item:
                Intent i = getIntent();
                final String token = i.getStringExtra(MainActivity.TOKEN);

                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra(TOKEN, token);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initSpinnerCostum(){
        final Spinner spinnerCustom = (Spinner) findViewById(R.id.spinnerCustom);
        // Spinner Drop down elements
        final ArrayList<String> languages = new ArrayList<String>();
        languages.add("Jabodetabek");
        languages.add("Bandung (ComingSoon)");
        languages.add("Yogyakarta(ComingSoon)");

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getApplicationContext(), languages);
        spinnerCustom.setAdapter(customSpinnerAdapter);
        spinnerCustom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void newMatch(View view) {
        showAddLocationDialog();
    }

    private void showAddLocationDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);  //Dialog
        LayoutInflater inflater = getLayoutInflater();  //Dialog

        final View dialogView = inflater.inflate(R.layout.add_dialog, null); //Dialog
        dialogBuilder.setView(dialogView);  //Dialog

        final EditText editTextLocation = (EditText) dialogView.findViewById(R.id.editTextLocation);    //Dialog
        final Spinner spinnerRegion = (Spinner) dialogView.findViewById(R.id.spinnerRegion);    //Dialog
        final Button buttonAddLocation = (Button) dialogView.findViewById(R.id.buttonAddLocation);

        final ArrayList<String> languages = new ArrayList<String>();
        languages.add("Jabodetabek");
        languages.add("Bandung (ComingSoon)");
        languages.add("Yogyakarta(ComingSoon)");

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getApplicationContext(), languages);
        spinnerRegion.setAdapter(customSpinnerAdapter);


        final AlertDialog alertDialog = dialogBuilder.create();   //Dialog
        alertDialog.show(); //Dialog

        buttonAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String location = editTextLocation.getText().toString().trim();

                if (!TextUtils.isEmpty(location)){
                    String id = databaseRegion.push().getKey();

                    ModalRegion region = new ModalRegion(id, location);
                    databaseRegion.child(id).setValue(region);

                    Toast.makeText(getApplicationContext(), "Location added", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();

                } else {
                    Toast.makeText(getApplicationContext(), "You Should Enter a location", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void showDialogDelete (final String locationId, String locationName){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);  //Dialog
        LayoutInflater inflater = getLayoutInflater();  //Dialog

        final View dialogView = inflater.inflate(R.layout.deletedialog, null); //Dialog
        dialogBuilder.setView(dialogView);  //Dialog

        final TextView tvDelete = (TextView) dialogView.findViewById(R.id.textViewDelete);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDelete);

        final AlertDialog alertDialog = dialogBuilder.create();   //Dialog
        alertDialog.show(); //Dialog

        tvDelete.setText(locationName);

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteLocation(locationId);

                alertDialog.dismiss();
            }
        });



    }

    private void deleteLocation(String locationId) {
        DatabaseReference drlocation = FirebaseDatabase.getInstance().getReference("region").child(locationId);
        DatabaseReference drmatch = FirebaseDatabase.getInstance().getReference("match").child(locationId);
        DatabaseReference drscore = FirebaseDatabase.getInstance().getReference("score").child(locationId);
        DatabaseReference drsets= FirebaseDatabase.getInstance().getReference("sets").child(locationId);

        drlocation.removeValue();
        drmatch.removeValue();
        drscore.removeValue();
        drsets.removeValue();

        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
    }

}
