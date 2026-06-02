import { Link } from "react-router-dom";

function Navbar() {
    return (
        <header className="navbar">
            <div className="brand">
                <span className="logo">💼</span>
                <Link to="/">BolsaEmpleo</Link>
            </div>

            <nav>
                <Link to="/">Inicio</Link>
                <Link to="/buscar">Buscar</Link>
                <Link to="/registro/empresa">Empresa</Link>
                <Link to="/registro/oferente">Oferente</Link>
                <Link to="/login">Login</Link>
            </nav>
        </header>
    );
}

export default Navbar;