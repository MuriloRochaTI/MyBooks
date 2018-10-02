package br.com.senaijandira.mybooks;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class LerLivroActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {

        setContentView(R.layout.activity_ler_livro);

        super.onCreate(savedInstanceState, persistentState);

    }
}

