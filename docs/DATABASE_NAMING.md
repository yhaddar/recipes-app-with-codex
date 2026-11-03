# Recipe Manager Database Naming Conventions

These conventions apply to PostgreSQL objects introduced by Version 2. They
keep SQL, JPA mappings, migrations, and API-facing repository code predictable.

## Identifiers

- Use lowercase `snake_case` for schemas, tables, columns, indexes, constraints,
  and sequences.
- Use plural table names for collections, such as `users`, `recipes`, and
  `blog_posts`.
- Use singular Java entity class names mapped explicitly to the plural table.
- Keep the application schema as PostgreSQL `public`; no additional schema is
  introduced without an approved architectural decision.

## Keys and relationships

- Use UUID primary keys for application-owned records.
- Name primary keys `pk_<table>`.
- Name foreign keys `fk_<table>_<referenced_table>`; if a table has multiple
  references to the same table, add the local column name.
- Use `<singular_table>_id` for foreign-key columns.
- Use join tables named `<left_table>_<right_table>` in alphabetical order when
  the relationship is many-to-many.

## Constraints and indexes

- Name unique constraints `uk_<table>_<column>`.
- Name check constraints `ck_<table>_<rule>`.
- Name indexes `ix_<table>_<column>`; composite indexes list columns in query
  order.
- Prefer database constraints for required fields, uniqueness, ownership
  relationships, and finite status values.

## Timestamps and status

- Use `created_at` and `updated_at` as `timestamp with time zone` columns for
  mutable application records.
- Store persisted instants in UTC. Morocco's UTC-day handling is an application
  query concern and must not be encoded by changing database timezone semantics.
- Use explicit `status` columns for lifecycle state and constrain values in the
  migration or map them to PostgreSQL enums only when the state is stable.

## Migration rules

- Flyway migration filenames use ordered versions, for example
  `V1__create_users.sql`.
- Applied migrations are immutable. Corrections require a new migration.
- Each migration is idempotent in its intended application order, but must not
  use broad destructive operations to recover from an error.
