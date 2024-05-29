package com.saidbah.gestionstockbac.dto.request;

import com.saidbah.gestionstockbac.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {

    private String code;

    private String designation;

    private Long companyId;

    private Long userId;
}
