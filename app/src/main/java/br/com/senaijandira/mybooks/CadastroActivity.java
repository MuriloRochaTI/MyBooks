package br.com.senaijandira.mybooks;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.InputStream;

import br.com.senaijandira.mybooks.db.MyBooksDatabase;
import br.com.senaijandira.mybooks.model.Livro;

public class CadastroActivity extends AppCompatActivity {

    ImageView imgLivroCapa;
    Bitmap livroCapa;

    EditText txtTitulo, txtDescricao;

    //VARIAVEL QUE NÃO MUDA O VALOR
    private final int COD_REQ_GALERIA = 101;

    private MyBooksDatabase myBooksDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        //instancia do banco de dados
        myBooksDB = Room.databaseBuilder(getApplicationContext(), MyBooksDatabase.class, Utils.DATABASE_NAME).fallbackToDestructiveMigration().allowMainThreadQueries().build();

        imgLivroCapa = findViewById(R.id.imgLivroCapa);

    }

    public void abrirGaleria(View view) {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

        //AÇÃO DE ABRIR CONTEUDO AO CLICAR NA IMAGEM
        intent.setType("image/*");

        startActivityForResult( Intent.createChooser(intent,"Selecione uma imagem"), COD_REQ_GALERIA );

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == COD_REQ_GALERIA && resultCode == Activity.RESULT_OK){

            try{
                InputStream input = getContentResolver().openInputStream(data.getData());

                //CONVERTEU PARA BITMAP
                livroCapa = BitmapFactory.decodeStream(input);

                //Exibindo na tela
                imgLivroCapa.setImageBitmap(livroCapa);


                txtTitulo = findViewById(R.id.txtTitulo);
                txtDescricao = findViewById(R.id.txtDescricao);


            }catch (Exception ex){
                ex.printStackTrace();
            }

        }
    }

    public void salvarLivro(View view) {

        String titulo = "";
        String descricao = "";

        byte[] capa;

        titulo = txtTitulo.getText().toString();

        descricao = txtDescricao.getText().toString();


        capa = Utils.toByteArray(livroCapa);
        Livro livro = new Livro(capa, titulo, descricao);

        //Inserir na variável estática da MainActivity
        /*int tamanhoArray = MainActivity.livros.length;
        MainActivity.livros = Arrays.copyOf(MainActivity.livros, tamanhoArray+1);
        MainActivity.livros[tamanhoArray] = livro;
        */

        //Inserir no banco de dados
        myBooksDB.daoLivro().inserir(livro);


    }



}
