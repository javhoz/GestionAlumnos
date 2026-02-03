# Configuración de Entornos - GestionAlumnos

## Entornos Configurados

Se han implementado tres entornos para la aplicación Spring Boot: **dev**, **test** y **prod**.

## Archivos de Configuración

### Application YAML Files
- `application.yml` - Configuración principal con variables de entorno
- `application-dev.yml` - Configuración de desarrollo
- `application-test.yml` - Configuración de testing  
- `application-prod.yml` - Configuración de producción

### Environment Files
- `.env.development` - Variables para entorno de desarrollo
- `.env.test` - Variables para entorno de testing
- `.env.production` - Variables para entorno de producción

## Características por Entorno

### Development (dev)
- **Base de datos**: H2 en memoria
- **MongoDB**: Local en puerto 27017
- **Logging**: Nivel DEBUG
- **Actuator**: Todos los endpoints expuestos
- **CORS**: Permisivo para localhost
- **Vaadin**: Modo desarrollo activado
- **H2 Console**: Habilitada en `/h2-console`

### Test
- **Base de datos**: H2 en memoria (aislada)
- **MongoDB**: Base de datos separada para tests
- **Logging**: Mínimo (WARN/INFO)
- **Actuator**: Solo endpoint health
- **Server**: Puerto aleatorio para tests paralelos
- **Security**: Mock habilitado

### Production (prod)
- **Base de datos**: MySQL/PostgreSQL real
- **MongoDB**: Base de datos de producción
- **Logging**: Nivel WARN, archivo de logs
- **Actuator**: Endpoints limitados (health, info, metrics)
- **SSL**: Habilitado por defecto
- **Security**: Headers de seguridad configurados
- **Rate Limiting**: Habilitado
- **CORS**: Restrictivo (solo dominios permitidos)

## Uso

### Ejecutar con Maven Profiles

```bash
# Desarrollo (default)
mvn spring-boot:run

# Testing
mvn spring-boot:run -Dspring-boot.run.profiles=test

# Producción
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

### Ejecutar con Variables de Entorno

```bash
# Cargar archivo .env específico
export $(cat .env.development | xargs) && mvn spring-boot:run

# O directamente
SPRING_PROFILES_ACTIVE=prod mvn spring-boot:run
```

### Tests

```bash
# Ejecutar tests con perfil test
mvn test -Dspring.profiles.active=test
```

## Variables de Entorno Críticas (Producción)

**OBLIGATORIO configurar estas variables en producción:**

- `DB_URL` - URL de base de datos MySQL/PostgreSQL
- `DB_USERNAME` - Usuario de base de datos
- `DB_PASSWORD` - Contraseña de base de datos
- `JWT_SECRET` - Secreto JWT fuerte (mínimo 256 bits)
- `MONGODB_URI` - URI de MongoDB producción
- `ALLOWED_ORIGINS` - Dominios permitidos para CORS
- `SSL_KEYSTORE_PASSWORD` - Contraseña del keystore SSL

## Seguridad en Producción

1. **Nunca** usar valores por defecto en producción
2. **Cambiar** todos los secrets y passwords
3. **Configurar** SSL/TLS correctamente
4. **Restringir** CORS a dominios específicos
5. **Usar** base de datos real (no H2)
6. **Habilitar** rate limiting
7. **Configurar** logging a archivo
8. **Monitorear** con Actuator endpoints

## Docker

Para Docker, usar el profile correspondiente:

```bash
# Development
docker run -e SPRING_PROFILES_ACTIVE=dev ...

# Production  
docker run -e SPRING_PROFILES_ACTIVE=prod \
           -e DB_URL=jdbc:mysql://db:3306/gestion_alumnos_prod \
           -e JWT_SECRET=tu_secreto_fuerte ...
```

## Verificación

Para verificar el entorno activo:

```bash
curl http://localhost:8080/actuator/info
```

O revisar los logs al inicio de la aplicación.
