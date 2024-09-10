package com.lavinia.retrofitcep.model.dados

import retrofit2.http.GET
import retrofit2.http.Path

interface EnderecoServiceIF {

    @GET("{cep}/json/")
    suspend fun findByCep(@Path("cep") cep: String): Endereco

}