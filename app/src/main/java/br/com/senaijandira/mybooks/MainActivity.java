package br.com.senaijandira.mybooks;

import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.senaijandira.mybooks.db.MyBooksDatabase;
import br.com.senaijandira.mybooks.model.Livro;

public class MainActivity extends AppCompatActivity {

    LinearLayout listaLivros;

    public static Livro[] livros;

    private MyBooksDatabase myBooksDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listaLivros = findViewById(R.id.listaLivros);

        //INSTANCIA DO BANCO DE DADOS
        myBooksDB = Room.databaseBuilder(getApplicationContext(), MyBooksDatabase.class, Utils.DATABASE_NAME).fallbackToDestructiveMigration().allowMainThreadQueries().build();

    }

    @Override
    protected void onResume() {
        super.onResume();

        //fazer oselect no banco
        livros = myBooksDB.daoLivro().selecionarTodos();

        listaLivros.removeAllViews();
        for(Livro l : livros){

            criarLivro(l, listaLivros);
        }
    }


    public void deletarLivro(final Livro livro, final View v){
        //Toast.makeText(this, livro.getTitulo(), Toast.LENGTH_SHORT).show(); comando para testar

        //MENSAGEM PARA O USÚARIO DELETAR
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Deletar");
        alert.setMessage("Tem certeza que deseja deletar?");

        alert.setNegativeButton("Não", null);

        alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //DELETAR LIVRO DO BANCO
                myBooksDB.daoLivro().deletar(livro);

                //DELETAR LIVRO DA TELA
                listaLivros.removeView(v);
            }
        });

        alert.show();

    }

    public void criarLivro(final Livro livro, ViewGroup root){

        //Carrega um XML
        final View v = LayoutInflater.from(this).inflate(R.layout.livro_layout, root, false);

        //Obtendo os ID's
        ImageView imgLivroCapa = v.findViewById(R.id.img_LivroCapa);
        TextView txtLivroTitulo = v.findViewById(R.id.txtLivroTitulo);
        TextView txtLivroDescricao = v.findViewById(R.id.txtLivroDescricao);

        //img lixeira
        ImageView imgDeleteLivro = v.findViewById(R.id.imgDeleteLivro);

        //Click da lixeira
        imgDeleteLivro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletarLivro(livro, v);

            }
        });

        //Transformando array de byte em BitMap
        imgLivroCapa.setImageBitmap( Utils.toBitmap(livro.getCapa()));

        //Setando o titulo do livro
        txtLivroTitulo.setText(livro.getTitulo());

        //Setando a descrição do livro
        txtLivroDescricao.setText(livro.getDescricao());

        //Exibindo na tela
        root.addView(v);
    }

    //CADASTRAR UM LIVRO
    public void abrirCadastro(View v){

        startActivity(new Intent(this, CadastroActivity.class));
    }

    //LER O LIVRO
    public void lerLivro(View v){
        startActivity(new Intent(this, LerLivrosActivity.class));
    }

    //ADICIONAR LIVRO AOS FAVORITOS
    public void livroFavorito(View v){

    }

}
