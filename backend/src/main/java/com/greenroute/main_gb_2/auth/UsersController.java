package com.greenroute.main_gb_2.auth;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.List; import java.util.Map;

@RestController @RequestMapping("/api/users")
public class UsersController
{
  private final JdbcTemplate jdbc;
  public UsersController(JdbcTemplate jdbc)
  {
     this.jdbc = jdbc; 
    }
  @GetMapping public List<Map<String,Object>> list()
  { 
    return jdbc.queryForList("SELECT id, username, role, address, status FROM users"); 
  }
}
