package com.busanit501.springproject3.lhs.service;



import com.busanit501.springproject3.lhs.dto.PredictionResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Log4j2
public class ImageUploadService {

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PredictionResponseDTO sendImageToDjangoServer(byte[] imageBytes, String filename) throws IOException {

        RequestBody fileBody = RequestBody.create(imageBytes, MediaType.parse("image/jpeg"));

        MultipartBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", filename, fileBody)
                .build();


        Request request = new Request.Builder()
                .url("http://localhost:8000/api/classify/")
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String responseBody = response.body().string();
            log.info("responseBody : " + responseBody);

            return objectMapper.readValue(responseBody, PredictionResponseDTO.class);
        }
    }


}
