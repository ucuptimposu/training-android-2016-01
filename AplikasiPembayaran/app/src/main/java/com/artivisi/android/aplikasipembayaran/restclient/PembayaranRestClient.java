package com.artivisi.android.aplikasipembayaran.restclient;

import android.util.Log;

import com.artivisi.android.aplikasipembayaran.dto.GenericResponse;
import com.artivisi.android.aplikasipembayaran.dto.PageProduk;
import com.artivisi.android.aplikasipembayaran.dto.Produk;
import com.artivisi.android.aplikasipembayaran.exception.GagalLoginException;

import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by endymuhardin on 4/6/16.
 */
public class PembayaranRestClient {

    private String serverUrl = "http://192.168.100.5:8080";
    private RestTemplate restTemplate;

    public PembayaranRestClient(String url) {
        this.serverUrl = url;
        restTemplate = new RestTemplate();
        ((SimpleClientHttpRequestFactory)restTemplate.getRequestFactory())
                .setConnectTimeout(3 * 1000);
    }

    public GenericResponse login(String username, String password) throws GagalLoginException {
        String url = serverUrl + "/api/login";

        HashMap<String, String> requestData = new HashMap<>();
        requestData.put("username", username);
        requestData.put("password", password);

        try {
            return restTemplate.postForObject(url, requestData, GenericResponse.class);
        } catch (Exception err){
            throw new GagalLoginException("Server tidak bisa dihubungi");
        }
    }

    public void updateToken(String username, String token) throws GagalLoginException {
        String url = serverUrl + "/api/user/" + username + "/handphone";

        HashMap<String, String> requestData = new HashMap<>();
        requestData.put("gcm_token", token);

        try {
            restTemplate.put(url, requestData);
        } catch (Exception err){
            throw new GagalLoginException("Server tidak bisa dihubungi");
        }
    }

    public PageProduk getSemuaProduk() throws GagalLoginException {
        String url = serverUrl + "/api/produk/";

        try {
            ResponseEntity<PageProduk> p =  restTemplate.getForEntity(url, PageProduk.class, new Object[]{});
            return p.getBody();
        } catch (Exception err){
            Log.e("rest ", err.getMessage());
            throw new GagalLoginException("Server : " + url + " tidak bisa dihubungi");
        }
    }

}
