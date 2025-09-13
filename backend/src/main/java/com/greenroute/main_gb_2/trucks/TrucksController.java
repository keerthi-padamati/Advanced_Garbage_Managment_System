package com.greenroute.main_gb_2.trucks;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.List; import java.util.Map;
@RestController @RequestMapping("/api/trucks")
public class TrucksController {
  private final JdbcTemplate jdbc;
  public TrucksController(JdbcTemplate jdbc)
  { 
    this.jdbc = jdbc; 
  }
  @GetMapping public List<Map<String,Object>> all()
  { 
    return jdbc.queryForList("SELECT * FROM trucks"); 
  }
  @PutMapping("/{id}/location") public Map<String,Object> setLocation(@PathVariable int id, @RequestBody Map<String,Double> body){ jdbc.update("UPDATE trucks SET latitude=?, longitude=? WHERE id=?", body.get("latitude"), body.get("longitude"), id); return Map.of("success",true); 
}
}
