package com.quynhtd.ecommerce.dto.perfume;

import com.quynhtd.ecommerce.enums.SearchPerfume;
import lombok.Data;

@Data
public class SearchTypeRequest {
    private SearchPerfume searchType;
    private String text;
}
