function RegistroEmpresa() {
    return (
        <section className="form-page">
            <h1>Registro de Empresa</h1>

            <form className="form-card">
                <label>Nombre</label>
                <input type="text" />

                <label>Localización</label>
                <input type="text" />

                <label>Correo electrónico</label>
                <input type="email" />

                <label>Teléfono</label>
                <input type="text" />

                <label>Descripción</label>
                <textarea />

                <label>Clave</label>
                <input type="password" />

                <button type="button">Registrarse</button>
            </form>
        </section>
    );
}

export default RegistroEmpresa;