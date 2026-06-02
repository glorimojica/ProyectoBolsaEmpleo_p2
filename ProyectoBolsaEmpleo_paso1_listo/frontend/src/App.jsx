import { BrowserRouter, Routes, Route } from "react-router-dom";
import Navbar from "./components/Navbar";
import Footer from "./components/Footer";
import Home from "./pages/public/Home";
import BuscarPuestos from "./pages/public/BuscarPuestos";
import Login from "./pages/public/Login";
import RegistroEmpresa from "./pages/public/RegistroEmpresa";
import RegistroOferente from "./pages/public/RegistroOferente";
import "./styles.css";

function App() {
  return (
      <BrowserRouter>
        <Navbar />

        <main className="main-content">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/buscar" element={<BuscarPuestos />} />
            <Route path="/login" element={<Login />} />
            <Route path="/registro/empresa" element={<RegistroEmpresa />} />
            <Route path="/registro/oferente" element={<RegistroOferente />} />
          </Routes>
        </main>

        <Footer />
      </BrowserRouter>
  );
}

export default App;