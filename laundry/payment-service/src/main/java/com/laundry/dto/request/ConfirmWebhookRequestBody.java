package com.laundry.dto.request;

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