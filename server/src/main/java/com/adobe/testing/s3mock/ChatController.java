package com.adobe.testing.s3mock;

import com.adobe.testing.s3mock.dto.chat.Question;
import com.adobe.testing.s3mock.dto.chat.Response;
import com.adobe.testing.s3mock.util.HttpVisitModelUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * receive chat request, and invoke AI model with prompt
 */
@RestController
@CrossOrigin(origins = "*", exposedHeaders = "*")
@RequestMapping("")
public class ChatController {

  @GetMapping("/chat")
  public ResponseEntity<String> AIBucketChat(@RequestParam String question) throws JsonProcessingException {

    // 1. get bucket content
    Response bucketResponse = HttpVisitModelUtil.getBucketContent();
    // 2. content + question for chat
    String chatQuestion = buildQuestion(bucketResponse, question);
    Response answerResponse = HttpVisitModelUtil.chatBucket(chatQuestion);

    if (answerResponse.isSuccess()) {
//      ObjectMapper objectMapper = new ObjectMapper();
//      ChatResponse chatResponse = objectMapper.convertValue(answerResponse.getContent(), ChatResponse.class);
      return ResponseEntity.ok(answerResponse.getContent());
    }
     return ResponseEntity.ok("I am thinking" + question);
  }

  private String objectToJsonString(Object object) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
    return mapper.writeValueAsString(object);
  }


  private String buildQuestion(Response bucketResponse, String question) {
    if (bucketResponse.isSuccess()) {
      return "Based on the content" + bucketResponse.getContent()  + "Answer question: " + question;
    }
    return question;
  }

  @PostMapping("/chatTo")
  public ResponseEntity<String> handleRequest(@RequestBody Question question) {
    String askContent = question.getQuestion();
    return ResponseEntity.ok(askContent);
  }

}
