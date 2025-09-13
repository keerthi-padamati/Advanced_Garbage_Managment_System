package com.greenroute.main_gb_2.auth;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;


@RestController @RequestMapping("/api/auth")
public class AuthController {
  private final JdbcAuthService authService;
  public AuthController(JdbcAuthService authService)
  { 
    this.authService = authService; 
  }


  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody Map<String,String> body)
  {
    String u = body.get("username"), p = body.get("password"), a = body.get("address");
    if(u==null || p==null)
    return ResponseEntity.badRequest().body(Map.of("success",false,"message","username/password required"));
    boolean created = authService.registerUser(u,p,a);
    if(!created)
    return ResponseEntity.badRequest().body(Map.of("success",false,"message","username already exists"));
    return ResponseEntity.ok(Map.of("success",true,"message","Registered"));
  }

  
  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody Map<String,String> body)
  {
    var user = authService.authenticate(body.get("username"), body.get("password"));
    if(user==null) return ResponseEntity.status(401).body(Map.of("success",false,"message","Invalid credentials"));
    return ResponseEntity.ok(Map.of("success",true,"id",user.get("id"),"username",user.get("username"),"role",user.get("role")));
  }
}
