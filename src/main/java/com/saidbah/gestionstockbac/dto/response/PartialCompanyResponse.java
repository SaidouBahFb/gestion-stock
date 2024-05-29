package com.saidbah.gestionstockbac.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartialCompanyResponse {

    private Long id;

    private String name;
}
