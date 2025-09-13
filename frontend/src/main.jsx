import React from 'react'
import { createRoot } from 'react-dom/client'
import { BrowserRouter, Routes, Route, Link } from 'react-router-dom'
import ApiProvider from './lib/ApiProvider'
import Login from './pages/Login'
import Register from './pages/Register'
import Admin from './pages/Admin'
import Driver from './pages/Driver'
import User from './pages/User'
function Shell(){
  return (<BrowserRouter><ApiProvider>
    <header><div className="container"><b>Green Route Navigator</b>
    <span style={{float:'right'}}><Link to="/">Login</Link><Link to="/register">Register</Link></span></div></header>
    <div className="container">
      <Routes>
        <Route path="/" element={<Login/>}/>
        <Route path="/register" element={<Register/>}/>
        <Route path="/admin" element={<Admin/>}/>
        <Route path="/driver" element={<Driver/>}/>
        <Route path="/user" element={<User/>}/>
      </Routes>
    </div>
  </ApiProvider></BrowserRouter>)
}
createRoot(document.getElementById('root')).render(<Shell/>)
