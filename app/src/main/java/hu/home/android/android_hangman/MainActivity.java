package hu.home.android.android_hangman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class MainActivity extends AppCompatActivity {

    private Button minuszBtn;
    private Button pluszBtn;
    private Button tippelBtn;
    private TextView tippTextView;
    private ImageView hangmanImg;
    private TextView resultTextView;

    private Random rnd;
    private List<String> szavakLista;
    private String veletlenSzo;
    private String kiirtSzo;

    private char[] betuk;
    private int aktivBetu;
    private ArrayList<String> tippeltBetuk;
    private int[] kepek = IntStream.range(R.drawable.akasztofa00, R.drawable.akasztofa13 + 1).toArray();
    private int hibak;

    private StringBuilder sb;

    public MainActivity() {
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("aktivBetu", aktivBetu);
        outState.putInt("hibak", hibak);
        outState.putString("kiirtSzo", kiirtSzo);
        outState.putString("veletlenSzo", veletlenSzo);
        outState.putStringArrayList("tippeltBetuk", tippeltBetuk);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        init();
        addEventListeners();
        if (savedInstanceState != null){
            Log.d("masodikInstance", "masodik ");
            aktivBetu = savedInstanceState.getInt("aktivBetu");
            hibak = savedInstanceState.getInt("hibak");
            kiirtSzo = savedInstanceState.getString("kiirtSzo");
            veletlenSzo = savedInstanceState.getString("veletlenSzo");
            tippeltBetuk = savedInstanceState.getStringArrayList("tippeltBetuk");

            tippTextView.setText(String.valueOf(betuk[aktivBetu]));
            if (tippeltBetuk.contains(String.valueOf(betuk[aktivBetu]))){
                tippTextView.setTextColor(getResources().getColor(R.color.black));
            }
            resultTextView.setText(kiirtSzo);
            hangmanImg.setImageResource(kepek[hibak]);
        }
    }

    private void addEventListeners() {
        minuszBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (aktivBetu == 0) {
                    aktivBetu = betuk.length - 1;
                } else {
                    aktivBetu--;
                }
                if (tippeltBetuk.contains(String.valueOf(betuk[aktivBetu]))) {
                    tippTextView.setTextColor(getResources().getColor(R.color.black));
                } else {
                    tippTextView.setTextColor(Color.parseColor("#d11723"));
                }
                tippTextView.setText(String.valueOf(betuk[aktivBetu]));
            }
        });
        pluszBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (aktivBetu == betuk.length - 1) {
                    aktivBetu = 0;
                } else {
                    aktivBetu++;
                }
                if (tippeltBetuk.contains(String.valueOf(betuk[aktivBetu]))) {
                    tippTextView.setTextColor(getResources().getColor(R.color.black));
                } else {
                    tippTextView.setTextColor(Color.parseColor("#d11723"));
                }
                tippTextView.setText(String.valueOf(betuk[aktivBetu]));
            }
        });
        tippelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tippTextView.setTextColor(getResources().getColor(R.color.black));
                if (tippeltBetuk.contains(String.valueOf(betuk[aktivBetu]))) {
                    Toast.makeText(MainActivity.this, "Ezt a betűt már tippelte", Toast.LENGTH_SHORT).show();
                    return;
                }
                tippeltBetuk.add(String.valueOf(betuk[aktivBetu]));
                if (veletlenSzo.toUpperCase().indexOf(betuk[aktivBetu]) == -1) {

                    hibak++;
                    Toast.makeText(MainActivity.this, "Hibás tipp!", Toast.LENGTH_SHORT).show();
                    hangmanImg.setImageResource(kepek[hibak]);
                    Log.d("vereseg", String.valueOf(hibak));
                    if (hibak == 13){
                        new AlertDialog.Builder(MainActivity.this).setTitle("Nem sikerült kitalálni!")
                                .setMessage("Szeretnél még egyet játszani?")
                                .setPositiveButton("Nem", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                })
                                .setNegativeButton("Igen", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    ujJatek();
                                    }
                                }).setCancelable(false).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Helyes tipp!", Toast.LENGTH_SHORT).show();
                    sb = new StringBuilder(kiirtSzo);
                    for (int i = 0; i < veletlenSzo.length(); i++) {
                        char tempChar = veletlenSzo.toUpperCase().charAt(i);
                        if (tempChar == betuk[aktivBetu]) {
                            sb.setCharAt(i * 2, betuk[aktivBetu]);
                        }
                    }
                    kiirtSzo = sb.toString();
                    resultTextView.setText(kiirtSzo);
                }
                if (kiirtSzo.indexOf('_') == -1) {
                    Log.d("nyert", "on nyert");
                    new AlertDialog.Builder(MainActivity.this).setTitle("Helyes megfejtés!")
                            .setMessage("Szeretnél még egyet játszani?")
                            .setPositiveButton("Nem", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            })
                            .setNegativeButton("Igen", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ujJatek();
                                }
                            }).setCancelable(false).show();
                }
            }
        });
    }

    private void ujJatek() {
        rnd = new Random();
        szavakLista = new ArrayList<>(Arrays.asList("almafa", "halászlé", "karácsonyfa", "töltöttkáposzta", "magyarország",
                "sapka", "kakukktojás", "disznótáp", "alkoholmérgezés", "parajfőzelék", "zöldtea", "futball", "kosárlabda",
                "építkezés", "magyartanár", "iskolaudvar", "játszótér", "hajléktalan", "budapest", "románia",
                "starwars", "tvműsor", "akasztófajáték", "asztalitenisz", "kecskeviadal", "banánpüré", "tükörtojás"));
        veletlenSzo = szavakLista.get(rnd.nextInt(szavakLista.size()));
        sb = new StringBuilder();
        for (int i = 0; i < veletlenSzo.length(); i++) {
            sb.append("_ ");
        }
        sb.setLength(sb.length() - 1);
        kiirtSzo = sb.toString();
        Log.d("veletlen", veletlenSzo);
        resultTextView.setText(kiirtSzo);

        aktivBetu = 0;
        tippeltBetuk = new ArrayList<>();
        hibak = 0;
        hangmanImg.setImageResource(kepek[0]);
        tippTextView.setText("A");
    }

    private void init() {
        minuszBtn = findViewById(R.id.minuszBtn);
        pluszBtn = findViewById(R.id.pluszBtn);
        tippelBtn = findViewById(R.id.tippelBtn);
        tippTextView = findViewById(R.id.tippTextView);
        tippTextView.setTextColor(Color.parseColor("#d11723"));
        hangmanImg = findViewById(R.id.hangmanImg);
        resultTextView = findViewById(R.id.resultTextView);
        betuk = new char[] {'A', 'Á', 'B','C','D','E','É','F','G','H','I','Í','J','K','L','M',
                'O','Ó','Ö','Ő','P','Q','R','S','T','U','Ú','Ü','Ű','V','W','X','Y','Z'};

        ujJatek();
    }
}