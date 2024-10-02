package com.codehouse.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SiteInfo {
    private String siteUrl;
    private String folderName;
    private String defaultCategory;
}

