package hu.home.android.android_hangman;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

    private char aktivBetu;
    private List<String> tippeltBetuk;
    private int[] kepek = IntStream.range(R.drawable.akasztofa00, R.drawable.akasztofa13 + 1).toArray();
    private int hibak;

    private StringBuilder sb;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        addEventListeners();
    }

    private void addEventListeners() {
        minuszBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (aktivBetu == 'A') {
                    aktivBetu = 'Z';
                } else {
                    aktivBetu--;
                }
                if (tippeltBetuk.contains(String.valueOf(aktivBetu))) {
                    tippTextView.setTextColor(getResources().getColor(R.color.black));
                } else {
                    tippTextView.setTextColor(Color.parseColor("#d11723"));
                }
                tippTextView.setText(String.valueOf(aktivBetu));
            }
        });
        pluszBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (aktivBetu == 'Z') {
                    aktivBetu = 'A';
                } else {
                    aktivBetu++;
                }
                if (tippeltBetuk.contains(String.valueOf(aktivBetu))) {
                    tippTextView.setTextColor(getResources().getColor(R.color.black));
                } else {
                    tippTextView.setTextColor(Color.parseColor("#d11723"));
                }
                tippTextView.setText(String.valueOf(aktivBetu));
            }
        });
        tippelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tippTextView.setTextColor(getResources().getColor(R.color.black));
                if (tippeltBetuk.contains(String.valueOf(aktivBetu))) {
                    Toast.makeText(MainActivity.this, "Ezt a betűt már tippelte", Toast.LENGTH_SHORT).show();
                    return;
                }
                tippeltBetuk.add(String.valueOf(aktivBetu));
                if (veletlenSzo.toUpperCase().indexOf(aktivBetu) == -1) {

                    hibak++;
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
                    sb = new StringBuilder(kiirtSzo);
                    for (int i = 0; i < veletlenSzo.length(); i++) {
                        char tempChar = veletlenSzo.toUpperCase().charAt(i);
                        if (tempChar == aktivBetu) {
                            sb.setCharAt(i * 2, aktivBetu);
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
        szavakLista = new ArrayList<>(Arrays.asList("almafa", "halaszle", "karacsonyfa", "toltottkaposzta", "magyarorszag",
                "sapka", "kakukktojas", "disznotap", "alkoholmergezes", "parajfozelek", "zoldtea", "futball", "kosarlaba",
                "epitkezes", "magyartanar", "iskolaudvar", "jatszoter", "hajlektalan", "budapest", "romania",
                "starwars", "tvmusor", "akasztofajatek", "asztalitenisz", "kecskeviadal", "bananpure", "tukortojas"));
        veletlenSzo = szavakLista.get(rnd.nextInt(szavakLista.size()));
        sb = new StringBuilder();
        for (int i = 0; i < veletlenSzo.length(); i++) {
            sb.append("_ ");
        }
        sb.setLength(sb.length() - 1);
        kiirtSzo = sb.toString();
        Log.d("veletlen", veletlenSzo);
        resultTextView.setText(kiirtSzo);

        aktivBetu = 'A';
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

        ujJatek();
    }
}