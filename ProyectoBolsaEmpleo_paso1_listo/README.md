## Se uso:

- Backend:

* Java
* Spring Boot
* Spring Security
* JWT
* Spring Data JPA
* Maven
* MySQL

- Frontend

* React
* Vite
* JavaScript
* Fetch API
* CSS

## Roles del sistema

* ADMIN: aprueba empresas, aprueba oferentes y gestiona características.
* EMPRESA: publica puestos, agrega requisitos, busca candidatos y visualiza CV.
* OFERENTE: edita su perfil, registra habilidades, sube CV y consulta puestos.
* PUBLIC: ve puestos públicos, busca puestos públicos, se registra e inicia sesión.


### Base de datos

1. Abrir MySQL Workbench.
2. Abrir el archivo.
3. Ejecutar todo el script.
4. Verificar que se creó la base de datos `bolsa_empleo`.
5. Cambiar la contraseña en properties.


| Rol      | Usuario                                                           | Estado    |
| -------- | ----------------------------------------------------------------- | --------- |
| ADMIN    | admin                                                             | Aprobado  |
| ADMIN    | [admin@admin.com](mailto:admin@admin.com)                         | Aprobado  |
| EMPRESA  | [empresa@empresa.com](mailto:empresa@empresa.com)                 | Aprobado  |
| OFERENTE | [oferente@oferente.com](mailto:oferente@oferente.com)             | Aprobado  |
| EMPRESA  | [empresa.pendiente@demo.com](mailto:empresa.pendiente@demo.com)   | Pendiente |
| OFERENTE | [oferente.pendiente@demo.com](mailto:oferente.pendiente@demo.com) | Pendiente |
| EMPRESA  | [empresa.rechazada@demo.com](mailto:empresa.rechazada@demo.com)   | Rechazado |
| OFERENTE | [oferente.rechazado@demo.com](mailto:oferente.rechazado@demo.com) | Rechazado |

