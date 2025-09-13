import React, { useEffect, useRef, useState } from 'react'
import { useApi } from '../lib/ApiProvider'
import L from 'leaflet'
export default function Driver(){
  const api=useApi(); const [truckId,setTruckId]=useState('2'); const [trucks,setTrucks]=useState([])
  const mapRef=useRef(null), map=useRef(null)
  useEffect(()=>{ if(!map.current){ map.current=L.map(mapRef.current).setView([16.3067,80.4365],12)
    L.tileLayer('https:
    const id=setInterval(load,3000); load(); return ()=>clearInterval(id)
  },[truckId])
  async function load(){ const t=await api.get('/trucks'); setTrucks(t.data); draw(t.data) }
  function draw(list){ map.current.eachLayer(l=>{ if(l._url===undefined) map.current.removeLayer(l) })
    list.forEach(t=>{ if(t.latitude&&t.longitude){ const icon=L.divIcon({className:'',html:`<div style="background:#1f2937;width:12px;height:12px;border-radius:50%"></div>`})
      L.marker([t.latitude,t.longitude],{icon}).addTo(map.current).bindPopup(`Truck ${t.truck_number}`) } })
  }
  async function setMyLocation(){ if(!navigator.geolocation) return; navigator.geolocation.getCurrentPosition(async (pos)=>{
    const {latitude,longitude}=pos.coords; await api.put('/trucks/'+truckId+'/location',{latitude,longitude}); load()
  })}
  return (<div className="grid">
    <div className="card"><h3>My route map</h3><div ref={mapRef} className="map"></div>
      <div style={{marginTop:12}}><label>Truck ID</label><input className="input" style={{maxWidth:120,marginLeft:8}} value={truckId} onChange={e=>setTruckId(e.target.value)} />
      <button className="btn" style={{marginLeft:8}} onClick={setMyLocation}>Update My Location</button></div></div>
    <div className="card"><h3>All trucks</h3><ul>{trucks.map(t=>(<li key={t.id}>#{t.id} {t.truck_number} [{t.latitude},{t.longitude}]</li>))}</ul></div>
  </div>)
}
