<div align="center">
   <h1>ERP</h1>
   <i>Módulo de finanzas de PSA</i>
   <br>
   <br>
   <p>
      <img alt="GitHub Actions Workflow Status" src="https://img.shields.io/github/actions/workflow/status/MatiasTK/PSA-Squad7-TribuB/build.yml?style=for-the-badge">
      <img alt="Uptime Robot status" src="https://img.shields.io/uptimerobot/status/m798436758-71702e52aafc0c8a3ee917a2?style=for-the-badge&label=API">
      <img alt="GitHub deployments" src="https://img.shields.io/github/deployments/MatiasTK/PSA-Squad7-TribuB/Production?style=for-the-badge&label=production">
      <img alt="GitHub License" src="https://img.shields.io/github/license/MatiasTK/PSA-Squad7-TribuB?style=for-the-badge">
   </p>

</div>

## Introducción

Proyecto final de la asignatura Ingeniería de Software, dictada por la cátedra Villagra en la Universidad de Buenos Aires, tiene como objetivo analizar los requerimientos de una empresa ficticia, PSA, y diseñar un sistema ERP para la misma. El sistema se estructura en dos módulos: uno para Finanzas y otro para Ventas. Este repositorio corresponde al desarrollo del módulo de Finanzas.

> [!NOTE]
> Los enlaces que originalmente dirigían al módulo de ventas fueron removidos a fin de evitar confusiones. Si desea acceder al repositorio del módulo de ventas deberá contactar a los autores, ya que el mismo no se encuentra disponible públicamente.

## Contribuidores

- [Matias Vallejos](https://github.com/MatiasTK)
- [Juan Sebastián del Rio](https://github.com/S2JuanS2)
- [Lautaro Jovanovics](https://github.com/lautiland)
- [Martín Alejandro Estrada Saavedra](https://github.com/martinSaav)

## Demo del proyecto

- [Frontend](https://psa-squad7-tribub.vercel.app/)
- [Backend](https://psa-squad7-tribub.onrender.com)

> [!IMPORTANT]
> Dado que la API esta hosteada en un servidor gratuito, la primera vez que se realice una petición a la API, esta tardará en responder debido a que el servidor se encuentra en estado de suspensión.

## Documentación de la API

La documentación se puede encontrar en la demostración del proyecto en la siguiente URL [Swagger Doc](https://psa-squad7-tribub.onrender.com/webjars/swagger-ui/index.html).

## Ejecución del proyecto (docker)

- Clonar el repositorio
- Ejecutar los servicios usando docker con el siguiente comando:

```bash
docker compose up -d --build
```

## Backend

### Pre-requisitos

- [Java 17](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)
- [Maven](https://maven.apache.org/download.cgi)
- [PostgreSQL](https://www.postgresql.org/download/)
- [Docker](https://www.docker.com/products/docker-desktop)

### Uso (docker)

- Ejecutar el siguiente comando en la carpeta `Backend`:

```bash
docker compose -f 'compose.yml' up -d --build 'backend'
```

### Uso (local)

- Crear una base de datos en PostgreSQL con el nombre `finanzas`
- Instalar las dependencias del proyecto ejecutando el siguiente comando en la carpeta `Backend`:

```bash
mvn install
```

- Correr spring boot con el siguiente comando:

```bash
mvn spring-boot:run
```

### Uso (test)

- Ejecutar los tests con el siguiente comando:

```bash
mvn test
```

### Stack

- [![Spring boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot) - Spring Boot.
- [![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/) - PostgreSQL.
- [![Cucumber](https://img.shields.io/badge/Cucumber-43B02A?style=for-the-badge&logo=cucumber&logoColor=white)](https://cucumber.io/) - Cucumber.
- [![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=Swagger&logoColor=white)](https://swagger.io/) - Swagger.
- [![Junit5](https://img.shields.io/badge/Junit5-25A162?style=for-the-badge&logo=junit5&logoColor=white)](https://junit.org/junit5/) - Junit5.

## Frontend

### Pre-requisitos

- [Node.js](https://nodejs.org/en/download/)
- [PNPM](https://pnpm.io/installation)

### Uso (docker)

- Ejecutar el siguiente comando en la carpeta `Frontend`:

```bash
docker compose -f 'compose.yml' up -d --build 'frontend'
```

### Uso (local)

- Instalar las dependencias del proyecto ejecutando el siguiente comando en la carpeta `Frontend`:

```bash
pnpm install
```

- Correr el proyecto con el siguiente comando:

```bash
pnpm run dev
```

### Stack

- [![React](https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB)](https://reactjs.org/) - React.
- [![TypeScript](https://img.shields.io/badge/TypeScript-007ACC?style=for-the-badge&logo=typescript&logoColor=white)](https://www.typescriptlang.org/) - TypeScript.
- [![Next.js](https://img.shields.io/badge/Next.js-000000?style=for-the-badge&logo=next.js&logoColor=white)](https://nextjs.org/) - Next.js.
- [![Tailwind CSS](   https://img.shields.io/badge/Tailwind_CSS-38B2AC?style=for-the-badge&logo=tailwind-css&logoColor=white)](https://tailwindcss.com/) - Tailwind CSS.
- [![Zod](https://img.shields.io/badge/Zod-000000?style=for-the-badge&logo=zod&logoColor=3068B7)](https://zod.dev/) - Zod.

## Licencia

Este proyecto está bajo la licencia MIT. Ver el archivo [LICENSE](LICENSE) para más detalles.
