package com.greenroute.main_gb_2.reports;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController
@RequestMapping("/api/collections")
public class CollectionsController 
{
  private final JdbcTemplate jdbc;
  public CollectionsController(JdbcTemplate jdbc)
  {
     this.jdbc = jdbc; 
    }
  @GetMapping("/summary")
  public Map<String,Object> summary()
  {
    Map<String,Object> r = jdbc.queryForMap("SELECT COALESCE(SUM(weight_kg),0) AS total_kg, COUNT(*) AS total_collections FROM collections");
    return r;
  }
}
