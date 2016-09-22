package com.buyme.alpesapp.service;

import com.buyme.alpesapp.catalog.EncomendaCatalog;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by juanv on 22/09/2016.
 */
public interface EncomendaService {

    public static final String BASE_URL = "http://192.168.1.32:8080/BuyMeService/webapi/encomenda/";

    @POST("encomendar/{clienteId}/{codigoProduto}/{quantidade}")
    Call<EncomendaCatalog> gerarEncomenda(@Path("clienteId") int clienteId, @Path("codigoProduto") String codigoProduto, @Path("quantidade") int quantidade);

}
