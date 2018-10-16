package br.com.senaijandira.mybooks;

import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import br.com.senaijandira.mybooks.adapter.LivroAdapter;
import br.com.senaijandira.mybooks.db.MyBooksDatabase;
import br.com.senaijandira.mybooks.model.Livro;

public class MainActivity extends AppCompatActivity {

    //ListVew que carregará os livros
    ListView lstViewLivros;
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
        setContentView(R.layout.activity_main);

        //Criando a instancia do banco de dados
        myBooksDb = Room.databaseBuilder(getApplicationContext(),
                MyBooksDatabase.class, Utils.DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        lstViewLivros = findViewById(R.id.lstViewLivros);
        imgLivroCapa = findViewById(R.id.imgLivroCapa);

        txtDescricao = (EditText)findViewById(R.id.txtDescricao);

        txtTitulo = (EditText)findViewById(R.id.txtTitulo);


        //Criar o adapter
        adapter = new LivroAdapter(this, myBooksDb);

        lstViewLivros.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getTitle().equals("Ler")){
            startActivity(new Intent(this, LerLivroActivity.class));
        }else{
            startActivity(new Intent(this, LivrosLidosActivity.class));
        }
        return true;
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


    //STARTANDO A CLASS EDITARACTIVITY
    public void editarLivro(View v){
        startActivity(new Intent(this, EditarActivity.class));

    }

    //ALERTS
    public void alert(String titulo, String msg){

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(titulo);
        alert.setMessage(msg);

        alert.create().show();
    }

}