package com.codehouse.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class WordpressPost {
    int id;
    String title;
    String content;
    String excerpt;
    int featureImage;
    String category;
    String postDate;
    String slug;
    String status;
    String tag;
}