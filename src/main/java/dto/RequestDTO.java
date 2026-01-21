package dto;

import java.util.Map;

public class RequestDTO {
   public String method;
   public Map<String, Integer> querystring;
   public Map<String, Object> body;
}
