package br.com.senaijandira.mybooks;

import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import br.com.senaijandira.mybooks.adapter.LivroAdapter;
import br.com.senaijandira.mybooks.db.MyBooksDatabase;
import br.com.senaijandira.mybooks.model.Livro;

public class LerLivroActivity extends AppCompatActivity {

    //ListVew que carregará os livros
    ListView lstViewLerLivros;
    Bitmap livroCapa;
    ImageView imgLivroCapa;
    EditText txtTitulo, txtDescricao;
    Livro livro;

    public static Livro[] livros;

    //Variavel de acesso ao Banco
    private MyBooksDatabase myBooksDb;

    //Adapter para criar a lista de livros
    LivroAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ler_livro);

        //Criando a instancia do banco de dados
        myBooksDb = Room.databaseBuilder(getApplicationContext(),
                MyBooksDatabase.class, Utils.DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        lstViewLerLivros = findViewById(R.id.lstViewLerLivros);
        imgLivroCapa = findViewById(R.id.imgLivroCapa);

        txtDescricao = (EditText)findViewById(R.id.txtDescricao);

        txtTitulo = (EditText)findViewById(R.id.txtTitulo);


        //Criar o adapter
        adapter = new LivroAdapter(this, myBooksDb);

        lstViewLerLivros.setAdapter(adapter);
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


    public void alert(String titulo, String msg){

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(titulo);
        alert.setMessage(msg);

        alert.create().show();
    }

}