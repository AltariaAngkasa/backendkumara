package com.kumara.backed.dto;

import java.math.BigDecimal;

public record ReportItemDTO(
    String productName,
    Long totalSold,
    BigDecimal totalRevenue
) {}