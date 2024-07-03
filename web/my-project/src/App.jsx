import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import HeroSection from './MainPage/HeroSection'
import Pricing from './MainPage/pricing'

function App() {
  const [count, setCount] = useState(0)

  return (
    <>
      <HeroSection />
      <Pricing />
    </>
  )
}

export default App
