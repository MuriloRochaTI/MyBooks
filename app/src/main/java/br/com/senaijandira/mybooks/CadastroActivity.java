package br.com.senaijandira.mybooks;

import android.app.Activity;
import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
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

    Bitmap livroCapa;
    ImageView imgLivroCapa;
    EditText txtTitulo, txtDescricao;

    private final int COD_REQ_GALERIA = 101;

    private MyBooksDatabase myBooksDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        //Criando a instancia do banco de dados
        myBooksDb = Room.databaseBuilder(getApplicationContext(),
                MyBooksDatabase.class, Utils.DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        imgLivroCapa = findViewById(R.id.imgLivroCapa);
        txtTitulo = findViewById(R.id.txtTitulo);
        txtDescricao = findViewById(R.id.txtDescricao);
    }

    public void abrirGaleria(View view) {

        Intent intent =
                new Intent(Intent.ACTION_GET_CONTENT);

        intent.setType("image/*");

        startActivityForResult(
                Intent.createChooser(intent,
                        "Selecione uma imagem"),
                COD_REQ_GALERIA
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == COD_REQ_GALERIA
                && resultCode == Activity.RESULT_OK){

            try{

                InputStream input =
                        getContentResolver()
                                .openInputStream(data.getData());

                //Converteu para bitmap
                livroCapa = BitmapFactory.decodeStream(input);

                //Exibindo na tela
                imgLivroCapa.setImageBitmap(livroCapa);

            }catch (Exception ex){
                ex.printStackTrace();
            }


        }

    }



    public void salvarLivro(View view) {

        Livro livro;
        String titulo = txtTitulo.getText().toString();
        String descricao = txtDescricao.getText().toString();

        if(livroCapa == null || titulo.equals("") || descricao.equals("")){
            alert("ERRO!", "Preencha todos os campos");

        }else{

            byte[] capa = Utils.toByteArray(livroCapa);

            titulo = txtTitulo.getText().toString();

            descricao = txtDescricao.getText().toString();

            livro = new Livro(0, capa, titulo, descricao,0);

            //Inserir no banco de dados
            myBooksDb.daoLivro().inserir(livro);

            alert("Cadastrado!", "Livro cadastrado com sucesso!");


        }



    }

    public void alert(String titulo, String msg){

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(titulo);
        alert.setMessage(msg);
        alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alert.setCancelable(false);
        alert.create().show();
    }
}