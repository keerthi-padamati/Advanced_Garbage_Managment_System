package com.greenroute.main_gb_2.bins;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.List; import java.util.Map;
@RestController @RequestMapping("/api/bins")
public class BinsController
{
  private final JdbcTemplate jdbc;
  public BinsController(JdbcTemplate jdbc)
  {
     this.jdbc = jdbc; 
  }
  @GetMapping public List<Map<String,Object>> all()
  {
     return jdbc.queryForList("SELECT * FROM bins");
  }
  @GetMapping("/user/{userId}") public List<Map<String,Object>> forUser(@PathVariable int userId)
  {
     return jdbc.queryForList("SELECT * FROM bins WHERE user_id=?", userId); 
  }
  @PutMapping("/{id}/status") public Map<String,Object> setStatus(@PathVariable int id, @RequestBody Map<String,String> body)
  {
     String s = body.getOrDefault("status","READY"); jdbc.update("UPDATE bins SET status=? WHERE id=?", s, id); return Map.of("success",true); 
  }
}
