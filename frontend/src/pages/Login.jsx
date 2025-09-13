import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useApi } from '../lib/ApiProvider'
export default function Login(){
  const api = useApi(), nav = useNavigate()
  const [username,setUsername] = useState(''), [password,setPassword] = useState('')
  const [error,setError] = useState(null), [loading,setLoading] = useState(false)
  async function submit(e){ e.preventDefault(); setError(null); setLoading(true)
    try{ const res = await api.post('/auth/login',{username,password})
      if(!res.data.success){ setError(res.data.message||'Login failed') }
      else{ const { role } = res.data; if(role==='ADMIN') nav('/admin'); else if(role==='DRIVER') nav('/driver'); else nav('/user') }
    }catch(err){ console.error(err); setError('Backend error or server not reachable') } finally{ setLoading(false) }
  }
  return (<div className="card" style={{maxWidth:480, margin:'40px auto'}}>
    <h2>Green Route Navigator</h2>
    <form onSubmit={submit}>
      <label>Username</label><input className="input" value={username} onChange={e=>setUsername(e.target.value)} />
      <label style={{marginTop:8, display:'block'}}>Password</label><input className="input" type="password" value={password} onChange={e=>setPassword(e.target.value)} />
      {error && <div style={{color:'#dc2626', marginTop:8}}>{error}</div>}
      <button className="btn" style={{marginTop:12}} disabled={loading}>{loading?'Signing in...':'Login'}</button>
    </form>
    <div style={{marginTop:12}}><a className="link" href="/register">Register</a></div>
  </div>)
}
