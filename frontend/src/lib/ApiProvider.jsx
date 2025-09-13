import React from 'react'
import axios from 'axios'
const ApiContext = React.createContext(null)
export default function ApiProvider({ children }){
  const api = axios.create({ baseURL:'http:
  return <ApiContext.Provider value={api}>{children}</ApiContext.Provider>
}
export const useApi = () => React.useContext(ApiContext)
