package com.greenroute.main_gb_2.trucks;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.*;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/routes")
public class RoutesController 
{
  private final JdbcTemplate jdbc;
  public RoutesController(JdbcTemplate jdbc)
  { 
    this.jdbc = jdbc; 
  }
  @PostMapping("/assign")
  public Map<String,Object> assign(@RequestBody Map<String,Object> body)
  {
    Object truckIdObj = body==null?null:body.get("truck_id");
    boolean auto = body!=null && (body.get("auto")!=null && Boolean.parseBoolean(body.get("auto").toString()));
    Integer truckId = truckIdObj==null?null:Integer.parseInt(truckIdObj.toString());
    List<Map<String,Object>> bins = jdbc.queryForList("SELECT * FROM bins WHERE status='READY' AND (route_id IS NULL OR route_id=0) LIMIT 50");
    if(bins.isEmpty()) return Map.of("success", false, "message", "No READY bins");
    if(truckId==null && auto){
      Map<String,Object> first = bins.get(0);
      double bx = ((Number)first.get("latitude")).doubleValue();
      double by = ((Number)first.get("longitude")).doubleValue();
      List<Map<String,Object>> trucks = jdbc.queryForList("SELECT * FROM trucks WHERE latitude IS NOT NULL AND longitude IS NOT NULL");
      if(trucks.isEmpty()) return Map.of("success", false, "message", "No trucks with location available");
      double best=Double.MAX_VALUE; Integer chosen=null;
      for(Map<String,Object> t: trucks){
        double tx = ((Number)t.get("latitude")).doubleValue();
        double ty = ((Number)t.get("longitude")).doubleValue();
        double d = Math.hypot(tx-bx, ty-by);
        if(d<best){ best=d; chosen = ((Number)t.get("id")).intValue(); 
      }
      }
      truckId = chosen;
    }
    if(truckId==null) 
    return Map.of("success", false, "message", "truck_id missing and auto not requested");
    jdbc.update("INSERT INTO routes (truck_id, status, route_name) VALUES (?,?,?)", truckId, "ASSIGNED", "Route for truck "+truckId);
    Long routeId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
    Map<String,Object> truck = jdbc.queryForMap("SELECT latitude, longitude FROM trucks WHERE id=?", truckId);
    double tx = 0, ty = 0;
    if(truck!=null && truck.get("latitude")!=null){ tx = ((Number)truck.get("latitude")).doubleValue(); ty = ((Number)truck.get("longitude")).doubleValue(); 
  }
    List<Map<String,Object>> remaining = new ArrayList<>(bins);
    List<Map<String,Object>> ordered = new ArrayList<>();
    while(!remaining.isEmpty()){
      Map<String,Object> nearest=null; double bestd=Double.MAX_VALUE;
      for(Map<String,Object> b: remaining){
        double bx2 = ((Number)b.get("latitude")).doubleValue(), by2 = ((Number)b.get("longitude")).doubleValue();
        double d = Math.hypot(tx-bx2, ty-by2);
        if(d<bestd){ 
          bestd=d; nearest=b; 
        }
      }
      ordered.add(nearest); remaining.remove(nearest);
      tx = ((Number)nearest.get("latitude")).doubleValue(); ty = ((Number)nearest.get("longitude")).doubleValue();
    }
    int order=1;
    for(Map<String,Object> b: ordered){
      jdbc.update("INSERT INTO route_stops (route_id, bin_id, stop_order, visited) VALUES (?,?,?,?)", routeId, ((Number)b.get("id")).intValue(), order++, false);
      jdbc.update("UPDATE bins SET status='ALLOCATED', route_id=? WHERE id=?", routeId, ((Number)b.get("id")).intValue());
    }
    try{
      String coords = ordered.stream().map(b -> b.get("longitude")+","+b.get("latitude")).collect(Collectors.joining(";"));
      String url = "https:
      RestTemplate rt = new RestTemplate();
      Map resp = rt.getForObject(url, Map.class);
      if(resp!=null && resp.get("routes")!=null){
        Map route = ((List<Map>)resp.get("routes")).get(0);
        Map geometry = (Map)route.get("geometry");
        jdbc.update("UPDATE routes SET polyline=? WHERE id=?", geometry.toString(), routeId);
      }
    }catch(Exception ex){ System.out.println("Routing fetch failed: "+ex.getMessage()); 
  }
    return Map.of("success", true, "route_id", routeId);
  }
  @GetMapping
  public List<Map<String,Object>> list(@RequestParam(required=false) Integer truckId){
    if(truckId!=null){
      List<Map<String,Object>> routes = jdbc.queryForList("SELECT * FROM routes WHERE truck_id=?", truckId);
      for(Map<String,Object> r: routes){
        List<Map<String,Object>> stops = jdbc.queryForList("SELECT rs.*, b.latitude, b.longitude FROM route_stops rs JOIN bins b ON rs.bin_id=b.id WHERE rs.route_id=? ORDER BY rs.stop_order", r.get("id"));
        r.put("stops", stops);
      }
      return routes;
    } 
    else {
      return jdbc.queryForList("SELECT * FROM routes");
    }
  }
}
