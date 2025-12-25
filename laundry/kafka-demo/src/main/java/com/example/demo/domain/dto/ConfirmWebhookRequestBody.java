package com.example.demo.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ConfirmWebhookRequestBody {
  private String webhookUrl;

  public ConfirmWebhookRequestBody(String webhookUrl) {
    this.webhookUrl = webhookUrl;
  }
}