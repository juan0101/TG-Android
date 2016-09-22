package com.buyme.alpesapp.service;

import com.buyme.alpesapp.catalog.ClienteCatalog;
import com.buyme.alpesapp.catalog.ProdutoCatalog;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by juanv on 18/08/2016.
 */
public interface ProdutoService {

    public static final String BASE_URL = "http://192.168.1.32:8080/BuyMeService/webapi/produto/";

    @GET("listar")
    Call<ProdutoCatalog> listProdutoCatalog();

    @GET("buscarProduto/{idProduto} ")
    Call<ProdutoCatalog> getOneProdutoCatalog(@Path("idProduto") int idProduto);
}
