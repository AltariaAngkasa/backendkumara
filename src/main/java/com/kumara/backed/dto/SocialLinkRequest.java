package com.kumara.backed.dto;

public record SocialLinkRequest(
    String platform,
    String url
) {}