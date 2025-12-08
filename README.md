# Pixel & Beans

<div align="center">
  <img src="src/main/resources/images/logo.png" alt="Logo" width="200" height="250">
</div>

_A java sample application to learn how to use the Swing library._

The project follows the instructions from the Object-Oriented Programming DUOC
course at this
[repository](https://github.com/cmartinezs/pixel-and-bean/tree/master).

---

## DB setup

This project uses Docker compose to serve the db. Just run:

```bash
docker compose up -d
```

Then, open `http://localhost:8080` on your browser to access the DB.

Run this query:

```sql
CREATE DATABASE pixelandbeans
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

GRANT ALL PRIVILEGES ON pixelandbeans.* TO 'appuser'@'%';
FLUSH PRIVILEGES;
```

And then proceed to execute the provided sql scripts on the sql directory agains
the `pixelandbeans` database.
