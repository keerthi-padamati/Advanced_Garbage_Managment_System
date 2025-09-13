import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useApi } from '../lib/ApiProvider'
export default function Register(){
  const api = useApi(), nav = useNavigate()
  const [form,setForm] = useState({username:'',password:'',address:''}); const [msg,setMsg] = useState(null)
  function set(k,v){ setForm(prev=>({...prev,[k]:v})) }
  async function submit(e){ e.preventDefault(); setMsg(null)
    try{ const res = await api.post('/auth/register', form)
      if(res.data.success){ setMsg('Registration successful. Login now.'); setTimeout(()=>nav('/'), 800) } else setMsg(res.data.message||'Registration failed')
    }catch(e){ setMsg('Registration failed') }
  }
  return (<div className="card" style={{maxWidth:520, margin:'40px auto'}}>
    <h2>Create account</h2>
    <form onSubmit={submit}>
      <label>Username</label><input className="input" value={form.username} onChange={e=>set('username', e.target.value)} />
      <label style={{marginTop:8, display:'block'}}>Password</label><input className="input" type="password" value={form.password} onChange={e=>set('password', e.target.value)} />
      <label style={{marginTop:8, display:'block'}}>Address</label><input className="input" value={form.address} onChange={e=>set('address', e.target.value)} />
      {msg && <div style={{marginTop:8}}>{msg}</div>}
      <button className="btn" style={{marginTop:12}}>Register</button>
    </form>
  </div>)
}
