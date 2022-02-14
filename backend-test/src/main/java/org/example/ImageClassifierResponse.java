package org.example;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageClassifierResponse {

    //    @JsonProperty("status")
    private String status;
    //    @JsonProperty("category")
    private String category;
    //    @JsonProperty("probability")
    private Float probability;


}