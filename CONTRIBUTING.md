# Contributing to Kungnection Backend

Thank you for your interest in contributing to Kungnection! This document provides guidelines and instructions for contributing.

## Getting Started

### Prerequisites

- Java 17 or higher
- Docker (for MySQL container)
- Gradle (or use the included wrapper)

### Setting Up the Development Environment

1. Clone the repository:
   ```bash
   git clone https://github.com/Seanachan/Kungnection.git
   cd Kungnection
   ```

2. Start the MySQL database:
   ```bash
   ./gradlew startMysql
   ```

3. Run the application:
   ```bash
   ./gradlew bootRun
   ```

4. Run tests:
   ```bash
   ./gradlew test
   ```

## Code Style Guidelines

### Java Code Style

- Use 4 spaces for indentation (no tabs)
- Follow standard Java naming conventions:
  - Classes: `PascalCase`
  - Methods and variables: `camelCase`
  - Constants: `UPPER_SNAKE_CASE`
- Write meaningful variable and method names
- Keep methods focused and reasonably sized

### Documentation

- Add JavaDoc comments to all public classes and methods
- Include `@param`, `@return`, and `@throws` tags where applicable
- Write comments in English

### Logging

- Use SLF4J for logging (already configured)
- Use appropriate log levels:
  - `DEBUG`: Detailed information for debugging
  - `INFO`: General operational information
  - `WARN`: Warning messages
  - `ERROR`: Error conditions

## Pull Request Process

1. Create a feature branch from `main`:
   ```bash
   git checkout -b feature/your-feature-name
   ```

2. Make your changes following the code style guidelines

3. Ensure all tests pass:
   ```bash
   ./gradlew test
   ```

4. Commit your changes with clear, descriptive messages:
   ```bash
   git commit -m "feat: add new feature description"
   ```

5. Push to your branch and create a Pull Request

### Commit Message Format

Use conventional commits format:
- `feat:` - New features
- `fix:` - Bug fixes
- `docs:` - Documentation changes
- `refactor:` - Code refactoring
- `test:` - Adding or updating tests
- `chore:` - Maintenance tasks

## Reporting Issues

When reporting issues, please include:
- A clear description of the problem
- Steps to reproduce the issue
- Expected vs actual behavior
- Environment details (OS, Java version, etc.)

## Questions?

Feel free to open an issue for any questions about contributing.
