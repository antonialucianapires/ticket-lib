# Omni Ticket Library

Omni Ticket Library is a generic Java library for managing ticket reservations, designed to be flexible and reusable in a variety of applications such as cinema, bus, events, and more. It provides a robust framework for handling booking logic, cancellation, ticket validation and more.

## Topics
- [Usage](#usage)
- [Technologies](#technologies)
- [Main Components](#main-components)
- [Contributing](#contributing)
- [License](#license)


## Usage
To use Omni Ticket Library in your project, you need to include it as a dependency in your build configuration.

### Maven
Add the following dependency to your `pom.xml` file:
```
<dependency>
    <groupId>io.github.antonialucianapires</groupId>
    <artifactId>ticket-lib</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Technologies
Omni Ticket Library is built with the following technologies:

- Java 17: The core programming language used.
- Maven: Dependency management and build tool.
- JUnit 5: For unit testing.
- Mockito: For mocking objects in tests.
- Lombok: To reduce boilerplate code such as getters, setters, and constructors.
- JaCoCo: For code coverage analysis.


## Main Components

### User
Represents a user with an ID, name and email address. Ensures that the email provided is in a valid format.

### Session
Represents a scheduled session that occurs at a specific location and within a specified time period. Each session is unique and identified by a session ID.

### Location
Represents a generic location with capabilities to be described through basic properties such as name, address, capacity and specific resources.

### LocationSeat
Represents a seat within a specific location, providing details such as seat identification, description, availability and associated location.

### ReservationStatus
Represents the status of a reservation, including default and custom status.

### Reservation
Represents a reservation for a session, detailing the user, session, reserved seats and the status of the reservation. This class manages the expiration and cancellation logic for a reservation.

### Ticket
Represents a ticket for a specific session and seat within an event, show or service. Encapsulates details about the ticket holder, associated seat, session, reservation, price and ticket validity.

## Contributing
Contributions are welcome! Please follow the contribution guidelines to submit pull requests or report issues.

## License
This library is distributed under the MIT license. See the LICENSE file for details.