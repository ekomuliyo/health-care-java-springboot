# Healthcare Management API

This project is a Healthcare Management API built using Spring Boot and Java, with PostgreSQL as the database. It integrates with the SATU SEHAT platform to manage healthcare medications.

## Features

- **Integration with SATU SEHAT**: This project integrates with the SATU SEHAT platform. For more details, visit the [SATU SEHAT office website](https://satusehat.kemkes.go.id/platform/docs/id/playbook/).
  
  ![SATU SEHAT Logo](https://satusehat.kemkes.go.id/_ipx/w_240/images/landing/logo-satusehat.png)

- **Spring Boot and Java**: The application is developed using Spring Boot, a popular Java framework for building web applications.

- **PostgreSQL Database**: The project uses PostgreSQL as its database to store and manage data efficiently.

- **Deployment on Railway**: The application is deployed on Railway. You can access the live version of the API [here](https://health-care-java-springboot-production.up.railway.app/swagger-ui/index.html).

- **Medication Module**: The project includes a module for managing medications, allowing for CRUD operations and integration with external systems.

## Getting Started

To get a local copy up and running, follow these simple steps.

### Prerequisites

- Java 17
- PostgreSQL
- Maven

### Installation

1. Clone the repo
   ```sh
   git clone https://github.com/ekomuliyo/health-care-java-springboot.git
   ```
2. Install Maven dependencies
   ```sh
   mvn install
   ```
3. Configure your PostgreSQL database in `application.properties`
4. Run the application
   ```sh
   mvn spring-boot:run
   ```

## Usage

Once the application is running, you can access the API documentation via Swagger UI at `/swagger-ui/index.html`.

## License

Distributed under the MIT License. See `LICENSE` for more information.

## Contact

Eko Muliyo - [ekomuliyo@gmail.com](mailto:ekomuliyo@gmail.com)

Project Link: [https://github.com/ekomuliyo/health-care-java-springboot](https://github.com/ekomuliyo/health-care-java-springboot)
