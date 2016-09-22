package com.buyme.alpesapp.service;

import com.buyme.alpesapp.catalog.ClienteCatalog;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by juanv on 08/08/2016.
 */
public interface ClienteService {

    public static final String BASE_URL = "http://192.168.1.32:8080/BuyMeService/webapi/cliente/";

    @GET("listar")
    Call<ClienteCatalog> listClienteCatalog();

    @GET("buscarCliente/{clienteLogin}/{clienteSenha}  ")
    Call<ClienteCatalog> getOneClienteCatalog(@Path("clienteLogin") String clienteLogin, @Path("clienteSenha") String clienteSenha);

}
