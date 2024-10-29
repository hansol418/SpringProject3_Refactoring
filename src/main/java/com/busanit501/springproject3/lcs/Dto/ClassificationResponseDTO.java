package com.busanit501.springproject3.lcs.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Map;

@Data
public class ClassificationResponseDTO {
    @JsonProperty("predicted_class_index")
    private int predictedClassIndex;

    @JsonProperty("predicted_class_label")
    private String predictedClassLabel;


    private double confidence;

    @JsonProperty("class_confidences")
    private Map<String, Double> classConfidences;
}

