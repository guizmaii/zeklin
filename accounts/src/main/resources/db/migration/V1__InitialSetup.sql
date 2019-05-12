CREATE TABLE ACCOUNTS (
  id uuid default random_uuid() PRIMARY KEY,
  first_name VARCHAR NOT NULL,
  last_name VARCHAR NOT NULL,
  email VARCHAR NOT NULL
);