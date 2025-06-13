# 🛫 SkyBooker - Sistema de Reservas de Vuelos

SkyBooker es una aplicación web moderna para la gestión y reserva de vuelos, desarrollada con Spring Boot y una interfaz web intuitiva. Permite a los usuarios buscar vuelos por destino, realizar reservas y gestionar su información personal.

## 📋 Tabla de Contenidos

- [Características](#-características)
- [Tecnologías Utilizadas](#-tecnologías-utilizadas)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Requisitos Previos](#-requisitos-previos)
- [Instalación y Configuración](#-instalación-y-configuración)
- [Uso de la Aplicación](#-uso-de-la-aplicación)
- [API Endpoints](#-api-endpoints)
- [Base de Datos](#-base-de-datos)
- [Arquitectura](#-arquitectura)
- [Solución de Problemas](#-solución-de-problemas)

## ✨ Características

### Funcionalidades Principales
- 🔍 **Búsqueda de Vuelos**: Busca vuelos por ciudad de destino (fecha opcional)
- 🎫 **Sistema de Reservas**: Realiza reservas de vuelos de forma sencilla
- 📋 **Historial de Reservas**: Consulta reservas anteriores por código de usuario
- 📊 **Panel de Administración**: Estadísticas y gestión de datos

### Características Técnicas
- 🚀 **Inicialización Automática**: Carga datos de prueba al iniciar la aplicación
- 💾 **Base de Datos en Memoria**: H2 Database para desarrollo y testing
- 🌐 **API REST**: Endpoints RESTful para todas las operaciones
- 📱 **Interfaz Responsive**: Diseño adaptable a diferentes dispositivos

## 🛠️ Tecnologías Utilizadas

### Backend
- **Java 17**
- **Spring Boot 3.5.0**
- **Spring Data JPA**
- **H2 Database**
- **Lombok**
- **Maven**

### Frontend
- **HTML5**
- **CSS3**
- **JavaScript**
- **Fetch API**

### Herramientas de Desarrollo
- **Maven Wrapper**
- **H2 Console** para gestión de base de datos
- **Spring Boot DevTools**

## 📁 Estructura del Proyecto

```
vuelos-spring-boot/
├── src/
│   ├── main/
│   │   ├── java/com/example/vuelosspringboot/
│   │   │   ├── config/
│   │   │   │   └── DataInitializer.java           # Inicialización de datos
│   │   │   ├── controllers/
│   │   │   │   ├── AdminController.java           # Endpoints de administración
│   │   │   │   ├── VueloController.java           # API de vuelos
│   │   │   │   ├── ReservaController.java         # API de reservas
│   │   │   │   └── BaseController*.java           # Controladores base
│   │   │   ├── entities/
│   │   │   │   ├── Vuelo.java                     # Entidad principal
│   │   │   │   ├── Ciudad.java, Aeropuerto.java   # Entidades de ubicación
│   │   │   │   ├── Usuario.java, Reserva.java     # Entidades de usuario
│   │   │   │   └── ...                            # Otras entidades
│   │   │   ├── services/
│   │   │   │   └── *Service*.java                 # Lógica de negocio
│   │   │   ├── repositories/
│   │   │   │   └── *Repository.java               # Acceso a datos
│   │   │   └── VuelosSpringBootApplication.java   # Clase principal
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── index.html                     # Interfaz principal
│   │       │   ├── style.css                      # Estilos
│   │       │   └── script.js                      # Lógica frontend
│   │       └── application.properties             # Configuración
├── pom.xml                                        # Dependencias Maven
└── README.md                                      # Este archivo
```

## 📋 Requisitos Previos

- **Java 17** o superior
- **Maven 3.6+** (opcional, se incluye Maven Wrapper)
- **Navegador web moderno** (Chrome, Firefox, Safari, Edge)

### Verificar Java
```bash
java -version
# Debe mostrar Java 17 o superior
```

## 🚀 Instalación y Configuración

### 1. Clonar o Descargar el Proyecto
```bash
git clone https://github.com/Resnick7/vuelos-spring-boot
cd vuelos-spring-boot
```

### 2. Ejecutar la Aplicación
```bash
# Usando Maven Wrapper (recomendado)
./mvnw spring-boot:run

# O en Windows
mvnw.cmd spring-boot:run

# Usando Maven instalado localmente
mvn spring-boot:run
```

### 3. Verificar la Instalación
La aplicación iniciará y mostrará logs similares a:
```
🚀 Iniciando carga de datos iniciales (versión minimalista)...
✅ Ciudades: 10
✅ Aerolíneas: 5  
✅ Aeropuertos: 10
✅ Usuarios: 5
✅ Total vuelos: 15
🎉 ¡Datos básicos cargados exitosamente!
```

### 4. Acceder a la Aplicación
- **Aplicación Principal**: http://localhost:8080
- **Consola H2 Database**: http://localhost:8080/h2-console

## 🎯 Uso de la Aplicación

### Interfaz Principal

#### 1. 🔍 Explorar Vuelos
- Selecciona una **ciudad de destino** del menú desplegable
- La **fecha es opcional** (puedes dejarla vacía para ver todos los vuelos)
- Haz clic en **"🚀 Explorar Vuelos Disponibles"**
- Navega por los resultados y haz clic en **"🎫 Reservar Ahora"**

#### 2. 📋 Historial de Reservas
- Ve a la pestaña **"📋 Historial de Reservas"**
- Introduce tu **código de usuario** (ID numérico, ej: 1, 2, 3...)
- Haz clic en **"Consultar"** para ver tus reservas

### Datos de Prueba Incluidos

Al iniciar la aplicación, se cargan automáticamente:
- **10 ciudades**: Madrid, Barcelona, París, Londres, Roma, etc.
- **5 aerolíneas**: Iberia, Vueling, Lufthansa, Air France, British Airways
- **5 usuarios** (IDs: 1-5):
    - Ana González (ID: 1)
    - Juan Pérez (ID: 2)
    - María López (ID: 3)
    - Carlos Rodríguez (ID: 4)
    - Laura Martínez (ID: 5)
- **15 vuelos** entre diferentes ciudades

## 🔌 API Endpoints

### Vuelos
```http
GET  /vuelos/buscar?ciudad={nombre}&fecha={yyyy-mm-dd}  # Buscar vuelos
GET  /vuelos/datos-reserva                              # Obtener datos para formularios
GET  /vuelos/test-datos                                 # Verificar estado de datos
```

### Reservas
```http
POST /reservas/crear                                    # Crear nueva reserva
GET  /reservas/usuario/{id}                             # Obtener reservas de un usuario
```

### Administración
```http
GET  /admin/dashboard                                   # Estadísticas generales
GET  /admin/debug-datos                                 # Información de debug
GET  /admin/health                                      # Estado del sistema
```

### Ejemplo de Uso de API
```bash
# Buscar vuelos a Madrid
curl "http://localhost:8080/vuelos/buscar?ciudad=madrid"

# Crear una reserva
curl -X POST http://localhost:8080/reservas/crear \
  -H "Content-Type: application/json" \
  -d '{"usuarioId": 1, "vueloId": 1}'

# Ver reservas del usuario 1
curl "http://localhost:8080/reservas/usuario/1"
```

## 💾 Base de Datos

### Configuración H2
- **Modo**: En memoria (se reinicia con la aplicación)
- **URL**: `jdbc:h2:mem:testdb`
- **Usuario**: `sa`
- **Contraseña**: (vacía)

### Acceso a H2 Console
1. Ve a http://localhost:8080/h2-console
2. Usa la configuración por defecto
3. Haz clic en "Connect"

### Tablas Principales
- `CIUDAD` - Ciudades disponibles
- `AEROPUERTO` - Aeropuertos por ciudad
- `AEROLINEA` - Compañías aéreas
- `VUELO` - Vuelos disponibles
- `USUARIO` - Usuarios del sistema
- `RESERVA` - Reservas realizadas

## 🏗️ Arquitectura

### Patrón MVC
- **Model**: Entidades JPA (`entities/`)
- **View**: Interfaz web (`static/`)
- **Controller**: Controllers REST (`controllers/`)

### Capas de la Aplicación
1. **Presentación**: HTML/CSS/JavaScript
2. **API REST**: Spring Boot Controllers
3. **Lógica de Negocio**: Services
4. **Acceso a Datos**: Repositories + JPA
5. **Persistencia**: H2 Database

## 🔧 Solución de Problemas

### Problemas Comunes

#### 1. La aplicación no inicia
```bash
# Verificar Java
java -version

# Limpiar y reiniciar
./mvnw clean
./mvnw spring-boot:run
```

#### 2. No se encuentran vuelos
- Usa los **botones de debug** en la interfaz (esquina inferior derecha)
- Verifica que los datos se cargaron correctamente
- Prueba buscar por "madrid", "barcelona", "paris"

#### 3. Error de base de datos
- La aplicación usa H2 en memoria, se reinicia automáticamente
- Si hay problemas persistentes, reinicia la aplicación

#### 4. Puerto ocupado
```bash
# Cambiar puerto en application.properties
server.port=8081

# O terminar proceso que usa el puerto 8080
# Windows
netstat -ano | findstr :8080
taskkill /F /PID <PID>

# Linux/Mac  
lsof -ti :8080 | xargs kill -9
```

### Herramientas de Debug Incluidas

#### Botones en la Interfaz
- **🔄 Test Conexión**: Verifica conectividad con el backend
- **🔍 Debug Datos**: Muestra estadísticas de la base de datos

#### Logs Útiles
Los logs de la aplicación muestran:
- ✅ Éxito en operaciones
- ❌ Errores detallados
- 📊 Estadísticas de datos cargados

### Configuración de Desarrollo

#### Cambiar a Base de Datos Persistente
Modifica `application.properties`:
```properties
# Cambiar de memoria a archivo
spring.datasource.url=jdbc:h2:file:./data/vuelos_db
```

#### Habilitar Logs Detallados
```properties
# Ver SQL ejecutado
spring.jpa.show-sql=true
logging.level.org.hibernate.SQL=DEBUG

# Ver parámetros de consultas
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

---

## 🤝 Contribuciones

Este proyecto fue desarrollado como parte de un curso de Programación Orientada a Objetos con Spring Boot.

---

## 📄 Licencia

Este proyecto es de uso educativo y está disponible bajo la licencia MIT.

---

**¡Disfruta explorando el mundo con SkyBooker!** ✈️🌍