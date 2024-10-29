package com.busanit501.springproject3.lcs.Controller;

import com.busanit501.springproject3.hjt.domain.HjtEntity;
import com.busanit501.springproject3.hjt.service.HjtService;
import com.busanit501.springproject3.lcs.Dto.ClassificationResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Log4j2
@RestController
public class ImageClassifyController {

    private final HjtService hjtService;

    public ImageClassifyController(HjtService hjtService) {
        this.hjtService = hjtService;
    }

    @PostMapping("/classify")
    public ResponseEntity<Map<String, String>> classifyImage(@RequestParam("image") MultipartFile image) {
        Map<String, String> result = new HashMap<>();

        if (image.isEmpty()) {
            result.put("error", "No file was submitted.");
            return ResponseEntity.badRequest().body(result);
        }

        String apiUrl = "http://localhost:8000/api/classify/";

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + image.getOriginalFilename());
            image.transferTo(convFile);

            HttpPost uploadFile = new HttpPost(apiUrl);

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addBinaryBody("image", convFile);

            HttpEntity multipart = builder.build();
            uploadFile.setEntity(multipart);

            HttpResponse response = httpClient.execute(uploadFile);
            HttpEntity responseEntity = response.getEntity();

            log.info("API 연결확인:" + responseEntity);

            String apiResult = EntityUtils.toString(responseEntity, "UTF-8");

            log.info("API 결과: " + apiResult);

            ClassificationResponseDTO classificationResponseDTO = extractClassificationResponseDTO(apiResult);
            if (classificationResponseDTO == null) {
                log.error("classificationResponseDTO가 null입니다. JSON 파싱에 실패했습니다.");
                result.put("error", "DTO 변환에 실패했습니다.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
            }

            log.info("DTO 내용: " + classificationResponseDTO);

            String predictedLabel = classificationResponseDTO.getPredictedClassLabel();
            if (predictedLabel == null) {
                log.error("Predicted Label이 null입니다. JSON 응답에 문제가 있을 수 있습니다.");
                result.put("error", "Predicted Label이 null입니다.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
            }

            Optional<HjtEntity> hjtEntityOpt = hjtService.findByToolName(predictedLabel);
            if (hjtEntityOpt.isPresent()) {
                String description = hjtEntityOpt.get().getDescription();
                result.put("description", description); // description 추가
                log.info("description 내용" + description);
            } else {
                log.warn("Tool description not found for predicted label: " + predictedLabel);
                result.put("error", "Tool description을 찾을 수 없습니다.");
            }

            log.info("Predicted Label: " + predictedLabel);

            String videoUrl = getVideoUrl(predictedLabel);

            result.put("predictedLabel", predictedLabel);
            result.put("videoUrl", videoUrl);

            if (!convFile.delete()) {
                System.err.println("Failed to delete the temporary file.");
            }

            log.info("응답 데이터: " + result);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            result.put("error", "File processing error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    private ClassificationResponseDTO extractClassificationResponseDTO(String apiResult) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(apiResult, ClassificationResponseDTO.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getVideoUrl(String predictedLabel) {
        switch (predictedLabel) {
            case "공구톱":
                return "https://www.youtube.com/embed/_iSGaOuexDs?si=7wN_9BMI_eDETmyj";
            case "공업용가위":
                return "https://www.youtube.com/embed/PUFCz9fv8as?si=qSxYTFiOyh60KnRE";
            case "그라인더":
                return "https://www.youtube.com/embed/u7D3_diB6RI?si=YvtKJ4npz7_FQSKk";
            case "니퍼":
                return "https://www.youtube.com/embed/Wq6fWnCWnpc?si=WB0jY9r-BTyJjl0m";
            case "드라이버":
                return "https://www.youtube.com/embed/UwPJTL35NlY?si=EKp1PMX_6eJeGAuf";
            case "망치":
                return "https://www.youtube.com/embed/lwJSNgASajs?si=qeWLHMbaDy-Kyjeb";
            case "스패너":
                return "https://www.youtube.com/embed/-OfQXPrZEw4?si=-y7wmTuadJIHbioW";
            case "전동드릴":
                return "https://www.youtube.com/embed/-qTMDoYMSqc?si=HJCZXUK_SNcQRoTr";
            case "줄자":
                return "https://www.youtube.com/embed/PZOod8DW9L8?si=oe-34fwObXULIJfX";
            case "버니어 캘리퍼스":
                return "https://www.youtube.com/embed/iTuMsyMcu9c?si=FvAgxpIU3x7RBInL";
            default:
                return "https://www.youtube.com/embed/i4xyVkhLcL8?si=C4BwODmGcG5YKXr2";
        }
    }
}
