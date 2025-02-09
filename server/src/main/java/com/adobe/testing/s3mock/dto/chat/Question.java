package com.adobe.testing.s3mock.dto.chat;

public class Question {
  private String question;

  public Question(String question) {
    this.question = question;
  }

  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }
}
