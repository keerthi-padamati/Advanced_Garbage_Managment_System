import React, { useEffect, useRef, useState } from 'react'
import { useApi } from '../lib/ApiProvider'
import L from 'leaflet'
export default function Admin(){
  const api = useApi(), mapRef = useRef(null), mapInst = useRef(null)
  const [bins,setBins] = useState([]), [trucks,setTrucks] = useState([])
  useEffect(()=>{
    if(!mapInst.current){ mapInst.current = L.map(mapRef.current).setView([16.3067,80.4365], 11)
      L.tileLayer('https:
    const id=setInterval(load,3000); load(); return ()=>clearInterval(id)
  },[])
  async function load(){ try{ const [b,t]=await Promise.all([api.get('/bins'),api.get('/trucks')]); setBins(b.data); setTrucks(t.data); draw(b.data,t.data) }catch(e){ console.error(e) } }
  function draw(bins,trucks){ mapInst.current.eachLayer(l=>{ if(l._url===undefined) mapInst.current.removeLayer(l) })
    bins.forEach(b=>{ if(b.latitude&&b.longitude){ const color=(b.status==='READY'||b.status==='FULL')?'red':'green'
      const icon=L.divIcon({className:'',html:`<div style="background:${color};width:12px;height:12px;border-radius:50%"></div>`})
      L.marker([b.latitude,b.longitude],{icon}).addTo(mapInst.current).bindPopup(`Bin #${b.id} - ${b.waste_type} - ${b.status}`) } })
    trucks.forEach(t=>{ if(t.latitude&&t.longitude){ const icon=L.divIcon({className:'',html:`<div style="background:#2563eb;width:14px;height:14px;border-radius:4px"></div>`})
      L.marker([t.latitude,t.longitude],{icon}).addTo(mapInst.current).bindPopup(`Truck #${t.id} - ${t.truck_number}`) } })
  }
  return (<div className="grid">
    <div className="card"><h3>System map</h3><div ref={mapRef} className="map"></div></div>
    <div className="card"><h3>Lists</h3><div style={{maxHeight:380,overflow:'auto'}}>
      <h4>Trucks</h4><ul>{trucks.map(t=>(<li key={t.id}>#{t.id} {t.truck_number} (driver {t.driver_id})</li>))}</ul>
      <h4>Bins</h4><ul>{bins.map(b=>(<li key={b.id}>#{b.id} {b.waste_type} - {b.status}</li>))}</ul>
    </div></div>
  </div>)
}
