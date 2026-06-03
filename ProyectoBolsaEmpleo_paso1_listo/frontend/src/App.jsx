import { BrowserRouter, Routes, Route } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext";

import Navbar from "./components/Navbar";
import Footer from "./components/Footer";
import ProtectedRoute from "./components/ProtectedRoute";

import Home from "./pages/public/Home";
import BuscarPuestos from "./pages/public/BuscarPuestos";
import Login from "./pages/public/Login";
import RegistroEmpresa from "./pages/public/RegistroEmpresa";
import RegistroOferente from "./pages/public/RegistroOferente";

import DashboardAdmin from "./pages/admin/DashboardAdmin";
import DashboardEmpresa from "./pages/empresa/DashboardEmpresa";
import DashboardOferente from "./pages/oferente/DashboardOferente";

import "./styles.css";

function App() {
    return (
        <BrowserRouter>
            <AuthProvider>
                <Navbar />

                <main className="main-content">
                    <Routes>
                        <Route path="/" element={<Home />} />
                        <Route path="/buscar" element={<BuscarPuestos />} />
                        <Route path="/login" element={<Login />} />
                        <Route path="/registro/empresa" element={<RegistroEmpresa />} />
                        <Route path="/registro/oferente" element={<RegistroOferente />} />

                        <Route
                            path="/admin/dashboard"
                            element={
                                <ProtectedRoute rolesPermitidos={["ADMIN"]}>
                                    <DashboardAdmin />
                                </ProtectedRoute>
                            }
                        />

                        <Route
                            path="/empresa/dashboard"
                            element={
                                <ProtectedRoute rolesPermitidos={["EMPRESA"]}>
                                    <DashboardEmpresa />
                                </ProtectedRoute>
                            }
                        />

                        <Route
                            path="/oferente/dashboard"
                            element={
                                <ProtectedRoute rolesPermitidos={["OFERENTE"]}>
                                    <DashboardOferente />
                                </ProtectedRoute>
                            }
                        />
                    </Routes>
                </main>

                <Footer />
            </AuthProvider>
        </BrowserRouter>
    );
}

export default App;