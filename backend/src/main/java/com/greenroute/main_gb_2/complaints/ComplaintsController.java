package com.greenroute.main_gb_2.complaints;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.List; import java.util.Map;
@RestController @RequestMapping("/api/complaints")
public class ComplaintsController
{
  private final JdbcTemplate jdbc;
  public ComplaintsController(JdbcTemplate jdbc)
  {
     this.jdbc = jdbc; 
  }
  @PostMapping public Map<String,Object> create(@RequestBody Map<String,Object> body)
  { 
    Integer u = (Integer) body.get("userId"); String m = (String) body.get("message"); jdbc.update("INSERT INTO complaints(user_id,message) VALUES(?,?)", u, m); 
    return Map.of("success",true); 
  }
  @GetMapping public List<Map<String,Object>> list()
  { 
    return jdbc.queryForList("SELECT c.*, u.username FROM complaints c JOIN users u ON u.id=c.user_id ORDER BY c.created_at DESC"); 
  }
}
