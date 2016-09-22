package com.buyme.alpesapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.buyme.alpesapp.catalog.ClienteCatalog;
import com.buyme.alpesapp.entity.Cliente;
import com.buyme.alpesapp.service.ClienteService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Cliente cliente = Cliente.getInstance();

    /**
     * MÉTODO QUE CRIA UM POP UP
     */
    private void showSimplePopUp() {
        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
        helpBuilder.setTitle("Alpes chocolate");
        helpBuilder.setMessage("Sucesso!");
        helpBuilder.setPositiveButton("Voltar",
                new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int which) { } });
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();
    }

    //MÉTODO INICIAL
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
    }

    /**
     * MÉTODO CHAMADO QUANDO O BOTÃO INICIAR É CLICADO
     * @param v (VIEW)
     */
    public void testarLogin(View v){

        //CAPTURANDO DADOS DA TELA
        EditText txtUsuario = (EditText) findViewById(R.id.login);
        String usuario = txtUsuario.getText().toString();
        EditText txtSenha = (EditText) findViewById(R.id.senha);
        String senha = txtSenha.getText().toString();

        //CRIANDO UM OBJETO RETROFIT
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ClienteService.BASE_URL) //PASSANDO A URL BASE
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ClienteService service = retrofit.create(ClienteService.class);
        Call<ClienteCatalog> clienteCatalog = service.getOneClienteCatalog(usuario,senha);

        //FUNÇÃO DE CALLBACK DE ACESSO AO WEB SERVICE
        clienteCatalog.enqueue(new Callback<ClienteCatalog>() {
            @Override
            public void onResponse(Call<ClienteCatalog> call, Response<ClienteCatalog> response) {
                if(!response.isSuccessful()){
                    Log.e("JUAN","ERRO no response");
                }else{
                    //RESPOSTA DO WEB SERVICE
                    ClienteCatalog cliCatalog = response.body();
                    Cliente clienteResposta = cliCatalog.cliente;
                    if(clienteResposta != null){
                        //SETANDO OS DADOS RECEBIDOS
                        cliente.setCidade(clienteResposta.getCidade());
                        cliente.setEmail(clienteResposta.getEmail());
                        cliente.setEndereco(clienteResposta.getEndereco());
                        cliente.setId(clienteResposta.getId());
                        cliente.setLogin(clienteResposta.getLogin());
                        cliente.setNome(clienteResposta.getNome());
                        cliente.setNumero(clienteResposta.getNumero());
                        cliente.setSenha(clienteResposta.getSenha());
                        cliente.setTelefone(clienteResposta.getTelefone());

                        Log.e("JUAN INFORMAÇÕES",String.format("%s : %s",clienteResposta.getNome(),clienteResposta.getEmail()));
                        mudarTela(); //MÉTODO PARA MUDAR A TELA
                    }
                    showSimplePopUp(); //POPUP DIZENDO QUE RECEBEU
                    //for(Cliente c: cliCatalog.cliente){ }
                }
            }

            @Override
            public void onFailure(Call<ClienteCatalog> call, Throwable t) {
                String message = t.getMessage();
                Log.e("JUAN", message);
                Log.e("JUAN","ERRO");
            }
        });
    }

    /**
     * MÉTODO PARA MUDAR DE TELA
     */
    public void mudarTela(){
        Intent intent = new Intent(this, ProdutosActivity.class); //INDICANDO PARA QUAL TELA DEVE IR
        //intent.putExtra("nome", nome);
        //intent.putExtra("data", data);
        startActivity(intent); //INICIAR NOVA TELA
    }

}
