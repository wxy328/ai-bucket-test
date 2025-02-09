package com.adobe.testing.s3mock.dto.chat;

import java.util.List;

public class ChatRequest {
  private String model;
  private List<ChatMessage> messages;
  private boolean stream = false;

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public List<ChatMessage> getMessages() {
    return messages;
  }

  public void setMessages(List<ChatMessage> messages) {
    this.messages = messages;
  }

  public boolean isStream() {
    return stream;
  }

  public void setStream(boolean stream) {
    this.stream = stream;
  }
}
