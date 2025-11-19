 package com.greenroute.main_gb_2.auth;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class JdbcAuthService
{
  private final JdbcTemplate jdbc;
  public JdbcAuthService(JdbcTemplate jdbc)
  {
     this.jdbc = jdbc; 
  }
  public boolean registerUser(String username, String password, String address)
  {
    Integer count = jdbc.queryForObject("SELECT COUNT(*) FROM users WHERE username = ?", Integer.class, username);
    if(count != null && count > 0) 
    return false;
    jdbc.update("INSERT INTO users(username,password,role,address,status) VALUES (?,?,?,?,?)", username, password, "USER", address, "ACTIVE");
    return true;
  }
  public Map<String,Object> authenticate(String username, String password)
  {
    try
    { 
      return jdbc.queryForMap("SELECT id, username, role FROM users WHERE username = ? AND password = <REDACTED>", username, password); 
    }
    catch(EmptyResultDataAccessException e)
    { 
      return null; 
    }
  }
}
