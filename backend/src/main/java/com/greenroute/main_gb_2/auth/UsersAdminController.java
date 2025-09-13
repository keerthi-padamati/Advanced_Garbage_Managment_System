package com.greenroute.main_gb_2.auth;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/users")
public class UsersAdminController
{
  private final JdbcTemplate jdbc;
  public UsersAdminController(JdbcTemplate jdbc)
  { 
    this.jdbc = jdbc; 
  }
  @GetMapping("/pending")
  public List<Map<String,Object>> pending()
  {
    return jdbc.queryForList("SELECT id,username,address FROM users WHERE status='PENDING'");
  }
  @PutMapping("/{id}/approve")
  public Map<String,Object> approve(@PathVariable int id)
  {
    jdbc.update("UPDATE users SET status='ACTIVE' WHERE id=?", id);
    return Map.of("success", true);
  }
  @PutMapping("/{id}/reject")
  public Map<String,Object> reject(@PathVariable int id)
  {
    jdbc.update("DELETE FROM users WHERE id=?", id);
    return Map.of("success", true);
  }
}
