package com.codehouse.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WordPressType {
    String type;
    String jsonFilePath;
    String filterColumn;
}
