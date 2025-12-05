# Universidad Tecnológica de México (UNITEC Campus SUR)

## Aplicaciones Móviles

### Equipo 18

### Integrantes:

### Flore Ricardo Cesar

### Romano Rodriguez Víctor Angel

### Rodriguez Cervantes Miguel Ángel

### Proyecto Final: Aplicación de Inventario

### 

### Repositorio : https://github.com/RicardoCFloresV/zafirohome



##### Descripción general del proyecto

El objetivo principal de la aplicación es **gestionar el inventario de forma eficiente**, permitiendo al personal administrativo consultar, crear, modificar y eliminar registros de productos y elementos relacionados.
La aplicación esta pensada para los administradores y encargados de inventarios dentro de la empresa Zafiro Home Construcciones.
Se creo una aplicación que permite el conocer de manera general la empresa Zafiro Home Construcciones esta se puede iniciar sesión en una ventana diferente por el medio del cual se permite observar las diferentes opciones del Panel de Administración las cuales son el Panel de Cajas, Marcas, Categorías, Unidades de Medida, Unidades de Volumen, Usuarios y Productos. De las cuales nos permiten buscar cada panel por ID o un listado de estos.

Se utilizaron las aplicaciones de Visual Studio, Android Studio con la cual se ocupa kotlin con jet compose.

Se requirió la importación de los siguientes comandos:import retrofit2.http.Body, import retrofit2.http.DELETE, import retrofit2.http.GET, import retrofit2.http.PUT, import retrofit2.http.Path, import okhttp3.MultipartBody, import okhttp3.RequestBody, import retrofit2.Response, import retrofit2.http.Multipart, import retrofit2.http.POST, import retrofit2.http.Part .

Junto con las siguientes implementaciones: implementation(libs.retrofit), implementation(libs.converter.gson),, implementation(libs.androidx.lifecycle.viewmodel.compose), implementation(libs.androidx.compose.material.icons.extended),implementation(libs.coil.compose), implementation(libs.retrofit.v290), implementation(libs.converter.gson.v290)



##### Instrucciones de instalación y ejecución

Para el optimo funcionamiento de la aplicación esta se instala directamente desde la computadora, se abre normalmente para poder ocupar las funciones de la aplicación con conexión a la base de datos y poder crear, modificar y eliminar datos en la aplicación se requiere un usuario y contraseña valido. Este seria Usuario **"RicardoAdmin"** y la contraseña seria **"Agato25"**. Una vez accediendo al usuario autorizado se podrá ocupar las funciones de la aplicación completa.



##### Explicación técnica del funcionamiento

Esta aplicación se conecta a un servidor conectado a una base de datos de SQL Server atreves de Node.js en el cual se puede crear, modificar y eliminar datos según se requiera en la empresa Zafiro Home Construcciones.



###### Explicación Técnica Detallada del Sistema

###### 1\. Arquitectura del Cliente (Android)



La aplicación está construida utilizando Kotlin y Jetpack Compose, siguiendo el paradigma de UI Declarativa. La arquitectura se basa en el patrón Single Activity, donde MainActivity.kt actúa como el contenedor principal y la navegación se gestiona mediante "State Hoisting" (elevación de estado).



&nbsp;   Gestión de Estado y Navegación: A diferencia de la navegación tradicional basada en Fragmentos, la aplicación utiliza variables de estado (mutableStateOf) dentro de un Composable principal (NavegacionPrincipal). Esto permite cambiar dinámicamente entre pantallas (Login, Dashboard, CRUDs) redibujando la interfaz de usuario de manera reactiva según el valor de pantallaActual.



&nbsp;   Diseño de Interfaz: Se utiliza Scaffold y componentes de Material Design 3 para asegurar una estructura visual coherente. El diseño es responsivo y modular, separando las pantallas en funciones Composable independientes (ej. LoginScreen, ProductosScreen).



###### 2\. Capa de Red y Conectividad (Networking)



La comunicación con el servidor se realiza mediante la librería Retrofit 2, configurada para manejar peticiones RESTful de manera asíncrona.



&nbsp;   Cliente HTTP y Gestión de Sesiones: Un aspecto crítico de la seguridad del sistema es la implementación de un CookieJar personalizado en RetrofitClient.kt. Dado que el backend (Node.js) utiliza sesiones (req.session), el cliente Android intercepta, almacena y reenvía automáticamente las cookies de sesión (como connect.sid) en cada petición subsecuente al Login. Esto garantiza que el usuario mantenga su autenticación mientras navega por la app.



&nbsp;   Configuración de Tiempos (Timeouts): Se ha configurado el cliente OkHttpClient con tiempos de espera extendidos (60 segundos para lectura, escritura y conexión). Esta decisión técnica se tomó específicamente para soportar la carga de imágenes pesadas sin que la conexión se cierre prematuramente.



###### 3\. Interfaz de Programación de Aplicaciones (API)



La interfaz ZafiroApiService define los contratos de comunicación con el servidor. El sistema soporta operaciones CRUD completas y casos de uso complejos:



&nbsp;   Jerarquía de Categorías: La API gestiona una estructura de datos relacional compleja dividida en tres niveles: Categorías Principales, Secundarias y Subcategorías, permitiendo una organización granular del inventario.



&nbsp;   Carga de Imágenes (Multipart): Se implementa el envío de archivos binarios mediante peticiones @Multipart. Esto permite enviar objetos MultipartBody.Part junto con datos primitivos (RequestBody para el ID del producto) en una sola transacción HTTP para asociar fotografías a los productos.



&nbsp;   Endpoints Implementados:



&nbsp;       Auth: Login y validación de roles (Admin/User).



&nbsp;       Inventario: Gestión de Marcas, Cajas, Tallas y Unidades de Volumen.



&nbsp;       Productos: Búsqueda por nombre y gestión completa de fichas de producto.



###### 4\. Stack Tecnológico y Librerías



El proyecto integra un conjunto robusto de librerías para garantizar el rendimiento y la mantenibilidad:



&nbsp;   Frontend: Android Jetpack Compose (UI), Material3.



&nbsp;   Networking:



&nbsp;       Retrofit 2.9.0: Cliente REST.



&nbsp;       OkHttp 3: Cliente HTTP subyacente y manejo de cookies.



&nbsp;       Gson: Serialización y deserialización de objetos JSON (Data Transfer Objects).



&nbsp;   Backend (Contexto): Node.js con Express (gestionando sesiones) conectado a Microsoft SQL Server.



&nbsp;   Manejo de Imágenes: Coil (para carga asíncrona de imágenes en UI) y soporte Multipart en Retrofit.



###### 5\. Flujo de Datos y Seguridad



El flujo de la aplicación comienza con la autenticación en /auth/login. La respuesta del servidor (LoginResponse) no solo confirma el acceso, sino que devuelve banderas de roles (isAdmin, isUser). Basado en esta respuesta, la aplicación decide si redirigir al ADMIN\_DASHBOARD (con acceso total a módulos de gestión) o al USER\_HOME (vista de tienda), protegiendo así las funciones críticas de administración.

