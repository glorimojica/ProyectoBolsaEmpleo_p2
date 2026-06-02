function RegistroOferente() {
    return (
        <section className="form-page">
            <h1>Registro de Oferente</h1>

            <form className="form-card">
                <label>Identificación</label>
                <input type="text" />

                <label>Nombre</label>
                <input type="text" />

                <label>Primer apellido</label>
                <input type="text" />

                <label>Nacionalidad</label>
                <input type="text" />

                <label>Teléfono</label>
                <input type="text" />

                <label>Correo electrónico</label>
                <input type="email" />

                <label>Lugar de residencia</label>
                <input type="text" />

                <label>Clave</label>
                <input type="password" />

                <button type="button">Registrarse</button>
            </form>
        </section>
    );
}

export default RegistroOferente;