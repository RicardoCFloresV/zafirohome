# Universidad Tecnológica de México (UNITEC Campus SUR)

## Aplicaciones Móviles

## Equipo 18

## Integrantes:

### Flore Ricardo Cesar

### Romano Rodriguez Víctor Angel

### Rodriguez Cervantes Miguel Ángel

## Proyecto Final: Aplicación de Inventario



#### Repositorio : https://github.com/RicardoCFloresV/zafirohome



###### Descripción general del proyecto

El objetivo principal de la aplicación es **gestionar el inventario de forma eficiente**, permitiendo al personal administrativo consultar, crear, modificar y eliminar registros de productos y elementos relacionados.
La aplicación esta pensada para los administradores y encargados de inventarios dentro de la empresa Zafiro Home Construcciones.
Se creo una aplicación que permite el conocer de manera general la empresa Zafiro Home Construcciones esta se puede iniciar sesión en una ventana diferente por el medio del cual se permite observar las diferentes opciones del Panel de Administración las cuales son el Panel de Cajas, Marcas, Categorías, Unidades de Medida, Unidades de Volumen, Usuarios y Productos. De las cuales nos permiten buscar cada panel por ID o un listado de estos.

Se utilizaron las aplicaciones de Visual Studio, Android Studio con la cual se ocupa kotlin con jet compose.

Se requirió la importación de los siguientes comandos:import retrofit2.http.Body, import retrofit2.http.DELETE, import retrofit2.http.GET, import retrofit2.http.PUT, import retrofit2.http.Path, import okhttp3.MultipartBody, import okhttp3.RequestBody, import retrofit2.Response, import retrofit2.http.Multipart, import retrofit2.http.POST, import retrofit2.http.Part .

Junto con las siguientes implementaciones: implementation(libs.retrofit), implementation(libs.converter.gson),, implementation(libs.androidx.lifecycle.viewmodel.compose), implementation(libs.androidx.compose.material.icons.extended),implementation(libs.coil.compose), implementation(libs.retrofit.v290), implementation(libs.converter.gson.v290)



###### Instrucciones de instalación y ejecución

Para el optimo funcionamiento de la aplicación esta se instala directamente desde la computadora, se abre normalmente para poder ocupar las funciones de la aplicación con conexión a la base de datos y poder crear, modificar y eliminar datos en la aplicación se requiere un usuario y contraseña valido. Este seria Usuario **"RicardoAdmin"** y la contraseña seria **"Agato25"**. Una vez accediendo al usuario autorizado se podrá ocupar las funciones de la aplicación completa.



###### Explicación técnica del funcionamiento

Esta aplicación se conecta a un servidor conectado a una base de datos de SQL Server atreves de Node.js en el cual se puede crear, modificar y eliminar datos según se requiera en la empresa Zafiro Home Construcciones.
