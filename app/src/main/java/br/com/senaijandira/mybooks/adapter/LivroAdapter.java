package br.com.senaijandira.mybooks.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.senaijandira.mybooks.EditarActivity;
import br.com.senaijandira.mybooks.LerLivroActivity;
import br.com.senaijandira.mybooks.R;
import br.com.senaijandira.mybooks.Utils;
import br.com.senaijandira.mybooks.db.MyBooksDatabase;
import br.com.senaijandira.mybooks.model.Livro;

public class LivroAdapter extends ArrayAdapter<Livro> {

    //Banco de dados
    private MyBooksDatabase myBooksDb;
    Bitmap livroCapa;
    ImageView imgLivroCapa;
    EditText txtTitulo, txtDescricao;
    Livro livro;

    public LivroAdapter(Context ctx, MyBooksDatabase myBooksDb) {
        super(ctx, 0, new ArrayList<Livro>());

        this.myBooksDb = myBooksDb;
    }



    private void deletarLivro(final Livro livro) {

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Deletar");
        alert.setMessage("Tem certeza que deseja deletar?");
        alert.setNegativeButton("Não", null);

        alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Remover do banco de dados
                myBooksDb.daoLivro().deletar(livro);

                //remover livro da lista
                remove(livro);
            }
        });

        alert.show();

    }



    public void editarLivro(final Livro livro) {

        Intent intent = new Intent(getContext(), EditarActivity.class);
        intent.putExtra("Livro", livro.getId());
        getContext().startActivity(intent);
        //myBooksDb.daoLivro().atualizar(livro);


    }

    public void lerLivro(final Livro livro){

        myBooksDb.daoLivro().atualizar(livro);
        Intent intent = new Intent(getContext(), LerLivroActivity.class);
        getContext().startActivity(intent);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            v = LayoutInflater.from(getContext())
                    .inflate(R.layout.livro_layout,
                            parent, false);
        }

        final Livro livro = getItem(position);

        ImageView imgLivroCapa = v.findViewById(R.id.imgLivroCapa);
        TextView txtLivroTitulo = v.findViewById(R.id.txtLivroTitulo);
        TextView txtLivroDescricao = v.findViewById(R.id.txtLivroDescricao);

        ImageView imgDeleteLivro = v.findViewById(
                R.id.imgDeleteLivro);

        imgDeleteLivro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletarLivro(livro);
            }
        });

        ImageView imgEditar = v.findViewById(R.id.imgEditar);

        imgEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editarLivro(livro);

            }
        });

        //Setando a imagem
        imgLivroCapa.setImageBitmap(
                Utils.toBitmap(livro.getCapa()));

        //Setando o titulo do livro
        txtLivroTitulo.setText(livro.getTitulo());

        //Setando a descrição do livro
        txtLivroDescricao.setText(livro.getDescricao());

        return v;
    }

    public void alert(String titulo, String msg){

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle(titulo);
        alert.setMessage(msg);

        alert.create().show();
    }


}
