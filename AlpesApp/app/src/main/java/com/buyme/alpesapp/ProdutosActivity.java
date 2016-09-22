package com.buyme.alpesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.buyme.alpesapp.catalog.ProdutoCatalog;
import com.buyme.alpesapp.entity.Cliente;
import com.buyme.alpesapp.entity.Encomenda;
import com.buyme.alpesapp.entity.Produto;
import com.buyme.alpesapp.service.ProdutoService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by juanv on 05/08/2016.
 */
public class ProdutosActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private Cliente cliente = Cliente.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.produtos);

        Log.e("LOGIN:",String.format("%s:%s",cliente.getNome(),cliente.getLogin()));
        getNomesProdutos(); //ACESSA O WEB SERVICE E PREENCHE A LISTA DE NOMES DE PRODUTOS
    }

    /**
     * Método que alimenta a lista de produtos
     */
    public void alimentarLista(List<String> nomes, final List<Produto> produtos){
        final ListView listaProdutos = (ListView) findViewById(R.id.listaProduto);
        //SETANDO A LISTA DE NOMES NO ADAPTER
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(ProdutosActivity.this, android.R.layout.simple_list_item_1, nomes);
        //SETANDO O ADAPTER NA LISTA (VIEW)
        listaProdutos.setAdapter(adapter);
        //MÉTODO ACIONADO QUANDO UM ITEM DA LISTA É CLICADO
        listaProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> listaProdutos, View view, int position, long id) {
                String nomeEscolhido = adapter.getItem(position);
                for(Produto p: produtos){
                    if(p.getNome().equals(nomeEscolhido)){
                        mudarTela(p);
                    }
                }
                Log.e("JUAN INFORMAÇÕES",String.format("%s : %s",nomeEscolhido,nomeEscolhido));
            }

        });
    }

    /**
     * MÉTODO QUE MUDA PARA A PROXIMA TELA
     * ENVIANDO O PRODUTO ESCOLHIDO PARA ENCOMENDA
     * @param p
     */
    private void mudarTela(Produto p) {
        Intent intent = new Intent(this, EncomendaActivity.class); //INDICANDO PARA QUAL TELA DEVE IR
        intent.putExtra("produto", p);
        startActivity(intent); //INICIAR NOVA TELA
    }

    /**
     * Método que acessa o web service e os produtos
     * Preenche a lista de nomes dos produtos
     */
    public void getNomesProdutos(){

        //CRIANDO UM OBJETO RETROFIT COM A URL DE CONEXAO BASE
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ProdutoService.BASE_URL) //SETANDO URL DE CONEXAO BASE
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ProdutoService service = retrofit.create(ProdutoService.class); //INDICANDO O SERVIÇO
        Call<ProdutoCatalog> produtoCatalog = service.listProdutoCatalog(); //INDICANDO A URL DO GET

        //CALLBACK DE RETORNO DO WEB SERVICE
        produtoCatalog.enqueue(new Callback<ProdutoCatalog>() {
            @Override
            public void onResponse(Call<ProdutoCatalog> call, Response<ProdutoCatalog> response) {
                if(!response.isSuccessful()){ //CASO HAJA UM ERRO GERA UM LOG
                    Log.e("JUAN","ERRO no response");
                }else{ // EM CASO DE SUCESSO
                    //ARMAZENA OS NOMES VINDOS DO WEBSERVICE
                    List<String> nomeProdutos = new ArrayList<String>();
                    ProdutoCatalog prodCatalog = response.body(); //CAPTURANDO OS PRODUTOS VINDO DO GSON
                    for(Produto p: prodCatalog.produtos){ //PERCORRENDO A LISTA DE PRODUTOS
                        Log.e("JUAN",String.format("%s",p.getNome()));
                        nomeProdutos.add(p.getNome()); //ADICIONANDO OS NOMES DOS PRODUTOS NA LISTA DE NOMES
                    }
                    alimentarLista(nomeProdutos,prodCatalog.produtos);
                }
            }
            @Override
            public void onFailure(Call<ProdutoCatalog> call, Throwable t) {
                Log.e("JUAN","ERRO");
            }
        });

    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) { }

}
