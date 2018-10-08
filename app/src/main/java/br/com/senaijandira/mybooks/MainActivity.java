package br.com.senaijandira.mybooks;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import br.com.senaijandira.mybooks.adapter.LivroAdapter;
import br.com.senaijandira.mybooks.db.MyBooksDatabase;
import br.com.senaijandira.mybooks.model.Livro;

public class MainActivity extends AppCompatActivity {

    //ListVew que carregará os livros
    ListView lstViewLivros;

    public static Livro[] livros;

    //Variavel de acesso ao Banco
    private MyBooksDatabase myBooksDb;

    //Adapter para criar a lista de livros
    LivroAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Criando a instancia do banco de dados
        myBooksDb = Room.databaseBuilder(getApplicationContext(),
                MyBooksDatabase.class, Utils.DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        lstViewLivros = findViewById(R.id.lstViewLivros);

        //Criar o adapter
        adapter = new LivroAdapter(this, myBooksDb);

        lstViewLivros.setAdapter(adapter);
    }



    @Override
    protected void onResume() {
        super.onResume();

        //Aqui faz um select no banco
        livros = myBooksDb.daoLivro().selecionarTodos();

        //Limpando a listView
        adapter.clear();

        //Adicionando os livros a lista
        adapter.addAll(livros);

    }

    // ********** INSTANCIAR ANTES NO manifests/AndroidManifest.xml
    public void abrirCadastro(View v){
        startActivity(new Intent(this,
                CadastroActivity.class));
    }

    public void lerLivro(View v){
        startActivity(new Intent(this, LerLivroActivity.class));
    }

}