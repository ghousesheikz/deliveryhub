package com.shaikhomes.deliveryhub.utility;



import android.annotation.SuppressLint;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpRequestRestAPI {

   /* ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
            .tlsVersions(TlsVersion.TLS_1_2)
            .cipherSuites(
                    CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
            .build();*/

    public static class UnsafeOkHttpClient {
        static OkHttpClient getUnsafeOkHttpClient() {
            try {
                // Create a trust manager that does not validate certificate chains
                final TrustManager[] trustAllCerts = new TrustManager[] {
                        new X509TrustManager() {
                            @SuppressLint("TrustAllX509TrustManager")
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @SuppressLint("TrustAllX509TrustManager")
                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return new java.security.cert.X509Certificate[]{};
                            }
                        }
                };

                // Install the all-trusting trust manager
                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

                // Create an ssl socket factory with our all-trusting manager
                final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

                OkHttpClient.Builder builder = new OkHttpClient.Builder();

                builder.connectTimeout(100, TimeUnit.SECONDS)
                        .readTimeout(100, TimeUnit.SECONDS)
                        .writeTimeout(100, TimeUnit.SECONDS);


                builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
                builder.hostnameVerifier(new HostnameVerifier() {
                    @SuppressLint("BadHostnameVerifier")
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });

                return builder.build();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private OkHttpClient client = UnsafeOkHttpClient.getUnsafeOkHttpClient();

    JSONArray commonJsonArray(URL url) {

        JSONArray json = null;


        try {

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("cache-control", "no-cache")
                    .get()
                    .build();


            Response response = client.newCall(request).execute();


            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            else {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        json = new JSONArray(response.body().string());
                    }
                }
                response.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return json;

    }

    public JSONObject commonJsonObject(URL url) {

        JSONObject json = null;
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try {


            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            else {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        json = new JSONObject(response.body().string());
                    }
                }
                response.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;

    }

    String commonResponse(URL url) {
        String response1 = null;
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        try {


            Response response = client.newCall(request).execute();


            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            else {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        response1 = response.body().string();
                    }
                }
                response.close();
            }
        } catch (Exception e) {

            e.printStackTrace();
        }

        return response1;

    }

    JSONArray postUrlJSON(URL url, String data) {


        JSONArray json = null;


        try {


            MediaType mediaType = MediaType.parse("application/json");

            RequestBody body = RequestBody.create(mediaType, data);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .build();


            Response response = client.newCall(request).execute();


            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            else {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        json = new JSONArray(response.body().string());
                    }

                }
                response.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return json;

    }

    JSONObject postUrlCallObject(URL url, String data) {


        JSONObject json = null;


        try {


            MediaType mediaType = MediaType.parse("application/json");

            RequestBody body = RequestBody.create(mediaType, data);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .build();


            Response response = client.newCall(request).execute();


            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            else {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        json = new JSONObject(response.body().string());
                    }
                }
                response.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    String postUrlCallString(URL url, String data) {
        String response1 = null;
        try {

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, data);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .build();

            Response response = client.newCall(request).execute();


            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            else {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        response1 = response.body().string();
                    }

                }
                response.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response1;
    }
}