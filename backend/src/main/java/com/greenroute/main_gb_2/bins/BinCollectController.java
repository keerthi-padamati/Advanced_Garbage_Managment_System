package com.greenroute.main_gb_2.bins;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
public class BinCollectController
{
  private final JdbcTemplate jdbc;
  public BinCollectController(JdbcTemplate jdbc)
  {
     this.jdbc = jdbc; 
  }

  @PutMapping("/api/bins/{id}/collected")
  public Map<String,Object> collected(@PathVariable int id, @RequestBody Map<String,Object> body)
  {
    Integer truckId = body.get("truck_id")==null ? null : Integer.parseInt(body.get("truck_id").toString());
    jdbc.update("INSERT INTO collections (bin_id, waste_type, weight_kg) SELECT id, waste_type, 5 FROM bins WHERE id=?", id);
    jdbc.update("UPDATE bins SET status='EMPTIED' WHERE id=?", id);
    if(truckId!=null)
    {
      jdbc.update("UPDATE route_stops rs JOIN routes r ON rs.route_id=r.id SET rs.visited=TRUE WHERE rs.bin_id=? AND r.truck_id=?", id, truckId);
    } 
    else
    {
      jdbc.update("UPDATE route_stops SET visited=TRUE WHERE bin_id=?", id);
    }
    return Map.of("success", true);
  }
}
