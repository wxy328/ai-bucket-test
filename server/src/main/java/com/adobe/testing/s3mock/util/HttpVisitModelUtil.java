package com.adobe.testing.s3mock.util;

import com.adobe.testing.s3mock.dto.chat.ChatMessage;
import com.adobe.testing.s3mock.dto.chat.ChatRequest;
import com.adobe.testing.s3mock.dto.chat.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HttpVisitModelUtil {

  private static String URI_CHAT_OLLAMA = "http://localhost:11434/api/chat";
  private static String MODEL_DEFAULT = "llama3:8b";

  private static String URI_GET_BUCKET = "http://localhost:9090/ai_bucket/my-test-bucket/my-file";


  // get content from s3mock
  public static Response getBucketContent() {
    return doGet(URI_GET_BUCKET);
  }

  // chat with llama3:8b
  public static Response chatBucket(String question) throws JsonProcessingException {
    ChatRequest chatRequest = buildChatRequest(question);

    ObjectMapper mapper = new ObjectMapper();
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
    String json = mapper.writeValueAsString(chatRequest);

    return doPost(URI_CHAT_OLLAMA, json);
  }


  public static Response doGet(String urlStr) {
    Response response = new Response();
    try {
      URL url = new URL(urlStr);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      conn.connect();
      int responseCode = conn.getResponseCode();
      if (responseCode == 200) {
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder stringBuffer = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
          stringBuffer.append(inputLine);
        }
        in.close();
        response.setContent(stringBuffer.toString());
        response.setSuccess(true);
      } else {
        response.setSuccess(false);
        response.setErrorMessage("Failed to send GET request" + urlStr);
      }
    } catch (Exception e) {
      response.setSuccess(false);
      response.setErrorMessage(e.getMessage());
    }
    return response;
  }

  public static Response doPost(String urlStr, String json) {
    Response response = new Response();

    try {
      URL url = new URL(urlStr);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setDoOutput(true);

      DataOutputStream out = new DataOutputStream(conn.getOutputStream());
      out.writeBytes(json);
      out.flush();
      out.close();

      int responseCode = conn.getResponseCode();
      if (responseCode == 200) {
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder stringBuffer = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
          stringBuffer.append(inputLine);
        }
        in.close();

        response.setContent(stringBuffer.toString());
        response.setSuccess(true);
      } else {
        response.setSuccess(false);
        response.setErrorMessage("Failed to send POST request" + urlStr);
      }
    } catch (Exception e) {
      response.setSuccess(false);
      response.setErrorMessage(e.getMessage());
    }
    return response;
  }


  public static ChatRequest buildChatRequest(String question) {
    ChatRequest request = new ChatRequest();

    request.setModel(MODEL_DEFAULT);
    request.setMessages(buildSingleChatMessage(question));

    return request;

  }



  public static List<ChatMessage> buildSingleChatMessage(String content) {
    List<ChatMessage> list = new ArrayList<>();

    list.add(buildChatMessage("user", content));

    return list;
  }

  public static ChatMessage buildChatMessage(String role, String content) {
    ChatMessage chatMessage = new ChatMessage();

    chatMessage.setRole(role);
    chatMessage.setContent(content);

    return chatMessage;
  }

}
