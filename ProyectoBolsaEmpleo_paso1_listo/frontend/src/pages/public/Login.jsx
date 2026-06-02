function Login() {
    return (
        <section className="form-page">
            <h1>Login</h1>

            <form className="form-card">
                <label>Usuario o correo</label>
                <input type="text" placeholder="Ingrese su usuario" />

                <label>Clave</label>
                <input type="password" placeholder="Ingrese su clave" />

                <button type="button">Ingresar</button>
            </form>
        </section>
    );
}

export default Login;