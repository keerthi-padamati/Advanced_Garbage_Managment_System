import React, { useEffect, useState } from 'react'
import { useApi } from '../lib/ApiProvider'
export default function User(){
  const api=useApi()
  const [userId,setUserId]=useState('4')
  const [bins,setBins]=useState([])
  const [msg,setMsg]=useState(null)
  async function load(){ const res=await api.get('/bins/user/'+userId); setBins(res.data) }
  useEffect(()=>{ load() },[userId])
  async function markReady(id){ await api.put('/bins/'+id+'/status',{status:'READY'}); setMsg('Marked READY'); load() }
  return (<div className="card">
    <h3>Your bins</h3>
    <div style={{display:'flex',gap:12,marginBottom:12}}><label>User ID</label>
      <input className="input" style={{maxWidth:120}} value={userId} onChange={e=>setUserId(e.target.value)} /></div>
    {msg && <div>{msg}</div>}
    <table style={{width:'100%',borderCollapse:'collapse'}}><thead><tr><th>ID</th><th>Type</th><th>Status</th><th>Actions</th></tr></thead>
      <tbody>{bins.map(b=>(<tr key={b.id}><td>{b.id}</td><td>{b.waste_type}</td><td>{b.status}</td><td><button className="btn" onClick={()=>markReady(b.id)}>Mark READY</button></td></tr>))}</tbody>
    </table>
  </div>)
}
