package com.artSoft.bankChecks.mapper.assistant;

import com.artSoft.bankChecks.model.user.dto.response.MessageResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class Helper {
   public Map<Locale, String> trimMapValues(Map<Locale, String> originalMap) {
      if (originalMap == null) {
         return Collections.emptyMap();
      }
      return originalMap.entrySet()
              .stream()
              .collect(Collectors.toMap(
                      Map.Entry::getKey,
                      entry -> entry.getValue().trim()
              ));
   }

   public Map<String, Double> trimKeyMapValues(Map<String, Double> originalMap) {
      if (originalMap == null) {
         return null;
      }
      Map<String, Double> trimmedKeyMap = new HashMap<>();
      for (Map.Entry<String, Double> entry : originalMap.entrySet()) {
         trimmedKeyMap.put(entry.getKey().trim(), entry.getValue());
      }
      return trimmedKeyMap;
   }

   public List<String> trimListValues(List<String> originalList) {
      if (originalList == null) {
         return new ArrayList<>();
      }

      // Use a Set to ensure uniqueness
      Set<String> uniqueTrimmedSet = new HashSet<>();

      // Trim each element and add to the set to ensure uniqueness
      for (String element : originalList) {
         if (element != null) {
            uniqueTrimmedSet.add(element.trim());
         }
      }

      // Convert the set back to a list
      return new ArrayList<>(uniqueTrimmedSet);
   }

   public String trimString(String originalString) {
      return originalString == null ? null : originalString.trim();
   }

   public MessageResponse toMessageResponse(String msg){
      return MessageResponse.builder()
              .message(msg)
              .build();
   }

   public LocalDateTime getCurrentDate() {
      return LocalDateTime.now();
   }

}
