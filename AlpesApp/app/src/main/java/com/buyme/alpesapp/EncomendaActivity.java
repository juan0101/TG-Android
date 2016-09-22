package com.buyme.alpesapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.buyme.alpesapp.catalog.EncomendaCatalog;
import com.buyme.alpesapp.entity.Cliente;
import com.buyme.alpesapp.entity.Encomenda;
import com.buyme.alpesapp.entity.Produto;
import com.buyme.alpesapp.service.EncomendaService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by juanv on 21/09/2016.
 */
public class EncomendaActivity extends AppCompatActivity{

    private Cliente cliente = Cliente.getInstance();
    private Produto produto = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.encomenda);

        Intent intent = getIntent();
        produto = (Produto) intent.getSerializableExtra("produto");

        alimentarTela();
    }

    /**
     * MÉTODO QUE ALIMENTA OS CAMPOS COM AS INFORMAÇÕES DO PRODUTO E CLIENTE
     */
    private void alimentarTela() {

        //capturando os campos de cliente,produto e preço
        TextView txtViewNomeCliente = (TextView) findViewById(R.id.cliente);
        TextView txtViewNomeProduto = (TextView) findViewById(R.id.nomeProduto);
        TextView txtViewPrecoProduto = (TextView) findViewById(R.id.precoProduto);

        //setando os valores nos campos
        txtViewNomeCliente.setText(cliente.getLogin());
        txtViewNomeProduto.setText(produto.getNome());
        txtViewPrecoProduto.setText(produto.getValor()+"");

    }

    /**
     * MÉTODO CHAMADO QUANDO O BOTÃO VOLTAR É CLICADO
     * @param v
     */
    public void voltar(View v){
        Intent intent = new Intent(this, ProdutosActivity.class); //INDICANDO PARA QUAL TELA DEVE IR
        startActivity(intent); //INICIAR NOVA TELA
    }

    /**
     * MÉTODO CHAMADO QUANDO O BOTÃO ENCOMENDAR É CLICADO
     * @param v
     */
    public void encomendar(View v){

        //capturando o campo de quantidade da tela
        EditText txtQuantidade = (EditText) findViewById(R.id.txtQuantidade);
        //passando o valor digitado para uma variavel inteira
        int quantidade = Integer.parseInt(txtQuantidade.getText().toString());

        fazerEncomenda(cliente.getId(),produto.getCodigo(),quantidade);
    }

    private void fazerEncomenda(int idCliente, String codigoProduto, int quantidade) {

        //CRIANDO UM OBJETO RETROFIT
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(EncomendaService.BASE_URL) //PASSANDO A URL BASE
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EncomendaService service = retrofit.create(EncomendaService.class);
        Call<EncomendaCatalog> encomendaCatalog = service.gerarEncomenda(idCliente,codigoProduto,quantidade);

        encomendaCatalog.enqueue(new Callback<EncomendaCatalog>() {
            @Override
            public void onResponse(Call<EncomendaCatalog> call, Response<EncomendaCatalog> response) {
                if(!response.isSuccessful()){
                    Log.e("ERRO:","ERRO NO RESPONSE");
                }else{
                    EncomendaCatalog encCatResp = response.body();
                    Encomenda encResposta = encCatResp.encomenda;
                    if(encResposta != null){
                        showSimplePopUp(true);
                        mudarTela();
                    }else {
                        showSimplePopUp(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<EncomendaCatalog> call, Throwable t) {
                Log.e("ERRO:","FALHA!!");
            }
        });

    }

    /**
     * MÉTODO QUE VOLTA PARA A TELA DE ESCOLHA DE PRODUTOS
     */
    private void mudarTela() {
        Intent intent = new Intent(this, ProdutosActivity.class); //INDICANDO PARA QUAL TELA DEVE IR
        startActivity(intent); //INICIAR NOVA TELA
    }


    /**
     * MÉTODO QUE CRIA UM POP UP
     */
    private void showSimplePopUp(boolean funcionou) {
        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
        helpBuilder.setTitle("Alpes chocolate");
        if(funcionou){
            helpBuilder.setMessage("Encomenda feita com sucesso!");
        }else{
            helpBuilder.setMessage("Desculpe, houve um erro! tente novamente.");
        }
        helpBuilder.setPositiveButton("Voltar",
                new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int which) { } });
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();
    }

}
